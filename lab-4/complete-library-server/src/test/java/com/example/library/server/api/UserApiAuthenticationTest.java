package com.example.library.server.api;

import com.example.library.server.business.BookService;
import com.example.library.server.business.UserService;
import com.example.library.server.dataaccess.User;
import com.example.library.server.dataaccess.UserBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoReactiveRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.config.EnableWebFlux;
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
@SpringBootTest(classes = UserApiAuthenticationTest.TestConfig.class)
@DisplayName("Access to user api")
class UserApiAuthenticationTest {

  @ComponentScan(
          basePackages = {
                  "com.example.library.server.api",
                  "com.example.library.server.business",
                  "com.example.library.server.config"
          })
  @EnableWebFlux
  @EnableWebFluxSecurity
  @EnableAutoConfiguration(
          exclude = {
                  MongoReactiveAutoConfiguration.class,
                  MongoAutoConfiguration.class,
                  MongoDataAutoConfiguration.class,
                  EmbeddedMongoAutoConfiguration.class,
                  MongoReactiveRepositoriesAutoConfiguration.class,
                  MongoRepositoriesAutoConfiguration.class
          })
  static class TestConfig {}

  @Autowired private ApplicationContext applicationContext;

  private WebTestClient webTestClient;

  @MockBean private UserService userService;

  @SuppressWarnings("unused")
  @MockBean
  private BookService bookService;

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
  class AuthenticatedUserApi {

    @Test
    @DisplayName("to get list of users")
    void verifyGetUsersAuthenticated() {

      UUID userId = UUID.randomUUID();
      given(userService.findAll()).willReturn(Flux.just(UserBuilder.user().withId(userId).build()));

      webTestClient
          .mutateWith(mockUser().roles("LIBRARY_ADMIN"))
          .get()
          .uri("/users")
          .accept(MediaType.APPLICATION_JSON)
          .exchange()
          .expectStatus()
          .isOk();
    }

    @Test
    @DisplayName("to get single user")
    void verifyGetUserAuthenticated() {

      UUID userId = UUID.randomUUID();

      given(userService.findById(userId))
          .willReturn(Mono.just(UserBuilder.user().withId(userId).build()));

      webTestClient
          .mutateWith(mockUser().roles("LIBRARY_ADMIN"))
          .get()
          .uri("/users/{userId}", userId)
          .accept(MediaType.APPLICATION_JSON)
          .exchange()
          .expectStatus()
          .isOk();
    }

    @Test
    @DisplayName("to delete a user")
    void verifyDeleteUserAuthenticated() {

      UUID userId = UUID.randomUUID();
      given(userService.deleteById(userId)).willReturn(Mono.empty());

      webTestClient
          .mutateWith(mockUser().roles("LIBRARY_ADMIN"))
          .mutateWith(csrf())
          .delete()
          .uri("/users/{userId}", userId)
          .accept(MediaType.APPLICATION_JSON)
          .exchange()
          .expectStatus()
          .isOk();
    }

    @Test
    @DisplayName("to create a new user")
    void verifyCreateUserAuthenticated() throws JsonProcessingException {

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
          .mutateWith(mockUser().roles("LIBRARY_ADMIN"))
          .mutateWith(csrf())
          .post()
          .uri("/users")
          .accept(MediaType.APPLICATION_JSON)
          .contentType(MediaType.APPLICATION_JSON)
          .body(BodyInserters.fromObject(new ObjectMapper().writeValueAsString(userResource)))
          .exchange()
          .expectStatus()
          .isOk();
    }
  }

  @DisplayName("as unauthenticated user is denied")
  @Nested
  class UnAuthenticatedUserApi {

    @Test
    @DisplayName("to get list of users")
    void verifyGetUsersUnAuthenticated() {

      webTestClient
          .get()
          .uri("/users")
          .accept(MediaType.APPLICATION_JSON)
          .exchange()
          .expectStatus()
          .isUnauthorized();
    }

    @Test
    @DisplayName("to get single user")
    void verifyGetUserUnAuthenticated() {

      UUID userId = UUID.randomUUID();

      webTestClient
          .get()
          .uri("/users/{userId}", userId)
          .accept(MediaType.APPLICATION_JSON)
          .exchange()
          .expectStatus()
          .isUnauthorized();
    }

    @Test
    @DisplayName("to delete a user")
    void verifyDeleteUserUnAuthenticated() {

      UUID userId = UUID.randomUUID();

      webTestClient
          .mutateWith(csrf())
          .delete()
          .uri("/users/{userId}", userId)
          .accept(MediaType.APPLICATION_JSON)
          .exchange()
          .expectStatus()
          .isUnauthorized();
    }

    @Test
    @DisplayName("to create a new user")
    void verifyCreateUserUnAuthenticated() throws JsonProcessingException {

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

      webTestClient
          .mutateWith(csrf())
          .post()
          .uri("/users")
          .accept(MediaType.APPLICATION_JSON)
          .contentType(MediaType.APPLICATION_JSON)
          .body(BodyInserters.fromObject(new ObjectMapper().writeValueAsString(userResource)))
          .exchange()
          .expectStatus()
          .isUnauthorized();
    }
  }
}
