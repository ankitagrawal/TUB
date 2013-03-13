package com.hk.domain.loyaltypg;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "badge")
public class Badge {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "min_score")
	private double minScore;
	
	@Column(name = "max_score")
	private double maxScore;

	@Column(name = "badge_name")
	private String badgeName;
	
	@Column(name = "loyalty_percentage")
	private double loyaltyPercentage;
	
	@Column(name = "icon_rel_url")
	private String iconRelUrl;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getMinScore() {
		return minScore;
	}

	public void setMinScore(double minScore) {
		this.minScore = minScore;
	}

	public double getMaxScore() {
		return maxScore;
	}

	public void setMaxScore(double maxScore) {
		this.maxScore = maxScore;
	}

	public String getBadgeName() {
		return badgeName;
	}

	public void setBadgeName(String badgeName) {
		this.badgeName = badgeName;
	}

	public double getLoyaltyPercentage() {
		return loyaltyPercentage;
	}

	public void setLoyaltyPercentage(double loyaltyPercentage) {
		this.loyaltyPercentage = loyaltyPercentage;
	}

	public String getIconRelUrl() {
		return iconRelUrl;
	}

	public void setIconRelUrl(String iconRelUrl) {
		this.iconRelUrl = iconRelUrl;
	}
}
