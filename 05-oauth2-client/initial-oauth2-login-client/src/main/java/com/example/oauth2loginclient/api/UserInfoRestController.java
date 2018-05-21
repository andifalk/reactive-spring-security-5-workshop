package com.example.oauth2loginclient.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class UserInfoRestController {

    private final RestTemplate restTemplate;
    private final OAuth2AuthorizedClientService clientService;

    @Autowired
    public UserInfoRestController(RestTemplate restTemplate, OAuth2AuthorizedClientService clientService) {
        this.restTemplate = restTemplate;
        this.clientService = clientService;
    }

    @GetMapping("/")
    UserInfo userInfo(OAuth2AuthenticationToken token) {
        OAuth2AuthorizedClient client = this.clientService
                .loadAuthorizedClient(
                        token.getAuthorizedClientRegistrationId(),
                        token.getName());
        String uri = client.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUri();
        ResponseEntity<UserInfo> responseEntity = this.restTemplate
                .exchange(uri, HttpMethod.GET, null, UserInfo.class);
        return responseEntity.getBody();
    }
}
