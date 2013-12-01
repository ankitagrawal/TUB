package com.hk.domain.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Rajni
 * Date: May 29, 2012
 * Time: 12:29:20 PM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(name = "purchase_form_type")
public class PurchaseFormType implements java.io.Serializable{

  @Id
//  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @Column(name = "name", nullable = false, length = 3)
  private String name;

  @Column(name = "description", nullable = false, length = 50)
  private String description;

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

   public String getDescription() {
        return description;
    }

   public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return id == null ? "" : id.toString();
    }

}
