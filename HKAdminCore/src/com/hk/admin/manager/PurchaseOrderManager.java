package com.hk.admin.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hk.admin.pact.dao.inventory.PoLineItemDao;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.admin.dto.inventory.PoLineItemDto;
import com.hk.admin.dto.inventory.PurchaseOrderDto;
import com.hk.admin.pact.dao.inventory.PurchaseOrderDao;
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
 * Created by IntelliJ IDEA. User: USER Date: Nov 20, 2011 Time: 10:54:30 PM To change this template use File | Settings |
 * File Templates.
 */
@Component
public class PurchaseOrderManager {

    @Autowired
    PurchaseOrderDao purchaseOrderDao;

	@Autowired
	PoLineItemDao poLineItemDao;

    @Autowired
    InventoryService inventoryService;

    @Autowired
    SkuService skuService;

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

        Cell cell = null;
        for (int columnNo = 0; columnNo < totalColumnNoInSheet1; columnNo++) {
            cell = row1.createCell(columnNo);
            cell.setCellStyle(style);
        }

        setCellValue(row1, 0, VARIANT_ID);
        setCellValue(row1, 1, VARIANT_NAME);
        setCellValue(row1, 2, UPC);
        setCellValue(row1, 3, ITEM);
        setCellValue(row1, 4, QTY);
        setCellValue(row1, 5, COST_PRICE);
        setCellValue(row1, 6, MRP);

        int rowCounter = 1;

        /*
        * String poDetail = ""; poDetail += ID + purchaseOrder.getId() + " " + CREATED_DATE +
        * dateFormat.format(purchaseOrder.getCreateDate()) + " "; poDetail += CREATED_BY +
        * purchaseOrder.getCreatedBy().getName() + " " + PO_DATE + dateFormat.format(purchaseOrder.getPoDate()) + " ";
        * poDetail += SUPPLIER + purchaseOrder.getSupplier().getName() + " " + STATUS +
        * purchaseOrder.getPurchaseOrderStatus().getName(); rowCounter++; row1 = sheet1.createRow(rowCounter); for (int
        * columnNo = 0; columnNo < totalColumnNoInSheet1; columnNo++) { cell = row1.createCell(columnNo);
        * cell.setCellStyle(style_highlight); } setCellValue(row1, 0, poDetail);
        */

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

            //check for variant name null
            String variantName = productVariant.getProduct().getName().concat(" ").concat(productVariant.getVariantName() == null ? "" : productVariant.getVariantName());
            setCellValue(row1, 1, variantName);

            setCellValue(row1, 2, productVariant.getUpc());
            setCellValue(row1, 3, productVariant.getProduct().getName() + " " + productOptions);
            setCellValue(row1, 4, poLineItem.getQty());
            setCellValue(row1, 5, String.valueOf(poLineItem.getCostPrice()));
            setCellValue(row1, 6, String.valueOf(poLineItem.getMrp()));
        }
        addEmptyLine(row1, sheet1, ++rowCounter, cell);
        row1 = sheet1.createRow(++rowCounter);
        cell = row1.createCell(0);
        cell.setCellStyle(style);
        setCellValue(row1, 0, INDICATE_PURCHASE_ORDER_CLAUSE);
        addEmptyLine(row1, sheet1, ++rowCounter, cell);
        row1 = sheet1.createRow(++rowCounter);
        cell = row1.createCell(0);
        cell.setCellStyle(style);
        setCellValue(row1, 0, ITEM_APPROVAL_CLAUSE);
        addEmptyLine(row1, sheet1, ++rowCounter, cell);
        row1 = sheet1.createRow(++rowCounter);
        cell = row1.createCell(0);
        cell.setCellStyle(style);
        setCellValue(row1, 0, NO_EXCESS_SUPPLY_CLAUSE);

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
                if(poLineItem.getMrp() != null) {
                    marginMrpVsCP = (poLineItem.getMrp() - poLineItem.getCostPrice())/poLineItem.getCostPrice()*100;
                }
	            if(poLineItem.getDiscountPercent() != null) {
		            taxable = taxable - poLineItem.getDiscountPercent()/100*taxable;
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
}
