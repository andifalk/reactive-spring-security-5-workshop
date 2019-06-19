package com.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

@DisplayName("Imperative versus reactive playground")
class ImperativeVsReactivePlayground {

  @DisplayName("imperative code")
  @Test
  void testImperative() {
    String msg = "World";
    String upperCaseMsg = msg.toUpperCase();
    String greeting = "Hello " + upperCaseMsg + "!";
    System.out.println(greeting);
  }

  @DisplayName("reactive code")
  @Test
  void testReactive() {
    Mono<String> mono = Mono.just("World")
            .map(String::toUpperCase)
            .map(um -> "Hello " + um + "!")
            .map(um -> {System.out.println(um); return um;});

    mono.subscribe();

    //StepVerifier.create(mono).expectNext("Hello WORLD!").verifyComplete();

  }
}
