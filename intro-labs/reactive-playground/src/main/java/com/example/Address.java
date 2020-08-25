package com.example;

import java.util.Objects;

public class Address {

    private final String city;
    private final Country country;

    public Address(String city, Country country) {
        this.city = city;
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public Country getCountry() {
        return country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(city, address.city) &&
                country == address.country;
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, country);
    }

    @Override
    public String toString() {
        return "Address{" +
                ", city='" + city + '\'' +
                ", country=" + country +
                '}';
    }
}
