package org.lomoy;

import org.lomoy.aoplib.MyLibLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyController {
    @Autowired MyService myService;

    @MyLibLog(myData="VC test")
    @GetMapping("/users")
    public String getUsers(String id){
        System.out.println(id);
        myService.publicMethodA();
        return id;
    }

}
