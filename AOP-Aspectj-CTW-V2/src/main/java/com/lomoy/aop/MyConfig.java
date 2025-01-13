package com.lomoy.aop;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.Aspects;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@Log4j2
public class MyConfig {

//    @Bean
//    @DependsOn("aspectThreadPool")
//    public MyLogAspect myLogAspect(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
//        MyLogAspect myLogAspect = new MyLogAspect(threadPoolTaskExecutor);
//        return myLogAspect;
//    }
    @Bean
    public MyAspect theAspect() {
        MyAspect aspect = Aspects.aspectOf(MyAspect.class);
        return aspect;
    }


    @Bean(name = "aspectThreadPool")
    public ThreadPoolTaskExecutor aspectThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);  // Core thread count
        executor.setMaxPoolSize(10); // Max thread count
        executor.setQueueCapacity(25); // Task queue capacity
        executor.setThreadNamePrefix("我的AOP自定义线程池-");
        executor.initialize();
        log.info("Aspect 依赖的Bean初始化完毕");
        return executor;
    }
}
