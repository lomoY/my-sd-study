package org.lomo.threadstudy;

import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 这个实验证明了inheritableThreadLocal的中设置的值可以被carry到子线程中去，但是threadLocal的值并不会
 */


@SpringBootApplication
public class inheritableThreadLocalStudy {
    private static InheritableThreadLocal<String> inheritableThreadLocal = new InheritableThreadLocal<>();
    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();
    public static void main(String[] args) {
        testInheritableThreadLocal();
        testThreadLocal();
    }

    static void testInheritableThreadLocal() {
        inheritableThreadLocal.set("testInheritableThreadLocal aaaaa");

        Thread childThread = new Thread(() -> {
            System.out.println("testInheritableThreadLocal 线程名: " + Thread.currentThread().getName()+" Child Thread: " + inheritableThreadLocal.get()); // "Main Thread Value"
            inheritableThreadLocal.set("bbbbb");
            System.out.println("testInheritableThreadLocal 线程名: " + Thread.currentThread().getName()+" 子线程重写后的值 " + inheritableThreadLocal.get());
        });

        childThread.start();

        System.out.println("testInheritableThreadLocal 线程名: " + Thread.currentThread().getName()+" Main Thread: " + inheritableThreadLocal.get());//只被继承，但子线程里对它的修改不会改变它本身。
    }

    static void testThreadLocal() {
        threadLocal.set("aaaaa");

        Thread childThread = new Thread(() -> {
            System.out.println("testThreadLocal 线程名: " + Thread.currentThread().getName()+" Child Thread: " + threadLocal.get()); // "Main Thread Value"
            threadLocal.set("bbbbb");
            System.out.println("testThreadLocal 线程名: " + Thread.currentThread().getName()+" 子线程重写后的值 " + threadLocal.get());
        });

        childThread.start();

        System.out.println("testThreadLocal 线程名: " + Thread.currentThread().getName()+" Main Thread: " + threadLocal.get());//只被继承，但子线程里对它的修改不会改变它本身。
    }
}
