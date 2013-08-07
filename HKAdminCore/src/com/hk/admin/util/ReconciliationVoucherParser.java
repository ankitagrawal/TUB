package com.hk.admin.util;

import com.hk.domain.inventory.rv.ReconciliationType;
import com.hk.domain.inventory.rv.RvLineItem;
import com.hk.domain.inventory.rv.ReconciliationVoucher;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.sku.SkuItemOwner;
import com.hk.domain.sku.SkuItemStatus;
import com.hk.domain.warehouse.Warehouse;
import com.hk.exception.NoSkuException;
import com.hk.pact.service.inventory.SkuGroupService;
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

    private static Logger logger = LoggerFactory.getLogger(ReconciliationVoucherParser.class);
    @Autowired
    private ProductVariantService productVariantService;

    @Autowired
    SkuService skuService;
    @Autowired
    SkuGroupService skuGroupService;

    StringBuilder message = new StringBuilder("");


    //private ReconciliationVoucher reconciliationVoucher;

    public List<RvLineItem> readAndCreateAddRVLineItems(String excelFilePath, String sheetName, ReconciliationVoucher reconciliationVoucher) throws Exception {
        List<RvLineItem> rvLineItems = new ArrayList<RvLineItem>();
        ExcelSheetParser parser = new ExcelSheetParser(excelFilePath, sheetName);
        Iterator<HKRow> rowIterator = parser.parse();
        int rowCount = 1;
        try {
            while (rowIterator.hasNext()) {
                HKRow row = rowIterator.next();
                String variantId = row.getColumnValue(XslConstants.VARIANT_ID);
                Double mrp = XslUtil.getDouble(row.getColumnValue(XslConstants.MRP));
                Double cost = XslUtil.getDouble(row.getColumnValue(XslConstants.COST));
                Long qty = XslUtil.getLong(row.getColumnValue(XslConstants.QTY));
                String batchNumber = row.getColumnValue(XslConstants.BATCH_NUMBER);
                String strExpiryDate = row.getColumnValue(XslConstants.EXP_DATE);
                String reconReason = row.getColumnValue(XslConstants.RECON_REASON);
                if (reconReason == null || StringUtils.isBlank(reconReason)) {
                    throw new Exception("Enter Recon Reason @ Row " + rowCount);
                }
                ReconciliationType reconciliationType = null;
                if (reconReason != null && !StringUtils.isBlank(reconReason)) {
                    reconciliationType = EnumReconciliationType.getAddReconciliationTypeByName(reconReason);
                    if (reconciliationType == null) {
                        throw new Exception("Invalid Recon Reason  @ Row:" + rowCount);
                    }
                }

                Date expiryDate = null;
                if (strExpiryDate != null && !StringUtils.isBlank(strExpiryDate)) {
                    expiryDate = XslUtil.getDate(strExpiryDate);
                    if (expiryDate == null) {
                        throw new Exception("Incorrect format for expiry date  @ Row " + rowCount);
                    }
                }
                String strMfgDate = row.getColumnValue(XslConstants.MFG_DATE);
                Date mfgDate = null;
                if (strMfgDate != null && !StringUtils.isBlank(strMfgDate)) {
                    mfgDate = XslUtil.getDate(strMfgDate);
                    if (mfgDate == null) {
                        throw new Exception("Incorrect format for mfg date  @ Row " + rowCount);
                    }
                }

                ProductVariant productVariant = getProductVariantService().getVariantById(variantId);

                if (cost == null || cost == 0D) {
                    if (productVariant != null) {
                        cost = productVariant.getCostPrice();
                    }
                }

                if (productVariant == null) {
                    throw new Exception("Incorrect product variant @ Row " + rowCount);
                }

                if (qty == null || qty <= 0) {
                    throw new Exception("Qty should be greater than zero @ Row " + rowCount);
                }

                if (mrp == null || mrp <= 0) {
                    throw new Exception("MRP should be greater than zero");
                }
                if (cost == null || cost <= 0) {
                    throw new Exception("COST should be greater than zero");
                }
                if (batchNumber == null || StringUtils.isBlank(batchNumber)) {
                    throw new Exception("Invalid batch number");
                }

                Sku sku = skuService.getSKU(productVariant, reconciliationVoucher.getWarehouse());

                if (productVariant != null) {
                    RvLineItem rvLineItem = new RvLineItem();
                    rvLineItem.setProductVariant(productVariant);
                    rvLineItem.setReconciliationType(reconciliationType);
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
            logger.error("Exception @ Row:" + (rowCount + 1) + e.getMessage());
            throw new Exception("Exception @ Row:" + (rowCount + 1) + ": " + e.getMessage(), e);
        }
        return rvLineItems;
    }

    public List<RvLineItem> readAndCreateSubtractRVLineItems(String excelFilePath, String sheetName, ReconciliationVoucher reconciliationVoucher) throws Exception {
        List<RvLineItem> rvLineItems = new ArrayList<RvLineItem>();
        ExcelSheetParser parser = new ExcelSheetParser(excelFilePath, sheetName);
        Warehouse warehouse = reconciliationVoucher.getWarehouse();
        Iterator<HKRow> rowIterator = parser.parse();
        int rowCount = 1;
        try {
            while (rowIterator.hasNext()) {
                HKRow row = rowIterator.next();
                String groupBarcode = row.getColumnValue(XslConstants.GROUP_BARCODE);
                String itemBarcode = row.getColumnValue(XslConstants.ITEM_BARCODE);
                String variantId = row.getColumnValue(XslConstants.VARIANT_ID);
                String reconReason = row.getColumnValue(XslConstants.RECON_REASON);
                String batchNumber = row.getColumnValue(XslConstants.BATCH_NUMBER);
                String strExpiryDate = row.getColumnValue(XslConstants.EXP_DATE);
                String qtyString = row.getColumnValue(XslConstants.QTY);
                String systemQtyString = row.getColumnValue(XslConstants.SYSTEM_QTY);
                String mrpString = row.getColumnValue(XslConstants.MRP);
                String costString = row.getColumnValue(XslConstants.COST);

                Double mrp = null;
                Double cost = null;
                Long qtyForDeletion = null;
                Long systemQty = null;


                if (mrpString != null && !StringUtils.isBlank(mrpString)) {
                    mrp = XslUtil.getDouble(mrpString);
                }

                if (costString != null && !StringUtils.isBlank(costString)) {
                    cost = XslUtil.getDouble(costString);
                }
                if (qtyString != null && !StringUtils.isBlank(qtyString)) {
                    qtyForDeletion = XslUtil.getLong(qtyString);
                }
                if (systemQtyString != null && !StringUtils.isBlank(systemQtyString)) {
                    systemQty = XslUtil.getLong(systemQtyString);
                }


                if (qtyForDeletion == null || qtyForDeletion <= 0) {
                    throw new Exception("Qty should be greater than zero @ Row :" + rowCount);
                }

                if (systemQty == null || systemQty <= 0) {
                    throw new Exception("Qty should be greater than zero @ Row :" + rowCount);
                }

                if (groupBarcode == null && itemBarcode == null && variantId == null && reconReason == null && mrp == null && cost == null && qtyForDeletion == null &&
                        batchNumber == null && strExpiryDate == null) {
                    return rvLineItems;
                }


                if (reconReason == null) {
                    throw new Exception("Blank Recon Reason  @ Row:" + rowCount);
                }
                ReconciliationType reconciliationType = EnumReconciliationType.getSubtractReconciliationTypeByName(reconReason);
                if (reconciliationType == null) {
                    throw new Exception("Invalid Recon Reason  @ Row:" + rowCount);
                }

                SkuGroup skuGroup = null;
                SkuItem skuItem = null;
                List<SkuItemStatus> skuItemStatusList = new ArrayList<SkuItemStatus>();
                List<SkuItemOwner> skuItemOwners = new ArrayList<SkuItemOwner>();
                if (groupBarcode != null && !StringUtils.isBlank(groupBarcode)) {
                    List<SkuGroup> skuGroupList = skuGroupService.getSkuGroup(groupBarcode.trim(), warehouse.getId());
                    if (skuGroupList == null || skuGroupList.isEmpty()) {
                        throw new Exception("Invalid Group Barcode  @ Row:" + rowCount);
                    }
                    skuGroup = getSkuGroupWithSystemQty(skuGroupList, systemQty.intValue());
                    if (skuGroup == null) {
                        throw new Exception("System Qty for Barcode :" + groupBarcode + " is less than Qty in Excel" + rowCount);
                    }

                } else if (itemBarcode != null && !StringUtils.isBlank(itemBarcode)) {
                    skuItem = skuGroupService.getSkuItemByBarcode(itemBarcode.trim(), warehouse.getId(), skuItemStatusList, skuItemOwners);
                    if (skuItem == null) {
                        throw new Exception("Invalid Item Barcode  @ Row:" + rowCount);
                    }
                } else {
                    throw new Exception("Invalid Barcode  @ Row:" + rowCount);
                }
                Date expiryDate = null;
                if (strExpiryDate != null && !StringUtils.isBlank(strExpiryDate)) {
                    expiryDate = XslUtil.getDate(strExpiryDate);
                    if (expiryDate == null) {
                        throw new Exception("Incorrect format for expiry date @ Row: " + rowCount);
                    }
                }
                String strMfgDate = row.getColumnValue(XslConstants.MFG_DATE);
                Date mfgDate = null;
                if (strMfgDate != null && !StringUtils.isBlank(strMfgDate)) {
                    mfgDate = XslUtil.getDate(strMfgDate);
                    if (mfgDate == null) {
                        throw new Exception("Incorrect format for mfg date @ Row: " + rowCount);
                    }
                }

                ProductVariant productVariant = getProductVariantService().getVariantById(variantId);

                if (cost == null || cost == 0D) {
                    if (productVariant != null) {
                        cost = productVariant.getCostPrice();
                    }
                }

                if (productVariant == null) {
                    throw new Exception("Incorrect product variant @ Row:" + rowCount);
                }


                if (mrp == null || mrp <= 0) {
                    throw new Exception("MRP should be greater than zero @ Row : " + rowCount);
                }
                if (cost == null || cost <= 0) {
                    throw new Exception("COST should be greater than zero @ Row : " + rowCount);
                }
                if (batchNumber == null || StringUtils.isBlank(batchNumber)) {
                    throw new Exception("Invalid batch number @ Row : " + rowCount);
                }

                Sku sku = skuService.getSKU(productVariant, reconciliationVoucher.getWarehouse());

                if (productVariant != null) {
                    RvLineItem rvLineItem = new RvLineItem();
                    if (skuGroup != null) {
                        rvLineItem.setSkuGroup(skuGroup);
                    } else {
                        rvLineItem.setSkuItem(skuItem);
                    }
                    rvLineItem.setProductVariant(productVariant);
                    rvLineItem.setBatchNumber(batchNumber);
                    rvLineItem.setQty(qtyForDeletion);
                    rvLineItem.setMrp(mrp);
                    rvLineItem.setCostPrice(cost);
                    rvLineItem.setMfgDate(mfgDate);
                    rvLineItem.setExpiryDate(expiryDate);
                    rvLineItem.setSku(sku);
                    rvLineItem.setReconciliationType(reconciliationType);
                    rvLineItem.setReconciliationVoucher(reconciliationVoucher);
                    rvLineItems.add(rvLineItem);

                    rowCount++;
                }
            }
        } catch (Exception e) {
            logger.error("Exception @ Row:" + (rowCount + 1) + e.getMessage());
            throw new Exception("Exception @ Row:" + (rowCount + 1) + ": " + e.getMessage(), e);
        }
        return rvLineItems;
    }


    public ProductVariantService getProductVariantService() {
        return productVariantService;
    }

    public SkuGroup getSkuGroupWithSystemQty(List<SkuGroup> skuGroupList, int qty) {
        for (SkuGroup skuGroup : skuGroupList) {
            List<SkuItem> inStockSkuItemList = skuGroupService.getInStockSkuItems(skuGroup);
            int systemQty = inStockSkuItemList.size();
            if (systemQty == qty) {
                return skuGroup;
            }
        }
        return null;
    }


    public List<RvLineItem> readAndCreateAddSubtractRvLineItemForProductAuditedForSingleBatch(String excelFilePath, String sheetName, ReconciliationVoucher reconciliationVoucher) throws Exception {
        List<RvLineItem> rvLineItems = new ArrayList<RvLineItem>();
        ExcelSheetParser parser = new ExcelSheetParser(excelFilePath, sheetName);
        Iterator<HKRow> rowIterator = parser.parse();
        int rowCount = 1;
        try {
            while (rowIterator.hasNext()) {
                HKRow row = rowIterator.next();
                String variantId = row.getColumnValue(XslConstants.VARIANT_ID);
                Long qty = XslUtil.getLong(row.getColumnValue(XslConstants.QTY));
                ProductVariant productVariant = getProductVariantService().getVariantById(variantId);
                Sku sku = null;

                try {
                    sku = skuService.getSKU(productVariant, reconciliationVoucher.getWarehouse());
                } catch (Exception ex) {
                    message.append("<br/>").append("Sku is not generated in system: For :" + variantId);
                    continue;
                }

                if (qty == null || qty <= 0) {
                    message.append("<br/>").append("Qty should be greater than zero For :" + variantId);
                    continue;
                }
                if (productVariant != null) {
                    RvLineItem rvLineItem = new RvLineItem();
                    rvLineItem.setProductVariant(productVariant);
                    rvLineItem.setReconciliationType(EnumReconciliationType.ProductVariantAudited.asReconciliationType());
                    rvLineItem.setQty(qty);
                    rvLineItem.setSku(sku);
                    rvLineItem.setReconciliationVoucher(reconciliationVoucher);
                    rvLineItems.add(rvLineItem);
                    rowCount++;
                }
            }
        } catch (Exception e) {
            logger.error("Exception @ Row:" + (rowCount + 1) + e.getMessage());
            throw new Exception("Exception @ Row:" + (rowCount + 1) + ": " + e.getMessage(), e);
        }
        return rvLineItems;
    }


    public List<RvLineItem> readAndCreateAddSubtractRvLineItemForProductAuditedForAnyBatch(String excelFilePath, String sheetName, ReconciliationVoucher reconciliationVoucher) throws Exception {
        List<RvLineItem> rvLineItems = new ArrayList<RvLineItem>();
        ExcelSheetParser parser = new ExcelSheetParser(excelFilePath, sheetName);
        Iterator<HKRow> rowIterator = parser.parse();
        int rowCount = 1;
        try {
            while (rowIterator.hasNext()) {
                HKRow row = rowIterator.next();
                String variantId = row.getColumnValue(XslConstants.VARIANT_ID);
                Long qty = XslUtil.getLong(row.getColumnValue(XslConstants.QTY));
                ProductVariant productVariant = getProductVariantService().getVariantById(variantId);
                Sku sku = null;

                try {
                    sku = skuService.getSKU(productVariant, reconciliationVoucher.getWarehouse());
                } catch (Exception ex) {
                    message.append("<br/>");
                    message.append("Sku is not generated in system: For :" + variantId);
                    continue;
                }

                if (qty == null || qty <= 0) {
                    message.append("<br/>");
                    message.append("Qty should be greater than zero For :" + variantId);
                    continue;
                }

                if (productVariant != null) {
                    RvLineItem rvLineItem = new RvLineItem();
                    rvLineItem.setProductVariant(productVariant);
                    rvLineItem.setReconciliationType(EnumReconciliationType.ProductVariantAudited.asReconciliationType());
                    rvLineItem.setQty(qty);
                    rvLineItem.setSku(sku);
                    rvLineItem.setReconciliationVoucher(reconciliationVoucher);
                    rvLineItems.add(rvLineItem);
                    rowCount++;
                }
            }
        } catch (Exception e) {
            logger.error("Exception @ Row:" + (rowCount + 1) + e.getMessage());
            throw new Exception("Exception @ Row:" + (rowCount + 1) + ": " + e.getMessage(), e);
        }
        return rvLineItems;
    }

    public StringBuilder getMessage() {
        return message;
    }

    public void setMessage(StringBuilder message) {
        this.message = message;
    }
}
