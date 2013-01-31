package com.hk.domain.user;

import javax.persistence.*;
import java.io.Serializable;


/**
 * Created by IntelliJ IDEA.
 * User: Ankit
 * Date: Nov 7, 2012
 * Time: 6:27:04 PM
 * To change this template use File | Settings | File Templates.
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "billing_address")
@PrimaryKeyJoinColumn(name = "billing_address_id")
public class BillingAddress extends Address implements Serializable {

  @Column(name = "pincode", nullable = false)
  private String pin;

  public String getPin() {
    return pin;
  }

  public void setPin(String pin) {
    this.pin = pin;
  }
}
