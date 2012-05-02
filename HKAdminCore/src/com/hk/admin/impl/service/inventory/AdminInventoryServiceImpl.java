package com.hk.admin.impl.service.inventory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.admin.impl.dao.inventory.AdminProductVariantInventoryDao;
import com.hk.admin.impl.dao.inventory.AdminSkuItemDao;
import com.hk.admin.impl.dao.inventory.PVDamageInventoryDao;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.admin.util.BarcodeUtil;
import com.hk.constants.inventory.EnumInvTxnType;
import com.hk.dao.BaseDao;
import com.hk.dao.inventory.ProductVariantInventoryDao;
import com.hk.dao.order.OrderDao;
import com.hk.dao.shippingOrder.ShippingOrderDao;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.InvTxnType;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.GrnLineItem;
import com.hk.domain.inventory.ProductVariantDamageInventory;
import com.hk.domain.inventory.ProductVariantInventory;
import com.hk.domain.inventory.rv.ReconciliationVoucher;
import com.hk.domain.inventory.rv.RvLineItem;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.manager.UserManager;
import com.hk.service.InventoryService;
import com.hk.service.ProductVariantService;
import com.hk.service.SkuService;
import com.hk.service.UserService;

@Service
public class AdminInventoryServiceImpl implements AdminInventoryService {

    @Autowired
    private BaseDao                         baseDao;
    @Autowired
    private AdminProductVariantInventoryDao adminPVIDao;
    @Autowired
    private PVDamageInventoryDao            pVDamageInventoryDao;
    @Autowired
    private UserService                     userService;
    @Autowired
    private SkuService                      skuService;
    @Autowired
    private InventoryService                inventoryService;
    @Autowired
    private ProductVariantService           productVariantService;
    @Autowired
    private UserManager                     userManager;
    @Autowired
    private AdminSkuItemDao                 adminSkuItemDao;
    @Autowired
    private ShippingOrderDao                shippingOrderDao;
    @Autowired
    private OrderDao                        orderDao;
    @Autowired
    private ProductVariantInventoryDao      productVariantInventoryDao;

    @Override
    public List<SkuGroup> getInStockSkuGroups(String upc) {
        // ProductVariant productVariant = productVariantDaoProvider.get().findVariantFromUPC(upc);
        List<ProductVariant> productVariantList = getProductVariantService().findVariantListFromUPC(upc);
        if (productVariantList == null || productVariantList.isEmpty()) {
            productVariantList = new ArrayList<ProductVariant>();
            productVariantList.add(getProductVariantService().getVariantById(upc));// UPC not available must have
            // entered Variant
            // Id
        }
        Warehouse warehouse = getUserManager().getAssignedWarehouse();
        return getAdminSkuItemDao().getInStockSkuGroups(productVariantList, warehouse);
    }

    /**
     * @param sku
     * @return Inventory count of all action awaiting and in process orders
     */
    public Long getBookedInventory(Sku sku) {
        Long bookedInventoryForSku = getShippingOrderDao().getBookedQtyOfSkuInQueue(sku);
        Long bookedInventoryForProductVariant = getOrderDao().getBookedQtyOfProductVariantInQueue(sku.getProductVariant());
        return bookedInventoryForSku + bookedInventoryForProductVariant;
    }

    public Long getBookedInventory(ProductVariant productVariant) {
        List<Sku> skuList = getSkuService().getSKUsForProductVariant(productVariant);
        if (skuList != null && !skuList.isEmpty()) {
            Long bookedInventoryForSku = getShippingOrderDao().getBookedQtyOfSkuInQueue(skuList);
            Long bookedInventoryForProductVariant = getOrderDao().getBookedQtyOfProductVariantInQueue(productVariant);
            return bookedInventoryForSku + bookedInventoryForProductVariant;
        } else {
            return getOrderDao().getBookedQtyOfProductVariantInQueue(productVariant);
        }
    }

    public Long getNetInventory(Sku sku) {
        return getProductVariantInventoryDao().getNetInventory(sku);
    }

    public Long getNetInventory(ProductVariant productVariant) {
        List<Sku> variantSkus = skuService.getSKUsForProductVariant(productVariant);
        Long netInventory = getProductVariantInventoryDao().getNetInventory(variantSkus);
        return netInventory;
    }

    public void adjustInventory(SkuGroup skuGroup, Long qty) {
        int counter = 0;
        List<SkuItem> skuItems = getAdminSkuItemDao().getInStockSkuItems(skuGroup);
        for (SkuItem skuItem : skuItems) {
            getAdminPVIDao().removeInventory(skuItem);
            getAdminSkuItemDao().delete(skuItem);
            counter++;
            if (counter == qty)
                break;
        }
        if (skuGroup.getSkuItems().isEmpty()) {
            getBaseDao().delete(skuGroup);
        }
    }

    public SkuGroup createSkuGroup(String batch, Date mfgDate, Date expiryDate, GoodsReceivedNote goodsReceivedNote, ReconciliationVoucher reconciliationVoucher, Sku sku) {
        SkuGroup skuGroup = new SkuGroup();
        skuGroup.setBatchNumber(batch);
        skuGroup.setMfgDate(mfgDate);
        skuGroup.setExpiryDate(expiryDate);
        skuGroup.setGoodsReceivedNote(goodsReceivedNote);
        skuGroup.setReconciliationVoucher(reconciliationVoucher);
        skuGroup.setSku(sku);
        skuGroup.setCreateDate(new Date());
        skuGroup = (SkuGroup) getBaseDao().save(skuGroup);
        if (skuGroup != null && skuGroup.getId() != null) {
            String skuGroupBarCode = BarcodeUtil.generateBarCodeForSKuGroup(skuGroup.getId());
            skuGroup.setBarcode(skuGroupBarCode);
            skuGroup = (SkuGroup) getBaseDao().save(skuGroup);
        }

        return skuGroup;
    }

    public void createSkuItemsAndCheckinInventory(SkuGroup skuGroup, Long qty, LineItem lineItem, GrnLineItem grnLineItem, RvLineItem rvLineItem, InvTxnType invTxnType, User txnBy) {
        for (int i = 0; i < qty; i++) {
            SkuItem skuItem = new SkuItem();
            skuItem.setSkuGroup(skuGroup);
            skuItem.setCreateDate(new Date());
            skuItem = (SkuItem) getBaseDao().save(skuItem);

            skuItem.setBarcode(skuItem.getId().toString());
            skuItem = (SkuItem) getBaseDao().save(skuItem);

            this.inventoryCheckinCheckout(skuGroup.getSku(), skuItem, lineItem, null, grnLineItem, rvLineItem, invTxnType, 1L, txnBy);
        }
    }

    public void inventoryCheckinCheckout(Sku sku, SkuItem skuItem, LineItem lineItem, ShippingOrder shippingOrder, GrnLineItem grnLineItem, RvLineItem rvLineItem,
            InvTxnType invTxnType, Long qty, User txnBy) {
        ProductVariantInventory pvi = new ProductVariantInventory();
        // pvi.setProductVariant(sku.getProductVariant());
        pvi.setSku(sku);
        pvi.setSkuItem(skuItem);
        pvi.setLineItem(lineItem);
        pvi.setShippingOrder(shippingOrder);
        pvi.setGrnLineItem(grnLineItem);
        pvi.setRvLineItem(rvLineItem);
        pvi.setInvTxnType(invTxnType);
        pvi.setQty(qty);// +1 = Checkin -1 = Checkout
        pvi.setTxnBy(txnBy);
        pvi.setTxnDate(new Date());
        getBaseDao().save(pvi);

        // Setting checked in qty to make increments in sync with checkin
        if (grnLineItem != null && qty > 0) {
            grnLineItem.setCheckedInQty(grnLineItem.getCheckedInQty() + qty);
            getBaseDao().save(grnLineItem);
        }
    }

    public Long countOfCheckedInUnitsForGrnLineItem(GrnLineItem grnLineItem) {
        Long count = getAdminPVIDao().getChechedinItemCount(grnLineItem);
        if (count == null) {
            count = 0L;
        }
        return count;
    }

    public Long countOfCheckedOutUnitsOfLineItem(LineItem lineItem) {
        Long count = 0L;
        if (getAdminPVIDao().getCheckedoutItemCount(lineItem) != null) {
            count = getAdminPVIDao().getCheckedoutItemCount(lineItem);
        }
        return Math.abs(count);
    }

    public Boolean areAllUnitsOfOrderCheckedOut(ShippingOrder shippingOrder) {
        Long checkedOutCounter = 0L;
        Long qtyCounter = 0L;
        for (LineItem lineItem : shippingOrder.getLineItems()) {
            if (lineItem.getQty() != null) {
                qtyCounter += lineItem.getQty();
            }
            if (getAdminPVIDao().getCheckedoutItemCount(lineItem) != null)
                checkedOutCounter += getAdminPVIDao().getCheckedoutItemCount(lineItem);
        }

        if (checkedOutCounter != 0 && Math.abs(checkedOutCounter) == qtyCounter) {
            return true;
        }

        return false;
    }

    public void damageInventoryCheckin(SkuItem skuItem, LineItem lineItem) {
        if (getPVDamageInventoryDao().getCheckedInPVDI(skuItem) == null) {
            ProductVariantDamageInventory pvi = new ProductVariantDamageInventory();
            pvi.setSku(skuItem.getSkuGroup().getSku());
            pvi.setSkuItem(skuItem);
            pvi.setLineItem(lineItem);
            pvi.setShippingOrder(lineItem != null ? lineItem.getShippingOrder() : null);
            pvi.setQty(1L);// Checkin
            pvi.setTxnDate(new Date());
            getPVDamageInventoryDao().save(pvi);
        }

    }

    public void reCheckInInventory(ShippingOrder shippingOrder) {
        // Recheckin InventoInry against checked out qty
        // if (shippingOrder.getOrderStatus().getId().equals(EnumShippingOrderStatus.SO_CheckedOut.getId())) {
        for (LineItem lineItem : shippingOrder.getLineItems()) {
            List<ProductVariantInventory> checkedOutInventories = getAdminPVIDao().getCheckedOutSkuItems(lineItem.getShippingOrder(), lineItem);
            for (ProductVariantInventory checkedOutInventory : checkedOutInventories) {
                this.inventoryCheckinCheckout(checkedOutInventory.getSku(), checkedOutInventory.getSkuItem(), lineItem, lineItem.getShippingOrder(),
                        checkedOutInventory.getGrnLineItem(), checkedOutInventory.getRvLineItem(), getInventoryService().getInventoryTxnType(EnumInvTxnType.CANCEL_CHECKIN), 1L,
                        getUserService().getLoggedInUser());
                // Bug Fix deleting checked out pvi to maintain sanity instead of re-checkin.
                // productVariantInventoryDaoProvider.get().remove(checkedOutInventory.getId());
                // Rechecking Inventory Health to mark variants instock/outofstock properly.
                getInventoryService().checkInventoryHealth(checkedOutInventory.getSku().getProductVariant());
            }
        }
        // }
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

    public AdminProductVariantInventoryDao getAdminPVIDao() {
        return adminPVIDao;
    }

    public void setAdminPVIDao(AdminProductVariantInventoryDao adminPVIDao) {
        this.adminPVIDao = adminPVIDao;
    }

    public PVDamageInventoryDao getPVDamageInventoryDao() {
        return pVDamageInventoryDao;
    }

    public void setPVDamageInventoryDao(PVDamageInventoryDao damageInventoryDao) {
        pVDamageInventoryDao = damageInventoryDao;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public InventoryService getInventoryService() {
        return inventoryService;
    }

    public void setInventoryService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public ProductVariantService getProductVariantService() {
        return productVariantService;
    }

    public void setProductVariantService(ProductVariantService productVariantService) {
        this.productVariantService = productVariantService;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public AdminSkuItemDao getAdminSkuItemDao() {
        return adminSkuItemDao;
    }

    public void setAdminSkuItemDao(AdminSkuItemDao adminSkuItemDao) {
        this.adminSkuItemDao = adminSkuItemDao;
    }

    public ShippingOrderDao getShippingOrderDao() {
        return shippingOrderDao;
    }

    public void setShippingOrderDao(ShippingOrderDao shippingOrderDao) {
        this.shippingOrderDao = shippingOrderDao;
    }

    public SkuService getSkuService() {
        return skuService;
    }

    public void setSkuService(SkuService skuService) {
        this.skuService = skuService;
    }

    public OrderDao getOrderDao() {
        return orderDao;
    }

    public void setOrderDao(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public ProductVariantInventoryDao getProductVariantInventoryDao() {
        return productVariantInventoryDao;
    }

    public void setProductVariantInventoryDao(ProductVariantInventoryDao productVariantInventoryDao) {
        this.productVariantInventoryDao = productVariantInventoryDao;
    }

}
