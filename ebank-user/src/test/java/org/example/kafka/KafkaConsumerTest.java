package org.example.kafka;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.junit.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

public class KafkaConsumerTest {

    public static Properties getProperties(){
        Properties props = new Properties();
        // broker server
        props.put("bootstrap.servers", "127.0.0.1:9092");

        // consumer group id, consumer in the same group can only consume one msg once
        // consumer in different group can repeatedly consume a same msg
        props.put("group.id", "ebank-g2");

        // default is latest, if need consume from earliest, need change to earliest
        // and consumer group change, and it will work
        props.put("auto.offset.reset", "earliest");

        // open auto commit offset
//        props.put("enable.auto.commit", "true");
        props.put("enable.auto.commit", "false");

        // auto commit offset delay time
        props.put("auto.commit.interval.ms", "1000");

        /**
         * deserializer
         */
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        return props;
    }

    @Test
    public void simpleConsumerTest() {
        Properties properties = getProperties();

        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);

        // subscribe title
        kafkaConsumer.subscribe(Arrays.asList(KafkaProducerTest.TOPIC_NAME));

        while (true) {
            // get time, block time
            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(100));

            for (ConsumerRecord record: records) {
                System.err.printf("topic=%s, offset=%d, key=%s, value=%s %n",
                        record.topic(), record.offset(), record.key(), record.value());
            }

            // Synchronized submit offset
//            kafkaConsumer.commitSync();

            if (!records.isEmpty()) {
                // asynchronously submit offset
                kafkaConsumer.commitAsync(new OffsetCommitCallback() {
                    @Override
                    public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception e) {
                        if (e == null) {
                            System.err.println("manually commit offset successfully:" + offsets);
                        } else {
                            System.err.println("manually commit offset fail:" + offsets);
                        }
                    }
                });
            }
        }
    }
}
