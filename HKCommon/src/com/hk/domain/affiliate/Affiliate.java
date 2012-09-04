package com.hk.domain.affiliate;

import com.hk.domain.catalog.category.Category;
import com.hk.domain.offer.Offer;
import com.hk.domain.user.User;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "affiliate")
public class Affiliate {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "code", nullable = false, length = 150)
	private String code;

	@Column(name = "check_in_favour_of", length = 150)
	private String checkInFavourOf;

	@Column(name = "affiliate_expiry_date")
	private Date expiryDate;

	@Column(name = "affiliate_pan_no")
	private String panNo;

	@Column(name = "ifsc_code")
	private String ifscCode;

	@Column(name = "bank_account_number")
	private String bankAccountNumber;

	@Column(name = "affiliate_website")
	private String websiteName;

	@Column(name = "api_key")
	private String apiKey;

	@Column(name = "affiliate_type")
	Long affiliateType;

	@Column(name = "affiliate_mode")
	Long affiliateMode;

	@Column(name = "main_address_id")
	Long mainAddressId;

	@Column(name = "valid_days")
	Long validDays;

	@Column(name = "weekly_coupon_limit")
	Long weeklyCouponLimit;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(
			name = "affiliate_has_category",
			joinColumns = {@JoinColumn(name = "affiliate_id", nullable = false, updatable = false)},
			inverseJoinColumns = {@JoinColumn(name = "category_name", nullable = false, updatable = false)}
	)
	private Set<Category> categories = new HashSet<Category>(0);

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "affiliate")
	private Set<AffiliateTxn> affiliateTxns = new HashSet<AffiliateTxn>(0);

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "offer_id")
	private Offer offer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "affiliate_status_id")
	private AffiliateStatus affiliateStatus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCheckInFavourOf() {
		return checkInFavourOf;
	}

	public void setCheckInFavourOf(String checkInFavourOf) {
		this.checkInFavourOf = checkInFavourOf;
	}

	public Long getMainAddressId() {
		return mainAddressId;
	}

	public void setMainAddressId(Long mainAddressId) {
		this.mainAddressId = mainAddressId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getPanNo() {
		return panNo;
	}

	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}

	public String getWebsiteName() {
		return websiteName;
	}

	public void setWebsiteName(String websiteName) {
		this.websiteName = websiteName;
	}

	public Long getAffiliateType() {
		return affiliateType;
	}

	public void setAffiliateType(Long affiliateType) {
		this.affiliateType = affiliateType;
	}

	public Long getAffiliateMode() {
		return affiliateMode;
	}

	public void setAffiliateMode(Long affiliateMode) {
		this.affiliateMode = affiliateMode;
	}

	public AffiliateStatus getAffiliateStatus() {
		return affiliateStatus;
	}

	public void setAffiliateStatus(AffiliateStatus affiliateStatus) {
		this.affiliateStatus = affiliateStatus;
	}

	public Offer getOffer() {
		return offer;
	}

	public void setOffer(Offer offer) {
		this.offer = offer;
	}

	public Long getValidDays() {
		return validDays;
	}

	public void setValidDays(Long validDays) {
		this.validDays = validDays;
	}

	public Long getWeeklyCouponLimit() {
		return weeklyCouponLimit;
	}

	public void setWeeklyCouponLimit(Long weeklyCouponLimit) {
		this.weeklyCouponLimit = weeklyCouponLimit;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	public String getBankAccountNumber() {
		return bankAccountNumber;
	}

	public void setBankAccountNumber(String bankAccountNumber) {
		this.bankAccountNumber = bankAccountNumber;
	}

	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}

	public Set<AffiliateTxn> getAffiliateTxns() {
		return affiliateTxns;
	}

	public void setAffiliateTxns(Set<AffiliateTxn> affiliateTxns) {
		this.affiliateTxns = affiliateTxns;
	}

	@Override
	public String toString() {
		return id == null ? "" : id.toString();
	}
}
