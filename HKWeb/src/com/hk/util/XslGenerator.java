package com.hk.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.hk.admin.pact.dao.courier.CourierDao;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.constants.XlsConstants;
import com.hk.domain.catalog.Manufacturer;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductExtraOption;
import com.hk.domain.catalog.product.ProductOption;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.Pincode;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierServiceInfo;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.GrnLineItem;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.service.ServiceLocatorFactory;

@Component
public class XslGenerator {

  private static Logger logger = LoggerFactory.getLogger(XslGenerator.class);

  public static final String ID = "ID";
  public static final String PINCODE = "PINCODE";
  public static final String IS_PREFERRED = "IS_PREFERRED";
  public static final String IS_PREFERRED_COD = "IS_PREFERRED_COD";
  public static final String ROUTING_CODE = "ROUTING_CODE";
  public static final String COD_AVAILABLE = "COD_AVAILABLE";
  public static final String COURIER_ID = "COURIER_ID";
  public static final String IS_DELETED = "IS_DELETED";
  public static final String CITY = "CITY";
  public static final String STATE = "STATE";
  public static final String LOCALITY= "LOCALITY";
  public static final String DEFAULT_COURIER_ID="DEFAULT_COURIER_ID";

  
  CourierDao courierDao;

  
  InventoryService inventoryService;
  
  private AdminInventoryService adminInventoryService;
  
  SkuService skuService;

  private Map<String, Integer> headerMap = new HashMap<String, Integer>();

  public File generateCatalogExcel(List<Product> products, String xslFilePath) throws Exception {

    File file = new File(xslFilePath);
    file.getParentFile().mkdirs();
    FileOutputStream out = new FileOutputStream(file);
    Workbook wb = new HSSFWorkbook();

    CellStyle style = wb.createCellStyle();
    Font font = wb.createFont();
    font.setFontHeightInPoints((short) 12);
    font.setColor(Font.COLOR_NORMAL);
    font.setBoldweight(Font.BOLDWEIGHT_BOLD);
    style.setFont(font);
    Sheet sheet1 = wb.createSheet("Product");
    Row row = sheet1.createRow(0);
    row.setHeightInPoints((short) 25);

    int totalColumnNo = 50;

    Set<Manufacturer> manufacturers = new HashSet<Manufacturer>();
    Cell cell;
    for (int columnNo = 0; columnNo < totalColumnNo; columnNo++) {
      cell = row.createCell(columnNo);
      cell.setCellStyle(style);
    }


    int cellCounter = 0;
    setHeaderCellValue(row, cellCounter++, XlsConstants.PRODUCT_ID);
    setHeaderCellValue(row, cellCounter++, XlsConstants.CATEGORY);
    setHeaderCellValue(row, cellCounter++, XlsConstants.PRIMARY_CATEGORY);
	  setHeaderCellValue(row, cellCounter++, XlsConstants.SECONDARY_CATEGORY);
    setHeaderCellValue(row, cellCounter++, XlsConstants.PRODUCT_NAME);
    setHeaderCellValue(row, cellCounter++, XlsConstants.SORTING);
    setHeaderCellValue(row, cellCounter++, XlsConstants.BRAND);
    setHeaderCellValue(row, cellCounter++, XlsConstants.MANUFACTURER);
    setHeaderCellValue(row, cellCounter++, XlsConstants.SUPPLIER_TIN);
    setHeaderCellValue(row, cellCounter++, XlsConstants.SUPPLIER_STATE);
    setHeaderCellValue(row, cellCounter++, XlsConstants.MIN_DAYS_TO_PROCESS);
    setHeaderCellValue(row, cellCounter++, XlsConstants.MAX_DAYS_TO_PROCESS);
    setHeaderCellValue(row, cellCounter++, XlsConstants.OVERVIEW);
    setHeaderCellValue(row, cellCounter++, XlsConstants.DESCRIPTION);
    setHeaderCellValue(row, cellCounter++, XlsConstants.RELATED_PRODUCTS);
    setHeaderCellValue(row, cellCounter++, XlsConstants.COLOR_PRODUCT);
    setHeaderCellValue(row, cellCounter++, XlsConstants.IS_SERVICE);
    setHeaderCellValue(row, cellCounter++, XlsConstants.IS_GOOGLE_AD_DISALLOWED);
    setHeaderCellValue(row, cellCounter++, XlsConstants.VARIANT_ID);
    setHeaderCellValue(row, cellCounter++, XlsConstants.VARIANT_SORTING);
    setHeaderCellValue(row, cellCounter++, XlsConstants.VARIANT_NAME);
    setHeaderCellValue(row, cellCounter++, XlsConstants.OPTIONS);
    setHeaderCellValue(row, cellCounter++, XlsConstants.EXTRA_OPTIONS);
    setHeaderCellValue(row, cellCounter++, XlsConstants.MRP);
    setHeaderCellValue(row, cellCounter++, XlsConstants.COST);
    setHeaderCellValue(row, cellCounter++, XlsConstants.TAX);
    setHeaderCellValue(row, cellCounter++, XlsConstants.PERCENTAGE_DISCOUNT);
    setHeaderCellValue(row, cellCounter++, XlsConstants.HK_PRICE);
    setHeaderCellValue(row, cellCounter++, XlsConstants.POSTPAID_AMOUNT);
    setHeaderCellValue(row, cellCounter++, XlsConstants.SHIPPING);
    setHeaderCellValue(row, cellCounter++, XlsConstants.AVAILABILITY);
    setHeaderCellValue(row, cellCounter++, XlsConstants.MANUFACTURER_CODE);
    setHeaderCellValue(row, cellCounter++, XlsConstants.DELETED);
    setHeaderCellValue(row, cellCounter++, XlsConstants.INVENTORY);
    setHeaderCellValue(row, cellCounter++, XlsConstants.CUTOFF_INVENTORY);
    setHeaderCellValue(row, cellCounter++, XlsConstants.AFFILIATE_CATEGORY);
    setHeaderCellValue(row, cellCounter++, XlsConstants.COLOR_HEX);
    setHeaderCellValue(row, cellCounter++, XlsConstants.MAIN_IMAGE_ID);
    setHeaderCellValue(row, cellCounter++, XlsConstants.LENGTH);
    setHeaderCellValue(row, cellCounter++, XlsConstants.BREADTH);
    setHeaderCellValue(row, cellCounter++, XlsConstants.HEIGHT);
    setHeaderCellValue(row, cellCounter++, XlsConstants.WEIGHT);
    setHeaderCellValue(row, cellCounter++, XlsConstants.UPC);
    setHeaderCellValue(row, cellCounter++, XlsConstants.SERVICE_TYPE);
    setHeaderCellValue(row, cellCounter++, XlsConstants.PAYMENT_TYPE);


    int initialRowNo = 1;
    for (Product product : products) {
      if (product.getManufacturer() != null) {
        manufacturers.add(product.getManufacturer());
      }
      int variantCtr = 0;

      for (ProductVariant productVariant : product.getProductVariants()) {
        variantCtr++;
        row = sheet1.createRow(initialRowNo);
        for (int columnNo = 0; columnNo < totalColumnNo; columnNo++) {
          row.createCell(columnNo);
        }
        if (variantCtr == 1) {
          setCellValue(row, this.getColumnIndex(XlsConstants.PRODUCT_ID), product.getId());
          setCellValue(row, this.getColumnIndex(XlsConstants.CATEGORY), getCategories(product));
          if (product.getPrimaryCategory() != null) {
            setCellValue(row, this.getColumnIndex(XlsConstants.PRIMARY_CATEGORY), product.getPrimaryCategory().getDisplayName());
          } else {
            setCellValue(row, this.getColumnIndex(XlsConstants.PRIMARY_CATEGORY), "");
          }
	        if (product.getSecondaryCategory() != null) {
            setCellValue(row, this.getColumnIndex(XlsConstants.SECONDARY_CATEGORY), product.getSecondaryCategory().getDisplayName());
          } else {
            setCellValue(row, this.getColumnIndex(XlsConstants.SECONDARY_CATEGORY), "");
          }
          setCellValue(row, this.getColumnIndex(XlsConstants.PRODUCT_NAME), product.getName());
          setCellValue(row, this.getColumnIndex(XlsConstants.SORTING), product.getOrderRanking());
          setCellValue(row, this.getColumnIndex(XlsConstants.BRAND), product.getBrand());
          if (product.getManufacturer() != null) {
            setCellValue(row, this.getColumnIndex(XlsConstants.MANUFACTURER), product.getManufacturer().getName());
          }
          if (product.getSupplier() != null) {
            setCellValue(row, this.getColumnIndex(XlsConstants.SUPPLIER_TIN), product.getSupplier().getTinNumber());
            setCellValue(row, this.getColumnIndex(XlsConstants.SUPPLIER_STATE), product.getSupplier().getState());
          }
          setCellValue(row, this.getColumnIndex(XlsConstants.MIN_DAYS_TO_PROCESS), product.getMinDays());
          setCellValue(row, this.getColumnIndex(XlsConstants.MAX_DAYS_TO_PROCESS), product.getMaxDays());
          setCellValue(row, this.getColumnIndex(XlsConstants.OVERVIEW), "");
          setCellValue(row, this.getColumnIndex(XlsConstants.DESCRIPTION), "");
          setCellValue(row, this.getColumnIndex(XlsConstants.RELATED_PRODUCTS), getRelatedProducts(product));
          setCellValue(row, this.getColumnIndex(XlsConstants.COLOR_PRODUCT), product.isProductHaveColorOptions() != null ? product.isProductHaveColorOptions() ? "Y" : "N" : null);
          setCellValue(row, this.getColumnIndex(XlsConstants.IS_SERVICE), product.isService() != null ? product.isService() ? "Y" : "N" : null);
          setCellValue(row, this.getColumnIndex(XlsConstants.IS_GOOGLE_AD_DISALLOWED), product.isGoogleAdDisallowed() != null ? product.isGoogleAdDisallowed() ? "Y" : "N" : null);
        }
        if (productVariant.getId().startsWith(product.getId() + "-")) {
          setCellValue(row, this.getColumnIndex(XlsConstants.VARIANT_ID), productVariant.getId());
          setCellValue(row, this.getColumnIndex(XlsConstants.VARIANT_SORTING), productVariant.getOrderRanking());
          setCellValue(row, this.getColumnIndex(XlsConstants.VARIANT_NAME), productVariant.getVariantName());
          setCellValue(row, this.getColumnIndex(XlsConstants.OPTIONS), getOptions(productVariant));
          setCellValue(row, this.getColumnIndex(XlsConstants.EXTRA_OPTIONS), getExtraOptions(productVariant));
          setCellValue(row, this.getColumnIndex(XlsConstants.MRP), productVariant.getMarkedPrice());
	        setCellValue(row, this.getColumnIndex(XlsConstants.COST), productVariant.getCostPrice());
          setCellValue(row, this.getColumnIndex(XlsConstants.POSTPAID_AMOUNT), productVariant.getPostpaidAmount());
          setCellValue(row, this.getColumnIndex(XlsConstants.HK_PRICE), productVariant.getHkPrice(null));

          //not applicable now as part of catalog.
          
          /*setCellValue(row, this.getColumnIndex(XslParser.TAX), productVariant.getTax().getName());*/
          setCellValue(row, this.getColumnIndex(XlsConstants.PERCENTAGE_DISCOUNT), productVariant.getDiscountPercent());
          setCellValue(row, this.getColumnIndex(XlsConstants.SHIPPING), productVariant.getShippingBasePrice());
          setCellValue(row, this.getColumnIndex(XlsConstants.AVAILABILITY), productVariant.isOutOfStock() ? "N" : "Y");
          setCellValue(row, this.getColumnIndex(XlsConstants.MANUFACTURER_CODE), "");
          setCellValue(row, this.getColumnIndex(XlsConstants.DELETED), productVariant.isDeleted() ? "Y" : "N");
          Long inventory = getAdminInventoryService().getNetInventory(productVariant);
          setCellValue(row, this.getColumnIndex(XlsConstants.INVENTORY), inventory);
          Long cutoffInventory = inventoryService.getAggregateCutoffInventory(productVariant);
          setCellValue(row, this.getColumnIndex(XlsConstants.CUTOFF_INVENTORY), cutoffInventory);
          setCellValue(row, this.getColumnIndex(XlsConstants.AFFILIATE_CATEGORY), productVariant.getAffiliateCategory() != null ? productVariant.getAffiliateCategory().getAffiliateCategoryName() : "");
          setCellValue(row, this.getColumnIndex(XlsConstants.COLOR_HEX), productVariant.getColorHex() == null ? "" : productVariant.getColorHex());
          setCellValue(row, this.getColumnIndex(XlsConstants.MAIN_IMAGE_ID), productVariant.getMainImageId() == null ? null : productVariant.getMainImageId());
          setCellValue(row, this.getColumnIndex(XlsConstants.LENGTH), productVariant.getLength() == null ? null : productVariant.getLength());
          setCellValue(row, this.getColumnIndex(XlsConstants.BREADTH), productVariant.getBreadth() == null ? null : productVariant.getBreadth());
          setCellValue(row, this.getColumnIndex(XlsConstants.HEIGHT), productVariant.getHeight() == null ? null : productVariant.getHeight());
          setCellValue(row, this.getColumnIndex(XlsConstants.WEIGHT), productVariant.getWeight() == null ? null : productVariant.getWeight());
          setCellValue(row, this.getColumnIndex(XlsConstants.UPC), productVariant.getUpc() == null ? null : productVariant.getUpc());
          setCellValue(row, this.getColumnIndex(XlsConstants.SERVICE_TYPE), productVariant.getServiceType() != null ? productVariant.getServiceType().getName() : null);
          setCellValue(row, this.getColumnIndex(XlsConstants.PAYMENT_TYPE), productVariant.getPaymentType() != null ? productVariant.getPaymentType().getName() : null);
          initialRowNo++;
        }
      }
    }

    Sheet sheet2 = wb.createSheet("Manufacturer");
    int manufacturerColumnNo = 10;
    row = sheet2.createRow(0);
    row.setHeightInPoints((short) 25);

    for (int columnNo = 0; columnNo < manufacturerColumnNo; columnNo++) {
      cell = row.createCell(columnNo);
      cell.setCellStyle(style);
    }
    setCellValue(row, 0, XlsConstants.MANUFACTURER_NAME);
    setCellValue(row, 1, XlsConstants.MANUFACTURER_WEBSITE);
    setCellValue(row, 2, XlsConstants.MANUFACTURER_DESCRIPTION);
    setCellValue(row, 3, XlsConstants.MANUFACTURER_EMAIL);
    setCellValue(row, 4, XlsConstants.MANUFACTURER_PAN_INDIA);

    int rowNo = 1;
    for (Manufacturer manufacturer : manufacturers) {
      row = sheet2.createRow(rowNo);
      for (int cellNo = 0; cellNo < manufacturerColumnNo; cellNo++) {
        cell = row.createCell(cellNo);
      }
      rowNo++;
    }
    rowNo = 1;
    for (Manufacturer manufacturer : manufacturers) {
      row = sheet2.getRow(rowNo);
      setCellValue(row, 0, manufacturer.getName());
      setCellValue(row, 1, manufacturer.getWebsite());
      setCellValue(row, 2, manufacturer.getDescription());
      setCellValue(row, 3, manufacturer.getEmail());
      setCellValue(row, 4, manufacturer.isAvailableAllOverIndia() == null ? "N" : (manufacturer.isAvailableAllOverIndia() ? "Y" : "N"));
      rowNo++;
    }

    wb.write(out);
    out.close();
    return file;
  }

  public File generateCouerierServiceInfoXsl(List<CourierServiceInfo> courierServiceInfoList, String xslFilePath) throws Exception {

    File file = new File(xslFilePath);
    file.getParentFile().mkdirs();
    FileOutputStream out = new FileOutputStream(file);
    Workbook wb = new HSSFWorkbook();

    CellStyle style = wb.createCellStyle();
    Font font = wb.createFont();
    font.setFontHeightInPoints((short) 12);
    font.setColor(Font.COLOR_NORMAL);
    font.setBoldweight(Font.BOLDWEIGHT_BOLD);
    style.setFont(font);
    Sheet sheet1 = wb.createSheet("CourierServiceInfo");
    Row row = sheet1.createRow(0);
    row.setHeightInPoints((short) 25);

    int totalColumnNo = 8;

    Cell cell;
    for (int columnNo = 0; columnNo < totalColumnNo; columnNo++) {
      cell = row.createCell(columnNo);
      cell.setCellStyle(style);
    }
    setCellValue(row, 0, PINCODE);
    setCellValue(row, 1, COURIER_ID);
    setCellValue(row, 2, COD_AVAILABLE);
    setCellValue(row, 3, IS_PREFERRED);
    setCellValue(row, 4, IS_PREFERRED_COD);
    setCellValue(row, 5, ROUTING_CODE);
    setCellValue(row, 6, IS_DELETED);


    int initialRowNo = 1;
    for (CourierServiceInfo courierServiceInfo : courierServiceInfoList) {

      row = sheet1.createRow(initialRowNo);
      for (int columnNo = 0; columnNo < totalColumnNo; columnNo++) {
        row.createCell(columnNo);
      }

      Pincode pincode = courierServiceInfo.getPincode();
      setCellValue(row, 0, pincode.getPincode());
      setCellValue(row, 1, courierServiceInfo.getCourier().getId());
      setCellValue(row, 2, courierServiceInfo.isCodAvailable() ? "Y" : "N");
      setCellValue(row, 3, courierServiceInfo.isPreferred() ? "Y" : "N");
      setCellValue(row, 4, courierServiceInfo.isPreferredCod() ? "Y" : "N");
      setCellValue(row, 5, courierServiceInfo.getRoutingCode());
      setCellValue(row, 6, courierServiceInfo.isDeleted() ? "Y" : "N");

      initialRowNo++;

    }

    Sheet sheet2 = wb.createSheet("Courier IDs");
    row = sheet2.createRow(0);
    row.setHeightInPoints((short) 25);

    int totalColumnNo2 = 2;

    for (int columnNo = 0; columnNo < totalColumnNo; columnNo++) {
      cell = row.createCell(columnNo);
      cell.setCellStyle(style);
    }
    setCellValue(row, 0, "Courier");
    setCellValue(row, 1, "ID");

    int initialRowNo2 = 1;
    List<Courier> courierList = courierDao.getAllCouriers();
    for (Courier courier : courierList) {
      row = sheet2.createRow(initialRowNo2);
      for (int columnNo = 0; columnNo < totalColumnNo2; columnNo++) {
        row.createCell(columnNo);
      }

      setCellValue(row, 0, courier.getName());
      setCellValue(row, 1, courier.getId());

      initialRowNo2++;
    }

    wb.write(out);
    out.close();
    return file;
  }

  public File generateGRNXsl(GoodsReceivedNote goodsReceivedNote, String xslFilePath) throws Exception {
    AdminInventoryService adminInventoryService = ServiceLocatorFactory.getService(AdminInventoryService.class);
    File file = new File(xslFilePath);
    file.getParentFile().mkdirs();
    FileOutputStream out = new FileOutputStream(file);
    Workbook wb = new HSSFWorkbook();

    CellStyle style = wb.createCellStyle();
    Font font = wb.createFont();
    font.setFontHeightInPoints((short) 12);
    font.setColor(Font.COLOR_NORMAL);
    font.setBoldweight(Font.BOLDWEIGHT_BOLD);
    style.setFont(font);
    Sheet sheet1 = wb.createSheet("GRN-" + goodsReceivedNote.getId());
    Row row = sheet1.createRow(0);
    row.setHeightInPoints((short) 25);

    int totalColumnNo = 10;

    Cell cell;
    for (int columnNo = 0; columnNo < totalColumnNo; columnNo++) {
      cell = row.createCell(columnNo);
      cell.setCellStyle(style);
    }
    setCellValue(row, 0, XlsConstants.GRN_LINE_ITEM_ID);
    setCellValue(row, 1, XlsConstants.VARIANT_ID);
    setCellValue(row, 2, XlsConstants.QTY);
    setCellValue(row, 3, XlsConstants.COST);
    setCellValue(row, 4, XlsConstants.MRP);
    setCellValue(row, 5, XlsConstants.BATCH_NUMBER);
    setCellValue(row, 6, XlsConstants.MFG_DATE);
    setCellValue(row, 7, XlsConstants.EXP_DATE);
    setCellValue(row, 8, XlsConstants.CHECKIN_QTY);


    int initialRowNo = 1;
    for (GrnLineItem grnLineItem : goodsReceivedNote.getGrnLineItems()) {

      row = sheet1.createRow(initialRowNo);
      for (int columnNo = 0; columnNo < totalColumnNo; columnNo++) {
        row.createCell(columnNo);
      }

      setCellValue(row, 0, grnLineItem.getId());
      setCellValue(row, 1, grnLineItem.getProductVariant().getId());
      setCellValue(row, 2, grnLineItem.getQty());
      setCellValue(row, 3, grnLineItem.getCostPrice());
      setCellValue(row, 4, grnLineItem.getMrp());
      setCellValue(row, 5, "");
      setCellValue(row, 6, "");
      setCellValue(row, 7, "");
      setCellValue(row, 8, adminInventoryService.countOfCheckedInUnitsForGrnLineItem(grnLineItem));

      initialRowNo++;
    }

    wb.write(out);
    out.close();
    return file;
  }

  public File generatePincodeXsl(List<Pincode> pincodeList, String xslFilePath) throws Exception {

     File file = new File(xslFilePath);
     file.getParentFile().mkdirs();
     FileOutputStream out = new FileOutputStream(file);
     Workbook wb = new HSSFWorkbook();

     CellStyle style = wb.createCellStyle();
     Font font = wb.createFont();
     font.setFontHeightInPoints((short) 12);
     font.setColor(Font.COLOR_NORMAL);
     font.setBoldweight(Font.BOLDWEIGHT_BOLD);
     style.setFont(font);
     Sheet sheet1 = wb.createSheet("PincodeInfo");
     Row row = sheet1.createRow(0);
     row.setHeightInPoints((short) 25);

     int totalColumnNo = 7;

     Cell cell;
     for (int columnNo = 0; columnNo < totalColumnNo; columnNo++) {
       cell = row.createCell(columnNo);
       cell.setCellStyle(style);
     }
     setCellValue(row, 0, ID);
     setCellValue(row, 1, PINCODE);
     setCellValue(row, 2, CITY);
     setCellValue(row, 3, STATE);
     setCellValue(row, 4, LOCALITY);
     setCellValue(row, 5, DEFAULT_COURIER_ID);

     int initialRowNo = 1;
     for (Pincode pincode : pincodeList) {

       row = sheet1.createRow(initialRowNo);
       for (int columnNo = 0; columnNo < totalColumnNo; columnNo++) {
         row.createCell(columnNo);
       }

       setCellValue(row, 0, pincode.getId());
       setCellValue(row, 1, pincode.getPincode());
       setCellValue(row, 2, pincode.getCity());
       setCellValue(row, 3, pincode.getState());
       setCellValue(row, 4, pincode.getLocality());
       setCellValue(row, 5, pincode.getDefaultCourier()!=null?pincode.getDefaultCourier().getId():null);

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

  private void setHeaderCellValue(Row row, int column, String cellValue) {
    if (cellValue == null) cellValue = "";
    Cell cell = row.getCell(column);
    cell.setCellValue(cellValue);

    this.setRowMap(cellValue, column);
  }


  public String getCategories(Product product) {
    String result = "";
    for (Category category : product.getCategories()) {
      result = result + category.getDisplayName() + "|";
    }
    if (!result.isEmpty())
      result = result.charAt(result.length() - 1) == '|' ? result.substring(0, result.length() - 1) : result;
    return result;
  }

  private String getRelatedProducts(Product product) {
    String result = "";
    for (Product relatedProduct : product.getRelatedProducts()) {
      result = result + relatedProduct.getId() + "|";
    }
    if (!result.isEmpty())
      result = result.charAt(result.length() - 1) == '|' ? result.substring(0, result.length() - 1) : result;
    return result;
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

  private String getExtraOptions(ProductVariant productVariant) {
    String result = "";
    for (ProductExtraOption productExtraOption : productVariant.getProductExtraOptions()) {
      result = result + productExtraOption.getName() + ":" + productExtraOption.getValue() + "|";
    }
    if (!result.isEmpty())
      result = result.charAt(result.length() - 1) == '|' ? result.substring(0, result.length() - 1) : result;
    return result;
  }

  private void setRowMap(String columnHeader, int columnIndex) {
    headerMap.put(columnHeader, columnIndex);
  }

  private Integer getColumnIndex(String columnHeader) {
    return headerMap.get(columnHeader);
  }

public AdminInventoryService getAdminInventoryService() {
    return adminInventoryService;
}

public void setAdminInventoryService(AdminInventoryService adminInventoryService) {
    this.adminInventoryService = adminInventoryService;
}

  
}
