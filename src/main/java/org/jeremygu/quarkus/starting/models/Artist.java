package org.jeremygu.quarkus.starting.models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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

  /**
   * mappedBy = "artists" indicates that the Artist entity is the inverse side of
   * the relationship, and the artists field in the Film entity owns the
   * relationship.
   */
  @ManyToMany(mappedBy = "artists")
  @JsonbTransient  // Prevent serialization of this property to break the cycle
  public Set<Film> films = new HashSet<>();

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
