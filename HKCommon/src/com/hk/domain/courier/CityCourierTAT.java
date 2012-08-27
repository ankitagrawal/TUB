package com.hk.domain.courier;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.hk.domain.core.City;

/**
 * Created by IntelliJ IDEA.
 * User: Seema
 * Date: Jun 4, 2012
 * Time: 5:28:35 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "city_courier_tat")
public class CityCourierTAT {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "city_id")
  private City city;


  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "courier_id")
  private Courier courier;

  @Column(name = "turnaround_time")
  private Long turnaroundTime;

  public City getCity() {
    return city;
  }

  public void setCity(City city) {
    this.city = city;
  }

  public Courier getCourier() {
    return courier;
  }

  public void setCourier(Courier courier) {
    this.courier = courier;
  }

  public Long getTurnaroundTime() {
    return turnaroundTime;
  }

  public void setTurnaroundTime(Long turnaroundTime) {
    this.turnaroundTime = turnaroundTime;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
   @Override
  public String toString() {
    return id != null ? id.toString() : "";
  }

}
