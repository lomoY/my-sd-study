package org.lomo.sutdy;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.*;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    static ExecutorService executorServiceOne = Executors.newFixedThreadPool(4);
    static ExecutorService executorServiceTwo = Executors.newFixedThreadPool(4);


    public static void main(String[] args) {
        System.out.println("Hello world!");

        Tracer tracer = GlobalOpenTelemetry.getTracerProvider().get("my-tracer","v1");

        Span span = tracer.spanBuilder("span-v1")
                .setParent(Context.root())
                .startSpan();

        String initialTraceId = span.getSpanContext().getTraceId();

        try(Scope scope = span.makeCurrent()) {
            Context currentContext = Context.current();
            System.out.println(currentContext);
        }

        myLog(initialTraceId);

        try(Scope scope  = span.makeCurrent()) {
            async1Wrap();
        }

    }

    static void async1Wrap(){
        Tracer tracer = GlobalOpenTelemetry.getTracerProvider().get("my-tracer","v1");
        Span span = tracer.spanBuilder("span-v2")
                .startSpan();

        try (Scope scope = span.makeCurrent()) {
            Context currentContext = Context.current();
            System.out.println(currentContext);
            executorServiceOne.submit(Context.current().wrap(()->{
                async1();
            }));
        }

    }

    static void async1(){
        Tracer tracer = GlobalOpenTelemetry.getTracerProvider().get("my-tracer","v1");
        Span span = tracer.spanBuilder("span-v3")
                .startSpan();

        //scope有是什么
        try (Scope scope = span.makeCurrent()) {
            Context currentContext = Context.current();
            System.out.println(currentContext);
            System.out.println("async1 adfadsf");
        }
    }

    public static void myLog(String traceId){
        System.out.println("[[[线程名:" + Thread.currentThread().getName()+"]]] TraceId="+traceId);
    }
}