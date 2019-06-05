package com.example.oidc.client.api;

import com.example.oidc.client.api.resource.BookResource;
import com.example.oidc.client.api.resource.CreateBookResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Controller
public class BookController {

  private final WebClient webClient;

  @Value("${library.server}")
  private String libraryServer;

  public BookController(WebClient webClient) {
    this.webClient = webClient;
  }

  @ModelAttribute("books")
  Flux<BookResource> books() {
    return webClient
        .get()
        .uri(libraryServer + "/books")
        .retrieve()
        .onStatus(
            s -> s.equals(HttpStatus.UNAUTHORIZED),
            cr -> Mono.just(new BadCredentialsException("Not authenticated")))
        .onStatus(
            s -> s.equals(HttpStatus.FORBIDDEN),
            cr -> Mono.just(new AccessDeniedException("Not authorized")))
        .onStatus(
            HttpStatus::is4xxClientError,
            cr -> Mono.just(new IllegalArgumentException(cr.statusCode().getReasonPhrase())))
        .onStatus(
            HttpStatus::is5xxServerError,
            cr -> Mono.just(new Exception(cr.statusCode().getReasonPhrase())))
        .bodyToFlux(BookResource.class);
  }

  @GetMapping("/")
  Mono<String> index(@AuthenticationPrincipal OAuth2User user, Model model) {

    model.addAttribute("fullname", user.getName());
    model.addAttribute(
        "isCurator",
        user.getAuthorities().stream().anyMatch(ga -> ga.getAuthority().equals("library_curator")));
    return Mono.just("index");
  }

  @GetMapping("/createbook")
  String createForm(Model model) {

    model.addAttribute("book", new CreateBookResource());

    return "createbookform";
  }

  @PostMapping("/create")
  Mono<String> create(
      CreateBookResource createBookResource, ServerWebExchange serverWebExchange, Model model)
      throws IOException {

    return webClient
        .post()
        .uri(libraryServer + "/books")
        .body(Mono.just(createBookResource), CreateBookResource.class)
        .retrieve()
        .onStatus(
            s -> s.equals(HttpStatus.UNAUTHORIZED),
            cr -> Mono.just(new BadCredentialsException("Not authenticated")))
        .onStatus(
            s -> s.equals(HttpStatus.FORBIDDEN),
            cr -> Mono.just(new AccessDeniedException("Not authorized")))
        .onStatus(
            HttpStatus::is4xxClientError,
            cr -> Mono.just(new IllegalArgumentException(cr.statusCode().getReasonPhrase())))
        .onStatus(
            HttpStatus::is5xxServerError,
            cr -> Mono.just(new Exception(cr.statusCode().getReasonPhrase())))
        .bodyToMono(BookResource.class)
        .log()
        .then(Mono.just("redirect:/"));
  }

  @GetMapping("/borrow")
  Mono<String> borrowBook(@RequestParam("identifier") String identifier) {
    return webClient
        .post()
        .uri(libraryServer + "/books/{bookId}/borrow", identifier)
        .retrieve()
        .onStatus(
            s -> s.equals(HttpStatus.UNAUTHORIZED),
            cr -> Mono.just(new BadCredentialsException("Not authenticated")))
        .onStatus(
            s -> s.equals(HttpStatus.FORBIDDEN),
            cr -> Mono.just(new AccessDeniedException("Not authorized")))
        .onStatus(
            HttpStatus::is4xxClientError,
            cr -> Mono.just(new IllegalArgumentException(cr.statusCode().getReasonPhrase())))
        .onStatus(
            HttpStatus::is5xxServerError,
            cr -> Mono.just(new Exception(cr.statusCode().getReasonPhrase())))
        .bodyToMono(BookResource.class)
        .log()
        .then(Mono.just("redirect:/"));
  }

  @GetMapping("/return")
  Mono<String> returnBook(
      @RequestParam("identifier") String identifier, ServerWebExchange serverWebExchange) {
    return webClient
        .post()
        .uri(libraryServer + "/books/{bookId}/return", identifier)
        .retrieve()
        .onStatus(
            s -> s.equals(HttpStatus.UNAUTHORIZED),
            cr -> Mono.just(new BadCredentialsException("Not authenticated")))
        .onStatus(
            s -> s.equals(HttpStatus.FORBIDDEN),
            cr -> Mono.just(new AccessDeniedException("Not authorized")))
        .onStatus(
            HttpStatus::is4xxClientError,
            cr -> Mono.just(new IllegalArgumentException(cr.statusCode().getReasonPhrase())))
        .onStatus(
            HttpStatus::is5xxServerError,
            cr -> Mono.just(new Exception(cr.statusCode().getReasonPhrase())))
        .bodyToMono(BookResource.class)
        .then(Mono.just("redirect:/"));
  }
}
