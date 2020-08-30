package com.example.server.coffee;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document
public class CoffeeEntity {

  @Id private final UUID id;

  private final String name;

  @PersistenceConstructor
  public CoffeeEntity(UUID id, String name) {
    this.id = id;
    this.name = name;
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return "Coffee{" + "id=" + id + ", name='" + name + '\'' + '}';
  }
}
