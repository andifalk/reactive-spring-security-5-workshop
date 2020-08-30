package com.example.server.coffee;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class CoffeeResourceAssembler {

  public CoffeeResource toModel(CoffeeEntity entity) {
    return new CoffeeResource(entity.getId(), entity.getName());
  }

  public Collection<CoffeeResource> toCollectionModel(Collection<CoffeeEntity> entities) {
    return entities.stream().map(this::toModel).collect(Collectors.toList());
  }
}
