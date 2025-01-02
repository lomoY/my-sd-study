package org.lomo.study.bravestracing;

import brave.Span;
import brave.Tracing;
import brave.propagation.CurrentTraceContext;
import brave.propagation.TraceContext;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws InterruptedException {

//        ExecutorService executor  = Executors.newFixedThreadPool(10);
//        executor.submit(()-> System.out.println(22222));//这个是ok的

        // 创建 Tracing 实例
        Tracing tracing = BraveConfiguration.createTracing();

        // 创建 TracingThreadPool
        TracingThreadPool tracingThreadPool = new TracingThreadPool(tracing);


        // 自定义 TraceId
        String customTraceId = "1234567890abcdef1234567890abcdef";
        TraceContext customTraceContext = TraceContext.newBuilder()
                .traceIdHigh(0) // 高位 traceId（如果不需要 128-bit，可以设置为 0）
                .traceId(Long.parseUnsignedLong(customTraceId.substring(16), 16)) // 低位 traceId
                .spanId(1) // 起始 SpanId
                .sampled(true) // 是否采样
                .build();
        Span span = tracing.tracer().newTrace().name("main-span").start();



        try (CurrentTraceContext.Scope scope = tracing.currentTraceContext().newScope(customTraceContext)) {
            // 激活上下文后，TraceContext 应该可以获取
            System.out.println("Activated TraceContext: " + tracing.currentTraceContext().get());
            System.out.println("TraceId: " + tracing.currentTraceContext().get().traceIdString());
        } finally {
            // 确保 Span 完成
            span.finish();
        }

//        if (tracing.currentTraceContext().get() == null) {
//            System.out.println("No TraceContext available in the current thread.");
//        } else {
//            System.out.println("TraceContext available. Submitting task...");
//        }

//        tracingThreadPool.submitTask(() -> {
//            if (tracing.currentTraceContext().get() == null) {
//                System.out.println("No TraceContext available in current thread...");
//            } else {
//                System.out.println("Executing task with TraceId: " + tracing.currentTraceContext().get().traceIdString());
//            }
//        });


        // 提交异步任务
        try (CurrentTraceContext.Scope scope = tracing.currentTraceContext().newScope(customTraceContext)) {
            System.out.println("Submitting task...");
            tracingThreadPool.submitTask(() -> {
                if (tracing.currentTraceContext().get() == null) {
                    System.out.println("No TraceContext available in current thread...");
                } else {
                    System.out.println("Executing task with TraceId: " + tracing.currentTraceContext().get().traceIdString());
                }
            });
            System.out.println("Executing task with TraceId: " + tracing.currentTraceContext().get().traceIdString());


        } finally {
            span.finish();
        }

        Thread.sleep(2000);
        // 关闭线程池
        tracingThreadPool.shutdown();
    }
}