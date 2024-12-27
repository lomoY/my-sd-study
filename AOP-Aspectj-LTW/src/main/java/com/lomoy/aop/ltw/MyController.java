package com.lomoy.aop.ltw;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyController {

    @Autowired MyService myService;

    @MyLog(myData="VC test")
    @GetMapping("/users")
    public String getUsers(String id){
        myService.publicMethodA();
        return "ok";
    }

}
