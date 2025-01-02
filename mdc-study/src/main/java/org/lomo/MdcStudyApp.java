package org.lomo;

import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.CompletableFuture;


@SpringBootApplication
public class MdcStudyApp
{
    public static void main( String[] args )
    {
        test();
        SpringApplication.run(MdcStudyApp.class, args);
    }





    public static void test() {

        MDC.put("myId","aaaa");
        AsyncMethod();
        System.out.println(MDC.get("myId"));
        AsyncMethod2();
    }

    public static void AsyncMethod(){

//        var mdcContext = MDC.getCopyOfContextMap();

        CompletableFuture.runAsync(()->{
//            if (mdcContext != null) {
//                MDC.setContextMap(mdcContext);
//            }
            System.out.println("async method called "+ MDC.get("myId"));
        });
    }

    public static void AsyncMethod2(){

        var mdcContext = MDC.getCopyOfContextMap();

        CompletableFuture.runAsync(()->{
            if (mdcContext != null) {
                MDC.setContextMap(mdcContext);
            }
            System.out.println("async method called "+ MDC.get("myId"));
        });
    }
}
