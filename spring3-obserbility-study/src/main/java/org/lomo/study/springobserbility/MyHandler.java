package org.lomo.study.springobserbility;


import io.micrometer.common.KeyValue;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationHandler;


import io.micrometer.observation.contextpropagation.ObservationThreadLocalAccessor;
import io.micrometer.tracing.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.StreamSupport;

// Example of plugging in a custom handler that in this case will print a statement before and after all observations take place
//@Component
class MyHandler implements ObservationHandler<Observation.Context> {

    @Autowired
    private  Tracer tracer;
    private static final Logger log = LoggerFactory.getLogger(MyHandler.class);

    @Override
    public void onStart(Observation.Context context) {
//        System.out.println(tracer.currentSpan().context().traceId());
        // 尝试获取 traceId
        String traceId = context.getOrDefault("traceId", "No Trace ID Found");
        System.out.println(traceId);
        log.info("Before running the observation for context [{}], userType [{}]", context.getName(), getUserTypeFromContext(context));
    }

    @Override
    public void onStop(Observation.Context context) {
//        System.out.println(tracer.currentSpan().context().traceId());
        String contextName = context.getContextualName();
        Object obj = context.get(contextName);
        // 尝试获取 traceId
        String traceId = context.getOrDefault("traceId", "No Trace ID Found");
        System.out.println(traceId);
        log.info("After running the observation for context [{}], userType [{}]", context.getName(), getUserTypeFromContext(context));
    }

    @Override
    public boolean supportsContext(Observation.Context context) {
        return true;
    }

    private String getUserTypeFromContext(Observation.Context context) {
        return StreamSupport.stream(context.getLowCardinalityKeyValues().spliterator(), false)
                .filter(keyValue -> "userType".equals(keyValue.getKey()))
                .map(KeyValue::getValue)
                .findFirst()
                .orElse("UNKNOWN");
    }
}