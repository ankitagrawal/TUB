package com.hk.admin.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.admin.pact.dao.marketing.AmazonFeedDao;
import com.hk.domain.amazon.AmazonFeed;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.exception.HealthKartCatalogUploadException;
import com.hk.pact.dao.catalog.product.ProductVariantDao;

@Component
public class AmazonXslParser {

    private static Logger logger = LoggerFactory.getLogger(AmazonXslParser.class);

    private Set<ProductVariant> productVariantList = null;
    private Set<AmazonFeed> amazonFeedSet = null;

    public static final String SKU = "SKU";
    public static final String RECOMMENDED_BROWSE_NODE = "Recommended Browse Node";
    public static final String DESCRIPTION = "Description";
    public static final String TITLE = "Title";

    public static final String ITEM_PACKAGE_QTY = "Item package quantity";
    public static final String WARRANTY = "Warranty";
    public static final String GENDER = "Gender";
    public static final String BATTERIES_INCLUDED = "Batteries Included";

    @Autowired
    private AmazonFeedDao amazonFeedDao;
    @Autowired
    private ProductVariantDao productVariantDao;
    
    
    private AmazonFeed amazonProduct;

    @SuppressWarnings("unchecked")
    public Set<AmazonFeed> readAmazonCatalog(File objInFile) throws Exception {

      logger.info("parsing products from amazon catalog : " + objInFile.getAbsolutePath());

      POIFSFileSystem objInFileSys = new POIFSFileSystem(new FileInputStream(objInFile));

      HSSFWorkbook workbook = new HSSFWorkbook(objInFileSys);

      // Assuming there is only one sheet, the first one only will be picked
      HSSFSheet productSheet = workbook.getSheet("Amazon");
      Iterator<Row> objRowIt = productSheet.rowIterator();
      @SuppressWarnings("unused")
    Iterator objCellIterator = null;

      // Declaring data elements
     // Product product = null;
      ProductVariant productVariant = null;
      List<ProductVariant> productVariants = null;
      Map<Integer, String> headerMap;
      Map<Integer, String> rowMap;
      productVariantList = new HashSet<ProductVariant>();
      amazonFeedSet = new HashSet<AmazonFeed>();

      int rowCount = 2;
      objRowIt.next();
//      objRowIt.next();
      try {
        headerMap = getRowMap(objRowIt);

        // Iterating on the available rows
        while (objRowIt.hasNext()) {
          rowMap = getRowMap(objRowIt);

          // checking if variantId is present. if no variant id is present then tis is a blank row, continue
          String variantId = getCellValue(SKU, rowMap, headerMap);
          if (StringUtils.isBlank(variantId)) {
            continue;
          } else {
            // this is a variant
            productVariants = new ArrayList<ProductVariant>();

            productVariant = getProductVariantDao().getVariantById(variantId);
            if (productVariant == null) {
              throw new HealthKartCatalogUploadException("Invalid product variant id, exception @ Row", rowCount);
            }
            if (productVariantList.contains(productVariant)) {
              throw new HealthKartCatalogUploadException("Duplicate product variant id in excel, Exception @ Row:", rowCount);
            }
            String description = getCellValue(DESCRIPTION, rowMap, headerMap);

            String amazonBrowseNode = getCellValue(RECOMMENDED_BROWSE_NODE, rowMap, headerMap);
            if (StringUtils.isBlank(amazonBrowseNode)) {
              throw new HealthKartCatalogUploadException("Amazon browse node not to be null or empty, Exception @ Row:", rowCount);
            }
            String productTitle = getCellValue(TITLE, rowMap, headerMap);

            String itemPackageQty = getCellValue(ITEM_PACKAGE_QTY, rowMap, headerMap);

            Long itemPackageQuantity = StringUtils.isEmpty(itemPackageQty) ? 0 : Long.parseLong(itemPackageQty);

            String warranty = getCellValue(WARRANTY, rowMap, headerMap);
            String gender = getCellValue(GENDER, rowMap, headerMap);
            String batteriesIncluded = getCellValue(BATTERIES_INCLUDED, rowMap, headerMap);

            amazonProduct = getAmazonFeedDao().findByPV(productVariant);
            if (amazonProduct == null)
              amazonProduct = new AmazonFeed();

            amazonProduct.setProductVariant(productVariant);
            amazonProduct.setDescription(description);
            amazonProduct.setTitle(productTitle);
            amazonProduct.setAmazonBrowseNode(amazonBrowseNode);
            amazonProduct.setItemPackageQuantity(itemPackageQuantity);
            amazonProduct.setWarranty(warranty);
            amazonProduct.setGender(gender);
            amazonProduct.setBatteriesIncluded(batteriesIncluded);

            amazonFeedSet.add(amazonProduct);
            productVariantList.add(productVariant);
          }

          productVariants.add(productVariant);

          logger.debug("read row " + rowCount);
          rowCount++;
        }
      } catch (Exception e) {
        logger.error("Exception @ Row:" + rowCount + " " + e.getMessage(), e.getStackTrace());
        throw new Exception("Exception @ Row:" + rowCount + " " + e.getMessage(), e);
      }
      return amazonFeedSet;
    }

    private String getCellValue(String header, Map<Integer, String> rowMap, Map<Integer, String> headerMap) {
      Integer columnIndex = getColumnIndex(header, headerMap);
      if (columnIndex == null) return null;
      String cellVal = rowMap.get(columnIndex);
      return cellVal == null ? "" : cellVal.trim();
    }

    private Integer getColumnIndex(String header, Map<Integer, String> headerMap) {
      Integer columnIndex = null;
      for (Integer key : headerMap.keySet()) {
        if (headerMap.get(key).equals(header)) columnIndex = key;
      }
      return columnIndex;
    }

    /*private Date getDate(String value) {
      Date date = null;
      try {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM");
        date = (Date) formatter.parse(value);
      } catch (Exception e) {

      }
      return date;
    }


    private Double getDouble(String value) {
      Double valueInDouble = null;
      try {
        valueInDouble = Double.parseDouble(value);
      } catch (Exception e) {

      }
      return valueInDouble;
    }

    private Long getLong(String value) {
      Long valueInLong = null;
      try {
        valueInLong = Long.parseLong(value.replace(".0", ""));
      } catch (Exception e) {

      }
      return valueInLong;
    }*/

    @SuppressWarnings("unchecked")
    private Map<Integer, String> getRowMap(Iterator<Row> objRowIt) {
      // Header are read and related columns are taken care of
      // accordignly.

      Map<Integer, String> headerMap = new HashMap<Integer, String>();

      HSSFRow headers = (HSSFRow) objRowIt.next();
      Iterator objCellIterator = headers.cellIterator();
      while (objCellIterator.hasNext()) {
        HSSFCell headerCell = (HSSFCell) objCellIterator.next();
        int headerColIndex = 0;
        headerColIndex = headerCell.getColumnIndex();
        Object cellValue = null;
        try {
          headerCell.setCellType(Cell.CELL_TYPE_STRING);
          cellValue = headerCell.getStringCellValue();
          headerMap.put(headerColIndex, cellValue.toString());
        } catch (Exception e) {
          logger.debug("error trying to read column " + headerColIndex + " as String on Row " + headers.getRowNum() + " : Cell toString = " + headerCell.toString());
          logger.debug("Now trying to read as numeric");
          try {
            cellValue = headerCell.getNumericCellValue();
            headerMap.put(headerColIndex, cellValue.toString());
          } catch (Exception e1) {
            logger.debug("error reading cell value as numeric - " + headerCell.toString());
          }
        }
      }

      return headerMap;
    }

    public AmazonFeedDao getAmazonFeedDao() {
        return amazonFeedDao;
    }

    public void setAmazonFeedDao(AmazonFeedDao amazonFeedDao) {
        this.amazonFeedDao = amazonFeedDao;
    }


    public ProductVariantDao getProductVariantDao() {
        return productVariantDao;
    }

    public void setProductVariantDao(ProductVariantDao productVariantDao) {
        this.productVariantDao = productVariantDao;
    }

    
}
