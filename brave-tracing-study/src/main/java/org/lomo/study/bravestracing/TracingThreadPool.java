package org.lomo.study.bravestracing;

import brave.Tracing;
import brave.propagation.CurrentTraceContext;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TracingThreadPool {
    private final ExecutorService executor;
    private final CurrentTraceContext currentTraceContext;

    public TracingThreadPool(Tracing tracing) {
        this.currentTraceContext = tracing.currentTraceContext();
//        this.executor = Executors.newFixedThreadPool(10); // 定义线程池
        this.executor=currentTraceContext.executorService(Executors.newFixedThreadPool(10));
        System.out.println("Thread pool created.");
    }

    public void submitTask(Runnable task) {
        // 包装任务，确保 Trace Context 传播
        System.out.println("Submitting task...");

        executor.submit(currentTraceContext.wrap(task));
    }

    public <T> Future<T> submitTask(Callable<T> task) {
        // 包装任务，确保 Trace Context 传播
        return executor.submit(currentTraceContext.wrap(task));
    }

    public void shutdown() {
        executor.shutdown();
    }
}
