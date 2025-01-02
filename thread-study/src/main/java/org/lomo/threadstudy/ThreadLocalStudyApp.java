package org.lomo.threadstudy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadLocalStudyApp {


    public static void main(String[] args) {

        //初始化一个用3个线程的固定线程池
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        //创建一个threadLocal变量
        ThreadLocal<String> threadLocalStr = new ThreadLocal<>();

        //在线程池中随机选择一个线程设置ThreadLocal的值
        executorService.submit(()->{
            threadLocalStr.set("这是ThreadLocalStr");
            System.out.println("在" + Thread.currentThread().getName() +"中，设置ThreadLocal的值为=" + threadLocalStr.get());
        });

        //随机调用线程来获取当前线程中ThreadLocal的值
        for (int i = 0; i < 10; i++) {
            executorService.submit(()->{
                System.out.println("在" + Thread.currentThread().getName() +"中，尝试获取ThreadLocal= " + threadLocalStr.get());
            });
        }
    }
}
