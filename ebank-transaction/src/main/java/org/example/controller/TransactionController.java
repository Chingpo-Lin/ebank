package org.example.controller;


import io.swagger.annotations.Api;
import org.example.request.TransferRequest;
import org.example.service.TransactionService;
import org.example.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import springfox.documentation.spring.web.json.Json;

import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Bob
 * @since 2023-01-16
 */
@Api("Transaction Module")
@RestController
@RequestMapping("/s8/transaction/v1/")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    /**
     * transfer money
     * @param transferRequest
     * @return
     */
    @PostMapping("transfer")
    public JsonData transfer(@RequestBody TransferRequest transferRequest) {
        JsonData jsonData = transactionService.transfer(transferRequest);
        return jsonData;
    }

    /**
     * will return the pagination include sender name, receiver name, currency, value date, amount
     * @param page
     * @param size
     * @return
     */
    @GetMapping("page")
    public JsonData page(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "year", defaultValue = "0") int year,
            @RequestParam(value = "month", defaultValue = "0") int month) {

        Map<String, Object> pageResult = transactionService.page(page, size, year, month);
        return JsonData.buildSuccess(pageResult);
    }
}

