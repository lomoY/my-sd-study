package org.lomo.sutdy.demo;


import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Hooks;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {

        SpringApplication.run(Main.class,args);
    }

    @PostConstruct
    public void init() {
        Hooks.enableAutomaticContextPropagation();
    }
}