package com.hk.admin.util;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.Tax;
import com.hk.domain.sku.Sku;
import com.hk.domain.warehouse.Warehouse;
import com.hk.exception.HealthKartCatalogUploadException;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.core.TaxService;
import com.hk.pact.service.core.WarehouseService;
import com.hk.pact.service.inventory.SkuService;
import org.apache.commons.io.IOUtils;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA. User: Rajni Date: Apr 3, 2012 Time: 12:15:53 PM To change this template use File | Settings |
 * File Templates.
 */
@Component
public class SkuXslParser {

    private static Logger         logger              = LoggerFactory.getLogger(XslParser.class);
    private Set<Sku>              skuSet              = null;
    private ProductVariant        productVariantObj   = null;
    private Sku                   skuProduct          = null;

    // constants for retrieving values from excel.
    public static final String    PRODUCT_VARIANT_ID  = "PRODUCT_VARIANT_ID";
    public static final String    WAREHOUSE_ID        = "WAREHOUSE_ID";
    public static final String    COST_PRICE          = "COST_PRICE";
    public static final String    CUT_OFF_INVENTORY   = "CUT_OFF_INVENTORY";
    public static final String    FORECASTED_QUANTITY = "FORECASTED_QUANTITY";
    public static final String    TAX_ID              = "TAX_ID";

    @Autowired
    private WarehouseService      warehouseService;

    @Autowired
    private SkuService            skuService;

    @Autowired
    private TaxService            taxService;
    @Autowired
    private ProductVariantService productVariantService;

    public Set<Sku> readSKUCatalog(File objInFile) throws Exception {

        logger.info("parsing products from SKU catalog : " + objInFile.getAbsolutePath());

        InputStream poiInputStream = new FileInputStream(objInFile);
        POIFSFileSystem objInFileSys = new POIFSFileSystem(poiInputStream);

        // POIFSFileSystem objInFileSys = new POIFSFileSystem(new FileInputStream(objInFile));

        HSSFWorkbook workbook = new HSSFWorkbook(objInFileSys);

        // Assuming there is only one sheet, the first one only will be picked
        // HSSFSheet productSheet = workbook.getSheet("Sku");
        HSSFSheet productSheet = workbook.getSheetAt(0);
        Iterator<Row> objRowIt = productSheet.rowIterator();
        /*Iterator objCellIterator = null;*/

        // Declaring data elements
        List<String> pVariantWarehouseList = new ArrayList<String>();
        Map<Integer, String> headerMap;
        Map<Integer, String> rowMap;
        skuSet = new HashSet<Sku>();

        int rowCount = 1;

        try {
            headerMap = getRowMap(objRowIt);

            // Iterating on the available rows
            while (objRowIt.hasNext()) {
                rowMap = getRowMap(objRowIt);

                Tax taxObj = null;
                Warehouse warehouseObj = null;
                String variantId = null;
                long warehouseId = 0l;
                String taxName = null;
                /*double costPrice = 0.0;*/
                long cutOffInventory = 0l;
                long forecastedQty = 0l;
                String variant_warehouse_id = null;

                try {
                    variantId = getCellValue(PRODUCT_VARIANT_ID, rowMap, headerMap);
                    // warehouseId = getLong(getCellValue(WAREHOUSE_ID, rowMap, headerMap));
                    warehouseId = getWarehouseService().findByIdentifier(getCellValue(WAREHOUSE_ID, rowMap, headerMap)).getId();
                    taxName = getCellValue(TAX_ID, rowMap, headerMap);
                    // costPrice = getDouble(getCellValue(COST_PRICE, rowMap, headerMap));
                    cutOffInventory = getLong(getCellValue(CUT_OFF_INVENTORY, rowMap, headerMap));
                    forecastedQty = getLong(getCellValue(FORECASTED_QUANTITY, rowMap, headerMap));
                    variant_warehouse_id = variantId + "-" + String.valueOf(warehouseId);

                } catch (NullPointerException ne) {
                    logger.error("Error while uploading sku excel", ne);
                    throw new HealthKartCatalogUploadException("Blank Column in excel.", rowCount);
                    

                }

                // checking if any of the six values is blank then continue.
                if (!checkForBlankColumn(variantId, String.valueOf(warehouseId), taxName, String.valueOf(cutOffInventory), String.valueOf(forecastedQty))) {

                    continue;
                } else {
                    // this is a variant
                    // productVariants = new ArrayList<ProductVariant>();

                    productVariantObj = getProductVariantService().getVariantById(variantId);
                    if (productVariantObj == null) {
                        throw new HealthKartCatalogUploadException("Invalid product variant id in excel.", rowCount);
                    }

                    // checking if warehouseObj is valid or not.
                    warehouseObj = getWarehouseService().getWarehouseById(warehouseId);
                    if (warehouseObj == null) {
                        throw new HealthKartCatalogUploadException("Invalid warehouse id in excel.", rowCount);

                    }

                    // Checking for duplicate prodcutVariant-warehouse_id combination.
                    if (pVariantWarehouseList.size() > 0 && pVariantWarehouseList != null) {
                        if (pVariantWarehouseList.contains(variant_warehouse_id)) {
                            throw new HealthKartCatalogUploadException("Duplicate product variant and Warehouse id in excel.", rowCount);
                        }
                    }

                    // checking if taxObj is valid or not.
                    taxObj = getTaxService().findByName(taxName);
                    if (taxObj == null) {
                        throw new HealthKartCatalogUploadException("Invalid tax id in excel.", rowCount);
                    }

		                skuProduct = getSkuService().findSKU(productVariantObj, warehouseObj);
		                if (skuProduct == null) {
			                skuProduct = new Sku();
			                skuProduct.setProductVariant(productVariantObj);
			                skuProduct.setWarehouse(warehouseObj);
			                skuProduct.setTax(taxObj);
			                skuProduct.setCutOffInventory(cutOffInventory);
			                skuProduct.setForecastedQuantity(forecastedQty);
			                skuProduct.setCreateDate(new Date());
			                skuProduct.setUpdateDate(new Date());
			                skuSet.add(skuProduct);
		                } else {
                        throw new HealthKartCatalogUploadException("SKU already exists in db.", rowCount);
                    }
                    pVariantWarehouseList.add(variant_warehouse_id);
                }
                logger.debug("read row " + rowCount);
                rowCount++;
            }
        } catch (HealthKartCatalogUploadException e) {
            logger.error("Exception @ Row:" + rowCount + " " + e.getMessage());
            throw new Exception("Exception @ Row:" + rowCount + " " + e.getMessage());
        } finally {
            if (poiInputStream != null) {
                IOUtils.closeQuietly(poiInputStream);
            }
        }
        return skuSet;
    }

    private boolean checkForBlankColumn(String productVariant, String warehouseId, String taxName, String cutOfInventory, String forecastedQty) {
        if (StringUtils.isBlank(productVariant) || StringUtils.isBlank(warehouseId) || StringUtils.isBlank(taxName) || StringUtils.isBlank(cutOfInventory)
                || StringUtils.isBlank(forecastedQty)) {
            return false;
        }
        return true;
    }

    private String getCellValue(String header, Map<Integer, String> rowMap, Map<Integer, String> headerMap) {
        Integer columnIndex = getColumnIndex(header, headerMap);
        if (columnIndex == null)
            return null;
        String cellVal = rowMap.get(columnIndex);
        return cellVal == null ? "" : cellVal.trim();
    }

    private Integer getColumnIndex(String header, Map<Integer, String> headerMap) {
        Integer columnIndex = null;
        for (Integer key : headerMap.keySet()) {
            if (headerMap.get(key).equals(header))
                columnIndex = key;
        }
        return columnIndex;
    }

    @SuppressWarnings("unused")
    private Date getDate(String value) {
        Date date = null;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM");
            date = (Date) formatter.parse(value);
        } catch (Exception e) {

        }
        return date;
    }

    @SuppressWarnings("unused")
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
    }

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

    public WarehouseService getWarehouseService() {
        return warehouseService;
    }

    public void setWarehouseService(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    public SkuService getSkuService() {
        return skuService;
    }

    public void setSkuService(SkuService skuService) {
        this.skuService = skuService;
    }

    public TaxService getTaxService() {
        return taxService;
    }

    public void setTaxService(TaxService taxService) {
        this.taxService = taxService;
    }

    public ProductVariantService getProductVariantService() {
        return productVariantService;
    }

    public void setProductVariantService(ProductVariantService productVariantService) {
        this.productVariantService = productVariantService;
    }

}