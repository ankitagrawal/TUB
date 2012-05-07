package com.hk.admin.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

import com.hk.admin.dto.inventory.PoLineItemDto;
import com.hk.admin.dto.inventory.PurchaseOrderDto;
import com.hk.admin.impl.dao.inventory.PurchaseOrderDao;
import com.hk.admin.util.TaxUtil;
import com.hk.domain.accounting.PoLineItem;
import com.hk.domain.catalog.product.ProductOption;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.sku.Sku;
import com.hk.domain.warehouse.Warehouse;
import com.hk.dto.TaxComponent;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.inventory.SkuService;

/**
 * Created by IntelliJ IDEA.
 * User: USER
 * Date: Nov 20, 2011
 * Time: 10:54:30 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class PurchaseOrderManager {

  
 PurchaseOrderDao purchaseOrderDaoProvider;
  
  InventoryService inventoryService;
  
  SkuService skuService;

  private List<PurchaseOrder> purchaseOrderList = new ArrayList<PurchaseOrder>();
  private List<PoLineItem> poLineItemList = new ArrayList<PoLineItem>();
  public static final String PO_DETAILS = "PO_DETAILS";
  public static final String ITEM = "ITEM";
  public static final String VARIANT_ID = "VARIANT ID";
  public static final String UPC = "UPC";
  private static final String QTY = "QTY";
  private static final String COST_PRICE = "COST_PRICE";
  private static final String MRP = "MRP";
  public static final String CHECKED_IN_QTY = "CHECKED-IN QTY";
  private static final String ID = "ID:";
  private static final String CREATED_DATE = "Created Date:";
  public static final String CREATED_BY = "Created By:";
  public static final String PO_DATE = "PO Date:";
  public static final String SUPPLIER = "Supplier:";
  public static final String STATUS = "Status:";

  SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");


  public File generatePurchaseOrderXls(String xslFilePath, PurchaseOrder purchaseOrder) throws Exception {
    File file = new File(xslFilePath);
    FileOutputStream out = new FileOutputStream(file);
    Workbook wb = new HSSFWorkbook();

    CellStyle style = wb.createCellStyle();
    CellStyle style_highlight = wb.createCellStyle();
    style_highlight.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
    style_highlight.setFillPattern(CellStyle.SOLID_FOREGROUND);
    Font font = wb.createFont();
    font.setFontHeightInPoints((short) 11);
    font.setColor(Font.COLOR_NORMAL);
    font.setBoldweight(Font.BOLDWEIGHT_BOLD);
    style.setFont(font);
    Font font_bold = wb.createFont();
    font.setBoldweight(Font.BOLDWEIGHT_BOLD);
    style_highlight.setFont(font_bold);
    Sheet sheet1 = wb.createSheet("PO#" + purchaseOrder.getId());
    Row row1 = sheet1.createRow(0);
    row1.setHeightInPoints((short) 30);


    int totalColumnNoInSheet1 = 10;

    Cell cell;
    for (int columnNo = 0; columnNo < totalColumnNoInSheet1; columnNo++) {
      cell = row1.createCell(columnNo);
      cell.setCellStyle(style);
    }

    setCellValue(row1, 0, VARIANT_ID);
    setCellValue(row1, 1, UPC);
    setCellValue(row1, 2, ITEM);
    setCellValue(row1, 3, QTY);
    setCellValue(row1, 4, COST_PRICE);
    setCellValue(row1, 5, MRP);

    int rowCounter = 1;

    /*String poDetail = "";
    poDetail += ID + purchaseOrder.getId() + " " + CREATED_DATE + dateFormat.format(purchaseOrder.getCreateDate()) + " ";
    poDetail += CREATED_BY + purchaseOrder.getCreatedBy().getName() + " " + PO_DATE + dateFormat.format(purchaseOrder.getPoDate()) + " ";
    poDetail += SUPPLIER + purchaseOrder.getSupplier().getName() + " " + STATUS + purchaseOrder.getPurchaseOrderStatus().getName();
    rowCounter++;
    row1 = sheet1.createRow(rowCounter);
    for (int columnNo = 0; columnNo < totalColumnNoInSheet1; columnNo++) {
      cell = row1.createCell(columnNo);
      cell.setCellStyle(style_highlight);
    }
    setCellValue(row1, 0, poDetail);*/

    poLineItemList = purchaseOrder.getPoLineItems();
    for (PoLineItem poLineItem : poLineItemList) {
      rowCounter++;
      row1 = sheet1.createRow(rowCounter);
      for (int columnNo = 0; columnNo < totalColumnNoInSheet1; columnNo++) {
        cell = row1.createCell(columnNo);
      }
      String productOptions = "";
      ProductVariant productVariant = poLineItem.getSku().getProductVariant();
      if (poLineItem != null && productVariant != null && productVariant.getProductOptions() != null) {
        for (ProductOption productOption : productVariant.getProductOptions()) {
          if (productOption != null && productOption.getName() != null) {
            productOptions += productOption.getName() + " " + productOption.getValue() + " ";
          }
        }
      }

      setCellValue(row1, 0, productVariant.getId());
      setCellValue(row1, 1, productVariant.getUpc());
      setCellValue(row1, 2, productVariant.getProduct().getName() + " " + productOptions);
      setCellValue(row1, 3, poLineItem.getQty());
      setCellValue(row1, 4, String.valueOf(poLineItem.getCostPrice()));
      setCellValue(row1, 5, String.valueOf(poLineItem.getMrp()));
    }

    wb.write(out);
    out.close();
    return file;
  }

  public PurchaseOrderDto generatePurchaseOrderDto(PurchaseOrder purchaseOrder) {

    PurchaseOrderDto purchaseOrderDto = new PurchaseOrderDto();
    List<PoLineItemDto> poLineItemDtoList = new ArrayList<PoLineItemDto>();
    purchaseOrderDto.setPurchaseOrder(purchaseOrder);

    Double totalTax = 0.0;
    Double totalSurcharge = 0.0;
    Double totalTaxable = 0.0;
    Double totalPayable = 0.0;

    Warehouse warehouse = purchaseOrder.getWarehouse();

    for (PoLineItem poLineItem : purchaseOrder.getPoLineItems()) {

      Double taxable = 0.0;
      Double tax = 0.0;
      Double surcharge = 0.0;
      Double payable = 0.0;
      ProductVariant productVariant = poLineItem.getSku().getProductVariant();
      Sku sku = skuService.getSKU(productVariant, warehouse);

      PoLineItemDto poLineItemDto = new PoLineItemDto();
      poLineItemDto.setPoLineItem(poLineItem);
      if (poLineItem != null && poLineItem.getCostPrice() != null && poLineItem.getQty() != null) {
        taxable = poLineItem.getCostPrice() * poLineItem.getQty();
      }
	    if (purchaseOrder.getSupplier() != null && purchaseOrder.getSupplier().getState() != null
			    && productVariant != null && sku.getTax() != null) {
		    TaxComponent taxComponent = TaxUtil.getSupplierTaxForPV(purchaseOrder.getSupplier(), sku, taxable);
		    tax = taxComponent.getTax();
		    surcharge = taxComponent.getSurcharge();
		    /*if (purchaseOrder.getSupplier().getState().equals(sku.getWarehouse().getState())) {
									tax = sku.getTax().getValue() * taxable;
		     * Surcharge is calculated only if the state is HARYANA
		     *//*
	        if(sku.getWarehouse().getState().equalsIgnoreCase(StateList.HARYANA)){
          surcharge = tax * StateList.SURCHARGE;
	        }
        } else {
          if (sku.getTax().getValue() != 0.0) {
            tax = StateList.CST * taxable;
            //surcharge = tax * StateList.SURCHARGE;
          }
        }*/
	    }
      payable = taxable + tax + surcharge;
      poLineItemDto.setTaxable(taxable);
      poLineItemDto.setPayable(payable);
      poLineItemDto.setSurcharge(surcharge);
      poLineItemDto.setTax(tax);

      poLineItemDtoList.add(poLineItemDto);
      totalTaxable += taxable;
      totalTax += tax;
      totalSurcharge += surcharge;
      totalPayable += payable;

    }
    purchaseOrderDto.setPoLineItemDtoList(poLineItemDtoList);
    purchaseOrderDto.setTotalTaxable(totalTaxable);
    purchaseOrderDto.setTotalTax(totalTax);
    purchaseOrderDto.setTotalSurcharge(totalSurcharge);
    purchaseOrderDto.setTotalPayable(totalPayable);
    return purchaseOrderDto;

  }

  private void setCellValue(Row row, int column, Long cellValue) {
    if (cellValue != null) {
      Cell cell = row.getCell(column);
      cell.setCellValue(cellValue);
    }
  }

  private void setCellValue(Row row, int column, String cellValue) {
    if (cellValue == null) cellValue = "";
    Cell cell = row.getCell(column);
    cell.setCellValue(cellValue);
  }

  private void setCellValue(Row row, int column, Date cellValue) {
    if (cellValue != null) {
      Cell cell = row.getCell(column);
      cell.setCellValue(cellValue);
    }
  }

  public List<PurchaseOrder> getPurchaseOrderList() {
    return purchaseOrderList;
  }

  public void setPurchaseOrderList(List<PurchaseOrder> purchaseOrderList) {
    this.purchaseOrderList = purchaseOrderList;
  }
}
