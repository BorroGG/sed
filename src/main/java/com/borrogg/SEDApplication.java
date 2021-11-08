package com.borrogg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan("com.borrogg.entities")
@SpringBootApplication
public class SEDApplication  {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SEDApplication.class);
        app.run(args);
    }
}
