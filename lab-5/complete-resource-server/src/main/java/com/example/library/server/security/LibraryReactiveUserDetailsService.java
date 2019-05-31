package com.example.library.server.security;

import com.example.library.server.business.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class LibraryReactiveUserDetailsService implements ReactiveUserDetailsService {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(LibraryReactiveUserDetailsService.class);

  private final UserService userService;

  public LibraryReactiveUserDetailsService(UserService userService) {
    this.userService = userService;
  }

  @Override
  public Mono<UserDetails> findByUsername(String username) {
    LOGGER.info("Finding user for user name {}", username);

    return userService.findOneByEmail(username).map(LibraryUser::new);
  }
}
