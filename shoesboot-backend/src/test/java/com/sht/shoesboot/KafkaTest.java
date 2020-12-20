package com.sht.shoesboot;

import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @author Aaron
 * @date 2020/10/26 20:23
 */
@SpringBootTest
public class KafkaTest {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private AdminClient adminClient;

    @Test
    void testDemo() throws InterruptedException {

        /*String jsonString = JSONObject.toJSONString(new Item(1L, "小米手机7", " 手机",
                "小米", 3499.00, "http://image.leyou.com/13123.jpg"));
        kafkaTemplate.send("topic.quick.demo", jsonString);*/

        //休眠5秒，为了使监听器有足够的时间监听到topic的数据
        //Thread.sleep(5000);
    }

    @Test
    void createTopic() throws InterruptedException {
        //topicName  分区数为8   副本数为1
        NewTopic topic = new NewTopic("topic.quick.shoes", 1, (short) 1);
        adminClient.createTopics(Arrays.asList(topic));
        Thread.sleep(1000);
    }

    @Test
    public void testSelectTopicInfo() throws ExecutionException, InterruptedException {
        DescribeTopicsResult result = adminClient.describeTopics(Arrays.asList("topic.quick.shoes"));
        //lambda表达式
        result.all().get().forEach((k,v)->System.out.println("k: "+k+" ,v: "+v.toString()+"\n"));
    }

    @Test
    public void testTemplateSend() {
        //发送带有时间戳的消息
        kafkaTemplate.send("topic.quick.shoes", 0, System.currentTimeMillis(), 0, "send message with timestamp");

        //使用ProducerRecord发送消息
        ProducerRecord record = new ProducerRecord("topic.quick.shoes", "use ProducerRecord to send message");
        kafkaTemplate.send(record);

        //使用Message发送消息
        Map map = new HashMap();
        map.put(KafkaHeaders.TOPIC, "topic.quick.shoes");
        map.put(KafkaHeaders.PARTITION_ID, 0);
        map.put(KafkaHeaders.MESSAGE_KEY, 0);
        GenericMessage message = new GenericMessage("use Message to send message",new MessageHeaders(map));
        kafkaTemplate.send(message);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testSendBigData() throws InterruptedException {
        for (int i = 77; i < 120; i++) {
            //kafkaTemplate.send("topic.quick.demo", new Item(Integer.toUnsignedLong(i),"title" + i, "category" + i, "brand" + i, 13.5 * i, "http://image/url" + i).toString());
            Thread.sleep(1000);
        }
    }
}
