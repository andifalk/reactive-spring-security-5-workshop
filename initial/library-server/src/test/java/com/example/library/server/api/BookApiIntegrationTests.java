package com.example.library.server.api;

import com.example.library.server.business.BookResource;
import com.example.library.server.business.BookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

@ExtendWith(SpringExtension.class)
@WebFluxTest(BookRestController.class)
@AutoConfigureRestDocs
class BookApiIntegrationTests {

  @Autowired
  private WebTestClient webClient;

  @MockBean
  private BookService bookService;

  @Test
  @DisplayName("Verify get request for books")
  void verifyAndDocumentGetBooks() {

    UUID bookId = UUID.randomUUID();
    given(bookService.findAll()).willReturn(Flux.just(
            new BookResource(bookId, "1234566", "title", "description", List.of("Author"), null)));

    webClient
        .get().uri("/books").accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .json(
            "[{\"id\":\"" + bookId + "\",\"isbn\":\"1234566\",\"title\":\"title\",\"description\":\"description\",\"authors\":[\"Author\"],\"borrowed\":null}]")
        .consumeWith(document("get-books"));
  }
}
