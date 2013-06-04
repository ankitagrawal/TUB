package com.hk.admin.dto.accounting;

import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.user.B2bUserDetails;
import com.hk.domain.reverseOrder.ReverseOrder;
import com.hk.domain.reverseOrder.ReverseLineItem;
import com.hk.constants.core.EnumTax;
import com.hk.constants.core.TaxConstants;
import com.hk.constants.courier.StateList;
import com.hk.helper.InvoiceNumHelper;
import com.hk.core.fliter.LineItemFilter;

import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Neha
 * Date: Feb 13, 2013
 * Time: 4:40:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReverseOrderInvoiceDto {

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

  //for accounting invoice
  private double totalTaxable;
  private double totalSurcharge;
  private double totalTax;
  private double totalSummaryAmount;
  private double totalSummaryTax;
  private double totalSummarySurcharge;
  private double totalSummaryPayable;
  private long totalSummaryQty;
  HashMap<String, Long> summaryQtyMap = new HashMap<String, Long>();
  HashMap<String, Double> summaryAmountMap = new HashMap<String, Double>();
  HashMap<String, Double> summaryTaxMap = new HashMap<String, Double>();
  HashMap<String, Double> summaryPayableMap = new HashMap<String, Double>();
  HashMap<String, Double> summarySurchargeMap = new HashMap<String, Double>();
  private Set<InvoiceLineItemDto> invoiceLineItemDtos = new HashSet<InvoiceLineItemDto>(0);
  private B2bUserDetails b2bUserDetails;
  String invoiceType;
  String accountingInvoiceHeading;
  String invoiceTinNumber;
  String invoiceDLnumber;



  public ReverseOrderInvoiceDto(ReverseOrder reverseOrder, B2bUserDetails b2bUserDetailsLocal) {

    for (EnumTax enumTax : EnumTax.getEnumTaxByType(TaxConstants.VAT_TYPE)) {
      summaryAmountMap.put(enumTax.getName(), 0.0);
      summaryTaxMap.put(enumTax.getName(), 0.0);
      summarySurchargeMap.put(enumTax.getName(), 0.0);
      summaryPayableMap.put(enumTax.getName(), 0.0);
      summaryQtyMap.put(enumTax.getName(), 0L);
    }

    invoiceType = InvoiceNumHelper.getInvoiceType(reverseOrder.getShippingOrder().isServiceOrder(), reverseOrder.getShippingOrder().getBaseOrder().isB2bOrder());      
	setB2bUserDetails(b2bUserDetailsLocal);

	Set<LineItem> shippingOrderLineItems = new HashSet<LineItem>();
	for(ReverseLineItem reverseLineItem : reverseOrder.getReverseLineItems()){
		LineItem referredLineItem  = reverseLineItem.getReferredLineItem();
		referredLineItem.setQty(reverseLineItem.getReturnQty());
		shippingOrderLineItems.add(referredLineItem);
	}
    LineItemFilter productLineItemFilter = new LineItemFilter(shippingOrderLineItems);
    shippingOrderLineItems = productLineItemFilter.filter();

    //invoiceLineItemDtos = InvoiceLineItemDto.generateInvoiceLineItemDto(shippingOrderLineItem);

    for (LineItem productLineItem : shippingOrderLineItems) {
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

//      if (productLineItem.getHkPrice() != null) {
//        shipping += productLineItem.getShippingCharges();
//      }

//      if (productLineItem.getHkPrice() != null) {
//        cod += productLineItem.getCodCharges();
//      }
      InvoiceLineItemDto invoiceLineItemdto = new InvoiceLineItemDto(productLineItem);
      invoiceLineItemDtos.add(invoiceLineItemdto);

      // for accounting invoice
      totalTax += invoiceLineItemdto.getTax();
      totalTaxable += invoiceLineItemdto.getTaxable();
      totalSurcharge += invoiceLineItemdto.getSurcharge();

      for (EnumTax enumTax : EnumTax.getEnumTaxByType(TaxConstants.VAT_TYPE)) {
        if (!enumTax.equals(EnumTax.NA)) {
          if (invoiceLineItemdto.getTaxValue() == enumTax.getValue()) {
            getSummaryMapsForVat(enumTax.getName(), invoiceLineItemdto);
          }
        }
      }

    }

    for (Map.Entry<String, Double> entry : summaryAmountMap.entrySet()) {
      totalSummaryAmount += entry.getValue();
    }
    for (Map.Entry<String, Double> entry : summaryTaxMap.entrySet()) {
      totalSummaryTax += entry.getValue();
    }
    for (Map.Entry<String, Double> entry : summarySurchargeMap.entrySet()) {
      totalSummarySurcharge += entry.getValue();
    }
    for (Map.Entry<String, Double> entry : summaryPayableMap.entrySet()) {
      totalSummaryPayable += entry.getValue();
    }
    for (Map.Entry<String, Long> entry : summaryQtyMap.entrySet()) {
      totalSummaryQty += entry.getValue();
    }

	cod = 0.0;
	shipping = 0.0;  
    grandTotal = itemsTotal - totalDiscount - rewardPoints + shipping + cod;

    warehouseName = reverseOrder.getShippingOrder().getWarehouse().getName();
    warehouseCity = reverseOrder.getShippingOrder().getWarehouse().getCity();
    if (reverseOrder.getShippingOrder().getWarehouse().getLine1() == null) {
      warehouseAddressLine1 = reverseOrder.getShippingOrder().getWarehouse().getLine1();
    } else {
      warehouseAddressLine1 = "";
    }
    if (reverseOrder.getShippingOrder().getWarehouse().getLine2() == null) {
      warehouseAddressLine2 = reverseOrder.getShippingOrder().getWarehouse().getLine2();
    } else {
      warehouseAddressLine2 = "";
    }
    warehousePh = reverseOrder.getShippingOrder().getWarehouse().getWhPhone();
    warehousePincode = reverseOrder.getShippingOrder().getWarehouse().getPincode();
    warehouseTin = reverseOrder.getShippingOrder().getWarehouse().getTin();
    warehouseState = reverseOrder.getShippingOrder().getWarehouse().getState();

    if (b2bUserDetails != null) {

      //setting invoice heading based on BaseOrder's state.
      if (reverseOrder.getShippingOrder().getBaseOrder().getAddress().getState().equalsIgnoreCase(StateList.HARYANA) && b2bUserDetails.getTin() != null) {
        accountingInvoiceHeading = TaxConstants.TAX_INVOICE;
      } else if (reverseOrder.getShippingOrder().getBaseOrder().getAddress().getState().equalsIgnoreCase(StateList.MAHARASHTRA) && b2bUserDetails.getTin() != null) {
        accountingInvoiceHeading = TaxConstants.TAX_INVOICE;
      } else {
        accountingInvoiceHeading = TaxConstants.RETAIL_INVOICE;
      }

      //setting TIN number based on warehouse state.
      if (warehouseState.equalsIgnoreCase(StateList.HARYANA)) {
        invoiceTinNumber = TaxConstants.B2B_HARYANA_TIN;
      } else if (warehouseState.equalsIgnoreCase(StateList.MAHARASHTRA)) {
        invoiceTinNumber = TaxConstants.B2B_MAHARASHTRA_TIN;
      }

      //setting DL number
      invoiceDLnumber = TaxConstants.B2B_DL_NUMBER;
    } else {
      accountingInvoiceHeading = TaxConstants.RETAIL_INVOICE;
      invoiceTinNumber = TaxConstants.NON_B2B_TIN;
    }

  }

  public void getSummaryMapsForVat(String taxName, InvoiceLineItemDto invoiceLineItemdto) {
    summaryAmountMap.put(taxName, summaryAmountMap.get(taxName) + invoiceLineItemdto.getTaxable());
    summaryTaxMap.put(taxName, summaryTaxMap.get(taxName) + invoiceLineItemdto.getTax());
    summarySurchargeMap.put(taxName, summarySurchargeMap.get(taxName) + invoiceLineItemdto.getSurcharge());
    summaryQtyMap.put(taxName, summaryQtyMap.get(taxName) + invoiceLineItemdto.getQty());
    summaryPayableMap.put(taxName, summaryAmountMap.get(taxName) + summaryTaxMap.get(taxName) + summarySurchargeMap.get(taxName));
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

  public double getTotalTaxable() {
    if (totalTaxable < 0.0) {
      return 0.0;
    }
    return totalTaxable;
  }

  public double getTotalSurcharge() {
    if (totalSurcharge < 0.0) {
      return 0.0;
    }
    return totalSurcharge;
  }

  public double getTotalTax() {
    return totalTax;
  }

  public String getInvoiceType() {
    return invoiceType;
  }

  public String getAccountingInvoiceHeading() {
    return accountingInvoiceHeading;
  }

  public void setAccountingInvoiceHeading(String accountingInvoiceHeading) {
    this.accountingInvoiceHeading = accountingInvoiceHeading;
  }

  public String getInvoiceTinNumber() {
    return invoiceTinNumber;
  }

  public void setInvoiceTinNumber(String invoiceTinNumber) {
    this.invoiceTinNumber = invoiceTinNumber;
  }

  public String getInvoiceDLnumber() {
    return invoiceDLnumber;
  }

  public void setInvoiceDLnumber(String invoiceDLnumber) {
    this.invoiceDLnumber = invoiceDLnumber;
  }

  public HashMap<String, Double> getSummaryAmountMap() {
    return summaryAmountMap;
  }

  public HashMap<String, Double> getSummaryTaxMap() {
    return summaryTaxMap;
  }

  public HashMap<String, Double> getSummaryPayableMap() {
    return summaryPayableMap;
  }

  public HashMap<String, Double> getSummarySurchargeMap() {
    return summarySurchargeMap;
  }

  public HashMap<String, Long> getSummaryQtyMap() {
    return summaryQtyMap;
  }

  public double getTotalSummaryAmount() {
    return totalSummaryAmount;
  }

  public double getTotalSummaryTax() {
    return totalSummaryTax;
  }

  public double getTotalSummarySurcharge() {
    return totalSummarySurcharge;
  }

  public double getTotalSummaryPayable() {
    return totalSummaryPayable;
  }

  public long getTotalSummaryQty() {
    return totalSummaryQty;
  }

  public B2bUserDetails getB2bUserDetails() {
    return b2bUserDetails;
  }

  public void setB2bUserDetails(B2bUserDetails b2bUserDetails) {
    this.b2bUserDetails = b2bUserDetails;
  }

  public static void main(String[] args) {

    HashMap<String, Long> summaryQtyMap = new HashMap<String, Long>();
    HashMap<String, Double> summaryAmountMap = new HashMap<String, Double>();
    HashMap<String, Double> summaryTaxMap = new HashMap<String, Double>();
    HashMap<String, Double> summaryPayableMap = new HashMap<String, Double>();
    HashMap<String, Double> summarySurchargeMap = new HashMap<String, Double>();

    for (EnumTax enumTax : EnumTax.getEnumTaxByType(TaxConstants.VAT_TYPE)) {

      System.out.println(enumTax.name());
      summaryAmountMap.put(enumTax.name(), 0.0);
      summaryTaxMap.put(enumTax.name(), 0.0);
      summarySurchargeMap.put(enumTax.name(), 0.0);
      summaryPayableMap.put(enumTax.name(), 0.0);
      summaryQtyMap.put(enumTax.name(), 0L);

    }


  }
}
