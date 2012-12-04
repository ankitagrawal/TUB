package com.hk.domain.core;
// Generated Dec 26, 2011 1:31:41 PM by Hibernate Tools 3.2.4.CR1


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.hk.domain.courier.Courier;
import org.apache.commons.lang.builder.EqualsBuilder;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private City city;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_id")
    private State state;

    @Column(name = "region", nullable = true, length = 25)
    private String region;


    @Column(name = "locality", length = 25)
    private String locality;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "default_courier_id", nullable = true)
    private Courier defaultCourier;

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

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getLocality() {
        return this.locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
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

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Pincode)) {
			return false;
		}
		Pincode pincode = (Pincode) obj;
		if (this.id != null && pincode.getId() != null) {
			return this.id.equals(pincode.getId());
		} else {
			if (this.pincode != null && pincode.getPincode() != null) {
				EqualsBuilder equalsBuilder = new EqualsBuilder();
				equalsBuilder.append(this.pincode, pincode.getPincode());
				return equalsBuilder.isEquals();
			}
			return false;
		}
	}
}


