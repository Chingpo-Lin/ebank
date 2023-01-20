package org.example.service;

import org.example.model.TransferInfo;
import org.example.model.UserAccountDO;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.request.CreateAccountRequest;
import org.example.utils.JsonData;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Bob
 * @since 2023-01-19
 */
public interface UserAccountService extends IService<UserAccountDO> {
    JsonData transfer(TransferInfo transferInfo);

    JsonData createAccount(CreateAccountRequest createAccountRequest);
}
