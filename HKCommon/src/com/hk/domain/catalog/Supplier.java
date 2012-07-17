package com.hk.domain.catalog;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "supplier")
/* @Cache(usage = CacheConcurrencyStrategy.READ_WRITE) */
public class Supplier implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long   id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "line1")
    private String line1;

    @Column(name = "line2")
    private String line2;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "pincode")
    private String pincode;

    @Column(name = "tin_number", unique = true)
    private String tinNumber;

    @Column(name = "contact_person")
    private String contactPerson;

    @Column(name = "contact_number")
    private String contactNumber;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", length = 19)
    private Date   createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_date", length = 19)
    private Date   updateDate;

    @Column(name = "credit_period")
    private  Integer CreditPeriod;

     @Column(name = "brands")
    private String BrandName;

     @Column(name = "dam_exp_cond")
    private Double DamageConditions;

     @Column(name = "email_id")
    private String email_id;

     @Column(name = "margins")
    private String Margins;

     @Column(name = "terms_of_trade")
    private String TOT;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }


    public String getTOT() {
        return TOT;
    }

    public void setTOT(String TOT) {
        this.TOT = TOT;
    }

    public void setName(String name) {
        this.name = name;

    }

    public String getMargins() {
        return Margins;
    }

    public void setMargins(String margins) {
        Margins = margins;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getDamageConditions() {
        return DamageConditions;
    }

    public void setDamageConditions(String damageConditions) {
        DamageConditions = damageConditions;
    }

    public String getBrandName() {
        return BrandName;
    }

    public void setBrandName(String brandName) {
        BrandName = brandName;
    }

    public String getCreditPeriod() {
        return CreditPeriod;
    }

    public void setCreditPeriod(String creditPeriod) {
        CreditPeriod = creditPeriod;
    }

    public String getLine1() {
        return line1;

    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getTinNumber() {
        return tinNumber;
    }

    public void setTinNumber(String tinNumber) {
        this.tinNumber = tinNumber;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        // if (o == null || getClass() != o.getClass()) return false;

        if (o instanceof Supplier) {
            Supplier that = (Supplier) o;

            if (getTinNumber() != null ? !getTinNumber().equals(that.getTinNumber()) : that.getTinNumber() != null)
                return false;

            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return getName() != null ? getName().hashCode() : 0;
    }

    @Override
    public String toString() {
        return this.id != null ? this.id.toString() : "";
    }
}