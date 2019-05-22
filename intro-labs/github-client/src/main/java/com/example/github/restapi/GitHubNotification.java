package com.example.github.restapi;

import java.time.LocalDateTime;

/** GitHub Notifications. */
public class GitHubNotification {
  private String id;
  private GitHubRepository repository;
  private GitHubSubject subject;
  private String reason;
  private LocalDateTime updatedAt;
  private LocalDateTime lastReadAt;

  public String getId() {
    return id;
  }

  public LocalDateTime getLastReadAt() {
    return lastReadAt;
  }

  public void setLastread_at(LocalDateTime lastReadAt) {
    this.lastReadAt = lastReadAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdated_at(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public GitHubSubject getSubject() {
    return subject;
  }

  public void setSubject(GitHubSubject subject) {
    this.subject = subject;
  }

  public GitHubRepository getRepository() {
    return repository;
  }

  public void setRepository(GitHubRepository repository) {
    this.repository = repository;
  }

  public void setId(String id) {
    this.id = id;
  }
}
