package com.example.authserver;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementation for {@link UserDetailsService}.
 */
@Service
public class AuthorizationServerUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public AuthorizationServerUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findOneByEmail(userName);
        if (user == null) {
            throw new UsernameNotFoundException("No user found for " + userName);
        } else {
            return user;
        }
    }
}
