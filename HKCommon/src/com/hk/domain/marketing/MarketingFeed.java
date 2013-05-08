package com.hk.domain.marketing;

import com.hk.domain.catalog.product.Product;
import com.hk.domain.inventory.rtv.ExtraInventoryLineItem;

import javax.persistence.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: marut
 * Date: 19/04/13
 * Time: 12:01 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "marketing_feed")
public class MarketingFeed {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(name = "feed_name", nullable = false)
    String feedName;

    /**
     * Not creating any foreign key constraint
     */
    @Column(name = "product_id", nullable = false)
    String productId;

    public String getFeedName() {
        return feedName;
    }

    public void setFeedName(String feedName) {
        this.feedName = feedName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
