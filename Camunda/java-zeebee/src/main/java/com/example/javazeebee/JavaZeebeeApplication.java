package com.example.javazeebee;

import io.camunda.zeebe.spring.client.EnableZeebeClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableZeebeClient

public class JavaZeebeeApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaZeebeeApplication.class, args);
    }

}
