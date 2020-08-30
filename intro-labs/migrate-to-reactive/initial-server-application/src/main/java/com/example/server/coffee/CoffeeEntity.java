package com.example.server.coffee;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import java.util.UUID;

@Entity
public class CoffeeEntity extends AbstractPersistable<UUID> {

  private UUID identifier;

  private String name;

  public CoffeeEntity() {}

  public CoffeeEntity(UUID identifier, String name) {
    this.identifier = identifier;
    this.name = name;
  }

  public UUID getIdentifier() {
    return identifier;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return "Coffee{" + "identifier=" + identifier + ", name='" + name + '\'' + '}';
  }
}
