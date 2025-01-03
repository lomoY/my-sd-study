package org.lomo.study;


import io.micrometer.observation.annotation.Observed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MyController {
    private static final Logger log = LoggerFactory.getLogger(MyController.class);

    @Autowired
    MyService myService;


    @GetMapping("/perform")
    public String getUsers(){
        log.info("hahaha");
        //如果是System.out.print, 是不回得到traceId的
        myService.methodA();
        return "ok";
    }
}
