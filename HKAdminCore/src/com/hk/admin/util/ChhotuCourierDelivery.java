package com.hk.admin.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.annotations.SerializedName;

/**
 * Created by IntelliJ IDEA.
 * User: Developer
 * Date: Jan 9, 2012
 * Time: 8:10:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class ChhotuCourierDelivery {

  private static Logger logger = LoggerFactory.getLogger(ChhotuCourierDelivery.class);

  public ChhotuCourierDelivery() {
  }

  @SerializedName("delivery_type")
  private String deliveryType;

  @SerializedName("cod_amount")
  private String codAmount;

  @SerializedName("customer_name")
  private String customerName;

  @SerializedName("pin_code")
  private String pinCode;

  @SerializedName("delivery_date")
  private String deliveryDate;

  @SerializedName("tracking_number")
  private String trackingId;

  @SerializedName("shipment_status")
  private String shipmentStatus;

  @SerializedName("STATUS")
  private String myStatus;

  public String getDeliveryType() {
    return deliveryType;
  }

  public void setDeliveryType(String deliveryType) {
    this.deliveryType = deliveryType;
  }

  public String getCodAmount() {
    return codAmount;
  }

  public void setCodAmount(String codAmount) {
    this.codAmount = codAmount;
  }

  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public String getPinCode() {
    return pinCode;
  }

  public void setPinCode(String pinCode) {
    this.pinCode = pinCode;
  }

  public String getDeliveryDate() {
    return deliveryDate;
  }

  public void setDeliveryDate(String deliveryDate) {
    this.deliveryDate = deliveryDate;
  }

  public String getTrackingId() {
    return trackingId;
  }

  public void setTrackingId(String trackingId) {
    this.trackingId = trackingId;
  }

  public String getShipmentStatus() {
    return shipmentStatus;
  }

  public void setShipmentStatus(String shipmentStatus) {
    this.shipmentStatus = shipmentStatus;
  }

  public String getMyStatus() {
    return myStatus;
  }

  public void setMyStatus(String myStatus) {
    this.myStatus = myStatus;
  }

  public Date getFormattedDeliveryDate() {
    Date formattedDate = null;
    if (deliveryDate != null) {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      try {
        formattedDate = sdf.parse(deliveryDate);
      }
      catch (Exception e) {
        logger.error(" exception in parsing chhotu courier deliver format : date was :" + deliveryDate + " tracking id : " + trackingId);
      }
    }

    return formattedDate;
  }
}
