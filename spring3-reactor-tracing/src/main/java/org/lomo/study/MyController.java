package org.lomo.study;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

@RestController
class MyController {
    private static final Logger log = LoggerFactory.getLogger(MyController.class);
    private final MyUserService myUserService;

    MyController(MyUserService myUserService) {
        this.myUserService = myUserService;
    }

    @GetMapping("/user/{userId}")
    String userName(@PathVariable("userId") String userId) {
        log.info("Got a request");
        handleRequest().block();
        myUserService.userName(userId);
        return "";
    }

    static Mono<Void> handleRequest() {
        // initRequest(); -- no write to ThreadLocal
        log("Assembling the chain");

        return Mono.just("test-product")
                // <1>
                .delayElement(Duration.ofMillis(1))
                .flatMap(product ->
                        Flux.concat(
                                addProduct(product),
                                notifyShop(product)).then());
//                .contextWrite(
//                        Context.of("CORRELATION_ID", correlationId())); // <2>
    }

    static Mono<Void> addProduct(String productName) {
        log.info("haha");
        log("Adding product: " + productName);
        return Mono.empty();
    }

    static Mono<Boolean> notifyShop(String productName) {
        log("Notifying shop about: " + productName);
        return Mono.just(true);
    }

    static long correlationId() {
        return Math.abs(ThreadLocalRandom.current().nextLong());
    }

    private static void log(String message) {
        // 从 MDC 获取 traceId 并打印日志
        String traceId = MDC.get("traceId");
        System.out.println("[" + traceId + "] " + message);
    }

}