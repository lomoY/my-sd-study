package com.lomoy.aop;

import org.springframework.stereotype.Component;

@Component
public class MyStaticClass {

    @MyLog(myData="VC test")
    public static String staticGetString(){
        return "This is a string";
    }
}
