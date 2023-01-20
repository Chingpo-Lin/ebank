package org.example.kafka;

import org.apache.kafka.clients.admin.*;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.ExecutionException;

public class KafkaAdminTest {

    private static final String TOPIC_NAME = "ebank-v1-topic-test";

    /**
     * config admin client
     * @return
     */
    public static AdminClient initAdminClient(){
        Properties properties = new Properties();
        properties.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,"127.0.0.1:9092");
        AdminClient adminClient = AdminClient.create(properties);

        return adminClient;
    }

    /**
     * create topic test
     */
    @Test
    public void createTopicTest() {
        AdminClient adminClient = initAdminClient();

        // partition num & replication num
        NewTopic newTopic = new NewTopic(TOPIC_NAME, 5, (short) 1);

        CreateTopicsResult createTopicsResult = adminClient.createTopics(Arrays.asList(newTopic));
        try {
            // future wait for create, will not cause exception if success
            createTopicsResult.all().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * list topic test
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void listTopicTest() throws ExecutionException, InterruptedException {
        AdminClient adminClient = initAdminClient();

        ListTopicsOptions options = new ListTopicsOptions();
        // see internal topic
        options.listInternal(true);

        ListTopicsResult listTopicsResult = adminClient.listTopics(options);
        Set<String> topics = listTopicsResult.names().get();

        for (String name: topics) {
            System.err.println(name);
        }
    }

    /**
     * delete topic test
     */
    @Test
    public void delTopicTest() throws ExecutionException, InterruptedException {
        AdminClient adminClient = initAdminClient();

        DeleteTopicsResult deleteTopicsResult = adminClient.deleteTopics(Arrays.asList("ebank-topic-test"));
        deleteTopicsResult.all().get();
    }

    /**
     * check topic detail
     */
    @Test
    public void detailTopicTest() throws ExecutionException, InterruptedException {
        AdminClient adminClient = initAdminClient();

        DescribeTopicsResult describeTopicsResult = adminClient.describeTopics(Arrays.asList(TOPIC_NAME));
        Map<String, TopicDescription> stringTopicDescriptionMap = describeTopicsResult.all().get();
        Set<Map.Entry<String, TopicDescription>> entries = stringTopicDescriptionMap.entrySet();

        entries.stream().forEach((entry)->
                System.out.println("name: " + entry.getKey() + " , desc:"+ entry.getValue()));
    }

    /**
     * add partitions
     */
    @Test
    public void increasePartitionTopicTest() throws ExecutionException, InterruptedException {
        Map<String, NewPartitions> infoMap = new HashMap<>(1);
        AdminClient adminClient = initAdminClient();

        NewPartitions newPartitions = NewPartitions.increaseTo(5);

        infoMap.put(TOPIC_NAME, newPartitions);

        CreatePartitionsResult createPartitionsResult = adminClient.createPartitions(infoMap);
        createPartitionsResult.all().get();
    }
}
