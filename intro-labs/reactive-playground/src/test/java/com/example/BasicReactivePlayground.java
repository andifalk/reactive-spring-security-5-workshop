package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.blockhound.BlockHound;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Basic reactive playground")
class BasicReactivePlayground {

  private static final Logger log = LoggerFactory.getLogger(BasicReactivePlayground.class);

  @BeforeEach
  void init() {
    BlockHound.install();
    Hooks.onOperatorDebug();
  }

  @DisplayName("imperative vs reactive")
  @Nested
  class ImperativeVsReactive {

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

  @DisplayName("create Mono")
  @Nested
  class createMono {

    @DisplayName("with empty()")
    @Test
    void testCreateMonoEmpty() {
      StepVerifier.create(Mono.empty()).expectNextCount(0).verifyComplete();
    }

    @DisplayName("with just()")
    @Test
    void testCreateMonoJust() {
      StepVerifier.create(Mono.just("Hello")).expectNext("Hello").verifyComplete();
    }

    @DisplayName("with justOrEmpty()")
    @Test
    void testCreateMonoJustOrEmpty() {
      StepVerifier.create(Mono.justOrEmpty(Optional.of("Hello")))
          .expectNext("Hello")
          .verifyComplete();
    }

    @DisplayName("with defer()")
    @Test
    void testCreateMonoDefer() {
      StepVerifier.create(Mono.defer(() -> Mono.just("Hello")))
          .expectNext("Hello")
          .verifyComplete();
    }

    @DisplayName("with create()")
    @Test
    void testCreateMonoCreate() {
      StepVerifier.create(Mono.create(sink -> sink.success("Hello")))
          .expectNext("Hello")
          .verifyComplete();
    }

    @DisplayName("with error()")
    @Test
    void testCreateMonoError() {
      StepVerifier.create(Mono.error(new IllegalArgumentException("error")))
          .expectError(IllegalArgumentException.class)
          .verify();
    }
  }

  @DisplayName("create Flux")
  @Nested
  class CreateFlux {

    @DisplayName("with empty()")
    @Test
    void testCreateFluxEmpty() {
      StepVerifier.create(Flux.empty()).expectNextCount(0).verifyComplete();
    }

    @DisplayName("with just()")
    @Test
    void testCreateFluxJust() {
      StepVerifier.create(Flux.just("Hello", "World"))
          .expectNext("Hello")
          .expectNext("World")
          .verifyComplete();
    }

    @DisplayName("with defer()")
    @Test
    void testCreateFluxDefer() {
      StepVerifier.create(Flux.defer(() -> Mono.just("Hello")))
          .expectNext("Hello")
          .verifyComplete();
    }

    @DisplayName("with defer()")
    @Test
    void testCreateFluxCreate() {
      StepVerifier.create(
              Flux.create(
                  sink -> {
                    sink.next("Hello");
                    sink.complete();
                  }))
          .expectNext("Hello")
          .verifyComplete();
    }

    @DisplayName("with fromArray()")
    @Test
    void testCreateFluxFromArray() {
      StepVerifier.create(Flux.fromArray(new String[] {"Hello"}))
          .expectNext("Hello")
          .verifyComplete();
    }

    @DisplayName("with fromIterable()")
    @Test
    void testCreateFluxFromIterable() {
      StepVerifier.create(Flux.fromIterable(Arrays.asList("Hello", "World")))
          .expectNext("Hello")
          .expectNext("World")
          .verifyComplete();
    }

    @DisplayName("with fromGenerate()")
    @Test
    void testCreateFluxFromGenerate() {
      StepVerifier.create(
              Flux.generate(
                  sink -> {
                    sink.next("Hello");
                    sink.complete();
                  }))
          .expectNext("Hello")
          .verifyComplete();
    }

    @DisplayName("with fromStream()")
    @Test
    void testCreateFluxFromStream() {
      StepVerifier.create(Flux.fromStream(Stream.of("Hello", "World")))
          .expectNext("Hello")
          .expectNext("World")
          .verifyComplete();
      StepVerifier.create(Flux.range(1, 5)).expectNextCount(5).verifyComplete();
      StepVerifier.create(Flux.error(new IllegalArgumentException("error")))
          .expectError(IllegalArgumentException.class)
          .verify();
    }

    @DisplayName("with range()")
    @Test
    void testCreateFluxRange() {
      StepVerifier.create(Flux.range(1, 5)).expectNextCount(5).verifyComplete();
      StepVerifier.create(Flux.error(new IllegalArgumentException("error")))
          .expectError(IllegalArgumentException.class)
          .verify();
    }

    @DisplayName("with error()")
    @Test
    void testCreateFluxError() {
      StepVerifier.create(Flux.error(new IllegalArgumentException("error")))
          .expectError(IllegalArgumentException.class)
          .verify();
    }
  }

  @DisplayName("transformations")
  @Nested
  class Transformations {

    @DisplayName("with collect()")
    @Test
    void testTransformWithCollect() {
      Mono<String> helloMono = Flux.just("Hello", "World").collect(Collectors.joining(" "));
      StepVerifier.create(helloMono).expectNext("Hello World").verifyComplete();
    }

    @DisplayName("with count()")
    @Test
    void testTransformWithCount() {
      StepVerifier.create(Flux.range(1, 10).count()).expectNext(10L).verifyComplete();
    }

    @DisplayName("with reduce()")
    @Test
    void testTransformWithReduce() {
      StepVerifier.create(Flux.range(1, 5).reduce(1, Integer::sum)).expectNext(16).verifyComplete();
    }

    @DisplayName("with handle()")
    @Test
    void testTransformWithHandle() {
      StepVerifier.create(
              Flux.range(1, 5)
                  .handle(
                      (consumer, sink) -> {
                        if (consumer > 3) sink.next(consumer);
                      }))
          .expectNextCount(2)
          .verifyComplete();
    }

    @DisplayName("with groupBy()")
    @Test
    void testTransformWithGroupBy() {
      StepVerifier.create(
              Flux.range(1, 10)
                  .log()
                  .groupBy(i -> i % 2 == 0 ? "even" : "odd")
                  .flatMap(s -> Flux.just(Objects.requireNonNull(s.key()))))
          .expectNextCount(2)
          .verifyComplete();
    }

    @DisplayName("with map()")
    @Test
    void testTransformWithMap() {
      StepVerifier.create(Flux.just(
              new Person("Peter", "Parker", new Address("Stuttgart", Country.GERMANY)),
              new Person("Max", "Muster", new Address("Berlin", Country.GERMANY)),
              new Person("Steffi", "Maier", new Address("Wien", Country.AUSTRIA)))
              .map(p -> p.getFirstName() + " " + p.getLastName() + " in " + p.getAddress().getCity()).log())
              .expectNext("Peter Parker in Stuttgart")
              .expectNext("Max Muster in Berlin")
              .expectNext("Steffi Maier in Wien")
              .verifyComplete();
    }

    @DisplayName("with flatMap()")
    @Test
    void testTransformWithFlatMap() {
      StepVerifier.create(Flux.just(
              new Person("Peter", "Parker", new Address("Stuttgart", Country.GERMANY)),
              new Person("Max", "Muster", new Address("Berlin", Country.GERMANY)),
              new Person("Steffi", "Maier", new Address("Wien", Country.AUSTRIA)))
              .flatMap(p -> Flux.just(p.getFirstName() + " " + p.getLastName(), "Lives in " + p.getAddress().getCity())).log())
              .expectNext("Peter Parker")
              .expectNext("Lives in Stuttgart")
              .expectNext("Max Muster")
              .expectNext("Lives in Berlin")
              .expectNext("Steffi Maier")
              .expectNext("Lives in Wien")
              .verifyComplete();
    }
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
        .subscribe(log::info, err -> log.error(err.getMessage(), err), () -> log.info("Completed"));
  }

  @DisplayName("delay")
  @Nested
  class Delays {

    @DisplayName("with delaySequence()")
    @Test
    void testDelaySequence() {
      Flux<Integer> sequence =
          Flux.range(1, 10).delaySequence(Duration.ofSeconds(1)).map(i -> i + 1).log();

      StepVerifier.create(sequence).expectNextCount(10).verifyComplete();
    }

    @DisplayName("with delayElements()")
    @Test
    void testDelayElements() {
      Flux<Integer> sequence =
          Flux.range(1, 10).delayElements(Duration.ofMillis(500)).map(i -> i + 1).log();

      StepVerifier.create(sequence).expectNextCount(10).verifyComplete();
    }

    @DisplayName("with delaySubscription()")
    @Test
    void testDelaySubscription() {
      Flux<Integer> sequence =
          Flux.range(1, 10).map(i -> i + 1).delaySubscription(Duration.ofSeconds(1)).log();

      StepVerifier.create(sequence).expectNextCount(10).verifyComplete();
    }
  }

  @Test
  void testBlockhound() {

    Scheduler canNotBlock = Schedulers.newParallel("test", 3);
    Scheduler canBlock = Schedulers.elastic();
    Flux<String> stringFlux =
        Flux.just("A", "B", "C", "D", "E", "F")
            .publishOn(canBlock)
            .map(String::toLowerCase)
            .log()
            .doOnNext(s -> blocking());

    StepVerifier.create(stringFlux).expectNextCount(6).verifyComplete();
  }

  private void blocking() {
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
