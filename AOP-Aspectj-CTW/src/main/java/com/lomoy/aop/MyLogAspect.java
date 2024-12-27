package com.lomoy.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Aspect
public class MyLogAspect {
    /**
     * Aspectj requires a default contractor. You can try to remove it, and you will see error
     */
    public MyLogAspect(){

    }

    @Before("execution(private * com.lomoy..*(..))")
    public void capturePrivateMethods(JoinPoint joinPoint) {
        System.out.println(Thread.currentThread().getName() + ": @Before Captured method: " + joinPoint.getSignature());
    }

    @Before("@annotation(MyLog)")
    public void beforeAdviceWithThread(JoinPoint joinPoint){
        String signatrureName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        System.out.println(Thread.currentThread().getName() + ": @Before Captured method: " + joinPoint.getSignature());
    }

    @After("@annotation(MyLog)")
    public void afterAdviceWithThread(JoinPoint joinPoint){
        System.out.println(111);
        String signatrureName = joinPoint.getSignature().getName();
        System.out.println(Thread.currentThread().getName() + ": @After Captured method: " + signatrureName);
    }
}
