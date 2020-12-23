package com.sht.shoesboot.kafka;

import com.alibaba.fastjson.JSONObject;
import com.sht.shoesboot.entity.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Aaron
 * @date 2020/11/26 20:04
 */
@Component
public class KafkaProducer {

    @Resource
    private KafkaTemplate<Integer, Object> defaultKafkaTemplate;

    @Autowired
    private KafkaSendResultHandler producerListener;

    public void send(Goods data) {
        defaultKafkaTemplate.setProducerListener(producerListener);
        defaultKafkaTemplate.send("topic.quick.shoes", 0, System.currentTimeMillis(), data.getId(), JSONObject.toJSONString(data));

    }
}
