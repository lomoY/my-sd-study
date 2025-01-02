package org.lomo.contextpropagation.test4;

import org.lomo.contextpropagation.test3.WrappedRunnable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;




@RestController
public class ControllerV4 {
    static final ThreadLocal<Long> CORRELATION_ID = new ThreadLocal<>();


    @GetMapping(value="/performV4", produces = MediaType.APPLICATION_JSON_VALUE)
    public Integer getUsers(){

        System.out.println("get user 方法执行了");
        int result = getNumber();
        return 1;
    }


    @MyControllerAdvise(myData = "on controller")

    public int getNumber() {
        return 1;
    }

    CompletableFuture<Void> handleRequest() {
        return CompletableFuture
                .runAsync(new WrappedRunnable(
                        () -> addProduct("test-product")))
                .thenRunAsync(new WrappedRunnable(
                        () -> notifyShop("test-product")));
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
