package com.example.library.server.business;

import com.example.library.server.dataaccess.User;

import java.time.Instant;

public class BorrowStateResource {

    private Instant on;
    private UserResource by;

    public BorrowStateResource() {
    }

    public BorrowStateResource(Instant on, UserResource by) {
        this.on = on;
        this.by = by;
    }

    public Instant getOn() {
        return on;
    }

    public UserResource getBy() {
        return by;
    }

    public void setOn(Instant on) {
        this.on = on;
    }

    public void setBy(UserResource by) {
        this.by = by;
    }
}
