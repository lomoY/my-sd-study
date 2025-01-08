package org.lomo.study;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.context.Context;
//import reactor.core.publisher.Hooks;
//import reactor.core.publisher.Mono;
//import reactor.core.scheduler.Schedulers;
//import reactor.util.context.Context;


@SpringBootApplication
public class Main {
    public static void main(String[] args) {
//
//        System.out.println("Hello world!");
//        Hooks.enableAutomaticContextPropagation();
//
//        // 在主线程的上下文中放置一些数据
//        Mono.deferContextual(context -> {
//                    String userId = context.getOrDefault("userId", "unknown");
//                    String traceId = context.getOrDefault("traceId", "unknown");
//                    System.out.println(Thread.currentThread() +"Processing traceId in main thread: " + traceId);
//                    System.out.println(Thread.currentThread() +"Processing userId in main thread: " + userId);
//                    return Mono.just(userId);
//                })
//                .flatMap(userId -> processInDifferentThread(userId))
//                .contextWrite(Context.of("userId", "12346")) // 添加上下文数据
//                .block(); // 阻塞等待完成


    }


    private static Mono<String> processInDifferentThread(String userId) {
        return Mono.deferContextual(context -> {
            String contextUserId = context.getOrDefault("userId", "unknown");
            System.out.println(Thread.currentThread() +" Processing userId in a different thread: " + contextUserId);
            return Mono.just(contextUserId);
        }).subscribeOn(Schedulers.boundedElastic()); // 切换到不同线程
    }

    private static String fetchContextFromNonReactorMethod() {
        return ReactorContextHolder.get("userId").orElse("unknown");
    }

    static class ReactorContextHolder {

        private static final ThreadLocal<Context> contextThreadLocal = new ThreadLocal<>();

        public static void set(Context context) {
            contextThreadLocal.set(context);
        }

        public static void clear() {
            contextThreadLocal.remove();
        }

        public static java.util.Optional<String> get(String key) {
            Context context = contextThreadLocal.get();
            if (context != null && context.hasKey(key)) {
                return java.util.Optional.of(context.get(key).toString());
            }
            return java.util.Optional.empty();
        }
    }
}