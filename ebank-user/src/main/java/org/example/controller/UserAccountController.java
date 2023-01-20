package org.example.controller;


import org.example.model.TransferInfo;
import org.example.request.CreateAccountRequest;
import org.example.service.UserAccountService;
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
 * @since 2023-01-19
 */
@RestController
@RequestMapping("/s8/user_account/v1/")
public class UserAccountController {

    @Autowired
    private UserAccountService userAccountService;

    /**
     * create account for current user
     * @param createAccountRequest
     * @return
     */
    @PostMapping("create_account")
    public JsonData createAccount(@RequestBody CreateAccountRequest createAccountRequest) {
        JsonData jsonData = userAccountService.createAccount(createAccountRequest);
        return jsonData;
    }


    /**
     * process the transfer
     * @param transferInfo
     * @return
     */
    @PostMapping("transfer")
    public JsonData transferPrepare(@RequestBody TransferInfo transferInfo) {
        JsonData jsonData = userAccountService.transfer(transferInfo);
        return jsonData;
    }
}

