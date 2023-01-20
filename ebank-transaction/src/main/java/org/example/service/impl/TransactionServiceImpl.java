package org.example.service.impl;

import org.example.enums.BizCodeEnum;
import org.example.exception.BizException;
import org.example.interceptor.LoginInterceptor;
import org.example.model.LoginUser;
import org.example.model.TransactionDO;
import org.example.mapper.TransactionMapper;
import org.example.request.TransferRequest;
import org.example.service.TransactionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.utils.JsonData;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 *  service implementation
 * </p>
 *
 * @author Bob
 * @since 2023-01-16
 */
@Service
public class TransactionServiceImpl implements TransactionService {

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
     * 6. send transactionVO info into Kafka
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



        return null;
    }
}
