package org.example.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TransferRequest {

    /**
     * currency
     */
    private String currency;

    /**
     * amount
     */
    private BigDecimal amount;

    /**
     * international bank account number
     */
    private Integer accountIban;

    /**
     * transfer description
     */
    private String description;

    /**
     * receiver user id
     */
    private Long to;

}
