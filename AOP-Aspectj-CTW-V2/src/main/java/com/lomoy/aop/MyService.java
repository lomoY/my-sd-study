package com.lomoy.aop;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Log4j2
public class MyService {

    @MyLog(myData="VC test")
    public void publicMethodA(){
        publicMethodB();
        privateMethodB();
        String staticString =  MyStaticClass.staticGetString();
    }


    public Mono<String> publicMonoMethodA(){
        log.info("publicMonoMethodA");

        Mono<String> monoExample = Mono.just("abc") // 创建 Mono 数据源
                .map(this::publicMonoMethodB)     // 将字符串转换为大写
                .flatMap(value -> publicMonoMethodC(value));

//        publicMethodB();
        return monoExample;
    }

    @JokerEntry
    public Mono<String> publicMonoMethodB(String str){
        log.info("publicMonoMethodB");
       return Mono.just(str.toUpperCase());
    }

    public Mono<String> publicMonoMethodC(Mono<String> strMono) {
        return strMono.map(str->str+"modified");
    }

    public void publicMethodB() {
        log.info("publicMethodB这个方法被调用了");
        publicMethodC();
    }

    @MyLog(myData="VC test")
    public void publicMethodC() {
        return;
    }


    @MyLog(myData="VC test")
    private void privateMethodB() {
    }

}
