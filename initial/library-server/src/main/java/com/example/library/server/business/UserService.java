package com.example.library.server.business;

import com.example.library.server.dataaccess.User;
import com.example.library.server.dataaccess.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<UserResource> findOneByEmail(String email) {
        return userRepository.findOneByEmail(email).map(
            u -> new ModelMapper().map(u, UserResource.class)
        );
    }

    public Mono<UserResource> save(UserResource userResource) {
        return userRepository.save(new ModelMapper().map(userResource, User.class)).map(
            u -> new ModelMapper().map(u, UserResource.class)
        );
    }

    public Mono<UserResource> findById(UUID uuid) {
        return userRepository.findById(uuid).map(
            u -> new ModelMapper().map(u, UserResource.class)
        );
    }

    public Flux<UserResource> findAll() {
        return userRepository.findAll().map(
            u -> new ModelMapper().map(u, UserResource.class)
        );
    }

    public Mono<Void> deleteById(UUID uuid) {
        return userRepository.deleteById(uuid);
    }
}
