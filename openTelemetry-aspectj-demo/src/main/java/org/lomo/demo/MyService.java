package org.lomo.demo;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Log4j2
public class MyService {

    ExecutorService executorService = Executors.newFixedThreadPool(3);

    @MyDependencyAnno
    public void methodA(){
        log.info("methodA is executed");
        executorService.submit(()->{
            methodB("haha");
        });
    }


    @MyDependencyAnno
    public void methodB(String input){
        log.info("methodB is executed");
        log.info("Thread Name=" + Thread.currentThread().getName());
    }
}
