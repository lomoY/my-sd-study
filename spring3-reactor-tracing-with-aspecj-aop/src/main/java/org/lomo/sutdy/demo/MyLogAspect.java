package org.lomo.sutdy.demo;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

@Aspect
public class MyLogAspect {


    private static final Logger log = LoggerFactory.getLogger(MyLogAspect.class);


    public MyLogAspect(){}


    @Around("@annotation(JokerCapture)")
    public Object beforeAdviceWithThread(ProceedingJoinPoint jp) throws Throwable {
        String signatrureName = jp.getSignature().getName();
        Object[] args = jp.getArgs();
        log.info(MDC.get("traceId") + "captured in joker " + jp.getSignature());
        log.info(Thread.currentThread().getName() + ": @Before Captured method: " + jp.getSignature());

        Object result = jp.proceed();

        return result;
    }


}
