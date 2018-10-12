package com.example.library.server.api;

import com.example.library.server.business.UserResource;
import com.example.library.server.business.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.*;

/**
 * Reactive handler for users.
 */
@RestController
@RequestMapping(path = "/users")
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @SuppressWarnings("unused")
    @GetMapping
    public ResponseEntity<List<UserResource>> getAllUsers() {
        return ok().contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(userService.findAll());
    }

    @GetMapping(path = "/{userId}")
    public ResponseEntity<UserResource> getUser(@PathVariable("userId") UUID userId) {
        UserResource userResource = userService.findById(userId);
        if (userResource != null) {
            return ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(userResource);
        } else {
            return notFound().build();
        }
    }

    @DeleteMapping(path = "/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") UUID userId) {
        userService.deleteById(userId);
        return noContent().build();
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@Valid @RequestBody UserResource userResource) {
        userService.create(userResource);
        return ok().build();
    }
}
