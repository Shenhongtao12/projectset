package com.sht.shoesboot.config;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Aaron
 * @date 2020/11/26 21:04
 */
//@Configuration
public class KafkaInitialConfiguration {

    /**
     * 创建TopicName为topic.quick.initial的Topic并设置分区数为8以及副本数为1
     * @return
     */
    @Bean
    public NewTopic initialTopic() {
        return new NewTopic("topic.quick.initial",8, (short) 1 );
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> props = new HashMap<>(1);
        //配置Kafka实例的连接地址
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "47.98.128.88:9092");
        KafkaAdmin admin = new KafkaAdmin(props);
        return admin;
    }

    @Bean
    public AdminClient adminClient() {
        return AdminClient.create(kafkaAdmin().getConfig());
    }
}
