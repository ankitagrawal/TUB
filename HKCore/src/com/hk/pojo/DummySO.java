package com.hk.pojo;

import com.hk.domain.courier.Courier;

/**
 * Created with IntelliJ IDEA.
 * User: Pratham
 * Date: 5/8/12
 * Time: 1:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class DummySO {

    DummyOrder dummyOrder;
    Courier courier;
    Long shipmentCost = 0L;

    public DummySO(DummyOrder dummyOrder) {
        this.dummyOrder = dummyOrder;
    }

    public DummyOrder getDummyOrder() {
        return dummyOrder;
    }

    public void setDummyOrder(DummyOrder dummyOrder) {
        this.dummyOrder = dummyOrder;
    }

    public Courier getCourier() {
        return courier;
    }

    public void setCourier(Courier courier) {
        this.courier = courier;
    }

    public Long getShipmentCost() {
        return shipmentCost;
    }

    public void setShipmentCost(Long shipmentCost) {
        this.shipmentCost = shipmentCost;
    }
}
