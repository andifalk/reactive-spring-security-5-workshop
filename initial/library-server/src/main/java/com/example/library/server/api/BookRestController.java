package com.example.library.server.api;

import com.example.library.server.business.BookResource;
import com.example.library.server.business.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class BookRestController {

    private final BookService bookService;

    @Autowired
    public BookRestController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public ResponseEntity<Flux<BookResource>> getAllBooks() {
        return ResponseEntity.ok(bookService.findAll());
    }

}
