package com.example.library.server;

import com.example.library.server.common.Role;
import com.example.library.server.dataaccess.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.IdGenerator;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

/**
 * Store initial users and books in mongodb.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final UUID USER_IDENTIFIER = UUID.fromString("c47641ee-e63c-4c13-8cd2-1c2490aee0b3");
    private static final UUID CURATOR_IDENTIFIER = UUID.fromString("40c5ad0d-41f7-494b-8157-33fad16012aa");
    private static final UUID ADMIN_IDENTIFIER = UUID.fromString("0d2c04f1-e25f-41b5-b4cd-3566a081200f");

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final IdGenerator idGenerator;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(BookRepository bookRepository, UserRepository userRepository, IdGenerator idGenerator, PasswordEncoder passwordEncoder) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.idGenerator = idGenerator;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        createUsers();
        createBooks();
    }

    private void createUsers() {
        final Logger logger = LoggerFactory.getLogger(this.getClass());

        logger.info("Creating users with USER, CURATOR and ADMIN roles...");
        userRepository
                .save(
                        new User(
                                USER_IDENTIFIER,
                                "user@example.com",
                                passwordEncoder.encode("user"),
                                "Library",
                                "User",
                                List.of(Role.USER)))
                .subscribe();
        userRepository
                .save(
                        new User(
                                CURATOR_IDENTIFIER,
                                "curator@example.com",
                                passwordEncoder.encode("curator"),
                                "Library",
                                "Curator",
                                List.of(Role.USER, Role.CURATOR)))
                .subscribe();
        userRepository
                .save(
                        new User(
                                ADMIN_IDENTIFIER,
                                "admin@example.com",
                                passwordEncoder.encode("admin"),
                                "Library",
                                "Administrator",
                                List.of(Role.USER, Role.CURATOR, Role.ADMIN)))
                .subscribe();

        Flux<User> users = userRepository.findAll();
        users.delaySubscription(Duration.ofMillis(100)).subscribe(
            u -> logger.info("User '{}' created with id '{}'", u.getEmail(), u.getId())
        );
    }

    private void createBooks() {
        final Logger logger = LoggerFactory.getLogger(this.getClass());

        User user = userRepository.findOneByEmail("user@example.com").block();

        logger.info("Creating some initial books...");
        bookRepository
            .save(
                new Book(
                    idGenerator.generateId(),
                    "9780132350884",
                    "Clean Code",
                    "Even bad code can function. But if code isn’t clean, it can bring a development " +
                            "organization to its knees. Every year, countless hours and significant resources are " +
                            "lost because of poorly written code. But it doesn’t have to be that way. " +
                            "Noted software expert Robert C. Martin presents a revolutionary paradigm with Clean Code: " +
                            "A Handbook of Agile Software Craftsmanship . Martin has teamed up with his colleagues from " +
                            "Object Mentor to distill their best agile practice of cleaning code “on the fly” into a book " +
                            "that will instill within you the values of a software craftsman and make you a better " +
                            "programmer—but only if you work at it.",
                    List.of("Bob C. Martin"),
                    true, user))
            .subscribe();
        bookRepository
            .save(
                    new Book(
                            idGenerator.generateId(),
                            "9781449374648",
                            "Cloud Native Java",
                            "What separates the traditional enterprise from the likes of Amazon, Netflix, " +
                                    "and Etsy? Those companies have refined the art of cloud native development to " +
                                    "maintain their competitive edge and stay well ahead of the competition. " +
                                    "This practical guide shows Java/JVM developers how to build better software, " +
                                    "faster, using Spring Boot, Spring Cloud, and Cloud Foundry.",
                            List.of("Josh Long", "Kenny Bastiani"), false, null))
            .subscribe();
        bookRepository
            .save(
                    new Book(
                            idGenerator.generateId(),
                            "9781617291203",
                            "Spring in Action: Covers Spring 4",
                            "Spring in Action, Fourth Edition is a hands-on guide to the Spring Framework, " +
                                    "updated for version 4. It covers the latest features, tools, and practices " +
                                    "including Spring MVC, REST, Security, Web Flow, and more. You'll move between " +
                                    "short snippets and an ongoing example as you learn to build simple and efficient " +
                                    "J2EE applications. Author Craig Walls has a special knack for crisp and " +
                                    "entertaining examples that zoom in on the features and techniques you really need.",
                            List.of("Craig Walls"), false, null))
            .subscribe();
        bookRepository
            .save(
                    new Book(
                            idGenerator.generateId(),
                            "9781942788003",
                            "The DevOps Handbook",
                            "Wondering if The DevOps Handbook is for you? Authors, Gene Kim, Jez Humble, " +
                                    "Patrick Debois and John Willis developed this book for anyone looking to transform " +
                                    "their IT organization—especially those who want to make serious changes through the " +
                                    "DevOps methodology to increase productivity, profitability and win the marketplace.",
                            List.of("Gene Kim", "Jez Humble", "Patrick Debois"), false, null))
            .subscribe();

        Flux<Book> books = bookRepository.findAll();
        books.delaySubscription(Duration.ofMillis(100)).subscribe(
                b -> logger.info("Book '{}' created with isbn '{}' and id '{}'", b.getTitle(), b.getIsbn(), b.getId())
        );

    }
}
