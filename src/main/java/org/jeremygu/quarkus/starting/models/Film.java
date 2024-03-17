package org.jeremygu.quarkus.starting.models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "artists", schema = "uaprod")
public class Film extends PanacheEntityBase {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long filmId;

  @Column(name = "title")
  public String title;

  @Column(name = "description")
  public String description;

  @Column(name = "rating")
  public String rating;

}
