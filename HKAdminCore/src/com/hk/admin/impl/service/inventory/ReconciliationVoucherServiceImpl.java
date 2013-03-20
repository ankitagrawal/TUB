package com.hk.admin.impl.service.inventory;

import com.hk.admin.pact.dao.inventory.AdminProductVariantInventoryDao;
import com.hk.admin.pact.dao.inventory.AdminSkuItemDao;
import com.hk.admin.pact.dao.inventory.ReconciliationVoucherDao;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.admin.pact.service.inventory.ReconciliationVoucherService;
import com.hk.admin.util.ReconciliationVoucherParser;
import com.hk.constants.inventory.EnumInvTxnType;
import com.hk.constants.inventory.EnumReconciliationType;
import com.hk.constants.sku.EnumSkuItemStatus;
import com.hk.domain.inventory.rv.ReconciliationVoucher;
import com.hk.domain.inventory.rv.RvLineItem;
import com.hk.domain.inventory.rv.ReconciliationType;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.user.User;
import com.hk.domain.core.InvTxnType;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.catalog.product.ProductVariantDao;
import com.hk.pact.dao.sku.SkuGroupDao;
import com.hk.pact.dao.sku.SkuItemDao;
import com.hk.pact.dao.user.UserDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.inventory.SkuGroupService;
import com.hk.pact.service.inventory.SkuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Neha
 * Date: Aug 6, 2012
 * Time: 1:26:40 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class ReconciliationVoucherServiceImpl implements ReconciliationVoucherService {

    private static Logger logger = LoggerFactory.getLogger(ReconciliationVoucherServiceImpl.class);
    @Autowired
    private BaseDao baseDao;
    @Autowired
    ReconciliationVoucherDao reconciliationVoucherDao;
    @Autowired
    SkuItemDao skuItemDao;
    @Autowired
    UserService userService;
    @Autowired
    SkuGroupService skuGroupService;

    @Autowired
    ProductVariantDao productVariantDao;
    @Autowired
    ReconciliationVoucherParser rvParser;

    @Autowired
    UserDao userDao;
    @Autowired
    SkuGroupDao skuGroupDao;
    @Autowired
    AdminSkuItemDao adminSkuItemDao;
    @Autowired
    AdminInventoryService adminInventoryService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    AdminProductVariantInventoryDao productVariantInventoryDao;
    @Autowired
    SkuService skuService;
    @Autowired
    private ProductVariantService productVariantService;


    public void reconcileAddRV(User loggedOnUser, List<RvLineItem> rvLineItems, ReconciliationVoucher reconciliationVoucher) {


        if (reconciliationVoucher == null || reconciliationVoucher.getId() == null) {
            // reconciliationVoucher = new ReconciliationVoucher();
            reconciliationVoucher.setCreateDate(new Date());
            reconciliationVoucher.setCreatedBy(loggedOnUser);
            reconciliationVoucher.setReconciliationType(EnumReconciliationType.Add.asReconciliationType());
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

                int additionType = rvLineItem.getReconciliationType().getId().intValue();
                InvTxnType invTxnType = null;

                switch (additionType) {
                    case 10:
                        invTxnType = inventoryService.getInventoryTxnType(EnumInvTxnType.RV_CHECKIN);
                        break;
                    case 30:
                        invTxnType = inventoryService.getInventoryTxnType(EnumInvTxnType.RV_DAMAGED);
                        break;
                    case 40:
                        invTxnType = inventoryService.getInventoryTxnType(EnumInvTxnType.RV_EXPIRED);
                        break;
                    case 60:
                        invTxnType = inventoryService.getInventoryTxnType(EnumInvTxnType.RV_BATCH_MISMATCH);
                        break;
                    case 70:
                        invTxnType = inventoryService.getInventoryTxnType(EnumInvTxnType.RV_CUSTOMER_RETURN);
                        break;
                    case 80:
                        invTxnType = inventoryService.getInventoryTxnType(EnumInvTxnType.RV_PHARMA_RETURN);
                        break;
                    case 90:
                        invTxnType = inventoryService.getInventoryTxnType(EnumInvTxnType.RV_FREE_VARIANT_RECONCILE);
                        break;
                }

                rvLineItem.setSku(sku);
                rvLineItem.setReconciliationVoucher(reconciliationVoucher);
                rvLineItem = (RvLineItem) getBaseDao().save(rvLineItem);
                if (productVariantInventoryDao.getPVIForRV(sku, rvLineItem).isEmpty()) {
                    // Create batch and checkin inv
//                        SkuGroup skuGroup = adminInventoryService.createSkuGroup(rvLineItem.getBatchNumber(), rvLineItem.getMfgDate(), rvLineItem.getExpiryDate(), rvLineItem.getCostPrice(), rvLineItem.getMrp(), null, reconciliationVoucher, null, sku);
                    SkuGroup skuGroup = adminInventoryService.createSkuGroupWithoutBarcode(rvLineItem.getBatchNumber(), rvLineItem.getMfgDate(), rvLineItem.getExpiryDate(), rvLineItem.getCostPrice(), rvLineItem.getMrp(), null, reconciliationVoucher, null, sku);
                    adminInventoryService.createSkuItemsAndCheckinInventory(skuGroup, rvLineItem.getQty(), null, null, rvLineItem, null, invTxnType, loggedOnUser);
                    rvLineItem.setSkuGroup(skuGroup);
                }
                rvLineItem.setReconciledQty(rvLineItem.getQty());
                rvLineItem = (RvLineItem) getBaseDao().save(rvLineItem);


                // Check inventory health now.
                inventoryService.checkInventoryHealth(rvLineItem.getSku().getProductVariant());
            }
        }
    }

    public ProductVariantService getProductVariantService() {
        return productVariantService;
    }

    public void setProductVariantService(ProductVariantService productVariantService) {
        this.productVariantService = productVariantService;
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public ReconciliationVoucher save(ReconciliationVoucher reconciliationVoucher) {
        return (ReconciliationVoucher) baseDao.save(reconciliationVoucher);

    }
    /* Commenting code for Bulk reconcile and save
    public RvLineItem reconcile(RvLineItem rvLineItem, ReconciliationVoucher reconciliationVoucher, List<SkuItem> skuItemList) {
        User loggedOnUser = userService.getLoggedInUser();
        if (rvLineItem.getId() == null) {
            rvLineItem.setReconciliationVoucher(reconciliationVoucher);
            rvLineItem = (RvLineItem) getBaseDao().save(rvLineItem);
        }

        try {
            int targetReconciledQty = rvLineItem.getQty().intValue() - rvLineItem.getReconciledQty().intValue();
            int counter = 0;
            if (rvLineItem.getReconciliationType().getId().equals(EnumReconciliationType.Damage.getId())) {
                // Delete from entered batches.
                for (SkuItem instockSkuItem : skuItemList) {
                    if (counter < Math.abs(targetReconciledQty)) {
                        adminInventoryService.inventoryCheckinCheckout(rvLineItem.getSku(), instockSkuItem, null, null, null, rvLineItem, null,
                                EnumInvTxnType.RV_DAMAGED.asInvTxnType(), -1L, loggedOnUser);
                        adminInventoryService.damageInventoryCheckin(instockSkuItem, null);
                        counter++;
                    } else {
                        break;
                    }
                }

            } else if (rvLineItem.getReconciliationType().getId().equals(EnumReconciliationType.Expired.getId())) {
                // Delete from available batches.
                for (SkuItem instockSkuItem : skuItemList) {
                    if (counter < Math.abs(targetReconciledQty)) {
                        adminInventoryService.inventoryCheckinCheckout(rvLineItem.getSku(), instockSkuItem, null, null, null, rvLineItem, null,
                                EnumInvTxnType.RV_EXPIRED.asInvTxnType(), -1L, loggedOnUser);
                        // inventoryService.damageInventoryCheckin(instockSkuItem, null);
                        counter++;
                    } else {
                        break;
                    }
                }

            } else if (rvLineItem.getReconciliationType().getId().equals(EnumReconciliationType.Lost.getId())) {
                // Delete from available batches.
                for (SkuItem instockSkuItem : skuItemList) {
                    if (counter < Math.abs(targetReconciledQty)) {
                        adminInventoryService.inventoryCheckinCheckout(rvLineItem.getSku(), instockSkuItem, null, null, null, rvLineItem, null,
                                EnumInvTxnType.RV_LOST_PILFERAGE.asInvTxnType(), -1L, loggedOnUser);
                        // inventoryService.damageInventoryCheckin(instockSkuItem, null);
                        counter++;
                    } else {
                        break;
                    }
                }

            } else if (rvLineItem.getReconciliationType().getId().equals(EnumReconciliationType.BatchMismatch.getId())) {
                // Delete from available batches.
                for (SkuItem instockSkuItem : skuItemList) {
                    if (counter < Math.abs(targetReconciledQty)) {
                        adminInventoryService.inventoryCheckinCheckout(rvLineItem.getSku(), instockSkuItem, null, null, null, rvLineItem, null,
                                EnumInvTxnType.RV_BATCH_MISMATCH.asInvTxnType(), -1L, loggedOnUser);
                        // inventoryService.damageInventoryCheckin(instockSkuItem, null);
                        counter++;
                    } else {
                        break;
                    }
                }

            } else if (rvLineItem.getReconciliationType().getId().equals(EnumReconciliationType.MrpMismatch.getId())) {
                // Delete from available batches.
                for (SkuItem instockSkuItem : skuItemList) {
                    if (counter < Math.abs(targetReconciledQty)) {
                        adminInventoryService.inventoryCheckinCheckout(rvLineItem.getSku(), instockSkuItem, null, null, null, rvLineItem, null,
                                EnumInvTxnType.RV_MRP_MISMATCH.asInvTxnType(), -1L, loggedOnUser);
                        // inventoryService.damageInventoryCheckin(instockSkuItem, null);
                        counter++;
                    } else {
                        break;
                    }
                }

            } else if (rvLineItem.getReconciliationType().getId().equals(EnumReconciliationType.NonMoving.getId())) {
                // Delete from available batches.
                for (SkuItem instockSkuItem : skuItemList) {
                    if (counter < Math.abs(targetReconciledQty)) {
                        adminInventoryService.inventoryCheckinCheckout(rvLineItem.getSku(), instockSkuItem, null, null, null, rvLineItem, null,
                                EnumInvTxnType.RV_NON_MOVING.asInvTxnType(), -1L, loggedOnUser);
                        // inventoryService.damageInventoryCheckin(instockSkuItem, null);
                        counter++;
                    } else {
                        break;
                    }
                }

            }
            long alreadyReconciledQty = rvLineItem.getReconciledQty().intValue() + counter;
            rvLineItem.setReconciledQty(Long.valueOf(alreadyReconciledQty));
            rvLineItem = (RvLineItem) getBaseDao().save(rvLineItem);

        } catch (Exception e) {
            return null;
        }
        // Check inventory health now.
        inventoryService.checkInventoryHealth(rvLineItem.getSku().getProductVariant());


        return rvLineItem;
    }
   */

    /* Commenting Code for bulk reconcile
    public void save(List<RvLineItem> rvLineItems, ReconciliationVoucher reconciliationVoucher) {
        logger.debug("rvLineItems@Save: " + rvLineItems.size());

        for (RvLineItem rvLineItem : rvLineItems) {
            if (rvLineItem.getQty() != null && rvLineItem.getQty() == 0 && rvLineItem.getId() != null) {
                getBaseDao().delete(rvLineItem);
                continue;
            }
            if (rvLineItem.getId() == null) {
                rvLineItem.setReconciliationVoucher(reconciliationVoucher);
                getBaseDao().save(rvLineItem);
            } else {
                getBaseDao().saveOrUpdate(rvLineItem);
            }
            baseDao.saveOrUpdate(reconciliationVoucher);

        }
    }
    */
    public void delete(ReconciliationVoucher reconciliationVoucher) {
        getBaseDao().delete(reconciliationVoucher);
    }


    public RvLineItem createRVLineItemWithBasicDetails(SkuGroup skuGroup, Sku sku) {
        RvLineItem rvLineItem = new RvLineItem();
        rvLineItem.setProductVariant(sku.getProductVariant());
        rvLineItem.setBatchNumber(skuGroup.getBatchNumber());
        rvLineItem.setQty(1L);
        rvLineItem.setMrp(skuGroup.getMrp());
        rvLineItem.setCostPrice(skuGroup.getCostPrice());
        rvLineItem.setMfgDate(skuGroup.getMfgDate());
        rvLineItem.setExpiryDate(skuGroup.getExpiryDate());
        rvLineItem.setSku(sku);
        rvLineItem.setSkuGroup(skuGroup);
        return rvLineItem;
    }


    @Transactional
    public void reconcileSubtractRV(ReconciliationVoucher reconciliationVoucher, List<RvLineItem> rvLineItemList) {
        for (RvLineItem rvLineItem : rvLineItemList) {
            ReconciliationType reconciliationType = rvLineItem.getReconciliationType();
            if (rvLineItem.getSkuItem() != null) {
                rvLineItem.setSkuGroup(rvLineItem.getSkuItem().getSkuGroup());
                reconcileSKUItems(reconciliationVoucher, reconciliationType, rvLineItem.getSkuItem(), "Uploaded By Excel By Batches");
            } else {
                SkuGroup skuGroup = rvLineItem.getSkuGroup();
                List<SkuItem> skuItemList = skuGroupService.getInStockSkuItems(skuGroup);
                int qty = rvLineItem.getQty().intValue();
                while (qty > 0) {
                    reconcileSKUItems(reconciliationVoucher, reconciliationType, skuItemList.get(qty - 1), "Uploaded by Excel By Item Barcode");
                    qty--;
                }
            }
        }
    }



    public  RvLineItem reconcileSKUItems(ReconciliationVoucher reconciliationVoucher, ReconciliationType reconciliationType, SkuItem skuItem, String remarks) {
        SkuGroup skuGroup = skuItem.getSkuGroup();
        Sku sku = skuGroup.getSku();
        int subtractionType = reconciliationType.getId().intValue();
        InvTxnType invTxnType = null;

        switch (subtractionType) {
            case 30:
                invTxnType = inventoryService.getInventoryTxnType(EnumInvTxnType.RV_DAMAGED);
                skuItem.setSkuItemStatus(EnumSkuItemStatus.Damaged.getSkuItemStatus());
                break;
            case 40:
                invTxnType = inventoryService.getInventoryTxnType(EnumInvTxnType.RV_EXPIRED);
                skuItem.setSkuItemStatus(EnumSkuItemStatus.Expired.getSkuItemStatus());
                break;
            case 50:
                invTxnType = inventoryService.getInventoryTxnType(EnumInvTxnType.RV_LOST_PILFERAGE);
                skuItem.setSkuItemStatus(EnumSkuItemStatus.Lost.getSkuItemStatus());
                break;
            case 60:
                invTxnType = inventoryService.getInventoryTxnType(EnumInvTxnType.RV_BATCH_MISMATCH);
                skuItem.setSkuItemStatus(EnumSkuItemStatus.BatchMismatch.getSkuItemStatus());
                break;
            case 70:
                invTxnType = inventoryService.getInventoryTxnType(EnumInvTxnType.RV_MRP_MISMATCH);
                skuItem.setSkuItemStatus(EnumSkuItemStatus.MrpMismatch.getSkuItemStatus());
                break;
            case 80:
                invTxnType = inventoryService.getInventoryTxnType(EnumInvTxnType.RV_NON_MOVING);
                skuItem.setSkuItemStatus(EnumSkuItemStatus.NonMoving.getSkuItemStatus());
                break;
            case 90:
                invTxnType = inventoryService.getInventoryTxnType(EnumInvTxnType.RV_FREE_VARIANT_RECONCILE);
                skuItem.setSkuItemStatus(EnumSkuItemStatus.FreeVariant.getSkuItemStatus());
                break;
        }

            RvLineItem rvLineItem = reconciliationVoucherDao.getRvLineItems(reconciliationVoucher, sku, skuGroup, reconciliationType);
            if (rvLineItem == null) {
                rvLineItem = createRVLineItemWithBasicDetails(skuGroup, sku);
            }
            rvLineItem.setReconciliationType(reconciliationType);
            rvLineItem.setReconciliationVoucher(reconciliationVoucher);
            rvLineItem.setRemarks(remarks);
            if (rvLineItem.getReconciledQty() != null) {
                rvLineItem.setReconciledQty(rvLineItem.getReconciledQty() + 1L);
                rvLineItem.setQty(rvLineItem.getQty() + 1L);
            } else {
                rvLineItem.setReconciledQty(1L);
                rvLineItem.setQty(1L);
            }


        skuItem = skuGroupService.saveSkuItem(skuItem);
        rvLineItem = (RvLineItem) getBaseDao().save(rvLineItem);
        adminInventoryService.inventoryCheckinCheckout(sku, skuItem, null, null, null, rvLineItem, null,
                invTxnType, -1L, userService.getLoggedInUser());

        if (reconciliationType.getId().equals(EnumReconciliationType.Damage.getId())) {
            adminInventoryService.damageInventoryCheckin(skuItem, null);
        }
        return rvLineItem;
    }


}
