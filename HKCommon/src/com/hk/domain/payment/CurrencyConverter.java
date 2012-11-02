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
  private String basecurrencycode;

  @Column(name = "foreign_currency_code", length = 45)
  private String foreigncurrencycode;

  @Column(name = "conversion_rate")
  private Double conversionrate;

  @Temporal(TemporalType.TIMESTAMP)
	@Column (name = "update_date", length = 19)
	private Date updateDate;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBasecurrencycode() {
        return basecurrencycode;
    }

    public void setBasecurrencycode(String basecurrencycode) {
        this.basecurrencycode = basecurrencycode;
    }

    public Double getConversionrate() {
        return conversionrate;
    }

    public void setConversionrate(Double conversionrate) {
        this.conversionrate = conversionrate;
    }

    public String getForeigncurrencycode() {
        return foreigncurrencycode;
    }

    public void setForeigncurrencycode(String foreigncurrencycode) {
        this.foreigncurrencycode = foreigncurrencycode;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    
}
