package com.example;

import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;
import reactor.util.function.Tuples;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Basic reactive playground")
@Tag("manual")
class BasicReactivePlaygroundTest {

  private static final Logger log = LoggerFactory.getLogger(BasicReactivePlaygroundTest.class);

  @BeforeEach
  void init() {
    BlockHound.install();
    Hooks.onOperatorDebug();
  }

  @DisplayName("imperative vs reactive")
  @Nested
  class ImperativeReactiveTest {

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
  class CreateMonoTest {

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
  class CreateFluxTest {

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
  class TransformationTest {

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
      StepVerifier.create(
              Flux.just(
                      new Person("Peter", "Parker", new Address("Stuttgart", Country.GERMANY)),
                      new Person("Max", "Muster", new Address("Berlin", Country.GERMANY)),
                      new Person("Steffi", "Maier", new Address("Wien", Country.AUSTRIA)))
                  .map(
                      p ->
                          p.getFirstName()
                              + " "
                              + p.getLastName()
                              + " in "
                              + p.getAddress().getCity())
                  .log())
          .expectNext("Peter Parker in Stuttgart")
          .expectNext("Max Muster in Berlin")
          .expectNext("Steffi Maier in Wien")
          .verifyComplete();
    }

    @DisplayName("with flatMap()")
    @Test
    void testTransformWithFlatMap() {
      StepVerifier.create(
              Flux.just(
                      new Person("Peter", "Parker", new Address("Stuttgart", Country.GERMANY)),
                      new Person("Max", "Muster", new Address("Berlin", Country.GERMANY)),
                      new Person("Steffi", "Maier", new Address("Wien", Country.AUSTRIA)))
                  .flatMap(
                      p ->
                          Flux.just(
                              p.getFirstName() + " " + p.getLastName(),
                              "Lives in " + p.getAddress().getCity()))
                  .log())
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
  class DelayTest {

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

  @DisplayName("streams")
  @Test
  void testStream() {
    Flux<LocalDateTime> dateTimeFlux = Flux.fromStream(Stream.generate(LocalDateTime::now)).log();
    StepVerifier.create(dateTimeFlux).expectNextCount(10).thenCancel().verify();
  }

  @DisplayName("blocking")
  @Nested
  class BlockingTest {

    @DisplayName("detection")
    @Test
    void testBlockDetection() {

      Scheduler canNotBlock = Schedulers.newParallel("eventLoop", 4);
      Flux<String> stringFlux =
          Flux.just("a", "b", "c", "d", "e", "f")
              .subscribeOn(canNotBlock)
              .log()
              .map(this::blockingOperation);

      StepVerifier.create(stringFlux).expectError(BlockingOperationError.class).verify();
    }

    @DisplayName("wrapping")
    @Test
    void testWrapBlockingCall() {
      Flux<String> stringFlux =
          Flux.just("a", "b", "c", "d", "e", "f").log().flatMap(this::blockingWrapper);
      StepVerifier.create(stringFlux).expectNext("A", "B", "C", "D", "E", "F").verifyComplete();
    }

    private Mono<String> blockingWrapper(String s) {
      return Mono.fromCallable(() -> blockingOperation(s)).subscribeOn(Schedulers.boundedElastic());
    }

    private String blockingOperation(String input) {
      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        // ignore
      }
      return input.toUpperCase();
    }
  }

  @DisplayName("merge streams")
  @Nested
  class MergeStreamTest {

    @DisplayName("concat")
    @RepeatedTest(5)
    void concat() {
      Flux<Integer> flux1 = Flux.range(1, 5).delayElements(Duration.ofMillis(200));
      Flux<Integer> flux2 = Flux.range(6, 5);
      StepVerifier.create(flux1.concatWith(flux2))
          .expectNext(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
          .verifyComplete();
    }

    @DisplayName("merge")
    @RepeatedTest(5)
    void merge() {
      Flux<Integer> flux1 = Flux.range(1, 5).delayElements(Duration.ofMillis(200));
      Flux<Integer> flux2 = Flux.range(6, 5);
      StepVerifier.create(flux1.mergeWith(flux2).log())
          .expectNext(6, 7, 8, 9, 10, 1, 2, 3, 4, 5)
          .verifyComplete();
    }

    @DisplayName("merge sequential")
    @RepeatedTest(5)
    void mergeSequential() {
      Flux<Integer> flux1 = Flux.range(1, 5).delayElements(Duration.ofMillis(200));
      Flux<Integer> flux2 = Flux.range(6, 5);
      StepVerifier.create(Flux.mergeSequential(flux1, flux2))
          .expectNext(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
          .verifyComplete();
    }

    @DisplayName("zip with")
    @RepeatedTest(5)
    void zipWith() {
      Flux<Integer> flux1 = Flux.range(1, 5).delayElements(Duration.ofMillis(200));
      Flux<Integer> flux2 = Flux.range(6, 5);
      StepVerifier.create(flux1.zipWith(flux2))
          .expectNext(
              Tuples.of(1, 6), Tuples.of(2, 7), Tuples.of(3, 8), Tuples.of(4, 9), Tuples.of(5, 10))
          .verifyComplete();
    }
  }

  // In Reactive Streams, errors are terminal events i.e. it stops the sequence
  // and gets propagated to the last step (the subscriber)
  @DisplayName("error handling")
  @Nested
  class ErrorHandlingTest {

    @DisplayName("try-catch-return")
    @Test
    void tryCatchWithReturn() {
      StepVerifier.create(Flux.range(1, 10).log().map(this::someOtherCalc).onErrorReturn(6))
          .expectNext(2, 6)
          .expectComplete()
          .verify();
    }

    @DisplayName("try-catch-resume")
    @Test
    void tryCatchWithResume() {
      StepVerifier.create(
              Flux.range(1, 10).log().map(this::someOtherCalc).onErrorResume(t -> Mono.just(42)))
          .expectNext(2, 42)
          .expectComplete()
          .verify();
    }

    @DisplayName("try-catch-continue")
    @Test
    void tryCatchWithContinue() {
      StepVerifier.create(
              Flux.range(1, 10)
                  .log()
                  .map(this::someOtherCalc)
                  .onErrorContinue((t, c) -> log.error(t.getMessage())))
          .expectNext(2, 6, 8, 10, 12, 14, 16, 18, 20)
          .expectComplete()
          .verify();
    }

    @DisplayName("try-catch-remap")
    @Test
    void tryCatchWithRemap() {
      StepVerifier.create(
              Flux.range(1, 10)
                  .log()
                  .map(this::someOtherCalc)
                  .onErrorMap(originalThrowable -> new RuntimeException("My own exception")))
          .expectNext(2)
          .expectError(RuntimeException.class)
          .verify();
    }

    @DisplayName("try-catch-log")
    @Test
    void tryCatchWithLog() {
      StepVerifier.create(
              Flux.range(1, 10)
                  .log()
                  .map(this::someOtherCalc)
                  .doOnError(
                      throwable -> log.error("Error in calculation: {}", throwable.getMessage())))
          .expectNext(2)
          .expectError(IllegalArgumentException.class)
          .verify();
    }

    @DisplayName("try-catch-finally")
    @Test
    void tryCatchFinally() {
      StepVerifier.create(
              Flux.range(1, 10)
                  .log()
                  .map(this::someOtherCalc)
                  .doOnError(
                      throwable -> log.error("Error in calculation: {}", throwable.getMessage()))
                  .doFinally(signalType -> log.info("Finally: {}", signalType.name())))
          .expectNext(2)
          .expectError(IllegalArgumentException.class)
          .verify();
    }

    @DisplayName("try-catch-checked-exception")
    @Test
    void tryCatchWithHandle() {
      StepVerifier.create(
              Flux.range(1, 10)
                  .handle(
                      (input, sink) -> {
                        try {
                          sink.next(someCalc(input));
                        } catch (InvalidCalculationError ex) {
                          sink.error(ex);
                        }
                      }))
          .expectNext(2)
          .expectError()
          .verify();
    }

    private int someCalc(int i) throws InvalidCalculationError {
      if (i == 2) {
        throw new InvalidCalculationError("Invalid number " + i);
      } else {
        return i * 2;
      }
    }

    private int someOtherCalc(int i) {
      if (i == 2) {
        throw new IllegalArgumentException("Invalid number " + i);
      } else {
        return i * 2;
      }
    }

    class InvalidCalculationError extends Exception {
      InvalidCalculationError(String message) {
        super(message);
      }
    }
  }

  @DisplayName("your playground")
  @Test
  void playgroundTest() {}
}
