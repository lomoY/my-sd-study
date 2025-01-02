package org.lomo.threadstudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@SpringBootApplication
public class ThreadStudyApp
{
    public static void main( String[] args ) throws InterruptedException {
        test();
        SpringApplication.run(ThreadStudyApp.class, args);
    }

    /**
     * 这个例子展示了线程安全和线程不安全
     */
    public static void test() throws InterruptedException {
        // 线程不安全的 ArrayList
        List<Integer> unsafeList = new ArrayList<>();
        // 线程安全的 ArrayList
        List<Integer> safeList = Collections.synchronizedList(new ArrayList<>());

        // 创建多个线程，向列表中添加元素
        Runnable unsafeTask = () -> {
            for (int i = 0; i < 1000; i++) {
                unsafeList.add(i);
            }
        };

        Runnable safeTask = () -> {
            for (int i = 0; i < 1000; i++) {
                safeList.add(i);
            }
        };

        // 启动线程
        Thread thread1 = new Thread(unsafeTask);
        Thread thread2 = new Thread(unsafeTask);
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        // 启动线程（线程安全的任务）
        Thread thread3 = new Thread(safeTask);
        Thread thread4 = new Thread(safeTask);
        thread3.start();
        thread4.start();
        thread3.join();
        thread4.join();

        // 打印结果
        System.out.println("Size of unsafeList: " + unsafeList.size());
        System.out.println("Size of safeList: " + safeList.size());
    }
}
