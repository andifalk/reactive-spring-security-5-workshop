package com.example.library.server.business;

import java.util.List;
import java.util.UUID;

public class BookResource {

    private UUID id;

    private String isbn;

    private String title;

    private String description;

    private List<String> authors;

    private BorrowStateResource borrowed;

    public BookResource() {
    }

    public BookResource(UUID id, String isbn, String title, String description, List<String> authors, BorrowStateResource borrowed) {
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

    public BorrowStateResource getBorrowed() {
        return borrowed;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public void setBorrowed(BorrowStateResource borrowed) {
        this.borrowed = borrowed;
    }
}
