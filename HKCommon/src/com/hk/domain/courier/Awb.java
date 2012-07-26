package com.hk.domain.courier;
// Generated Dec 27, 2011 7:04:58 PM by Hibernate Tools 3.2.4.CR1


import com.hk.domain.warehouse.Warehouse;

import javax.persistence.*;

/**
 * Awb generated by hbm2java
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "awb", uniqueConstraints = @UniqueConstraint(columnNames = {"courier_id", "awb_number"}))
public class Awb implements java.io.Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "courier_id", nullable = false)
  private Courier courier;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "warehouse_id", nullable = false)
  private Warehouse warehouse;


  @Column(name = "awb_number", nullable = false, length = 20)
  private String awbNumber;


  @Column(name = "awb_bar_code", nullable = false, length = 25)
  private String awbBarCode;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "awb_status_id", nullable = false)
  private AwbStatus awbStatus;

  @Column(name = "used", nullable = false)
  private boolean used;

  @Column(name = "cod", nullable = false)
  private boolean cod;

  public boolean getCod() {
    return cod;
  }

  public void setCod(boolean cod) {
    this.cod = cod;
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

  public Warehouse getWarehouse() {
    return this.warehouse;
  }

  public void setWarehouse(Warehouse warehouse) {
    this.warehouse = warehouse;
  }

  public String getAwbNumber() {
    return this.awbNumber;
  }

  public void setAwbNumber(String awbNumber) {
    this.awbNumber = awbNumber;
  }

  public String getAwbBarCode() {
    return this.awbBarCode;
  }

  public void setAwbBarCode(String awbBarCode) {
    this.awbBarCode = awbBarCode;
  }

  public boolean getUsed() {
    return this.used;
  }

  public void setUsed(boolean used) {
    this.used = used;
  }

    public AwbStatus getAwbStatus() {
        return awbStatus;
    }

    public void setAwbStatus(AwbStatus awbStatus) {
        this.awbStatus = awbStatus;
    }

    public boolean equals(Object obj) {
    if (!(obj instanceof Awb)) {
      throw new ClassCastException("object compared are not of same Type");

    }
    Awb awb = (Awb) obj;
    if (this.awbNumber.equals(awb.getAwbNumber())) {
      return true;
    } else
      return false;

  }


  

}


