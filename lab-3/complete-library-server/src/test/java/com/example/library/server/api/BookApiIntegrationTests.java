package com.example.library.server.api;

import com.example.library.server.business.BookService;
import com.example.library.server.config.IdGeneratorConfiguration;
import com.example.library.server.config.ModelMapperConfiguration;
import com.example.library.server.dataaccess.Book;
import com.example.library.server.dataaccess.BookBuilder;
import com.example.library.server.dataaccess.UserBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebFluxTest
@Import({
  BookResourceAssembler.class,
  ModelMapperConfiguration.class,
  IdGeneratorConfiguration.class
})
@WithMockUser
@DisplayName("Verify book api")
class BookApiIntegrationTests {

  @Autowired private ApplicationContext applicationContext;

  private WebTestClient webTestClient;

  @MockBean private BookService bookService;

  @BeforeEach
  void setUp(RestDocumentationContextProvider restDocumentation) {
    this.webTestClient =
        WebTestClient.bindToApplicationContext(applicationContext)
            .configureClient()
            .filter(
                documentationConfiguration(restDocumentation)
                    .operationPreprocessors()
                    .withRequestDefaults(prettyPrint())
                    .withResponseDefaults(prettyPrint()))
            .build();
  }

  @Test
  @DisplayName("to get list of books")
  void verifyAndDocumentGetBooks() {

    UUID bookId = UUID.randomUUID();
    UUID userId = UUID.randomUUID();

    given(bookService.findAll())
        .willReturn(
            Flux.just(
                BookBuilder.book()
                    .withId(bookId)
                    .withBorrowedBy(UserBuilder.user().withId(userId).build())
                    .build()));

    webTestClient
        .get()
        .uri("/books")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .json(
            "[{\"id\":\""
                + bookId
                + "\",\"isbn\":\"123-456789123\",\"title\":\"Book title\","
                + "\"description\":\"Book description\",\"authors\":[\"Author\"],\"borrowed\":true,"
                + "\"borrowedBy\":{\"id\":\""
                + userId
                + "\",\"email\":\"john.doe@example.com\","
                + "\"firstName\":\"John\",\"lastName\":\"Doe\"}}]\n")
        .consumeWith(
            document(
                "get-books", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())));
  }

  @Test
  @DisplayName("to get single book")
  void verifyAndDocumentGetBook() {

    UUID bookId = UUID.randomUUID();
    UUID userId = UUID.randomUUID();

    given(bookService.findById(bookId))
        .willReturn(
            Mono.just(
                BookBuilder.book()
                    .withId(bookId)
                    .withBorrowedBy(UserBuilder.user().withId(userId).build())
                    .build()));

    webTestClient
        .get()
        .uri("/books/{bookId}", bookId)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .json(
            "{\"id\":\""
                + bookId
                + "\",\"isbn\":\"123-456789123\",\"title\":\"Book title\","
                + "\"description\":\"Book description\",\"authors\":[\"Author\"],\"borrowed\":true,"
                + "\"borrowedBy\":{\"id\":\""
                + userId
                + "\",\"email\":\"john.doe@example.com\","
                + "\"firstName\":\"John\",\"lastName\":\"Doe\"}}")
        .consumeWith(
            document(
                "get-book", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())));
  }

  @Test
  @DisplayName("to delete a book")
  void verifyAndDocumentDeleteBook() {

    UUID bookId = UUID.randomUUID();
    given(bookService.deleteById(bookId)).willReturn(Mono.empty());

    webTestClient
        .mutateWith(csrf())
        .delete()
        .uri("/books/{bookId}", bookId)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .consumeWith(
            document(
                "delete-book",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
  }

  @Test
  @DisplayName("to borrow a book")
  void verifyAndDocumentBorrowBook() {

    UUID bookId = UUID.randomUUID();

    webTestClient
        .mutateWith(csrf())
        .post()
        .uri("/books/{bookId}/borrow", bookId)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .consumeWith(
            document(
                "borrow-book",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
  }

  @Test
  @DisplayName("to return a borrowed book")
  void verifyAndDocumentReturnBook() {

    UUID bookId = UUID.randomUUID();

    webTestClient
        .mutateWith(csrf())
        .post()
        .uri("/books/{bookId}/return", bookId)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .consumeWith(
            document(
                "return-book",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
  }

  @SuppressWarnings("unchecked")
  @Test
  @DisplayName("to create a new book")
  void verifyAndDocumentCreateBook() throws JsonProcessingException {

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
        .isCreated()
        .expectBody()
        .consumeWith(
            document(
                "create-book",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));

    ArgumentCaptor<Mono> bookArg = ArgumentCaptor.forClass(Mono.class);
    verify(bookService).create(bookArg.capture());

    assertThat(bookArg.getValue().block()).isNotNull().isEqualTo(expectedBook);
  }
}
