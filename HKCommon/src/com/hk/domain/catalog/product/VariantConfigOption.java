package com.hk.domain.catalog.product;
// Generated Feb 7, 2012 4:39:32 PM by Hibernate Tools 3.2.4.CR1


import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;

/**
 * VariantConfigOption generated by hbm2java
 */
@Entity
@Table(name = "variant_config_option")
public class VariantConfigOption implements java.io.Serializable, Comparable<VariantConfigOption> {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @Column(name = "name", nullable = false, length = 45)
  private String name;

  @Column(name = "display_name", nullable = false, length = 50)
  private String displayName;

  @Column(name = "display_order", nullable = false)
  private long displayOrder;

  @Column(name = "additional_param", length = 20)
  private String additionalParam;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "variant_config_id", nullable = false)
  private VariantConfig variantConfig;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "variantConfigOption")
  @Sort(type= SortType.NATURAL)
  private Set<VariantConfigValues> variantConfigValues = new HashSet<VariantConfigValues>();


  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public VariantConfig getVariantConfig() {
    return this.variantConfig;
  }

  public void setVariantConfig(VariantConfig variantConfig) {
    this.variantConfig = variantConfig;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<VariantConfigValues> getVariantConfigValues() {
    return variantConfigValues;
  }

  public void setVariantConfigValues(Set<VariantConfigValues> variantConfigValues) {
    this.variantConfigValues = variantConfigValues;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public long getDisplayOrder() {
    return displayOrder;
  }

  public void setDisplayOrder(long displayOrder) {
    this.displayOrder = displayOrder;
  }

  public String getAdditionalParam() {
    return additionalParam;
  }

  public void setAdditionalParam(String additionalParam) {
    this.additionalParam = additionalParam;
  }

  public int compareTo(VariantConfigOption o) {
    return ((Long)o.getDisplayOrder()).compareTo(this.displayOrder);
  }
}


