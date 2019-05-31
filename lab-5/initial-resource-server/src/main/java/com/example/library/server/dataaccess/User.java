package com.example.library.server.dataaccess;

import com.example.library.server.common.Role;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Document(collection = "users")
public class User {

  @Id private UUID id;

  @Indexed private String email;

  private String firstName;

  private String lastName;

  private List<Role> roles;

  public User() {}

  public User(User user) {
    this(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getRoles());
  }

  @PersistenceConstructor
  public User(UUID id, String email, String firstName, String lastName, List<Role> roles) {
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
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return id.equals(user.id)
        && email.equals(user.email)
        && firstName.equals(user.firstName)
        && lastName.equals(user.lastName)
        && roles.equals(user.roles);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, email, firstName, lastName, roles);
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
