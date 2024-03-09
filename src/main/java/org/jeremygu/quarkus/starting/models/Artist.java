package org.jeremygu.quarkus.starting.models;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_artists")
public class Artist {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY) // For auto-generated primary key
  private Long id;

  @Column(name = "name", nullable = false, length = 100)
  private String name;

  @Column(name = "bio")
  private String bio;

  @Column(name = "created_date", nullable = true, columnDefinition = "TIMESTAMP")
  private Instant createdDate = Instant.now();

  // constructors
  public Artist(String name, String bio) {
    this.name = name;
    this.bio = bio;
  }

  // JPA requires a no-arg constructor
  public Artist() {
  }

  public Artist(Long id, String name, String bio, Instant createdDate) {
    this.id = id;
    this.name = name;
    this.bio = bio;
    this.createdDate = createdDate;
  }

  // getters & setters
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
