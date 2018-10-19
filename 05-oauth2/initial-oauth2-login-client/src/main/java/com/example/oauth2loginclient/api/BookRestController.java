package com.example.oauth2loginclient.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@RestController
public class BookRestController {

  private final WebClient webClient;

  @Autowired
  public BookRestController(WebClient webClient) {
    this.webClient = webClient;
  }

  @GetMapping("/books")
  Flux<BookResource> books() {
    return webClient
        .get()
        .uri("http://localhost:8080/books")
        .retrieve()
        .bodyToFlux(BookResource.class);
  }

  @GetMapping("/users")
  Flux<UserResource> users() {
    return webClient
        .get()
        .uri("http://localhost:8080/users")
        .retrieve()
        .bodyToFlux(UserResource.class);
  }
}
