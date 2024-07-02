package com.lhx.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@ComponentScan("com.lhx")
@EnableScheduling
public class UserServiceMain8102 {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceMain8102.class, args);
    }
}