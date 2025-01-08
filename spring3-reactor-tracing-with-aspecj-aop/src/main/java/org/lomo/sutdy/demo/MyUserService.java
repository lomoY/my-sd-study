package org.lomo.sutdy.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
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

    @JokerCapture
    void userName(String userId) {
        log.info("Getting user name for user with id " + userId);
        try {
            Thread.sleep(random.nextLong(200L)); // simulates latency
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        asyncMono()
                .publishOn(Schedulers.fromExecutorService(executorService))
                .flatMap(ele->{
//                    String newStr = ele + " team";
                    return Mono.just(buildStr(ele));
                }).subscribeOn(Schedulers.boundedElastic()).subscribe();
        ;
        asyncMethod("hahaha");
        executorService.submit(()->asyncMethod("hohoho"));

        asyncWithSpringAnnotation("heiheihei");
        executorService.submit(()->asyncMethod("heyheyhey"));

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


    @JokerCapture
    Mono<String> asyncMono() {
        return Mono.just("wakanda")
                .doOnNext(ele->log.info(ele));
//                .subscribeOn(Schedulers.boundedElastic());
//                .subscribe();
    }


    @JokerCapture
    String buildStr(String str) {
        return str + "team";
    }
}
