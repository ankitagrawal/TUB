package com.hk.domain.order.analytics;

/**
 * Created with IntelliJ IDEA.
 * User: Pratham
 * Date: 7/13/12
 * Time: 12:08 AM
 * To change this template use File | Settings | File Templates.
 */

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.warehouse.Warehouse;

import javax.persistence.*;
import java.util.Date;

/**
 * DemandHistory generated by hbm2java
 */
@Entity
@Table (name = "demand_history")
public class DemandHistory implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variant_id", nullable = false)
    private ProductVariant productVariant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @Column (name = "demand")
    private Long demand;

    @Temporal (TemporalType.DATE)
    @Column (name = "demand_date", length = 10)
    private Date demandDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductVariant getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(ProductVariant productVariant) {
        this.productVariant = productVariant;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public Long getDemand() {
        return demand;
    }

    public void setDemand(Long demand) {
        this.demand = demand;
    }

    public Date getDemandDate() {
        return demandDate;
    }

    public void setDemandDate(Date demandDate) {
        this.demandDate = demandDate;
    }

}


