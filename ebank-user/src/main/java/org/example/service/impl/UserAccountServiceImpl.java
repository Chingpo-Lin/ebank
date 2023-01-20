package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.catalina.User;
import org.example.enums.BizCodeEnum;
import org.example.exception.BizException;
import org.example.interceptor.LoginInterceptor;
import org.example.model.LoginUser;
import org.example.model.TransferInfo;
import org.example.model.UserAccountDO;
import org.example.mapper.UserAccountMapper;
import org.example.model.UserDO;
import org.example.request.CreateAccountRequest;
import org.example.service.UserAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Bob
 * @since 2023-01-19
 */
@Service
public class UserAccountServiceImpl extends ServiceImpl<UserAccountMapper, UserAccountDO> implements UserAccountService {

    @Autowired
    private UserAccountMapper userAccountMapper;

    /**
     * transfer
     *
     * ------ feign call to user service:
     *
     * 2. check if both send & receive user own that currency account
     * 3. minus balance from send user
     * - because I use a sql to complete the process,
     * - it will only update when balance >= send amount
     * 4. add balance to receive user
     * - only execute when third step success
     *
     * ------ go back to transaction service:
     *
     * @param transferInfo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public JsonData transfer(TransferInfo transferInfo) {

        String currency = transferInfo.getCurrency();

        // check if sender own that currency account
        UserAccountDO senderAccountDO = userAccountMapper.selectOne(new QueryWrapper<UserAccountDO>()
                .eq("user_id", transferInfo.getFrom())
                .eq("currency", currency));

        if (senderAccountDO == null) {
            throw new BizException(BizCodeEnum.ACCOUNT_CURRENCY_SENDER_NOT_EXIST);
        }

        // check if receiver own that currency account
        UserAccountDO receiverAccountDO = userAccountMapper.selectOne(new QueryWrapper<UserAccountDO>()
                .eq("user_id", transferInfo.getTo())
                .eq("currency", currency));

        if (receiverAccountDO == null) {
            throw new BizException(BizCodeEnum.ACCOUNT_CURRENCY_RECEIVER_NOT_EXIST);
        }

        BigDecimal amount = transferInfo.getAmount();

        // since sql check balance, and then update it in atomic action, so
        // if will not has error in multi-thread
        int rows = userAccountMapper.updateAccount(amount, senderAccountDO.getId());

        // if bug happen in here, if will rollback, and sender account will not lost money

        if (rows == 1) {
            receiverAccountDO.setBalance(receiverAccountDO.getBalance().add(amount));
            userAccountMapper.updateById(receiverAccountDO);
        } else {
            throw new BizException(BizCodeEnum.BALANCE_NOT_ENOUGH);
        }

        return JsonData.buildSuccess();
    }

    /**
     * create account
     * @param createAccountRequest
     * @return
     */
    @Override
    public JsonData createAccount(CreateAccountRequest createAccountRequest) {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        Long userId = loginUser.getId();

        UserAccountDO userAccountDO = userAccountMapper.selectOne(new QueryWrapper<UserAccountDO>()
                .eq("user_id", userId)
                .eq("currency", createAccountRequest.getCurrency()));

        if (userAccountDO != null) {
            throw new BizException(BizCodeEnum.ACCOUNT_CURRENCY_EXIST);
        }

        userAccountDO = new UserAccountDO();
        userAccountDO.setUserId(userId);
        userAccountDO.setCurrency(createAccountRequest.getCurrency());
        userAccountDO.setBalance(new BigDecimal(0));

        userAccountMapper.insert(userAccountDO);

        return JsonData.buildSuccess();
    }
}
