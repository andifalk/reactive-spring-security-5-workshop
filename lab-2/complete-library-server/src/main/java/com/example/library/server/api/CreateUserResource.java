package com.example.library.server.api;

import com.example.library.server.common.Role;

import java.util.List;
import java.util.UUID;

public class CreateUserResource extends UserResource {

  private String password;

  public CreateUserResource() {}

  public CreateUserResource(
      UUID id, String email, String password, String firstName, String lastName, List<Role> roles) {
    super(id, email, firstName, lastName, roles);
    this.password = password;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
