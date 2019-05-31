package com.example.library.server.dataaccess;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BookBuilder {

  private UUID id = UUID.randomUUID();

  private String isbn = "123-456789123";

  private String title = "Book title";

  private String description = "Book description";

  private List<String> authors = new ArrayList<>();

  private boolean borrowed;

  private User borrowedBy;

  private BookBuilder() {
    this.authors.add("Author");
  }

  public static BookBuilder book() {
    return new BookBuilder();
  }

  public BookBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public BookBuilder withIsbn(String isbn) {
    this.isbn = isbn;
    return this;
  }

  public BookBuilder withTitle(String title) {
    this.title = title;
    return this;
  }

  public BookBuilder withDescription(String description) {
    this.description = description;
    return this;
  }

  public BookBuilder withAuthors(List<String> authors) {
    this.authors = authors;
    return this;
  }

  public BookBuilder withBorrowedBy(User user) {
    this.borrowedBy = user;
    this.borrowed = (user != null);
    return this;
  }

  public Book build() {
    return new Book(
        this.id,
        this.isbn,
        this.title,
        this.description,
        this.authors,
        this.borrowed,
        this.borrowedBy);
  }
}
