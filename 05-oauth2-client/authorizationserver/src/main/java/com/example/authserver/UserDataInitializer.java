package com.example.authserver;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Initializes some users with passwords.
 */
@Component
public class UserDataInitializer implements CommandLineRunner {

    private static final UUID USER_IDENTIFIER = UUID.fromString("c47641ee-e63c-4c13-8cd2-1c2490aee0b3");
    private static final UUID CURATOR_IDENTIFIER = UUID.fromString("40c5ad0d-41f7-494b-8157-33fad16012aa");
    private static final UUID ADMIN_IDENTIFIER = UUID.fromString("0d2c04f1-e25f-41b5-b4cd-3566a081200f");

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... strings) {
    Stream.of(
            new User(
                USER_IDENTIFIER,
                "Library",
                "User",
                "user@example.com",
                passwordEncoder.encode("user"),
                Collections.singletonList("USER")),
            new User(
                CURATOR_IDENTIFIER,
                "Library",
                "Curator",
                "curator@example.com",
                passwordEncoder.encode("curator"),
                Arrays.asList("USER", "CURATOR")),
            new User(
                ADMIN_IDENTIFIER,
                "Library",
                "Admin",
                "admin@example.com",
                passwordEncoder.encode("admin"),
                Arrays.asList("USER", "ADMIN")))
        .forEach(userRepository::save);
    }
}
