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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.admin.dto.inventory.DebitNoteDto;
import com.hk.admin.dto.inventory.DebitNoteLineItemDto;
import com.hk.admin.dto.inventory.GRNDto;
import com.hk.admin.dto.inventory.GrnLineItemDto;
import com.hk.admin.pact.dao.inventory.GoodsReceivedNoteDao;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.admin.util.TaxUtil;
import com.hk.domain.accounting.DebitNote;
import com.hk.domain.accounting.DebitNoteLineItem;
import com.hk.domain.catalog.product.ProductOption;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.GrnLineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.warehouse.Warehouse;
import com.hk.dto.TaxComponent;
import com.hk.pact.service.inventory.SkuService;

@Component
public class GRNManager {

    @Autowired
    private GoodsReceivedNoteDao    goodsReceivedNoteDao;
    @Autowired
    private AdminInventoryService   adminInventoryService;
    @Autowired
    private SkuService              skuService;

    private List<GoodsReceivedNote> grnList         = new ArrayList<GoodsReceivedNote>();
    private List<GrnLineItem>       grnLineItemList = new ArrayList<GrnLineItem>();
    public static final String      PO_DETAILS      = "PO_DETAILS";
    public static final String      ITEM            = "ITEM";
    public static final String      VARIANT_ID      = "VARIANT ID";
    public static final String      UPC             = "UPC";
    private static final String     QTY             = "QTY";
    public static final String      CHECKED_IN_QTY  = "CHECKED-IN QTY";
    private static final String     ID              = "ID:";
    private static final String     CREATED_DATE    = "Created Date:";
    public static final String      CREATED_BY      = "Created By:";
    public static final String      PO_DATE         = "PO Date:";
    public static final String      SUPPLIER        = "Supplier:";
    public static final String      STATUS          = "Status:";

    SimpleDateFormat                dateFormat      = new SimpleDateFormat("dd/MM/yyyy");

    public File generateGRNXls(String xslFilePath, Long grnStatusValue, Date startDate, Date endDate, Warehouse warehouse, Boolean reconciled) throws Exception {

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
        Sheet sheet1 = wb.createSheet("GoodsReceivedNote-GRN List");
        Row row1 = sheet1.createRow(0);
        row1.setHeightInPoints((short) 30);

        int totalColumnNoInSheet1 = 6;

        Cell cell;
        for (int columnNo = 0; columnNo < totalColumnNoInSheet1; columnNo++) {
            cell = row1.createCell(columnNo);
            cell.setCellStyle(style);
        }
        setCellValue(row1, 0, PO_DETAILS);
        setCellValue(row1, 1, ITEM);
        setCellValue(row1, 2, VARIANT_ID);
        setCellValue(row1, 3, UPC);
        setCellValue(row1, 4, QTY);
        setCellValue(row1, 5, CHECKED_IN_QTY);
        // setCellValue(row1, 6, ACTUAL_TIME_ELAPSED);

        /*
         * SimpleDateFormat sdf_day_of_week = new SimpleDateFormat("E"); SimpleDateFormat sdf_hour = new
         * SimpleDateFormat("H"); SimpleDateFormat sdf_complete_date_format = new SimpleDateFormat("EEE MMM dd HH:mm:ss
         * zzz yyyy");
         */

        int rowCounter = 1;

        grnList = getGoodsReceivedNoteDao().listGRNsExcludingStatusInTimeFrame(grnStatusValue, startDate, endDate, warehouse, reconciled);
        // System.out.println("grnList: " + grnList.size());
        for (GoodsReceivedNote grn : grnList) {
            String poDetail = "";
            poDetail += ID + grn.getId() + " " + CREATED_DATE + dateFormat.format(grn.getGrnDate()) + " ";
            poDetail += CREATED_BY + grn.getPurchaseOrder().getCreatedBy().getName() + " " + PO_DATE + dateFormat.format(grn.getPurchaseOrder().getPoDate()) + " ";
            poDetail += SUPPLIER + grn.getPurchaseOrder().getSupplier().getName() + " " + STATUS + grn.getGrnStatus().getName();
            rowCounter++;
            row1 = sheet1.createRow(rowCounter);
            for (int columnNo = 0; columnNo < totalColumnNoInSheet1; columnNo++) {
                cell = row1.createCell(columnNo);
                cell.setCellStyle(style_highlight);
            }
            setCellValue(row1, 0, poDetail);
            grnLineItemList = grn.getGrnLineItems();
            for (GrnLineItem grnLineItem : grnLineItemList) {
                rowCounter++;
                row1 = sheet1.createRow(rowCounter);
                for (int columnNo = 0; columnNo < totalColumnNoInSheet1; columnNo++) {
                    cell = row1.createCell(columnNo);
                }
                String productOptions = "";
                if (grnLineItem != null && grnLineItem.getSku().getProductVariant() != null && grnLineItem.getSku().getProductVariant().getProductOptions() != null) {
                    for (ProductOption productOption : grnLineItem.getSku().getProductVariant().getProductOptions()) {
                        if (productOption != null && productOption.getName() != null) {
                            productOptions += productOption.getName() + " " + productOption.getValue() + " ";
                        }
                    }
                }
                setCellValue(row1, 1, grnLineItem.getSku().getProductVariant().getProduct().getName() + productOptions);
                setCellValue(row1, 2, grnLineItem.getSku().getProductVariant().getId());
                setCellValue(row1, 3, grnLineItem.getSku().getProductVariant().getUpc());
                setCellValue(row1, 4, grnLineItem.getQty());
                setCellValue(row1, 5, getAdminInventoryService().countOfCheckedInUnitsForGrnLineItem(grnLineItem));
            }
        }
        wb.write(out);
        out.close();
        return file;
    }

    public GRNDto generateGRNDto(GoodsReceivedNote grn) {
        GRNDto grnDto = new GRNDto();
        List<GrnLineItemDto> grnLineItemDtoList = new ArrayList<GrnLineItemDto>();
        grnDto.setGrn(grn);

        Double totalTax = 0.0;
        Double totalSurcharge = 0.0;
        Double totalTaxable = 0.0;
        Double totalPayable = 0.0;
        Double marginMrpVsCP = 0.0;

        @SuppressWarnings("unused")
        Warehouse warehouse = grn.getWarehouse();

        for (GrnLineItem grnLineItem : grn.getGrnLineItems()) {

            Double taxable = 0.0;
            Double payable = 0.0;

            GrnLineItemDto grnLineItemDto = new GrnLineItemDto();
            grnLineItemDto.setGrnLineItem(grnLineItem);
            if (grnLineItem != null && grnLineItem.getCostPrice() != null && grnLineItem.getQty() != null) {
                taxable = grnLineItem.getCostPrice() * grnLineItem.getQty();
                if(grnLineItem.getMrp() != null) {
                    marginMrpVsCP = (grnLineItem.getMrp() - grnLineItem.getCostPrice())/grnLineItem.getMrp()*100;
                }
            }

            Sku sku = grnLineItem.getSku();

            TaxComponent taxComponent = TaxUtil.getSupplierTaxForPV(grn.getPurchaseOrder().getSupplier(), sku, taxable);
            Double tax = taxComponent.getTax();
            Double surcharge = taxComponent.getSurcharge();

            payable = taxable + tax + surcharge;
            grnLineItemDto.setTaxable(taxable);
            grnLineItemDto.setPayable(payable);
            grnLineItemDto.setSurcharge(surcharge);
            grnLineItemDto.setTax(tax);
            grnLineItemDto.setMarginMrpVsCP(marginMrpVsCP);

            grnLineItemDtoList.add(grnLineItemDto);
            totalTaxable += taxable;
            totalTax += tax;
            totalSurcharge += surcharge;
            totalPayable += payable;

        }
        grnDto.setGrnLineItemDtoList(grnLineItemDtoList);
        grnDto.setTotalTaxable(totalTaxable);
        grnDto.setTotalTax(totalTax);
        grnDto.setTotalSurcharge(totalSurcharge);
        grnDto.setTotalPayable(totalPayable);
        return grnDto;

    }

    public DebitNoteDto generateDebitNoteDto(DebitNote debitNote) {
        DebitNoteDto debitNoteDto = new DebitNoteDto();
        List<DebitNoteLineItemDto> debitNoteLineItemDtoList = new ArrayList<DebitNoteLineItemDto>();
        Double totalTax = 0.0;
        Double totalSurcharge = 0.0;
        Double totalTaxable = 0.0;
        Double totalPayable = 0.0;

        for (DebitNoteLineItem debitNoteLineItem : debitNote.getDebitNoteLineItems()) {

            Double taxable = 0.0;
            Double tax = 0.0;
            Double surcharge = 0.0;
            Double payable = 0.0;

            Sku sku = debitNoteLineItem.getSku();

            // ProductVariant productVariant = debitNoteLineItem.getProductVariant();

            DebitNoteLineItemDto debitNoteLineItemDto = new DebitNoteLineItemDto();
            debitNoteLineItemDto.setDebitNoteLineItem(debitNoteLineItem);
            if (debitNoteLineItem != null && debitNoteLineItem.getCostPrice() != null && debitNoteLineItem.getQty() != null) {
                taxable = debitNoteLineItem.getCostPrice() * debitNoteLineItem.getQty();
            }
            // GoodsReceivedNote grn = debitNoteLineItem.getDebitNote().getGoodsReceivedNote();
            @SuppressWarnings("unused")
            Warehouse warehouse = debitNote.getWarehouse();
            // Sku sku = skuService.getSKU(productVariant, warehouse);
            ProductVariant productVariant = sku.getProductVariant();

            if (debitNote.getSupplier() != null && debitNote.getSupplier().getState() != null && productVariant != null && sku.getTax() != null) {
                /*
                 * Tax skuTax = sku.getTax(); if
                 * (grn.getPurchaseOrder().getSupplier().getState().equals(sku.getWarehouse().getState())) { tax =
                 * skuTax.getValue() * taxable; surcharge = tax * StateList.SURCHARGE; } else { if (skuTax.getValue() !=
                 * 0.0) { tax = StateList.CST * taxable; //surcharge = tax * StateList.SURCHARGE; } }
                 */
                TaxComponent taxComponent = TaxUtil.getSupplierTaxForPV(debitNote.getSupplier(), sku, taxable);
                tax = taxComponent.getTax();
                surcharge = taxComponent.getSurcharge();
            }
            payable = taxable + tax + surcharge;
            debitNoteLineItemDto.setTaxable(taxable);
            debitNoteLineItemDto.setPayable(payable);
            debitNoteLineItemDto.setSurcharge(surcharge);
            debitNoteLineItemDto.setTax(tax);

            debitNoteLineItemDtoList.add(debitNoteLineItemDto);
            totalTaxable += taxable;
            totalTax += tax;
            totalSurcharge += surcharge;
            totalPayable += payable;

        }
        debitNoteDto.setDebitNoteLineItemDtoList(debitNoteLineItemDtoList);
        debitNoteDto.setTotalTaxable(totalTaxable);
        debitNoteDto.setTotalTax(totalTax);
        debitNoteDto.setTotalSurcharge(totalSurcharge);
        debitNoteDto.setTotalPayable(totalPayable);
        return debitNoteDto;

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

    @SuppressWarnings("unused")
    private void setCellValue(Row row, int column, Date cellValue) {
        if (cellValue != null) {
            Cell cell = row.getCell(column);
            cell.setCellValue(cellValue);
        }
    }

    public GoodsReceivedNoteDao getGoodsReceivedNoteDao() {
        return goodsReceivedNoteDao;
    }

    public void setGoodsReceivedNoteDao(GoodsReceivedNoteDao goodsReceivedNoteDao) {
        this.goodsReceivedNoteDao = goodsReceivedNoteDao;
    }

    public AdminInventoryService getAdminInventoryService() {
        return adminInventoryService;
    }

    public void setInventoryService(AdminInventoryService inventoryService) {
        this.adminInventoryService = inventoryService;
    }

    public SkuService getSkuService() {
        return skuService;
    }

    public void setSkuService(SkuService skuService) {
        this.skuService = skuService;
    }

}