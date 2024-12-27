package org.example;

import org.springframework.stereotype.Service;

@Service
public class MyService {

    @MyLog(myData="VC test")
    public void publicMethodA(){
        privateMethodB();
    }

    @MyLog(myData="VC test")
    private void privateMethodB() {
        privateMethodC();
    }

    @MyLog(myData="VC test")
    public void privateMethodC() {
        return;
    }


}
