package com.example.authorizationcode.client.web;

import com.example.authorizationcode.client.jwt.JsonWebToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

@Controller
public class TokenRequestController {

  private final WebClient webClient;

  @Value("${democlient.token.endpoint}")
  private URL tokenEndpointUrl;

  @Value("${democlient.token.clientid}")
  private String clientid;

  @Value("${democlient.token.client-secret}")
  private String clientSecret;

  @Value("${democlient.token.redirect-uri}")
  private URI redirectUri;

  public TokenRequestController(WebClient webClient) {
    this.webClient = webClient;
  }

  @GetMapping("/tokenrequest")
  public Mono<String> tokenRequest(
      @RequestParam("code") String code, @RequestParam("state") String state, Model model)
      throws URISyntaxException {
    model.addAttribute("token_endpoint", tokenEndpointUrl.toString());
    String tokenRequestBody =
        "grant_type=authorization_code&code="
            + code
            + "&state="
            + state
            + "&redirect_uri="
            + redirectUri.toString()
            + "&client_id="
            + clientid
            + "&client_secret="
            + clientSecret;

    return performTokenRequest(model, tokenRequestBody)
        .map(
            r -> {
              if (r) {
                return "access-token";
              } else {
                return "error";
              }
            });
  }

  @GetMapping("/refreshtoken")
  public Mono<String> refreshTokenRequest(
      @RequestParam("refresh_token") String refreshToken, Model model) throws URISyntaxException {
    model.addAttribute("token_endpoint", tokenEndpointUrl.toString());
    String tokenRequestBody =
        "grant_type=refresh_token&refresh_token="
            + refreshToken
            + "&client_id="
            + clientid
            + "&client_secret="
            + clientSecret;

    return performTokenRequest(model, tokenRequestBody)
        .map(
            r -> {
              if (r) {
                model.addAttribute("refresh_token", refreshToken);
                return "access-token";
              } else {
                return "error";
              }
            });
  }

  private Mono<Boolean> performTokenRequest(Model model, String tokenRequestBody)
      throws URISyntaxException {
    return webClient
        .post()
        .uri(tokenEndpointUrl.toURI())
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(tokenRequestBody), String.class)
        .retrieve()
        .bodyToMono(TokenResponse.class)
        .flatMap(
            tr -> {
              model.addAttribute("access_token", tr.getAccess_token());
              model.addAttribute(
                  "refresh_token", tr.getRefresh_token() != null ? tr.getRefresh_token() : "");
              model.addAttribute("scope", tr.getScope() != null ? tr.getScope() : "");
              model.addAttribute("expires_in", tr.getExpires_in());
              model.addAttribute("token_type", tr.getToken_type());
              JsonWebToken jwt = new JsonWebToken(tr.getAccess_token());
              if (jwt.isJwt()) {
                model.addAttribute("jwt_header", jwt.getHeader());
                model.addAttribute("jwt_payload", jwt.getPayload());
                model.addAttribute("jwt_signature", jwt.getSignature());
              } else {
                model.addAttribute("jwt_header", "--");
                model.addAttribute("jwt_payload", "--");
                model.addAttribute("jwt_signature", "--");
              }
              return Mono.just(true);
            })
        .onErrorResume(
            p -> p instanceof WebClientResponseException,
            t -> {
              model.addAttribute("error", "Error getting token");
              model.addAttribute(
                  "error_description", ((WebClientResponseException) t).getResponseBodyAsString());
              return Mono.just(false);
            });
  }
}
