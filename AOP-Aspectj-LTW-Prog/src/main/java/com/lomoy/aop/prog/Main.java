package com.lomoy.aop.prog;

import org.aspectj.weaver.loadtime.Agent;
import org.aspectj.weaver.loadtime.ClassPreProcessorAgentAdapter;
import org.aspectj.weaver.loadtime.WeavingURLClassLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

@SpringBootApplication
public class Main
{
    public static void main(String[] args) throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException {

        // 配置 AspectJ LTW
        CustomAspectJConfig.configureLTW();
        SpringApplication.run(Main.class, args);
    }
}
