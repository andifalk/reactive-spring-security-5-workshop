package com.example.oidc.client.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Controller
@RequestMapping("/books")
public class BookController {

  private static final Logger LOGGER = LoggerFactory.getLogger(BookController.class);

  private final WebClient webClient;

  @Value("${library.server}")
  private String libraryServer;

  public BookController(WebClient webClient) {
    this.webClient = webClient;
  }

  @GetMapping("/")
  Mono<String> index(@AuthenticationPrincipal User user, Model model) {

    model.addAttribute("fullname", user.getUsername());
    model.addAttribute(
        "isCurator",
        user.getAuthorities().stream().anyMatch(ga -> ga.getAuthority().equals("library_curator")));
    return webClient
        .get()
        .uri(libraryServer + "/books")
        .retrieve()
        .onStatus(
            s -> s.equals(HttpStatus.UNAUTHORIZED),
            cr -> Mono.just(new BadCredentialsException("Not authenticated")))
        .onStatus(
            HttpStatus::is4xxClientError,
            cr -> Mono.just(new IllegalArgumentException(cr.statusCode().getReasonPhrase())))
        .onStatus(
            HttpStatus::is5xxServerError,
            cr -> Mono.just(new Exception(cr.statusCode().getReasonPhrase())))
        .bodyToFlux(BookResource.class)
        .log()
        .collectList()
        .map(
            c -> {
              model.addAttribute("books", c);
              return "index";
            });
  }

  @GetMapping("/createbook")
  String createForm(Model model) {

    model.addAttribute("book", new CreateBookResource());

    return "createbookform";
  }

  @PostMapping("/create")
  Mono<ResponseEntity<String>> create(
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
            HttpStatus::is4xxClientError,
            cr -> Mono.just(new IllegalArgumentException(cr.statusCode().getReasonPhrase())))
        .onStatus(
            HttpStatus::is5xxServerError,
            cr -> Mono.just(new Exception(cr.statusCode().getReasonPhrase())))
        .bodyToMono(BookResource.class)
        .log()
        .then(Mono.just(ResponseEntity.ok().body("OK")));
  }

  @GetMapping("/borrow")
  String borrowBook(@RequestParam("identifier") String identifier) throws IOException {
    webClient
        .post()
        .uri(libraryServer + "/books/{bookId}/borrow", identifier)
        .retrieve()
        .onStatus(
            s -> s.equals(HttpStatus.UNAUTHORIZED),
            cr -> Mono.just(new BadCredentialsException("Not authenticated")))
        .onStatus(
            HttpStatus::is4xxClientError,
            cr -> Mono.just(new IllegalArgumentException(cr.statusCode().getReasonPhrase())))
        .onStatus(
            HttpStatus::is5xxServerError,
            cr -> Mono.just(new Exception(cr.statusCode().getReasonPhrase())))
        .bodyToMono(BookResource.class)
        .log()
        .block();

    // response.sendRedirect(request.getContextPath());
    return null;
  }

  @GetMapping("/return")
  String returnBook(@RequestParam("identifier") String identifier) throws IOException {
    webClient
        .post()
        .uri(libraryServer + "/books/{bookId}/return", identifier)
        .retrieve()
        .onStatus(
            s -> s.equals(HttpStatus.UNAUTHORIZED),
            cr -> Mono.just(new BadCredentialsException("Not authenticated")))
        .onStatus(
            HttpStatus::is4xxClientError,
            cr -> Mono.just(new IllegalArgumentException(cr.statusCode().getReasonPhrase())))
        .onStatus(
            HttpStatus::is5xxServerError,
            cr -> Mono.just(new Exception(cr.statusCode().getReasonPhrase())))
        .bodyToMono(BookResource.class)
        .log()
        .block();

    // response.sendRedirect(request.getContextPath());
    return null;
  }
}
