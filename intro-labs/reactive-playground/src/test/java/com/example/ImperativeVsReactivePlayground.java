package com.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Imperative versus reactive playground")
class ImperativeVsReactivePlayground {

  @DisplayName("imperative code")
  @Test
  void testImperative() {
    String msg = "World";
    String upperCaseMsg = msg.toUpperCase();
    String greeting = "Hello " + upperCaseMsg + "!";
    assertThat(greeting).isEqualTo("Hello WORLD!");
  }

  @DisplayName("functional code")
  @Test
  void testFunctional() {
    String greeting =
        Stream.of("World")
            .map(String::toUpperCase)
            .map(um -> "Hello " + um + "!")
            .collect(Collectors.joining());
    assertThat(greeting).isEqualTo("Hello WORLD!");
  }

  @DisplayName("reactive code")
  @Test
  void testReactive() {
    Mono<String> greeting =
        Mono.just("World").map(String::toUpperCase).map(um -> "Hello " + um + "!");
    StepVerifier.create(greeting).expectNext("Hello WORLD!").verifyComplete();
  }
}
