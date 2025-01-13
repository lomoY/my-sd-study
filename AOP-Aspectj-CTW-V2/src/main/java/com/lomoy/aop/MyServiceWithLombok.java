package com.lomoy.aop;

import lombok.Data;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Data
@Getter
@Component
public class MyServiceWithLombok {
    String name;
}
