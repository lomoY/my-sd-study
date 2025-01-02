package org.example;



import brave.Tracing;
import brave.propagation.B3Propagation;
import brave.propagation.ThreadLocalCurrentTraceContext;
import brave.sampler.Sampler;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.brave.bridge.BraveBaggageManager;
import io.micrometer.tracing.brave.bridge.BraveCurrentTraceContext;
import io.micrometer.tracing.brave.bridge.BraveTracer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Controller
public class MockController {
    private Tracer tracer;
    private Executor executor;


    public MockController(){
        this.tracer = createTracer();
    }

    private Tracer createTracer() {
        // 设置 Brave 的 TraceContext 管理
        // 创建 Brave 的核心组件
        Tracing tracing = Tracing.newBuilder()
                .currentTraceContext(ThreadLocalCurrentTraceContext.create()) // 跟踪上下文
                .sampler(Sampler.ALWAYS_SAMPLE) // 始终采样
                .propagationFactory(B3Propagation.newFactoryBuilder().build())
                .build();

        this.executor = tracing.currentTraceContext().executorService(Executors.newCachedThreadPool());
        // 初始化 Micrometer 的 Tracer
        Tracer tracer = new BraveTracer(
                tracing.tracer(),
                new BraveCurrentTraceContext(tracing.currentTraceContext()),
                new BraveBaggageManager() // 使用默认的 BaggageManager
        );

        return tracer;
    }


    /**
     * 在一个线程内维护traceId
     * @return
     */
    @GetMapping("/perform")
    public String getUsersSameThread(){
        initTraceId();
        getA();
        return "ok";
    }

    public String getA() {
        getTraceId();
        return "ok";
    }


    /**
     * 在两个线程内维护traceId
     * @return
     */



    @GetMapping("/performasync")
    public Integer getUsers(){
        return 1;
    }

    @GetMapping("/perform2")
    public String getUsers2(){
        return "ok";
    }



    void initTraceId(){
        Span parentSpan = tracer.currentSpan();
        if (parentSpan != null) {
            String traceId = parentSpan.context().traceId();
            System.out.println("线程名:"+ Thread.currentThread().getName()+ " [Parent Thread] Trace ID: " + traceId);
        }
        // 生成新的 trace span
        Span span = tracer.nextSpan().name("controller-span");
        try (Tracer.SpanInScope spanInScope = tracer.withSpan(span.start())) {
            System.out.println("线程名:"+ Thread.currentThread().getName()+ " [Controller] Generated and propagated traceId: " + span.context().traceId());
            span.tag("taxValue","abc");
            span.event("myEvent");
        } finally {
            span.end();
        }
    }

    void getTraceId(){
        Span testSpan = tracer.nextSpan().name("controller-span");
        String parentTraceId=testSpan.context().traceId();
        System.out.println("线程名:"+ Thread.currentThread().getName()+ " [Dependency] Propagated Parent traceId: " +parentTraceId);

        Span currentSpan = tracer.currentSpan();
        if (currentSpan != null) {
            TraceContext context = currentSpan.context();
            System.out.println("线程名:"+ Thread.currentThread().getName()+ " [Dependency] Propagated traceId: " + context.traceId());

            // 使用当前上下文创建新的 Span 以传递到异步线程
                Span asyncSpan = tracer.nextSpan(currentSpan).name("dependency-async-span").start();
                try (Tracer.SpanInScope spanInScope = tracer.withSpan(asyncSpan)) {
                } catch (Throwable throwable) {
                    throw new RuntimeException(throwable);
                } finally {
                    asyncSpan.end();
                }

        } else {
            System.out.println("线程名:"+ Thread.currentThread().getName()+ " [Dependency] No traceId found in context.");
        }
    }
}
