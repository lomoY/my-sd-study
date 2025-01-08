package org.lomo.study;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
class MyUserService {
    private static final Logger log = LoggerFactory.getLogger(MyUserService.class);

    private final Random random = new Random();

    ExecutorService executorService = Executors.newFixedThreadPool(3);

    @Autowired
    @Qualifier("SpringManagedExecutor")
    ExecutorService springManagedExecutorService;
    void userName(String userId) {
        log.info("Getting user name for user with id " + userId);
        try {
            Thread.sleep(random.nextLong(200L)); // simulates latency
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        asyncMono();
        asyncMethod("hahaha");
//        ExecutorService executorService = Executors.newFixedThreadPool(3);
        executorService.submit(()->asyncMethod("hohoho"));
        springManagedExecutorService.submit(()->asyncMethod("hohoho2"));

        asyncWithSpringAnnotation("heiheihei");
        executorService.submit(()->asyncMethod("heyheyhey"));
        springManagedExecutorService.submit(()->asyncMethod("heyheyhey2"));

        CompletableFuture.runAsync(()->asyncWithSpringAnnotation("lol"));

    }


//    @Async
    void asyncMethod(String data) {
        log.info(data);
    }

    @Async
    void asyncWithSpringAnnotation(String data) {
        log.info(data);
    }

    void asyncMono() {
        Mono.just("abcdefg")
                .doOnNext(ele->log.info(ele))
//                .subscribeOn(Schedulers.boundedElastic())
                .subscribeOn(Schedulers.fromExecutor(springManagedExecutorService))
//                .publishOn(Schedulers.fromExecutor(springManagedExecutorService))
                .subscribe();
    }
}
