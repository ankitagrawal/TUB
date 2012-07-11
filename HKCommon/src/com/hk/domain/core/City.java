package com.hk.domain.core;

import javax.persistence.*;
import java.util.Comparator;

/**
 * Created by IntelliJ IDEA.
 * User:User
 * Date: Jun 20, 2012
 * Time: 7:15:12 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "city")
public class City implements java.io.Serializable,Comparator<City>{

  @Id
  @Column(name = "id", unique = true, nullable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;


  @Column(name = "name", nullable = false, length = 45)
  private String name;


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

  @Override
  public String toString() {
    return id == null ? "" : id.toString();
  }

  public int compare(City city1, City city2) {
      return city1.getName().toUpperCase().compareTo(city2.getName().toUpperCase());

    }
}
