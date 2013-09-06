package com.hk.admin.util.helper;

import com.hk.admin.dto.inventory.CreateInventoryFileDto;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.admin.util.XslUtil;
import com.hk.constants.inventory.EnumReconciliationType;
import com.hk.domain.cycleCount.CycleCount;
import com.hk.domain.inventory.rv.ReconciliationType;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.service.inventory.SkuGroupService;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;

import com.hk.domain.cycleCount.CycleCountItem;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.sku.SkuItemStatus;
import com.hk.util.io.HkXlsWriter;
import com.hk.constants.XslConstants;
import com.hk.constants.sku.EnumSkuItemStatus;
import com.hk.admin.util.BarcodeUtil;
import com.hk.pact.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.stripes.action.Resolution;

/**
 * Created by IntelliJ IDEA.
 * User:Seema
 * Date: Jan 16, 2013
 * Time: 12:53:30 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class CycleCountHelper {
    File xlsFile;
    @Autowired
    private UserService userService;
    @Autowired
    SkuGroupService skuGroupService;
    @Autowired
    AdminInventoryService adminInventoryService;
    private static Logger logger = LoggerFactory.getLogger(CycleCountHelper.class);

    public File generateReconVoucherAddExcel(List<CycleCountItem> cycleCountItems, File xlsFile, Map<Long, Integer> cycleCountPVImap) {
        this.xlsFile = xlsFile;
        HkXlsWriter xlsWriter = new HkXlsWriter();
        int xlsRow = 1;
        xlsWriter.addHeader(XslConstants.VARIANT_ID, XslConstants.VARIANT_ID);
        xlsWriter.addHeader(XslConstants.QTY, XslConstants.QTY);
        xlsWriter.addHeader(XslConstants.BATCH_NUMBER, XslConstants.BATCH_NUMBER);
        xlsWriter.addHeader(XslConstants.EXP_DATE, XslConstants.EXP_DATE);
        xlsWriter.addHeader(XslConstants.MFG_DATE, XslConstants.MFG_DATE);
        xlsWriter.addHeader(XslConstants.MRP, XslConstants.MRP);
        xlsWriter.addHeader(XslConstants.COST, XslConstants.COST);
        xlsWriter.addHeader(XslConstants.RECON_REASON, XslConstants.RECON_REASON);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");

        for (CycleCountItem cycleCountItem : cycleCountItems) {
            SkuGroup skuGroup = cycleCountItem.getSkuGroup();
            xlsWriter.addCell(xlsRow, skuGroup.getSku().getProductVariant().getId());

            int pvi = cycleCountPVImap.get(cycleCountItem.getId());
            int scannedQty = cycleCountItem.getScannedQty();
            int qtyToAdd = Math.abs(pvi - scannedQty);
            xlsWriter.addCell(xlsRow, qtyToAdd);
            xlsWriter.addCell(xlsRow, skuGroup.getBatchNumber());
            String expiryDate = "";
            if (skuGroup.getExpiryDate() != null) {
                expiryDate = sdf.format(skuGroup.getExpiryDate());
            }
            xlsWriter.addCell(xlsRow, expiryDate);
            String mfgDate = "";
            if (skuGroup.getMfgDate() != null) {
                mfgDate = sdf.format(skuGroup.getMfgDate());
            }
            xlsWriter.addCell(xlsRow, mfgDate);

            xlsWriter.addCell(xlsRow, skuGroup.getMrp());
            xlsWriter.addCell(xlsRow, skuGroup.getCostPrice());
            xlsWriter.addCell(xlsRow, "");
            xlsRow++;
        }
        xlsWriter.writeData(xlsFile, "Sheet1");
        return xlsFile;

    }


    public File generateSubtractRvExcel(List<CycleCountItem> cycleCountItems, File xlsFile) {
        this.xlsFile = xlsFile;
        HkXlsWriter xlsWriter = new HkXlsWriter();
        int xlsRow = 1;
        xlsWriter.addHeader(XslConstants.GROUP_BARCODE, XslConstants.GROUP_BARCODE);
        xlsWriter.addHeader(XslConstants.ITEM_BARCODE, XslConstants.ITEM_BARCODE);
        xlsWriter.addHeader(XslConstants.VARIANT_ID, XslConstants.VARIANT_ID);
        xlsWriter.addHeader(XslConstants.QTY, XslConstants.QTY);
        xlsWriter.addHeader(XslConstants.BATCH_NUMBER, XslConstants.BATCH_NUMBER);
        xlsWriter.addHeader(XslConstants.EXP_DATE, XslConstants.EXP_DATE);
        xlsWriter.addHeader(XslConstants.MFG_DATE, XslConstants.MFG_DATE);
        xlsWriter.addHeader(XslConstants.MRP, XslConstants.MRP);
        xlsWriter.addHeader(XslConstants.COST, XslConstants.COST);
        xlsWriter.addHeader(XslConstants.SYSTEM_QTY, XslConstants.SYSTEM_QTY);
        xlsWriter.addHeader(XslConstants.RECON_REASON, XslConstants.RECON_REASON);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");

        for (CycleCountItem cycleCountItem : cycleCountItems) {
            String groupBarcode = "";
            String itemBarcode = "";
            SkuGroup skuGroup = null;
            if (cycleCountItem.getSkuGroup() == null) {
                xlsWriter.addCell(xlsRow, "");
            } else {
                skuGroup = cycleCountItem.getSkuGroup();
                groupBarcode = skuGroup.getBarcode();
                xlsWriter.addCell(xlsRow, groupBarcode);
            }
            if (cycleCountItem.getSkuItem() == null) {
                xlsWriter.addCell(xlsRow, "");
            } else {
                skuGroup = cycleCountItem.getSkuItem().getSkuGroup();
                itemBarcode = cycleCountItem.getSkuItem().getBarcode();
                xlsWriter.addCell(xlsRow, itemBarcode);
            }
            xlsWriter.addCell(xlsRow, skuGroup.getSku().getProductVariant().getId());
            int systemQty = cycleCountItem.getSystemQty();
            int scannedQty = cycleCountItem.getScannedQty();
            int qtyToSubtract = Math.abs(systemQty - scannedQty);
            xlsWriter.addCell(xlsRow, qtyToSubtract);
            xlsWriter.addCell(xlsRow, skuGroup.getBatchNumber());
            String expiryDate = "";
            if (skuGroup.getExpiryDate() != null) {
                expiryDate = sdf.format(skuGroup.getExpiryDate());
            }
            xlsWriter.addCell(xlsRow, expiryDate);
            String mfgDate = "";
            if (skuGroup.getMfgDate() != null) {
                mfgDate = sdf.format(skuGroup.getMfgDate());
            }
            xlsWriter.addCell(xlsRow, mfgDate);

            xlsWriter.addCell(xlsRow, skuGroup.getMrp());
            xlsWriter.addCell(xlsRow, skuGroup.getCostPrice());
            xlsWriter.addCell(xlsRow, cycleCountItem.getSystemQty());
            xlsWriter.addCell(xlsRow, "");

            xlsRow++;
        }
        xlsWriter.writeData(xlsFile, "Sheet1");
        return xlsFile;
    }


    public File generateCompleteCycleCountExcel(List<CycleCountItem> cycleCountItems, File xlsFile, Map<Long, Integer> cycleCountPVImap, List<SkuGroup> skuGroupList, Map<Long, Integer> missedSkuGroupSystemInventoryMap, List<SkuGroup> scannedSkuItemGroupList) {
    	List<Long> skuItemStatusIdList = new ArrayList<Long>();
    	skuItemStatusIdList.add( EnumSkuItemStatus.Checked_IN.getId());
    	skuItemStatusIdList.add( EnumSkuItemStatus.BOOKED.getId());
    	skuItemStatusIdList.add( EnumSkuItemStatus.TEMP_BOOKED.getId());
        
    	
    	this.xlsFile = xlsFile;
        HkXlsWriter xlsWriter = new HkXlsWriter();
        int xlsRow = 1;
        xlsWriter.addHeader(XslConstants.VARIANT_ID, XslConstants.VARIANT_ID);
        xlsWriter.addHeader(XslConstants.VARIANT_NAME, XslConstants.VARIANT_NAME);
        xlsWriter.addHeader(XslConstants.BATCH_NUMBER, XslConstants.BATCH_NUMBER);
        xlsWriter.addHeader(XslConstants.HK_BARCODE, XslConstants.HK_BARCODE);
        xlsWriter.addHeader(XslConstants.SCANNED_QTY, XslConstants.SCANNED_QTY);
        xlsWriter.addHeader(XslConstants.SYSTEM_QTY, XslConstants.SYSTEM_QTY);
        xlsWriter.addHeader(XslConstants.VARINACE_QTY, XslConstants.VARINACE_QTY);
        xlsWriter.addHeader(XslConstants.EXP_DATE, XslConstants.EXP_DATE);
        xlsWriter.addHeader(XslConstants.MFG_DATE, XslConstants.MFG_DATE);
        xlsWriter.addHeader(XslConstants.MRP, XslConstants.MRP);
        xlsWriter.addHeader(XslConstants.COST, XslConstants.COST);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");


        List<SkuItem> scannedSkuItems = new ArrayList<SkuItem>();

        for (CycleCountItem cycleCountItem : cycleCountItems) {
            SkuGroup skuGroup = null;
            if (cycleCountItem.getSkuGroup() != null) {
                skuGroup = cycleCountItem.getSkuGroup();
            } else {
                skuGroup = cycleCountItem.getSkuItem().getSkuGroup();
                scannedSkuItems.add(cycleCountItem.getSkuItem());
            }
            xlsWriter.addCell(xlsRow, skuGroup.getSku().getProductVariant().getId());
            xlsWriter.addCell(xlsRow, skuGroup.getSku().getProductVariant().getProduct().getName());
            xlsWriter.addCell(xlsRow, skuGroup.getBatchNumber());
            if (cycleCountItem.getSkuGroup() != null) {
                xlsWriter.addCell(xlsRow, skuGroup.getBarcode());
            } else {
                xlsWriter.addCell(xlsRow, cycleCountItem.getSkuItem().getBarcode());
            }

            int scannedQty = cycleCountItem.getScannedQty().intValue();
            xlsWriter.addCell(xlsRow, scannedQty);
            int sysQty = 0;
            if (cycleCountItem.getSkuGroup() != null) {
                sysQty = cycleCountPVImap.get(cycleCountItem.getId());
            } else {
                sysQty = 1;
            }
            xlsWriter.addCell(xlsRow, sysQty);
            int diffQty = sysQty - scannedQty;
            xlsWriter.addCell(xlsRow, diffQty);
            String expiryDate = "";
            if (skuGroup.getExpiryDate() != null) {
                expiryDate = sdf.format(skuGroup.getExpiryDate());
            }
            xlsWriter.addCell(xlsRow, expiryDate);
            String mfgDate = "";
            if (skuGroup.getMfgDate() != null) {
                mfgDate = sdf.format(skuGroup.getMfgDate());
            }
            xlsWriter.addCell(xlsRow, mfgDate);
            Double mrp = 0d;
            if (skuGroup.getMrp() != null) {
                mrp = skuGroup.getMrp();
            }
            xlsWriter.addCell(xlsRow, mrp);
            xlsWriter.addCell(xlsRow, skuGroup.getCostPrice());

            xlsRow++;

        }


        List<SkuItem> totalSkuItemsForScannedItemGroup = new ArrayList<SkuItem>();
        for (SkuGroup scannedSkuItemGroup : scannedSkuItemGroupList) {
            totalSkuItemsForScannedItemGroup.addAll(scannedSkuItemGroup.getSkuItems());
        }

        totalSkuItemsForScannedItemGroup.removeAll(scannedSkuItems);
        Collections.sort(totalSkuItemsForScannedItemGroup);
        /* Add skuitem information of group which were partial scanned */

        

        for (SkuItem skuItem : totalSkuItemsForScannedItemGroup) {
            SkuGroup skuGroup = skuItem.getSkuGroup();
            if (skuItemStatusIdList.contains(skuItem.getSkuItemStatus().getId()) && skuGroup.getSku().getWarehouse() == userService.getWarehouseForLoggedInUser()) {
                xlsWriter.addCell(xlsRow, skuGroup.getSku().getProductVariant().getId());
                xlsWriter.addCell(xlsRow, skuGroup.getSku().getProductVariant().getProduct().getName());
                xlsWriter.addCell(xlsRow, skuGroup.getBatchNumber());
                xlsWriter.addCell(xlsRow, skuItem.getBarcode());
                xlsWriter.addCell(xlsRow, "0");
                int sysQty = 1;
                xlsWriter.addCell(xlsRow, sysQty);
                xlsWriter.addCell(xlsRow, sysQty);
                String expiryDate = "";
                if (skuGroup.getExpiryDate() != null) {
                    expiryDate = sdf.format(skuGroup.getExpiryDate());
                }
                xlsWriter.addCell(xlsRow, expiryDate);
                String mfgDate = "";
                if (skuGroup.getMfgDate() != null) {
                    mfgDate = sdf.format(skuGroup.getMfgDate());
                }
                xlsWriter.addCell(xlsRow, mfgDate);
                Double mrp = 0d;
                if (skuGroup.getMrp() != null) {
                    mrp = skuGroup.getMrp();
                }
                xlsWriter.addCell(xlsRow, mrp);
                xlsWriter.addCell(xlsRow, skuGroup.getCostPrice());

                xlsRow++;
            }
        }
        /* Add SkuGroup Missed in Scanning */

        for (SkuGroup skuGroup : skuGroupList) {
            xlsWriter.addCell(xlsRow, skuGroup.getSku().getProductVariant().getId());
            xlsWriter.addCell(xlsRow, skuGroup.getSku().getProductVariant().getProduct().getName());
            xlsWriter.addCell(xlsRow, skuGroup.getBatchNumber());
            if (skuGroup.getBarcode() != null) {
                xlsWriter.addCell(xlsRow, skuGroup.getBarcode());
            } else {
                xlsWriter.addCell(xlsRow, BarcodeUtil.BARCODE_SKU_ITEM_PREFIX_AQ + skuGroup.getId());
            }

            xlsWriter.addCell(xlsRow, "0");
            int sysQty = missedSkuGroupSystemInventoryMap.get(skuGroup.getId());
            xlsWriter.addCell(xlsRow, sysQty);
            xlsWriter.addCell(xlsRow, sysQty);
            String expiryDate = "";
            if (skuGroup.getExpiryDate() != null) {
                expiryDate = sdf.format(skuGroup.getExpiryDate());
            }
            xlsWriter.addCell(xlsRow, expiryDate);
            String mfgDate = "";
            if (skuGroup.getMfgDate() != null) {
                mfgDate = sdf.format(skuGroup.getMfgDate());
            }
            xlsWriter.addCell(xlsRow, mfgDate);
            Double mrp = 0d;
            if (skuGroup.getMrp() != null) {
                mrp = skuGroup.getMrp();
            }
            xlsWriter.addCell(xlsRow, mrp);
            xlsWriter.addCell(xlsRow, skuGroup.getCostPrice());

            xlsRow++;
        }


        xlsWriter.writeData(xlsFile, "Sheet1");
        return xlsFile;
    }


    public File generateSkuGroupNotScannedExcel(List<SkuGroup> skuGroupList, File xlsFile, Map<Long, Integer> missedSkuGroupSystemInventoryMap) {
        this.xlsFile = xlsFile;
        HkXlsWriter xlsWriter = new HkXlsWriter();
        int xlsRow = 1;
        xlsWriter.addHeader(XslConstants.HK_BARCODE, XslConstants.HK_BARCODE);
        xlsWriter.addHeader(XslConstants.VARIANT_ID, XslConstants.VARIANT_ID);
        xlsWriter.addHeader(XslConstants.PRODUCT_NAME, XslConstants.PRODUCT_NAME);
        xlsWriter.addHeader(XslConstants.VARIANT_NAME, XslConstants.VARIANT_NAME);
        xlsWriter.addHeader(XslConstants.MRP, XslConstants.MRP);
        xlsWriter.addHeader(XslConstants.MFG_DATE, XslConstants.MFG_DATE);
        xlsWriter.addHeader(XslConstants.EXP_DATE, XslConstants.EXP_DATE);

        xlsWriter.addHeader(XslConstants.SCANNED_QTY, XslConstants.SCANNED_QTY);
        xlsWriter.addHeader(XslConstants.BATCH_NUMBER, XslConstants.BATCH_NUMBER);
        xlsWriter.addHeader(XslConstants.SYSTEM_QTY, XslConstants.SYSTEM_QTY);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");

        for (SkuGroup skuGroup : skuGroupList) {
            if (skuGroup.getBarcode() == null || skuGroup.getBarcode().isEmpty()) {
                xlsWriter.addCell(xlsRow, BarcodeUtil.BARCODE_SKU_ITEM_PREFIX_AQ + skuGroup.getId());
            } else {
                xlsWriter.addCell(xlsRow, skuGroup.getBarcode());
            }
            xlsWriter.addCell(xlsRow, skuGroup.getSku().getProductVariant().getId());
            xlsWriter.addCell(xlsRow, skuGroup.getSku().getProductVariant().getProduct().getName());
            xlsWriter.addCell(xlsRow, skuGroup.getSku().getProductVariant().getOptionsPipeSeparated());
            Double mrp = 0d;
            if (skuGroup.getMrp() != null) {
                mrp = skuGroup.getMrp();
            }
            xlsWriter.addCell(xlsRow, mrp);
            String mfgDate = "";
            if (skuGroup.getMfgDate() != null) {
                mfgDate = sdf.format(skuGroup.getMfgDate());
            }
            xlsWriter.addCell(xlsRow, mfgDate);
            String expiryDate = "";
            if (skuGroup.getExpiryDate() != null) {
                expiryDate = sdf.format(skuGroup.getExpiryDate());
            }
            xlsWriter.addCell(xlsRow, expiryDate);
            int systemQty = missedSkuGroupSystemInventoryMap.get(skuGroup.getId());
            xlsWriter.addCell(xlsRow, "0");
            xlsWriter.addCell(xlsRow, skuGroup.getBatchNumber());
            xlsWriter.addCell(xlsRow, systemQty);
            xlsRow++;
        }
        xlsWriter.writeData(xlsFile, "MissedBatches");
        return xlsFile;
    }


    public Resolution download() {
        return new Resolution() {
            public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
                OutputStream out = null;
                InputStream in = new BufferedInputStream(new FileInputStream(xlsFile));
                res.setContentLength((int) xlsFile.length());
                res.setHeader("Content-Disposition", "attachment; filename=\"" + xlsFile.getName() + "\";");
                out = res.getOutputStream();

                // Copy the contents of the file to the output stream
                byte[] buf = new byte[4096];
                int count = 0;
                while ((count = in.read(buf)) >= 0) {
                    out.write(buf, 0, count);
                }
            }
        };

    }


    public Map<String, Integer> readCycleCountNotepad(File file) throws IOException {
        logger.debug("parsing Cycle Count Notepad : " + file.getAbsolutePath());
        Map<String, Integer> barcodeQtyMap = new HashMap<String, Integer>();
        BufferedReader buffer = new BufferedReader(new FileReader(file));
        String barcode = null;
        while ((barcode = buffer.readLine()) != null) {
            if (barcode.isEmpty()) {
                continue;
            }
            if (barcodeQtyMap.containsKey(barcode)) {
                int qty = barcodeQtyMap.get(barcode);
                barcodeQtyMap.put(barcode, (qty + 1));
            } else {
                barcodeQtyMap.put(barcode, 1);
            }
        }


        return barcodeQtyMap;
    }


    // populate Map of SkuGroup and List of SkuItems for Items Scanned by Item Level Barcode
    public Map<SkuGroup, List<SkuItem>> populateScannedSkuItemsBySkuGroupMap(List<CycleCountItem> cycleCountItems) {
        Map<SkuGroup, List<SkuItem>> totalScannedSkuItemBySkuGroup = new HashMap<SkuGroup, List<SkuItem>>();
        for (CycleCountItem cycleCountItem : cycleCountItems) {
            if (cycleCountItem.getSkuItem() != null) {
                SkuItem skuItem = cycleCountItem.getSkuItem();
                if (totalScannedSkuItemBySkuGroup.containsKey(skuItem.getSkuGroup())) {
                    List<SkuItem> alreadyStoredSkuItemList = totalScannedSkuItemBySkuGroup.get(skuItem.getSkuGroup());
                    alreadyStoredSkuItemList.add(cycleCountItem.getSkuItem());
                    totalScannedSkuItemBySkuGroup.put(skuItem.getSkuGroup(), alreadyStoredSkuItemList);
                } else {
                    List<SkuItem> skuItemList = new ArrayList<SkuItem>();
                    skuItemList.add(skuItem);
                    totalScannedSkuItemBySkuGroup.put(skuItem.getSkuGroup(), skuItemList);
                }
            }
        }
        return totalScannedSkuItemBySkuGroup;
    }

    //Creating Cycle Count items For Batch Level and Item Level Barcode
    public void generateCycleCountItemsCombiningSkuGroupAndSkuItemScanning(CycleCount cycleCount, Map<Long, Integer> cycleCountPviMap, List<SkuGroup> skuGroupNeverScanned, Map<Long, Integer> missedSkuGroupSystemInventoryMap, File excelFile) {
    	List<SkuItemStatus> skuItemStatusList = new ArrayList<SkuItemStatus>();
        skuItemStatusList.add( EnumSkuItemStatus.Checked_IN.getSkuItemStatus());
        skuItemStatusList.add( EnumSkuItemStatus.BOOKED.getSkuItemStatus());
        skuItemStatusList.add( EnumSkuItemStatus.TEMP_BOOKED.getSkuItemStatus());
        
        List<CycleCountItem> cycleCountItems = cycleCount.getCycleCountItems();
        List<CycleCountItem> cycleCountItemListForRVSubtract = new ArrayList<CycleCountItem>();
        Map<SkuGroup, List<SkuItem>> scannedSkuItemsMapOriginal = populateScannedSkuItemsBySkuGroupMap(cycleCountItems);
        Map<SkuGroup, List<SkuItem>> scannedSkuItemsBySkuGroupMap = new HashMap<SkuGroup, List<SkuItem>>(scannedSkuItemsMapOriginal);

        for (CycleCountItem cycleCountItem : cycleCountItems) {
            int systemQty = 0, scannedQty = 0;
            SkuGroup skuGroup = null;
            //Batch Level Scanning
            if (cycleCountItem.getSkuGroup() != null) {
                systemQty = cycleCountPviMap.get(cycleCountItem.getId());
                scannedQty = cycleCountItem.getScannedQty();

            } else {
                //Item Level Scanning
                skuGroup = cycleCountItem.getSkuItem().getSkuGroup();
                if (scannedSkuItemsBySkuGroupMap.containsKey(skuGroup)) {
                    List<SkuItem> skuItemList = skuGroupService.getInStockSkuItems(skuGroup, skuItemStatusList);
                    if (skuItemList != null) {
                        systemQty = skuItemList.size();
                    }
                    cycleCountItem.setSkuGroup(skuGroup);
                    scannedQty = scannedSkuItemsBySkuGroupMap.get(skuGroup).size();
                    scannedSkuItemsBySkuGroupMap.remove(skuGroup);
                } else {
                    continue;
                }
            }
            cycleCountItem.setScannedQty(scannedQty);
            cycleCountItem.setSystemQty(systemQty);
            if (systemQty - scannedQty > 0) {
                cycleCountItemListForRVSubtract.add(cycleCountItem);
            }


        }

        filterBySkuItemAndSkuGroup(cycleCountItemListForRVSubtract, scannedSkuItemsMapOriginal, skuGroupNeverScanned, missedSkuGroupSystemInventoryMap,cycleCount, excelFile);


    }

    private void filterBySkuItemAndSkuGroup(List<CycleCountItem> cycleCountItemListForRVSubtract, Map<SkuGroup, List<SkuItem>> scannedSkuItemsMapOriginal, List<SkuGroup> skuGroupNeverScanned,
                                            Map<Long, Integer> missedSkuGroupSystemInventoryMap,CycleCount cycleCount, File excelFile) {
    	List<SkuItemStatus> skuItemStatusList = new ArrayList<SkuItemStatus>();
        skuItemStatusList.add( EnumSkuItemStatus.Checked_IN.getSkuItemStatus());
        skuItemStatusList.add( EnumSkuItemStatus.BOOKED.getSkuItemStatus());
        skuItemStatusList.add( EnumSkuItemStatus.TEMP_BOOKED.getSkuItemStatus());
        List<CycleCountItem> finalListOfCycleCountItemsList = new ArrayList<CycleCountItem>();
        for (CycleCountItem cycleCountItem : cycleCountItemListForRVSubtract) {
            SkuGroup skuGroup = cycleCountItem.getSkuGroup();
            if (scannedSkuItemsMapOriginal.containsKey(skuGroup)) {
                //Item Scanned by Item Level Barcode
                List<SkuItem> skuItemListShouldNotBeDeleted = scannedSkuItemsMapOriginal.get(skuGroup);
                List<SkuItem> inStockSkuItem = skuGroupService.getInStockSkuItems(skuGroup, skuItemStatusList);
                inStockSkuItem.removeAll(skuItemListShouldNotBeDeleted);
                List<SkuItem> skuItemShouldBeDeleted = inStockSkuItem;
                List<CycleCountItem> cycleCountItemListForDeletion = createCycleCountItemsForSkuItemsEligibleForDeletion(skuItemShouldBeDeleted, cycleCountItem);
                finalListOfCycleCountItemsList.addAll(cycleCountItemListForDeletion);
            } else {
                //Item Scanned by Batch Level(SkuGroup) Barcode
                cycleCountItem.setSkuItem(null);
                finalListOfCycleCountItemsList.add(cycleCountItem);
            }

        }

        for (SkuGroup skuGroup : skuGroupNeverScanned) {
            CycleCountItem cycleCountItemAtSkUGroupLevel = new CycleCountItem();
            //for batches which are not scanned in CC and  whose item level barcode are not generated yet
            if (skuGroup.getBarcode() != null) {
                cycleCountItemAtSkUGroupLevel.setSkuGroup(skuGroup);
                cycleCountItemAtSkUGroupLevel.setSkuItem(null);
                cycleCountItemAtSkUGroupLevel.setSystemQty(missedSkuGroupSystemInventoryMap.get(skuGroup.getId()));
                cycleCountItemAtSkUGroupLevel.setScannedQty(0);
                finalListOfCycleCountItemsList.add(cycleCountItemAtSkUGroupLevel);
            } else {
                //Sku Item which are never scanned in CC and whose item level barcode are generated
                List<SkuItem> checkedInSkuItem = skuGroupService.getInStockSkuItems(skuGroup, skuItemStatusList);
                List<CycleCountItem> cycleCountItemListForDeletion = createCycleCountItemsForSkuItemsEligibleForDeletion(checkedInSkuItem, cycleCount);
                finalListOfCycleCountItemsList.addAll(cycleCountItemListForDeletion);
            }

        }

        generateSubtractRvExcel(finalListOfCycleCountItemsList, excelFile);

    }

    //Create CC item s for all Sku Item of missed Sku Group ,They will be added in Excel sheet in the same form
    private List<CycleCountItem> createCycleCountItemsForSkuItemsEligibleForDeletion(List<SkuItem> skuItemShouldBeDeleted, CycleCount cycleCount) {
        List<CycleCountItem> cycleCountItemListForSkuItems = new ArrayList<CycleCountItem>();
        for (SkuItem skuItem : skuItemShouldBeDeleted) {
            CycleCountItem cycleCountItemAtSkuItemLevel = new CycleCountItem();
            cycleCountItemAtSkuItemLevel.setSystemQty(1);
            cycleCountItemAtSkuItemLevel.setScannedQty(0);
            cycleCountItemAtSkuItemLevel.setCycleCount(cycleCount);
            cycleCountItemAtSkuItemLevel.setSkuItem(skuItem);
            cycleCountItemAtSkuItemLevel.setSkuGroup(null);
            cycleCountItemListForSkuItems.add(cycleCountItemAtSkuItemLevel);

        }
        return cycleCountItemListForSkuItems;
    }

    //Create Cycle count Item for every Sku Item Which are not Scanned , They will be added in Excel sheet in the same form
    private List<CycleCountItem> createCycleCountItemsForSkuItemsEligibleForDeletion(List<SkuItem> skuItemShouldBeDeleted, CycleCountItem cycleCountItem) {
        List<CycleCountItem> cycleCountItemListForSkuItems = new ArrayList<CycleCountItem>();

        int qtyToBeDeleted = cycleCountItem.getSystemQty() - cycleCountItem.getScannedQty();
        while (qtyToBeDeleted > 0) {
            CycleCountItem cycleCountItemAtSkuItemLevel = new CycleCountItem();
            cycleCountItemAtSkuItemLevel.setSystemQty(1);
            cycleCountItemAtSkuItemLevel.setScannedQty(0);
            cycleCountItemAtSkuItemLevel.setCycleCount(cycleCountItem.getCycleCount());
            SkuItem skuItem = skuItemShouldBeDeleted.get(qtyToBeDeleted - 1);
            cycleCountItemAtSkuItemLevel.setSkuItem(skuItem);
            cycleCountItemAtSkuItemLevel.setSkuGroup(null);
            cycleCountItemListForSkuItems.add(cycleCountItemAtSkuItemLevel);
            qtyToBeDeleted--;
        }
        return cycleCountItemListForSkuItems;
    }


}

