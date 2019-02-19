package com.example.library.server.api;

import com.example.library.server.business.BookService;
import com.example.library.server.business.UserResource;
import com.example.library.server.business.UserService;
import com.example.library.server.common.Role;
import com.example.library.server.config.UserRouter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
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
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

@ExtendWith(SpringExtension.class)
@WebFluxTest
@Import({UserHandler.class, UserRouter.class})
@AutoConfigureRestDocs
@DisplayName("Verify user api")
@WithMockUser
class UserApiIntegrationTests {

  @Autowired private WebTestClient webClient;

  @MockBean private UserService userService;

  @MockBean private BookService bookService;

  @Test
  @DisplayName("to get list of users")
  void verifyAndDocumentGetUsers() {

    UUID userId = UUID.randomUUID();
    given(userService.findAll())
        .willReturn(
            Flux.just(
                new UserResource(
                    userId,
                    "test@example.com",
                    "first",
                    "last",
                    Collections.singletonList(Role.USER))));

    webClient
        .get()
        .uri("/users")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .json(
            "[{\"id\":\""
                + userId
                + "\",\"email\":\"test@example.com\",\"firstName\":\"first\",\"lastName\":\"last\"}]")
        .consumeWith(
            document(
                "get-users", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())));
  }

  @Test
  @DisplayName("to get single user")
  void verifyAndDocumentGetUser() {

    UUID userId = UUID.randomUUID();

    given(userService.findById(userId))
        .willReturn(
            Mono.just(
                new UserResource(
                    userId,
                    "test@example.com",
                    "first",
                    "last",
                    Collections.singletonList(Role.USER))));

    webClient
        .get()
        .uri("/users/{userId}", userId)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .json(
            "{\"id\":\""
                + userId
                + "\",\"email\":\"test@example.com\",\"firstName\":\"first\",\"lastName\":\"last\"}")
        .consumeWith(
            document(
                "get-user", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())));
  }

  @Test
  @DisplayName("to delete a user")
  void verifyAndDocumentDeleteUser() {

    UUID userId = UUID.randomUUID();
    given(userService.deleteById(userId)).willReturn(Mono.empty());

    webClient
        .mutateWith(csrf())
        .delete()
        .uri("/users/{userId}", userId)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .consumeWith(
            document(
                "delete-user",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
  }

  @SuppressWarnings("unchecked")
  @Test
  @DisplayName("to create a new user")
  void verifyAndDocumentCreateUser() throws JsonProcessingException {

    UserResource userResource =
        new UserResource(
            UUID.randomUUID(),
            "test@example.com",
            "first",
            "last",
            Collections.singletonList(Role.USER));

    given(userService.create(any()))
        .willAnswer(
            i -> {
              ((Mono<UserResource>) i.getArgument(0)).subscribe();
              return Mono.empty();
            });

    webClient
        .mutateWith(csrf())
        .post()
        .uri("/users")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromObject(new ObjectMapper().writeValueAsString(userResource)))
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .consumeWith(
            document(
                "create-user",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
  }
}
