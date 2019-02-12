package com.example.oauth2loginclient.api;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
public class UserInfoRestController {

    @GetMapping("/userinfo")
    Mono<Map<String, Object>> userInfo(@AuthenticationPrincipal OAuth2User oauth2User) {
        return Mono.just(oauth2User.getAttributes());
    }
}
