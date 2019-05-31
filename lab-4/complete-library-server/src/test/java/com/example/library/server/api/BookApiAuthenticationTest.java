package com.example.library.server.api;

import com.example.library.server.business.BookService;
import com.example.library.server.dataaccess.Book;
import com.example.library.server.dataaccess.BookBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockUser;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.springSecurity;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@DisplayName("Access to book api")
class BookApiAuthenticationTest {

  @Autowired private ApplicationContext applicationContext;

  private WebTestClient webTestClient;

  @MockBean private BookService bookService;

  @BeforeEach
  void setUp() {
    this.webTestClient =
        WebTestClient.bindToApplicationContext(applicationContext)
            .apply(springSecurity())
            .configureClient()
            .build();
  }

  @DisplayName("as authenticated user is granted")
  @Nested
  class AuthenticatedBookApi {

    @WithMockUser
    @Test
    @DisplayName("to get list of books")
    void verifyGetBooksAuthenticated() {

      given(bookService.findAll())
              .willReturn(
                      Flux.just(
                              BookBuilder.book().build()));

      webTestClient
              .get()
              .uri("/books")
              .accept(MediaType.APPLICATION_JSON)
              .exchange()
              .expectStatus()
              .isOk()
              .expectHeader().exists("X-XSS-Protection")
              .expectHeader().valueEquals("X-Frame-Options", "DENY");
    }

    @Test
    @DisplayName("to get single book")
    void verifyGetBookAuthenticated() {

      UUID bookId = UUID.randomUUID();

      given(bookService.findById(bookId))
              .willReturn(
                      Mono.just(
                              BookBuilder.book()
                                      .withId(bookId)
                                      .build()));

      webTestClient
              .mutateWith(mockUser())
              .get()
              .uri("/books/{bookId}", bookId)
              .accept(MediaType.APPLICATION_JSON)
              .exchange()
              .expectStatus()
              .isOk();
    }

    @Test
    @DisplayName("to delete a book")
    void verifyDeleteBookAuthenticated() {

      UUID bookId = UUID.randomUUID();
      given(bookService.deleteById(bookId)).willReturn(Mono.empty());

      webTestClient
              .mutateWith(mockUser().roles("LIBRARY_CURATOR"))
              .mutateWith(csrf())
              .delete()
              .uri("/books/{bookId}", bookId)
              .accept(MediaType.APPLICATION_JSON)
              .exchange()
              .expectStatus()
              .isOk();
    }

    @Test
    @DisplayName("to borrow a book")
    void verifyBorrowBookAuthenticated() {

      UUID bookId = UUID.randomUUID();

      webTestClient
              .mutateWith(mockUser().roles("LIBRARY_USER"))
              .mutateWith(csrf())
              .post()
              .uri("/books/{bookId}/borrow", bookId)
              .accept(MediaType.APPLICATION_JSON)
              .exchange()
              .expectStatus()
              .isOk()
              .expectBody();
    }

    @Test
    @DisplayName("to return a borrowed book")
    void verifyReturnBookAuthenticated() {

      UUID bookId = UUID.randomUUID();

      webTestClient
              .mutateWith(mockUser().roles("LIBRARY_USER"))
              .mutateWith(csrf())
              .post()
              .uri("/books/{bookId}/return", bookId)
              .accept(MediaType.APPLICATION_JSON)
              .exchange()
              .expectStatus()
              .isOk()
              .expectBody();
    }

    @Test
    @DisplayName("to create a new book")
    void verifyCreateBookAuthenticated() throws JsonProcessingException {

      UUID bookId = UUID.randomUUID();
      Book expectedBook = BookBuilder.book().withId(bookId).build();

      BookResource bookResource =
              new BookResource(
                      bookId,
                      expectedBook.getIsbn(),
                      expectedBook.getTitle(),
                      expectedBook.getDescription(),
                      expectedBook.getAuthors(),
                      expectedBook.isBorrowed(),
                      null);

      given(bookService.create(any())).willAnswer(b -> Mono.empty());

      webTestClient
              .mutateWith(mockUser().roles("LIBRARY_CURATOR"))
              .mutateWith(csrf())
              .post()
              .uri("/books")
              .accept(MediaType.APPLICATION_JSON)
              .contentType(MediaType.APPLICATION_JSON)
              .body(BodyInserters.fromObject(new ObjectMapper().writeValueAsString(bookResource)))
              .exchange()
              .expectStatus()
              .isCreated();
    }

  }

  @DisplayName("as unauthenticated user is denied with 401")
  @Nested
  class UnAuthenticatedBookApi {

    @Test
    @DisplayName("to get list of books")
    void verifyGetBooksUnAuthenticated() {

      webTestClient
              .get()
              .uri("/books")
              .accept(MediaType.APPLICATION_JSON)
              .exchange()
              .expectStatus()
              .isUnauthorized();
    }

    @Test
    @DisplayName("to get single book")
    void verifyGetBookUnAuthenticated() {

      UUID bookId = UUID.randomUUID();

      webTestClient
              .get()
              .uri("/books/{bookId}", bookId)
              .accept(MediaType.APPLICATION_JSON)
              .exchange()
              .expectStatus()
              .isUnauthorized();
    }

    @Test
    @DisplayName("to delete a book")
    void verifyDeleteBookUnAuthenticated() {

      UUID bookId = UUID.randomUUID();

      webTestClient
              .mutateWith(csrf())
              .delete()
              .uri("/books/{bookId}", bookId)
              .accept(MediaType.APPLICATION_JSON)
              .exchange()
              .expectStatus()
              .isUnauthorized();
    }

    @Test
    @DisplayName("to borrow a book")
    void verifyBorrowBookUnAuthenticated() {

      UUID bookId = UUID.randomUUID();

      webTestClient
              .mutateWith(csrf())
              .post()
              .uri("/books/{bookId}/borrow", bookId)
              .accept(MediaType.APPLICATION_JSON)
              .exchange()
              .expectStatus()
              .isUnauthorized();
    }

    @Test
    @DisplayName("to return a borrowed book")
    void verifyReturnBookUnAuthenticated() {

      UUID bookId = UUID.randomUUID();

      webTestClient
              .mutateWith(csrf())
              .post()
              .uri("/books/{bookId}/return", bookId)
              .accept(MediaType.APPLICATION_JSON)
              .exchange()
              .expectStatus()
              .isUnauthorized();
    }

    @Test
    @DisplayName("to create a new book")
    void verifyCreateBookUnAuthenticated() throws JsonProcessingException {

      UUID bookId = UUID.randomUUID();
      Book expectedBook = BookBuilder.book().withId(bookId).build();

      BookResource bookResource =
              new BookResource(
                      bookId,
                      expectedBook.getIsbn(),
                      expectedBook.getTitle(),
                      expectedBook.getDescription(),
                      expectedBook.getAuthors(),
                      expectedBook.isBorrowed(),
                      null);

      given(bookService.create(any())).willAnswer(b -> Mono.empty());

      webTestClient
              .mutateWith(csrf())
              .post()
              .uri("/books")
              .accept(MediaType.APPLICATION_JSON)
              .contentType(MediaType.APPLICATION_JSON)
              .body(BodyInserters.fromObject(new ObjectMapper().writeValueAsString(bookResource)))
              .exchange()
              .expectStatus()
              .isUnauthorized();
    }
  }
}
