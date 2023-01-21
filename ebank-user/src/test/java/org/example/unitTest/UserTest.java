package org.example.unitTest;

import lombok.extern.slf4j.Slf4j;
import org.example.UserApplication;
import org.example.request.UserLoginRequest;
import org.example.request.UserRegisterRequest;
import org.example.service.UserService;
import org.example.utils.JsonData;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserApplication.class)
@Slf4j
public class UserTest {

    @Autowired
    private UserService userService;

    @Test
    public void registerTest() {
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setMail("test@gmail.com");
        userRegisterRequest.setName("testBob");
        userRegisterRequest.setSex(0);
        userRegisterRequest.setPwd("test");

        JsonData jsonData = userService.register(userRegisterRequest);
        Assert.assertEquals("0", jsonData.getCode().toString());
    }

    @Test
    public void loginTest() {
        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setMail("test@gmail.com");
        userLoginRequest.setPwd("test");

        JsonData jsonData = userService.login(userLoginRequest);
        Assert.assertEquals("0", jsonData.getCode().toString());
    }
}
