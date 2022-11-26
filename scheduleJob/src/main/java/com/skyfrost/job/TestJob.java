package com.skyfrost.job;

import org.quartz.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TestJob implements Job {

    // 此处用于编写定时任务主逻辑
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDetail jobDetail = jobExecutionContext.getJobDetail();
        JobKey key = jobDetail.getKey();
        System.out.println(key.getName());
        System.out.println(key.getGroup());
        System.out.println("hello job exec"+new Date());
    }
}
