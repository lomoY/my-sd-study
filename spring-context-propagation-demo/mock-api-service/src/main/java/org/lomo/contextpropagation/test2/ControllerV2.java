package org.lomo.contextpropagation.test2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

@RestController
public class ControllerV2 {
    static final ThreadLocal<Long> CORRELATION_ID = new ThreadLocal<>();

    @GetMapping("/performV2")
    public String getUsers(){

        initRequest();

        handleRequest();
        return "ok";
    }

    public CompletableFuture<Void> handleRequest() {
        return CompletableFuture
                .runAsync(() -> addProduct("test-product"))
                .thenRunAsync(() -> notifyShop("test-product"));
    }

    void addProduct(String productName) {
        log("Adding product: " + productName);
        // ...
    }

    void notifyShop(String productName) {
        log("Notifying shop about: " + productName);
        // ...
    }

    void initRequest() {
        CORRELATION_ID.set(correlationId());
        System.out.println("初始化Correlation Id 完成");

    }


   public long correlationId() {
        return Math.abs(ThreadLocalRandom.current().nextLong());
    }

  public  void log(String message) {
        String threadName = Thread.currentThread().getName();
        String threadNameTail = threadName.substring(
                Math.max(0, threadName.length() - 10));
        System.out.printf("[%10s][%20s] %s%n",
                threadNameTail, CORRELATION_ID.get(), message);
    }
}
