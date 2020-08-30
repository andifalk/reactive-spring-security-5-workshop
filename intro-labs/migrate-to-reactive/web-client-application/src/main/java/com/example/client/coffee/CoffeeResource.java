package com.example.client.coffee;

import java.util.UUID;

public class CoffeeResource {
  private UUID id;
  private String name;

  public void setId(UUID id) {
    this.id = id;
  }

  public void setName(String name) {
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
