package com.example.oauth2loginclient.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;
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
            .attributes(clientRegistrationId("uaa"))
        //.attributes(oauth2AuthorizedClient(authorizedClient))
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
