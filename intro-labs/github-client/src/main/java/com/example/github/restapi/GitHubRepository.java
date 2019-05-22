package com.example.github.restapi;

public class GitHubRepository {
  private String id;
  private String name;
  private String fullName;

  public String getId() {
    return id;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFull_name(String fullName) {
    this.fullName = fullName;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setId(String id) {
    this.id = id;
  }
}
