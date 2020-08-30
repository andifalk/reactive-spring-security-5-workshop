package com.example.server.coffee;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import java.util.UUID;

public interface CoffeeRepository extends ReactiveMongoRepository<CoffeeEntity, UUID> {}
