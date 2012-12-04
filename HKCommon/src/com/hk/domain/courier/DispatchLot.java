package com.hk.domain.courier;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 12/3/12
 * Time: 4:49 PM
 * To change this template use File | Settings | File Templates.
 */

@SuppressWarnings ("serial")
@Entity
@Table(name = "dispatch_lot")
public class DispatchLot implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "docket_number", nullable = false)
	private String docketNumber;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "courier_id", nullable = false)
	private Courier courier;

	@Column(name = "zone", nullable = false)
	private String zone;

	@Column(name = "source", nullable = false)
	private String source;

	@Column(name = "destination", nullable = false)
	private String destination;

	@Column(name = "no_of_shipments_sent", nullable = false)
	private Long noOfShipmentsSent = 0L;

	@Column(name = "no_of_shipments_received")
	private Long noOfShipmentsReceived;

	@Column(name = "no_of_mother_bags")
	private Long noOfMotherBags;

	@Column(name = "total_weight", nullable = false)
	private Double totalWeight = 0D;

/*	@Column(name = "delivery_date")
	private Date deliveryDate;*/

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_dt")
	private Date createDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_dt")
	private Date updateDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDocketNumber() {
		return docketNumber;
	}

	public void setDocketNumber(String docketNumber) {
		this.docketNumber = docketNumber;
	}

	public Courier getCourier() {
		return courier;
	}

	public void setCourier(Courier courier) {
		this.courier = courier;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public Long getNoOfShipmentsSent() {
		return noOfShipmentsSent;
	}

	public void setNoOfShipmentsSent(Long noOfShipmentsSent) {
		this.noOfShipmentsSent = noOfShipmentsSent;
	}

	public Long getNoOfShipmentsReceived() {
		return noOfShipmentsReceived;
	}

	public void setNoOfShipmentsReceived(Long noOfShipmentsReceived) {
		this.noOfShipmentsReceived = noOfShipmentsReceived;
	}

	public Long getNoOfMotherBags() {
		return noOfMotherBags;
	}

	public void setNoOfMotherBags(Long noOfMotherBags) {
		this.noOfMotherBags = noOfMotherBags;
	}

	public Double getTotalWeight() {
		return totalWeight;
	}

	public void setTotalWeight(Double totalWeight) {
		this.totalWeight = totalWeight;
	}

/*
	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
*/

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
}
