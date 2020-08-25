package com.example;

import java.util.Objects;

public class Person {

  private final String firstName;
  private final String lastName;
  private final Address address;

  public Person(String firstName, String lastName, Address address) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.address = address;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public Address getAddress() {
    return address;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Person person = (Person) o;
    return Objects.equals(firstName, person.firstName)
        && Objects.equals(lastName, person.lastName)
        && Objects.equals(address, person.address);
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstName, lastName, address);
  }

  @Override
  public String toString() {
    return "Person{"
        + "firstName='"
        + firstName
        + '\''
        + ", lastName='"
        + lastName
        + '\''
        + ", addresses="
        + address
        + '}';
  }
}
