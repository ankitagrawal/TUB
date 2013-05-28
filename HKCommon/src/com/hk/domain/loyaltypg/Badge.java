package com.hk.domain.loyaltypg;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "badge")
public class Badge implements Serializable, Comparable<Badge>{
	
	private static final long serialVersionUID = 1L;

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
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getMinScore() {
		return this.minScore;
	}

	public void setMinScore(double minScore) {
		this.minScore = minScore;
	}

	public double getMaxScore() {
		return (long)this.maxScore;
	}

	public void setMaxScore(double maxScore) {
		this.maxScore = maxScore;
	}

	public String getBadgeName() {
		return this.badgeName;
	}

	public void setBadgeName(String badgeName) {
		this.badgeName = badgeName;
	}

	/**
	 * @return the loyaltyPercentage
	 */
	public double getLoyaltyPercentage() {
		return this.loyaltyPercentage;
	}

	/**
	 * @param loyaltyPercentage the loyaltyPercentage to set
	 */
	public void setLoyaltyPercentage(double loyaltyPercentage) {
		this.loyaltyPercentage = loyaltyPercentage;
	}

	public String getIconRelUrl() {
		return this.iconRelUrl;
	}

	public void setIconRelUrl(String iconRelUrl) {
		this.iconRelUrl = iconRelUrl;
	}

	@Override
	public int compareTo(Badge o) {
		return Double.valueOf(this.minScore).compareTo(Double.valueOf(o.minScore));
		/*if(o.maxScore == -1) return -1;
		if(this.maxScore == -1) return +1;
		return Double.valueOf(this.maxScore).compareTo(Double.valueOf(o.maxScore));*/
	}
}
