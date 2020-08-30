package com.example.server.coffee;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CoffeeRepository extends JpaRepository<CoffeeEntity, UUID> {}
