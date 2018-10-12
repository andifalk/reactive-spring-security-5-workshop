package com.example.library.server.api;

import com.example.library.server.business.BookService;
import com.example.library.server.business.UserResource;
import com.example.library.server.business.UserService;
import com.example.library.server.common.Role;
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
@WebMvcTest(UserRestController.class)
@AutoConfigureRestDocs
@DisplayName("Verify user api")
class UserApiIntegrationTests {

  @Autowired
  private MockMvc webClient;

  @MockBean
  private UserService userService;

  @MockBean
  private BookService bookService;

  @Test
  @DisplayName("to get list of users")
  @WithMockUser(roles = "ADMIN")
  void verifyAndDocumentGetUsers() throws Exception {

    UUID userId = UUID.randomUUID();
    given(userService.findAll())
        .willReturn(
            Collections.singletonList(
                new UserResource(
                    userId,
                    "test@example.com",
                    "first",
                    "last",
                    Collections.singletonList(Role.USER))));

    webClient
            .perform(
                    get("/users")
                            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8)
                            .accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(
                    document(
                            "get-users",
                            preprocessResponse(prettyPrint())));
  }

  @Test
  @DisplayName("to get single user")
  @WithMockUser(roles = "ADMIN")
  void verifyAndDocumentGetUser() throws Exception {

    UUID userId = UUID.randomUUID();

    given(userService.findById(userId))
        .willReturn(
                new UserResource(
                    userId,
                    "test@example.com",
                    "first",
                    "last",
                    Collections.singletonList(Role.USER)));

    webClient
            .perform(
                    get("/users/{userId}", userId)
                            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8)
                            .accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(
                    document(
                            "get-user",
                            preprocessResponse(prettyPrint())));
  }

  @Test
  @DisplayName("to delete a user")
  @WithMockUser(roles = "ADMIN")
  void verifyAndDocumentDeleteUser() throws Exception {

    UUID userId = UUID.randomUUID();

    webClient
            .perform(
                    delete("/users/{userId}", userId).with(csrf())
                            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8)
                            .accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE)))
            .andExpect(status().isNoContent())
            .andDo(
                    document(
                            "delete-user",
                            preprocessResponse(prettyPrint())));

    verify(userService).deleteById(eq(userId));
  }

  @SuppressWarnings("unchecked")
  @Test
  @DisplayName("to create a new user")
  @WithMockUser(roles = "ADMIN")
  void verifyAndDocumentCreateUser() throws Exception {

    UserResource userResource =
        new UserResource(
            null,
            "test@example.com",
            "first",
            "last",
            Collections.singletonList(Role.USER));

    webClient
            .perform(
                    post("/users").with(csrf())
                            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                            .content(new ObjectMapper().writeValueAsString(userResource))
                            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8)
                            .accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE)))
            .andExpect(status().isOk())
            .andDo(
                    document(
                            "create-user",
                            preprocessResponse(prettyPrint())));

  }
}
