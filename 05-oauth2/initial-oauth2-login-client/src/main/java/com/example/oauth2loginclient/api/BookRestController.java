package com.example.oauth2loginclient.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class BookRestController {

    private final RestTemplate restTemplate;

    @Autowired
    public BookRestController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/books")
    List<BookResource> books() {
        ResponseEntity<List<BookResource>> responseEntity = this.restTemplate
                .exchange("http://localhost:8080/books", HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<BookResource>>() {
                        });
        return responseEntity.getBody();
    }

    @GetMapping("/users")
    List<UserResource> users() {
        ResponseEntity<List<UserResource>> responseEntity = this.restTemplate
                .exchange("http://localhost:8080/users", HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<UserResource>>() {
                        });
        return responseEntity.getBody();
    }

}
