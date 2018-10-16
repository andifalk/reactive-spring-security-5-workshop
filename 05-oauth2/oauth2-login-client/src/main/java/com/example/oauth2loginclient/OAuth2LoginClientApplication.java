package com.example.oauth2loginclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
public class OAuth2LoginClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(OAuth2LoginClientApplication.class, args);
    }
}
