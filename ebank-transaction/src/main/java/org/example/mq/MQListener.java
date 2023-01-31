package org.example.mq;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.enums.BizCodeEnum;
import org.example.exception.BizException;
import org.example.feign.UserFeignService;
import org.example.model.TransferInfo;
import org.example.service.TransactionService;
import org.example.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;

@Component
@Slf4j
public class MQListener {

    @Autowired
    private UserFeignService userFeignService;

    @Autowired
    private TransactionService transactionService;

    /**
     * consume msg from page
     * @param record
     * @param ack
     * @param topic
     */
    @KafkaListener(topics = {"ebank-transaction-topic"},groupId = "ebank-group1")
    public void onMessage1(ConsumerRecord<?, ?> record, Acknowledgment ack,
                           @Header(KafkaHeaders.RECEIVED_TOPIC) String topic){

        log.info("listen to kafka with topic:{}, in partition:{}, with value:{}",
                topic, record.partition(), record.value());
        ack.acknowledge(); // this will submit msg
    }

    /**
     * handle transfer
     * @param record
     * @param ack
     * @param topic
     */
    @KafkaListener(topics = {"ebank-transfer-topic"},groupId = "ebank-group2")
    public void onMessage2(ConsumerRecord<?, ?> record, Acknowledgment ack,
                           @Header(KafkaHeaders.RECEIVED_TOPIC) String topic){

        log.info("listen to kafka with topic:{}, in partition:{}, with value:{}",
                topic, record.partition(), record.value());
        JSONObject jsonObject = JSON.parseObject(record.value().toString());

        try {
            boolean flag = transactionService.transferConsume(jsonObject);
            if (flag) {
                ack.acknowledge(); // this will submit msg
            } else {
                throw new BizException(BizCodeEnum.ACCOUNT_TRANSFER_FAIL);
            }
        } catch (Exception e) {
            log.error("fail", jsonObject);
        }
    }
}