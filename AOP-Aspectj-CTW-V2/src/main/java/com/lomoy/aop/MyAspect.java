package com.lomoy.aop;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static com.lomoy.aop.PointcutConstants.JOKER_ENTRY;


@Aspect
@Log4j2
@Component
@DependsOn("aspectThreadPool")
public class MyAspect {

    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public MyAspect() {
        // 无参构造方法
        log.info("无参数构造器初始化完成");
    }

//    @PostConstruct
//    void init(ThreadPoolTaskExecutor threadPoolTaskExecutor){
//        this.threadPoolTaskExecutor=threadPoolTaskExecutor;
//    }

//    @Autowired
//    public void setThreadPoolTaskExecutor(ThreadPoolTaskExecutor threadPoolTaskExecutor){
//        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
//        log.info("MyLogAspect initialized");
//    }



//    @Before("@annotation(MyControllerAnno)")
//    public void beforeAdviceWithThread(JoinPoint joinPoint){
//        String signatrureName = joinPoint.getSignature().getName();
//        Object[] args = joinPoint.getArgs();
//        System.out.println(Thread.currentThread().getName() + ": @Before Captured method: " + joinPoint.getSignature());
//    }

//    @After("@annotation(MyLog)")
//    public void afterAdviceWithThread(JoinPoint joinPoint){
//        log.info(11111);
//        String signatrureName = joinPoint.getSignature().getName();
//        System.out.println(Thread.currentThread().getName() + ": @After Captured method: " + signatrureName);
//    }

//    这个配置会因为和目标函数返回值不同而不能被weave
//    @Around("@annotation(JokerEntry)")
//    public Object afterAdviceWithThread(ProceedingJoinPoint jp) throws Throwable {
//        log.info(11111);
//        String signatrureName = jp.getSignature().getName();
//        System.out.println(Thread.currentThread().getName() + ": @After Captured method: " + signatrureName);
//
//        return jp.proceed();
//    }

    //    这个配置会因为和目标函数返回值不同而不能被weave
//    @Around("@annotation(JokerEntry)")
    public Object afterAdviceWithThread(ProceedingJoinPoint jp) throws Throwable {

        try{
            log.info(11111);
            this.threadPoolTaskExecutor.submit(()->log.info(22));
            String signatrureName = jp.getSignature().getName();
            System.out.println(Thread.currentThread().getName() + ": @After Captured method: " + signatrureName);

            Object result = jp.proceed();
            return result;
        }
        catch (Exception e){
            log.error(e);
        }

        return null;
    }

    @Around(JOKER_ENTRY)
    public Object springanno(ProceedingJoinPoint jp) throws Throwable {
        try{
//            log.info(2222);
//            this.threadPoolTaskExecutor.submit(()->log.info(22));
            String signatrureName = jp.getSignature().getName();
//            System.out.println(Thread.currentThread().getName() + ": @After Captured method: " + signatrureName);

            Object result = jp.proceed();
            if (result instanceof Mono) {

                return ((Mono<?>) result)
                        .flatMap(payload -> {
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            CapturedData capturedData = constructCapturedData(payload);
                            return sendToKafka(capturedData).then(((Mono<?>) result));
                        })
                        .doOnError(error -> log.error("Error processing Mono:{} ",error.getMessage()));
//                        .subscribe();
            }
            return result;
        }
        catch (Exception e){
            log.error(e);
        }

        return null;
    }

    private CapturedData constructCapturedData(Object payload) {
        // Implement the logic to construct CapturedData from the payload
        CapturedData capturedData = CapturedData.builder()
                .dummy("dummy")
                .build();
        // Example: populate fields based on payload
        // capturedData.setField(payload.someField());
        return capturedData;
    }

    private Mono<Void> sendToKafka(CapturedData capturedData) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Mono.defer(()->{
            log.info("数据已经发送到Kafka");
            return Mono.empty();
        });
//        return


    }

}
