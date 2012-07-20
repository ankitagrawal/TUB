package com.hk.web.action.admin.inventory;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.admin.pact.dao.inventory.AdminProductVariantInventoryDao;
import com.hk.admin.pact.dao.inventory.AdminSkuItemDao;
import com.hk.admin.pact.dao.inventory.ReconciliationVoucherDao;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.admin.util.XslUtil;
import com.hk.constants.XslConstants;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.inventory.EnumInvTxnType;
import com.hk.constants.inventory.EnumReconciliationType;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.rv.ReconciliationType;
import com.hk.domain.inventory.rv.ReconciliationVoucher;
import com.hk.domain.inventory.rv.RvLineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.catalog.product.ProductVariantDao;
import com.hk.pact.dao.sku.SkuGroupDao;
import com.hk.pact.dao.user.UserDao;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.util.io.ExcelSheetParser;
import com.hk.util.io.HKRow;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import org.apache.log4j.Logger;
import org.apache.solr.common.util.StrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

@Secure(hasAnyPermissions = { PermissionConstants.RECON_VOUCHER_MANAGEMENT }, authActionBean = AdminPermissionAction.class)
@Component
public class ReconciliationVoucherAction extends BasePaginatedAction {

    private static Logger               logger                 = Logger.getLogger(ReconciliationVoucherAction.class);
    @Autowired
    ReconciliationVoucherDao            reconciliationVoucherDao;

    @Autowired
    ProductVariantDao                   productVariantDao;
    @Autowired
    UserDao                             userDao;
    @Autowired
    SkuGroupDao                         skuGroupDao;
    @Autowired
    AdminSkuItemDao                     adminSkuItemDao;
    @Autowired
    AdminInventoryService               adminInventoryService;
    @Autowired
    private InventoryService            inventoryService;
    @Autowired
    AdminProductVariantInventoryDao     productVariantInventoryDao;
    @Autowired
    SkuService                          skuService;
    @Autowired
    private ProductVariantService productVariantService;

    private ReconciliationVoucher       reconciliationVoucher;
    private List<ReconciliationVoucher> reconciliationVouchers = new ArrayList<ReconciliationVoucher>();
    private List<RvLineItem>            rvLineItems            = new ArrayList<RvLineItem>();
    public String                       productVariantId;
    Page                                reconciliationVoucherPage;
    private Integer                     defaultPerPage         = 30;
    private Date                        createDate;
    private String                      userLogin;
    private Warehouse                   warehouse;

    @Validate(required = true, on = "parse")
    private FileBean                    fileBean;

    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String                   adminUploadsPath;

    @DefaultHandler
    public Resolution pre() {
        if (warehouse == null && getPrincipalUser() != null && getPrincipalUser().getSelectedWarehouse() != null) {
            warehouse = getPrincipalUser().getSelectedWarehouse();
        }
        reconciliationVoucherPage = reconciliationVoucherDao.searchReconciliationVoucher(createDate, userLogin, warehouse, getPageNo(), getPerPage());
        reconciliationVouchers = reconciliationVoucherPage.getList();
        return new ForwardResolution("/pages/admin/reconciliationVoucherList.jsp");
    }

    public Resolution view() {
        if (reconciliationVoucher != null) {
            logger.debug("reconciliationVoucher@Pre: " + reconciliationVoucher.getId());
        }
        return new ForwardResolution("/pages/admin/reconciliationVoucher.jsp");
    }

    public Resolution save() {
        User loggedOnUser = null;
        if (getPrincipal() != null) {
            loggedOnUser = getUserService().getUserById(getPrincipal().getId());
        }

        if (reconciliationVoucher == null || reconciliationVoucher.getId() == null) {
            // reconciliationVoucher = new ReconciliationVoucher();
            reconciliationVoucher.setCreateDate(new Date());
            reconciliationVoucher.setCreatedBy(loggedOnUser);
        }
        reconciliationVoucher = (ReconciliationVoucher) reconciliationVoucherDao.save(reconciliationVoucher);

        logger.debug("rvLineItems@Save: " + rvLineItems.size());

        for (RvLineItem rvLineItem : rvLineItems) {
            Sku sku = rvLineItem.getSku();
            if (sku == null) {
                sku = skuService.getSKU(rvLineItem.getProductVariant(), reconciliationVoucher.getWarehouse());
            }
            if (rvLineItem.getQty() != null && rvLineItem.getQty() == 0 && rvLineItem.getId() != null) {
                getBaseDao().delete(rvLineItem);
            } else if (rvLineItem.getId() == null) {
                if (rvLineItem.getReconciliationType().getId().equals(EnumReconciliationType.Add.getId())) {
                    rvLineItem.setSku(sku);
                    rvLineItem.setReconciliationVoucher(reconciliationVoucher);
                    rvLineItem = (RvLineItem) getBaseDao().save(rvLineItem);
                    if (productVariantInventoryDao.getPVIForRV(sku, rvLineItem).isEmpty()) {
                        // Create batch and checkin inv
                        SkuGroup skuGroup = adminInventoryService.createSkuGroup(rvLineItem.getBatchNumber(), rvLineItem.getMfgDate(), rvLineItem.getExpiryDate(), rvLineItem.getCostPrice(), rvLineItem.getMrp(), null, reconciliationVoucher, null, sku);
                        adminInventoryService.createSkuItemsAndCheckinInventory(skuGroup, rvLineItem.getQty(), null, null, rvLineItem, null, inventoryService.getInventoryTxnType(EnumInvTxnType.RV_CHECKIN), loggedOnUser);
                    }
                } else if (rvLineItem.getReconciliationType().getId().equals(EnumReconciliationType.Subtract.getId())) {
                    List<SkuItem> instockSkuItems = adminSkuItemDao.getInStockSkuItemsBySku(sku);
                    if (!instockSkuItems.isEmpty()) {
                        rvLineItem.setSku(sku);
                        rvLineItem.setReconciliationVoucher(reconciliationVoucher);
                        rvLineItem = (RvLineItem) getBaseDao().save(rvLineItem);

                        if (productVariantInventoryDao.getPVIForRV(sku, rvLineItem).isEmpty()) {
                            // Delete from available batches.
                            int counter = 0;
                            for (SkuItem instockSkuItem : instockSkuItems) {
                                if (counter < Math.abs(rvLineItem.getQty())) {
                                    adminInventoryService.inventoryCheckinCheckout(sku, instockSkuItem, null, null, null, rvLineItem,null,
                                            inventoryService.getInventoryTxnType(EnumInvTxnType.RV_LOST_PILFERAGE), -1L, loggedOnUser);
                                    counter++;
                                } else {
                                    break;
                                }
                            }
                        }
                    }
                } else if (rvLineItem.getReconciliationType().getId().equals(EnumReconciliationType.Damage.getId())) {
                    List<SkuItem> instockSkuItems = adminSkuItemDao.getInStockSkuItemsBySku(sku);
                    if (!instockSkuItems.isEmpty()) {
                        rvLineItem.setSku(sku);
                        rvLineItem.setReconciliationVoucher(reconciliationVoucher);
                        rvLineItem = (RvLineItem) getBaseDao().save(rvLineItem);
                        if (productVariantInventoryDao.getPVIForRV(sku, rvLineItem).isEmpty()) {
                            // Delete from available batches.
                            int counter = 0;
                            for (SkuItem instockSkuItem : instockSkuItems) {
                                if (counter < Math.abs(rvLineItem.getQty())) {
                                    adminInventoryService.inventoryCheckinCheckout(sku, instockSkuItem, null, null, null, rvLineItem,null,
                                            inventoryService.getInventoryTxnType(EnumInvTxnType.RV_DAMAGED), -1L, loggedOnUser);
                                    adminInventoryService.damageInventoryCheckin(instockSkuItem, null);
                                    counter++;
                                } else {
                                    break;
                                }
                            }
                        }
                    }
                } else if (rvLineItem.getReconciliationType().getId().equals(EnumReconciliationType.Expired.getId())) {
                    List<SkuItem> instockSkuItems = adminSkuItemDao.getInStockSkuItemsBySku(sku);
                    if (!instockSkuItems.isEmpty()) {
                        rvLineItem.setSku(sku);
                        rvLineItem.setReconciliationVoucher(reconciliationVoucher);
                        rvLineItem = (RvLineItem) getBaseDao().save(rvLineItem);
                        if (productVariantInventoryDao.getPVIForRV(sku, rvLineItem).isEmpty()) {
                            // Delete from available batches.
                            int counter = 0;
                            for (SkuItem instockSkuItem : instockSkuItems) {
                                if (counter < Math.abs(rvLineItem.getQty())) {
                                    adminInventoryService.inventoryCheckinCheckout(sku, instockSkuItem, null, null, null, rvLineItem,null,
                                            inventoryService.getInventoryTxnType(EnumInvTxnType.RV_EXPIRED), -1L, loggedOnUser);
                                    // inventoryService.damageInventoryCheckin(instockSkuItem, null);
                                    counter++;
                                } else {
                                    break;
                                }
                            }
                        }
                    }
                }
                // Check inventory health now.
                inventoryService.checkInventoryHealth(rvLineItem.getSku().getProductVariant());
            }
        }
        addRedirectAlertMessage(new SimpleMessage("Changes saved."));
        return new RedirectResolution(ReconciliationVoucherAction.class);
    }

    public Resolution parse() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String excelFilePath = adminUploadsPath + "/rvFiles/" + reconciliationVoucher.getId() + sdf.format(new Date())+ ".xls";
        File excelFile = new File(excelFilePath);
        excelFile.getParentFile().mkdirs();
        fileBean.save(excelFile);

        try {
            rvLineItems = readAndCreateRVLineItems(excelFilePath, "Sheet1");
            save();
        } catch (Exception e) {
            logger.error("Exception while reading excel sheet.", e);
            addRedirectAlertMessage(new SimpleMessage("Upload failed - " + e.getMessage()));
        }
        return new RedirectResolution(ReconciliationVoucherAction.class);
    }

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
                if(strExpiryDate != null) {
                    expiryDate = XslUtil.getDate(strExpiryDate);
                    if(expiryDate == null) {
                        throw new Exception("Incorrect format for expiry date ");
                    }
                }
                String strMfgDate = row.getColumnValue(XslConstants.MFG_DATE);
                Date mfgDate = new Date();
                if(strMfgDate != null) {
                    mfgDate = XslUtil.getDate(strMfgDate);
                    if(mfgDate == null) {
                        throw new Exception("Incorrect format for mfg date ");
                    }
                }

                ProductVariant productVariant = getProductVariantService().getVariantById(variantId);

                if(productVariant == null || qty == null || mrp == null || cost == null || batchNumber == null || mrp <=0D || cost <= 0 || qty <=0) {
                    throw new Exception("Incorrect value for one or more columns");
                }
                Sku sku = skuService.getSKU(productVariant, reconciliationVoucher.getWarehouse());

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

}