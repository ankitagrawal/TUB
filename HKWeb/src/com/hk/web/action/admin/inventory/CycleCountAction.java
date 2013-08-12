package com.hk.web.action.admin.inventory;


import com.akube.framework.stripes.action.BasePaginatedAction;

import com.akube.framework.dao.Page;
import com.hk.admin.dto.inventory.CreateInventoryFileDto;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.domain.cycleCount.CycleCountItem;
import com.hk.domain.cycleCount.CycleCount;
import com.hk.domain.warehouse.Warehouse;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.sku.SkuItemOwner;
import com.hk.domain.sku.SkuItemStatus;
import com.hk.domain.core.JSONObject;

import com.hk.domain.user.User;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.admin.pact.service.inventory.CycleCountService;

import com.hk.admin.util.helper.CycleCountHelper;
import com.hk.pact.service.inventory.SkuGroupService;
import com.hk.pact.service.UserService;
import com.hk.pact.service.catalog.ProductService;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.constants.inventory.EnumCycleCountStatus;
import com.hk.constants.inventory.EnumAuditStatus;
import com.hk.constants.core.Keys;
import com.hk.constants.sku.EnumSkuItemOwner;
import com.hk.constants.sku.EnumSkuItemStatus;
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
import org.jsoup.helper.StringUtil;


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
    ProductVariantService productVariantService;
    @Autowired
    SkuService skuService;
    @Autowired
    AdminInventoryService adminInventoryService;


    @Value("#{hkEnvProps['" + Keys.Env.adminDownloads + "']}")
    String adminDownloadsPath;

    private List<CycleCountItem> cycleCountItems = new ArrayList<CycleCountItem>();
    private List<CycleCount> cycleCountList;
    private CycleCount cycleCount;
    private String hkBarcode;
    private String message;
    private Map<String, String> hkBarcodeErrorsMap = new HashMap<String, String>();
    private Map<Long, Integer> cycleCountPviMap = new HashMap<Long, Integer>();
    private Map<Long, Integer> missedSkuGroupSystemInventoryMap = new HashMap<Long, Integer>();
    private Map<Long, Integer> skuItemScannedQtyMap = new HashMap<Long, Integer>();
    private Map<Long, Integer> skuItemSystemQtyMap = new HashMap<Long, Integer>();
    private String cycleCountPVImapString;
    private boolean error = false;
    private Page cycleCountPage;
    private String auditorLogin;
    private Date startDate;
    private Date endDate;
    private Integer defaultPerPage = 20;
    FileBean fileBean;
    private String auditBy;
    private Integer cycleCountType;
    private Long cycleCountStatus;
    private List<SkuGroup> missedSkuGroupList = new ArrayList<SkuGroup>();
    private List<SkuGroup> scannedSkuItemGroupList = new ArrayList<SkuGroup>();
    private SkuItem skuItem;
    private String productVariantId;


    public Resolution directToCycleCountPage() {
        return view();

    }


    public Resolution createCycleCount() {
        return new RedirectResolution("/pages/admin/createCycleCount.jsp");
    }


    private boolean ifBrandProductCycleCountInProgress(String brandEntered) {
        Warehouse warehouse = userService.getWarehouseForLoggedInUser();
        /* if product of same brand in audit pending*/
        List<Product> productList = productService.getAllProductByBrand(brandEntered);
        if (productList != null) {
            for (Product product : productList) {
                List<CycleCount> cycleCounts = cycleCountService.getCycleCountInProgress(null, product, null, warehouse);
                if (cycleCounts != null && cycleCounts.size() > 0) {
                    message = "OOops !!! ERROR :  Cycle Count of Product : " + product.getId() + " of same brand : " + brandEntered + " Already in Progress. Close if first";
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
                    message = "OOops !!! ERROR :  Cycle count of PV Id : " + productVariant.getId() + " of same brand already in Progress. Close it first";
                    return true;
                }
            }
        }
        return false;
    }


    private boolean validateBrand() {
        boolean doesBrandExist = productService.doesBrandExist(auditBy);
        User auditor = getPrincipalUser();
        Warehouse warehouse = auditor.getSelectedWarehouse();
        if (!doesBrandExist) {
            message = "Invalid Brand";
            return false;
        } else {
            /* Cycle Count of brand , CC of product and CC of product variant related to brand should not in pending status */
            List<CycleCount> cycleCounts = cycleCountService.getCycleCountInProgress(auditBy, null, null, userService.getWarehouseForLoggedInUser());
            if (cycleCounts != null && cycleCounts.size() > 0) {
                message = "Brand's Cycle Count Already In progress in " + warehouse.getCity();
                return false;
            }
            boolean brandsProductCCInProgress = ifBrandProductCycleCountInProgress(auditBy);
            if (brandsProductCCInProgress) {
                return false;
            }
        }
        return true;
    }


    public Resolution saveCycleCount() {
        boolean returnToCreateCycleCountPage = false;
        boolean returnToCycleCountListPage = false;
        if (cycleCount == null) {
            cycleCount = new CycleCount();
        }
        /*CC by brand */
        if (cycleCountType == 1) {
            boolean validBrand = validateBrand();
            if (validBrand) {
                cycleCount.setBrand(auditBy.trim());
            } else {
                returnToCreateCycleCountPage = true;
            }

        } else if (cycleCountType == 2) {
            /* CC by product */
            Product product = productService.getProductById(auditBy);
            if (product == null) {
                returnToCreateCycleCountPage = true;
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
            /* CC by product  variant*/
            ProductVariant productVariant = productVariantService.getVariantById(auditBy);
            if (productVariant == null) {
                returnToCreateCycleCountPage = true;
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
        if (returnToCreateCycleCountPage) {
            return new ForwardResolution("/pages/admin/createCycleCount.jsp");
        }
        if (returnToCycleCountListPage) {
            addRedirectAlertMessage(new SimpleMessage(message));
            return new RedirectResolution(CycleCountAction.class, "pre");
        }
        addRedirectAlertMessage(new SimpleMessage("Changes Saved"));
        cycleCount = cycleCountService.createAndSaveNewCycleCount(cycleCount);
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
            if (cycleCountItem.getSkuGroup() != null) {
                if (!(cycleCountPviMap.containsKey(cycleCountItem.getId()))) {
                    //new entry added by another auditor
                	List<SkuItemStatus> skuItemStatusList = new ArrayList<SkuItemStatus>();
                    skuItemStatusList.add( EnumSkuItemStatus.Checked_IN.getSkuItemStatus());
                    skuItemStatusList.add( EnumSkuItemStatus.BOOKED.getSkuItemStatus());
                    skuItemStatusList.add( EnumSkuItemStatus.TEMP_BOOKED.getSkuItemStatus());
                    newEntryInMap++;
                    List<SkuItem> skuItemList = skuGroupService.getInStockSkuItems(cycleCountItem.getSkuGroup(), skuItemStatusList);
                    int pvi = 0;
                    if (skuItemList != null) {
                        pvi = skuItemList.size();
                    }
                    cycleCountPviMap.put(cycleCountItem.getId(), pvi);
                }
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
    	List<SkuItemStatus> skuItemStatusList = new ArrayList<SkuItemStatus>();
        skuItemStatusList.add( EnumSkuItemStatus.Checked_IN.getSkuItemStatus());
        skuItemStatusList.add( EnumSkuItemStatus.BOOKED.getSkuItemStatus());
        skuItemStatusList.add( EnumSkuItemStatus.TEMP_BOOKED.getSkuItemStatus());
        message = null;
        error = false;
        if ((hkBarcode != null) && (!(StringUtils.isEmpty(hkBarcode.trim())))) {
            SkuItem skuItem = findBySkuItem(hkBarcode);
            if (skuItem != null) {
                CycleCountItem existingCycleCountItem = cycleCountService.getCycleCountItem(cycleCount, null, skuItem);
                if (existingCycleCountItem != null) {
                    message = hkBarcode + " --> already Scanned";
                    return new RedirectResolution(CycleCountAction.class, "view").addParameter("cycleCount", cycleCount.getId()).addParameter("message", message).addParameter("error", true);
                } else {
                    SkuItem validSkuItem = getValidSkuItem(skuItem);
                    if (validSkuItem == null) {
                        message = hkBarcode + " --> Invalid Barcode for this Audit";
                        return new RedirectResolution(CycleCountAction.class, "view").addParameter("cycleCount", cycleCount.getId()).addParameter("message", message).addParameter("error", true);
                    }
                    CycleCountItem validCycleCountItem = cycleCountService.createCycleCountItem(null, skuItem, cycleCount, 1);
                    cycleCountService.save(validCycleCountItem);
                    message = "Sucessfully Scanned for:" + hkBarcode;
                    return new RedirectResolution(CycleCountAction.class, "view").addParameter("cycleCount", cycleCount.getId()).addParameter("message", message).addParameter("error", false);
                }
            } else {

                List<SkuGroup> validSkuGroupList = findSkuGroup(hkBarcode);
                CycleCountItem validCycleCountItem = null;
                SkuGroup validSkuGroup = null;
                if (validSkuGroupList.size() > 0) {
                    if (cycleCountPVImapString != null) {
                        cycleCountPviMap = getMapFromJsonString(cycleCountPVImapString);
                    }
                    for (SkuGroup skuGroup : validSkuGroupList) {
                        CycleCountItem cycleCountItemFromDb = cycleCountService.getCycleCountItem(cycleCount, skuGroup, null);
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
                        validCycleCountItem = cycleCountService.createCycleCountItem(validSkuGroup, null, cycleCount, 1);
                        validCycleCountItem = cycleCountService.save(validCycleCountItem);
                        List<SkuItem> skuItemList = skuGroupService.getInStockSkuItems(validSkuGroup, skuItemStatusList);
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
                    message = "Successfully Scanned for " + hkBarcode;
                }
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
                    String brandInAudit = cycleCount.getBrand();
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
        List<CreateInventoryFileDto> createInventoryFileDtoList = new ArrayList<CreateInventoryFileDto>();
        
        List<SkuItemStatus> skuItemStatusList = new ArrayList<SkuItemStatus>();
        skuItemStatusList.add( EnumSkuItemStatus.Checked_IN.getSkuItemStatus());
        skuItemStatusList.add( EnumSkuItemStatus.BOOKED.getSkuItemStatus());
        skuItemStatusList.add( EnumSkuItemStatus.TEMP_BOOKED.getSkuItemStatus());
        List<SkuItemOwner> skuItemOwnerList = new ArrayList<SkuItemOwner>();
        skuItemOwnerList.add(EnumSkuItemOwner.SELF.getSkuItemOwnerStatus());
        
        
        if (cycleCount.getBrand() != null) {
            String brand = cycleCount.getBrand();
            createInventoryFileDtoList = adminInventoryService.getCheckedInSkuGroup(brand, warehouse, null, null, skuItemStatusList, skuItemOwnerList);

        } else if (cycleCount.getProduct() != null) {
            createInventoryFileDtoList = adminInventoryService.getCheckedInSkuGroup(null, warehouse, cycleCount.getProduct(), null, skuItemStatusList, skuItemOwnerList);

        } else {
            createInventoryFileDtoList = adminInventoryService.getCheckedInSkuGroup(null, warehouse, null, cycleCount.getProductVariant(), skuItemStatusList, skuItemOwnerList);

        }

        List<CycleCountItem> cycleCountItemList = cycleCount.getCycleCountItems();
        List<SkuGroup> scannedSkuGroupList = new ArrayList<SkuGroup>();

        List<SkuGroup> originalScannedSkuItemGroupList = new ArrayList<SkuGroup>();

        if (cycleCountItemList != null) {
            for (CycleCountItem cycleCountItem : cycleCountItemList) {
                if (cycleCountItem.getSkuGroup() != null) {
                    scannedSkuGroupList.add(cycleCountItem.getSkuGroup());
                }

                if (cycleCountItem.getSkuItem() != null) {
                    originalScannedSkuItemGroupList.add(cycleCountItem.getSkuItem().getSkuGroup());
                }
            }
        }

        for (SkuGroup skuGroup : originalScannedSkuItemGroupList) {
            if (!skuItemScannedQtyMap.containsKey(skuGroup.getId())) {
                skuItemScannedQtyMap.put(skuGroup.getId(), 1);
            } else {
                int value = skuItemScannedQtyMap.get(skuGroup.getId());
                skuItemScannedQtyMap.put(skuGroup.getId(), value + 1);
            }

        }
        //    Adding Item level scanned sku groups to get batches that were never scanned.
        Set<SkuGroup> ScannedSkuItemGroupSet = new HashSet<SkuGroup>(originalScannedSkuItemGroupList);
        scannedSkuItemGroupList = new ArrayList<SkuGroup>(ScannedSkuItemGroupSet);

        scannedSkuGroupList.addAll(scannedSkuItemGroupList);
        for (CreateInventoryFileDto createInventoryFileDto : createInventoryFileDtoList) {
            SkuGroup skuGroup = createInventoryFileDto.getSkuGroup();
            int pvi = createInventoryFileDto.getSumQty().intValue();

            if (scannedSkuItemGroupList.contains(skuGroup)) {
                skuItemSystemQtyMap.put(skuGroup.getId(), pvi);
            }
            if (scannedSkuGroupList.contains(skuGroup)) {
                continue;
            }
            skuGroupList.add(skuGroup);
            missedSkuGroupSystemInventoryMap.put(skuGroup.getId(), pvi);

        }
        if (skuGroupList.size() > 0) {
            Collections.sort(skuGroupList);
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
        cycleCountHelper.generateSkuGroupNotScannedExcel(skuGroupList, excelFile, missedSkuGroupSystemInventoryMap);
        return cycleCountHelper.download();
    }


    public Resolution save() {
        if (cycleCount.getCycleStatus().equals(EnumCycleCountStatus.InProgress.getId())) {
            cycleCount.setCycleStatus(EnumCycleCountStatus.RequestForApproval.getId());
            cycleCount = cycleCountService.save(cycleCount);
        }
        cycleCount = getCycleCount();
        cycleCountItems = cycleCount.getCycleCountItems();
        populateScannedSkuGroupSystemQtyMap(cycleCountItems);
        /*get all skuGroup  missed in Scanning   */
        missedSkuGroupList = missedSkuGroupInScanning(cycleCount);
        return new ForwardResolution("/pages/admin/CycleCountVariance.jsp");
    }

    private void populateScannedSkuGroupSystemQtyMap(List<CycleCountItem> cycleCountItems) {
    	List<SkuItemStatus> skuItemStatusList = new ArrayList<SkuItemStatus>();
        skuItemStatusList.add( EnumSkuItemStatus.Checked_IN.getSkuItemStatus());
        skuItemStatusList.add( EnumSkuItemStatus.BOOKED.getSkuItemStatus());
        skuItemStatusList.add( EnumSkuItemStatus.TEMP_BOOKED.getSkuItemStatus());
        cycleCountPviMap = new HashMap<Long, Integer>();
        for (CycleCountItem cycleCountItem : cycleCountItems) {
            if (cycleCountItem.getSkuGroup() != null) {
            	
                List<SkuItem> skuItemList = skuGroupService.getInStockSkuItems(cycleCountItem.getSkuGroup(), skuItemStatusList);
                int pvi = 0;
                if (skuItemList != null) {
                    pvi = skuItemList.size();
                }
                cycleCountPviMap.put(cycleCountItem.getId(), pvi);
            }
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
        cycleCount = cycleCountService.save(cycleCount);
        return new RedirectResolution(CycleCountAction.class, "pre");
    }


    public Resolution generateReconAddExcel() {

        List<CycleCountItem> cycleCountItems = cycleCount.getCycleCountItems();
        List<CycleCountItem> cycleCountItemsForAddRecon = new ArrayList<CycleCountItem>();
        populateScannedSkuGroupSystemQtyMap(cycleCountItems);
        for (CycleCountItem cycleCountItem : cycleCountItems) {
            if (cycleCountItem.getSkuGroup() != null) {
                int pvi = cycleCountPviMap.get(cycleCountItem.getId());
                int scannedQty = cycleCountItem.getScannedQty();
                if ((pvi - scannedQty) < 0) {
                    cycleCountItemsForAddRecon.add(cycleCountItem);
                }
            }
        }
        Date todayDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String excelFilePath = adminDownloadsPath + "/cycleCountExcelFiles/" + "CycleCount_" + cycleCount.getId() + "_RvAdd" + sdf.format(todayDate) + ".xls";
        final File excelFile = new File(excelFilePath);
        cycleCountHelper.generateReconVoucherAddExcel(cycleCountItemsForAddRecon, excelFile, cycleCountPviMap);
        return cycleCountHelper.download();
    }


    public Resolution generateSubtractRVExcel() {
        List<CycleCountItem> cycleCountItems = cycleCount.getCycleCountItems();
        populateScannedSkuGroupSystemQtyMap(cycleCountItems);
        List<SkuGroup> skuGroupNeverScanned = missedSkuGroupInScanning(cycleCount);
        Date todayDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String excelFilePath = adminDownloadsPath + "/cycleCountExcelFiles/" + "CycleCount_" + cycleCount.getId() + "_RvSubtract" + sdf.format(todayDate) + ".xls";
        final File excelFile = new File(excelFilePath);
        cycleCountHelper.generateCycleCountItemsCombiningSkuGroupAndSkuItemScanning(cycleCount, cycleCountPviMap, skuGroupNeverScanned, missedSkuGroupSystemInventoryMap, excelFile);
        return cycleCountHelper.download();
    }


    public Resolution generateCompleteCycleExcel() {
        List<CycleCountItem> cycleCountItems = cycleCount.getCycleCountItems();
        populateScannedSkuGroupSystemQtyMap(cycleCountItems);
        List<SkuGroup> skuGroupList = missedSkuGroupInScanning(cycleCount);
        Date todayDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String excelFilePath = adminDownloadsPath + "/cycleCountExcelFiles/" + "CompleteCycleCount_" + cycleCount.getId() + "_Variance" + sdf.format(todayDate) + ".xls";
        final File excelFile = new File(excelFilePath);
        cycleCountHelper.generateCompleteCycleCountExcel(cycleCountItems, excelFile, cycleCountPviMap, skuGroupList, missedSkuGroupSystemInventoryMap, scannedSkuItemGroupList);
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
                SkuItem skuItem = findBySkuItem(hkbarcodeFromNotepad);
                if (skuItem != null) {
                    CycleCountItem existingCycleCountItem = cycleCountService.getCycleCountItem(cycleCount, null, skuItem);
                    if (existingCycleCountItem != null) {
                        message = hkbarcodeFromNotepad + "-> Already Scanned Item";
                        hkBarcodeErrorsMap.put(hkbarcodeFromNotepad, message);
                    } else {
                        SkuItem validSkuItem = getValidSkuItem(skuItem);
                        if (validSkuItem != null) {
                            CycleCountItem cycleCountItemNew = new CycleCountItem();
                            cycleCountItemNew.setSkuGroup(null);
                            cycleCountItemNew.setCycleCount(cycleCount);
                            cycleCountItemNew.setScannedQty(1);
                            cycleCountItemNew.setSkuItem(skuItem);
                            cycleCountService.save(cycleCountItemNew);

                        } else {
                            message = hkbarcodeFromNotepad + "-> Invalid Item barcode for this Audit";
                            hkBarcodeErrorsMap.put(hkbarcodeFromNotepad, message);
                        }
                    }
                } else {
                    List<SkuGroup> validSkuGroupList = findSkuGroup(hkbarcodeFromNotepad);
                    if (validSkuGroupList != null && validSkuGroupList.size() > 0) {
                        int notepadScannedQty = hkBarcodeQtyMap.get(hkbarcodeFromNotepad);
                        for (SkuGroup skuGroup : validSkuGroupList) {
                            if (notepadScannedQty > 0) {
                            	List<SkuItemStatus> skuItemStatusList = new ArrayList<SkuItemStatus>();
                                skuItemStatusList.add( EnumSkuItemStatus.Checked_IN.getSkuItemStatus());
                                skuItemStatusList.add( EnumSkuItemStatus.BOOKED.getSkuItemStatus());
                                skuItemStatusList.add( EnumSkuItemStatus.TEMP_BOOKED.getSkuItemStatus());
                                List<SkuItem> skuItemList = skuGroupService.getInStockSkuItems(skuGroup, skuItemStatusList);
                                CycleCountItem cycleCountItemFromDb = cycleCountService.getCycleCountItem(cycleCount, skuGroup, null);
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
                                                notepadScannedQty = 0;
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
                                                notepadScannedQty = 0;
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
                        }

                    } else {
                        hkBarcodeErrorsMap.put(hkbarcodeFromNotepad, message);
                    }

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

    private SkuItem findBySkuItem(String hkBarcode) {
        //SkuItem skuItem = skuGroupService.getSkuItemByBarcode(hkBarcode.trim(), userService.getWarehouseForLoggedInUser().getId(), EnumSkuItemStatus.Checked_IN.getId());
    	List<SkuItemStatus> skuItemStatusList = new ArrayList<SkuItemStatus>();
        skuItemStatusList.add( EnumSkuItemStatus.Checked_IN.getSkuItemStatus());
        skuItemStatusList.add( EnumSkuItemStatus.BOOKED.getSkuItemStatus());
        skuItemStatusList.add( EnumSkuItemStatus.TEMP_BOOKED.getSkuItemStatus());

        List<SkuItemOwner> skuItemOwnerList = new ArrayList<SkuItemOwner>();
        skuItemOwnerList.add(EnumSkuItemOwner.SELF.getSkuItemOwnerStatus());
        SkuItem skuItem = skuGroupService.getSkuItemByBarcode(hkBarcode.trim(), userService.getWarehouseForLoggedInUser().getId(), skuItemStatusList, skuItemOwnerList);
        return skuItem;

    }

    private SkuItem getValidSkuItem(SkuItem skuItem) {
        ProductVariant productVariant = skuItem.getSkuGroup().getSku().getProductVariant();
        switch (cycleCountType) {
            case 1:
                String brandInAudit = cycleCount.getBrand();
                return productVariant.getProduct().getBrand().equalsIgnoreCase(brandInAudit) ? skuItem : null;

            case 2:
                String productId = cycleCount.getProduct().getId();
                return productVariant.getProduct().getId().equalsIgnoreCase(productId) ? skuItem : null;

            case 3:
                String productVariantId = cycleCount.getProductVariant().getId();
                return productVariant.getId().equalsIgnoreCase(productVariantId) ? skuItem : null;

            default:
                return null;
        }
    }


//    public Resolution deleteScannedSkuItem() {
//
//        if (cycleCount == null) {
//            addRedirectAlertMessage(new SimpleMessage("Invalid Cycle Count"));
//            return new RedirectResolution(CycleCountAction.class, "save").addParameter("cycleCount", cycleCount.getId());
//        }
//
//        if (StringUtil.isBlank(hkBarcode)) {
//            addRedirectAlertMessage(new SimpleMessage("Barcode cannot be blank"));
//            return new RedirectResolution(CycleCountAction.class, "save").addParameter("cycleCount", cycleCount.getId());
//        }
//
//        User loggedOnUser = null;
//        if (getPrincipal() != null) {
//            loggedOnUser = getUserService().getUserById(getPrincipal().getId());
//        }
//        SkuItem skuItem = findBySkuItem(hkBarcode.trim());
//        if (skuItem == null) {
//            addRedirectAlertMessage(new SimpleMessage(" Invalid Barcode"));
//            return new RedirectResolution(CycleCountAction.class, "save").addParameter("cycleCount", cycleCount.getId());
//        }
//        CycleCountItem existingCycleCountItem = cycleCountService.getCycleCountItem(cycleCount, null, skuItem);
//        if (existingCycleCountItem == null) {
//            addRedirectAlertMessage(new SimpleMessage( skuItem.getBarcode() + ":Invalid  Item to delete"));
//            return new RedirectResolution(CycleCountAction.class, "save").addParameter("cycleCount", cycleCount.getId());
//        }
//        cycleCountService.removeScannedSkuItemFromCycleCountItem(cycleCount, skuItem);
//        addRedirectAlertMessage(new SimpleMessage(skuItem.getBarcode() + " : has been deleted."));
////        return new RedirectResolution(ViewSkuItemAction.class, "pre").addParameter("cycleCount", cycleCount).addParameter("skuGroup", skuItem.getSkuGroup()).addParameter("entityId", EnumSkuItemTransferMode.CYCLE_COUNT.getId());
//        return new RedirectResolution(CycleCountAction.class, "save").addParameter("cycleCount", cycleCount.getId()).addParameter("messageColor", "green");
//    }


    public Resolution deleteScannedSkuItem() {

        if (cycleCount == null) {
            addRedirectAlertMessage(new SimpleMessage("Invalid Cycle Count"));
            return new RedirectResolution(CycleCountAction.class, "save").addParameter("cycleCount", cycleCount.getId());
        }

        if (StringUtil.isBlank(hkBarcode)) {
            addRedirectAlertMessage(new SimpleMessage("Barcode cannot be blank"));
            return new RedirectResolution(CycleCountAction.class, "save").addParameter("cycleCount", cycleCount.getId());
        }

        User loggedOnUser = null;
        if (getPrincipal() != null) {
            loggedOnUser = getUserService().getUserById(getPrincipal().getId());
        }
        SkuItem skuItem = findBySkuItem(hkBarcode.trim());
        if (skuItem != null) {

            CycleCountItem existingCycleCountItem = cycleCountService.getCycleCountItem(cycleCount, null, skuItem);
            if (existingCycleCountItem == null) {
                addRedirectAlertMessage(new SimpleMessage(skuItem.getBarcode() + ":Invalid  Item to delete"));
                return new RedirectResolution(CycleCountAction.class, "save").addParameter("cycleCount", cycleCount.getId());
            }
            cycleCountService.removeScannedSkuItemFromCycleCountItem(cycleCount, skuItem);
            addRedirectAlertMessage(new SimpleMessage(skuItem.getBarcode() + " : has been deleted."));

        } else {
        	List<SkuItemStatus> skuItemStatusList = new ArrayList<SkuItemStatus>();
            skuItemStatusList.add( EnumSkuItemStatus.Checked_IN.getSkuItemStatus());
            skuItemStatusList.add( EnumSkuItemStatus.BOOKED.getSkuItemStatus());
            skuItemStatusList.add( EnumSkuItemStatus.TEMP_BOOKED.getSkuItemStatus());
            SkuGroup skuGroup = skuGroupService.getInStockSkuGroup(hkBarcode, userService.getWarehouseForLoggedInUser().getId(), skuItemStatusList);
            if (skuGroup == null) {
                addRedirectAlertMessage(new SimpleMessage("Invalid  Barcode"));
                return new RedirectResolution(CycleCountAction.class, "save").addParameter("cycleCount", cycleCount.getId());
            }
            CycleCountItem existingCycleCountItem = null;
            for (CycleCountItem cycleCountItem : cycleCount.getCycleCountItems()) {
                if (skuGroup == cycleCountItem.getSkuGroup()) {
                    existingCycleCountItem = cycleCountItem;
                    break;
                }
            }

            if (existingCycleCountItem != null && existingCycleCountItem.getScannedQty() > 0) {
                existingCycleCountItem.setScannedQty(existingCycleCountItem.getScannedQty() - 1);
                cycleCountService.save(existingCycleCountItem);

            } else {
                addRedirectAlertMessage(new SimpleMessage("No Scanned item found"));
                return new RedirectResolution(CycleCountAction.class, "save").addParameter("cycleCount", cycleCount.getId());
            }


        }
//       return new RedirectResolution(ViewSkuItemAction.class, "pre").addParameter("cycleCount", cycleCount).addParameter("skuGroup", skuItem.getSkuGroup()).addParameter("entityId", EnumSkuItemTransferMode.CYCLE_COUNT.getId());
        addRedirectAlertMessage(new SimpleMessage("Database updated"));
        return new RedirectResolution(CycleCountAction.class, "save").addParameter("cycleCount", cycleCount.getId()).addParameter("messageColor", "green");
    }


    public Resolution moveToInProgressStatus() {
        cycleCount.setCycleStatus(EnumCycleCountStatus.InProgress.getId());
        cycleCount = cycleCountService.save(cycleCount);
        return new RedirectResolution(CycleCountAction.class, "view").addParameter("cycleCount", cycleCount.getId()).addParameter("cycleCountPVImapString", cycleCountPVImapString);

    }

    public Resolution deleteAllScannedBatchForPVId() {
        ProductVariant productVariant = productVariantService.getVariantById(productVariantId.trim());
        if (productVariant != null) {
            cycleCountService.deleteAllCycleCountItemsOfProductVariant(cycleCount, productVariant);
        } else {
            message = "Invalid Variant ID";
        }
        return new RedirectResolution(CycleCountAction.class, "view").addParameter("cycleCount", cycleCount.getId()).
                addParameter("message", message).addParameter("error", error);

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
        params.add("auditBy");
        params.add("auditorLogin");
        params.add("startDate");
        params.add("endDate");
        params.add("cycleCountStatus");
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

    public Map<Long, Integer> getMissedSkuGroupSystemInventoryMap() {
        return missedSkuGroupSystemInventoryMap;
    }

    public void setMissedSkuGroupSystemInventoryMap(Map<Long, Integer> missedSkuGroupSystemInventoryMap) {
        this.missedSkuGroupSystemInventoryMap = missedSkuGroupSystemInventoryMap;
    }

    public Map<Long, Integer> getSkuItemSystemQtyMap() {
        return skuItemSystemQtyMap;
    }

    public void setSkuItemSystemQtyMap(Map<Long, Integer> skuItemSystemQtyMap) {
        this.skuItemSystemQtyMap = skuItemSystemQtyMap;
    }

    public Map<Long, Integer> getSkuItemScannedQtyMap() {
        return skuItemScannedQtyMap;
    }

    public void setSkuItemScannedQtyMap(Map<Long, Integer> skuItemScannedQtyMap) {
        this.skuItemScannedQtyMap = skuItemScannedQtyMap;
    }

    public List<SkuGroup> getScannedSkuItemGroupList() {
        return scannedSkuItemGroupList;
    }

    public void setScannedSkuItemGroupList(List<SkuGroup> scannedSkuItemGroupList) {
        this.scannedSkuItemGroupList = scannedSkuItemGroupList;
    }

    public SkuItem getSkuItem() {
        return skuItem;
    }

    public void setSkuItem(SkuItem skuItem) {
        this.skuItem = skuItem;
    }


    public String getProductVariantId() {
        return productVariantId;
    }

    public void setProductVariantId(String productVariantId) {
        this.productVariantId = productVariantId;
    }
}
