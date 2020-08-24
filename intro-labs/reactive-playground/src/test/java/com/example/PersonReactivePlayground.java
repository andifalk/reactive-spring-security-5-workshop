package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

@DisplayName("Person Reactive Playground")
class PersonReactivePlayground {

  private static final Logger LOGGER = LoggerFactory.getLogger(PersonReactivePlayground.class.getName());

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

  @DisplayName("with persons")
  @Test
  void testWithPersons() {

    Flux.fromIterable(personSet).subscribe(s -> LOGGER.info(s.toString()));

    Flux<Person> personFlux = Flux.fromIterable(personSet).log();

    StepVerifier.create(personFlux).expectNextCount(7).verifyComplete();
  }
}
