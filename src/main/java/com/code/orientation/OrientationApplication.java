package com.code.orientation;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@MapperScan("com.code.orientation.mapper")
@EnableAsync
@EnableSwagger2
@EnableScheduling
@SpringBootApplication
public class OrientationApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrientationApplication.class, args);
    }

}
