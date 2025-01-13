package org.lomo.study;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        reactorExecution();
//        sequenceExecution();
    }











    private static void reactorExecution(){
        // 记录开始时间
        long startTime = System.currentTimeMillis();

        // 输入数据
        String input = "hello, world";

        // 第一步：收到一个 String 的数据
        System.out.println("Step 1: Received data -> " + input);

        // 第二步和第三步：方法 A 和方法 B 并行执行
        Mono<String> step2 = Mono.fromCallable(() -> {
            try {
                dummyApi(input);
                return "Step 2 completed.";
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Step 2 interrupted.");
            }
        });
//                .subscribeOn(Schedulers.boundedElastic());

        Mono<String> step3 = Mono.fromCallable(() -> {
            try {
                return toUpperCase(input);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Step 3 interrupted.");
            }
        });
//        .subscribeOn(Schedulers.boundedElastic());

        // 使用 Mono.zip 确保 step2 和 step3 并行执行
        Mono.zip(step2, step3)
                .doOnNext(tuple -> {
                    System.out.println(tuple.getT1());
                    System.out.println("Step 4: Result -> " + tuple.getT2());
                    long endTime = System.currentTimeMillis();
                    System.out.println("Total time taken: " + (endTime - startTime) / 1000.0 + " seconds");
                })
                .doFinally(signal -> {
                    long endTime = System.currentTimeMillis();
                    System.out.println("Total time taken: " + (endTime - startTime) / 1000.0 + " seconds");
                })
                .block();
    }


    private static void sequenceExecution() throws InterruptedException {
        // 记录开始时间
        long startTime = System.currentTimeMillis();

// 输入数据
        String input = "hello, world";

        // 第一步：收到一个 String 的数据
        System.out.println("Step 1: Received data -> " + input);

        // 第二步：方法 A 将 String 的数据发送到 dummyApi
        System.out.println("Step 2: Sending data to dummyApi...");
        dummyApi(input);

        // 第三步：方法 B 将 String 改为大写
        System.out.println("Step 3: Converting data to uppercase...");
        String result = toUpperCase(input);

        // 第四步：将方法 B 的结果返回
        System.out.println("Step 4: Result -> " + result);

        // 记录结束时间并计算耗时
        long endTime = System.currentTimeMillis();
        System.out.println("Total time taken: " + (endTime - startTime) / 1000.0 + " seconds");
    }

    // 模拟调用 dummyApi，耗时 2 秒
    private static void dummyApi(String data) throws InterruptedException {
        System.out.println("dummyApi processed data start: " + data);
        Thread.sleep(2000); // 模拟耗时
        System.out.println("dummyApi processed data end: " + data);
    }

    // 模拟将字符串转为大写，耗时 2 秒
    private static String toUpperCase(String data) throws InterruptedException {
        Thread.sleep(2000); // 模拟耗时
        System.out.println("第三步：改为大写字母");
        return data.toUpperCase();
    }

}