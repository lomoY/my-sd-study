package org.example;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MockController {

    @GetMapping("/perform")
    public Integer getUsers(){
        return 1;
    }

    @GetMapping("/perform2")
    public String getUsers2(){
        return "ok";
    }
}
