package org.lomo.contextpropagation.test4;


import io.micrometer.context.ContextRegistry;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.util.context.Context;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

import javax.annotation.PostConstruct;

import static org.lomo.contextpropagation.test4.ControllerV4.CORRELATION_ID;

//@Aspect
public class ContextPropagatingAspect {
    private final Scheduler dedicatedScheduler = Schedulers.newBoundedElastic(10, 100, "dedicated-pool");



//    @Around("@annotation(MyControllerAdvise)")
//    public Object propagateContext(ProceedingJoinPoint joinPoint) throws Throwable {
//        return Mono.deferContextual(context -> {
//            ContextRegistry.getInstance().registerThreadLocalAccessor(
//                    "CORRELATION_ID",
//                    CORRELATION_ID::get,
//                    CORRELATION_ID::set,
//                    CORRELATION_ID::remove
//            );
//            System.out.println("ThreadLocalAccessor for CORRELATION_ID registered.");
//                    // Add traceId or other context data
//                    String traceId = context.getOrDefault("traceId", "default-trace-id");
//                    System.out.println("TraceId: " + traceId);
//
//                    // Execute the target method
//                    return Mono.fromCallable(() -> {
//                                try {
//                                     joinPoint.proceed();
//                                     return 1;
//                                } catch (Throwable e) {
//                                    throw new RuntimeException(e);
//                                }
//                            })
//                            .subscribeOn(dedicatedScheduler);
//                });
//    }


//    下面这个方法会报错: org.springframework.core.codec.CodecException: No SSE encoder configured and the data is not String.
//	at org.springframework.http.codec.ServerSentEventHttpMessageWriter.encodeEvent(ServerSentEventHttpMessageWriter.java:171) ~[spring-web-5.3.20.jar:5.3.20]
//	Suppressed: reactor.core.publisher.FluxOnAssembly$OnAssemblyException:
//Assembly trace from producer [reactor.core.publisher.FluxMapFuseable] :
//	reactor.core.publisher.Flux.map


//    @Around("@annotation(MyControllerAdvise)")
//    public Object propagateContext(ProceedingJoinPoint joinPoint) throws Throwable {
//        // 获取目标方法的返回类型
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        Class<?> returnType = signature.getReturnType();
//
//        // 异步执行逻辑
//        Mono<Object> asyncOperation = Mono.deferContextual(context -> {
//            // 注册ThreadLocalAccessor
//            ContextRegistry.getInstance().registerThreadLocalAccessor(
//                    "CORRELATION_ID",
//                    CORRELATION_ID::get,
//                    CORRELATION_ID::set,
//                    CORRELATION_ID::remove
//            );
//            System.out.println("ThreadLocalAccessor for CORRELATION_ID registered.");
//
//            // 获取上下文信息
//            String traceId = context.getOrDefault("traceId", "default-trace-id");
//            System.out.println("TraceId: " + traceId);
//
//            // 执行目标方法
//            return Mono.fromCallable(() -> {
//                try {
//                    return joinPoint.proceed();
//                } catch (Throwable e) {
//                    throw new RuntimeException(e);
//                }
//            }).subscribeOn(dedicatedScheduler);
//        });
//
//        // 根据目标方法返回类型处理
//        if (Mono.class.isAssignableFrom(returnType)) {
//            // 如果目标方法返回值是Mono，直接返回异步操作
//            return asyncOperation.flatMap(result -> (Mono<?>) result);
//        } else {
//            // 如果目标方法返回值是普通类型，返回异步操作，但不阻塞目标线程
//            asyncOperation.subscribe(result -> {
//                // 可以在此处理AOP后的结果，或仅执行附加逻辑
//                System.out.println("AOP operation completed asynchronously: " + result);
//            });
//            // 直接返回目标方法执行结果
//            return joinPoint.proceed();
//        }
//    }


    @Around("@annotation(MyControllerAdvise)")
    public Object propagateContext(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Class<?> returnType = signature.getReturnType();

        // 异步操作（独立运行）
        Mono.deferContextual(context -> {
            ContextRegistry.getInstance().registerThreadLocalAccessor(
                    "CORRELATION_ID",
                    CORRELATION_ID::get,
                    CORRELATION_ID::set,
                    CORRELATION_ID::remove
            );
            System.out.println("ThreadLocalAccessor for CORRELATION_ID registered.");

            String traceId = context.getOrDefault("traceId", "default-trace-id");
            System.out.println("TraceId: " + traceId);

            return Mono.fromCallable(() -> {
                try {
                    return joinPoint.proceed();
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }).subscribeOn(dedicatedScheduler);
        }).subscribe(res -> {
            System.out.println("AOP asynchronous operation completed: " + res);
        });

        // 同步返回目标方法的执行结果
        return joinPoint.proceed();
    }



}
