package com.skyfrost.producer;

import com.alibaba.fastjson.JSON;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
public class MessageProducer {

    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * 使用kafaka模版发送异步消息
     * @param msg
     */
    public void sendMessage(String msg) {
        Map<String, Object> message = new HashMap<String, Object>();
        message.put("message", "send kafka msg");
        kafkaTemplate.send("test", JSON.toJSONString(message));
    }

    /**
     * 发送同步消息
     * @param msg
     */
    public void sendAsyncMessage(String msg) throws ExecutionException, InterruptedException, TimeoutException {
        Map<String, Object> message = new HashMap<String, Object>();
        message.put("message", "send asyncKafka msg");
        ListenableFuture<SendResult<String, Object>> result = kafkaTemplate.send("test", JSON.toJSONString(message));
        // 设置超时时间，超时后不再等待
        SendResult<String, Object> stringObjectSendResult = result.get(5, TimeUnit.SECONDS);
        // 获取消息发送结果
        Object value = stringObjectSendResult.getProducerRecord().value();
    }

    /**
     * 指定分区发送消息
     * @param msg
     */
    public void sendPartition (String key) {
        kafkaTemplate.send("test", 0, key, "key=" + key + "，msg=指定0号分区");
    }

    /**
     * 不指定分区发送消息
     * @param key
     */
    public void set(String key) {
        kafkaTemplate.send("test", key, "key=" + key + "，msg=不指定分区");
    }
}
