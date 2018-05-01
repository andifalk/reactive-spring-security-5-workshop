package com.example.library.server.dataaccess;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Document(collection = "books")
public class Book {

    @Id
    private UUID id;

    @Indexed(unique = true)
    private String isbn;

    @Indexed
    private String title;

    @TextIndexed
    private String description;

    private List<String> authors;

    private BorrowState borrowed;

    @PersistenceConstructor
    public Book(UUID id, String isbn, String title, String description, List<String> authors, BorrowState borrowed) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.description = description;
        this.authors = authors;
        this.borrowed = borrowed;
    }

    public UUID getId() {
        return id;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public BorrowState getBorrowed() {
        return borrowed;
    }
}
