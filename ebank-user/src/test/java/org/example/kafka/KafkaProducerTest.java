package org.example.kafka;

import org.apache.kafka.clients.producer.*;
import org.example.UserApplication;
import org.junit.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.common.record.Record;
import org.junit.jupiter.params.shadow.com.univocity.parsers.common.record.RecordMetaData;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class KafkaProducerTest {

    public static final String TOPIC_NAME = "ebank-v1-topic-test";

    public static Properties getProperties(){
        Properties props = new Properties();
        props.put("bootstrap.servers", "127.0.0.1:9092");
        //props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"112.74.55.160:9092");

        // when producer send data to leader, can set reliability lever with 0,1, or all to acks
        props.put("acks", "all");
        //props.put(ProducerConfig.ACKS_CONFIG, "all");

        // set retry times to 0 to avoid sending repeated msg if request fail.
        props.put("retries", 0);
        //props.put(ProducerConfig.RETRIES_CONFIG, 0);

        // batch size will put unsent msg in cache, default is 16kb
        props.put("batch.size", 16384);

        /**
         * default is 0, send msg immediately, even batch size is not filled
         * to reduce request amount, set linger.ms > 0
         * msg will be sent if either batch is filled or linger.ms reach
         */
        props.put("linger.ms", 1);

        /**
         * buffer.memory is to limit the cache size producer can use, default is 32mb
         * if too small, will cause msg saved in cache too fast and sender has limited time
         * to send msg to kafka. if too large, will block user thread to write msg.
         */
        props.put("buffer.memory", 33554432);

        /**
         * key serializer is to serialize key and value object ProducerRecord provided by user
         * key serializer must be set
         * even there is no specific key in msg, serializer must be a real
         * org.apache.kafka.common.serialization class, and turn key into byte array
         */
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
        return props;
    }

    /**
     * send msg
     */
    @Test
    public void testSend() {
        Properties properties = getProperties();

        Producer<String, String> producer = new KafkaProducer<>(properties);

        for (int i = 0; i < 3; i++) {
            Future<RecordMetadata> future = producer.send(new ProducerRecord<>(TOPIC_NAME, "ebank--uncommit---key" + i, "ebank-value" + i));

            try {
                // if don't care result,   can ignore this line
                RecordMetadata recordMetadata = future.get();
                System.out.println("sending status: " + recordMetadata.toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        producer.close();
    }

    /**
     * send with callback
     */
    @Test
    public void testSendWithCallback() {
        Properties properties = getProperties();

        Producer<String, String> producer = new KafkaProducer<>(properties);

        for (int i = 0; i < 3; i++) {
            producer.send(new ProducerRecord<>(TOPIC_NAME, "ebank-key" + i, "ebank-value" + i), new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if (e == null) {
                        System.err.println("send status: " + recordMetadata);
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        }
        producer.close();
    }

    // ebank-v1-topic-test
    /**
     * send with callback and partition
     *
     * ordered msg
     */
    @Test
    public void testSendWithCallbackAndPartition() {
        Properties properties = getProperties();

        Producer<String, String> producer = new KafkaProducer<>(properties);

        for (int i = 0; i < 10; i++) {
            producer.send(new ProducerRecord<>(TOPIC_NAME, 4, "ebank-key" + i, "ebank-value" + i), new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if (e == null) {
                        System.err.println("send status: " + recordMetadata);
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        }
        producer.close();
    }

    /**
     * send with callback and partition
     *
     * ordered msg
     */
    @Test
    public void testSendWithPartitionStrategy() {
        Properties properties = getProperties();

        properties.put("partitioner.class", "org.example.config.EbankKafkaPartitioner");

        Producer<String, String> producer = new KafkaProducer<>(properties);

        for (int i = 0; i < 10; i++) {
            producer.send(new ProducerRecord<>(TOPIC_NAME, "ebank","ebank-v1-value" + i), new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if (e == null) {
                        System.err.println("send status: " + recordMetadata);
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        }
        producer.close();
    }
}
