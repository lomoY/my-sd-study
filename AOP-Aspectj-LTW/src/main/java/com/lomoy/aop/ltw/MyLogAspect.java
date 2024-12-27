package com.lomoy.aop.ltw;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class MyLogAspect {

    final String MY_BEFORE = "execution(* com.lomoy..*(..))";
    /**
     * Aspectj requires a default contractor. You can try to remove it, and you will see error
     */
//    public MyLogAspect(){
//
//    }

    @Before(MY_BEFORE)
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("Before method: " + joinPoint.getSignature().getName());
    }
}
