package org.lomo.contextpropagation.test4;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.config.WebFluxConfigurer;


/**
 * 报错: org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'errorWebExceptionHandler' defined in class path resource
 */

//@Configuration
public class WebFluxConfig implements WebFluxConfigurer {

    @Override
    public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
        // 确保 JSON 编码器被注册
        configurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder());
        configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder());
    }
}
