
package com.hk.web.action.admin.inventory;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.akube.framework.stripes.controller.JsonHandler;
import com.hk.admin.pact.dao.inventory.AdminProductVariantInventoryDao;
import com.hk.admin.pact.dao.inventory.AdminSkuItemDao;
import com.hk.admin.pact.dao.inventory.ReconciliationVoucherDao;
import com.hk.admin.pact.service.catalog.product.ProductVariantSupplierInfoService;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.admin.pact.service.inventory.ReconciliationVoucherService;
import com.hk.admin.util.BarcodeUtil;
import com.hk.admin.util.ReconciliationVoucherParser;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.courier.StateList;
import com.hk.constants.inventory.EnumReconciliationType;
import com.hk.constants.sku.EnumSkuItemOwner;
import com.hk.constants.sku.EnumSkuItemStatus;
import com.hk.domain.catalog.Supplier;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.rv.ReconciliationVoucher;
import com.hk.domain.inventory.rv.RvLineItem;
import com.hk.domain.inventory.rv.ReconciliationType;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.sku.SkuItemOwner;
import com.hk.domain.sku.SkuItemStatus;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.exception.NoSkuException;
import com.hk.pact.dao.catalog.product.ProductVariantDao;
import com.hk.pact.dao.core.SupplierDao;
import com.hk.pact.dao.user.UserDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.inventory.SkuGroupService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.web.HealthkartResponse;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Secure(hasAnyPermissions = {PermissionConstants.RECON_VOUCHER_MANAGEMENT}, authActionBean = AdminPermissionAction.class)
@Component
public class ReconciliationVoucherAction extends BasePaginatedAction {

    private static Logger logger = Logger.getLogger(ReconciliationVoucherAction.class);
    @Autowired
    ReconciliationVoucherDao reconciliationVoucherDao;

    @Autowired
    ProductVariantDao productVariantDao;
    @Autowired
    private ReconciliationVoucherParser rvParser;
    @Autowired
    ReconciliationVoucherService reconciliationVoucherService;
    @Autowired 
    SupplierDao supplierDao;

    @Autowired
    UserDao userDao;
    @Autowired
    AdminSkuItemDao adminSkuItemDao;
    @Autowired
    AdminInventoryService adminInventoryService;
    /*@Autowired
         private InventoryService inventoryService;*/
    @Autowired
    AdminProductVariantInventoryDao productVariantInventoryDao;
    @Autowired
    SkuService skuService;
    @Autowired
    private ProductVariantService productVariantService;
    @Autowired
    SkuGroupService skuGroupService;
    @Autowired
    UserService userService;
    @Autowired 
    ProductVariantSupplierInfoService productVariantSupplierInfoService;

    private ReconciliationVoucher reconciliationVoucher;
    private List<ReconciliationVoucher> reconciliationVouchers = new ArrayList<ReconciliationVoucher>();
    private List<RvLineItem> rvLineItems = new ArrayList<RvLineItem>();
    private RvLineItem rvLineItem;
    public String productVariantId;
    Page reconciliationVoucherPage;
    private Integer defaultPerPage = 30;
    private Date createDate;
    private String userLogin;
    private Warehouse warehouse;
    private Integer askedQty;
    private String batchNumber;
    private String errorMessage;
    File printBarcode;
    private String upc;
    private RvLineItem rvLineItemSaved;
    private ReconciliationType reconciliationType;
    private String remarks;
    private Supplier                supplier = null;
    private Long warehouseId;
    private String barcode;
    private Boolean isDebitNoteCreated;


    @Validate(required = true, on = "parse")
    private FileBean fileBean;

    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String adminUploadsPath;

    @Value("#{hkEnvProps['" + Keys.Env.barcodeGurgaon + "']}")
    String barcodeGurgaon;

    @Value("#{hkEnvProps['" + Keys.Env.barcodeMumbai + "']}")
    String barcodeMumbai;

    @SuppressWarnings("unchecked")
    @DefaultHandler
    public Resolution pre() {
        if (warehouse == null && getPrincipalUser() != null && getPrincipalUser().getSelectedWarehouse() != null) {
            warehouse = getPrincipalUser().getSelectedWarehouse();
        }
        reconciliationVoucherPage = reconciliationVoucherDao.searchReconciliationVoucher(createDate, userLogin, warehouse, getPageNo(), getPerPage());
        reconciliationVouchers = reconciliationVoucherPage.getList();
        return new ForwardResolution("/pages/admin/reconciliationVoucherList.jsp");
    }


    public Resolution parseAddRVExcel() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String excelFilePath = adminUploadsPath + "/rvFiles/" + reconciliationVoucher.getId() + sdf.format(new Date()) + ".xls";
        File excelFile = new File(excelFilePath);
        excelFile.getParentFile().mkdirs();
        fileBean.save(excelFile);

        try {
            //reconciliationVoucher has warehouse and reconciliation date
            rvLineItems = rvParser.readAndCreateAddRVLineItems(excelFilePath, "Sheet1", reconciliationVoucher);
            reconcileAdd();
        } catch (Exception e) {
            logger.error("Exception while reading excel sheet.", e);
            addRedirectAlertMessage(new SimpleMessage("Upload failed - " + e.getMessage()));
        }
        return new RedirectResolution(ReconciliationVoucherAction.class);
    }

    public Resolution parseSubtractRVExcel() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String excelFilePath = adminUploadsPath + "/rvFiles/" + reconciliationVoucher.getId() + sdf.format(new Date()) + ".xls";
        File excelFile = new File(excelFilePath);
        excelFile.getParentFile().mkdirs();
        fileBean.save(excelFile);

        try {
            //reconciliationVoucher has warehouse and reconciliation date
            List<RvLineItem> rvLineItemList = rvParser.readAndCreateSubtractRVLineItems(excelFilePath, "Sheet1", reconciliationVoucher);
            reconciliationVoucherService.reconcileSubtractRV(reconciliationVoucher, rvLineItemList);
        } catch (Exception e) {
            logger.error("Exception while reading excel sheet.", e);
            addRedirectAlertMessage(new SimpleMessage("Upload failed - " + e.getMessage()));
        }
        return new RedirectResolution(ReconciliationVoucherAction.class);
    }


    public Resolution subtractReconciliation() {
        if (reconciliationVoucher != null) {
            rvLineItems = reconciliationVoucher.getRvLineItems();
            logger.debug("reconciliationVoucher@Pre: " + reconciliationVoucher.getId());
        }

        return new ForwardResolution("/pages/admin/createReconciliationVoucher.jsp");
    }


    public Resolution addReconciliation() {
        if (reconciliationVoucher != null) {
            logger.debug("reconciliationVoucher@Pre: " + reconciliationVoucher.getId());
        }

        return new ForwardResolution("/pages/admin/reconciliationVoucher.jsp");
    }


    public Resolution reconcileAdd() {

        User loggedOnUser = null;
        if (getPrincipal() != null) {
            loggedOnUser = getUserService().getUserById(getPrincipal().getId());
        }
        if (rvLineItems != null && rvLineItems.size() > 0) {
            reconciliationVoucherService.reconcileAddRV(loggedOnUser, rvLineItems, reconciliationVoucher);
            addRedirectAlertMessage(new SimpleMessage("Changes saved."));
        }

        return new RedirectResolution(ReconciliationVoucherAction.class);
    }



    public Resolution create() {
        User loggedOnUser = null;
        if (getPrincipal() != null) {
            loggedOnUser = getUserService().getUserById(getPrincipal().getId());
        }
        
        if (reconciliationVoucher == null || reconciliationVoucher.getId() == null) {
        	 if(supplier!=null){
        		 reconciliationVoucher = new ReconciliationVoucher();
        		 reconciliationVoucher.setSupplier(supplier);
        		 reconciliationVoucher.setReconciliationType(EnumReconciliationType.RVForDebitNote.asReconciliationType());
        		 reconciliationVoucher.setWarehouse(loggedOnUser.getSelectedWarehouse());
        	 }
            reconciliationVoucher.setCreateDate(new Date());
            reconciliationVoucher.setCreatedBy(loggedOnUser);
        }
        
        reconciliationVoucher = reconciliationVoucherService.save(reconciliationVoucher);
        isDebitNoteCreated = reconciliationVoucherService.getDebitNote(reconciliationVoucher)!=null?Boolean.TRUE:Boolean.FALSE;
        return new ForwardResolution("/pages/admin/editReconciliationVoucher.jsp").addParameter("reconciliationVoucher", reconciliationVoucher.getId());
    }


	public Resolution SubtractReconciled() {
		SkuItem skuItem = null;
		if (reconciliationVoucher == null) {
			addRedirectAlertMessage(new SimpleMessage("Invalid Reconcilliation "));
			return new ForwardResolution("/pages/admin/reconciliationVoucherList.jsp");
		}

		if (StringUtils.isBlank(upc)) {
			addRedirectAlertMessage(new SimpleMessage("Barcode cannot be blank"));
			return new ForwardResolution("/pages/admin/editReconciliationVoucher.jsp").addParameter("reconciliationVoucher", reconciliationVoucher.getId());
		}

		User loggedOnUser = null;
		if (getPrincipal() != null) {
			loggedOnUser = getUserService().getUserById(getPrincipal().getId());
		}
		SkuItem skuItemBarcode = skuGroupService.getSkuItemByBarcode(upc, userService.getWarehouseForLoggedInUser().getId(),
				EnumSkuItemStatus.Checked_IN.getId());
		if (skuItemBarcode != null) {
			skuItem = skuItemBarcode;
		} else {
			List<SkuItem> inStockSkuItemList = adminInventoryService.getInStockSkuItems(upc, userService.getWarehouseForLoggedInUser());
			if (inStockSkuItemList != null && inStockSkuItemList.size() > 0) {
				skuItem = inStockSkuItemList.get(0);
			}
		}
		if (skuItem == null) {
			addRedirectAlertMessage(new SimpleMessage("Either Invalid barcode or No Item found"));
			return new ForwardResolution("/pages/admin/editReconciliationVoucher.jsp").addParameter("reconciliationVoucher", reconciliationVoucher.getId());
		}


		if (reconciliationVoucherService.reconcileSKUItems(reconciliationVoucher, reconciliationType, skuItem, remarks) == null) {
			addRedirectAlertMessage(new SimpleMessage("Error occured in saving RVLineitem"));
			return new ForwardResolution("/pages/admin/editReconciliationVoucher.jsp").addParameter("reconciliationVoucher", reconciliationVoucher.getId());
		}
		//validating this skuitem against any entries in booking table
		reconciliationVoucherService.validateSkuItem(skuItem);
		addRedirectAlertMessage(new SimpleMessage("DataBase Updated"));
		return new RedirectResolution("/pages/admin/editReconciliationVoucher.jsp").addParameter("reconciliationVoucher", reconciliationVoucher.getId());
	}


    public Resolution downloadBarcode() {

        if (rvLineItem == null) {
            return new RedirectResolution("/pages/admin/reconciliationVoucher.jsp").addParameter("reconciliationVoucher", reconciliationVoucher.getId());
        }
        List<SkuItem> checkedInSkuItems = adminInventoryService.getCheckedInOrOutSkuItems(rvLineItem, null, null, null, 1L);
        if (checkedInSkuItems == null || checkedInSkuItems.size() < 1) {
            addRedirectAlertMessage(new SimpleMessage(" Please do checkin some items for Downlaoding Barcode "));
            return new RedirectResolution("/pages/admin/reconciliationVoucher.jsp").addParameter("reconciliationVoucher", reconciliationVoucher.getId());
        }

        ProductVariant productVariant = rvLineItem.getSku().getProductVariant();
        Map<Long, String> skuItemDataMap = adminInventoryService.skuItemBarcodeMap(checkedInSkuItems);

        String barcodeFilePath = null;
        Warehouse userWarehouse = null;
        if (getUserService().getWarehouseForLoggedInUser() != null) {
            userWarehouse = userService.getWarehouseForLoggedInUser();
        } else {
            addRedirectAlertMessage(new SimpleMessage("There is no warehouse attached with the logged in user. Please check with the admin."));
            return new RedirectResolution(ReconciliationVoucherAction.class);
        }
        if (userWarehouse.getState().equalsIgnoreCase(StateList.HARYANA)) {
            barcodeFilePath = barcodeGurgaon;
        } else {
            barcodeFilePath = barcodeMumbai;
        }
        barcodeFilePath = barcodeFilePath + "/" + "printBarcode_" + "rv_" + rvLineItem.getReconciliationVoucher().getId() + "_" + productVariant.getId() + "_"
                + StringUtils.substring(userWarehouse.getCity(), 0, 3) + ".txt";

        try {
            printBarcode = BarcodeUtil.createBarcodeFileForSkuItem(barcodeFilePath, skuItemDataMap);
        } catch (IOException e) {
            logger.error("Exception while appending on barcode file", e);
        }
        addRedirectAlertMessage(new SimpleMessage("Print Barcodes downloaded Successfully."));
        return new HTTPResponseResolution();
    }


    public Resolution downloadAllBarcode() {
        String barcodeFilePath = null;
        Map<Long, String> skuItemDataMap = new HashMap<Long, String>();
        List<RvLineItem> rvLineItems = reconciliationVoucher.getRvLineItems();
        if (rvLineItems == null || rvLineItems.size() < 1) {
            addRedirectAlertMessage(new SimpleMessage(" Please do checkin some items for Downlaoding Barcode "));
            return new ForwardResolution("/pages/admin/reconciliationVoucherList.jsp");
        }
        for (RvLineItem rvLineItem : rvLineItems) {
            List<SkuItem> checkedInSkuItems = adminInventoryService.getCheckedInOrOutSkuItems(rvLineItem, null, null, null, 1L);
            if (checkedInSkuItems != null && checkedInSkuItems.size() > 0) {
                SkuGroup skuGroup = checkedInSkuItems.get(0).getSkuGroup();
                Map<Long, String> skuItemBarcodeMap = adminInventoryService.skuItemBarcodeMap(checkedInSkuItems);
                skuItemDataMap.putAll(skuItemBarcodeMap);
                Warehouse userWarehouse = null;
                if (getUserService().getWarehouseForLoggedInUser() != null) {
                    userWarehouse = userService.getWarehouseForLoggedInUser();
                } else {
                    addRedirectAlertMessage(new SimpleMessage("There is no warehouse attached with the logged in user. Please check with the admin."));
                    return new RedirectResolution(InventoryCheckinAction.class);
                }
                if (userWarehouse.getState().equalsIgnoreCase(StateList.HARYANA)) {
                    barcodeFilePath = barcodeGurgaon;
                } else {
                    barcodeFilePath = barcodeMumbai;
                }
                barcodeFilePath = barcodeFilePath + "/" + "printBarcode_" + "rv_" + reconciliationVoucher.getId() + "_All_"
                        + StringUtils.substring(userWarehouse.getCity(), 0, 3) + ".txt";
            }
        }
        try {
            if (skuItemDataMap.size() < 1) {
                addRedirectAlertMessage(new SimpleMessage(" Please do checkin some items for Downlaoding Barcode "));
                return new RedirectResolution("/pages/admin/reconciliationVoucher.jsp").addParameter("reconciliationVoucher", reconciliationVoucher.getId());
            }
            printBarcode = BarcodeUtil.createBarcodeFileForSkuItem(barcodeFilePath, skuItemDataMap);
        } catch (IOException e) {
            logger.error("Exception while appending on barcode file", e);
        }
        addRedirectAlertMessage(new SimpleMessage("Print Barcode downloaded Successfully."));
        return new HTTPResponseResolution();
    }


    public class HTTPResponseResolution implements Resolution {
        public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
            InputStream in = new BufferedInputStream(new FileInputStream(printBarcode));
            res.setContentType("text/plain");
            res.setCharacterEncoding("UTF-8");
            res.setContentLength((int) printBarcode.length());
            res.setHeader("Content-Disposition", "attachment; filename=\"" + printBarcode.getName() + "\";");
            OutputStream out = res.getOutputStream();

            // Copy the contents of the file to the output stream
            byte[] buf = new byte[4096];
            int count = 0;
            while ((count = in.read(buf)) >= 0) {
                out.write(buf, 0, count);
            }
            in.close();
            out.flush();
            out.close();
        }

    }

    public Resolution createProductAuditedForSingleBatchPage() {
        User loggedOnUser = userService.getLoggedInUser();
        if (reconciliationVoucher == null || reconciliationVoucher.getId() == null) {
            String remark = " Subtract Inventory Variant By Single Batch";
            reconciliationVoucher = reconciliationVoucherService.createReconciliationVoucher(EnumReconciliationType.ProductVariantAudited.asReconciliationType(), remark);
        } else {
            rvLineItems = reconciliationVoucher.getRvLineItems();
        }
        return new RedirectResolution(ReconciliationVoucherAction.class, "directToSubtractPVInventoryFromSingleBatchPage").addParameter("reconciliationVoucher", reconciliationVoucher.getId());

    }

    public Resolution directToSubtractPVInventoryFromSingleBatchPage() {
        rvLineItems = reconciliationVoucher.getRvLineItems();
        return new ForwardResolution("/pages/admin/inventory/subtractPVInventoryFromSingleBatch.jsp");
    }


    public Resolution createProductAuditedForAnyBatchPage() {
        User loggedOnUser = userService.getLoggedInUser();
        if (reconciliationVoucher == null || reconciliationVoucher.getId() == null) {
            String remark = " Subtract Inventory Variant Any Batch";
            reconciliationVoucher = reconciliationVoucherService.createReconciliationVoucher(EnumReconciliationType.ProductVariantAudited.asReconciliationType(), remark);
        } else {
            rvLineItems = reconciliationVoucher.getRvLineItems();
        }
        return new RedirectResolution(ReconciliationVoucherAction.class, "directToSubtractPVInventoryFromAnyBatchPage").addParameter("reconciliationVoucher", reconciliationVoucher.getId());

    }


    public Resolution directToSubtractPVInventoryFromAnyBatchPage() {
        rvLineItems = reconciliationVoucher.getRvLineItems();
        return new ForwardResolution("/pages/admin/inventory/subtractPVInventoryFromAnyBatch.jsp");
    }

    public Resolution directToShowProductAuditedInventoryPage() {
        rvLineItems = reconciliationVoucher.getRvLineItems();
        return new ForwardResolution("/pages/admin/inventory/showProductAuditedInventory.jsp");
    }

    public HealthkartResponse subtractInventory(boolean singleBatch) {
        HealthkartResponse healthkartResponse = null;
        warehouse = userService.getWarehouseForLoggedInUser();
        if (rvLineItems != null && (!rvLineItems.isEmpty())) {
            for (RvLineItem rvLineItemTemp : rvLineItems) {
                if (rvLineItemTemp != null) {
                    rvLineItem = rvLineItemTemp;
                    break;
                }
            }

            ProductVariant productVariant = rvLineItem.getProductVariant();
            if (productVariant != null) {
                //get Sku
                Sku sku = null;
                try {
                    sku = skuService.getSKU(productVariant, warehouse);
                } catch (NoSkuException ex) {
                    return new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Operation Failed ::Sku is not created in system");
                }
                if (sku != null) {
                    List<SkuItem> inStockSkuItems = null;
                    if (singleBatch) {
                        List<SkuGroup> skuGroupList = skuGroupService.getAllInStockSkuGroups(sku);
                        if (skuGroupList.size() > 0) {
                            if (skuGroupList.size() == 1) {
                                inStockSkuItems = skuGroupService.getInStockSkuItems(skuGroupList.get(0));
                            } else {
                                return new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Operation Failed :: Inventory Present in Multiple batches ");
                            }
                        } else {
                            return new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Operation Failed :: NO Inventory ");
                        }
                    } else {
                        inStockSkuItems = skuGroupService.getCheckedInSkuItems(sku);
                    }
                    long systemQty = 0;
                    if (inStockSkuItems != null && inStockSkuItems.size() > 0) {
                        int deleteQty = rvLineItem.getQty().intValue();
                        systemQty = inStockSkuItems.size();
                        if (systemQty >= deleteQty) {
                            rvLineItem.setReconciliationVoucher(reconciliationVoucher);
                            rvLineItemSaved = reconciliationVoucherService.reconcileInventoryForPV(rvLineItem, inStockSkuItems, sku);
                        } else {
                            return new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Operation Failed :: Batch contains Qty  " + systemQty + "  only");
                        }
                    } else {
                        return new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Operation Failed :: NO Inventory");
                    }

                } else {
                    return new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Sku Not Created in System");
                }
            } else {
                return new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Operation Failed ::Invalid Product Variant Id ");
            }


        }
        return healthkartResponse;

    }


    @JsonHandler
    public Resolution subtractInventoryForPVFromSingleBatch() {
        Map dataMap = new HashMap();
        HealthkartResponse healthkartResponse = subtractInventory(true);
        if (rvLineItemSaved != null) {
            dataMap.put("rvLineItem", rvLineItem);
            healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "successful", dataMap);
        }
        return new JsonResolution(healthkartResponse);
    }

    @JsonHandler
    public Resolution subtractInventoryForPVFromAnyBatch() {
        Map dataMap = new HashMap();
        HealthkartResponse healthkartResponse = subtractInventory(false);
        if (rvLineItemSaved != null) {
            dataMap.put("rvLineItem", rvLineItem);
            healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "successful", dataMap);
        }
        return new JsonResolution(healthkartResponse);
    }


    public Resolution uploadSubtractExcelForProductAuditedForSingleBatch() throws Exception {
        StringBuilder errors = new StringBuilder("");
        rvParser.setMessage(new StringBuilder(""));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String excelFilePath = adminUploadsPath + "/rvFiles/" + reconciliationVoucher.getId() + sdf.format(new Date()) + ".xls";
        File excelFile = new File(excelFilePath);
        excelFile.getParentFile().mkdirs();
        fileBean.save(excelFile);

        try {
            //reconciliationVoucher has warehouse and reconciliation date
            String remark = "Excel Upload for Variant Audited";
            rvLineItems = rvParser.readAndCreateAddSubtractRvLineItemForProductAuditedForSingleBatch(excelFilePath, "Sheet1", reconciliationVoucher);
            errors.append(rvParser.getMessage());

            for (RvLineItem rvLineItem : rvLineItems) {
                Sku sku = rvLineItem.getSku();
                String VariantId = sku.getProductVariant().getId();
                int qty = rvLineItem.getQty().intValue();
                SkuGroup skuGroup = null;
                List<SkuGroup> skuGroupList = skuGroupService.getAllInStockSkuGroups(sku);
                if (skuGroupList.size() > 0) {
                    if (skuGroupList.size() == 1) {
                        skuGroup = skuGroupList.get(0);
                        List<SkuItem> inStockSkuItems = skuGroupService.getInStockSkuItems(skuGroup);
                        if (inStockSkuItems != null && inStockSkuItems.size() > 0) {
                            int systemQty = inStockSkuItems.size();
                            if (systemQty >= qty) {
                                rvLineItem.setSkuGroup(skuGroup);
                                rvLineItemSaved = reconciliationVoucherService.reconcileInventoryForPV(rvLineItem, inStockSkuItems, rvLineItem.getSku());
                            } else {
                                errors.append("<br/>").append("Batch contains Qty: " + systemQty + " only for  " + VariantId);

                            }

                        } else {
                            errors.append("<br/>").append("No Inventory  For " + VariantId);

                        }
                    } else {
                        errors.append("<br/> ").append("Upload failed Multiple Batches present For " + VariantId);

                    }

                } else {
                    errors.append("<br/>").append("No Inventory For " + VariantId);

                }


            }
        } catch (Exception e) {
            logger.error("Exception while reading excel sheet.", e);
            addRedirectAlertMessage(new SimpleMessage("Upload failed - " + e.getMessage()));
        }
        errorMessage = null;
        errorMessage = errors.toString();
        return new RedirectResolution(ReconciliationVoucherAction.class, "directToSubtractPVInventoryFromSingleBatchPage").addParameter("reconciliationVoucher", reconciliationVoucher.getId())
                .addParameter("errorMessage", errorMessage);


    }


    public Resolution uploadSubtractExcelForProductAuditedForAnyBatch() throws Exception {
        StringBuilder errors = new StringBuilder("");
        //make error empty
        rvParser.setMessage(new StringBuilder(""));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String excelFilePath = adminUploadsPath + "/rvFiles/" + reconciliationVoucher.getId() + sdf.format(new Date()) + ".xls";
        File excelFile = new File(excelFilePath);
        excelFile.getParentFile().mkdirs();
        fileBean.save(excelFile);

        try {
            //reconciliationVoucher has warehouse and reconciliation date
            String remark = "Excel Upload for Variant Audited";
            rvLineItems = rvParser.readAndCreateAddSubtractRvLineItemForProductAuditedForAnyBatch(excelFilePath, "Sheet1", reconciliationVoucher);

            errors.append(rvParser.getMessage());
            for (RvLineItem rvLineItem : rvLineItems) {
                List<SkuItem> inStockSkuItems = skuGroupService.getCheckedInSkuItems(rvLineItem.getSku());
                if (inStockSkuItems != null && inStockSkuItems.size() > 0) {
                    int systemQty = 0;
                    int deleteQty = rvLineItem.getQty().intValue();
                    systemQty = inStockSkuItems.size();
                    if (systemQty >= deleteQty) {
                        rvLineItemSaved = reconciliationVoucherService.reconcileInventoryForPV(rvLineItem, inStockSkuItems, rvLineItem.getSku());
                    } else {
                        errors.append("<br/>");
                        errors.append(" Batch contains Qty  " + systemQty + "  only For " + rvLineItem.getSku().getProductVariant().getId());
                    }
                } else {
                    errors.append("<br/>").append("No Inventory  For " + rvLineItem.getSku().getProductVariant().getId());
                }
            }
        } catch (Exception e) {
            logger.error("Exception while reading excel sheet.", e);
            addRedirectAlertMessage(new SimpleMessage("Upload failed - " + e.getMessage()));
        }
        errorMessage = null;
        errorMessage = errors.toString();
        return new RedirectResolution(ReconciliationVoucherAction.class, "directToSubtractPVInventoryFromAnyBatchPage").addParameter("reconciliationVoucher", reconciliationVoucher.getId())
                .addParameter("errorMessage", errorMessage);


    }


    @JsonHandler
	public Resolution getSupplierAgainstBarcode() {
		Map<Object, Object> dataMap = new HashMap<Object, Object>();
		List<SkuItemStatus> skuItemStatusList = new ArrayList<SkuItemStatus>();
        skuItemStatusList.add( EnumSkuItemStatus.Checked_IN.getSkuItemStatus());
        skuItemStatusList.add( EnumSkuItemStatus.BOOKED.getSkuItemStatus());
        skuItemStatusList.add( EnumSkuItemStatus.TEMP_BOOKED.getSkuItemStatus());
        
        List<SkuItemOwner> skuItemOwnerList = new ArrayList<SkuItemOwner>();
        skuItemOwnerList.add(EnumSkuItemOwner.SELF.getSkuItemOwnerStatus());
		HealthkartResponse healthkartResponse = null;
		SkuItem skuItem = null;
		if (StringUtils.isNotBlank(barcode) && warehouseId != null) {
			try {
				SkuItem skuItemBarcode = skuGroupService.getSkuItemByBarcode(barcode, userService.getWarehouseForLoggedInUser().getId(), skuItemStatusList, skuItemOwnerList);
		        if (skuItemBarcode != null) {
		            skuItem = skuItemBarcode;
		        } else {
		            List<SkuItem> inStockSkuItemList = adminInventoryService.getInStockSkuItems(upc, userService.getWarehouseForLoggedInUser());
		            if (inStockSkuItemList != null && inStockSkuItemList.size() > 0) {
		                skuItem = inStockSkuItemList.get(0);
		            }
		        }
		        ProductVariant productVariant;
				SkuGroup skuGroup = skuGroupService.getInStockSkuGroup(barcode, warehouseId);
				if(skuItem!=null){
					productVariant = skuItem.getSkuGroup().getSku().getProductVariant();
				}
				else{
					productVariant = skuGroup.getSku().getProductVariant();
				}
				Supplier supplier = productVariantSupplierInfoService.getSupplierFromProductVariant(productVariant);
				if (supplier != null) {
					dataMap.put("supplier", supplier);
					healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Valid Supplier Name",
							dataMap);
				}

			} catch (Exception e) {
				healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, e.getMessage());
			}
		} else {
			healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Invalid Input Data");
		}
		noCache();
		return new JsonResolution(healthkartResponse);
	}
	
	public Resolution createDebitNote(){
		if(warehouse==null){
			addRedirectAlertMessage(new SimpleMessage("Please select a warehouse"));
			return new RedirectResolution(ReconciliationVoucherAction.class).addParameter("create");
		}
		if(reconciliationVoucherService.getDebitNote(reconciliationVoucher)!=null){
			addRedirectAlertMessage(new SimpleMessage("Debit Note Number - "+reconciliationVoucherService.getDebitNote(reconciliationVoucher).getId()+"has already been created against the RV"));
			return new RedirectResolution(DebitNoteAction.class);
		}
		return new RedirectResolution(DebitNoteAction.class).addParameter("debitNoteFromRV").addParameter("reconciliationVoucher", reconciliationVoucher.getId()).addParameter("warehouse", warehouse.getId());
	}
    
    public ReconciliationVoucher getReconciliationVoucher() {
        return reconciliationVoucher;
    }

    public void setReconciliationVoucher(ReconciliationVoucher reconciliationVoucher) {
        this.reconciliationVoucher = reconciliationVoucher;
    }

    public List<ReconciliationVoucher> getReconciliationVouchers() {
        return reconciliationVouchers;
    }

    public void setReconciliationVouchers(List<ReconciliationVoucher> reconciliationVouchers) {
        this.reconciliationVouchers = reconciliationVouchers;
    }

    public List<RvLineItem> getRvLineItems() {
        return rvLineItems;
    }

    public void setRvLineItems(List<RvLineItem> rvLineItems) {
        this.rvLineItems = rvLineItems;
    }

    public String getProductVariantId() {
        return productVariantId;
    }

    public void setProductVariantId(String productVariantId) {
        this.productVariantId = productVariantId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public int getPerPageDefault() {
        return defaultPerPage;
    }

    public int getPageCount() {
        return reconciliationVoucherPage == null ? 0 : reconciliationVoucherPage.getTotalPages();
    }

    public int getResultCount() {
        return reconciliationVoucherPage == null ? 0 : reconciliationVoucherPage.getTotalResults();
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public FileBean getFileBean() {
        return fileBean;
    }

    public void setFileBean(FileBean fileBean) {
        this.fileBean = fileBean;
    }

    public ProductVariantService getProductVariantService() {
        return productVariantService;
    }

    public void setProductVariantService(ProductVariantService productVariantService) {
        this.productVariantService = productVariantService;
    }

    public Set<String> getParamSet() {
        HashSet<String> params = new HashSet<String>();
        params.add("createDate");
        params.add("userLogin");
        params.add("warehouse");
        return params;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public RvLineItem getRvLineItem() {
        return rvLineItem;
    }

    public void setRvLineItem(RvLineItem rvLineItem) {
        this.rvLineItem = rvLineItem;
    }

    public Integer getAskedQty() {
        return askedQty;
    }

    public void setAskedQty(Integer askedQty) {
        this.askedQty = askedQty;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public ReconciliationType getReconciliationType() {
        return reconciliationType;
    }

    public void setReconciliationType(ReconciliationType reconciliationType) {
        this.reconciliationType = reconciliationType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public Long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	
	public Boolean getIsDebitNoteCreated() {
		return isDebitNoteCreated;
	}

	public void setIsDebitNoteCreated(Boolean isDebitNoteCreated) {
		this.isDebitNoteCreated = isDebitNoteCreated;
	}
    
}