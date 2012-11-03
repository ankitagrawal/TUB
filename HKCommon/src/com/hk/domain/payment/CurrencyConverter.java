package com.hk.domain.payment;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Ankit
 * Date: Nov 2, 2012
 * Time: 5:32:59 PM
 * To change this template use File | Settings | File Templates.
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "currency_converter")

public class CurrencyConverter implements java.io.Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @Column(name = "base_currency_code", nullable = false, length = 45)
  private String baseCurrencyCode;

  @Column(name = "foreign_currency_code", length = 45)
  private String foreignCurrencyCode;

  @Column(name = "conversion_rate")
  private Double conversionRate;

  @Temporal(TemporalType.TIMESTAMP)
	@Column (name = "updated_date", length = 19)
	private Date updateDate;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Date getUpdateDate() {
        return updateDate;
    }

    public String getBaseCurrencyCode() {
        return baseCurrencyCode;
    }

    public void setBaseCurrencyCode(String baseCurrencyCode) {
        this.baseCurrencyCode = baseCurrencyCode;
    }

    public String getForeignCurrencyCode() {
        return foreignCurrencyCode;
    }

    public void setForeignCurrencyCode(String foreignCurrencyCode) {
        this.foreignCurrencyCode = foreignCurrencyCode;
    }

    public Double getConversionRate() {
        return conversionRate;
    }

    public void setConversionRate(Double conversionRate) {
        this.conversionRate = conversionRate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    
}
