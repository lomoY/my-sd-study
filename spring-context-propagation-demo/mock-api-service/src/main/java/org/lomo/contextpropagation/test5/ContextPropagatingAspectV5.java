package org.lomo.contextpropagation.test5;


import brave.Tracing;
import brave.baggage.BaggagePropagation;
import brave.baggage.BaggagePropagation.FactoryBuilder;
import brave.handler.SpanHandler;
import brave.propagation.StrictCurrentTraceContext;
import brave.propagation.B3Propagation;
import brave.propagation.ThreadLocalCurrentTraceContext;
import brave.sampler.Sampler;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.brave.bridge.BraveBaggageManager;
import io.micrometer.tracing.brave.bridge.BraveCurrentTraceContext;
import io.micrometer.tracing.brave.bridge.BraveTracer;
import io.micrometer.tracing.propagation.Propagator;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Aspect
public class ContextPropagatingAspectV5 {
    private Tracer tracer;
    private final Scheduler dedicatedScheduler = Schedulers.newBoundedElastic(10, 100, "dedicated-pool");
    private  Executor executor;

    public ContextPropagatingAspectV5() {
        // 初始化 Tracer
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


//
//    @Around("@annotation(MyControllerAnnotation)")
//    public Object MyControllerAnnotation(ProceedingJoinPoint joinPoint) throws Throwable {
//        String id="";
//        if (tracer.currentSpan()!=null) {
//            id= tracer.currentSpan().context().traceId();
////            System.out.println(joinPoint.getSignature().getName()+" Trace ID: " + id);
//
//        }
//
//        // 创建一个新的 span
//        Span span = tracer.nextSpan().name("propagate-context");
//        try (Tracer.SpanInScope spanInScope = tracer.withSpan(span)) {
//            // 此时 tracer.currentSpan() 不再是 null
//            if (!id.isBlank()) {
//                System.out.println(joinPoint.getSignature().getName()+" Trace ID: " + id);
//
//            } else {
//                System.out.println(joinPoint.getSignature().getName()+" Trace ID: " + tracer.currentSpan().context().traceId());
//
//            }
//
//            // 执行目标方法
//            return joinPoint.proceed();
//        } finally {
//            // 确保 span 结束
//            span.end();
//        }
//    }

    // 处理 MyOnControllerAnno 注解的方法
//    @Around("@annotation(MyControllerAnnotation)")
//    public Object handleController(ProceedingJoinPoint joinPoint) throws Throwable {
//        // 生成新的 trace span
//        Span span = tracer.nextSpan().name("controller-span").start();
//        try (Tracer.SpanInScope spanInScope = tracer.withSpan(span)) {
//            System.out.println("[Controller] Generated and propagated traceId: " + span.context().traceId());
//            return joinPoint.proceed();
//        } finally {
//            span.end();
//        }
//    }

    @Around("@annotation(MyControllerAnnotation)")
    public Object handleController(ProceedingJoinPoint joinPoint) throws Throwable {
        Span parentSpan = tracer.currentSpan();
        if (parentSpan != null) {
            String traceId = parentSpan.context().traceId();
            System.out.println("线程名:"+ Thread.currentThread().getName()+ " [Parent Thread] Trace ID: " + traceId);
        }
        // 生成新的 trace span
        Span span = tracer.nextSpan().name("controller-span").start();
        try (Tracer.SpanInScope spanInScope = tracer.withSpan(span)) {
            System.out.println("线程名:"+ Thread.currentThread().getName()+ " [Controller] Generated and propagated traceId: " + span.context().traceId());
            span.tag("taxValue","abc");
            span.event("myEvent");
            return joinPoint.proceed();
        } finally {
            span.end();
        }
    }



//    @Around("@annotation(MyDependencyAnnotation)")
//    public Object propagateContext(ProceedingJoinPoint joinPoint) throws Throwable {
//        String id="";
//        if (tracer.currentSpan()!=null) {
//            id= tracer.currentSpan().context().traceId();
////            System.out.println(joinPoint.getSignature().getName()+" Trace ID: " + id);
//
//        }
//
//        // 创建一个新的 span
//       Span span = tracer.nextSpan().name("propagate-context");
//        try (Tracer.SpanInScope spanInScope = tracer.withSpan(span)) {
//            // 此时 tracer.currentSpan() 不再是 null
//            if (!id.isBlank()) {
//                System.out.println(joinPoint.getSignature().getName()+" Trace ID: " + id);
//
//            } else {
//                System.out.println(joinPoint.getSignature().getName()+" Trace ID: " + tracer.currentSpan().context().traceId());
//
//            }
//
//            // 执行目标方法
//            return joinPoint.proceed();
//        } finally {
//            // 确保 span 结束
//            span.end();
//        }
//    }


    // 处理 MyOnDependencyAnno 注解的方法
    @Around("@annotation(MyDependencyAnnotation)")
    public Object handleDependency(ProceedingJoinPoint joinPoint) throws Throwable {
        Span testSpan = tracer.nextSpan().name("controller-span");
        String parentTraceId=testSpan.context().traceId();
        System.out.println("线程名:"+ Thread.currentThread().getName()+ " [Dependency] Propagated Parent traceId: " +parentTraceId);

        Span currentSpan = tracer.currentSpan();
        if (currentSpan != null) {
            TraceContext context = currentSpan.context();
            System.out.println("线程名:"+ Thread.currentThread().getName()+ " [Dependency] Propagated traceId: " + context.traceId());

            // 使用当前上下文创建新的 Span 以传递到异步线程
            return CompletableFuture.supplyAsync(() -> {
                Span asyncSpan = tracer.nextSpan(currentSpan).name("dependency-async-span").start();
                try (Tracer.SpanInScope spanInScope = tracer.withSpan(asyncSpan)) {
                    return joinPoint.proceed();
                } catch (Throwable throwable) {
                    throw new RuntimeException(throwable);
                } finally {
                    asyncSpan.end();
                }
            }, executor).get();
        } else {
            System.out.println("线程名:"+ Thread.currentThread().getName()+ " [Dependency] No traceId found in context.");
            return joinPoint.proceed();
        }
    }

}
