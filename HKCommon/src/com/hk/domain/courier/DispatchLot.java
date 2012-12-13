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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "zone_id", nullable = false)
	private Zone zone;

	@Column(name = "source", nullable = false)
	private String source;

	@Column(name = "destination", nullable = false)
	private String destination;

	@Column(name = "no_of_shipments_sent")
	private Long noOfShipmentsSent;

	@Column(name = "no_of_shipments_received")
	private Long noOfShipmentsReceived;

	@Column(name = "no_of_mother_bags")
	private Long noOfMotherBags;

	@Column(name = "total_weight")
	private Double totalWeight;

	@Column(name = "remarks")
	private String remarks;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dispatch_lot_status_id", nullable = false)
	private DispatchLotStatus dispatchLotStatus;

	@Column(name = "dispatch_date")
	private Date dispatchDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_dt", nullable = false)
	private Date createDate = new Date();

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "delivery_dt")
	private Date deliveryDate;

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

	public Zone getZone() {
		return zone;
	}

	public void setZone(Zone zone) {
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

	public Date getDispatchDate() {
		return dispatchDate;
	}

	public void setDispatchDate(Date dispatchDate) {
		this.dispatchDate = dispatchDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public DispatchLotStatus getDispatchLotStatus() {
		return dispatchLotStatus;
	}

	public void setDispatchLotStatus(DispatchLotStatus dispatchLotStatus) {
		this.dispatchLotStatus = dispatchLotStatus;
	}
}
