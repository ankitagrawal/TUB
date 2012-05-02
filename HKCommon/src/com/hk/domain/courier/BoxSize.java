package com.hk.domain.courier;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@SuppressWarnings("serial")
@Entity
@Table(name = "box_size")
public class BoxSize implements java.io.Serializable {

  @Id
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @Column(name = "name", nullable = false, length = 45)
  private String name;

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return id == null ? "" : id.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (!(o instanceof BoxSize))
    return false;

    BoxSize boxSize = (BoxSize) o;
    return id.equals(boxSize.getId());
  }

  @Override
    public int hashCode() {
      int result = id.hashCode();
      result = 31 * result + name.hashCode();
      return result;
    }

}