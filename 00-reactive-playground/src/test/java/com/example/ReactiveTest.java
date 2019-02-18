package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

@DisplayName("Reactive playground")
class ReactiveTest {

  private static final Logger LOGGER = Logger.getLogger(ReactiveTest.class.getName());

  private Set<Person> personSet;

  @BeforeEach
  void initializeTestObjects() {
    personSet = new HashSet<>();

    Person person = new Person("Max", "Mustermann");
    person.addAddress(new Address("Hauptstr.", "11", "70155", "Stuttgart", Country.GERMANY));
    personSet.add(person);
    person = new Person("Donald", "Duck");
    person.addAddress(new Address("Duckstr.", "20", "11111", "Entenhausen", Country.GERMANY));
    personSet.add(person);
    person = new Person("Dagobert", "Duck");
    person.addAddress(new Address("Duckstr.", "99", "11111", "Entenhausen", Country.GERMANY));
    personSet.add(person);
    person = new Person("Mickey", "Mouse");
    person.addAddress(new Address("Duckstr.", "75", "11111", "Entenhausen", Country.GERMANY));
    personSet.add(person);
    person = new Person("Daisy", "Duck");
    person.addAddress(new Address("Duckstr.", "80", "11111", "Entenhausen", Country.GERMANY));
    personSet.add(person);
    person = new Person("Stefan", "Müller");
    person.addAddress(new Address("Nebengasse", "2", "12345", "Wien", Country.AUSTRIA));
    personSet.add(person);
    person = new Person("Gerda", "Maier");
    person.addAddress(new Address("Zentralplatz", "2", "33333", "Basel", Country.SWITZERLAND));
    personSet.add(person);
  }

  @DisplayName("with strings")
  @Test
  void testWithString() throws InterruptedException {

    ParallelFlux<String> hello_reactive = Flux.just("Hello Reactive")
            .delayElements(Duration.ofMillis(100))
            .parallel()
            .runOn(Schedulers.newParallel("test", 5))
            .log()
            .map(String::toUpperCase)
            .flatMap(s -> Flux.just(s.split("")));
    hello_reactive.subscribe(System.out::println);

    Thread.sleep(500);

    //StepVerifier.create(hello_reactive).expectNext("H").expectNextCount(13).verifyComplete();
  }

  @DisplayName("with persons")
  @Test
  void testWithPersons() {

    Flux.fromIterable(personSet).subscribe(s -> LOGGER.info(s.toString()));

    Flux<Person> personFlux = Flux.fromIterable(personSet).log();

    StepVerifier.create(personFlux).expectNextCount(7).verifyComplete();
  }
}
