package com.hk.domain.sku;

import com.akube.framework.gson.JsonSkip;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;

import javax.persistence.*;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Nihal
 * Date: 8/14/13
 * Time: 4:35 PM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(name = "foreign_si_cli")
public class ForeignSkuItemCLI {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @Column(name="foreign_sku_item_id")
  private Long skuItemId;
  
  @JsonSkip
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="cart_line_item_id")
  private CartLineItem cartLineItem;

  @Column(name="foreign_shipping_order_id")
  private Long foreignShippingOrderId;

    public String getForeignShippingOrderGatewayId() {
        return foreignShippingOrderGatewayId;
    }

    public void setForeignShippingOrderGatewayId(String foreignShippingOrderGatewayId) {
        this.foreignShippingOrderGatewayId = foreignShippingOrderGatewayId;
    }

    @Column(name="foreign_shipping_order_gateway_id")
  private  String foreignShippingOrderGatewayId;

  @Column(name="foreign_base_order_id")
  private Long foreignOrderId;

  @Column(name = "unit_num", nullable = false)
  private Long unitNum;
  
  @Column(name="counter")
  private Long counter;

  @JsonSkip
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "create_dt", nullable = false, length = 19)
  private Date createDate = new Date();

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "update_dt", length = 19)
  private Date updateDate;

  @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_variant_id", nullable = false)
  private ProductVariant productVariant;
  
  @Column(name = "foreign_sku_group_id")
  private Long foreignSkuGroupId;
  
  @Column(name="foreign_barcode")
  private String foreignBarcode;
  
  @Column(name="fsg_batch_number")
  private String fsgBatchNumber;
  
  @Column(name="fsg_mrp")
  private Double fsgMrp;
  
  @Column(name="aqua_mrp")
  private Double aquaMrp;
  
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name="fsg_mfg_dt")
  private Date fsgMfgDate;
  
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name= "fsg_expiry_dt")
  private Date fsgExpiryDate;
  
  @Column(name="fsg_cost_price")
  private Double fsgCostPrice;
  
  @Column(name="processed_status")
  private String processedStatus;

  @Column(name="shipping_order_booking_type")
  private Long shippingOrderBookingTypeId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getUnitNum() {
    return unitNum;
  }

  public void setUnitNum(Long unitNum) {
    this.unitNum = unitNum;
  }

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

  public Long getForeignShippingOrderId() {
    return foreignShippingOrderId;
  }

  public void setForeignShippingOrderId(Long foreignShippingOrderId) {
    this.foreignShippingOrderId = foreignShippingOrderId;
  }

  public Long getForeignOrderId() {
    return foreignOrderId;
  }

  public void setForeignOrderId(Long foreignOrderId) {
    this.foreignOrderId = foreignOrderId;
  }

	public Long getSkuItemId() {
		return skuItemId;
	}

	public void setSkuItemId(Long skuItemId) {
		this.skuItemId = skuItemId;
	}

	public Long getCounter() {
		return counter;
	}

	public void setCounter(Long counter) {
		this.counter = counter;
	}

	public ProductVariant getProductVariant() {
		return productVariant;
	}

	public void setProductVariant(ProductVariant productVariant) {
		this.productVariant = productVariant;
	}

	public CartLineItem getCartLineItem() {
		return cartLineItem;
	}

	public void setCartLineItem(CartLineItem cartLineItem) {
		this.cartLineItem = cartLineItem;
	}

	public Long getForeignSkuGroupId() {
		return foreignSkuGroupId;
	}

	public void setForeignSkuGroupId(Long foreignSkuGroupId) {
		this.foreignSkuGroupId = foreignSkuGroupId;
	}

	public String getForeignBarcode() {
		return foreignBarcode;
	}

	public void setForeignBarcode(String foreignBarcode) {
		this.foreignBarcode = foreignBarcode;
	}

	public String getFsgBatchNumber() {
		return fsgBatchNumber;
	}

	public void setFsgBatchNumber(String fsgBatchNumber) {
		this.fsgBatchNumber = fsgBatchNumber;
	}

	public Double getFsgMrp() {
		return fsgMrp;
	}

	public void setFsgMrp(Double fsgMrp) {
		this.fsgMrp = fsgMrp;
	}

	public Double getAquaMrp() {
		return aquaMrp;
	}

	public void setAquaMrp(Double aquaMrp) {
		this.aquaMrp = aquaMrp;
	}

	public Date getFsgMfgDate() {
		return fsgMfgDate;
	}

	public void setFsgMfgDate(Date fsgMfgDate) {
		this.fsgMfgDate = fsgMfgDate;
	}

	public Date getFsgExpiryDate() {
		return fsgExpiryDate;
	}

	public void setFsgExpiryDate(Date fsgExpiryDate) {
		this.fsgExpiryDate = fsgExpiryDate;
	}

	public Double getFsgCostPrice() {
		return fsgCostPrice;
	}

	public void setFsgCostPrice(Double fsgCostPrice) {
		this.fsgCostPrice = fsgCostPrice;
	}

	public String getProcessedStatus() {
		return processedStatus;
	}

	public void setProcessedStatus(String processedStatus) {
		this.processedStatus = processedStatus;
	}

  public Long getShippingOrderBookingTypeId() {
    return shippingOrderBookingTypeId;
  }

  public void setShippingOrderBookingTypeId(Long shippingOrderBookingTypeId) {
    this.shippingOrderBookingTypeId = shippingOrderBookingTypeId;
  }
}
