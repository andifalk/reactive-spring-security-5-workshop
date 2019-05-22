package com.example.github.restapi;

public class GitHubSubject {
  private String title;
  private String url;
  private String type;
  private String latestCommentUrl;

  public String getTitle() {
    return title;
  }

  public String getLatestCommentUrl() {
    return latestCommentUrl;
  }

  public void setLatestCommentUrl(String latestCommentUrl) {
    this.latestCommentUrl = latestCommentUrl;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public void setTitle(String title) {
    this.title = title;
  }
}
