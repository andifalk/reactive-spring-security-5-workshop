package com.example.server.coffee;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class CoffeeResourceAssembler {

  public CoffeeResource toModel(CoffeeEntity entity) {
    return new CoffeeResource(entity.getId(), entity.getName());
  }

  public Flux<CoffeeResource> toCollectionModel(Flux<CoffeeEntity> entities) {
    return entities.map(this::toModel);
  }
}
