package org.lomo.study.bravestracing;

import brave.Span;
import brave.Tracing;
import brave.propagation.CurrentTraceContext;

import java.util.concurrent.ExecutorService;

public class AsyncService {
    private final ExecutorService executor;
    private final Tracing tracing;

    public AsyncService(ExecutorService executor, Tracing tracing) {
        this.executor = executor;
        this.tracing = tracing;
    }

    public void executeAsyncTask() {
        // 开启一个新 Span
        Span span = tracing.tracer().newTrace().name("async-task").start();

        // 提交任务到线程池
        executor.submit(() -> {
            try (CurrentTraceContext.Scope scope = tracing.currentTraceContext().newScope(span.context())) {
                // 在这里执行逻辑，并传播 TraceContext
                System.out.println("Running async task with TraceId: " + span.context().traceIdString());
            } finally {
                span.finish(); // 结束 Span
            }
        });
    }
}
