package com.example.library.server.api;

import com.example.library.server.business.BookResource;
import com.example.library.server.business.BookService;
import com.example.library.server.security.LibraryUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

/**
 * REST api for books.
 */
@RestController
public class BookRestController {

    private static final String PATH_VARIABLE_BOOK_ID = "bookId";

    private static final String PATH_BOOK_ID = "{" + PATH_VARIABLE_BOOK_ID + "}";

    private final BookService bookService;

    @Autowired
    public BookRestController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public List<BookResource> getAllBooks() {
        return bookService.findAll();
    }

    @GetMapping("/books/" + PATH_BOOK_ID)
    public ResponseEntity<BookResource> getBookById(@PathVariable(PATH_VARIABLE_BOOK_ID) UUID bookId) {
        BookResource bookResource = bookService.findById(bookId);
        if (bookResource != null) {
            return ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(bookResource);
        } else {
            return notFound().build();
        }
    }

    @PostMapping("/books/" + PATH_BOOK_ID + "/borrow")
    public ResponseEntity<Void> borrowBookById(
            @PathVariable(PATH_VARIABLE_BOOK_ID) UUID bookId, @AuthenticationPrincipal LibraryUser user) {
        bookService.borrowById(bookId, user.getUserResource().getId());
        return ok().build();
    }

    @PostMapping("/books/" + PATH_BOOK_ID + "/return")
    public ResponseEntity<Void> returnBookById(
            @PathVariable(PATH_VARIABLE_BOOK_ID) UUID bookId, @AuthenticationPrincipal LibraryUser user) {
        bookService.returnById(bookId, user.getUserResource().getId());
        return ok().build();
    }

    @PostMapping("/books")
    public ResponseEntity<Void> createBook(@Validated @RequestBody BookResource bookResource) {
        bookService.create(bookResource);
        return ok().build();
    }

    @DeleteMapping("/books/" + PATH_BOOK_ID)
    public ResponseEntity<Void> deleteBook(@PathVariable(PATH_VARIABLE_BOOK_ID) UUID bookId) {
        bookService.deleteById(bookId);
        return ok().build();
    }
}
