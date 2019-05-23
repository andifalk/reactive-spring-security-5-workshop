package com.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@DisplayName("Basic reactive playground")
class BasicReactivePlayground {

  private static final Logger LOGGER = LoggerFactory.getLogger(BasicReactivePlayground.class);

  @DisplayName("create Mono")
  @Test
  void testCreateMono() {
    StepVerifier.create(Mono.empty()).expectNextCount(0).verifyComplete();
    StepVerifier.create(Mono.just("Hello")).expectNext("Hello").verifyComplete();
    StepVerifier.create(Mono.defer(() -> Mono.just("Hello"))).expectNext("Hello").verifyComplete();
    StepVerifier.create(Mono.create(sink -> sink.success("Hello"))).expectNext("Hello").verifyComplete();
    StepVerifier.create(Mono.justOrEmpty(Optional.of("Hello"))).expectNext("Hello").verifyComplete();
    StepVerifier.create(Mono.error(new IllegalArgumentException("error"))).expectError(IllegalArgumentException.class).verify();
  }

  @DisplayName("create Flux")
  @Test
  void testCreateFlux() {
    StepVerifier.create(Flux.empty()).expectNextCount(0).verifyComplete();
    StepVerifier.create(Flux.just("Hello", "World")).expectNext("Hello").expectNext("World").verifyComplete();
    StepVerifier.create(Flux.defer(() -> Mono.just("Hello"))).expectNext("Hello").verifyComplete();
    StepVerifier.create(Flux.create(sink -> { sink.next("Hello"); sink.complete(); })).expectNext("Hello").verifyComplete();
    StepVerifier.create(Flux.fromArray(new String[] {"Hello"})).expectNext("Hello").verifyComplete();
    StepVerifier.create(Flux.fromIterable(Arrays.asList("Hello", "World"))).expectNext("Hello").expectNext("World").verifyComplete();
    StepVerifier.create(Flux.generate(sink -> {sink.next("Hello"); sink.complete();})).expectNext("Hello").verifyComplete();
    StepVerifier.create(Flux.fromStream(Stream.of("Hello", "World"))).expectNext("Hello").expectNext("World").verifyComplete();
    StepVerifier.create(Flux.range(1, 5)).expectNextCount(5).verifyComplete();
    StepVerifier.create(Flux.error(new IllegalArgumentException("error"))).expectError(IllegalArgumentException.class).verify();
  }

  @DisplayName("transformations")
  @Test
  void testTransform() {
    Mono<String> helloMono = Flux.fromStream(Stream.of("Hello", "World"))
            .collect(Collectors.joining(" "));
    StepVerifier.create(helloMono).expectNext("Hello World").verifyComplete();

    StepVerifier.create(Flux.range(1, 10).count()).expectNext(10L).verifyComplete();
    StepVerifier.create(Flux.range(1, 5).reduce(1, Integer::sum)).expectNext(16).verifyComplete();
    StepVerifier.create(Flux.range(1, 5).handle((a,b) -> {if (a > 3) b.next(a);})).expectNextCount(2).verifyComplete();
    StepVerifier.create(Flux.range(1, 10)
            .groupBy(i -> i % 2 == 0 ? "even" : "odd")
            .flatMap(s -> Flux.just(s.key())))
            .expectNextCount(2).verifyComplete();

  }

  @DisplayName("subscription")
  @Test
  void testSubscribe() {
    Flux.range(1, 10)
        .map(
            i -> {
              if (i == 2) {
                throw new IllegalArgumentException("Invalid value " + i);
              } else {
                return i;
              }
            })
        .map(Object::toString)
        .subscribe(
            LOGGER::info,
            err -> LOGGER.error(err.getMessage(), err),
            () -> LOGGER.info("Completed"));
  }

  @DisplayName("delay elements with sleep")
  @Test
  void testDelayWithSleep() throws InterruptedException {

    Flux.range(1, 10)
        .log()
        .delayElements(Duration.ofMillis(50))
        .filter(i -> i > 3)
        .subscribe(null, null, () -> LOGGER.info("Completed"));

    Thread.sleep(500);
  }

  @DisplayName("delay elements with step verifier")
  @Test
  void testDelayWithStepVerifier() {

    Flux<Integer> rangeFlux =
        Flux.range(1, 10).log().delayElements(Duration.ofMillis(50)).filter(i -> i > 3);

    StepVerifier.create(rangeFlux).expectNextCount(7).verifyComplete();
  }
}
