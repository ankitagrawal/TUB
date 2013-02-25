package com.hk.web.action.admin.inventory;


import com.akube.framework.stripes.action.BasePaginatedAction;

import com.akube.framework.dao.Page;
import com.hk.domain.cycleCount.CycleCountItem;
import com.hk.domain.cycleCount.CycleCount;
import com.hk.domain.sku.Sku;
import com.hk.domain.warehouse.Warehouse;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.core.JSONObject;

import com.hk.domain.user.User;
import com.hk.domain.inventory.BrandsToAudit;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.admin.pact.service.inventory.CycleCountService;
import com.hk.admin.pact.dao.inventory.BrandsToAuditDao;
import com.hk.admin.util.helper.CycleCountHelper;
import com.hk.pact.service.inventory.SkuGroupService;
import com.hk.pact.service.UserService;
import com.hk.pact.service.catalog.ProductService;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.constants.inventory.EnumCycleCountStatus;
import com.hk.constants.inventory.EnumAuditStatus;
import com.hk.constants.core.Keys;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;


import java.util.*;

import java.lang.reflect.Type;
import java.io.*;
import java.text.SimpleDateFormat;

import com.hk.pact.service.inventory.SkuService;
import net.sourceforge.stripes.action.*;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by IntelliJ IDEA.
 * User:Seema
 * Date: Jan 10, 2013
 * Time: 10:09:56 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class CycleCountAction extends BasePaginatedAction {

    private static Logger logger = LoggerFactory.getLogger(CycleCountAction.class);

    @Autowired
    CycleCountService cycleCountService;
    @Autowired
    SkuGroupService skuGroupService;
    @Autowired
    UserService userService;
    @Autowired
    CycleCountHelper cycleCountHelper;
    @Autowired
    ProductService productService;
    @Autowired
    BrandsToAuditDao brandsToAuditDao;
    @Autowired
    ProductVariantService productVariantService;
    @Autowired
    SkuService skuService;


    @Value("#{hkEnvProps['" + Keys.Env.adminDownloads + "']}")
    String adminDownloadsPath;

    private List<CycleCountItem> cycleCountItems = new ArrayList<CycleCountItem>();
    private List<CycleCount> cycleCountList;
    private CycleCount cycleCount;
    private String hkBarcode;
    private String message;
    private Map<String, String> hkBarcodeErrorsMap = new HashMap<String, String>();
    private Map<Long, Integer> cycleCountPviMap = new HashMap<Long, Integer>();
    private Map<Long, Integer> skuGroupSystemInventoryMap = new HashMap<Long, Integer>();
    private String cycleCountPVImapString;
    private boolean error = false;
    private Page cycleCountPage;
    private String brand;
    private String auditorLogin;
    private Date startDate;
    private Date endDate;
    private Integer defaultPerPage = 20;
    FileBean fileBean;
    private String auditBy;
    private Integer cycleCountType;
    private Long cycleCountStatus;
    private List<SkuGroup> missedSkuGroupList = new ArrayList<SkuGroup>();

    public Resolution directToCycleCountPage() {

        if (cycleCount.getId() == null) {
            BrandsToAudit brandsToAudit = cycleCount.getBrandsToAudit();
            boolean auditInProgress = ifBrandProductCyclecountInProgress(brandsToAudit.getBrand());
            if (!auditInProgress) {
                cycleCount = saveCycleCountInDb(cycleCount);
            }
        }

        return view();

    }

    private CycleCount saveCycleCountInDb(CycleCount cycleCount) {
        cycleCount.setCreateDate(new Date());
        cycleCount.setUser(userService.getLoggedInUser());
        cycleCount.setCycleStatus(EnumCycleCountStatus.InProgress.getId());
        cycleCount.setWarehouse(userService.getWarehouseForLoggedInUser());
        cycleCount = cycleCountService.save(cycleCount);
        return cycleCount;
    }


    public Resolution createCycleCount() {
        return new RedirectResolution("/pages/admin/createCycleCount.jsp");
    }


    private boolean ifBrandProductCyclecountInProgress(String brandEntered) {
        Warehouse warehouse = userService.getWarehouseForLoggedInUser();
        /* if product of same brand in audit pending*/
        List<Product> productList = productService.getAllProductByBrand(brandEntered);
        if (productList != null) {
            for (Product product : productList) {
                List<CycleCount> cycleCounts = cycleCountService.getCycleCountInProgress(null, product, null, warehouse);
                if (cycleCounts != null && cycleCounts.size() > 0) {
                    message = "OOoops !!! ERROR :  Cycle Count of Product : " + product.getId() + " of same brand : " + brandEntered + " Already in Progress. Close if first";
                    return true;
                }
                boolean productVariantCcInProgress = ifProductVariantsCycleCountInProgress(product, brandEntered);
                if (productVariantCcInProgress) {
                    return true;
                }
            }
        }
        return false;

    }


    private boolean ifProductVariantsCycleCountInProgress(Product product, String brandEntered) {
        List<ProductVariant> productVariantsList = productVariantService.getProductVariantsByProductId(product.getId());
        if (productVariantsList != null) {
            for (ProductVariant productVariant : productVariantsList) {
                List<CycleCount> cycleCountsList = cycleCountService.getCycleCountInProgress(null, null, productVariant, userService.getWarehouseForLoggedInUser());
                if (cycleCountsList != null && cycleCountsList.size() > 0) {
                    message = "OOooopss !!! ERROR :  Cycle count of PV Id : " + productVariant.getId() + " of same brand : " + brandEntered + " already in Progress. Close it first";
                    return true;
                }
            }
        }
        return false;
    }


    private BrandsToAudit validateBrand() {
        BrandsToAudit brandsToAudit = null;
        boolean doesBrandExist = productService.doesBrandExist(auditBy);
        User auditor = getPrincipalUser();
        Warehouse warehouse = auditor.getSelectedWarehouse();
        if (!doesBrandExist) {
            message = "Invalid Brand";
        } else {
            List<BrandsToAudit> brandsToAuditInDb = brandsToAuditDao.getBrandsToAudit(auditBy, EnumAuditStatus.Pending.getId(), warehouse);
            /* Audit entry for this brand already exists in database with status pending in specific warehouse */
            if (brandsToAuditInDb != null && brandsToAuditInDb.size() > 0) {
                message = "Brand Audit Already In progress in warehouse" + warehouse.getCity();
                return brandsToAudit;
            }
            boolean brandAuditInProgress = ifBrandProductCyclecountInProgress(auditBy);
            if (!brandAuditInProgress) {
                brandsToAudit = new BrandsToAudit();
                brandsToAudit.setAuditStatus(EnumAuditStatus.Pending.getId());
                brandsToAudit.setAuditor(auditor);
                brandsToAudit.setWarehouse(warehouse);
                brandsToAudit.setBrand(auditBy);
                brandsToAudit.setAuditDate(new Date());
                brandsToAudit = (BrandsToAudit) brandsToAuditDao.save(brandsToAudit);
                addRedirectAlertMessage(new SimpleMessage("Cycle Count Created By Brand"));
            }
        }
        return brandsToAudit;
    }


    public Resolution saveCycleCount() {
        boolean returnToCreateCountPage = false;
        boolean returnToCycleCountListPage = false;
        if (cycleCount == null) {
            cycleCount = new CycleCount();
        }
        if (cycleCountType == 1) {
            BrandsToAudit brandsToAudit = validateBrand();
            if (brandsToAudit == null) {
                if (message.equals("Invalid Brand")) {
                    returnToCreateCountPage = true;
                } else {
                    returnToCycleCountListPage = true;
                }

            }
            cycleCount.setBrandsToAudit(brandsToAudit);
        } else if (cycleCountType == 2) {
            Product product = productService.getProductById(auditBy);
            if (product == null) {
                returnToCreateCountPage = true;
                message = "Invalid Product Id ";
            } else {
                List<CycleCount> cycleCounts = cycleCountService.getCycleCountInProgress(null, product, null, userService.getWarehouseForLoggedInUser());
                if (cycleCounts != null && cycleCounts.size() > 0) {
                    returnToCycleCountListPage = true;
                    message = "Product Cycle Count Already In Progress";
                } else {
                    boolean variantsOfProductCcInProgress = ifProductVariantsCycleCountInProgress(product, auditBy);
                    if (variantsOfProductCcInProgress) {
                        returnToCycleCountListPage = true;
                    }
                }
            }
            cycleCount.setProduct(product);

        } else {
            ProductVariant productVariant = productVariantService.getVariantById(auditBy);
            if (productVariant == null) {
                returnToCreateCountPage = true;
                message = "Invalid Product Variant Id ";
            } else {
                List<CycleCount> cycleCounts = cycleCountService.getCycleCountInProgress(null, null, productVariant, userService.getWarehouseForLoggedInUser());
                if (cycleCounts != null && cycleCounts.size() > 0) {
                    returnToCycleCountListPage = true;
                    message = "Product Variant Cycle Count Already In Progress";
                }
            }
            cycleCount.setProductVariant(productVariant);

        }
        if (returnToCreateCountPage) {
            return new ForwardResolution("/pages/admin/createCycleCount.jsp");
        }
        if (returnToCycleCountListPage) {
            addRedirectAlertMessage(new SimpleMessage(message));
            return new RedirectResolution(CycleCountAction.class, "pre");
        }
        addRedirectAlertMessage(new SimpleMessage("Changes Saved"));
        cycleCount = saveCycleCountInDb(cycleCount);
        return new RedirectResolution(CycleCountAction.class, "pre");
    }


    @DefaultHandler
    public Resolution pre() {
        Warehouse warehouse = userService.getWarehouseForLoggedInUser();
        User auditor = null;
        if (StringUtils.isNotBlank(auditorLogin)) {
            auditor = getUserService().findByLogin(auditorLogin);
        }
        cycleCountPage = cycleCountService.searchCycleList(auditBy, cycleCountStatus, warehouse, auditor, startDate, endDate, getPageNo(), getPerPage());
        if (cycleCountPage != null) {
            cycleCountList = cycleCountPage.getList();
        }
        return new ForwardResolution("/pages/admin/cycleCountList.jsp");

    }


    public Resolution view() {
        //Make null ,to clear field in case of forward request
        setHkBarcode(null);
        cycleCountItems = cycleCount.getCycleCountItems();
        if (cycleCountPVImapString != null) {
            cycleCountPviMap = getMapFromJsonString(cycleCountPVImapString);
        }
        cycleCountItems = cycleCount.getCycleCountItems();
        int newEntryInMap = 0;
        for (CycleCountItem cycleCountItem : cycleCountItems) {
            if (!(cycleCountPviMap.containsKey(cycleCountItem.getId()))) {
                //new entry added by another auditor
                newEntryInMap++;
                List<SkuItem> skuItemList = skuGroupService.getInStockSkuItems(cycleCountItem.getSkuGroup());
                int pvi = 0;
                if (skuItemList != null) {
                    pvi = skuItemList.size();
                }
                cycleCountPviMap.put(cycleCountItem.getId(), pvi);
            }
        }
        if (cycleCountPviMap != null && cycleCountPviMap.size() > 0) {
            if (newEntryInMap > 0) {
                cycleCountPVImapString = getStringFromMap(cycleCountPviMap);
            }
        }

        return new ForwardResolution("/pages/admin/cycleCount.jsp");
    }

    public Resolution saveScanned() {
        message = null;
        error = false;
        if ((hkBarcode != null) && (!(StringUtils.isEmpty(hkBarcode.trim())))) {
            List<SkuGroup> validSkuGroupList = findSkuGroup(hkBarcode);
            CycleCountItem validCycleCountItem = null;
            SkuGroup validSkuGroup = null;
            if (validSkuGroupList.size() > 0) {
                if (cycleCountPVImapString != null) {
                    cycleCountPviMap = getMapFromJsonString(cycleCountPVImapString);
                }
                for (SkuGroup skuGroup : validSkuGroupList) {
                    CycleCountItem cycleCountItemFromDb = cycleCountService.getCycleCountItem(cycleCount, skuGroup);
                    if (cycleCountItemFromDb == null) {
                        validSkuGroup = skuGroup;
                        break;
                    } else {
                        int pvi = cycleCountPviMap.get(cycleCountItemFromDb.getId());
                        if ((cycleCountItemFromDb.getScannedQty().intValue()) < pvi) {
                            cycleCountItemFromDb.setScannedQty(cycleCountItemFromDb.getScannedQty().intValue() + 1);
                            validCycleCountItem = cycleCountItemFromDb;
                            break;
                        } else {
                            if ((validSkuGroupList.indexOf(skuGroup)) == (validSkuGroupList.size() - 1)) {
                                cycleCountItemFromDb.setScannedQty(cycleCountItemFromDb.getScannedQty().intValue() + 1);
                                validCycleCountItem = cycleCountItemFromDb;
                            }

                        }

                    }
                }

                if (validCycleCountItem == null) {
                    validCycleCountItem = new CycleCountItem();
                    validCycleCountItem.setSkuGroup(validSkuGroup);
                    validCycleCountItem.setCycleCount(cycleCount);
                    validCycleCountItem.setScannedQty(1);
                    validCycleCountItem = cycleCountService.save(validCycleCountItem);
                    List<SkuItem> skuItemList = skuGroupService.getInStockSkuItems(validSkuGroup);
                    int pvi = 0;
                    if (skuItemList != null) {
                        pvi = skuItemList.size();
                    }
                    cycleCountPviMap.put(validCycleCountItem.getId(), pvi);
                    cycleCountPVImapString = getStringFromMap(cycleCountPviMap);
                } else {
                    cycleCountService.save(validCycleCountItem);
                }


            } else {
                hkBarcodeErrorsMap.put(hkBarcode, message);
            }
            if (message == null) {
                message = "Sucessfully Scanned for " + hkBarcode;
            }
        }

        return new RedirectResolution(CycleCountAction.class, "view").addParameter("message", message).addParameter("cycleCount", cycleCount.getId())
                .addParameter("cycleCountPVImapString", cycleCountPVImapString).addParameter("error", error);
    }

    private List<SkuGroup> findSkuGroup(String hkBarcode) {
        Warehouse warehouse = userService.getWarehouseForLoggedInUser();
        List<SkuGroup> skuGroupFromDb = skuGroupService.getSkuGroup(hkBarcode.trim(), warehouse.getId());
        List<SkuGroup> skuGroupListResult = new ArrayList<SkuGroup>();
        if (skuGroupFromDb != null && skuGroupFromDb.size() > 0) {
            for (SkuGroup skuGroupCheckBrand : skuGroupFromDb) {
                ProductVariant productVariant = skuGroupCheckBrand.getSku().getProductVariant();
                if (cycleCountType == 1) {
                    String brandInAudit = cycleCount.getBrandsToAudit().getBrand();
                    if (productVariant.getProduct().getBrand().equalsIgnoreCase(brandInAudit)) {
                        skuGroupListResult.add(skuGroupCheckBrand);
                    } else {
                        error = true;
                        message = hkBarcode + "  ->  does not belong to brand";
                        return skuGroupListResult;
                    }
                } else if (cycleCountType == 2) {
                    String productId = cycleCount.getProduct().getId();
                    if (productVariant.getProduct().getId().equalsIgnoreCase(productId)) {
                        skuGroupListResult.add(skuGroupCheckBrand);
                    } else {
                        error = true;
                        message = hkBarcode + "  ->  does not belong to Product";
                        return skuGroupListResult;
                    }

                } else {
                    String productVariantId = cycleCount.getProductVariant().getId();
                    if (productVariant.getId().equalsIgnoreCase(productVariantId)) {
                        skuGroupListResult.add(skuGroupCheckBrand);
                    } else {
                        error = true;
                        message = hkBarcode + "  ->  does not belong to Product Variant";
                        return skuGroupListResult;
                    }

                }
            }
        } else {
            error = true;
            message = hkBarcode + "  ->  Invalid Hk Barcode ";
        }
        return skuGroupListResult;
    }

    private List<SkuGroup> missedSkuGroupInScanning(CycleCount cycleCount) {
        List<SkuGroup> skuGroupList = new ArrayList<SkuGroup>();
        Warehouse warehouse = cycleCount.getWarehouse();
        if (cycleCount.getBrandsToAudit() != null) {
            List<SkuGroup> skuGroupListForBrand = skuGroupService.getCheckedInSkuGroup(cycleCount.getBrandsToAudit().getBrand(), cycleCount.getWarehouse(), null);
            if (skuGroupListForBrand != null) {
                skuGroupList.addAll(skuGroupListForBrand);
            }
        } else if (cycleCount.getProduct() != null) {
            List<SkuGroup> skuGroupListForProduct = skuGroupService.getCheckedInSkuGroup(null, cycleCount.getWarehouse(), cycleCount.getProduct());
            if (skuGroupListForProduct != null) {
                skuGroupList.addAll(skuGroupListForProduct);
            }
        } else {
            List<SkuGroup> skuGroupForPv = skuGroupService.getInStockSkuGroups(skuService.getSKU(cycleCount.getProductVariant(), warehouse));
            if (skuGroupForPv != null) {
                skuGroupList.addAll(skuGroupForPv);
            }
            skuGroupList.addAll(skuGroupForPv);
        }


        if (cycleCount.getCycleCountItems() != null) {
            for (CycleCountItem cycleCountItem : cycleCount.getCycleCountItems()) {
                if (skuGroupList.contains(cycleCountItem.getSkuGroup())) {
                    skuGroupList.remove(cycleCountItem.getSkuGroup());
                }
            }
        }
        if (skuGroupList.size() > 0) {
            for (SkuGroup skuGroup : skuGroupList) {
                List<SkuItem> skuItemList = skuGroupService.getInStockSkuItems(skuGroup);
                int pvi = 0;
                if (skuItemList != null) {
                    pvi = skuItemList.size();
                }
                skuGroupSystemInventoryMap.put(skuGroup.getId(), pvi);

            }
        }
        return skuGroupList;
    }


    public Resolution downloadSkuGroupMissedInScanning() {
        List<SkuGroup> skuGroupList = missedSkuGroupInScanning(cycleCount);
        Collections.sort(skuGroupList);
        Date todayDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String excelFilePath = adminDownloadsPath + "/cycleCountExcelFiles/" + "MissedBatches_" + cycleCount.getId() + "" + sdf.format(todayDate) + ".xls";
        final File excelFile = new File(excelFilePath);
        cycleCountHelper.generateSkuGroupNotScannedExcel(skuGroupList, excelFile, skuGroupSystemInventoryMap);
        return cycleCountHelper.download();
    }


    public Resolution save() {
        if (cycleCount.getCycleStatus().equals(EnumCycleCountStatus.InProgress.getId())) {
            cycleCount.setCycleStatus(EnumCycleCountStatus.RequestForApproval.getId());
            cycleCount = cycleCountService.save(cycleCount);
        }
        cycleCount = getCycleCount();
        cycleCountItems = cycleCount.getCycleCountItems();
        populateScannedPviVarianceMap(cycleCountItems);
        /*get all skuGroup  missed in Scanning   */
        missedSkuGroupList = missedSkuGroupInScanning(cycleCount);
        return new ForwardResolution("/pages/admin/CycleCountVariance.jsp");
    }

    private void populateScannedPviVarianceMap(List<CycleCountItem> cycleCountItems) {
        cycleCountPviMap = new HashMap<Long, Integer>();
        for (CycleCountItem cycleCountItem : cycleCountItems) {
            List<SkuItem> skuItemList = skuGroupService.getInStockSkuItems(cycleCountItem.getSkuGroup());
            int pvi = 0;
            if (skuItemList != null) {
                pvi = skuItemList.size();
            }
            cycleCountPviMap.put(cycleCountItem.getId(), pvi);
        }

    }

    public Resolution saveVariance() {
        for (CycleCountItem cycleCountItem : cycleCountItems) {
            cycleCountService.save(cycleCountItem);
        }
        cycleCount.setCycleStatus(EnumCycleCountStatus.Approved.getId());
        cycleCount = cycleCountService.save(cycleCount);
        return new RedirectResolution(CycleCountAction.class, "save").addParameter("cycleCount", cycleCount.getId());
    }

    public Resolution closeCycleCount() {
        cycleCount.setCycleStatus(EnumCycleCountStatus.Closed.getId());
        if (cycleCountType != null && cycleCountType == 1) {
            cycleCount.getBrandsToAudit().setAuditStatus(EnumAuditStatus.Done.getId());
        }
        cycleCount = cycleCountService.save(cycleCount);
        return new RedirectResolution(CycleCountAction.class, "pre");
    }


    public Resolution generateReconAddExcel() {
        List<CycleCountItem> cycleCountItems = cycleCount.getCycleCountItems();
        List<CycleCountItem> cycleCountItemsForAddRecon = new ArrayList<CycleCountItem>();
        populateScannedPviVarianceMap(cycleCountItems);
        for (CycleCountItem cycleCountItem : cycleCountItems) {
            int pvi = cycleCountPviMap.get(cycleCountItem.getId());
            int scannedQty = cycleCountItem.getScannedQty();
            if ((pvi - scannedQty) < 0) {
                cycleCountItemsForAddRecon.add(cycleCountItem);
            }
        }
        Date todayDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String excelFilePath = adminDownloadsPath + "/cycleCountExcelFiles/" + "CycleCount_" + cycleCount.getId() + "_RvAdd" + sdf.format(todayDate) + ".xls";
        final File excelFile = new File(excelFilePath);
        cycleCountHelper.generateReconVoucherAddExcel(cycleCountItemsForAddRecon, excelFile, cycleCountPviMap);
        return cycleCountHelper.download();

    }


    public Resolution generateCompleteCycleExcel() {
        List<CycleCountItem> cycleCountItems = cycleCount.getCycleCountItems();
        populateScannedPviVarianceMap(cycleCountItems);
       List<SkuGroup> skuGroupList =  missedSkuGroupInScanning(cycleCount);
        Date todayDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String excelFilePath = adminDownloadsPath + "/cycleCountExcelFiles/" + "CompleteCycleCount_" + cycleCount.getId() + "_Variance" + sdf.format(todayDate) + ".xls";
        final File excelFile = new File(excelFilePath);
        cycleCountHelper.generateCompleteCycleCountExcel(cycleCountItems, excelFile, cycleCountPviMap ,skuGroupList, skuGroupSystemInventoryMap);
        return cycleCountHelper.download();
    }


    //cycle count by uploading notepad.
    public Resolution uploadCycleCountNotepad() {
        if (fileBean == null || (!(fileBean.getContentType().equals("text/plain")))) {
            error = true;
            message = "Upload notepad text file only";
            return new RedirectResolution(CycleCountAction.class, "view").addParameter("cycleCount", cycleCount.getId()).addParameter("message", message).addParameter("error", error);
        }

        try {
            hkBarcodeErrorsMap = new HashMap<String, String>();
            String excelFilePath = adminDownloadsPath + "/cycleCountExcelFiles/" + System.currentTimeMillis() + ".txt";
            File excelFile = new File(excelFilePath);
            excelFile.getParentFile().mkdirs();
            fileBean.save(excelFile);
            Map<String, Integer> hkBarcodeQtyMap = cycleCountHelper.readCycleCountNotepad(excelFile);
            cycleCountPviMap = new HashMap<Long, Integer>();
            for (String hkbarcodeFromNotepad : hkBarcodeQtyMap.keySet()) {
                List<SkuGroup> validSkuGroupList = findSkuGroup(hkbarcodeFromNotepad);
                if (validSkuGroupList != null && validSkuGroupList.size() > 0) {
                    int notepadScannedQty = hkBarcodeQtyMap.get(hkbarcodeFromNotepad);
                    for (SkuGroup skuGroup : validSkuGroupList) {
                        List<SkuItem> skuItemList = skuGroupService.getInStockSkuItems(skuGroup);
                        CycleCountItem cycleCountItemFromDb = cycleCountService.getCycleCountItem(cycleCount, skuGroup);
                        int pviQty = 0;
                        if (skuItemList != null && skuItemList.size() > 0) {
                            pviQty = skuItemList.size();
                        }
                        if (cycleCountItemFromDb == null) {
                            CycleCountItem cycleCountItemNew = new CycleCountItem();
                            cycleCountItemNew.setSkuGroup(skuGroup);
                            cycleCountItemNew.setCycleCount(cycleCount);
                            if (pviQty > 0) {
                                if ((validSkuGroupList.indexOf(skuGroup)) == (validSkuGroupList.size() - 1)) {
                                    cycleCountItemNew.setScannedQty(notepadScannedQty);
                                } else {
                                    if (notepadScannedQty >= pviQty) {
                                        cycleCountItemNew.setScannedQty(pviQty);
                                        notepadScannedQty = notepadScannedQty - pviQty;
                                    } else {
                                        cycleCountItemNew.setScannedQty(notepadScannedQty);
                                    }

                                }
                            } else {
                                if ((validSkuGroupList.indexOf(skuGroup)) == (validSkuGroupList.size() - 1)) {
                                    cycleCountItemNew.setScannedQty(notepadScannedQty);
                                } else {
                                    continue;
                                }
                            }
                            cycleCountItemNew = cycleCountService.save(cycleCountItemNew);
                            cycleCountPviMap.put(cycleCountItemNew.getId(), pviQty);
                        } else {
                            int alreadySavedScannedQty = cycleCountItemFromDb.getScannedQty();
                            int fillPviQty = pviQty - alreadySavedScannedQty;
                            /* Handles case of multiple Skugroup for same barcode */
                            if (fillPviQty > 0) {
                                /* check if this the last skuGroup */
                                if ((validSkuGroupList.indexOf(skuGroup)) == (validSkuGroupList.size() - 1)) {
                                    cycleCountItemFromDb.setScannedQty(notepadScannedQty + alreadySavedScannedQty);
                                } else {
                                    if (notepadScannedQty >= fillPviQty) {
                                        cycleCountItemFromDb.setScannedQty(pviQty);
                                        notepadScannedQty = notepadScannedQty - fillPviQty;
                                    } else {
                                        cycleCountItemFromDb.setScannedQty(notepadScannedQty + alreadySavedScannedQty);
                                    }

                                }

                            } else {
                                if ((validSkuGroupList.indexOf(skuGroup)) == (validSkuGroupList.size() - 1)) {
                                    cycleCountItemFromDb.setScannedQty(notepadScannedQty + alreadySavedScannedQty);
                                }
                            }

                            cycleCountService.save(cycleCountItemFromDb);
                        }


                    }

                } else {
                    hkBarcodeErrorsMap.put(hkbarcodeFromNotepad, message);
                }

            }
            if (hkBarcodeErrorsMap.size() > 0) {
                error = true;
                message = "";
                for (String hkBarcode : hkBarcodeErrorsMap.keySet()) {
                    message = message + hkBarcodeErrorsMap.get(hkBarcode) + "   ,   ";
                }
            }
        } catch (IOException e) {
            addRedirectAlertMessage(new SimpleMessage(e.getMessage()));
            return new ForwardResolution("/pages/admin/cycleCount.jsp");
        }
        return new RedirectResolution(CycleCountAction.class, "view").addParameter("cycleCount", cycleCount.getId()).addParameter("message", message).addParameter("error", error);
    }


    private String getStringFromMap(Map<Long, Integer> cycleCountPVImap) {
        StringBuilder stringBuilder = new StringBuilder();
        JSONObject.appendJSONMap(cycleCountPVImap, stringBuilder);
        return stringBuilder.toString();
    }

    private Map<Long, Integer> getMapFromJsonString(String jsonString) {
        Type type = new TypeToken<Map<Long, Integer>>() {
        }.getType();
        Gson gson = new Gson();
        Map<Long, Integer> mapFromString = gson.fromJson(jsonString, type);
        return mapFromString;

    }

    public List<CycleCountItem> getCycleCountItems() {
        return cycleCountItems;
    }

    public void setCycleCountItems(List<CycleCountItem> cycleCountItems) {
        this.cycleCountItems = cycleCountItems;
    }

    public String getHkBarcode() {
        return hkBarcode;
    }

    public void setHkBarcode(String hkBarcode) {
        this.hkBarcode = hkBarcode;
    }

    public CycleCount getCycleCount() {
        return cycleCount;
    }

    public void setCycleCount(CycleCount cycleCount) {
        this.cycleCount = cycleCount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<Long, Integer> getCycleCountPviMap() {
        return cycleCountPviMap;
    }

    public void setCycleCountPviMap(Map<Long, Integer> cycleCountPviMap) {
        this.cycleCountPviMap = cycleCountPviMap;
    }

    public Map<String, String> getHkBarcodeErrorsMap() {
        return hkBarcodeErrorsMap;
    }

    public void setHkBarcodeErrorsMap(Map<String, String> hkBarcodeErrorsMap) {
        this.hkBarcodeErrorsMap = hkBarcodeErrorsMap;
    }

    public String getCycleCountPVImapString() {
        return cycleCountPVImapString;
    }

    public void setCycleCountPVImapString(String cycleCountPVImapString) {
        this.cycleCountPVImapString = cycleCountPVImapString;
    }

    public boolean getError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }


    public void setFileBean(FileBean fileBean) {
        this.fileBean = fileBean;
    }

    public int getPerPageDefault() {
        return defaultPerPage;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }


    public String getAuditorLogin() {
        return auditorLogin;
    }

    public void setAuditorLogin(String auditorLogin) {
        this.auditorLogin = auditorLogin;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }


    public int getPageCount() {
        return cycleCountPage == null ? 0 : cycleCountPage.getTotalPages();
    }

    public int getResultCount() {
        return cycleCountPage == null ? 0 : cycleCountPage.getTotalResults();
    }

    public Set<String> getParamSet() {
        HashSet<String> params = new HashSet<String>();
        params.add("brand");
        params.add("auditorLogin");
        params.add("startDate");
        params.add("endDate");
        return params;
    }


    public List<CycleCount> getCycleCountList() {
        return cycleCountList;
    }

    public void setCycleCountList(List<CycleCount> cycleCountList) {
        this.cycleCountList = cycleCountList;
    }

    public String getAuditBy() {
        return auditBy;
    }

    public void setAuditBy(String auditBy) {
        this.auditBy = auditBy;
    }

    public Integer getCycleCountType() {
        return cycleCountType;
    }

    public void setCycleCountType(Integer cycleCountType) {
        this.cycleCountType = cycleCountType;
    }

    public Long getCycleCountStatus() {
        return cycleCountStatus;
    }

    public void setCycleCountStatus(Long cycleCountStatus) {
        this.cycleCountStatus = cycleCountStatus;
    }

    public List<SkuGroup> getMissedSkuGroupList() {
        return missedSkuGroupList;
    }

    public void setMissedSkuGroupList(List<SkuGroup> missedSkuGroupList) {
        this.missedSkuGroupList = missedSkuGroupList;
    }

    public Map<Long, Integer> getSkuGroupSystemInventoryMap() {
        return skuGroupSystemInventoryMap;
    }

    public void setSkuGroupSystemInventoryMap(Map<Long, Integer> skuGroupSystemInventoryMap) {
        this.skuGroupSystemInventoryMap = skuGroupSystemInventoryMap;
    }
}
