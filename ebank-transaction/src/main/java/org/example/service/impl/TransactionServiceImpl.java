package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.example.enums.BizCodeEnum;
import org.example.enums.KafkaTopicEnum;
import org.example.exception.BizException;
import org.example.feign.UserFeignService;
import org.example.interceptor.LoginInterceptor;
import org.example.model.LoginUser;
import org.example.model.TransactionDO;
import org.example.mapper.TransactionMapper;
import org.example.model.TransferInfo;
import org.example.request.TransferRequest;
import org.example.service.TransactionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.utils.JsonData;
import org.example.vo.TransactionVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  service implementation
 * </p>
 *
 * @author Bob
 * @since 2023-01-16
 */
@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private UserFeignService userFeignService;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * transfer
     *
     * 1. create and set transactionDO object and check if send user has a same id as receive user
     *
     * ------ feign call to user service:
     *
     * 2. check if both user & receive user own that currency account
     * 3. minus balance from send user
     * - because I use one sql to complete the process,
     * - it will only update when balance >= send amount
     * 4. add balance to receive user
     * - only execute when third step success
     *
     * ------ go back to transaction service:
     *
     * 5. check feign call status and save transactionDO object into db
     *
     * @param transferRequest
     * @return
     */
    @Override
    public JsonData transfer(TransferRequest transferRequest) {

        LoginUser loginUser = LoginInterceptor.threadLocal.get();

        TransactionDO transactionDO = new TransactionDO();

        // set from transfer request
        transactionDO.setCurrency(transferRequest.getCurrency());
        transactionDO.setAmount(transferRequest.getAmount());
        transactionDO.setAccountIban(transferRequest.getAccountIban());
        transactionDO.setDescription(transferRequest.getDescription());
        transactionDO.setTo(transferRequest.getTo());

        // set from current info
        transactionDO.setValueDate(new Date());
        transactionDO.setFrom(loginUser.getId());

        if (transactionDO.getFrom().equals(transactionDO.getTo())) {
            throw new BizException(BizCodeEnum.TRANSFER_SAME_USER);
        }

        TransferInfo transferInfo = new TransferInfo();
        transferInfo.setAmount(transferRequest.getAmount());
        transferInfo.setCurrency(transferRequest.getCurrency());
        transferInfo.setFrom(loginUser.getId());
        transferInfo.setTo(transferRequest.getTo());
        // make feign call
        JsonData jsonData = userFeignService.transfer(transferInfo);

        log.info("jsonData is:{}", jsonData);

        // 5. check feign call status and save transactionDO object into db if success
        if (jsonData.getCode() != 0) {
            throw new BizException(BizCodeEnum.ACCOUNT_TRANSFER_FAIL);
        }

        transactionMapper.insert(transactionDO);

        return jsonData.buildSuccess();
    }

    /**
     * will return the pagination include sender name, receiver name, currency, value date, amount
     * @param page
     * @param size
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Map<String, Object> page(int page, int size, int year, int month) {

        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        Page<TransactionDO> pageInfo = new Page<>(page, size);

        IPage<TransactionDO> transactionDOIPage = null;
        if (year == 0 || month == 0) {
            transactionDOIPage = transactionMapper.selectPage(pageInfo,
                    new QueryWrapper<TransactionDO>().eq("`from`", loginUser.getId()));
        } else {
            String time = "%s-%s-01";
            String start_date = String.format(time, year, month);
            if (month == 12) {
                year++;
                month = 1;
            } else {
                month++;
            }
            String end_date = String.format(time, year, month);
            transactionDOIPage = transactionMapper.selectPage(pageInfo,
                    new LambdaQueryWrapper<TransactionDO>().eq(TransactionDO::getFrom, loginUser.getId())
                            .apply(!StringUtils.isEmpty(start_date),
                                    "date_format (value_date,'%Y-%m') >= date_format('" + start_date + "','%Y-%m')")
                            .apply(!StringUtils.isEmpty(end_date),
                                    "date_format (value_date,'%Y-%m') < date_format('" + end_date + "','%Y-%m')")
                            .orderByDesc(TransactionDO::getValueDate));
        }

        List<TransactionDO> transactionDOList = transactionDOIPage.getRecords();

        List<TransactionVO> transactionVOList = transactionDOList.stream().map(item -> {
            TransactionVO transactionVO = new TransactionVO();
            BeanUtils.copyProperties(item, transactionVO);
            transactionVO.setSenderName(loginUser.getName());
            String receiverName = userFeignService.getUserName(item.getTo()).getData().toString();
            transactionVO.setReceiverName(receiverName);

            // consume by Kafka
            sendKafkaMsg(item);

            return transactionVO;
        }).collect(Collectors.toList());

        Map<String, Object> pageMap = new HashMap<>(3);
        pageMap.put("total_record", transactionDOIPage.getTotal());
        pageMap.put("total_page", transactionDOIPage.getPages());
        pageMap.put("current_data",transactionVOList);
        return pageMap;
    }

    private void sendKafkaMsg(TransactionDO transactionDO) {
        String topic = KafkaTopicEnum.BANK_TRANSACTION_TOPIC.getName();

        kafkaTemplate.send(topic, transactionDO.getId().toString(), transactionDO.toString()).addCallback(success -> {
            String topicName = success.getRecordMetadata().topic();
            int partition = success.getRecordMetadata().partition();
            long offset = success.getRecordMetadata().offset();
            log.info("succussfully send by kafka with topic:{}, " +
                    "partition:{}, offset:{}", topicName, partition, offset);
        }, failure -> {
            log.info("fail to send to kafka:{}", failure.getMessage() );
        });
    }
}
