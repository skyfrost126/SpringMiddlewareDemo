package com.skyfrost.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AnnotationJob {

    @Scheduled(cron = "0 0 1 * * ?")
    public void annotationJob() {
        // spring自带的定时任务，不需要配置数据库，任务启动时间由cron表达式控制
        // 任务主逻辑 ...
    }
}
