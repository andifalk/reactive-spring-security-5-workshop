package com.example.server.coffee;

import java.util.UUID;

public class CoffeeResource {
  private final UUID id;
  private final String name;

  public CoffeeResource(UUID id, String name) {
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
