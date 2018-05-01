package com.example.library.server.dataaccess;

import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "books")
public class BorrowState {

    private Instant on;

    @DBRef
    private User by;

    @PersistenceConstructor
    public BorrowState(Instant on, User by) {
        this.on = on;
        this.by = by;
    }

    public Instant getOn() {
        return on;
    }

    public User getBy() {
        return by;
    }
}
