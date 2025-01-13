package com.lomoy.aop;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

@Controller
@Log4j2
public class MyController {

    @Autowired MyService myService;

    @Autowired MyServiceWithLombok myServiceWithLombok;

    @MyLog(myData="VC test")
    @GetMapping("/str")
    public String str(){
        log.info("str");
        myService.publicMethodA();
        myServiceWithLombok.getName();
        return "OK";
    }

//    @JokerEntry
    @GetMapping("/monostr")
    public Mono<String> getMonoStr(){
        log.info("monostr");
        return myService.publicMonoMethodA().doOnSuccess(ele->log.info(ele));
    }

    @GetMapping("/monostrANNO")
    public String strANNO(){
        log.info("strSSSSS");
        myService.publicMethodA();
        myServiceWithLombok.getName();
        return "OK";
    }

}
