package org.jeremygu.quarkus.starting.models;

import java.util.HashSet;
import java.util.Set;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "films", schema = "uaprod")
public class Film extends PanacheEntityBase {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "film_id")
  public Long filmId;

  @Column(name = "title")
  public String title;

  @Column(name = "description")
  public String description;

  @Column(name = "rating")
  public String rating;

  /**
   * the @JoinTable annotation explicitly defines the join table and its columns,
   * marking this side as the owning side of the relationship.
   */
  @ManyToMany
  @JoinTable(name = "films_artists", schema = "uaprod", joinColumns = @JoinColumn(name = "film_id"), inverseJoinColumns = @JoinColumn(name = "artist_id"))
  public Set<Artist> artists = new HashSet<>();

  public Film(String title, String description, String rating) {
    this.title = title;
    this.description = description;
    this.rating = rating;
  }

  // JPA requires a no-arg constructor
  public Film() {
  }

}
