package com.skyfrost;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling // 开启定时任务注解
public class App 
{
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
