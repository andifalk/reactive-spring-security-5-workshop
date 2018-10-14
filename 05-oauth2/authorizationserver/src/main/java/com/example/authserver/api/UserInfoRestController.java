package com.example.authserver.api;

import com.example.authserver.User;
import com.example.authserver.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class UserInfoRestController {

    private final UserRepository userRepository;

    @Autowired
    public UserInfoRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @SuppressWarnings("unused")
    @GetMapping("/resources/userinfo")
    User userInfo(Principal principal) {
        return userRepository.findOneByEmail(principal.getName());
    }
}
