package com.example;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Person {

    private final String firstName;
    private final String lastName;
    private final Set<Address> addresses;

    public Person(String firstName, String lastName, Set<Address> addresses) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.addresses = addresses != null ? addresses : new HashSet<>();
    }

    public Person(String firstName, String lastName) {
        this(firstName, lastName, new HashSet<>());
    }

    public void addAddress(Address address) {
        this.addresses.add(address);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Set<Address> getAddresses() {
        return addresses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(firstName, person.firstName) &&
                Objects.equals(lastName, person.lastName) &&
                Objects.equals(addresses, person.addresses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, addresses);
    }

    @Override
    public String toString() {
        return "Person{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", addresses=" + addresses +
                '}';
    }
}
