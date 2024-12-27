package com.lomoy.aop.ltw;

import org.springframework.stereotype.Component;

@Component
public class MyStaticClass {

    @MyLog(myData="VC test")
    public static String staticGetString(){
        return "This is a string";
    }
}
