package org.example.service;

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

    Map<String, Object> page(int page, int size);
}
