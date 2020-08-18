package com.hydeze.hypad;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.hydeze.hypad.mapper")
public class HypadApplication {

    public static void main(String[] args) {
        SpringApplication.run(HypadApplication.class, args);
    }

}
