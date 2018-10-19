package com.example.library.server.business;

import com.example.library.server.dataaccess.User;
import com.example.library.server.dataaccess.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.IdGenerator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@PreAuthorize("hasRole('ADMIN')")
public class UserService {

    private final UserRepository userRepository;
    private final IdGenerator idGenerator;

    @Autowired
    public UserService(UserRepository userRepository, IdGenerator idGenerator) {
        this.userRepository = userRepository;
        this.idGenerator = idGenerator;
    }

    @PreAuthorize("isAnonymous() or isAuthenticated()")
    public Mono<UserResource> findOneByEmail(String email) {
        return userRepository.findOneByEmail(email).map(this::convert);
    }

    public Mono<Void> create(Mono<UserResource> userResource) {
        return userRepository.insert(userResource.map(this::convert)).then();
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
        return new UserResource(u.getId(), u.getEmail(), u.getFirstName(), u.getLastName(), u.getRoles());
    }

    private User convert(UserResource ur) {
        return new User(ur.getId() == null ? idGenerator.generateId() : ur.getId(), ur.getEmail(),
                ur.getFirstName(), ur.getLastName(), ur.getRoles());
    }
}
