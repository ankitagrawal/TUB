package com.hk.report.dto.order.reconcilation;

import java.util.Date;

import com.hk.domain.courier.Courier;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.user.Address;
import com.hk.domain.warehouse.Warehouse;


public class ReconcilationReportDto {

    String invoiceId;
    Date orderDate;
    String name;
    String city;
    String pincode;
    String payment;
    Double total;
    String awb;
    Date shipmentDate;
    Date deliveryDate;
    //String reconciled;
    boolean reconciled;
    String orderStatus;
    Double boxWeight;
    String boxSize;
    Warehouse warehouse;
    Courier courier;
    ShippingOrder shippingOrder;
    Address address;
    String orderCategory;
    boolean isB2B;

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Courier getCourier() {
        return courier;
    }

    public void setCourier(Courier courier) {
        this.courier = courier;
    }

    public String getAwb() {
        return awb;
    }

    public void setAwb(String awb) {
        this.awb = awb;
    }

    public Date getShipmentDate() {
        return shipmentDate;
    }

    public void setshipmentDate(Date shipmentDate) {
        this.shipmentDate = shipmentDate;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    /*public String getReconciled() {
        return reconciled;
    }

    public void setReconciled(String reconciled) {
        this.reconciled = reconciled;
    }
*/

    public boolean isReconciled() {
        return reconciled;
    }

    public void setReconciled(boolean reconciled) {
        this.reconciled = reconciled;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Double getBoxWeight() {
        return boxWeight;
    }

    public void setBoxWeight(Double boxWeight) {
        this.boxWeight = boxWeight;
    }

    public String getBoxSize() {
        return boxSize;
    }

    public void setBoxSize(String boxSize) {
        this.boxSize = boxSize;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public ShippingOrder getShippingOrder() {
        return shippingOrder;
    }

    public void setShippingOrder(ShippingOrder shippingOrder) {
        this.shippingOrder = shippingOrder;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getOrderCategory() {
        return orderCategory;
    }

    public void setOrderCategory(String orderCategory) {
        this.orderCategory = orderCategory;
    }

    public boolean isB2B() {
        return isB2B;
    }

    public void setB2B(boolean b2B) {
        isB2B = b2B;
    }
}