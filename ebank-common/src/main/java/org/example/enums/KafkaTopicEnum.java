package org.example.enums;

public enum KafkaTopicEnum {

    BANK_TRANSACTION_PAGE_TOPIC("ebank-transaction-topic"),
    BANK_TRANSACTION_TRANSFER_TOPIC("ebank-transfer-topic");

    private String name;

    private KafkaTopicEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
