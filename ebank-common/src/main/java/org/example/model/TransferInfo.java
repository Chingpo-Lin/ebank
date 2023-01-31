package org.example.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferInfo {

    private Long from;

    private Long to;

    private BigDecimal amount;

    private String currency;

    private Long transactionId;
}
