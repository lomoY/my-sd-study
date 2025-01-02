package org.example;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.*;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.extension.trace.propagation.B3Propagator;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class OpenTelemetryTracer {
    private  OpenTelemetry openTelemetry = GlobalOpenTelemetry.get();
    private  Tracer tracer = openTelemetry.getTracer("example-tracer");

    public void initTraceId(){
        SdkTracerProvider tracerProvider = SdkTracerProvider.builder().build();
        Tracer tracer = tracerProvider.get("my-service");
        TextMapPropagator propagator = B3Propagator.injectingMultiHeaders();



        // Step 1: Generate a custom traceId
        String customTraceId = UUID.randomUUID().toString().replace("-", "");
        String customSpanId = generateRandomSpanId();
        // Step 2: Create a SpanContext with the custom traceId
        SpanContext spanContext = SpanContext.create(
                customTraceId,  // traceId
                customSpanId,  // spanId
                TraceFlags.getDefault(),  // traceFlags
                TraceState.getDefault()   // traceState
        );

        // Step 3: Set the SpanContext in the current Context
        Context contextWithCustomTrace = Context.current().with(Span.wrap(spanContext));

        // Step 4: Create a new Span using the custom context
        try (Scope scope = contextWithCustomTrace.makeCurrent()) {
            Span span = tracer.spanBuilder("CustomTraceSpan")
                    .setParent(contextWithCustomTrace)  // Link the custom traceId
//                    .setSpanKind(SpanKind.SERVER)
                    .startSpan();

            // Add some attributes or events
            span.setAttribute("example.attribute", "value");
            span.addEvent("Event in Custom Trace Span");

            // End the span
//            span.end();
        }


    }

    public String getTraceId(){

        Span span = tracer.spanBuilder("CustomTraceSpan").startSpan();
        try (Scope scope = span.makeCurrent()) {
            // 在当前上下文中工作
            String traceId = span.getSpanContext().getTraceId();
            System.out.println("Get traceId" + traceId);
            return traceId;

        } finally {
            // 结束 Span
            span.end();
        }
    }

    private static String generateRandomSpanId() {
        // Generate a random 8-byte spanId (16 hexadecimal characters)
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }
}
