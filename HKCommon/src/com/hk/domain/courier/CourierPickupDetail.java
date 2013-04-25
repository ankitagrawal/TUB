package com.hk.domain.courier;

import com.hk.domain.order.ShippingOrder;
import com.hk.domain.inventory.rv.ReconciliationStatus;
import com.hk.domain.user.User;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA. * User: Neha * Date: Dec 4, 2012 * Time: 2:17:08 PM
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings ("serial")
@Entity
@Table(name = "courier_pickup_detail")
public class CourierPickupDetail implements java.io.Serializable{

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long                        id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courier_id", nullable = false)
    private Courier courier;

	  @Column(name = "pickup_confirmation_no", length = 70)
	  private String pickupConfirmationNo;

	  @Column(name = "tracking_no", length = 70)
	  private String trackingNo;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "pickup_date", nullable = false, length = 19)
    private Date pickupDate;

	  @ManyToOne
    @JoinColumn(name = "pickup_status_id", nullable = false)
	  private PickupStatus pickupStatus;

	  public Long getId() {
		return id;
	   }

	  public void setId(Long id) {
		this.id = id;
	   }

	  public Courier getCourier() {
		return courier;
	   }

	  public void setCourier(Courier courier) {
		this.courier = courier;
	   }

	  public String getPickupConfirmationNo() {
		return pickupConfirmationNo;
	   }

   	public void setPickupConfirmationNo(String pickupConfirmationNo) {
		this.pickupConfirmationNo = pickupConfirmationNo;
	   }

	  public Date getPickupDate() {
		return pickupDate;
	   }

	  public void setPickupDate(Date pickupDate) {
		this.pickupDate = pickupDate;
	   }

	  public PickupStatus getPickupStatus() {
		return pickupStatus;
	   }

	  public void setPickupStatus(PickupStatus pickupStatus) {
		this.pickupStatus = pickupStatus;
	    }

	  public String getTrackingNo() {
		return trackingNo;
	   }

	  public void setTrackingNo(String trackingNo) {
		this.trackingNo = trackingNo;
	   }
}
