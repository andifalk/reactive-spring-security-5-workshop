package com.example.library.server.business;

import com.example.library.server.OAuth2LibraryServerApplication;
import com.example.library.server.common.Role;
import com.example.library.server.dataaccess.Book;
import com.example.library.server.dataaccess.BookRepository;
import com.example.library.server.dataaccess.User;
import com.example.library.server.dataaccess.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.util.IdGenerator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Verify that book service")
@SpringJUnitConfig(OAuth2LibraryServerApplication.class)
class BookServiceTest {

    @Autowired
    private BookService bookService;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private IdGenerator idGenerator;

    @DisplayName("grants access to create a book for role 'CURATOR'")
    @Test
    @WithMockUser(roles = "CURATOR")
    void verifyCreateAccessIsGrantedForCurator() {
        when(bookRepository.insert(Mockito.<Mono<Book>>any())).thenReturn(Flux.just(new Book()));
        StepVerifier.create(bookService.create(Mono.just(new BookResource(UUID.randomUUID(),
                "123456789", "title", "description", Collections.singletonList("author"),
                false, null)
        ))).verifyComplete();
    }

    @DisplayName("denies access to create a book for roles 'USER' and 'ADMIN'")
    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    void verifyCreateAccessIsDeniedForUserAndAdmin() {
        StepVerifier.create(bookService.create(Mono.just(new BookResource(UUID.randomUUID(),
                "123456789", "title", "description", Collections.singletonList("author"),
                false, null)
        ))).verifyError(AccessDeniedException.class);
    }

    @DisplayName("denies access to create a book for anonymous user")
    @Test
    void verifyCreateAccessIsDeniedForUnauthenticated() {
        StepVerifier.create(bookService.create(Mono.just(new BookResource(UUID.randomUUID(),
                "123456789", "title", "description", Collections.singletonList("author"),
                false, null)
        ))).verifyError(AccessDeniedException.class);
    }

    @DisplayName("grants access to find a book by id for role 'USER'")
    @Test
    @WithMockUser
    void verifyFindByIdAccessIsGrantedForRoleUser() {
        when(bookRepository.findById(any(UUID.class))).thenReturn(Mono.just(new Book()));
        StepVerifier.create(bookService.findById(UUID.randomUUID())).expectNextCount(1).verifyComplete();
    }

    @DisplayName("grants access to find a book by id for role 'CURATOR'")
    @Test
    @WithMockUser(roles = "CURATOR")
    void verifyFindByIdAccessIsGrantedForRoleCurator() {
        when(bookRepository.findById(any(UUID.class))).thenReturn(Mono.just(new Book()));
        StepVerifier.create(bookService.findById(UUID.randomUUID())).expectNextCount(1).verifyComplete();
    }

    @DisplayName("grants access to find a book by id for role 'ADMIN'")
    @Test
    @WithMockUser(roles = "ADMIN")
    void verifyFindByIdAccessIsGrantedForRoleAdmin() {
        when(bookRepository.findById(any(UUID.class))).thenReturn(Mono.just(new Book()));
        StepVerifier.create(bookService.findById(UUID.randomUUID())).expectNextCount(1).verifyComplete();
    }

    @DisplayName("denies access to find a book by id for anonymous user")
    @Test
    void verifyFindByIdAccessIsDeniedForUnauthenticated() {
        StepVerifier.create(bookService.findById(UUID.randomUUID())).verifyError(AccessDeniedException.class);
    }

    @DisplayName("grants access to borrow a book by id for role 'USER'")
    @Test
    @WithMockUser
    void verifyBorrowByIdAccessIsGrantedForUser() {
        Book book = new Book(UUID.randomUUID(), "123456", "title", "description", Arrays.asList("author1", "author2"), false, null);
        when(bookRepository.findById(any(UUID.class))).thenReturn(Mono.just(book));
        when(bookRepository.save(any(Book.class))).thenReturn(Mono.just(book));
        when(userRepository.findById(any(UUID.class))).thenReturn(
                Mono.just(new User(UUID.randomUUID(), "test@example.com", "secret", "Max",
                        "Maier", Collections.singletonList(Role.USER))));
        StepVerifier.create(bookService.borrowById(UUID.randomUUID(), UUID.randomUUID())).verifyComplete();

        verify(bookRepository).save(any());
    }

    @DisplayName("denies access to borrow a book by id for roles 'CURATOR' or 'ADMIN'")
    @Test
    @WithMockUser(roles = {"CURATOR", "ADMIN"})
    void verifyBorrowByIdAccessIsDeniedForCuratorOrAdmin() {
        StepVerifier.create(bookService.borrowById(UUID.randomUUID(), UUID.randomUUID())).verifyError(AccessDeniedException.class);
    }

    @DisplayName("denies access to borrow a book by id for anonymous user")
    @Test
    void verifyBorrowByIdAccessIsDeniedForUnauthenticated() {
        StepVerifier.create(bookService.borrowById(UUID.randomUUID(), UUID.randomUUID())).verifyError(AccessDeniedException.class);
    }

    @DisplayName("grants access to return a book by id for role 'USER'")
    @Test
    @WithMockUser
    void verifyReturnByIdAccessIsGrantedForUser() {
        Book book = new Book(UUID.randomUUID(), "123456", "title", "description", Arrays.asList("author1", "author2"), false, null);
        when(bookRepository.findById(any(UUID.class))).thenReturn(Mono.just(book));
        when(bookRepository.save(any(Book.class))).thenReturn(Mono.just(book));
        when(userRepository.findById(any(UUID.class))).thenReturn(
                Mono.just(new User(UUID.randomUUID(), "test@example.com", "secret", "Max",
                        "Maier", Collections.singletonList(Role.USER))));
        StepVerifier.create(bookService.returnById(UUID.randomUUID(), UUID.randomUUID())).verifyComplete();
    }

    @DisplayName("denies access to return a book by id for roles 'CURATOR' or 'ADMIN'")
    @Test
    @WithMockUser(roles = {"CURATOR", "ADMIN"})
    void verifyReturnByIdAccessIsDeniedForCuratorOrAdmin() {
        StepVerifier.create(bookService.returnById(UUID.randomUUID(), UUID.randomUUID())).verifyError(AccessDeniedException.class);
    }

    @DisplayName("denies access to return a book by id for anonymous user")
    @Test
    void verifyReturnByIdAccessIsDeniedForUnauthenticated() {
        StepVerifier.create(bookService.returnById(UUID.randomUUID(), UUID.randomUUID())).verifyError(AccessDeniedException.class);
    }

    @DisplayName("grants access to find all books for role 'USER'")
    @Test
    @WithMockUser
    void verifyFindAllAccessIsGrantedForUser() {
        when(bookRepository.findAll()).thenReturn(Flux.just(new Book()));
        StepVerifier.create(bookService.findAll()).expectNextCount(1).verifyComplete();
    }

    @DisplayName("grants access to find all books for role 'CURATOR'")
    @Test
    @WithMockUser(roles = "CURATOR")
    void verifyFindAllAccessIsGrantedForCurator() {
        when(bookRepository.findAll()).thenReturn(Flux.just(new Book()));
        StepVerifier.create(bookService.findAll()).expectNextCount(1).verifyComplete();
    }

    @DisplayName("grants access to find all books for role 'ADMIN'")
    @Test
    @WithMockUser(roles = "ADMIN")
    void verifyFindAllAccessIsGrantedForAdmin() {
        when(bookRepository.findAll()).thenReturn(Flux.just(new Book()));
        StepVerifier.create(bookService.findAll()).expectNextCount(1).verifyComplete();
    }

    @DisplayName("denies access to find all books for anonymous user")
    @Test
    @WithAnonymousUser
    void verifyFindAllAccessIsDeniedForUnauthenticated() {
        StepVerifier.create(bookService.findAll()).verifyError(AccessDeniedException.class);
    }

    @DisplayName("grants access to delete a book for role 'CURATOR'")
    @Test
    @WithMockUser(roles = {"CURATOR"})
    void verifyDeleteByIdAccessIsGrantedForCurator() {
        when(bookRepository.deleteById(any(UUID.class))).thenReturn(Mono.empty());
        StepVerifier.create(bookService.deleteById(UUID.randomUUID())).verifyComplete();
    }

    @DisplayName("denies access to delete a book for role 'USER' and 'ADMIN'")
    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    void verifyDeleteByIdAccessIsDeniedForUserAndAdmin() {
        StepVerifier.create(bookService.deleteById(UUID.randomUUID())).verifyError(AccessDeniedException.class);
    }

    @DisplayName("denies access to delete a book for anonymous user")
    @Test
    void verifyDeleteByIdAccessIsDeniedForUnauthenticated() {
        StepVerifier.create(bookService.deleteById(UUID.randomUUID())).verifyError(AccessDeniedException.class);
    }

}