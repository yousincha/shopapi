package com.example.shopapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class ShopapiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopapiApplication.class, args);
    }

}
