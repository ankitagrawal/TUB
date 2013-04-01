package com.hk.domain.courier;
// Generated Dec 26, 2011 1:31:41 PM by Hibernate Tools 3.2.4.CR1


import com.hk.domain.core.Pincode;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@SuppressWarnings("serial")
@Entity
@Table(name = "pincode_courier_mapping", uniqueConstraints = @UniqueConstraint(columnNames = {"pincode_id", "courier_id"}))
public class PincodeCourierMapping implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pincode_id", nullable = false)
    private Pincode pincode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courier_id", nullable = false)
    private Courier courier;

    @Column(name = "prepaid_air", nullable = false)
    private boolean prepaidAir;

    @Column(name = "prepaid_ground", nullable = false)
    private boolean prepaidGround;

    @Column(name = "cod_air", nullable = false)
    private boolean codAir;

    @Column(name = "cod_ground", nullable = false)
    private boolean codGround;

    @Column(name = "routing_code", length = 45)
    private String routingCode;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (!(o instanceof PincodeCourierMapping))
      return false;

    PincodeCourierMapping pincodeCourierMapping = (PincodeCourierMapping) o;
    if (pincodeCourierMapping.getPincode()!=null && pincodeCourierMapping.getCourier()!=null && this.pincode!=null && this.courier!=null) {
      return (pincodeCourierMapping.getPincode().equals(this.pincode) && pincodeCourierMapping.getCourier().equals(this.courier));
    }
    return false;
  }

  @Override
  public int hashCode() {
    return this.id != null ? id.hashCode() : 0;
  }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pincode getPincode() {
        return pincode;
    }

    public void setPincode(Pincode pincode) {
        this.pincode = pincode;
    }

    public Courier getCourier() {
        return courier;
    }

    public void setCourier(Courier courier) {
        this.courier = courier;
    }

    public boolean isPrepaidAir() {
        return prepaidAir;
    }

    public void setPrepaidAir(boolean prepaidAir) {
        this.prepaidAir = prepaidAir;
    }

    public boolean isPrepaidGround() {
        return prepaidGround;
    }

    public void setPrepaidGround(boolean prepaidGround) {
        this.prepaidGround = prepaidGround;
    }

    public boolean isCodAir() {
        return codAir;
    }

    public void setCodAir(boolean codAir) {
        this.codAir = codAir;
    }

    public boolean isCodGround() {
        return codGround;
    }

    public void setCodGround(boolean codGround) {
        this.codGround = codGround;
    }

    public String getRoutingCode() {
        return routingCode;
    }

    public void setRoutingCode(String routingCode) {
        this.routingCode = routingCode;
    }
}



