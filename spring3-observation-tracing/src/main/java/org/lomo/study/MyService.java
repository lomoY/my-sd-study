package org.lomo.study;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class MyService {
    private static final Logger log = LoggerFactory.getLogger(MyService.class);
    ExecutorService executorService = Executors.newFixedThreadPool(3);



    public void methodA() {
        log.info("methodA called");
        executorService.submit(()->{methodB();});
    }

    public void methodB(){
        log.info("methodB called");
    }


}
