package com.example.server.coffee;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.UUID;
import java.util.stream.Stream;

@Component
public class DataInitializer implements CommandLineRunner {

  private final Logger log = LoggerFactory.getLogger(DataInitializer.class);
  private final CoffeeRepository coffeeRepository;

  public DataInitializer(CoffeeRepository coffeeRepository) {
    this.coffeeRepository = coffeeRepository;
  }

  @Override
  public void run(String... args) {
    Flux.fromStream(
            Stream.of(
                new CoffeeEntity(UUID.randomUUID(), "Espresso"),
                new CoffeeEntity(UUID.randomUUID(), "Latte"),
                new CoffeeEntity(UUID.randomUUID(), "Cappuccino"),
                new CoffeeEntity(UUID.randomUUID(), "Black Coffee")))
        .flatMap(coffeeRepository::save)
        .then(coffeeRepository.count())
        .subscribe(c -> log.info("Created {} coffee entities", c));
  }
}
