package org.lomo.study;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class MyConfiguration {

    @Qualifier("SpringManagedExecutor")
    @Bean
    ExecutorService getExecutorService(){
        return Executors.newFixedThreadPool(3);
    }
}
