package com.hk.domain.order;
// Generated May 18, 2012 5:47:39 PM by Hibernate Tools 3.2.4.CR1


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SecondaryReferrerForOrder generated by hbm2java
 */
@Entity
@Table(name="secondary_referrer_for_order")
public class SecondaryReferrerForOrder  implements java.io.Serializable {


 
    @Id 
    
    @Column(name="id", unique=true, nullable=false)
    private Long id;
 
    
    @Column(name="name", length=100)
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


}


