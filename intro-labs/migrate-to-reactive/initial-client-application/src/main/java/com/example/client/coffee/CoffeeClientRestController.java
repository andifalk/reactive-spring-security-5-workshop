package com.example.client.coffee;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/coffees")
public class CoffeeClientRestController {

  private final CoffeeClientService coffeeClientService;

  public CoffeeClientRestController(CoffeeClientService coffeeClientService) {
    this.coffeeClientService = coffeeClientService;
  }

  @GetMapping
  ResponseEntity<Collection<CoffeeResource>> allCoffees() {
    return ResponseEntity.ok(coffeeClientService.allCoffees());
  }

}
