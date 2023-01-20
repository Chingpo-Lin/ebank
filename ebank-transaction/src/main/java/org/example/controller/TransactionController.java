package org.example.controller;


import org.example.request.TransferRequest;
import org.example.service.TransactionService;
import org.example.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.spring.web.json.Json;

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
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("transfer")
    public JsonData transfer(@RequestBody TransferRequest transferRequest) {
        JsonData jsonData = transactionService.transfer(transferRequest);
        return null;
    }

}

