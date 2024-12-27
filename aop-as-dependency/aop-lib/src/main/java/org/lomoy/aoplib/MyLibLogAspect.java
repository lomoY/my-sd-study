package org.lomoy.aoplib;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class MyLibLogAspect {
    /**
     * Aspectj requires a default contractor. You can try to remove it, and you will see error
     */
    public MyLibLogAspect(){

    }

    @Before("execution(private * com.lomoy..*(..))")
    public void capturePrivateMethods(JoinPoint joinPoint) {
        System.out.println(Thread.currentThread().getName() + ": @Before Captured method: " + joinPoint.getSignature());
    }

    @Before("@annotation(MyLibLog)")
    public void beforeAdviceWithThread(JoinPoint joinPoint){
        String signatrureName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        System.out.println(Thread.currentThread().getName() + ": @Before Captured method: " + joinPoint.getSignature());
    }

    @After("@annotation(MyLibLog)")
    public void afterAdviceWithThread(JoinPoint joinPoint){
        String signatrureName = joinPoint.getSignature().getName();
        System.out.println(Thread.currentThread().getName() + ": @After Captured method: " + signatrureName);
    }
}
