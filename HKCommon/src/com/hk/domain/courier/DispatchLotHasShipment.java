package com.hk.domain.courier;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 12/3/12
 * Time: 5:19 PM
 * To change this template use File | Settings | File Templates.
 */

@SuppressWarnings ("serial")
@Entity
@Table(name = "dispatch_lot_has_shipment", uniqueConstraints = @UniqueConstraint(columnNames = { "dispatch_lot_id", "shipment_id" }))
public class DispatchLotHasShipment implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dispatch_lot_id", nullable = false)
	private DispatchLot dispatchLot;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "shipment_id", nullable = false)
	private Shipment shipment;

	@Column(name="shipment_status", nullable = false)
	private String shipmentStatus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DispatchLot getDispatchLot() {
		return dispatchLot;
	}

	public void setDispatchLot(DispatchLot dispatchLot) {
		this.dispatchLot = dispatchLot;
	}

	public Shipment getShipment() {
		return shipment;
	}

	public void setShipment(Shipment shipment) {
		this.shipment = shipment;
	}

	public String getShipmentStatus() {
		return shipmentStatus;
	}

	public void setShipmentStatus(String shipmentStatus) {
		this.shipmentStatus = shipmentStatus;
	}
}
