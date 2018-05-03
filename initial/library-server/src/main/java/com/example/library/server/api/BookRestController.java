package com.example.library.server.api;

import com.example.library.server.business.BookResource;
import com.example.library.server.business.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

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

    @PostMapping("/books")
    public Mono<BookResource> createBook(@RequestBody BookResource bookResource) {
        return bookService.save(bookResource);
    }

    @DeleteMapping("/books/{bookId}")
    public Mono<ResponseEntity<Void>> deleteBook(@PathVariable("bookId") UUID bookId) {
        return bookService.deleteById(bookId).map(ResponseEntity::ok);
    }
}
