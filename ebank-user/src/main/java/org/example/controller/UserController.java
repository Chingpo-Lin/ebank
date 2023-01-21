package org.example.controller;


import io.swagger.annotations.Api;
import org.example.enums.BizCodeEnum;
import org.example.exception.BizException;
import org.example.model.TransferInfo;
import org.example.request.UserLoginRequest;
import org.example.request.UserRegisterRequest;
import org.example.service.UserAccountService;
import org.example.service.UserService;
import org.example.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Bob
 * @since 2023-01-16
 */
@Api("User Module")
@RestController
@RequestMapping("/s8/user/v1/")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * user register
     * @param registerRequest
     * @return
     */
    @PostMapping("register")
    public JsonData register(@RequestBody UserRegisterRequest registerRequest) {
        JsonData jsonData = userService.register(registerRequest);
        return jsonData;
    }

    /**
     * user login
     * @return
     */
    @PostMapping("login")
    public JsonData login(@RequestBody UserLoginRequest userLoginRequest) {
        JsonData jsonData = userService.login(userLoginRequest);
        return jsonData;
    }

    @GetMapping("get_name/{user_id}")
    public JsonData getUserName(@PathVariable("user_id") long userId) {
        String name = userService.getById(userId).getName();
        if (name != null) {
            return JsonData.buildSuccess(name);
        } else {
            throw new BizException(BizCodeEnum.ACCOUNT_NOT_EXIST);
        }
    }
}

