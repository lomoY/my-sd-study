package com.lomoy.aop.prog;

import org.aspectj.weaver.loadtime.ClassPreProcessorAgentAdapter;
import org.aspectj.weaver.tools.WeavingAdaptor;

import java.lang.instrument.Instrumentation;

public class CustomAspectJConfig {
    public static void configureLTW() {
        try {
            // 获取当前的 Instrumentation
            Instrumentation instrumentation = org.aspectj.weaver.loadtime.Agent.getInstrumentation();
            if (instrumentation == null) {
                throw new IllegalStateException("Instrumentation is not available. Ensure -javaagent is configured.");
            }

            if (instrumentation != null) {
                System.out.println("Can redefine classes: " + instrumentation.isRedefineClassesSupported());
                System.out.println("Can retransform classes: " + instrumentation.isRetransformClassesSupported());
                System.out.println("JVM Version: " + System.getProperty("java.version"));
                System.out.println("JVM Vendor: " + System.getProperty("java.vendor"));
                System.out.println("JVM Name: " + System.getProperty("java.vm.name"));
            } else {
                System.out.println("Instrumentation is null. Ensure -javaagent is configured.");
            }


            // 添加 AspectJ 的 ClassFileTransformer
            instrumentation.addTransformer(new ClassPreProcessorAgentAdapter(), true);

            System.out.println("AspectJ Load-Time Weaving configured successfully.");
        } catch (Exception e) {
            throw new RuntimeException("Failed to configure AspectJ LTW", e);
        }
    }
}

