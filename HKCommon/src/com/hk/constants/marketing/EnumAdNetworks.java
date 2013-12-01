package com.hk.constants.marketing;

import com.hk.domain.marketing.AdNetworks;

/**
 * Generated
 */
public enum EnumAdNetworks {
  Google(10L, "Google"),
  Facebook(20L, "Facebook"),
	Vizury(30L, "Vizury");

  private java.lang.String name;
  private java.lang.Long id;

  EnumAdNetworks(Long id, java.lang.String name) {
    this.name = name;
    this.id = id;
  }

  public java.lang.String getName() {
    return name;
  }

  public java.lang.Long getId() {
    return id;
  }

  public AdNetworks asAdNetworks() {
    AdNetworks adNetworks = new AdNetworks();
    adNetworks.setId(this.getId());
    adNetworks.setName(this.getName());
    return adNetworks;
  }

}

