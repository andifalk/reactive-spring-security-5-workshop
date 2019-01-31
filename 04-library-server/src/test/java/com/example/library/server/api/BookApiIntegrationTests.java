package com.example.library.server.api;

import com.example.library.server.business.BookResource;
import com.example.library.server.business.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

@ExtendWith(SpringExtension.class)
@WebFluxTest(BookRestController.class)
@AutoConfigureRestDocs
@DisplayName("Verify book api")
@WithMockUser
class BookApiIntegrationTests {

  @Autowired
  private WebTestClient webClient;

  @MockBean
  private BookService bookService;

  @Test
  @DisplayName("to get list of books")
  void verifyAndDocumentGetBooks() {

    UUID bookId = UUID.randomUUID();
    given(bookService.findAll())
        .willReturn(
            Flux.just(
                new BookResource(
                    bookId,
                    "1234566",
                    "title",
                    "description",
                    Collections.singletonList("Author"),
                    false,
                    null)));

    webClient
        .get()
        .uri("/books")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .json(
            "[{\"id\":\"" + bookId + "\",\"isbn\":\"1234566\",\"title\":\"title\",\"description\":\"description\",\"authors\":[\"Author\"],\"borrowed\":false,\"borrowedBy\":null}]")
        .consumeWith(document("get-books"));
  }

  @Test
  @DisplayName("to get single book")
  void verifyAndDocumentGetBook() {

    UUID bookId = UUID.randomUUID();
    given(bookService.findById(bookId))
        .willReturn(
            Mono.just(
                new BookResource(
                    bookId,
                    "1234566",
                    "title",
                    "description",
                    Collections.singletonList("Author"),
                    false,
                    null)));

    webClient
            .get().uri("/books/{bookId}", bookId).accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody()
            .json(
                    "{\"id\":\"" + bookId + "\",\"isbn\":\"1234566\",\"title\":\"title\",\"description\":\"description\",\"authors\":[\"Author\"],\"borrowed\":false,\"borrowedBy\":null}")
            .consumeWith(document("get-book"));
  }

  @Test
  @DisplayName("to delete a book")
  void verifyAndDocumentDeleteBook() {

    UUID bookId = UUID.randomUUID();
    given(bookService.deleteById(bookId)).willReturn(Mono.empty());

    webClient
            .mutateWith(csrf())
            .delete().uri("/books/{bookId}", bookId).accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody()
            .consumeWith(document("delete-book"));

    verify(bookService).deleteById(eq(bookId));
  }

  @Test
  @DisplayName("to borrow a book")
  void verifyAndDocumentBorrowBook() {

    UUID bookId = UUID.randomUUID();

    webClient
            .mutateWith(csrf())
            .post().uri("/books/{bookId}/borrow", bookId).accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody()
            .consumeWith(document("borrow-book"));

    verify(bookService).borrowById(any(), any());
  }

  @Test
  @DisplayName("to return a borrowed book")
  void verifyAndDocumentReturnBook() {

    UUID bookId = UUID.randomUUID();

    webClient
            .mutateWith(csrf())
            .post().uri("/books/{bookId}/return", bookId).accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody()
            .consumeWith(document("return-book"));

    verify(bookService).returnById(any(), any());
  }

  @SuppressWarnings("unchecked")
  @Test
  @DisplayName("to create a new book")
  void verifyAndDocumentCreateBook() throws JsonProcessingException {

    BookResource bookResource =
        new BookResource(
            null,
            "1234566",
            "title",
            "description",
            Collections.singletonList("Author"),
            false,
            null);

    given(bookService.create(any())).willAnswer(
      i -> {
        ((Mono<BookResource>) i.getArgument(0)).subscribe();
        return Mono.empty();
      }
    );

    webClient
            .mutateWith(csrf())
            .post().uri("/books").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromObject(
                    new ObjectMapper().writeValueAsString(bookResource)))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody().consumeWith(document("create-book"));
  }
}
