package com.hk.admin.util.inventory;

import com.hk.admin.pact.dao.inventory.AdminProductVariantInventoryDao;
import com.hk.domain.catalog.product.ProductOption;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.crossDomain.InventoryBarcodeMapItem;
import com.hk.domain.order.ShippingOrder;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;

@Component
public class InventoryBarcodeXslManager {

  @Autowired
  AdminProductVariantInventoryDao adminProductVariantInventoryDao;

  public static final String SKU_GROUP_ID = "SKU_GROUP_ID";
  public static final String SKU_GROUP_BARCODE = "SKU_GROUP_BARCODE";
  public static final String SKU_ITEM_BARCODE = "SKU_ITEM_BARCODE";
  public static final String VARIANT_ID = "VARIANT_ID";
  public static final String NAME = "NAME";
  public static final String COST_PRICE = "COST_PRICE";
  public static final String MRP = "MRP";
  public static final String BATCH_NUMBER = "BATCH_NUMBER";
  public static final String MFG_DATE = "MFG_DATE";
  public static final String EXP_DATE = "EXP_DATE";


  public File generateCatalogExcel(ShippingOrder shippingOrder, String xslFilePath) throws Exception {

    File file = new File(xslFilePath);
    file.getParentFile().mkdirs();
    FileOutputStream out = new FileOutputStream(file);
    Workbook wb = new HSSFWorkbook();

    CellStyle style = wb.createCellStyle();
    Font font = wb.createFont();
    font.setFontHeightInPoints((short) 11);
    font.setColor(Font.COLOR_NORMAL);
    font.setBoldweight(Font.BOLDWEIGHT_BOLD);
    style.setFont(font);
    Sheet sheet1 = wb.createSheet("Checkedout Barcodes - " + shippingOrder.getId());

    Row row = sheet1.createRow(0);
    int totalColumnNo = 10;
    for (int columnNo = 0; columnNo < totalColumnNo; columnNo++) {
      row.createCell(columnNo);
    }

    Cell cell;
     int columnNo = 0;
    for (columnNo = 0; columnNo < totalColumnNo; columnNo++) {
      cell = row.createCell(columnNo);
      cell.setCellStyle(style);
    }
    columnNo = 0;
    setCellValue(row, columnNo++, SKU_GROUP_ID);
    setCellValue(row, columnNo++, SKU_GROUP_BARCODE);
    setCellValue(row, columnNo++, SKU_ITEM_BARCODE);
    setCellValue(row, columnNo++, VARIANT_ID);
    setCellValue(row, columnNo++, NAME);
    setCellValue(row, columnNo++, COST_PRICE);
    setCellValue(row, columnNo++, MRP);
    setCellValue(row, columnNo++, BATCH_NUMBER);
    setCellValue(row, columnNo++, MFG_DATE);
    setCellValue(row, columnNo++, EXP_DATE);
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    int initialRowNo = 1;
    for (InventoryBarcodeMapItem item : adminProductVariantInventoryDao.getCheckedOutBarcodeInfo(shippingOrder)) {
      row = sheet1.createRow(initialRowNo);
      for (columnNo = 0; columnNo < totalColumnNo; columnNo++) {
        row.createCell(columnNo);
      }
      columnNo = 0;
      setCellValue(row, columnNo++, item.getSkuGroupId());
      setCellValue(row, columnNo++, item.getSkuGroupBarcode());
      setCellValue(row, columnNo++, item.getSkuItemBarcode());
      setCellValue(row, columnNo++, item.getVariantId());
      setCellValue(row, columnNo++, item.getName());
      setCellValue(row, columnNo++, item.getCostPrice());
      setCellValue(row, columnNo++, item.getMrp());
      setCellValue(row, columnNo++, item.getBatchNumber());
      if (item.getMfgDate() != null)
        setCellValue(row, columnNo++, sdf.format(item.getMfgDate()));
      if (item.getExpDate() != null)
        setCellValue(row, columnNo++, sdf.format(item.getExpDate()));

      initialRowNo++;
    }

    wb.write(out);
    out.close();
    return file;
  }

  private void setCellValue(Row row, int column, Double cellValue) {
    if (cellValue != null) {
      Cell cell = row.getCell(column);
      cell.setCellValue(cellValue);
    }
  }

  @SuppressWarnings("unused")
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


  private String getOptions(ProductVariant productVariant) {
    String result = "";
    for (ProductOption productOption : productVariant.getProductOptions()) {
      result = result + productOption.getName() + ":" + productOption.getValue() + "|";
    }
    if (!result.isEmpty())
      result = result.charAt(result.length() - 1) == '|' ? result.substring(0, result.length() - 1) : result;
    return result;
  }

}