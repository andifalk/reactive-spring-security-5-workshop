package com.example.server.coffee;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class DataInitializer implements CommandLineRunner {

  private final Logger log = LoggerFactory.getLogger(DataInitializer.class);
  private final CoffeeRepository coffeeRepository;

  public DataInitializer(CoffeeRepository coffeeRepository) {
    this.coffeeRepository = coffeeRepository;
  }

  @Transactional
  @Override
  public void run(String... args) {
    long result =
        Stream.of(
                new CoffeeEntity(UUID.randomUUID(), "Espresso"),
                new CoffeeEntity(UUID.randomUUID(), "Latte"),
                new CoffeeEntity(UUID.randomUUID(), "Cappuccino"),
                new CoffeeEntity(UUID.randomUUID(), "Black Coffee"))
            .map(coffeeRepository::save)
            .collect(Collectors.toList())
            .stream()
            .map(c -> coffeeRepository.count())
            .count();
    log.info("Created {} coffee entities", result);
  }
}
