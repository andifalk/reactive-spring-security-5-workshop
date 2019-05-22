package com.example.github.restapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Controller
public class GitHubNotificationsController {

  private final String githubBaseApi;

  private final WebClient webClient;

  @Autowired
  public GitHubNotificationsController(
      WebClient webClient, @Value("${github.base.url}") String githubBaseApi) {
    this.webClient = webClient;
    this.githubBaseApi = githubBaseApi;
  }

  @GetMapping("/notifications")
  public String getNotifications(
      Model model,
      @RegisteredOAuth2AuthorizedClient(registrationId = "github")
          OAuth2AuthorizedClient authorizedClient) {
    model.addAttribute("scopes", authorizedClient.getAccessToken().getScopes());

    Mono<List<GitHubNotification>> notificationsSub =
        this.webClient
            .get()
            .uri(githubBaseApi + "notifications")
            .retrieve()
            .bodyToFlux(GitHubNotification.class)
            .collectList();
    model.addAttribute("notifications", notificationsSub.block());

    return "notifications";
  }
}
