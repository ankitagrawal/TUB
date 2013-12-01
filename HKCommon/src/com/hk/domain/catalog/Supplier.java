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

@SuppressWarnings("serial")
@Entity
@Table (name = "supplier")
public class Supplier implements java.io.Serializable {

	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	@Column (name = "id", unique = true, nullable = false)
	private Long id;

	@Column (name = "name", nullable = false, length = 100)
	private String name;

	@Column (name = "line1")
	private String line1;

	@Column (name = "line2")
	private String line2;

	@Column (name = "city")
	private String city;

	@Column (name = "state")
	private String state;

	@Column (name = "pincode")
	private String pincode;

	@Column (name = "tin_number", unique = true)
	private String tinNumber;

	@Column (name = "contact_person")
	private String contactPerson;

	@Column (name = "contact_number")
	private String contactNumber;

	@Temporal (TemporalType.TIMESTAMP)
	@Column (name = "create_date", length = 19)
	private Date createDate;

	@Temporal (TemporalType.TIMESTAMP)
	@Column (name = "update_date", length = 19)
	private Date updateDate;

	@Column (name = "brands")
	private String brandName;

	@Column (name = "dam_exp_cond")
	private String damageConditions;

	@Column (name = "email_id")
	private String email_id;

	@Column (name = "margins")
	private String margins;

	@Column (name = "terms_of_trade")
	private String tot;

	@Column (name = "credit_days", nullable = false)
	private Integer creditDays;

	@Column (name = "target_credit_days", nullable = false)
	private Integer targetCreditDays;

	@Column (name = "lead_time", nullable = false)
	private Integer leadTime;

	@Column (name = "active", nullable = false)
	private Boolean active;

	@Column (name = "comments")
	private String comments;
	
	@Column (name = "contact_person_2")
	private String contactPerson2;

	@Column (name = "contact_number_2")
	private String contactNumber2;
	
	@Column (name = "email_id_2")
	private String email_id2;
	
	@Column (name = "contact_person_3")
	private String contactPerson3;

	@Column (name = "contact_number_3")
	private String contactNumber3;
	
	@Column (name = "email_id_3")
	private String email_id3;

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
		return tot;
	}

	public void setTOT(String TOT) {
		this.tot = TOT;
	}

	public void setName(String name) {
		this.name = name;

	}

	public String getMargins() {
		return margins;
	}

	public void setMargins(String margins) {
		this.margins = margins;
	}

	public String getEmail_id() {
		return email_id;
	}

	public void setEmail_id(String email_id) {
		this.email_id = email_id;
	}

	public String getDamageConditions() {
		return damageConditions;
	}

	public void setDamageConditions(String damageConditions) {
		this.damageConditions = damageConditions;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
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

	public String getTot() {
		return tot;
	}

	public void setTot(String tot) {
		this.tot = tot;
	}

	public Integer getCreditDays() {
		return creditDays;
	}

	public void setCreditDays(Integer creditDays) {
		this.creditDays = creditDays;
	}

	public Integer getTargetCreditDays() {
		return targetCreditDays;
	}

	public void setTargetCreditDays(Integer targetCreditDays) {
		this.targetCreditDays = targetCreditDays;
	}

	public Integer getLeadTime() {
		return leadTime;
	}

	public void setLeadTime(Integer leadTime) {
		this.leadTime = leadTime;
	}

	public Boolean isActive() {
		return active;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	
	public String getContactPerson2() {
		return contactPerson2;
	}

	public void setContactPerson2(String contactPerson2) {
		this.contactPerson2 = contactPerson2;
	}

	public String getContactNumber2() {
		return contactNumber2;
	}

	public void setContactNumber2(String contactNumber2) {
		this.contactNumber2 = contactNumber2;
	}

	public String getEmail_id2() {
		return email_id2;
	}

	public void setEmail_id2(String email_id2) {
		this.email_id2 = email_id2;
	}

	public String getContactPerson3() {
		return contactPerson3;
	}

	public void setContactPerson3(String contactPerson3) {
		this.contactPerson3 = contactPerson3;
	}

	public String getContactNumber3() {
		return contactNumber3;
	}

	public void setContactNumber3(String contactNumber3) {
		this.contactNumber3 = contactNumber3;
	}

	public String getEmail_id3() {
		return email_id3;
	}

	public void setEmail_id3(String email_id3) {
		this.email_id3 = email_id3;
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