package com.example.library.server.api;

import com.example.library.server.common.Role;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;
import java.util.UUID;

public class UserResource {

  private UUID id;

  private String email;

  private String firstName;

  private String lastName;

  private List<Role> roles;

  public UserResource() {}

  public UserResource(UUID id, String email, String firstName, String lastName, List<Role> roles) {
    this.id = id;
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
    this.roles = roles;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public List<Role> getRoles() {
    return roles;
  }

  public void setRoles(List<Role> roles) {
    this.roles = roles;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("id", id)
        .append("email", email)
        .append("firstName", firstName)
        .append("lastName", lastName)
        .append("roles", roles)
        .toString();
  }
}
