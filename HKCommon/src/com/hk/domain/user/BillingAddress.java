package com.hk.domain.user;

import com.akube.framework.gson.JsonSkip;
import com.hk.domain.order.Order;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

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
@PrimaryKeyJoinColumn(name="billing_address_id")
public class BillingAddress extends Address implements Serializable {

    @JsonSkip
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "base_order_has_billing_address",
            joinColumns = {@JoinColumn(name = "billing_address_id", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "base_order_id", nullable = false, updatable = false)}
    )
    private List<Order> orders       = new ArrayList<Order>();

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {                          
        this.orders = orders;
    }
    
}
