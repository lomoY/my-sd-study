package org.lomo.contextpropagation.test5;

import org.lomo.contextpropagation.test3.WrappedRunnable;
import org.lomo.contextpropagation.test4.MyControllerAdvise;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;


@RestController
public class ControllerV5 {
    static final ThreadLocal<Long> CORRELATION_ID = new ThreadLocal<>();

    @MyControllerAnnotation(myData = "on controller")
    @GetMapping(value="/performV5")
    public String getUsers(){

        System.out.println("get user 方法执行了");
        CompletableFuture
                .runAsync(new WrappedRunnable(
                        () -> getNumber()));

        CompletableFuture
                .runAsync(new WrappedRunnable(
                        () -> getNumber2()));

        return "1";
    }


    @MyDependencyAnnotation(myData = "on controller")
    public int getNumber() {
        System.out.println("线程名:"+ Thread.currentThread().getName()+ " getNumber 方法源代码被执行了");
        getNumber2();
        return 1;
    }
    @MyDependencyAnnotation(myData = "on controller")
    public int getNumber2(){
        System.out.println("线程名:"+ Thread.currentThread().getName()+ " getNumber2 方法源代码被执行了");
        return 2;
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
