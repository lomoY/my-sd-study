package com.lomoy.aop;


import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAutoConfiguration
@EnableAspectJAutoProxy(exposeProxy=true)
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }


    @PostConstruct
    void init(){
    }

}
