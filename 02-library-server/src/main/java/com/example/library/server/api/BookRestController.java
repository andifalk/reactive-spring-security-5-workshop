package com.example.library.server.api;

import com.example.library.server.business.BookResource;
import com.example.library.server.business.BookService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class BookRestController {

    private final BookService bookService;

    @Autowired
    public BookRestController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public Flux<BookResource> getAllBooks() {
        return bookService.findAll();
    }

    @GetMapping("/books/{bookId}")
    public Mono<ResponseEntity<BookResource>> getBookById(@PathVariable("bookId") UUID bookId) {
        return bookService.findById(bookId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/books/{bookId}/borrow")
    public Mono<ResponseEntity<Void>> borrowBookById(@PathVariable("bookId") UUID bookId) {
        bookService.borrowById(bookId, null);
        return Mono.just(ResponseEntity.ok().build());
    }

    @PostMapping("/books/{bookId}/return")
    public Mono<ResponseEntity<Void>> returnBookById(@PathVariable("bookId") UUID bookId) {
        bookService.returnById(bookId, null);
        return Mono.just(ResponseEntity.ok().build());
    }

    @PostMapping("/books")
    public Mono<ResponseEntity<Void>> createBook(@Validated @RequestBody Mono<BookResource> bookResource) {
        bookService.create(bookResource).subscribe();
        return Mono.just(ResponseEntity.ok().build());
    }

    @DeleteMapping("/books/{bookId}")
    public Mono<ResponseEntity<Void>> deleteBook(@PathVariable("bookId") UUID bookId) {
        return bookService.deleteById(bookId).map(ResponseEntity::ok);
    }
}
