package org.lomo.contextpropagation.test1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadLocalRandom;

@RestController
public class ControllerV1 {
    static final ThreadLocal<Long> CORRELATION_ID = new ThreadLocal<>();

    @GetMapping("/performV1")
    public String getUsers(){

        initRequest();

        addProduct("test-product");
        notifyShop("test-product");
        return "ok";
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
