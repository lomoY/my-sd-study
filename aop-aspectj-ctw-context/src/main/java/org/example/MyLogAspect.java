package org.example;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
@Log4j2
public class MyLogAspect {

    public MyLogAspect(){

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
