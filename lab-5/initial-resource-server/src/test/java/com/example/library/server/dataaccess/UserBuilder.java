package com.example.library.server.dataaccess;

import com.example.library.server.common.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserBuilder {

  private UUID id = UUID.randomUUID();

  private String email = "john.doe@example.com";

  private String firstName = "John";

  private String lastName = "Doe";

  private List<Role> roles = new ArrayList<>();

  private UserBuilder() {
    roles.add(Role.LIBRARY_USER);
  }

  public static UserBuilder user() {
    return new UserBuilder();
  }

  public UserBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public UserBuilder withEmail(String email) {
    this.email = email;
    return this;
  }

  public UserBuilder withFirstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  public UserBuilder withLastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  public UserBuilder withRoles(List<Role> roles) {
    this.roles = roles;
    return this;
  }

  public User build() {
    return new User(this.id, this.email, this.firstName, this.lastName, this.roles);
  }
}
