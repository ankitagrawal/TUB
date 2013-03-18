package com.hk.admin.manager;

import static com.akube.framework.util.BaseUtils.newline;
import com.hk.admin.dto.inventory.PoLineItemDto;
import com.hk.admin.dto.inventory.PurchaseOrderDto;
import com.hk.admin.pact.dao.inventory.PoLineItemDao;
import com.hk.admin.pact.dao.inventory.PurchaseOrderDao;
import com.hk.admin.util.TaxUtil;
import static com.hk.constants.core.HealthkartConstants.CompanyName.brightLifeCarePvtLtd;
import com.hk.domain.accounting.PoLineItem;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.sku.Sku;
import com.hk.domain.warehouse.Warehouse;
import com.hk.dto.TaxComponent;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.pact.service.core.WarehouseService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA. User: USER Date: Nov 20, 2011 Time: 10:54:30 PM To change this template use File | Settings |
 * File Templates.
 */
@Component
public class PurchaseOrderManager {

	@Autowired
	PurchaseOrderDao purchaseOrderDao;

	@Autowired
	InventoryService inventoryService;

	@Autowired
	SkuService skuService;

	@Autowired
	PoLineItemDao poLineItemDao;

  @Autowired
  WarehouseService warehouseService;

	private List<PurchaseOrder> purchaseOrderList = new ArrayList<PurchaseOrder>();
	private List<PoLineItem> poLineItemList = new ArrayList<PoLineItem>();
	public static final String PO_DETAILS = "PO_DETAILS";
	public static final String ITEM = "ITEM";
	public static final String VARIANT_ID = "VARIANT ID";
	public static final String VARIANT_NAME = "VARIANT NAME";
	public static final String UPC = "UPC";
	private static final String QTY = "QTY";
	private static final String COST_PRICE = "COST_PRICE";
	private static final String MRP = "MRP";
	public static final String SUPPLIER_CODE = "SUPPLIER CODE";
	public static final String CHECKED_IN_QTY = "CHECKED-IN QTY";
	private static final String ID = "ID:";
	private static final String CREATED_DATE = "Created Date:";
	public static final String CREATED_BY = "Created By:";
	public static final String PO_DATE = "PO Date:";
	public static final String SUPPLIER = "Supplier:";
	public static final String STATUS = "Status:";
	public static final String INDICATE_PURCHASE_ORDER_CLAUSE = "1) Please indicate Purchase Order number on all invoice and challan and correspondence.";
	public static final String ITEM_APPROVAL_CLAUSE = "2) The item supplied will be subject to our approval and all rejections will be to your account.";
	public static final String NO_EXCESS_SUPPLY_CLAUSE = "3) No excess supply will be accepted ,unless agreed in writing by us.";
	public static final String TAX_RATE = "TAX RATE";
	public static final String TAXABLE = "TAXABLE";
	public static final String TAX = "TAX";
	public static final String SURCHARGE = "SURCHARGE";
	public static final String PAYABLE = "PAYABLE";
	public static final int NO_OF_COLUMNS_IN_ROW0 = 1;
	public static final int NO_OF_COLUMNS_IN_ROW1 = 3;

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
		int rowCounter = 0;
		Row row0 = sheet1.createRow(rowCounter++);
		Row row1 = sheet1.createRow(rowCounter++);
		Row row2 = sheet1.createRow(rowCounter++);
		row2.setHeightInPoints((short) 30);


		StringBuffer warehouseAddress = new StringBuffer();
		warehouseAddress.append("Billing Address- " + newline + brightLifeCarePvtLtd);
		if (purchaseOrder.getWarehouse() != null) {
			if (purchaseOrder.getWarehouse().getLine1() != null) {
				warehouseAddress.append(newline + purchaseOrder.getWarehouse().getLine1());
			}
			if (purchaseOrder.getWarehouse().getLine2() != null) {
				warehouseAddress.append(newline + purchaseOrder.getWarehouse().getLine2());
			}
			warehouseAddress.append(newline + purchaseOrder.getWarehouse().getCity());
			warehouseAddress.append(newline + purchaseOrder.getWarehouse().getState());
			//warehouseAddress.append(newline + "TIN: " + purchaseOrder.getWarehouse().getTin());
      if (purchaseOrder.getWarehouse().equals(getWarehouseService().getMumbaiWarehouse())) {
        warehouseAddress.append(newline + "TIN: 27210893736");
      } else {
        warehouseAddress.append(newline + "TIN: 06101832036");
      }

		}

		StringBuffer supplierDetails = new StringBuffer();
		supplierDetails.append(purchaseOrder.getSupplier().getName());
		if (purchaseOrder.getSupplier().getLine1() != null) {
			supplierDetails.append(newline + purchaseOrder.getSupplier().getLine1());
		}
		if (purchaseOrder.getSupplier().getLine2() != null) {
			supplierDetails.append(newline + purchaseOrder.getSupplier().getLine2());
		}
		if (purchaseOrder.getSupplier().getCity() != null) {
			supplierDetails.append(newline + purchaseOrder.getSupplier().getCity());
		}
		if (purchaseOrder.getSupplier().getState() != null) {
			supplierDetails.append(newline + purchaseOrder.getSupplier().getState());
		}

		for (int columnNo = 0; columnNo < NO_OF_COLUMNS_IN_ROW0; columnNo++) {
			row0.createCell(columnNo);
		}
		setCellValue(row0, 0, warehouseAddress.toString());

		for (int columnNo = 0; columnNo < NO_OF_COLUMNS_IN_ROW1; columnNo++) {
			row1.createCell(columnNo);
		}
		setCellValue(row1, 0, "Supplier - " + newline + supplierDetails);
		setCellValue(row1, 1, "PO#: " + purchaseOrder.getId() + newline +" PO Date: " +  dateFormat.format(purchaseOrder.getCreateDate()));
		setCellValue(row1, 2, "Contact Name: " + (purchaseOrder.getSupplier().getContactPerson() != null ? purchaseOrder.getSupplier().getContactPerson() : "")
				+ newline + " Contact Number: " + (purchaseOrder.getSupplier().getContactNumber() != null ? purchaseOrder.getSupplier().getContactNumber() : " "));

		int totalColumnNoInSheet1 = 13;

		Cell cell = null;

		for (int columnNo = 0; columnNo < totalColumnNoInSheet1; columnNo++) {
			cell = row2.createCell(columnNo);
			cell.setCellStyle(style);
		}

		setCellValue(row2, 0, VARIANT_ID);
		setCellValue(row2, 1, VARIANT_NAME);
		setCellValue(row2, 2, UPC);
		setCellValue(row2, 3, ITEM);
		setCellValue(row2, 4, QTY);
		setCellValue(row2, 5, MRP);
		setCellValue(row2, 6, COST_PRICE);
		setCellValue(row2, 7, TAX_RATE);
		setCellValue(row2, 8, TAXABLE);
		setCellValue(row2, 9, TAX);
		setCellValue(row2, 10, SURCHARGE);
		setCellValue(row2, 11, PAYABLE);
		setCellValue(row2, 12, SUPPLIER_CODE);

		/*
				* String poDetail = ""; poDetail += ID + purchaseOrder.getId() + " " + CREATED_DATE +
				* dateFormat.format(purchaseOrder.getCreateDate()) + " "; poDetail += CREATED_BY +
				* purchaseOrder.getCreatedBy().getName() + " " + PO_DATE + dateFormat.format(purchaseOrder.getPoDate()) + " ";
				* poDetail += SUPPLIER + purchaseOrder.getSupplier().getName() + " " + STATUS +
				* purchaseOrder.getPurchaseOrderStatus().getName(); rowCounter++; row2 = sheet1.createRow(rowCounter); for (int
				* columnNo = 0; columnNo < totalColumnNoInSheet1; columnNo++) { cell = row2.createCell(columnNo);
				* cell.setCellStyle(style_highlight); } setCellValue(row2, 0, poDetail);
				*/

		poLineItemList = purchaseOrder.getPoLineItems();
		for (PoLineItem poLineItem : poLineItemList) {
			rowCounter++;
			row2 = sheet1.createRow(rowCounter);
			for (int columnNo = 0; columnNo < totalColumnNoInSheet1; columnNo++) {
				cell = row2.createCell(columnNo);
			}
			ProductVariant productVariant = poLineItem.getSku().getProductVariant();
			String supplierCode = productVariant.getSupplierCode();
			setCellValue(row2, 0, productVariant.getId());

			//check for variant name null
			String variantName = productVariant.getProduct().getName().concat(" ").concat(productVariant.getVariantName() == null ? "" : productVariant.getVariantName());
			TaxComponent taxComponent = TaxUtil.getSupplierTaxForPV(purchaseOrder.getSupplier(), poLineItem.getSku(), poLineItem.getTaxableAmount());
			setCellValue(row2, 1, variantName);

			setCellValue(row2, 2, productVariant.getUpc());
			setCellValue(row2, 3, productVariant.getProduct().getName() + " " + productVariant.getOptionsCommaSeparated());
			setCellValue(row2, 4, poLineItem.getQty());
			setCellValue(row2, 5, String.valueOf(poLineItem.getMrp()));
			setCellValue(row2, 6, String.valueOf(poLineItem.getCostPrice()));
			setCellValue(row2, 7, String.valueOf(taxComponent.getTaxRate() * 100));
			setCellValue(row2, 8, poLineItem.getTaxableAmount() != null ? String.valueOf(poLineItem.getTaxableAmount()) : "");
			setCellValue(row2, 9, poLineItem.getTaxAmount() != null ? String.valueOf(poLineItem.getTaxAmount()) : "");
			setCellValue(row2, 10, poLineItem.getSurchargeAmount() != null ? String.valueOf(poLineItem.getSurchargeAmount()) : "");
			setCellValue(row2, 11, poLineItem.getPayableAmount() != null ? String.valueOf(poLineItem.getPayableAmount()) : "");
			setCellValue(row2, 12, supplierCode == null ? "" : supplierCode);
		}
		addEmptyLine(row2, sheet1, ++rowCounter, cell);
		row2 = sheet1.createRow(++rowCounter);
		cell = row2.createCell(0);
		cell.setCellStyle(style);
		setCellValue(row2, 0, INDICATE_PURCHASE_ORDER_CLAUSE);
		addEmptyLine(row2, sheet1, ++rowCounter, cell);
		row2 = sheet1.createRow(++rowCounter);
		cell = row2.createCell(0);
		cell.setCellStyle(style);
		setCellValue(row2, 0, ITEM_APPROVAL_CLAUSE);
		addEmptyLine(row2, sheet1, ++rowCounter, cell);
		row2 = sheet1.createRow(++rowCounter);
		cell = row2.createCell(0);
		cell.setCellStyle(style);
		setCellValue(row2, 0, NO_EXCESS_SUPPLY_CLAUSE);

		wb.write(out);
		out.close();
		return file;
	}

	private void addEmptyLine(Row row1, Sheet sheet1, int rowCounter, Cell cell) {
		row1 = sheet1.createRow(rowCounter);
		cell = row1.createCell(0);
		setCellValue(row1, 0, " ");

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
			Double marginMrpVsCP = 0.0;
			ProductVariant productVariant = poLineItem.getSku().getProductVariant();
			Sku sku = skuService.getSKU(productVariant, warehouse);

			PoLineItemDto poLineItemDto = new PoLineItemDto();
			poLineItemDto.setPoLineItem(poLineItem);
			if (poLineItem != null && poLineItem.getCostPrice() != null && poLineItem.getQty() != null) {
				taxable = poLineItem.getCostPrice() * poLineItem.getQty();
				if (poLineItem.getMrp() != null && poLineItem.getMrp() > 0) {
					marginMrpVsCP = (poLineItem.getMrp() - poLineItem.getCostPrice()) / poLineItem.getMrp() * 100;
				}
				if (poLineItem.getDiscountPercent() != null) {
					taxable = taxable - poLineItem.getDiscountPercent() / 100 * taxable;
				}

			}
			if (purchaseOrder.getSupplier() != null && purchaseOrder.getSupplier().getState() != null && productVariant != null && sku.getTax() != null) {
				TaxComponent taxComponent = TaxUtil.getSupplierTaxForPV(purchaseOrder.getSupplier(), sku, taxable);
				tax = taxComponent.getTax();
				surcharge = taxComponent.getSurcharge();
				/*
								* if (purchaseOrder.getSupplier().getState().equals(sku.getWarehouse().getState())) { tax =
								* sku.getTax().getValue() * taxable; Surcharge is calculated only if the state is HARYANA
								*//*
                     * if(sku.getWarehouse().getState().equalsIgnoreCase(StateList.HARYANA)){ surcharge = tax *
                     * StateList.SURCHARGE; } } else { if (sku.getTax().getValue() != 0.0) { tax = StateList.CST *
                     * taxable; //surcharge = tax * StateList.SURCHARGE; } }
                     */
			}
			payable = taxable + tax + surcharge;
			poLineItemDto.setTaxable(taxable);
			poLineItemDto.setPayable(payable);
			poLineItemDto.setSurcharge(surcharge);
			poLineItemDto.setTax(tax);

			poLineItemDto.setMarginMrpVsCP(marginMrpVsCP);

			poLineItemDtoList.add(poLineItemDto);
			totalTaxable += taxable;
			totalTax += tax;
			totalSurcharge += surcharge;
			totalPayable += payable;
			poLineItem.setTaxableAmount(taxable);
			poLineItem.setPayableAmount(payable);
			poLineItem.setSurchargeAmount(surcharge);
			poLineItem.setTaxAmount(tax);
			poLineItemDao.save(poLineItem);
		}
		purchaseOrderDto.setPoLineItemDtoList(poLineItemDtoList);
		purchaseOrderDto.setTotalTaxable(totalTaxable);
		purchaseOrderDto.setTotalTax(totalTax);
		purchaseOrderDto.setTotalSurcharge(totalSurcharge);
		purchaseOrderDto.setTotalPayable(totalPayable);
		double overallDiscount = purchaseOrder.getDiscount() == null ? 0.0 : purchaseOrder.getDiscount();
		purchaseOrderDto.setFinalPayable(totalPayable - overallDiscount);

		purchaseOrder.setTaxableAmount(totalTaxable);
		purchaseOrder.setTaxAmount(totalTax);
		purchaseOrder.setSurchargeAmount(totalSurcharge);
		purchaseOrderDao.save(purchaseOrder);
		return purchaseOrderDto;
	}

	private void setCellValue(Row row, int column, Long cellValue) {
		if (cellValue != null) {
			Cell cell = row.getCell(column);
			cell.setCellValue(cellValue);
		}
	}

	private void setCellValue(Row row, int column, String cellValue) {
		if (cellValue == null)
			cellValue = "";
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

  public WarehouseService getWarehouseService() {
    return warehouseService;
  }
}
