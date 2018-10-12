package com.example.library.server.api;

import com.example.library.server.business.BookResource;
import com.example.library.server.business.BookService;
import com.example.library.server.security.WithMockLibraryUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookRestController.class)
@AutoConfigureRestDocs
@DisplayName("Verify book api")
class BookApiIntegrationTests {

  @Autowired
  private MockMvc webClient;

  @MockBean
  private BookService bookService;

  @Test
  @DisplayName("to get list of books")
  @WithMockUser
  void verifyAndDocumentGetBooks() throws Exception {

    UUID bookId = UUID.randomUUID();
    given(bookService.findAll())
        .willReturn(
                Collections.singletonList(
                new BookResource(
                    bookId,
                    "1234566",
                    "title",
                    "description",
                    Collections.singletonList("Author"),
                    false,
                    null)));

    webClient
            .perform(
                    get("/books")
                            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8)
                            .accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(
                    document(
                            "get-books",
                            preprocessResponse(prettyPrint())));
  }

  @Test
  @DisplayName("to get single book")
  @WithMockUser
  void verifyAndDocumentGetBook() throws Exception {

    UUID bookId = UUID.randomUUID();
    given(bookService.findById(bookId))
        .willReturn(
            new BookResource(
                    bookId,
                    "1234566",
                    "title",
                    "description",
                    Collections.singletonList("Author"),
                    false,
                    null));

    webClient
            .perform(
                    get("/books/{bookId}", bookId)
                            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8)
                            .accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(
                    document(
                            "get-book",
                            preprocessResponse(prettyPrint())));
  }

  @Test
  @DisplayName("to delete a book")
  @WithMockUser
  void verifyAndDocumentDeleteBook() throws Exception {

    UUID bookId = UUID.randomUUID();

    webClient
            .perform(
                    delete("/books/{bookId}", bookId).with(csrf())
                            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8)
                            .accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE)))
            .andExpect(status().isOk())
            .andDo(
                    document(
                            "delete-book",
                            preprocessResponse(prettyPrint())));

    verify(bookService).deleteById(eq(bookId));
  }

  @Test
  @DisplayName("to borrow a book")
  @WithMockLibraryUser
  void verifyAndDocumentBorrowBook() throws Exception {

    UUID bookId = UUID.randomUUID();

    webClient
            .perform(
                    post("/books/{bookId}/borrow", bookId).with(csrf())
                            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8)
                            .accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE)))
            .andExpect(status().isOk())
            .andDo(
                    document(
                            "borrow-book",
                            preprocessResponse(prettyPrint())));

    verify(bookService).borrowById(any(), any());
  }

  @Test
  @DisplayName("to return a borrowed book")
  @WithMockLibraryUser
  void verifyAndDocumentReturnBook() throws Exception {

    UUID bookId = UUID.randomUUID();

    webClient
            .perform(
                    post("/books/{bookId}/return", bookId).with(csrf())
                            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8)
                            .accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE)))
            .andExpect(status().isOk())
            .andDo(
                    document(
                            "return-book",
                            preprocessResponse(prettyPrint())));

    verify(bookService).returnById(any(), any());
  }

  @SuppressWarnings("unchecked")
  @Test
  @DisplayName("to create a new book")
  @WithMockUser
  void verifyAndDocumentCreateBook() throws Exception {

    BookResource bookResource =
        new BookResource(
            null,
            "1234566",
            "title",
            "description",
            Collections.singletonList("Author"),
            false,
            null);

    webClient
            .perform(
                    post("/books").with(csrf())
                            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                            .content(new ObjectMapper().writeValueAsString(bookResource))
                            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8)
                            .accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE)))
            .andExpect(status().isOk())
            .andDo(
                    document(
                            "create-book",
                            preprocessResponse(prettyPrint())));
  }
}
