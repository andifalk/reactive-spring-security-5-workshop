package com.example.library.server.security;

import com.example.library.server.business.UserService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsPasswordService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class LibraryReactiveUserDetailsService implements ReactiveUserDetailsService, ReactiveUserDetailsPasswordService {

    private final UserService userService;

    public LibraryReactiveUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userService.findOneByEmail(username).map(LibraryUser::new);
    }

    @Override
    public Mono<UserDetails> updatePassword(UserDetails user, String newPassword) {
        return userService.findOneByEmail(user.getUsername())
                .doOnSuccess(u -> u.setPassword(newPassword))
                .flatMap(userService::update)
                .map(LibraryUser::new);
    }
}
