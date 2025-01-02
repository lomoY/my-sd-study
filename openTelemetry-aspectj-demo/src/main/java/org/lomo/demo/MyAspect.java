package org.lomo.demo;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class MyAspect {


    @Around("@annotation(MyControllerAnno)")
    public Object aroundOnController(ProceedingJoinPoint joinPoint) throws Throwable {
        Tracer tracer = GlobalOpenTelemetry.getTracerProvider().get("my-tracer","v1");
        Span span = tracer.spanBuilder("span-v1")
                .setParent(Context.root())
                .startSpan();

        String initialTraceId = span.getSpanContext().getTraceId();

        try(Scope scope = span.makeCurrent()) {
            System.out.println(Thread.currentThread().getName() + ": Captured method: " + joinPoint.getSignature()+",traceId="+span.getSpanContext().getTraceId());
            return joinPoint.proceed();
        } finally {
            span.end();
        }
    }

    @Around("@annotation(MyDependencyAnno)")
    public Object aroundDependency(ProceedingJoinPoint joinPoint) throws Throwable {
        Tracer tracer = GlobalOpenTelemetry.getTracerProvider().get("my-tracer","v1");
        Span span = tracer.spanBuilder("span-v2")
//                .setParent(Context.root())
                .startSpan();

        String initialTraceId = span.getSpanContext().getTraceId();

        try(Scope scope = span.makeCurrent()) {
            String signatrureName = joinPoint.getSignature().getName();
            Object[] args = joinPoint.getArgs();
            System.out.println(Thread.currentThread().getName() + ": @Before Captured method: " + joinPoint.getSignature());

            Context currentContext = Context.current();
            System.out.println("Thread=" + Thread.currentThread().getName() +", traceId="+span.getSpanContext().getTraceId());
            return joinPoint.proceed();
        }
    }
}
