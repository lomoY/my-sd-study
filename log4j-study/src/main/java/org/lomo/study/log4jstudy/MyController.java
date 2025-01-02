package org.lomo.study.log4jstudy;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.logging.Logger;

@Controller
public class MyController {

    static Logger log = Logger.getLogger(MyController.class.getName());

    @GetMapping("/users")
    public String getUsers(){

        log.info("okkk");
        Thread callOne = new Thread(()->{
            log.info("hhaha");
        });

        callOne.start();
        return "id";
    }

}
