package com.student.devflow;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.student.devflow.mapper")
public class DevflowBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevflowBackendApplication.class, args);
    }

}
