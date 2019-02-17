package com.example.library.server.api;

import com.example.library.server.business.BookResource;
import com.example.library.server.business.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * REST api for books.
 */
@RestController
@Validated
public class BookRestController {

    private static final String PATH_VARIABLE_BOOK_ID = "bookId";

    private static final String PATH_BOOK_ID = "{" + PATH_VARIABLE_BOOK_ID + "}";

    private final BookService bookService;

    @Autowired
    public BookRestController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public Flux<BookResource> getAllBooks() {
        return bookService.findAll();
    }

    @GetMapping("/books/" + PATH_BOOK_ID)
    public Mono<ResponseEntity<BookResource>> getBookById(@PathVariable(PATH_VARIABLE_BOOK_ID) UUID bookId) {
        return bookService.findById(bookId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/books/" + PATH_BOOK_ID + "/borrow")
    public Mono<Void> borrowBookById(@PathVariable(PATH_VARIABLE_BOOK_ID) UUID bookId) {
        return bookService.borrowById(bookId, null);
    }

    @PostMapping("/books/" + PATH_BOOK_ID + "/return")
    public Mono<Void> returnBookById(@PathVariable(PATH_VARIABLE_BOOK_ID) UUID bookId) {
        return bookService.returnById(bookId, null);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/books")
    public Mono<Void> createBook(@RequestBody Mono<BookResource> bookResource) {
        return bookService.create(bookResource);
    }

    @DeleteMapping("/books/" + PATH_BOOK_ID)
    public Mono<Void> deleteBook(@PathVariable(PATH_VARIABLE_BOOK_ID) UUID bookId) {
        return bookService.deleteById(bookId);
    }
}
