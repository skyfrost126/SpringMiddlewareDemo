package com.skyfrost.listener;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.stereotype.Component;

@Component
public class SendCallbackListener implements SendCallback {

    private int id;

    public SendCallbackListener(int id) {
        this.id = id;
    }

    @Override
    public void onSuccess(SendResult sendResult) {
        // 日志记录成功信息
    }

    @Override
    public void onException(Throwable throwable) {
        // 日志记录异常
    }

}
