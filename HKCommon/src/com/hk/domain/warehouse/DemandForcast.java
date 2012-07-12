package com.hk.domain.warehouse;

/**
 * Created by IntelliJ IDEA.
 * User: Neha
 * Date: Jul 10, 2012
 * Time: 3:45:26 PM
 * To change this template use File | Settings | File Templates.
 */
import com.hk.domain.catalog.product.ProductVariant;

import javax.persistence.*;
import java.util.Date;
import java.io.Serializable;

@Entity
@Table (name="demand_forcast")
public class DemandForcast {//implements Serializable{

     @Id
     @GeneratedValue(strategy = GenerationType.AUTO)
     @Column(name = "id", unique = true, nullable = false)
     private Long id;
    
     @Temporal(TemporalType.DATE)
     @Column(name = "forcast_date", nullable = false, length = 19)
     private Date forcastDate;

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "product_variant_id", unique = true, nullable = false)
     private ProductVariant productVariant;

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "warehouse_id", nullable = false)
     private Warehouse warehouse;

     @Column(name = "forcast_value", precision = 11, nullable=false)
     private Double forcastValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getForcastDate() {
        return forcastDate;
    }

    public void setForcastDate(Date ForcastDate) {
        this.forcastDate = ForcastDate;
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

    public Double getForcastValue() {
        return forcastValue;
    }

    public void setForcastValue(Double forcastValue) {
        this.forcastValue = forcastValue;
    }
}

