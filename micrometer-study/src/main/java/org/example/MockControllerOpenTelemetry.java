package org.example;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.data.SpanData;
import io.opentelemetry.api.GlobalOpenTelemetry;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MockControllerOpenTelemetry {
    OpenTelemetryTracer openTelemetryTracer = new OpenTelemetryTracer();
    private static OpenTelemetry openTelemetry = GlobalOpenTelemetry.get();
    private static Tracer tracer = openTelemetry.getTracer("example-tracer");

    @GetMapping("/performot")
    public String getUsersSameThread(){
        openTelemetryTracer.initTraceId();
        System.out.println("线程名:"+ Thread.currentThread().getName()+ " getUsersSameThread 方法源代码被执行了,它的traceId是:" + openTelemetryTracer.getTraceId());
        CompletableFuture.runAsync(() -> getA());
        return "ok";
    }

    public String getA() {
        System.out.println("线程名:"+ Thread.currentThread().getName()+ " getA 方法源代码被执行了,它的traceId是:" + openTelemetryTracer.getTraceId());
        return "ok";
    }

    void test(){
        // 创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        // 创建一个根 Span
        Span parentSpan = tracer.spanBuilder("ParentSpan").startSpan();

        try (Scope parentScope = parentSpan.makeCurrent()) {
            // 提交多线程任务
            for (int i = 1; i <= 5; i++) {
                int taskId = i;
                executorService.submit(() -> processTask(taskId));
            }
        } finally {
            // 关闭根 Span
            parentSpan.end();
        }

        executorService.shutdown();
    }

    private static void processTask(int taskId) {
        // 创建一个子 Span
        Span span = tracer.spanBuilder("Task-" + taskId).startSpan();
        try (Scope scope = span.makeCurrent()) {
            // 在当前上下文中工作
            System.out.println("Processing Task " + taskId + " with TraceId: " + span.getSpanContext().getTraceId());
        } finally {
            // 结束 Span
            span.end();
        }
    }
}
