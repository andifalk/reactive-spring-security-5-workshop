package com.example.library.server.config;

import com.example.library.server.common.Role;
import com.example.library.server.security.LibraryReactiveUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.boot.autoconfigure.security.reactive.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class WebSecurityConfiguration {

  private final LibraryReactiveUserDetailsService libraryReactiveUserDetailsService;

  @Autowired
  public WebSecurityConfiguration(
      LibraryReactiveUserDetailsService libraryReactiveUserDetailsService) {
    this.libraryReactiveUserDetailsService = libraryReactiveUserDetailsService;
  }

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
        .hasRole(Role.CURATOR.name())
        .pathMatchers(HttpMethod.DELETE, "/books")
        .hasRole(Role.CURATOR.name())
        .pathMatchers("/users/**")
        .hasRole(Role.ADMIN.name())
        .pathMatchers("/books/**")
        .hasRole(Role.USER.name())
        .anyExchange()
        .authenticated();
    return http.build();
  }

  @Bean
  public LibraryUserJwtAuthenticationConverter libraryUserJwtAuthenticationConverter() {
    return new LibraryUserJwtAuthenticationConverter(libraryReactiveUserDetailsService);
  }

  @Bean
  public LibraryUserRolesJwtAuthenticationConverter libraryUserRolesJwtAuthenticationConverter() {
    return new LibraryUserRolesJwtAuthenticationConverter(libraryReactiveUserDetailsService);
  }
}
