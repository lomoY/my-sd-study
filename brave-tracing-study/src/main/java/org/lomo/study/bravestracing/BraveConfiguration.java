package org.lomo.study.bravestracing;

import brave.Tracing;
import brave.context.slf4j.MDCScopeDecorator;
import brave.propagation.ThreadLocalCurrentTraceContext;

public class BraveConfiguration {
    public static Tracing createTracing() {
        // 创建 Tracing 实例，配置 MDC 用于日志记录
        return Tracing.newBuilder()
                .localServiceName("my-service") // 设置服务名称
                .currentTraceContext(ThreadLocalCurrentTraceContext.newBuilder()
//                        .addScopeDecorator(MDCScopeDecorator.get()) // 将 MDC 装饰器添加到 TraceContext
                        .build())

                .build();
    }
}
