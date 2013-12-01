package com.hk.domain.courier;

import com.akube.framework.gson.JsonSkip;
import com.hk.domain.order.ShippingOrder;

import javax.persistence.*;
import java.util.Date;

@SuppressWarnings ("serial")
@Entity
@Table (name = "shipment")
public class Shipment implements java.io.Serializable, Comparable<Shipment> {

	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	@Column (name = "id", unique = true, nullable = false)
	private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "awb_id")
    private Awb awb;

    @JsonSkip
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_order_id")
    private ShippingOrder shippingOrder;

    @JsonSkip
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipment_service_type_id")
    private ShipmentServiceType shipmentServiceType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "box_size_id")
    private BoxSize boxSize;

    @Column (name = "box_weight")
	private Double boxWeight;

	@Temporal (TemporalType.TIMESTAMP)
	@Column (name = "ship_date", length = 19)
	private Date shipDate;

	@Temporal (TemporalType.TIMESTAMP)
	@Column (name = "delivery_date", length = 19)
	private Date deliveryDate;

	@Temporal (TemporalType.TIMESTAMP)
	@Column (name = "return_date", length = 19)
	private Date returnDate;

	@Column (name = "email_sent")
	private Boolean isEmailSent;

	@Column (name = "shipment_charge")
	private Double shipmentCharge;

	@Column (name = "collection_charge")
	private Double collectionCharge;

    @Column (name = "order_placed_shipment_charge")
    private Double orderPlacedShipmentCharge;

	@Column (name = "estm_shipment_charge")
	private Double estmShipmentCharge;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "shipment_cost_calculate_date")
    private Date shipmentCostCalculateDate;

	@Column (name = "estm_collection_charge")
	private Double estmCollectionCharge;

	@Column (name = "extra_charge")
	private Double extraCharge;

	@Column (name = "picker")
	private String picker;

	@Column (name = "packer")
	private String packer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "zone_id", nullable = false)
	private Zone zone;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_dt", nullable = false)
	private Date createDate = new Date();

	@Transient
	private String trackLink;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "rto_initiated_date")
  private Date rtoInitiatedDate;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "rto_date")
  private Date rtoDate;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "lost_date")
  private Date lostDate;

	public int compareTo(Shipment shipment) {
		if (this.getId() < shipment.getId()) return -1;
		if (this.getId() > shipment.getId()) return 1;
		return 0;
	}

    public Double getOrderPlacedShipmentCharge() {
        return orderPlacedShipmentCharge;
    }

    public void setOrderPlacedShipmentCharge(Double orderPlacedShipmentCharge) {
        this.orderPlacedShipmentCharge = orderPlacedShipmentCharge;
    }

    public Date getShipmentCostCalculateDate() {
        return shipmentCostCalculateDate;
    }

    public void setShipmentCostCalculateDate(Date shipmentCostCalculateDate) {
        this.shipmentCostCalculateDate = shipmentCostCalculateDate;
    }

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public BoxSize getBoxSize() {
		return boxSize;
	}

	public void setBoxSize(BoxSize boxSize) {
		this.boxSize = boxSize;
	}

	public Double getBoxWeight() {
		return boxWeight;
	}

	public void setBoxWeight(Double boxWeight) {
		this.boxWeight = boxWeight;
	}

	public Date getShipDate() {
		return shipDate;
	}

	public void setShipDate(Date shipDate) {
		this.shipDate = shipDate;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getTrackLink() {
		return trackLink;
	}

	public void setTrackLink(String trackLink) {
		this.trackLink = trackLink;
	}

	public Boolean isEmailSent() {
		return isEmailSent;
	}

  public Boolean getEmailSent(){
    return isEmailSent;
  }
	public void setEmailSent(Boolean emailSent) {
		isEmailSent = emailSent;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public Double getEstmShipmentCharge() {
		return estmShipmentCharge;
	}

	public void setEstmShipmentCharge(Double estmShipmentCharge) {
		this.estmShipmentCharge = estmShipmentCharge;
	}

	public Double getEstmCollectionCharge() {
		return estmCollectionCharge;
	}

	public void setEstmCollectionCharge(Double estmCollectionCharge) {
		this.estmCollectionCharge = estmCollectionCharge;
	}

	public Double getExtraCharge() {
		return extraCharge;
	}

	public void setExtraCharge(Double extraCharge) {
		this.extraCharge = extraCharge;
	}

	public Double getShipmentCharge() {
		return shipmentCharge;
	}

	public void setShipmentCharge(Double shipmentCharge) {
		this.shipmentCharge = shipmentCharge;
	}

	public Double getCollectionCharge() {
		return collectionCharge;
	}

	public void setCollectionCharge(Double collectionCharge) {
		this.collectionCharge = collectionCharge;
	}

	public String getPicker() {
		return picker;
	}

	public void setPicker(String picker) {
		this.picker = picker;
	}

	public String getPacker() {
		return packer;
	}

	public void setPacker(String packer) {
		this.packer = packer;
	}

    public Awb getAwb() {
        return awb;
    }

    public void setAwb(Awb awb) {
        this.awb = awb;
    }

    public ShippingOrder getShippingOrder() {
        return shippingOrder;
    }

    public void setShippingOrder(ShippingOrder shippingOrder) {
        this.shippingOrder = shippingOrder;
    }

    @Override
	public String toString() {
		return id != null ? id.toString() : "";
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Zone getZone() {
		return zone;
	}

	public void setZone(Zone zone) {
		this.zone = zone;
	}

    public ShipmentServiceType getShipmentServiceType() {
        return shipmentServiceType;
    }

    public void setShipmentServiceType(ShipmentServiceType shipmentServiceType) {
        this.shipmentServiceType = shipmentServiceType;
    }

  public Date getRtoInitiatedDate() {
    return rtoInitiatedDate;
  }

  public void setRtoInitiatedDate(Date rtoInitiatedDate) {
    this.rtoInitiatedDate = rtoInitiatedDate;
  }

  public Date getRtoDate() {
    return rtoDate;
  }

  public void setRtoDate(Date rtoDate) {
    this.rtoDate = rtoDate;
  }

  public Date getLostDate() {
    return lostDate;
  }

  public void setLostDate(Date lostDate) {
    this.lostDate = lostDate;
  }
}
