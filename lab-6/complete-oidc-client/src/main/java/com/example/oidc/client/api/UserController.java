package com.example.oidc.client.api;

import com.example.oidc.client.api.resource.UserResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/users")
public class UserController {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

  private final WebClient webClient;

  @Value("${library.server}")
  private String libraryServer;

  public UserController(WebClient webClient) {
    this.webClient = webClient;
  }

  @GetMapping
  public String users() {
    return "users";
  }

  @ModelAttribute("allUsers")
  Flux<UserResource> allUsers() {
    return webClient
        .get()
        .uri(libraryServer + "/users")
        .retrieve()
        .onStatus(
            httpStatus -> HttpStatus.FORBIDDEN.value() == httpStatus.value(),
            clientResponse -> Mono.error(new AccessDeniedException("No access for this resource")))
        .onStatus(
            s -> s.equals(HttpStatus.FORBIDDEN),
            cr -> Mono.just(new AccessDeniedException("Not authorized")))
        .onStatus(
            HttpStatus::is5xxServerError,
            clientResponse -> Mono.error(new IllegalStateException("Internal error occurred")))
        .bodyToFlux(UserResource.class);
  }

  @ExceptionHandler(WebClientResponseException.class)
  public ResponseEntity<String> handleWebClientResponseException(WebClientResponseException ex) {
    LOGGER.error(
        "Error from WebClient - Status {}, Body {}",
        ex.getRawStatusCode(),
        ex.getResponseBodyAsString(),
        ex);
    return ResponseEntity.status(ex.getRawStatusCode()).body(ex.getResponseBodyAsString());
  }
}
