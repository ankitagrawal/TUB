package com.hk.domain.shippingOrder;

/*
 * User: Pratham
 * Date: 01/04/13  Time: 23:37
*/

import com.akube.framework.gson.JsonSkip;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.order.ShippingOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "shipping_order_category")
public class ShippingOrderCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_order_id", nullable = false)
    private ShippingOrder shippingOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_name", nullable = false)
    private Category category;

    @Column(name = "is_primary")
    private Boolean isPrimary = false;

    @JsonSkip
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_dt", nullable = false, length = 19)
    private Date createDate = new Date();

    public ShippingOrder getShippingOrder() {
        return shippingOrder;
    }

    public void setShippingOrder(ShippingOrder shippingOrder) {
        this.shippingOrder = shippingOrder;
    }

    public Category getCategory() {
        return category;
    }

    public Boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(Boolean primary) {
        isPrimary = primary;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o instanceof ShippingOrderCategory) {
            ShippingOrderCategory shippingOrderCategory = (ShippingOrderCategory) o;

            return new EqualsBuilder().append(this.shippingOrder.getId(), shippingOrderCategory.getShippingOrder().getId()).append(this.category.getName(), shippingOrderCategory.getCategory().getName()).isEquals();
        }

        return false;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.shippingOrder.getId()).append(this.category.getName()).toHashCode();
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

}
