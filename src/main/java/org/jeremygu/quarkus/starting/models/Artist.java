package org.jeremygu.quarkus.starting.models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import java.time.Instant;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "artists", schema = "uaprod")
public class Artist extends PanacheEntityBase {

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Column(name = "artist_id")
  public Long artistId;

  @Column(name = "name", nullable = false, length = 100)
  public String name;

  @Column(name = "bio")
  public String bio;

  @Column(name = "created_date", nullable = true, columnDefinition = "TIMESTAMP")
  public Instant createdDate = Instant.now();

  // constructors
  public Artist(String name, String bio) {
    this.name = name;
    this.bio = bio;
  }

  public Artist(String name, String bio, Instant createdDate) {
    this.name = name;
    this.bio = bio;
    this.createdDate = createdDate;
  }

  // JPA requires a no-arg constructor
  public Artist() {
  }
}
