package com.example.authorizationcode.client.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

@Controller
public class TokenIntrospectionController {

  private final WebClient webClient;

  @Value("${democlient.introspection.endpoint}")
  private URL tokenIntrospectionEndpointUrl;

  @Value("${democlient.token.clientid}")
  private String clientid;

  @Value("${democlient.token.client-secret}")
  private String clientSecret;

  public TokenIntrospectionController(WebClient webClient) {
    this.webClient = webClient;
  }

  @GetMapping("/introspection")
  public Mono<String> tokenRequest(@RequestParam("access_token") String accessToken, Model model)
      throws URISyntaxException {
    model.addAttribute("token_introspection_endpoint", tokenIntrospectionEndpointUrl.toString());
    String tokenRequestBody =
        "token="
            + accessToken
            + "&token_type_hint=access_token"
            + "&client_id="
            + clientid
            + "&client_secret="
            + clientSecret;

    return performTokenIntrospectionRequest(model, tokenRequestBody);
  }

  private Mono<String> performTokenIntrospectionRequest(Model model, String tokenRequestBody)
      throws URISyntaxException {
    return webClient
        .post()
        .uri(tokenIntrospectionEndpointUrl.toURI())
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(tokenRequestBody), String.class)
        .retrieve()
        .bodyToMono(String.class)
        .map(
            s -> {
              model.addAttribute("introspection_response", prettyJson(s));
              return "introspection";
            })
        .onErrorResume(
            p -> p instanceof WebClientResponseException,
            t -> {
              model.addAttribute("error", "Error getting token");
              model.addAttribute(
                  "error_description", ((WebClientResponseException) t).getResponseBodyAsString());
              return Mono.just("error");
            });
  }

  private String prettyJson(String raw) {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
    try {
      return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mapper.readValue(raw, Object.class));
    } catch (IOException e) {
      return raw;
    }
  }
}
