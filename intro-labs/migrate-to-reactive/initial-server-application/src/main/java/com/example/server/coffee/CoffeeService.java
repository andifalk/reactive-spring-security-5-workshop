package com.example.server.coffee;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Service
public class CoffeeService {

  private final CoffeeRepository coffeeRepository;

  public CoffeeService(CoffeeRepository coffeeRepository) {
    this.coffeeRepository = coffeeRepository;
  }

  public Collection<CoffeeEntity> findAll() {
    return coffeeRepository.findAll();
  }

  public Optional<CoffeeEntity> findOne(UUID id) {
    return coffeeRepository.findById(id);
  }
}
