package ru.udjin.sweater.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.*;

@Entity // This tells Hibernate to make a table out of this class
public class Message {
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  private Integer id;

  private String text;
  private String tag;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id")
  private User autor;

  public Message() {
  }

  public Message(String text, String tag, User user) {
    this.autor = user;
    this.text = text;
    this.tag = tag;
  }

  public String getAuthorName() {
    return autor != null ? autor.getUsername() : "<none>";
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getText() {
    return text;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public User getAutor() {
    return autor;
  }

  public void setAutor(User autor) {
    this.autor = autor;
  }
}
