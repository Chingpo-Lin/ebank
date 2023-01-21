package org.example.mq;


import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MQListener {
    /**
     * 􏰀􏰁􏷲􏶻
     * @param record

     */
    @KafkaListener(topics = {"ebank-transaction-topic"},groupId = "ebank-group1")
    public void onMessage1(ConsumerRecord<?, ?> record, Acknowledgment ack,
                           @Header(KafkaHeaders.RECEIVED_TOPIC) String topic){

        log.info("listen to kafka with topic:{}, in partition:{}, with value:{}",
                topic, record.partition(), record.value());
        ack.acknowledge(); // this will submit msg
    }
}