package com.hk.domain.order;

import com.hk.domain.user.BillingAddress;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Id;

/**
 * Created by IntelliJ IDEA.
 * User: Ankit
 * Date: Nov 8, 2012
 * Time: 5:16:05 PM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(name = "base_order_has_billing_address")
public class OrderBillingAddress implements java.io.Serializable {

    @Id
    @Column(name = "base_order_id", nullable = false, length = 150)
    private Order order;

    @Column(name = "billing_address_id", nullable = false, length = 150)
    private BillingAddress billingAddress ;
    

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public BillingAddress getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(BillingAddress billingAddress) {
        this.billingAddress = billingAddress;
    }
}
