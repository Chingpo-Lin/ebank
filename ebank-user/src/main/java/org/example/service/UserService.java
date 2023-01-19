package org.example.service;

import org.example.model.UserDO;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.request.UserLoginRequest;
import org.example.request.UserRegisterRequest;
import org.example.utils.JsonData;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Bob
 * @since 2023-01-16
 */
public interface UserService extends IService<UserDO> {

    JsonData register(UserRegisterRequest registerRequest);

    JsonData login(UserLoginRequest userLoginRequest);
}
