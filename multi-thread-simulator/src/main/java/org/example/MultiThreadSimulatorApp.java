package org.example;

import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Hello world!
 *
 */
public class MultiThreadSimulatorApp
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );

        String baseUrl = "http://localhost:8080/performot";
        WebClient webClient = WebClient.create();

        int requestCount = 500; // 模拟 50 个并发请求
        List<CompletableFuture<String>> futures = new ArrayList<>();

        for (int i = 0; i < requestCount; i++) {
            int index = i;
            CompletableFuture<String> future = CompletableFuture.supplyAsync(() ->
                    webClient.get()
                            .uri(baseUrl)
                            .retrieve()
                            .bodyToMono(String.class)
                            .block() // 阻塞等待结果
            );
            futures.add(future);
        }

        // 等待所有请求完成
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenRun(() -> System.out.println("All requests completed!"))
                .join();

        // 输出结果
        futures.forEach(future ->
                future.thenAccept(response -> System.out.println("Response: " + response))
        );
    }

}

