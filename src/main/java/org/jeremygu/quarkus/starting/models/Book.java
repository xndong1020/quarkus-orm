package org.jeremygu.quarkus.starting.models;

public class Book {
  public Book(int id, String title, String author, int yearOfPublication, String genre) {
    this.id = id;
    this.title = title;
    this.author = author;
    this.yearOfPublication = yearOfPublication;
    this.genre = genre;
  }
  public Integer id;
  public String title;
  public String author;
  public int yearOfPublication;
  public String genre;

  public Integer getId() {
    return id;
  }
  public String getTitle() {
    return title;
  }
  public String getAuthor() {
    return author;
  }
  public int getYearOfPublication() {
    return yearOfPublication;
  }
  public String getGenre() {
    return genre;
  }
  public void setId(int id) {
    this.id = id;
  }
  public void setTitle(String title) {
    this.title = title;
  }
  public void setAuthor(String author) {
    this.author = author;
  }
  public void setYearOfPublication(int yearOfPublication) {
    this.yearOfPublication = yearOfPublication;
  }
  public void setGenre(String genre) {
    this.genre = genre;
  }
}