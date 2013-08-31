package com.hk.domain.courier;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.hk.domain.warehouse.Warehouse;

import java.util.Date;

@SuppressWarnings("serial")
@Entity
@Table(name = "courier_pricing_engine")
public class CourierPricingEngine implements java.io.Serializable {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "courier_id", nullable = false)
  private Courier courier;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "warehouse_id", nullable = false)
  private Warehouse warehouse;


  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "region_type_id", nullable = false)
  private RegionType regionType;


  @Column(name = "first_base_wt", nullable = false, precision = 6)
  private Double firstBaseWt;


  @Column(name = "first_base_cost", nullable = false, precision = 6)
  private Double firstBaseCost;


  @Column(name = "second_base_wt", precision = 6)
  private Double secondBaseWt;


  @Column(name = "second_base_cost", precision = 6)
  private Double secondBaseCost;


  @Column(name = "additional_wt", nullable = false, precision = 6)
  private Double additionalWt;


  @Column(name = "additional_cost", nullable = false, precision = 6)
  private Double additionalCost;


  @Column(name = "fuel_surcharge", precision = 5, scale = 4)
  private Double fuelSurcharge;


  @Column(name = "min_cod_charges", precision = 6)
  private Double minCodCharges;


  @Column(name = "cod_cutoff_amount", precision = 5)
  private Double codCutoffAmount;


  @Column(name = "variable_cod_charges", precision = 5, scale = 4)
  private Double variableCodCharges;

  @Column(name = "valid_upto")
  private Date validUpto;

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Courier getCourier() {
    return courier;
  }

  public void setCourier(Courier courier) {
    this.courier = courier;
  }

  public Warehouse getWarehouse() {
    return warehouse;
  }

  public void setWarehouse(Warehouse warehouse) {
    this.warehouse = warehouse;
  }

  public RegionType getRegionType() {
    return regionType;
  }

  public void setRegionType(RegionType regionType) {
    this.regionType = regionType;
  }

  public Double getFirstBaseWt() {
    return this.firstBaseWt;
  }

  public void setFirstBaseWt(Double firstBaseWt) {
    this.firstBaseWt = firstBaseWt;
  }

  public Double getFirstBaseCost() {
    return this.firstBaseCost;
  }

  public void setFirstBaseCost(Double firstBaseCost) {
    this.firstBaseCost = firstBaseCost;
  }

  public Double getSecondBaseWt() {
    return this.secondBaseWt;
  }

  public void setSecondBaseWt(Double secondBaseWt) {
    this.secondBaseWt = secondBaseWt;
  }

  public Double getSecondBaseCost() {
    return this.secondBaseCost;
  }

  public void setSecondBaseCost(Double secondBaseCost) {
    this.secondBaseCost = secondBaseCost;
  }

  public Double getAdditionalWt() {
    return this.additionalWt;
  }

  public void setAdditionalWt(Double additionalWt) {
    this.additionalWt = additionalWt;
  }

  public Double getAdditionalCost() {
    return this.additionalCost;
  }

  public void setAdditionalCost(Double additionalCost) {
    this.additionalCost = additionalCost;
  }

  public Double getFuelSurcharge() {
    return this.fuelSurcharge;
  }

  public void setFuelSurcharge(Double fuelSurcharge) {
    this.fuelSurcharge = fuelSurcharge;
  }

  public Double getMinCodCharges() {
    return this.minCodCharges;
  }

  public void setMinCodCharges(Double minCodCharges) {
    this.minCodCharges = minCodCharges;
  }

  public Double getCodCutoffAmount() {
    return this.codCutoffAmount;
  }

  public void setCodCutoffAmount(Double codCutoffAmount) {
    this.codCutoffAmount = codCutoffAmount;
  }

  public Double getVariableCodCharges() {
    return this.variableCodCharges;
  }

  public void setVariableCodCharges(Double variableCodCharges) {
    this.variableCodCharges = variableCodCharges;
  }

  public Date getValidUpto() {
    return validUpto;
  }

  public void setValidUpto(Date validUpto) {
    this.validUpto = validUpto;
  }
}
