package com.example.server.coffee;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class CoffeeService {

  private final CoffeeRepository coffeeRepository;

  public CoffeeService(CoffeeRepository coffeeRepository) {
    this.coffeeRepository = coffeeRepository;
  }

  public Flux<CoffeeEntity> findAll() {
    return coffeeRepository.findAll();
  }

  public Mono<CoffeeEntity> findOne(UUID id) {
    return coffeeRepository.findById(id);
  }
}
