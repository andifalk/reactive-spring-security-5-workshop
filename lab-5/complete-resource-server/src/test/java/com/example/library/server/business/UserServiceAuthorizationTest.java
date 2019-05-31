package com.example.library.server.business;

import com.example.library.server.CompleteResourceServerApplication;
import com.example.library.server.common.Role;
import com.example.library.server.dataaccess.User;
import com.example.library.server.dataaccess.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("Verify that user service")
@SpringJUnitConfig(CompleteResourceServerApplication.class)
class UserServiceAuthorizationTest {

  @Autowired private UserService userService;

  @MockBean private UserRepository userRepository;

  @MockBean private ReactiveJwtDecoder reactiveJwtDecoder;

  @DisplayName("grants access to find one user by email for anonymous user")
  @Test
  void verifyFindOneByEmailAccessIsGrantedForUnauthenticated() {
    when(userRepository.findOneByEmail(any()))
        .thenReturn(
            Mono.just(
                new User(
                    UUID.randomUUID(),
                    "test@example.com",
                    "Max",
                    "Maier",
                    Collections.singletonList(Role.LIBRARY_USER))));
    StepVerifier.create(userService.findOneByEmail("test@example.com"))
        .expectNextCount(1)
        .verifyComplete();
  }

  @DisplayName(
      "grants access to find one user by email for roles 'LIBRARY_USER', 'LIBRARY_CURATOR' and 'LIBRARY_ADMIN'")
  @Test
  @WithMockUser(roles = {"LIBRARY_USER", "LIBRARY_CURATOR", "LIBRARY_ADMIN"})
  void verifyFindOneByEmailAccessIsGrantedForAllRoles() {
    when(userRepository.findOneByEmail(any()))
        .thenReturn(
            Mono.just(
                new User(
                    UUID.randomUUID(),
                    "test@example.com",
                    "Max",
                    "Maier",
                    Collections.singletonList(Role.LIBRARY_USER))));
    StepVerifier.create(userService.findOneByEmail("test@example.com"))
        .expectNextCount(1)
        .verifyComplete();
  }

  @DisplayName("grants access to create a user for role 'LIBRARY_ADMIN'")
  @Test
  @WithMockUser(roles = "LIBRARY_ADMIN")
  void verifyCreateAccessIsGrantedForAdmin() {
    when(userRepository.insert(Mockito.<Mono<User>>any()))
        .thenReturn(
            Flux.just(
                new User(
                    UUID.randomUUID(),
                    "test@example.com",
                    "Max",
                    "Maier",
                    Collections.singletonList(Role.LIBRARY_USER))));
    StepVerifier.create(
            userService.create(
                Mono.just(
                    new User(
                        UUID.randomUUID(),
                        "test@example.com",
                        "Max",
                        "Maier",
                        Collections.singletonList(Role.LIBRARY_USER)))))
        .verifyComplete();
  }

  @DisplayName("denies access to create a user for roles 'LIBRARY_USER' and 'LIBRARY_CURATOR'")
  @Test
  @WithMockUser(roles = {"LIBRARY_USER", "LIBRARY_CURATOR"})
  void verifyCreateAccessIsDeniedForUserAndCurator() {
    StepVerifier.create(
            userService.create(
                Mono.just(
                    new User(
                        UUID.randomUUID(),
                        "test@example.com",
                        "Max",
                        "Maier",
                        Collections.singletonList(Role.LIBRARY_USER)))))
        .verifyError(AccessDeniedException.class);
  }

  @DisplayName("denies access to create a user for anonymous user")
  @Test
  void verifyCreateAccessIsDeniedForUnauthenticated() {
    StepVerifier.create(
            userService.create(
                Mono.just(
                    new User(
                        UUID.randomUUID(),
                        "test@example.com",
                        "Max",
                        "Maier",
                        Collections.singletonList(Role.LIBRARY_USER)))))
        .verifyError(AccessDeniedException.class);
  }

  @DisplayName("grants access to find a user by id for role 'LIBRARY_ADMIN'")
  @Test
  @WithMockUser(roles = "LIBRARY_ADMIN")
  void verifyFindByIdAccessIsGrantedForAdmin() {
    when(userRepository.findById(any(UUID.class)))
        .thenReturn(
            Mono.just(
                new User(
                    UUID.randomUUID(),
                    "test@example.com",
                    "Max",
                    "Maier",
                    Collections.singletonList(Role.LIBRARY_USER))));
    StepVerifier.create(userService.findById(UUID.randomUUID()))
        .expectNextCount(1)
        .verifyComplete();
  }

  @DisplayName("denies access to find a user by id for roles 'LIBRARY_USER' and 'LIBRARY_CURATOR'")
  @Test
  @WithMockUser(roles = {"LIBRARY_USER", "LIBRARY_CURATOR"})
  void verifyFindByIdAccessIsDeniedForUserAndCurator() {
    StepVerifier.create(userService.findById(UUID.randomUUID()))
        .verifyError(AccessDeniedException.class);
  }

  @DisplayName("denies access to find a user by id for anonymous user")
  @Test
  void verifyFindByIdAccessIsDeniedForUnauthenticated() {
    StepVerifier.create(userService.findById(UUID.randomUUID()))
        .verifyError(AccessDeniedException.class);
  }

  @DisplayName("grants access to find all users for role 'LIBRARY_ADMIN'")
  @Test
  @WithMockUser(roles = "LIBRARY_ADMIN")
  void verifyFindAllAccessIsGrantedForAdmin() {
    when(userRepository.findAll())
        .thenReturn(
            Flux.just(
                new User(
                    UUID.randomUUID(),
                    "test@example.com",
                    "Max",
                    "Maier",
                    Collections.singletonList(Role.LIBRARY_USER))));
    StepVerifier.create(userService.findAll()).expectNextCount(1).verifyComplete();
  }

  @DisplayName("denies access to find all users for roles 'LIBRARY_USER' and 'LIBRARY_CURATOR'")
  @Test
  @WithMockUser(roles = {"LIBRARY_USER", "LIBRARY_CURATOR"})
  void verifyFindAllAccessIsDeniedForUserAndCurator() {
    StepVerifier.create(userService.findAll()).verifyError(AccessDeniedException.class);
  }

  @DisplayName("denies access to find all users for anonymous user")
  @Test
  void verifyFindAllAccessIsDeniedForUnauthenticated() {
    StepVerifier.create(userService.findAll()).verifyError(AccessDeniedException.class);
  }

  @DisplayName("grants access to delete a user for role 'LIBRARY_ADMIN'")
  @Test
  @WithMockUser(roles = "LIBRARY_ADMIN")
  void verifyDeleteByIdAccessIsGrantedForAdmin() {
    when(userRepository.deleteById(any(UUID.class))).thenReturn(Mono.empty());
    StepVerifier.create(userService.deleteById(UUID.randomUUID())).verifyComplete();
  }

  @DisplayName("denies access to delete a user for roles 'LIBRARY_USER' and 'LIBRARY_CURATOR'")
  @Test
  @WithMockUser(roles = {"LIBRARY_USER", "LIBRARY_CURATOR"})
  void verifyDeleteByIdAccessIsDeniedForUserAndCurator() {
    StepVerifier.create(userService.deleteById(UUID.randomUUID()))
        .verifyError(AccessDeniedException.class);
  }

  @DisplayName("denies access to delete a user for anonymous user")
  @Test
  void verifyDeleteByIdAccessIsDeniedForUnauthenticated() {
    StepVerifier.create(userService.deleteById(UUID.randomUUID()))
        .verifyError(AccessDeniedException.class);
  }
}
