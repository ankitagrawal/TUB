package com.hk.domain.hkDelivery;

import com.hk.domain.warehouse.Warehouse;

import javax.persistence.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Ankit Chhabra
 * Date: 8/26/13
 * Time: 3:18 PM
 */

@Entity
@Table(name = "hk_reach_pricing_engine")
public class HKReachPricingEngine {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "warehouse_id")
  private Warehouse warehouse;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "hub_id")
  private Hub hub;

  @Column(name = "inter_city_cost")
  private Double interCityCost;

  @Column(name = "fixed_cost")
  private Double fixedCost;

  @Column(name = "update_time")
  private Date updateTime;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Warehouse getWarehouse() {
    return warehouse;
  }

  public void setWarehouse(Warehouse warehouse) {
    this.warehouse = warehouse;
  }

  public Hub getHub() {
    return hub;
  }

  public void setHub(Hub hub) {
    this.hub = hub;
  }

  public Double getInterCityCost() {
    return interCityCost;
  }

  public void setInterCityCost(Double interCityCost) {
    this.interCityCost = interCityCost;
  }

  public Double getFixedCost() {
    return fixedCost;
  }

  public void setFixedCost(Double lastMileCost) {
    this.fixedCost = lastMileCost;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }
}
