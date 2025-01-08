package org.lomo.study;


import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Hooks;


@SpringBootApplication
public class Main2 {

    public static void main(String[] args) {

        SpringApplication.run(Main2.class,args);
    }

    @PostConstruct
    public void init() {
        Hooks.enableAutomaticContextPropagation();
    }

}
