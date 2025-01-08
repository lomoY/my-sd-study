package org.lomo.study.springobserbility;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.exporter.logging.LoggingSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
class MyConfiguration {

    //这个是提供给外部使用的
//   @Bean
//    public OpenTelemetrySdk openTelemetry() {
//        // Configure the OpenTelemetry SDK
//        SdkTracerProvider tracerProvider = SdkTracerProvider.builder()
//                .addSpanProcessor(SimpleSpanProcessor.create(new LoggingSpanExporter())) // Example exporter
//                .build();
//
//        OpenTelemetrySdk openTelemetrySdk = OpenTelemetrySdk.builder()
//                .setTracerProvider(tracerProvider)
//                .build();
//
//        GlobalOpenTelemetry.set(openTelemetrySdk);
//
//        return openTelemetrySdk;
//    }

//    @Bean
//    ObservationRegistry register(){
//        // Create an ObservationRegistry
//        ObservationRegistry registry = ObservationRegistry.create();
//// Register an ObservationHandler
//        registry.observationConfig().observationHandler(new MyHandler());
//
//// Create an Observation and observe your code!
//        Observation.createNotStarted("user.name", registry)
//                .contextualName("getting-user-name")
//                .lowCardinalityKeyValue("userType", "userType1") // let's assume that you can have 3 user types
//                .highCardinalityKeyValue("userId", "1234") // let's assume that this is an arbitrary number
//                .observe(() -> System.out.println("Hello")); // this is a shortcut for starting an observation, opening a scope, running user's code, closing the scope and stopping the observation
//
//        return registry;
//    }
//
//    // To have the @Observed support we need to register this aspect
//    @Bean
//    ObservedAspect observedAspect(ObservationRegistry observationRegistry) {
//        return new ObservedAspect(observationRegistry);
//    }


}