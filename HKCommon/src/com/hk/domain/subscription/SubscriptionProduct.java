package com.hk.domain.subscription;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.hk.domain.catalog.product.Product;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: Jun 17, 2012
 * Time: 1:14:28 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="subscription_product")
public class SubscriptionProduct{

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", unique = true, nullable = false)
    private Product product;

    @Column(name = "subscription_discount_180days", nullable = false)
    private Double subscriptionDiscount180Days;

    @Column(name = "subscription_discount_360days", nullable = false)
    private Double subscriptionDiscount360Days;

    @Column(name = "min_frequency_days", nullable = false)
    private int minFrequencyDays;

    @Column(name = "max_frequency_days", nullable = false)
    private int maxFrequencyDays;

    @Column(name = "max_qty_per_delivery", nullable = false)
    private int maxQtyPerDelivery;

    @Override
    public String toString() {
        return id != null ? id.toString() : "";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Double getSubscriptionDiscount180Days() {
        return subscriptionDiscount180Days;
    }

    public void setSubscriptionDiscount180Days(Double subscriptionDiscount180Days) {
        this.subscriptionDiscount180Days = subscriptionDiscount180Days;
    }

    public Double getSubscriptionDiscount360Days() {
        return subscriptionDiscount360Days;
    }

    public void setSubscriptionDiscount360Days(Double subscriptionDiscount360Days) {
        this.subscriptionDiscount360Days = subscriptionDiscount360Days;
    }

    public int getMinFrequencyDays() {
        return minFrequencyDays;
    }

    public void setMinFrequencyDays(int minFrequencyDays) {
        this.minFrequencyDays = minFrequencyDays;
    }

    public int getMaxFrequencyDays() {
        return maxFrequencyDays;
    }

    public void setMaxFrequencyDays(int maxFrequencyDays) {
        this.maxFrequencyDays = maxFrequencyDays;
    }

    public int getMaxQtyPerDelivery() {
        return maxQtyPerDelivery;
    }

    public void setMaxQtyPerDelivery(int maxQtyPerDelivery) {
        this.maxQtyPerDelivery = maxQtyPerDelivery;
    }
}