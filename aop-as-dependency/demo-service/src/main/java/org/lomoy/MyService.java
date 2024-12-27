package org.lomoy;

import org.lomoy.aoplib.MyLibLog;
import org.springframework.stereotype.Service;

@Service
public class MyService {

    @MyLibLog(myData="VC test")
    public void publicMethodA(){
        publicMethodB();
        privateMethodB();
    }


//    @MyLog(myData="VC test")
    public void publicMethodB() {
        publicMethodC();
    }

//    @MyLog(myData="VC test")
    public void publicMethodC() {
        return;
    }


//    @MyLog(myData="VC test")
    private void privateMethodB() {
    }

}
