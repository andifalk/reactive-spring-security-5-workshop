package com.example.server.coffee;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/coffees")
public class CoffeeRestController {

  private final CoffeeService coffeeService;
  private final CoffeeResourceAssembler coffeeResourceAssembler;

  public CoffeeRestController(
      CoffeeService coffeeService, CoffeeResourceAssembler coffeeResourceAssembler) {
    this.coffeeService = coffeeService;
    this.coffeeResourceAssembler = coffeeResourceAssembler;
  }

  @GetMapping
  ResponseEntity<Flux<CoffeeResource>> allCoffees() {
    return ResponseEntity.ok(coffeeResourceAssembler.toCollectionModel(coffeeService.findAll()));
  }

  @GetMapping("/{coffeeId}")
  Mono<ResponseEntity<CoffeeResource>> oneCoffee(@PathVariable("coffeeId") UUID coffeeId) {
    return coffeeService
        .findOne(coffeeId)
        .map(coffeeResourceAssembler::toModel)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }
}
