package com.example.library.server.api;

import com.example.library.server.business.BookService;
import com.example.library.server.business.UserService;
import com.example.library.server.config.IdGeneratorConfiguration;
import com.example.library.server.config.ModelMapperConfiguration;
import com.example.library.server.config.UserRouter;
import com.example.library.server.dataaccess.User;
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
  UserRouter.class,
  UserHandler.class,
  BookResourceAssembler.class,
  UserResourceAssembler.class,
  ModelMapperConfiguration.class,
  IdGeneratorConfiguration.class
})
@WithMockUser
@DisplayName("Verify user api")
class UserApiIntegrationTests {

  @Autowired private ApplicationContext applicationContext;

  private WebTestClient webTestClient;

  @MockBean private UserService userService;

  @SuppressWarnings("unused")
  @MockBean
  private BookService bookService;

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
  @DisplayName("to get list of users")
  void verifyAndDocumentGetUsers() {

    UUID userId = UUID.randomUUID();
    given(userService.findAll()).willReturn(Flux.just(UserBuilder.user().withId(userId).build()));

    webTestClient
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
                + "\",\"email\":\"john.doe@example.com\","
                + "\"firstName\":\"John\",\"lastName\":\"Doe\"}]")
        .consumeWith(
            document(
                "get-users", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())));
  }

  @Test
  @DisplayName("to get single user")
  void verifyAndDocumentGetUser() {

    UUID userId = UUID.randomUUID();

    given(userService.findById(userId))
        .willReturn(Mono.just(UserBuilder.user().withId(userId).build()));

    webTestClient
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
                + "\",\"email\":\"john.doe@example.com\","
                + "\"firstName\":\"John\",\"lastName\":\"Doe\","
                + "\"roles\":[\"LIBRARY_USER\"]}")
        .consumeWith(
            document(
                "get-user", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())));
  }

  @Test
  @DisplayName("to delete a user")
  void verifyAndDocumentDeleteUser() {

    UUID userId = UUID.randomUUID();
    given(userService.deleteById(userId)).willReturn(Mono.empty());

    webTestClient
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

    UUID userId = UUID.randomUUID();

    User expectedUser = UserBuilder.user().withId(userId).build();

    UserResource userResource =
        new CreateUserResource(
            userId,
            expectedUser.getEmail(),
            expectedUser.getPassword(),
            expectedUser.getFirstName(),
            expectedUser.getLastName(),
            expectedUser.getRoles());

    given(userService.create(any())).willAnswer(i -> Mono.empty());

    webTestClient
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

    ArgumentCaptor<Mono> userArg = ArgumentCaptor.forClass(Mono.class);
    verify(userService).create(userArg.capture());

    assertThat(userArg.getValue().block()).isNotNull().isEqualTo(expectedUser);
  }
}
