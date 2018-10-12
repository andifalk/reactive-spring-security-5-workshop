package com.example.library.server.dataaccess;

import org.apache.commons.lang3.builder.ToStringBuilder;
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

    private boolean borrowed;

    private User borrowedBy;

    public Book() {
    }

    @PersistenceConstructor
    public Book(UUID id, String isbn, String title, String description, List<String> authors, boolean borrowed, User borrowedBy) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.description = description;
        this.authors = authors;
        this.borrowed = borrowed;
        this.borrowedBy = borrowedBy;
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

    public boolean isBorrowed() {
        return borrowed;
    }

    public User getBorrowedBy() {
        return borrowedBy;
    }

    public void doBorrow(User user) {
        if (!this.borrowed) {
            this.borrowed = true;
            this.borrowedBy = user;
        }
    }

    public void doReturn(User user) {
        if (this.borrowed) {
            this.borrowed = false;
            this.borrowedBy = null;
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("title", title)
                .append("isbn", isbn)
                .append("description", description)
                .append("borrowed", borrowed)
                .append("borrowedBy", borrowedBy)
                .toString();
    }

}
