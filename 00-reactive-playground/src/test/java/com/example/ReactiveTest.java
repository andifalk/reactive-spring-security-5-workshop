package com.example;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

class ReactiveTest {

  @Test
  void testSimple() {
    Flux<String> aFlux = Flux.just("MyString");
    // Flux<String> aFlux = Flux.just("MyString").log();

    aFlux.subscribe(f -> System.out.println("Here's some value: " + f));

    // StepVerifier.create(aFlux).expectNext("MyString").verifyComplete();
  }
}
