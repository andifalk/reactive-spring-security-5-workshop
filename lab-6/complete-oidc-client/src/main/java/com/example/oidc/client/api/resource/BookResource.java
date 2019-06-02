package com.example.oidc.client.api.resource;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public class BookResource {

  private UUID id;

  @NotNull private String isbn;

  @NotNull private String title;

  @NotNull private String description;

  @NotNull private List<String> authors;

  private boolean borrowed;

  private UserInfo borrowedBy;

  public BookResource() {}

  public BookResource(
      UUID id,
      String isbn,
      String title,
      String description,
      List<String> authors,
      boolean borrowed,
      UserInfo borrowedBy) {
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

  public void setId(UUID id) {
    this.id = id;
  }

  public String getIsbn() {
    return isbn;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<String> getAuthors() {
    return authors;
  }

  public void setAuthors(List<String> authors) {
    this.authors = authors;
  }

  public boolean isBorrowed() {
    return borrowed;
  }

  public void setBorrowed(boolean borrowed) {
    this.borrowed = borrowed;
  }

  public UserInfo getBorrowedBy() {
    return borrowedBy;
  }

  public void setBorrowedBy(UserInfo borrowedBy) {
    this.borrowedBy = borrowedBy;
  }
}
