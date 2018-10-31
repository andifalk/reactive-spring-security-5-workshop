package com.example.oauth2loginclient.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@RestController
public class BookRestController {

  private final WebClient webClient;

  @Autowired
  public BookRestController(WebClient webClient) {
    this.webClient = webClient;
  }

  @GetMapping("/books")
  Flux<BookResource> books(
      @RegisteredOAuth2AuthorizedClient("uaa") OAuth2AuthorizedClient authorizedClient) {
    return webClient
        .get()
        .uri("http://localhost:8080/books")
        //.attributes(clientRegistrationId("uaa"))
        .attributes(oauth2AuthorizedClient(authorizedClient))
        .retrieve()
        .bodyToFlux(BookResource.class);
  }

  @GetMapping("/users")
  Flux<UserResource> users(
      @RegisteredOAuth2AuthorizedClient("uaa") OAuth2AuthorizedClient authorizedClient) {
    return webClient
        .get()
        .uri("http://localhost:8080/users")
        .attributes(oauth2AuthorizedClient(authorizedClient))
        .retrieve()
        .bodyToFlux(UserResource.class);
  }
}
