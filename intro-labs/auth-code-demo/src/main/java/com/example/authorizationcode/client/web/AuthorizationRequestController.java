package com.example.authorizationcode.client.web;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/** Controller for performing OAuth2 authorization request. */
@Controller
public class AuthorizationRequestController {

  @Value("${democlient.authorization.endpoint}")
  private URL authorizationEndpointUrl;

  @Value("${democlient.authorization.clientid}")
  private String clientid;

  @Value("${democlient.authorization.response-type}")
  private String responseType;

  @Value("${democlient.authorization.redirect-uri}")
  private URI redirectUri;

  @Value("${democlient.authorization.scope}")
  private List<String> scope = new ArrayList<>();

  private final WebClient webClient;

  private String authorizationRequest;

  public AuthorizationRequestController(WebClient webClient) {
    this.webClient = webClient;
  }

  @GetMapping("/")
  public String initiateAuthRequest(Model model) throws UnsupportedEncodingException {

    model.addAttribute("authorization_endpoint", authorizationEndpointUrl.toString());
    model.addAttribute("client_id", clientid);
    model.addAttribute("response_type", responseType);
    model.addAttribute("redirect_uri", redirectUri.toString());
    model.addAttribute("scope", String.join(" ", scope));

    String randomState = generateRandomState();
    model.addAttribute("state", randomState);

    createAuthorizationRequest(randomState);
    model.addAttribute("authorizationrequest", authorizationRequest);

    return "init-auth-request";
  }

  @GetMapping("/authrequest")
  @ResponseBody
  public Mono<ResponseEntity<String>> performAuthRequest() {
    return webClient
        .get()
        .uri(authorizationRequest)
        .exchange()
        .map(cr -> ResponseEntity.status(cr.statusCode()).body(cr.statusCode().getReasonPhrase()));
  }

  private String generateRandomState() throws UnsupportedEncodingException {
    return URLEncoder.encode(RandomStringUtils.randomAlphanumeric(16), "UTF-8");
  }

  private void createAuthorizationRequest(String randomState) throws UnsupportedEncodingException {
    authorizationRequest =
        authorizationEndpointUrl.toString()
            + "?response_type="
            + responseType
            + "&client_id="
            + URLEncoder.encode(clientid, "UTF-8")
            + "&state="
            + randomState
            + "&scope="
            + URLEncoder.encode(String.join(" ", scope), "UTF-8")
            + "&redirect_uri="
            + URLEncoder.encode(redirectUri.toString(), "UTF-8");
  }
}
