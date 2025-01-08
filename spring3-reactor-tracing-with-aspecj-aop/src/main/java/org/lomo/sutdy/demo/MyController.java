package org.lomo.sutdy.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

@RestController
class MyController {
    private static final Logger log = LoggerFactory.getLogger(MyController.class);
    private final MyUserService myUserService;

    MyController(MyUserService myUserService) {
        this.myUserService = myUserService;
    }


    @GetMapping("/user/{userId}")
    String userName(@PathVariable("userId") String userId) {
        log.info("Got a request");
        myUserService.userName(userId);
        return "ok";
    }

}