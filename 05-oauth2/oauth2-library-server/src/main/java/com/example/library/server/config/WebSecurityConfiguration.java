package com.example.library.server.config;

import com.example.library.server.common.Role;
import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.boot.autoconfigure.security.reactive.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class WebSecurityConfiguration {

  @Bean
  public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
    http.csrf()
        .disable()
        .authorizeExchange()
        .matchers(PathRequest.toStaticResources().atCommonLocations())
        .permitAll()
        .matchers(EndpointRequest.to("health"))
        .permitAll()
        .matchers(EndpointRequest.to("info"))
        .permitAll()
        .matchers(EndpointRequest.toAnyEndpoint())
        .hasRole(Role.ADMIN.name())
        .pathMatchers(HttpMethod.POST, "/books")
        .hasAuthority("SCOPE_curator")
        .pathMatchers(HttpMethod.DELETE, "/books")
        .hasAuthority("SCOPE_curator")
        .pathMatchers("/users/**")
        .hasAuthority("SCOPE_admin")
        .pathMatchers("/books/**")
        .hasAuthority("SCOPE_user")
        .anyExchange()
        .authenticated()
        .and()
        .oauth2ResourceServer()
        .jwt()
        .jwtAuthenticationConverter(
            new ReactiveJwtAuthenticationConverterAdapter(new GrantedAuthoritiesGroupExtractor()));
    return http.build();
  }
}
