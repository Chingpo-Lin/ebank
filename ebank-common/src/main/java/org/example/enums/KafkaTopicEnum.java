package org.example.enums;

public enum KafkaTopicEnum {

    BANK_TRANSACTION_TOPIC("ebank-transaction-topic");

    private String name;

    private KafkaTopicEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
