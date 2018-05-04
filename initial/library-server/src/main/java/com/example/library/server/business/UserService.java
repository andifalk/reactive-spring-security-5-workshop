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

    @Autowired
    public UserService(UserRepository userRepository, IdGenerator idGenerator) {
        this.userRepository = userRepository;
        this.idGenerator = idGenerator;
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

    public Mono<Void> create(Mono<UserResource> userResource) {
        return userRepository.insert(
                userResource.map(
                        ur -> new User(idGenerator.generateId(), ur.getEmail(), ur.getFirstName(), ur.getLastName(), ur.getRoles())))
                .then();
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
