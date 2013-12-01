package com.hk.domain.warehouse;

/**
 * Created by IntelliJ IDEA. User: Neha Date: Jul 10, 2012 Time: 3:45:26 PM To change this template use File | Settings |
 * File Templates.
 */
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.hk.domain.catalog.product.ProductVariant;

@SuppressWarnings("serial")
@Entity
@Table(name = "demand_forecast")
public class DemandForecast implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long           id;

    @Temporal(TemporalType.DATE)
    @Column(name = "forecast_date", nullable = false, length = 19)
    private Date           forecastDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variant_id", unique = true, nullable = false)
    private ProductVariant productVariant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse      warehouse;

    @Column(name = "forecast_value", precision = 11, nullable = false)
    private Double         forecastValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {

        this.id = id;
    }

    public Date getforecastDate() {
        return forecastDate;
    }

    public void setforecastDate(Date forecastDate) {
        this.forecastDate = forecastDate;
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

    public Double getforecastValue() {
        return forecastValue;
    }

    public void setforecastValue(Double forecastValue) {
        this.forecastValue = forecastValue;
    }
}
