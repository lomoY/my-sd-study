package org.lomo.study.springobserbility;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@EnableAsync
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        SpringApplication.run(Main.class, args);
    }
}