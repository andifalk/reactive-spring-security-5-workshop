package com.example.authentication.demo;

import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class ReactiveWebSecurityConfiguration {

  @Bean
  SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
    return http.authorizeExchange()
            .matchers(EndpointRequest.to(HealthEndpoint.class)).permitAll()
            .anyExchange()
            .authenticated()
            .and()
            .httpBasic().and().formLogin()
            .and().build();
  }

}
