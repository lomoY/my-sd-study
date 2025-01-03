package org.lomo.study;


import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationHandler;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.handler.TracingObservationHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfiguration {

//    @Bean
//    ObservationRegistry observationRegistry(Tracer tracer) {
//
//        ObservationRegistry observationRegistry = ObservationRegistry.create();
////        ObservationHandler<Observation.Context> tracingHandler = new TracingObservationHandler(tracer);
////        observationRegistry.observationConfig().observationHandler(tracingHandler);
//        return observationRegistry;
//    }

}
