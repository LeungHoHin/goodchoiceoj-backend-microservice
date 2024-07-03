package com.lhx.judgeservice;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.lhx")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.lhx.serviceclient.service"})
public class JudgeServiceMain8104 {
    public static void main(String[] args) {
        SpringApplication.run(JudgeServiceMain8104.class, args);
    }
}