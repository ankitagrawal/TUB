package com.hk.report.dto.accounting;

import java.util.HashSet;
import java.util.Set;

import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.filter.LineItemFilter;

/**
 * Created by IntelliJ IDEA.
 * User: Rahul
 * Date: Mar 14, 2012
 * Time: 10:21:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class InvoiceDto {

	private double itemsTotal;
	private double shipping;
	private double totalItemLevelDiscount;
	private double totalOrderLevelDiscount;
	private double totalDiscount;
	private double rewardPoints;
	private double cod;
	private double grandTotal;
	private String warehouseName;
	private String warehouseAddressLine1;
	private String warehouseAddressLine2;
	private String warehouseCity;
	private String warehouseState;
	private String warehouseTin;
	private String warehousePh;
	private String warehousePincode;


	private Set<InvoiceLineItemDto> invoiceLineItemDtos = new HashSet<InvoiceLineItemDto>(0);


	

	public InvoiceDto(ShippingOrder shippingOrder) {

		LineItemFilter productLineItemFilter = new LineItemFilter(shippingOrder.getLineItems());
		Set<LineItem> shippingOrderLineItem = productLineItemFilter.filter();

		//invoiceLineItemDtos = InvoiceLineItemDto.generateInvoiceLineItemDto(shippingOrderLineItem);

		for (LineItem productLineItem : shippingOrderLineItem) {

			if (productLineItem.getHkPrice() != null && productLineItem.getQty() != null) {
				itemsTotal += (productLineItem.getHkPrice() * productLineItem.getQty());
			}

			if (productLineItem.getDiscountOnHkPrice() != null) {
				totalItemLevelDiscount += productLineItem.getDiscountOnHkPrice();
			}

			if (productLineItem.getOrderLevelDiscount() != null) {
				totalOrderLevelDiscount += productLineItem.getOrderLevelDiscount();
			}

			if (productLineItem.getRewardPoints() != null) {
				rewardPoints += productLineItem.getRewardPoints();
			}

			totalDiscount = totalItemLevelDiscount + totalOrderLevelDiscount;

			if (productLineItem.getHkPrice() != null) {
				shipping += productLineItem.getShippingCharges();
			}

			if (productLineItem.getHkPrice() != null) {
				cod += productLineItem.getCodCharges();
			}
			
			invoiceLineItemDtos.add(new InvoiceLineItemDto(productLineItem));
		}

		grandTotal = itemsTotal - totalDiscount - rewardPoints + shipping + cod;

		warehouseName = shippingOrder.getWarehouse().getName();
		warehouseCity = shippingOrder.getWarehouse().getCity();
		if(shippingOrder.getWarehouse().getLine1()==null){
		warehouseAddressLine1 = shippingOrder.getWarehouse().getLine1();
		}
		else{
			warehouseAddressLine1 = "";
		}
		if(shippingOrder.getWarehouse().getLine2()==null){
		warehouseAddressLine2 = shippingOrder.getWarehouse().getLine2();
		}
		else{
			warehouseAddressLine2 = "";
		}
		warehousePh = shippingOrder.getWarehouse().getWhPhone();
		warehousePincode = shippingOrder.getWarehouse().getPincode();
		warehouseTin = shippingOrder.getWarehouse().getTin();
		warehouseState = shippingOrder.getWarehouse().getState();

	}

	public double getItemsTotal() {
		return itemsTotal;
	}

	public double getShipping() {
		return shipping;
	}

	public double getTotalItemLevelDiscount() {
		return totalItemLevelDiscount;
	}

	public double getTotalOrderLevelDiscount() {
		return totalOrderLevelDiscount;
	}

	public double getTotalDiscount() {
		return totalDiscount;
	}

	public double getRewardPoints() {
		return rewardPoints;
	}

	public double getCod() {
		return cod;
	}

	public double getGrandTotal() {
		return grandTotal;
	}

	public Set<InvoiceLineItemDto> getInvoiceLineItemDtos() {
		return invoiceLineItemDtos;
	}


	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public String getWarehouseAddressLine1() {
		return warehouseAddressLine1;
	}

	public void setWarehouseAddressLine1(String warehouseAddressLine1) {
		this.warehouseAddressLine1 = warehouseAddressLine1;
	}

	public String getWarehouseAddressLine2() {
		return warehouseAddressLine2;
	}

	public void setWarehouseAddressLine2(String warehouseAddressLine2) {
		this.warehouseAddressLine2 = warehouseAddressLine2;
	}

	public String getWarehouseCity() {
		return warehouseCity;
	}

	public void setWarehouseCity(String warehouseCity) {
		this.warehouseCity = warehouseCity;
	}

	public String getWarehouseState() {
		return warehouseState;
	}

	public void setWarehouseState(String warehouseState) {
		this.warehouseState = warehouseState;
	}

	public String getWarehouseTin() {
		return warehouseTin;
	}

	public void setWarehouseTin(String warehouseTin) {
		this.warehouseTin = warehouseTin;
	}

	public String getWarehousePh() {
		return warehousePh;
	}

	public void setWarehousePh(String warehousePh) {
		this.warehousePh = warehousePh;
	}

	public String getWarehousePincode() {
		return warehousePincode;
	}

	public void setWarehousePincode(String warehousePincode) {
		this.warehousePincode = warehousePincode;
	}
}
