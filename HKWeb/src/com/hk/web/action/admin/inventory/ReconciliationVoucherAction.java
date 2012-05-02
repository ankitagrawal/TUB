package com.hk.web.action.admin.inventory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;


import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.admin.impl.dao.inventory.AdminProductVariantInventoryDao;
import com.hk.admin.impl.dao.inventory.AdminSkuItemDao;
import com.hk.admin.impl.dao.inventory.ReconciliationVoucherDao;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.inventory.EnumInvTxnType;
import com.hk.constants.inventory.EnumReconciliationType;
import com.hk.dao.BaseDao;
import com.hk.dao.catalog.product.ProductVariantDao;
import com.hk.dao.sku.SkuGroupDao;
import com.hk.dao.user.UserDao;
import com.hk.domain.inventory.rv.ReconciliationType;
import com.hk.domain.inventory.rv.ReconciliationVoucher;
import com.hk.domain.inventory.rv.RvLineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.service.InventoryService;
import com.hk.service.SkuService;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(hasAnyPermissions = { PermissionConstants.RECON_VOUCHER_MANAGEMENT }, authActionBean = AdminPermissionAction.class)
@Component
public class ReconciliationVoucherAction extends BasePaginatedAction {

    private static Logger               logger                 = Logger.getLogger(ReconciliationVoucherAction.class);

    ReconciliationVoucherDao            reconciliationVoucherDao;

    BaseDao                             baseDao;

    ProductVariantDao                   productVariantDao;

    UserDao                             userDao;

    SkuGroupDao                         skuGroupDao;

    AdminSkuItemDao                     adminSkuItemDao;

    AdminInventoryService               adminInventoryService;

    private InventoryService            inventoryService;

    AdminProductVariantInventoryDao     productVariantInventoryDao;

    SkuService                          skuService;

    private ReconciliationVoucher       reconciliationVoucher;
    private List<ReconciliationVoucher> reconciliationVouchers = new ArrayList<ReconciliationVoucher>();
    private List<RvLineItem>            rvLineItems            = new ArrayList<RvLineItem>();
    public String                       productVariantId;
    Page                                reconciliationVoucherPage;
    private Integer                     defaultPerPage         = 30;
    private Date                        createDate;
    private String                      userLogin;
    private Warehouse                   warehouse;

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
                baseDao.delete(rvLineItem);
            } else if (rvLineItem.getId() == null) {
                if (rvLineItem.getReconciliationType().equals(reconciliationVoucherDao.get(ReconciliationType.class, EnumReconciliationType.Add.getId()))) {
                    rvLineItem.setSku(sku);
                    rvLineItem.setReconciliationVoucher(reconciliationVoucher);
                    rvLineItem = (RvLineItem) baseDao.save(rvLineItem);
                    if (productVariantInventoryDao.getPVIForRV(sku, rvLineItem).isEmpty()) {
                        // Create batch and checkin inv
                        SkuGroup skuGroup = adminInventoryService.createSkuGroup(rvLineItem.getBatchNumber(), rvLineItem.getMfgDate(), rvLineItem.getExpiryDate(), null,
                                reconciliationVoucher, sku);
                        adminInventoryService.createSkuItemsAndCheckinInventory(skuGroup, rvLineItem.getQty(), null, null, rvLineItem,
                                inventoryService.getInventoryTxnType(EnumInvTxnType.RV_CHECKIN), loggedOnUser);
                    }
                } else if (rvLineItem.getReconciliationType().equals(reconciliationVoucherDao.get(ReconciliationType.class, EnumReconciliationType.Subtract.getId()))) {
                    List<SkuItem> instockSkuItems = adminSkuItemDao.getInStockSkuItemsBySku(sku);
                    if (!instockSkuItems.isEmpty()) {
                        rvLineItem.setSku(sku);
                        rvLineItem.setReconciliationVoucher(reconciliationVoucher);
                        rvLineItem = (RvLineItem) baseDao.save(rvLineItem);

                        if (productVariantInventoryDao.getPVIForRV(sku, rvLineItem).isEmpty()) {
                            // Delete from available batches.
                            int counter = 0;
                            for (SkuItem instockSkuItem : instockSkuItems) {
                                if (counter < Math.abs(rvLineItem.getQty())) {
                                    adminInventoryService.inventoryCheckinCheckout(sku, instockSkuItem, null, null, null, rvLineItem,
                                            inventoryService.getInventoryTxnType(EnumInvTxnType.RV_LOST_PILFERAGE), -1L, loggedOnUser);
                                    counter++;
                                } else {
                                    break;
                                }
                            }
                        }
                    }
                } else if (rvLineItem.getReconciliationType().equals(reconciliationVoucherDao.get(ReconciliationType.class, EnumReconciliationType.Damage.getId()))) {
                    List<SkuItem> instockSkuItems = adminSkuItemDao.getInStockSkuItemsBySku(sku);
                    if (!instockSkuItems.isEmpty()) {
                        rvLineItem.setSku(sku);
                        rvLineItem.setReconciliationVoucher(reconciliationVoucher);
                        rvLineItem = (RvLineItem) baseDao.save(rvLineItem);
                        if (productVariantInventoryDao.getPVIForRV(sku, rvLineItem).isEmpty()) {
                            // Delete from available batches.
                            int counter = 0;
                            for (SkuItem instockSkuItem : instockSkuItems) {
                                if (counter < Math.abs(rvLineItem.getQty())) {
                                    adminInventoryService.inventoryCheckinCheckout(sku, instockSkuItem, null, null, null, rvLineItem,
                                            inventoryService.getInventoryTxnType(EnumInvTxnType.RV_DAMAGED), -1L, loggedOnUser);
                                    adminInventoryService.damageInventoryCheckin(instockSkuItem, null);
                                    counter++;
                                } else {
                                    break;
                                }
                            }
                        }
                    }
                } else if (rvLineItem.getReconciliationType().equals(reconciliationVoucherDao.get(ReconciliationType.class, EnumReconciliationType.Expired.getId()))) {
                    List<SkuItem> instockSkuItems = adminSkuItemDao.getInStockSkuItemsBySku(sku);
                    if (!instockSkuItems.isEmpty()) {
                        rvLineItem.setSku(sku);
                        rvLineItem.setReconciliationVoucher(reconciliationVoucher);
                        rvLineItem = (RvLineItem) baseDao.save(rvLineItem);
                        if (productVariantInventoryDao.getPVIForRV(sku, rvLineItem).isEmpty()) {
                            // Delete from available batches.
                            int counter = 0;
                            for (SkuItem instockSkuItem : instockSkuItems) {
                                if (counter < Math.abs(rvLineItem.getQty())) {
                                    adminInventoryService.inventoryCheckinCheckout(sku, instockSkuItem, null, null, null, rvLineItem,
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

    public Set<String> getParamSet() {
        HashSet<String> params = new HashSet<String>();
        params.add("createDate");
        params.add("userLogin");
        params.add("warehouse");
        return params;
    }

}