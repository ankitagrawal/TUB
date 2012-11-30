package com.hk.report.dto.analytics;

/**
 * Created by IntelliJ IDEA.
 * User: Ajeet
 * Date: 25 Nov, 2012
 * Time: 7:07:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class TrafficSrcPerformanceDto {
	
	private String trafficSrc;
	private Boolean trafficSrcPaid;
	private Long trafficCount;
	private Long orderCount;
	private Long firstOrderCount;

	public String getTrafficSrc() {
		return trafficSrc;
	}

	public void setTrafficSrc(String trafficSrc) {
		this.trafficSrc = trafficSrc;
	}

	public Boolean getTrafficSrcPaid() {
		return trafficSrcPaid;
	}

	public void setTrafficSrcPaid(Boolean trafficSrcPaid) {
		this.trafficSrcPaid = trafficSrcPaid;
	}

	public Long getTrafficCount() {
		return trafficCount;
	}

	public void setTrafficCount(Long trafficCount) {
		this.trafficCount = trafficCount;
	}

	public Long getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(Long orderCount) {
		this.orderCount = orderCount;
	}

	public Long getFirstOrderCount() {
		return firstOrderCount;
	}

	public void setFirstOrderCount(Long firstOrderCount) {
		this.firstOrderCount = firstOrderCount;
	}
}
