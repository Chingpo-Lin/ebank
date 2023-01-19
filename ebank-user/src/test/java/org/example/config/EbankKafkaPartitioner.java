package org.example.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.utils.Utils;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Slf4j
public class EbankKafkaPartitioner implements Partitioner {

    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {

        log.info("enter partition strategy");

        if (keyBytes == null || keyBytes.length == 0) {
            throw new IllegalArgumentException("key cannot be empty");
        }

        if ("ebank".equals(key)) {
            return 0;
        }

        List<PartitionInfo> partitionerList = cluster.partitionsForTopic(topic);
        int numPartitions = partitionerList.size();

        // hash the key bytes to choose a partition
        return Utils.toPositive(Utils.murmur2(keyBytes) % numPartitions);
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
