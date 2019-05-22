package com.example.library.server.business;

import com.example.library.server.dataaccess.User;
import com.example.library.server.dataaccess.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.IdGenerator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final IdGenerator idGenerator;
  private final ModelMapper modelMapper;

  @Autowired
  public UserService(
      UserRepository userRepository, IdGenerator idGenerator, ModelMapper modelMapper) {
    this.userRepository = userRepository;
    this.idGenerator = idGenerator;
    this.modelMapper = modelMapper;
  }

  public Mono<User> findOneByEmail(String email) {
    return userRepository.findOneByEmail(email);
  }

  public Mono<Void> create(Mono<UserResource> userResource) {
    return userRepository.insert(userResource.map(this::convert)).then();
  }

  public Mono<User> update(User user) {
    return userRepository.save(user);
  }

  public Mono<UserResource> findById(UUID uuid) {
    return userRepository.findById(uuid).map(this::convert);
  }

  public Flux<UserResource> findAll() {
    return userRepository.findAll().map(this::convert);
  }

  public Mono<Void> deleteById(UUID uuid) {
    return userRepository.deleteById(uuid);
  }

  private UserResource convert(User u) {
    return modelMapper.map(u, UserResource.class);
  }

  private User convert(UserResource ur) {
    User user = modelMapper.map(ur, User.class);
    if (user.getId() == null) {
      user.setId(idGenerator.generateId());
    }
    return user;
  }
}
