package com.hk.domain.core;
// Generated Dec 26, 2011 1:31:41 PM by Hibernate Tools 3.2.4.CR1


import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierServiceInfo;
import com.hk.domain.courier.StateCourierService;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Pincode generated by hbm2java
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "pincode", uniqueConstraints = @UniqueConstraint(columnNames = "pincode"))
/*@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)*/
public class Pincode implements java.io.Serializable {


  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;


  @Column(name = "pincode", unique = true)
  private String pincode;


  @Column(name = "city", nullable = false, length = 25)
  private String city;


  @Column(name = "state", nullable = false, length = 25)
  private String state;

  @Column(name = "region", nullable = true, length = 25)
  private String region;


  @Column(name = "locality", length = 25)
  private String locality;

	@ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "default_courier_id", nullable = true)
  private Courier defaultCourier;


  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "pincode")
  private Set<CourierServiceInfo> courierServiceInfos = new HashSet<CourierServiceInfo>(0);


  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "pincode")
  private Set<StateCourierService> stateCourierService = new HashSet<StateCourierService>(0);


  public Set<StateCourierService> getStateCourierService() {
    return stateCourierService;
  }

  public void setStateCourierService(Set<StateCourierService> stateCourierService) {
    this.stateCourierService = stateCourierService;
  }
  

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getPincode() {
    return pincode;
  }

  public void setPincode(String pincode) {
    this.pincode = pincode;
  }

  public String getCity() {
    return this.city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getState() {
    return this.state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getLocality() {
    return this.locality;
  }

  public void setLocality(String locality) {
    this.locality = locality;
  }

  public Set<CourierServiceInfo> getCourierServiceInfos() {
    return this.courierServiceInfos;
  }

  public void setCourierServiceInfos(Set<CourierServiceInfo> courierServiceInfos) {
    this.courierServiceInfos = courierServiceInfos;
  }

	public Courier getDefaultCourier() {
		return defaultCourier;
	}

	public void setDefaultCourier(Courier defaultCourier) {
		this.defaultCourier = defaultCourier;
	}

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}


