package com.hk.domain.analytics;
// Generated 22 Nov, 2012 2:21:02 PM by Hibernate Tools 3.2.4.CR1


import javax.persistence.*;
import java.util.Date;

/**
 * TrafficTracking generated by hbm2java
 */
@Entity
@Table (name = "traffic_tracking")
public class TrafficTracking implements java.io.Serializable {


	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	@Column (name = "id", unique = true, nullable = false)
	private Long id;

	@Column (name = "src_url", length = 500)
	private String srcUrl;

	@Column (name = "user_agent", length = 200)
	private String userAgent;


	@Column (name = "traffic_src", nullable = false, length = 45)
	private String trafficSrc;


	@Column (name = "traffic_src_details", length = 200)
	private String trafficSrcDetails;


	@Column (name = "traffic_src_paid", nullable = false)
	private Boolean trafficSrcPaid;


	@Column (name = "landing_url", nullable = false, length = 500)
	private String landingUrl;


	@Column (name = "primary_category", length = 80)
	private String primaryCategory;


	@Column (name = "product_id", length = 20)
	private String productId;


	@Column (name = "user_id")
	private Long userId;


	@Column (name = "ip", nullable = false, length = 45)
	private String ip;


	@Column (name = "session_id", nullable = false, length = 45)
	private String sessionId;


	@Column (name = "order_id")
	private Long orderId;

	@Temporal (TemporalType.TIMESTAMP)
	@Column (name = "create_dt", nullable = false, length = 19)
	private Date createDt;

	@Temporal (TemporalType.TIMESTAMP)
	@Column (name = "update_dt", nullable = false, length = 19)
	private Date updateDt;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSrcUrl() {
		return this.srcUrl;
	}

	public void setSrcUrl(String srcUrl) {
		this.srcUrl = srcUrl;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getTrafficSrc() {
		return this.trafficSrc;
	}

	public void setTrafficSrc(String trafficSrc) {
		this.trafficSrc = trafficSrc;
	}

	public String getTrafficSrcDetails() {
		return this.trafficSrcDetails;
	}

	public void setTrafficSrcDetails(String trafficSrcDetails) {
		this.trafficSrcDetails = trafficSrcDetails;
	}

	public Boolean isTrafficSrcPaid() {
		return trafficSrcPaid;
	}

	public void setTrafficSrcPaid(Boolean trafficSrcPaid) {
		this.trafficSrcPaid = trafficSrcPaid;
	}

	public String getLandingUrl() {
		return this.landingUrl;
	}

	public void setLandingUrl(String landingUrl) {
		this.landingUrl = landingUrl;
	}

	public String getPrimaryCategory() {
		return this.primaryCategory;
	}

	public void setPrimaryCategory(String primaryCategory) {
		this.primaryCategory = primaryCategory;
	}

	public String getProductId() {
		return this.productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getSessionId() {
		return this.sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Long getOrderId() {
		return this.orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Date getCreateDt() {
		return this.createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}

	public Date getUpdateDt() {
		return this.updateDt;
	}

	public void setUpdateDt(Date updateDt) {
		this.updateDt = updateDt;
	}
}


