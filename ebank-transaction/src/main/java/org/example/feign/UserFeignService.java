package org.example.feign;

import org.example.model.TransferInfo;
import org.example.utils.JsonData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "ebank-user-service")
public interface UserFeignService {

    /**
     * transfer process
     * @param transferInfo
     * @return
     */
    @PostMapping("/s8/user_account/v1/transfer")
    JsonData transfer(@RequestBody TransferInfo transferInfo);

    @GetMapping("s8/user/v1/get_name/{user_id}")
    JsonData getUserName(@PathVariable("user_id") long userId);
}