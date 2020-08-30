package com.example.client.coffee;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;

@Service
public class CoffeeClientService {

  private final Logger log = LoggerFactory.getLogger(CoffeeClientService.class);
  private final WebClient webClient;

  public CoffeeClientService(WebClient.Builder webClientBuilder) {
    this.webClient = webClientBuilder.baseUrl("http://localhost:9090").build();
  }

  public Flux<CoffeeResource> allCoffees() {
    log.info("Getting coffee list from server");
    return webClient
        .get()
        .uri("/coffees")
        .retrieve()
        .bodyToFlux(CoffeeResource.class)
        .onErrorResume(
            WebClientResponseException.class,
            t -> {
              log.error("Error getting coffees: {} ({})", t.getStatusCode(), t.getResponseBodyAsString());
              return Flux.empty();
            });
  }
}
