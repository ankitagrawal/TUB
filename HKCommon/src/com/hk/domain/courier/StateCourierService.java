package com.hk.domain.courier;
// Generated 5 Jan, 2012 2:39:47 PM by Hibernate Tools 3.2.4.CR1


import com.hk.domain.core.State;

import javax.persistence.*;

/**
 * StateCourierService generated by hbm2java
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "state_courier_service")
public class StateCourierService implements java.io.Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "courier_id")
  private Courier courier;


   @Column(name = "preference")
  private Long preference ;

  @ManyToOne
	@JoinColumn(name = "state_id")
  private State state ;

  public State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
  }

  public Long getPreference() {
    return preference;
  }

  public void setPreference(Long preference) {
    this.preference = preference;
  }



  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Courier getCourier() {
    return this.courier;
  }

  public void setCourier(Courier courier) {
    this.courier = courier;
  }


  @Override
  public String toString() {
    return id != null ? id.toString() : "";
  }

}


