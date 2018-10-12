package com.example.library.server.business;

import com.example.library.server.dataaccess.Book;
import com.example.library.server.dataaccess.BookRepository;
import com.example.library.server.dataaccess.User;
import com.example.library.server.dataaccess.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.IdGenerator;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@PreAuthorize("hasAnyRole('USER', 'CURATOR', 'ADMIN')")
public class BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final IdGenerator idGenerator;

    @Autowired
    public BookService(BookRepository bookRepository, UserRepository userRepository, IdGenerator idGenerator) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.idGenerator = idGenerator;
    }

    @PreAuthorize("hasRole('CURATOR')")
    public void create(BookResource bookResource) {
        bookRepository.insert(this.convert(bookResource));
    }

    public BookResource findById(UUID uuid) {
        return bookRepository.findById(uuid).map(this::convert).orElse(null);
    }

    @PreAuthorize("hasRole('USER')")
    public void borrowById(UUID uuid, UUID userId) {

        if (uuid == null || userId == null) {
            return;
        }

        Optional<Book> book = bookRepository.findById(uuid);
        if (book.isPresent()) {
            Optional<User> user = userRepository.findById(userId);
            if (user.isPresent()) {
                book.get().doBorrow(user.get());
                bookRepository.save(book.get());
            }
        }
    }

    @PreAuthorize("hasRole('USER')")
    public void returnById(UUID uuid, UUID userId) {

        if (uuid == null || userId == null) {
            return;
        }

        Optional<Book> book = bookRepository.findById(uuid);
        if (book.isPresent()) {
            Optional<User> user = userRepository.findById(userId);
            if (user.isPresent()) {
                book.get().doReturn(user.get());
                bookRepository.save(book.get());
            }
        }
    }

    public List<BookResource> findAll() {
        return bookRepository.findAll()
                .stream().map(this::convert).collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('CURATOR')")
    public void deleteById(UUID uuid) {
        bookRepository.deleteById(uuid);
    }

    private Book convert(BookResource br) {
        UserResource borrowedBy = br.getBorrowedBy();
        return new Book(
                idGenerator.generateId(), br.getIsbn(), br.getTitle(), br.getDescription(),
                br.getAuthors(), br.isBorrowed(),
                borrowedBy != null ? new User(borrowedBy.getId(), borrowedBy.getEmail(),
                        borrowedBy.getFirstName(), borrowedBy.getLastName(), borrowedBy.getRoles()) : null);
    }

    private BookResource convert(Book b) {
        User borrowedBy = b.getBorrowedBy();
        return new BookResource(b.getId(), b.getIsbn(), b.getTitle(), b.getDescription(),
                b.getAuthors(), b.isBorrowed(), borrowedBy != null ? new UserResource(borrowedBy.getId(), borrowedBy.getEmail(),
                borrowedBy.getFirstName(), borrowedBy.getLastName(), borrowedBy.getRoles()) : null);
    }
}
