package com.hk.domain.affiliate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "affiliate_txn_type")
public class AffiliateTxnType {
  @Id
  @Column(name="id",nullable=false)
  Long id;

  @Column(name = "name",nullable=false,length=45)
  String name;

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
}
