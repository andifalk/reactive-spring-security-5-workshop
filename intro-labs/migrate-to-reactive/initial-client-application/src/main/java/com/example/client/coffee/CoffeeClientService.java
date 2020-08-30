package com.example.client.coffee;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

@Service
public class CoffeeClientService {

  private final Logger log = LoggerFactory.getLogger(CoffeeClientService.class);
  private final RestTemplate restTemplate = new RestTemplate();

  public Collection<CoffeeResource> allCoffees() {
    ResponseEntity<CoffeeResource[]> coffeeResources = restTemplate.getForEntity("http://localhost:9090/coffees", CoffeeResource[].class);
    if (coffeeResources.getStatusCode() == HttpStatus.OK) {
      log.info("Got coffee list with {} entries", Objects.requireNonNull(coffeeResources.getBody()).length);
      return Arrays.asList(Objects.requireNonNull(coffeeResources.getBody()));
    } else {
      log.error("Error getting coffee list: {}", coffeeResources.getStatusCode());
      return Collections.emptyList();
    }
  }

}
