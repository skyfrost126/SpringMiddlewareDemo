package com.skyfrost.listener;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.ProducerListener;

import javax.annotation.PostConstruct;

/**
 *  异步消息监听
 */
@Configuration
public class MessageListener {

    private final static Logger logger = LoggerFactory.getLogger(MessageListener.class);


    @Autowired
    KafkaTemplate kafkaTemplate;

    //配置监听
    @PostConstruct
    private void listener() {
        kafkaTemplate.setProducerListener(new ProducerListener<String, Object>() {
            @Override
            public void onSuccess(ProducerRecord<String, Object> producerRecord, RecordMetadata recordMetadata) {
                logger.info("ok,message={}", producerRecord.value());
            }

            @Override
            public void onError(ProducerRecord<String, Object> producerRecord, Exception exception) {
                logger.error("error!message={}", producerRecord.value());
            }
        });
    }
}
