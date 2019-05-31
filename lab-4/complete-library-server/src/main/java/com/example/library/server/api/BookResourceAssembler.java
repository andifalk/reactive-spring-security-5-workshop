package com.example.library.server.api;

import com.example.library.server.dataaccess.Book;
import com.example.library.server.dataaccess.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.IdGenerator;

@Component
public class BookResourceAssembler {

  private final ModelMapper modelMapper;
  private final IdGenerator idGenerator;

  public BookResourceAssembler(ModelMapper modelMapper, IdGenerator idGenerator) {
    this.modelMapper = modelMapper;
    this.idGenerator = idGenerator;
  }

  public BookResource toResource(Book b) {
    User borrowedBy = b.getBorrowedBy();
    BookResource bookResource = modelMapper.map(b, BookResource.class);
    if (borrowedBy != null) {
      bookResource.setBorrowedBy(modelMapper.map(borrowedBy, UserResource.class));
    }
    return bookResource;
  }

  public Book toModel(BookResource br) {
    UserResource borrowedBy = br.getBorrowedBy();
    Book book = modelMapper.map(br, Book.class);
    if (borrowedBy != null) {
      book.doBorrow(modelMapper.map(borrowedBy, User.class));
    }
    if (book.getId() == null) {
      book.setId(idGenerator.generateId());
    }
    return book;
  }
}
