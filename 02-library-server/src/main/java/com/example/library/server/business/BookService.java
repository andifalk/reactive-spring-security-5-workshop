package com.example.library.server.business;

import com.example.library.server.dataaccess.Book;
import com.example.library.server.dataaccess.BookRepository;
import com.example.library.server.dataaccess.User;
import com.example.library.server.security.LibraryUser;
import org.modelmapper.ModelMapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.util.IdGenerator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final IdGenerator idGenerator;
    private final ModelMapper modelMapper;

    @Autowired
    public BookService(BookRepository bookRepository, IdGenerator idGenerator, ModelMapper modelMapper) {
        this.bookRepository = bookRepository;
        this.idGenerator = idGenerator;
        this.modelMapper = modelMapper;
    }

    public Mono<Void> create(Mono<BookResource> bookResource) {

        Mono<Book> book = bookResource.map(
                br -> new Book(
                        idGenerator.generateId(), br.getIsbn(), br.getTitle(), br.getDescription(),
                        br.getAuthors(), br.isBorrowed(), br.getBorrowedBy() != null ? modelMapper.map(br.getBorrowedBy(), User.class) : null))
                .doOnError(e -> LoggerFactory.getLogger(getClass()).error("Error: " + e.getMessage()));
        return bookRepository.insert(book).then();
    }

    public Mono<BookResource> findById(UUID uuid) {
        return bookRepository.findById(uuid).map(
                book -> modelMapper.map(book, BookResource.class));
    }

    public void borrowById(UUID uuid, @AuthenticationPrincipal LibraryUser libraryUser) {
        bookRepository.findById(uuid).subscribe(
            book ->  {
                book.doBorrow(null);
                bookRepository.save(book).subscribe();
            }
        );
    }

    public void returnById(UUID uuid, @AuthenticationPrincipal LibraryUser libraryUser) {
        bookRepository.findById(uuid).subscribe(
                book ->  {
                    book.doReturn(null);
                    bookRepository.save(book).subscribe();
                }
        );
    }

    public Flux<BookResource> findAll() {
        return bookRepository.findAll().map(
                b -> modelMapper.map(b, BookResource.class)
        );
    }

    public Mono<Void> deleteById(UUID uuid) {
        return bookRepository.deleteById(uuid).then();
    }
}
