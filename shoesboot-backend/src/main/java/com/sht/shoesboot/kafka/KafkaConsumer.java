package com.sht.shoesboot.kafka;

import com.sht.shoesboot.service.GoodsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author Aaron
 * @date 2020/11/26 20:19
 */
@Component
public class KafkaConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

    @Autowired
    private GoodsService goodsService;

    /**
     * 声明consumerID为demo，监听topicName为topic.quick.demo的Topic
     * @param msgData
     */
    @KafkaListener(id = "shoes", topics = "topic.quick.shoes")
    public void listen(String msgData) {
        LOGGER.info("shoes receive : " + msgData);
    }
}
