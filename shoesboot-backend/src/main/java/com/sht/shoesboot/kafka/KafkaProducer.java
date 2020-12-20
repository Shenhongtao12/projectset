package com.sht.shoesboot.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Aaron
 * @date 2020/11/26 20:04
 */
@Component
public class KafkaProducer {

    private KafkaTemplate<String, Object> kafkaTemplate;
}
