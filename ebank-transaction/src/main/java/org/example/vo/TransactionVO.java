package org.example.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TransactionVO {

    /**
     * from user name
     */
    private String senderName;

    /**
     * receive name
     */
    private String receiverName;

    /**
     * currency
     */
    private String currency;

    /**
     * amount
     */
    private BigDecimal amount;

    /**
     * transaction date
     */
    @JsonProperty("value_date")
    private Date valueDate;
}
