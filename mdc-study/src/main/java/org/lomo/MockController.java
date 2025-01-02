package org.lomo;



import org.slf4j.MDC;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MockController {

    int i =0;
    @GetMapping("/perform")
    public String getUsers(){

        System.out.println("当前的线程名: " + Thread.currentThread()+ ":"+i++);
        System.out.println("当前活跃的线程数量: " + Thread.activeCount());
        System.out.println("当前线程的Group: "+Thread.currentThread().getThreadGroup());
        return "ok";
    }
}
