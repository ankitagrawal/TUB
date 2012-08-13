package com.hk.admin.util;

import com.hk.domain.inventory.rv.RvLineItem;
import com.hk.domain.inventory.rv.ReconciliationVoucher;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.sku.Sku;
import com.hk.util.io.ExcelSheetParser;
import com.hk.util.io.HKRow;
import com.hk.constants.XslConstants;
import com.hk.constants.inventory.EnumReconciliationType;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.inventory.SkuService;


import java.util.List;
import java.util.Iterator;
import java.util.Date;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang.StringUtils;


/**
 * Created by IntelliJ IDEA.
 * User: Neha
 * Date: Aug 3, 2012
 * Time: 1:59:17 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class ReconciliationVoucherParser {

    private static Logger logger                 = LoggerFactory.getLogger(ReconciliationVoucherParser.class);
    @Autowired
    private ProductVariantService productVariantService;

    @Autowired
    SkuService skuService;

    private List<RvLineItem>            rvLineItems            = new ArrayList<RvLineItem>(); //confirm
    private ReconciliationVoucher reconciliationVoucher;

    public List<RvLineItem> readAndCreateRVLineItems(String excelFilePath, String sheetName) throws Exception {
        ExcelSheetParser parser = new ExcelSheetParser(excelFilePath, sheetName);
        Iterator<HKRow> rowIterator = parser.parse();
        int rowCount = 1;
        try {
            while (rowIterator.hasNext()) {
                HKRow row = rowIterator.next();
                String variantId = row.getColumnValue(XslConstants.VARIANT_ID);
                Double mrp = XslUtil.getDouble( row.getColumnValue(XslConstants.MRP) );
                Double cost = XslUtil.getDouble( row.getColumnValue(XslConstants.COST) );
                Long qty =  XslUtil.getLong( row.getColumnValue(XslConstants.QTY) );
                String batchNumber = row.getColumnValue(XslConstants.BATCH_NUMBER);
                String strExpiryDate = row.getColumnValue(XslConstants.EXP_DATE);
                Date expiryDate = new Date();
                if(strExpiryDate != null && !StringUtils.isBlank(strExpiryDate)) {
                    expiryDate = XslUtil.getDate(strExpiryDate);
                    if(expiryDate == null) {
                        throw new Exception("Incorrect format for expiry date ");
                    }
                }
                String strMfgDate = row.getColumnValue(XslConstants.MFG_DATE);
                Date mfgDate = new Date();
                if(strMfgDate != null && !StringUtils.isBlank(strMfgDate)) {
                    mfgDate = XslUtil.getDate(strMfgDate);
                    /*if(mfgDate == null) {
                        throw new Exception("Incorrect format for mfg date ");
                    }*/
                }

                ProductVariant productVariant = getProductVariantService().getVariantById(variantId);

                if (cost == null || cost == 0D) {
                    if (productVariant != null) {
                        cost = productVariant.getCostPrice();
                    }
                }

                if(productVariant == null) {
                    throw new Exception("Incorrect product variant");
                }

                if(qty == null || qty <= 0) {
                    throw new Exception("Qty should be greater than zero");
                }

                if(mrp == null || mrp <= 0) {
                    throw new Exception("MRP should be greater than zero");
                }
                if(cost == null || cost <= 0) {
                    throw new Exception("COST should be greater than zero");
                }
                if(batchNumber == null || StringUtils.isBlank(batchNumber)) {
                    throw new Exception("Invalid batch number");
                }

                Sku sku = skuService.getSKU(productVariant, getReconciliationVoucher().getWarehouse());

                if(productVariant != null) {
                    RvLineItem rvLineItem = new RvLineItem();
                    rvLineItem.setProductVariant(productVariant);
                    rvLineItem.setReconciliationType(EnumReconciliationType.Add.asReconciliationType());
                    rvLineItem.setBatchNumber(batchNumber);
                    rvLineItem.setQty(qty);
                    rvLineItem.setMrp(mrp);
                    rvLineItem.setCostPrice(cost);
                    rvLineItem.setMfgDate(mfgDate);
                    rvLineItem.setExpiryDate(expiryDate);
                    rvLineItem.setSku(sku);

                    rvLineItems.add(rvLineItem);
                    rowCount++;
                }
            }
        } catch (Exception e) {
            logger.error("Exception @ Row:" + (rowCount+1) + e.getMessage());
            throw new Exception("Exception @ Row:" + (rowCount+1)+ ": " +e.getMessage(), e);
        }
        return rvLineItems;
    }

    public ProductVariantService getProductVariantService() {
        return productVariantService;
    }

    public ReconciliationVoucher getReconciliationVoucher() {
        return reconciliationVoucher;
    }

    public void setReconciliationVoucher(ReconciliationVoucher reconciliationVoucher) {
        this.reconciliationVoucher = reconciliationVoucher;
    }
}
