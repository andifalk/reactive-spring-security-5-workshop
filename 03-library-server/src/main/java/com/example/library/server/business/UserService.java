package com.example.library.server.business;

import com.example.library.server.dataaccess.User;
import com.example.library.server.dataaccess.UserRepository;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    @Autowired
    public UserService(UserRepository userRepository, IdGenerator idGenerator, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.idGenerator = idGenerator;
        this.modelMapper = modelMapper;
    }

    public Mono<UserResource> findOneByEmail(String email) {
        return userRepository.findOneByEmail(email).map(
            u -> modelMapper.map(u, UserResource.class)
        );
    }

    public Mono<Void> create(Mono<UserResource> userResource) {
        return userRepository.insert(
                userResource.map(
                        ur -> new User(idGenerator.generateId(), ur.getEmail(), ur.getPassword(),
                                        ur.getFirstName(), ur.getLastName(), ur.getRoles())))
                .then();
    }

    public Mono<UserResource> findById(UUID uuid) {
        return userRepository.findById(uuid).map(
            u -> modelMapper.map(u, UserResource.class)
        );
    }

    public Flux<UserResource> findAll() {
        return userRepository.findAll().map(
            u -> modelMapper.map(u, UserResource.class)
        );
    }

    public Mono<Void> deleteById(UUID uuid) {
        return userRepository.deleteById(uuid);
    }
}
