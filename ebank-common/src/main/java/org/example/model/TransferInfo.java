package org.example.model;

import lombok.Data;

@Data
public class TransferInfo {

    private Long from;

    private Long to;

    private Long amount;

    private String currency;
}
