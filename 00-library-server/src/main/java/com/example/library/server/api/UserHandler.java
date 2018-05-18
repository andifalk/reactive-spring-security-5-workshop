package com.example.library.server.api;

import com.example.library.server.business.UserResource;
import com.example.library.server.business.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Reactive handler for users.
 */
@Component
public class UserHandler {

    private final UserService userService;

    @Autowired
    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    public Mono<ServerResponse> getAllUsers(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(userService.findAll(), UserResource.class);
    }

    public Mono<ServerResponse> getUser(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(userService.findById(UUID.fromString(request.pathVariable("userId"))), UserResource.class);
    }

    public Mono<ServerResponse> deleteUser(ServerRequest request) {
        userService.deleteById(UUID.fromString(request.pathVariable("userId"))).subscribe();
        return ServerResponse.ok().build();
    }

    public Mono<ServerResponse> createUser(ServerRequest request) {
        userService.create(request.bodyToMono(UserResource.class)).subscribe();
        return ServerResponse.ok().build();
    }

}
