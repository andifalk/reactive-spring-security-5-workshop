package com.example.oidc.client.api.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CreateBookResource {

  private String isbn;

  private String title;

  private String description;

  private boolean borrowed;

  private List<String> authors = new ArrayList<>();

  private UserResource borrowedBy = null;

  @SuppressWarnings("unused")
  public CreateBookResource() {}

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

  public String getAuthor() {
    return authors.isEmpty() ? null : authors.get(0);
  }

  public void setAuthor(String author) {
    this.authors.add(author);
  }

  public boolean isBorrowed() {
    return borrowed;
  }

  public void setBorrowed(boolean borrowed) {
    this.borrowed = borrowed;
  }

  public UserResource getBorrowedBy() {
    return borrowedBy;
  }

  public void setBorrowedBy(UserResource borrowedBy) {
    this.borrowedBy = borrowedBy;
  }

  public List<String> getAuthors() {
    return authors;
  }

  public void setAuthors(List<String> authors) {
    this.authors = authors;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    CreateBookResource bookResource = (CreateBookResource) o;
    return borrowed == bookResource.borrowed
        && isbn.equals(bookResource.isbn)
        && title.equals(bookResource.title)
        && description.equals(bookResource.description)
        && authors.equals(bookResource.authors)
        && Objects.equals(borrowedBy, bookResource.borrowedBy);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), isbn, title, description, borrowed, authors, borrowedBy);
  }

  @Override
  public String toString() {
    return "BookResource{"
        + ", isbn='"
        + isbn
        + '\''
        + ", title='"
        + title
        + '\''
        + ", description='"
        + description
        + '\''
        + ", borrowed="
        + borrowed
        + ", authors="
        + authors
        + ", borrowedBy="
        + borrowedBy
        + '}';
  }
}
