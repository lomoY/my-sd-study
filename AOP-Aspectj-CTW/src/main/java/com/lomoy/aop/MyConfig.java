package com.lomoy.aop;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class MyConfig {

    @Bean(name = "aspectThreadPool")
    public ThreadPoolTaskExecutor aspectThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);  // Core thread count
        executor.setMaxPoolSize(10); // Max thread count
        executor.setQueueCapacity(25); // Task queue capacity
        executor.setThreadNamePrefix("我的AOP自定义线程池-");
        executor.initialize();
        return executor;
    }
}
