package com.hk.domain.affiliate;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Pratham
 * Date: 8/22/12
 * Time: 1:44 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "affiliate_category_has_brand")
public class AffiliateCategoryHasBrand implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column (name = "id", unique = true, nullable = false)
	private Long id;

	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "affiliate_category_name_id", nullable = false)
	private AffiliateCategory affiliateCategory;

	@Column (name = "brand", nullable = false, length = 45)
	private String brand;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AffiliateCategory getAffiliateCategory() {
		return affiliateCategory;
	}

	public void setAffiliateCategory(AffiliateCategory affiliateCategory) {
		this.affiliateCategory = affiliateCategory;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}
}
