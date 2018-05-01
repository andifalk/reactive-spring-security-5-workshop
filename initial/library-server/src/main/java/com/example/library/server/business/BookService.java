package com.example.library.server.business;

import com.example.library.server.dataaccess.Book;
import com.example.library.server.dataaccess.BookRepository;
import com.example.library.server.dataaccess.BorrowState;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Mono<BookResource> save(BookResource bookResource) {
        Book book = new ModelMapper().map(bookResource, Book.class);
        return bookRepository.save(book)
                .map(s -> new ModelMapper().map(s, BookResource.class));
    }

    public Mono<BookResource> findById(UUID uuid) {
        return bookRepository.findById(uuid).map(
                b -> new ModelMapper().map(b, BookResource.class));
    }

    public Flux<BookResource> findAll() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(BorrowState.class, BorrowStateResource.class).addMapping(BorrowState::getBy, BorrowStateResource::setBy);
        return bookRepository.findAll().map(
                b -> modelMapper.map(b, BookResource.class)
        );
    }

    public Mono<Void> deleteById(UUID uuid) {
        return bookRepository.deleteById(uuid);
    }
}
