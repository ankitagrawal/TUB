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
import com.hk.domain.sku.SkuItemStatus;
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

    @Transactional
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

                Long additionType = rvLineItem.getReconciliationType().getId();
                EnumReconciliationType enumReconciliationType = EnumReconciliationType.getEnumReconciliationTypeById(additionType);
                InvTxnType invTxnType = null;

                switch (enumReconciliationType) {
                    case AddDamage:
                        invTxnType = inventoryService.getInventoryTxnType(EnumInvTxnType.RV_ADD_DAMAGED);
                        break;
                    case AddExpired:
                        invTxnType = inventoryService.getInventoryTxnType(EnumInvTxnType.RV_ADD_EXPIRED);
                        break;
                    case AddBatchMismatch:
                        invTxnType = inventoryService.getInventoryTxnType(EnumInvTxnType.RV_ADD_BATCH_MISMATCH);
                        break;
                    case CustomerReturn:
                        invTxnType = inventoryService.getInventoryTxnType(EnumInvTxnType.RV_CUSTOMER_RETURN);
                        break;
                    case PharmaReturn:
                        invTxnType = inventoryService.getInventoryTxnType(EnumInvTxnType.RV_PHARMA_RETURN);
                        break;
                    case AddFreeVariant:
                        invTxnType = inventoryService.getInventoryTxnType(EnumInvTxnType.RV_ADD_FREE_VARIANT_RECONCILE);
                        break;
                    case AddIncorrectCounting:
                        invTxnType = inventoryService.getInventoryTxnType(EnumInvTxnType.RV_ADD_INCORRECT_COUNTING);
                        break;
                }

                rvLineItem.setSku(sku);
                rvLineItem.setReconciliationVoucher(reconciliationVoucher);
                rvLineItem = (RvLineItem) getBaseDao().save(rvLineItem);
                SkuGroup skuGroup = null;
                if (productVariantInventoryDao.getPVIForRV(sku, rvLineItem).isEmpty()) {
                    // Create batch and checkin inv
//                        SkuGroup skuGroup = adminInventoryService.createSkuGroup(rvLineItem.getBatchNumber(), rvLineItem.getMfgDate(), rvLineItem.getExpiryDate(), rvLineItem.getCostPrice(), rvLineItem.getMrp(), null, reconciliationVoucher, null, sku);
                    skuGroup = adminInventoryService.createSkuGroupWithoutBarcode(rvLineItem.getBatchNumber(), rvLineItem.getMfgDate(), rvLineItem.getExpiryDate(), rvLineItem.getCostPrice(), rvLineItem.getMrp(), null, reconciliationVoucher, null, sku);
                    adminInventoryService.createSkuItemsAndCheckinInventory(skuGroup, rvLineItem.getQty(), null, null, rvLineItem, null, invTxnType, loggedOnUser);
                    rvLineItem.setSkuGroup(skuGroup);
                }
                rvLineItem.setReconciledQty(rvLineItem.getQty());
                rvLineItem = (RvLineItem) getBaseDao().save(rvLineItem);

                // Add PVI Entry with -1  and  set sku item status to expired
                if ((additionType.intValue()) == (EnumReconciliationType.AddExpired.getId().intValue())) {
                    for (SkuItem skuItem : skuGroup.getSkuItems()) {
                        adminInventoryService.inventoryCheckinCheckout(sku, skuItem, null, null, null, rvLineItem, null,
                                EnumInvTxnType.RV_ADD_EXPIRED_AUTOMATIC_DELETION.asInvTxnType(), -1L, userService.getLoggedInUser());
                        skuItem.setSkuItemStatus(EnumSkuItemStatus.Expired.getSkuItemStatus());
                        skuGroupService.saveSkuItem(skuItem);
                    }
                }

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
                //Uploaded Sku Item Level Wise
                rvLineItem.setSkuGroup(rvLineItem.getSkuItem().getSkuGroup());
                reconcileSKUItems(reconciliationVoucher, reconciliationType, rvLineItem.getSkuItem(), "Uploaded By Excel By Batches");
            } else {
                //Uploaded By Sku Group Level
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


    public ReconciliationVoucher createReconciliationVoucher(ReconciliationType reconciliationType, String remark) {
        User loggedOnUser = userService.getLoggedInUser();
        ReconciliationVoucher reconciliationVoucher = new ReconciliationVoucher();
        reconciliationVoucher.setReconciliationType(reconciliationType);
        reconciliationVoucher.setCreateDate(new Date());
        reconciliationVoucher.setCreatedBy(loggedOnUser);
        reconciliationVoucher.setWarehouse(userService.getWarehouseForLoggedInUser());
        reconciliationVoucher.setRemarks(remark);
        reconciliationVoucher.setReconciliationDate(new Date());
        reconciliationVoucher = (ReconciliationVoucher) reconciliationVoucherDao.save(reconciliationVoucher);
        return reconciliationVoucher;
    }


    @Transactional
    public RvLineItem reconcileSKUItems(ReconciliationVoucher reconciliationVoucher, ReconciliationType reconciliationType, SkuItem skuItem, String remarks) {
        SkuGroup skuGroup = skuItem.getSkuGroup();
        Sku sku = skuGroup.getSku();
        Long subtractionType = reconciliationType.getId();
        EnumReconciliationType subtractTypeEnum = EnumReconciliationType.getEnumReconciliationTypeById(subtractionType);
        InvTxnType invTxnType = null;
        SkuItemStatus skuItemStatus = null;


        switch (subtractTypeEnum) {
            case SubtractDamage:
                invTxnType = inventoryService.getInventoryTxnType(EnumInvTxnType.RV_SUBTRACT_DAMAGED);
                skuItemStatus = EnumSkuItemStatus.Damaged.getSkuItemStatus();
                break;
            case SubtractExpired:
                invTxnType = inventoryService.getInventoryTxnType(EnumInvTxnType.RV_SUBTRACT_EXPIRED);
                skuItemStatus = EnumSkuItemStatus.Expired.getSkuItemStatus();
                break;
            case Lost:
                invTxnType = inventoryService.getInventoryTxnType(EnumInvTxnType.RV_LOST_PILFERAGE);
                skuItemStatus = EnumSkuItemStatus.Lost.getSkuItemStatus();
                break;
            case SubtractBatchMismatch:
                invTxnType = inventoryService.getInventoryTxnType(EnumInvTxnType.RV_SUBTRACT_BATCH_MISMATCH);
                skuItemStatus = EnumSkuItemStatus.BatchMismatch.getSkuItemStatus();
                break;
            case MrpMismatch:
                invTxnType = inventoryService.getInventoryTxnType(EnumInvTxnType.RV_MRP_MISMATCH);
                skuItemStatus = EnumSkuItemStatus.MrpMismatch.getSkuItemStatus();
                break;
            case NonMoving:
                invTxnType = inventoryService.getInventoryTxnType(EnumInvTxnType.RV_NON_MOVING);
                skuItemStatus = EnumSkuItemStatus.NonMoving.getSkuItemStatus();
                break;
            case SubtractFreeVariant:
                invTxnType = inventoryService.getInventoryTxnType(EnumInvTxnType.RV_SUBTRACT_FREE_VARIANT_RECONCILE);
                skuItemStatus = EnumSkuItemStatus.FreeVariant.getSkuItemStatus();
                break;
            case SubtractIncorrectCounting:
                invTxnType = inventoryService.getInventoryTxnType(EnumInvTxnType.RV_SUBTRACT_INCORRECT_COUNTING);
                skuItemStatus = EnumSkuItemStatus.IncorrectCounting.getSkuItemStatus();
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


        rvLineItem = (RvLineItem) getBaseDao().save(rvLineItem);
        adminInventoryService.inventoryCheckinCheckout(sku, skuItem, null, null, null, rvLineItem, null,
                invTxnType, -1L, userService.getLoggedInUser());
        if (skuItemStatus != null) {
            skuItem.setSkuItemStatus(skuItemStatus);
        }
        skuItem = skuGroupService.saveSkuItem(skuItem);
        if (reconciliationType.getId().equals(EnumReconciliationType.SubtractDamage.getId())) {
            adminInventoryService.damageInventoryCheckin(skuItem, null);
        }
        // Check inventory health now.
        inventoryService.checkInventoryHealth(rvLineItem.getSku().getProductVariant());
        return rvLineItem;
    }


    @Transactional
    public RvLineItem reconcileInventoryForPV(RvLineItem rvLineItem, List<SkuItem> inStockSkuItems, Sku sku) {
        RvLineItem rvLineItemSaved = null;
        User loggedOnUser = userService.getLoggedInUser();
        int deleteQty = rvLineItem.getQty().intValue();
        rvLineItem.setSku(sku);
        rvLineItem.setReconciledQty(rvLineItem.getQty());
        rvLineItem.setReconciliationType(EnumReconciliationType.ProductVariantAudited.asReconciliationType());
        rvLineItemSaved = (RvLineItem) reconciliationVoucherDao.save(rvLineItem);

        for (int i = 0; i < deleteQty; i++) {
            SkuItem skuItem = inStockSkuItems.get(i);
            //Delete -1 entry in PVI
            adminInventoryService.inventoryCheckinCheckout(sku, skuItem, null, null, null, rvLineItemSaved, null,
                    inventoryService.getInventoryTxnType(EnumInvTxnType.PRODUCT_VARIANT_AUDITED), -1L, loggedOnUser);

            //set sku item status to Product_variant_ Audited
            skuItem.setSkuItemStatus(EnumSkuItemStatus.ProductVariantAudited.getSkuItemStatus());
            skuItem = skuGroupService.saveSkuItem(skuItem);
        }

        // Check inventory health now.
        inventoryService.checkInventoryHealth(sku.getProductVariant());
        //save RvLine item

        return rvLineItemSaved;
    }


}
