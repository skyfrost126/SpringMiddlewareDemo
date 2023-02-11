package com.skyfrost.comsumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MessageComsumer {

    /**
     * 消费者从 test topic中消费消息
     * group 默认使用配置文件的
     * @param consumerRecord
     */
    @KafkaListener(topics = {"test"})
    public void onMessage1(ConsumerRecord<?, ?> consumerRecord) {
        Optional<?> optional = Optional.ofNullable(consumerRecord.value());
        if (optional.isPresent()) {
            Object msg = optional.get();
        }
    }

    /**
     * 指定 0 号分区消费消息
     * @param consumerRecord
     */
    @KafkaListener(topics = {"test"},topicPattern = "0")
    public void onMessage(ConsumerRecord<?, ?> consumerRecord) {
        Optional<?> optional = Optional.ofNullable(consumerRecord.value());
        if (optional.isPresent()) {
            Object msg = optional.get();
        }
    }
}
