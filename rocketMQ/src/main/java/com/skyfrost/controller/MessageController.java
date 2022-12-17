package com.skyfrost.controller;


import com.skyfrost.consumer.SpringBootConsumer;
import com.skyfrost.listener.SendCallbackListener;
import entity.Person;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 通过 Controller 发送不同类型的消息
 */
@RestController
@RequestMapping("/messageController")
public class MessageController {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Value(value = "${rocketmq.producer.topic}:${rocketmq.producer.sync-tag}")
    private String syncTag;

    @Value(value = "${rocketmq.producer.topic}:${rocketmq.producer.async-tag}")
    private String asyncag;

    @Value(value = "${rocketmq.producer.topic}:${rocketmq.producer.oneway-tag}")
    private String onewayTag;

    /**
     * rocketmq 同步消息
     *
     * @param id 消息
     * @return 结果
     */
    @RequestMapping("/pushMessage.action")
    public SendStatus pushMessage(@RequestParam("id") int id) {
        // 构建消息
        String messageStr = "order id : " + id;
        Message<String> message = MessageBuilder.withPayload(messageStr)
                .setHeader(RocketMQHeaders.KEYS, id)
                .build();
        // 设置发送地和消息信息并发送同步消息
        SendResult sendResult = rocketMQTemplate.syncSend(syncTag, message);
        // 返回消息发送状态,有四种状态，
        // SendStatus.SEND_OK,FLUSH_DISK_TIMEOUT,FLUSH_SLAVE_TIMEOUT,SLAVE_NOT_AVAILABLE
        return sendResult.getSendStatus();
    }

    /**
     * 发送异步消息
     *
     * @param id 消息
     * @return 结果
     */
    @RequestMapping("/pushAsyncMessage.action")
    public SendStatus pushAsyncMessage(@RequestParam("id") int id) {
        // 构建消息
        String messageStr = "order id : " + id;
        Message<String> message = MessageBuilder.withPayload(messageStr)
                .setHeader(RocketMQHeaders.KEYS, id)
                .build();
        // 设置发送地和消息信息并发送异步消息
        rocketMQTemplate.asyncSend(asyncag, message, new SendCallbackListener(id));
        return null;
    }

    /**
     * 发送单向消息（不关注发送结果：记录日志）
     *
     * @param id 消息
     * @return 结果
     */
    @RequestMapping("/pushOneWayMessage.action")
    public SendStatus pushOneWayMessage(@RequestParam("id") int id) {
        // 构建消息
        String messageStr = "order id : " + id;
        Message<String> message = MessageBuilder.withPayload(messageStr)
                .setHeader(RocketMQHeaders.KEYS, id)
                .build();
        // 设置发送地和消息信息并发送单向消息
        rocketMQTemplate.sendOneWay(onewayTag, message);
        return null;
    }

    /**
     * 发送包含顺序的单向消息
     *
     * @param id 消息
     * @return 结果
     */
    @RequestMapping("/pushSequeueMessage.action")
    public SendStatus pushSequeueMessage(@RequestParam("id") int id) {
        // 创建三个不同订单的不同步骤
        for (int i = 0; i < 3; i++) {
            // 处理当前订单唯一标识
            String myId = id + "" + i;
            // 获取当前订单的操作步骤列表
            List<Person> personList = new ArrayList<Person>();
            // 省略造数据步骤代码

            // 依次操作步骤下发消息队列
            for (Person person : personList) {
                // 构建消息
                String messageStr = String.format("order id : %s, desc : %s", person.getId(), person.getAdress());
                Message<String> message = MessageBuilder.withPayload(messageStr)
                        .setHeader(RocketMQHeaders.KEYS, person.getId())
                        .build();
                // 设置顺序下发
                rocketMQTemplate.setMessageQueueSelector(new MessageQueueSelector() {
                    /**
                     * 设置放入同一个队列的规则
                     * @param list 消息列表
                     * @param message 当前消息
                     * @param o 比较的关键信息
                     * @return 消息队列
                     */
                    @Override
                    public MessageQueue select(List<MessageQueue> list, org.apache.rocketmq.common.message.Message message, Object o) {
                        // 根据当前消息的id，使用固定算法获取需要下发的队列
                        // （使用当前id和消息队列个数进行取模获取需要下发的队列，id和队列数量一样时，选择的队列坑肯定一样）
                        int queueNum = Integer.valueOf(String.valueOf(o)) % list.size();
                        return list.get(queueNum);
                    }
                });
                // 设置发送地和消息信息并发送消息（Orderly）
                rocketMQTemplate.syncSendOrderly(syncTag, message, person.getId().toString());
            }
        }
        return null;
    }

    /**
     * rocketmq 延迟消息
     *
     * @param id 消息
     * @return 结果
     */
    @RequestMapping("/pushDelayMessage.action")
    public SendStatus pushDelayMessage(@RequestParam("id") int id) {
        // 构建消息
        String messageStr = "order id : " + id;
        Message<String> message = MessageBuilder.withPayload(messageStr)
                .setHeader(RocketMQHeaders.KEYS, id)
                .build();
        // 设置超时和延时推送
        // 超时时针对请求broker然后结果返回给product的耗时
        // 现在RocketMq并不支持任意时间的延时，需要设置几个固定的延时等级，从1s到2h分别对应着等级1到18
        // private String messageDelayLevel = "1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h";
        SendResult sendResult = rocketMQTemplate.syncSend(syncTag, message, 1 * 1000l, 4);
        return sendResult.getSendStatus();
    }


}
