package org.example.controller;


import org.example.request.UserLoginRequest;
import org.example.request.UserRegisterRequest;
import org.example.service.UserService;
import org.example.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Bob
 * @since 2023-01-16
 */
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


}

