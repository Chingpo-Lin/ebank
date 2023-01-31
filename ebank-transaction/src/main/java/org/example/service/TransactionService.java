package org.example.service;

import com.alibaba.fastjson.JSONObject;
import org.example.model.TransactionDO;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.request.TransferRequest;
import org.example.utils.JsonData;

import java.util.Map;

/**
 * <p>
 *  service
 * </p>
 *
 * @author Bob
 * @since 2023-01-16
 */
public interface TransactionService {

    /**
     * transfer money into another acount
     * @param transferRequest
     * @return
     */
    JsonData transfer(TransferRequest transferRequest);

    /**
     * page
     * @param page
     * @param size
     * @param year
     * @param month
     * @return
     */
    Map<String, Object> page(int page, int size, int year, int month);

    /**
     * consume transfer by kafka
     * @param jsonObject
     * @return
     */
    boolean transferConsume(JSONObject jsonObject);
}
