package com.example.library.server.security;

import com.example.library.server.business.UserResource;
import com.example.library.server.business.UserService;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Primary
@Service
public class LibraryUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public LibraryUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserResource userResource = userService.findOneByEmail(username);
        if (userResource != null) {
            return new LibraryUser(userResource);
        } else {
            throw new UsernameNotFoundException("No user found for username " + username);
        }
    }
}
