package com.example.library.server.config;

import com.example.library.server.api.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/** Reactive router for users. */
@Configuration
public class UserRouter {

  @Bean
  public RouterFunction<ServerResponse> route(UserHandler userHandler) {

    return RouterFunctions.route(
            RequestPredicates.GET("/users")
                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON_UTF8)),
            userHandler::getAllUsers)
        .andRoute(
            RequestPredicates.GET("/users/{userId}")
                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON_UTF8)),
            userHandler::getUser)
        .andRoute(
            RequestPredicates.POST("/users")
                .and(RequestPredicates.contentType(MediaType.APPLICATION_JSON_UTF8)),
            userHandler::createUser)
        .andRoute(
            RequestPredicates.DELETE("/users/{userId}")
                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON_UTF8)),
            userHandler::deleteUser);
  }
}
