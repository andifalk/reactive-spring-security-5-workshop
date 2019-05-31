package com.example.library.server.business;

import com.example.library.server.dataaccess.User;
import com.example.library.server.dataaccess.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@PreAuthorize("hasRole('LIBRARY_ADMIN')")
public class UserService {

  private final UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @PreAuthorize("isAnonymous() or isAuthenticated()")
  public Mono<User> findOneByEmail(String email) {
    return userRepository.findOneByEmail(email);
  }

  public Mono<Void> create(Mono<User> user) {
    return userRepository.insert(user).then();
  }

  public Mono<User> update(User user) {
    return userRepository.save(user);
  }

  public Mono<User> findById(UUID uuid) {
    return userRepository.findById(uuid);
  }

  public Flux<User> findAll() {
    return userRepository.findAll();
  }

  public Mono<Void> deleteById(UUID uuid) {
    return userRepository.deleteById(uuid);
  }
}
