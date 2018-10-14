package com.example.oauth2loginclient.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration {

  @Bean
  RestTemplate restTemplate(OAuth2AuthorizedClientService clientService) {
    return new RestTemplateBuilder()
        .interceptors(
            (ClientHttpRequestInterceptor)
                (httpRequest, bytes, execution) -> {
                  Authentication authentication =
                      SecurityContextHolder.getContext().getAuthentication();
                  if (authentication instanceof OAuth2AuthenticationToken) {
                    OAuth2AuthenticationToken token =
                            (OAuth2AuthenticationToken) authentication;

                    OAuth2AuthorizedClient client =
                        clientService.loadAuthorizedClient(
                            token.getAuthorizedClientRegistrationId(), token.getName());

                    httpRequest
                        .getHeaders()
                        .add(
                            HttpHeaders.AUTHORIZATION,
                            String.format("Bearer %s", client.getAccessToken().getTokenValue()));
                  }
                  return execution.execute(httpRequest, bytes);
                })
        .build();
  }
}
