package com.hk.pojo;

import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.hk.domain.core.Pincode;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.payment.Payment;
import com.hk.domain.warehouse.Warehouse;

/**
 * Created with IntelliJ IDEA.
 * User: Pratham
 * Date: 5/25/12
 * Time: 5:33 PM
 * To change this template use File | Settings | File Templates.
 */

public class DummyOrder {

    List<CartLineItem> cartLineItemList;

    Warehouse warehouse;

    Double weight = 0D;

    Double amount = 0D;

    boolean isCod;

    boolean isGround;

    boolean isDropShip;

    boolean containsJIT;

    Pincode pincode;

    Payment payment;

    DummySO dummySO;

    Double taxIncurred = 0D;


    public DummyOrder(List<CartLineItem> cartLineItemList, Warehouse warehouse, boolean cod, Pincode pincode, Payment payment) {
        this.cartLineItemList = cartLineItemList;
        this.warehouse = warehouse;
        isCod = cod;
        this.pincode = pincode;
        this.payment = payment;
    }

    public Double getAmount() {
        amount = 0D;
        for (CartLineItem cartLineItem : cartLineItemList) {
            amount += (cartLineItem.getProductVariant().getHkPrice() * cartLineItem.getQty()) - cartLineItem.getDiscountOnHkPrice();
        }
        return amount;
    }

    public boolean isGround() {
        for (CartLineItem cartLineItem : cartLineItemList) {
            if (!cartLineItem.getProductVariant().getProduct().isGroundShipping()) {
                return false;
            }
        }
        return true;
    }

    public Double getWeight() {
        weight = 0D;
        Double variantWt = 0D;
        for (CartLineItem cartLineItem : cartLineItemList) {
            variantWt = cartLineItem.getProductVariant().getWeight();
            if (variantWt == null || variantWt == 0) {
                variantWt = 125D;
            }
            weight += variantWt * cartLineItem.getQty();
        }
        return weight;
    }

    public void setGround(boolean ground) {
        isGround = ground;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public boolean isCod() {
        return isCod;
    }

    public void setCod(boolean cod) {
        isCod = cod;
    }

    public Pincode getPincode() {
        return pincode;
    }

    public void setPincode(Pincode pincode) {
        this.pincode = pincode;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public List<CartLineItem> getCartLineItemList() {
        return cartLineItemList;
    }

    public void setCartLineItemList(List<CartLineItem> cartLineItemList) {
        this.cartLineItemList = cartLineItemList;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public DummySO getDummySO() {
        return dummySO;
    }

    public void setDummySO(DummySO dummySO) {
        this.dummySO = dummySO;
    }

    public Double getTaxIncurred() {
        return taxIncurred;
    }

    public void setTaxIncurred(Double taxIncurred) {
        this.taxIncurred = taxIncurred;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (o instanceof DummyOrder) {
            DummyOrder dummyOrder = (DummyOrder) o;

            EqualsBuilder equalsBuilder = new EqualsBuilder();
            if (this.getCartLineItemList() != null && dummyOrder.getCartLineItemList() != null) {
                equalsBuilder.append(this.getCartLineItemList(), dummyOrder.getCartLineItemList());
            }
            if (this.getPincode() != null && dummyOrder.getPincode() != null) {
                equalsBuilder.append(this.getPincode(), dummyOrder.getPincode());
            }
            if (this.getPayment() != null && dummyOrder.getPayment() != null) {
                equalsBuilder.append(this.getPayment(), dummyOrder.getPayment());
            }
            if (this.getWarehouse() != null && dummyOrder.getWarehouse() != null) {
                equalsBuilder.append(this.getWarehouse(), dummyOrder.getWarehouse());
            }
            return equalsBuilder.isEquals();
        }
        return false;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        if (this.getCartLineItemList() != null) {
            hashCodeBuilder.append(this.getCartLineItemList());
        }
        if (this.getPincode() != null) {
            hashCodeBuilder.append(this.getPincode().getPincode());
        }
        if (this.getWarehouse() != null) {
            hashCodeBuilder.append(this.getWarehouse().getId());
        }
        return hashCodeBuilder.toHashCode();
    }

    @Override
    public String toString() {

        StringBuilder cartLineItemList = new StringBuilder();

        for (CartLineItem cartLineItem : this.getCartLineItemList()) {
            cartLineItemList.append(cartLineItem.getProductVariant().getId());
            cartLineItemList.append("_#");
        }

        return "DummyOrder{" +
                "cartLineItemList=" + cartLineItemList +
                ", warehouse=" + warehouse.getIdentifier() +
                '}';
    }
}
