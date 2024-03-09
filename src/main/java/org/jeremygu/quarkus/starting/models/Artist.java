package org.jeremygu.quarkus.starting.models;

import java.time.Instant;

public class Artist {
  public Artist(Long id, String name, String bio, Instant createdDate) {
    this.id = id;
    this.name = name;
    this.bio = bio;
    this.createdDate = createdDate;
  }

  // must have a parameterless constructor for create artist
  public Artist() {
  }

  private Long id;
  private String name;
  private String bio;
  private Instant createdDate = Instant.now();

  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getBio() {
    return bio;
  }
  public void setBio(String bio) {
    this.bio = bio;
  }
  public Instant getCreatedDate() {
    return createdDate;
  }
  public void setCreatedDate(Instant createdDate) {
    this.createdDate = createdDate;
  }
}
