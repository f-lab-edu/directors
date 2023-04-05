package com.directors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class DirectorsApplication {

    public static void main(String[] args) {
        SpringApplication.run(DirectorsApplication.class, args);
    }

}
