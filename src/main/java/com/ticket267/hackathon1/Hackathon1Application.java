package com.ticket267.hackathon1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class Hackathon1Application {

    public static void main(String[] args) {
        SpringApplication.run(Hackathon1Application.class, args);
    }
}
