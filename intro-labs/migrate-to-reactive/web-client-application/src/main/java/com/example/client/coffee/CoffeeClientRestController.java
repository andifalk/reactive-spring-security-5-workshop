package com.example.client.coffee;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/coffees")
public class CoffeeClientRestController {

  private final CoffeeClientService coffeeClientService;

  public CoffeeClientRestController(CoffeeClientService coffeeClientService) {
    this.coffeeClientService = coffeeClientService;
  }

  @GetMapping
  Flux<CoffeeResource> allCoffees() {
    return coffeeClientService.allCoffees();
  }
}
