package com.hk.admin.impl.service.inventory;

import com.hk.admin.dto.inventory.CreateInventoryFileDto;
import com.hk.admin.pact.dao.inventory.AdminProductVariantInventoryDao;
import com.hk.admin.pact.dao.inventory.AdminSkuItemDao;
import com.hk.admin.pact.dao.inventory.ProductVariantDamageInventoryDao;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.admin.pact.service.inventory.GrnLineItemService;
import com.hk.admin.util.BarcodeUtil;
import com.hk.constants.inventory.EnumInvTxnType;
import com.hk.constants.sku.EnumSkuItemOwner;
import com.hk.constants.sku.EnumSkuItemStatus;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.catalog.product.VariantConfig;
import com.hk.domain.core.InvTxnType;
import com.hk.domain.inventory.*;
import com.hk.domain.inventory.rv.ReconciliationVoucher;
import com.hk.domain.inventory.rv.RvLineItem;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.sku.SkuItemCLI;
import com.hk.domain.sku.SkuItemLineItem;
import com.hk.domain.sku.SkuItemStatus;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.manager.UserManager;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.inventory.ProductVariantInventoryDao;
import com.hk.pact.dao.order.OrderDao;
import com.hk.pact.dao.shippingOrder.ShippingOrderDao;
import com.hk.pact.dao.sku.SkuItemLineItemDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.inventory.SkuGroupService;
import com.hk.pact.service.inventory.SkuItemLineItemService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.web.action.admin.inventory.ReconciliationVoucherAction;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class AdminInventoryServiceImpl implements AdminInventoryService {

	private static Logger logger = Logger.getLogger(AdminInventoryServiceImpl.class);
    @Autowired
    private BaseDao baseDao;
    @Autowired
    private AdminProductVariantInventoryDao adminPVIDao;
    @Autowired
    private ProductVariantDamageInventoryDao pVDamageInventoryDao;
    @Autowired
    private UserService userService;
    @Autowired
    private SkuService skuService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private ProductVariantService productVariantService;
    @Autowired
    private UserManager userManager;
    @Autowired
    private AdminSkuItemDao adminSkuItemDao;
    @Autowired
    private ShippingOrderDao shippingOrderDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ProductVariantInventoryDao productVariantInventoryDao;
    @Autowired
    private SkuGroupService skuGroupService;
    @Autowired
    GrnLineItemService grnLineItemService;
    @Autowired
    SkuItemLineItemService skuItemLineItemService;
    @Autowired
    SkuItemLineItemDao skuItemLineItemDao;


    
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




     public List<SkuGroup> getInStockSkuGroupsForReview(LineItem lineItem) {
        return getAdminSkuItemDao().getInStockSkuGroupsForReview(lineItem);
    }



    public List<SkuGroup> getSkuGroupsInReviewState() {
           return getAdminSkuItemDao().getSkuGroupsInReviewState();
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
        //List<Sku> skuList = getSkuService().getSKUsForProductVariant(productVariant);
        List<Sku> skuList = getSkuService().getSKUsForProductVariantAtServiceableWarehouses(productVariant);
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

    public Long getNetInventoryAtServiceableWarehouses(ProductVariant productVariant) {
        List<Sku> variantSkus = skuService.getSKUsForProductVariantAtServiceableWarehouses(productVariant);
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

    public SkuGroup createSkuGroup(String batch, Date mfgDate, Date expiryDate, Double costPrice, Double mrp, GoodsReceivedNote goodsReceivedNote, ReconciliationVoucher reconciliationVoucher, StockTransfer stockTransfer, Sku sku) {
//        SkuGroup skuGroup = new SkuGroup();
//        skuGroup.setBatchNumber(batch);
//        skuGroup.setMfgDate(mfgDate);
//        skuGroup.setExpiryDate(expiryDate);
//        skuGroup.setCostPrice(costPrice);
//        skuGroup.setMrp(mrp);
//        skuGroup.setGoodsReceivedNote(goodsReceivedNote);
//        skuGroup.setReconciliationVoucher(reconciliationVoucher);
//        skuGroup.setStockTransfer(stockTransfer);
//        skuGroup.setSku(sku);
//        skuGroup.setCreateDate(new Date());
//        skuGroup = (SkuGroup) getBaseDao().save(skuGroup);
        SkuGroup skuGroup = createSkuGroupWithoutBarcode(batch, mfgDate, expiryDate, costPrice, mrp, goodsReceivedNote, reconciliationVoucher, stockTransfer, sku);
        if (skuGroup != null && skuGroup.getId() != null) {
            String skuGroupBarCode = BarcodeUtil.generateBarCodeForSKuGroup(skuGroup.getId());
            skuGroup.setBarcode(skuGroupBarCode);
            skuGroup = (SkuGroup) getBaseDao().save(skuGroup);
        }

        return skuGroup;
    }


    public SkuGroup createSkuGroupWithoutBarcode(String batch, Date mfgDate, Date expiryDate, Double costPrice, Double mrp, GoodsReceivedNote goodsReceivedNote, ReconciliationVoucher reconciliationVoucher, StockTransfer stockTransfer, Sku sku) {
        SkuGroup skuGroup = new SkuGroup();
        skuGroup.setBatchNumber(batch);
        skuGroup.setMfgDate(mfgDate);
        skuGroup.setExpiryDate(expiryDate);
        skuGroup.setCostPrice(costPrice);
        skuGroup.setMrp(mrp);
        skuGroup.setGoodsReceivedNote(goodsReceivedNote);
        skuGroup.setReconciliationVoucher(reconciliationVoucher);
        skuGroup.setStockTransfer(stockTransfer);
        skuGroup.setSku(sku);
        skuGroup.setCreateDate(new Date());
        skuGroup = (SkuGroup) getBaseDao().save(skuGroup);
        return skuGroup;
    }


    public void createSkuItemsAndCheckinInventory(SkuGroup skuGroup, Long qty, LineItem lineItem, GrnLineItem grnLineItem, RvLineItem rvLineItem,
                                                  StockTransferLineItem stockTransferLineItem, InvTxnType invTxnType, User txnBy) {
        for (int i = 0; i < qty; i++) {
            SkuItem skuItem = new SkuItem();
            skuItem.setSkuGroup(skuGroup);
            skuItem.setCreateDate(new Date());
            skuItem.setSkuItemStatus(EnumSkuItemStatus.Checked_IN.getSkuItemStatus());
            skuItem.setSkuItemOwner(EnumSkuItemOwner.SELF.getSkuItemOwnerStatus());
//            skuItem = (SkuItem) getBaseDao().save(skuItem);

            //  generating Barcode at Skuitem level
            String skuItemBarcode = BarcodeUtil.generateBarCodeForSKuItem(skuGroup.getId(), i + 1);
            skuItem.setBarcode(skuItemBarcode);
            skuItem = (SkuItem) getBaseDao().save(skuItem);

            this.inventoryCheckinCheckout(skuGroup.getSku(), skuItem, lineItem, null, grnLineItem, rvLineItem, stockTransferLineItem, invTxnType, 1L, txnBy);
        }
    }

    public void inventoryCheckinCheckout(Sku sku, SkuItem skuItem, LineItem lineItem, ShippingOrder shippingOrder, GrnLineItem grnLineItem, RvLineItem rvLineItem,
                                         StockTransferLineItem stockTransferLineItem, InvTxnType invTxnType, Long qty, User txnBy) {
        ProductVariantInventory pvi = new ProductVariantInventory();
        // pvi.setProductVariant(sku.getProductVariant());
        pvi.setSku(sku);
        pvi.setSkuItem(skuItem);
        pvi.setLineItem(lineItem);
        pvi.setShippingOrder(shippingOrder);
        pvi.setGrnLineItem(grnLineItem);
        pvi.setRvLineItem(rvLineItem);
        pvi.setStockTransferLineItem(stockTransferLineItem);
        pvi.setInvTxnType(invTxnType);
        pvi.setQty(qty);// +1 = Checkin -1 = Checkout
        pvi.setTxnBy(txnBy);
        pvi.setTxnDate(new Date());
        getBaseDao().save(pvi);

        if (skuItem != null && qty < 0) {
            skuItem.setSkuItemStatus(EnumSkuItemStatus.Checked_OUT.getSkuItemStatus());
            skuItem.setSkuItemOwner(EnumSkuItemOwner.CUSTOMER.getSkuItemOwnerStatus());
            getBaseDao().save(skuItem);
        }

        if (skuItem != null && qty > 0) {
            skuItem.setSkuItemStatus(EnumSkuItemStatus.Checked_IN.getSkuItemStatus());
            skuItem.setSkuItemOwner(EnumSkuItemOwner.SELF.getSkuItemOwnerStatus());
            getBaseDao().save(skuItem);
        }

        // Setting checked in qty to make increments in sync with checkin
        if (grnLineItem != null && qty > 0) {
            grnLineItem.setCheckedInQty(grnLineItem.getCheckedInQty() + qty);
            getBaseDao().save(grnLineItem);
        }
    }

    public void inventoryCheckoutForStockTransfer(Sku sku, SkuItem skuItem, StockTransferLineItem stockTransferLineItem, Long qty, User txnBy) {
        ProductVariantInventory pvi = new ProductVariantInventory();
        pvi.setSku(sku);
        pvi.setSkuItem(skuItem);
        pvi.setStockTransferLineItem(stockTransferLineItem);
        pvi.setInvTxnType(inventoryService.getInventoryTxnType(EnumInvTxnType.STOCK_TRANSFER_CHECKOUT));
        pvi.setQty(qty);
        pvi.setTxnBy(txnBy);
        pvi.setTxnDate(new Date());
        getBaseDao().save(pvi);

        if (skuItem != null && qty < 0) {
            skuItem.setSkuItemStatus(EnumSkuItemStatus.Stock_Transfer_Out.getSkuItemStatus());
            skuItem.setSkuItemOwner(EnumSkuItemOwner.SELF.getSkuItemOwnerStatus());
            getBaseDao().save(skuItem);
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
        //User loggedOnUser = UserCache.getInstance().getLoggedInUser();
        User loggedOnUser = userService.getLoggedInUser();
        for (LineItem lineItem : shippingOrder.getLineItems()) {
            List<ProductVariantInventory> checkedOutInventories = getAdminPVIDao().getCheckedOutSkuItems(lineItem.getShippingOrder(), lineItem);
            for (ProductVariantInventory checkedOutInventory : checkedOutInventories) {
                this.inventoryCheckinCheckout(checkedOutInventory.getSku(), checkedOutInventory.getSkuItem(), lineItem, lineItem.getShippingOrder(),
                        checkedOutInventory.getGrnLineItem(), checkedOutInventory.getRvLineItem(), checkedOutInventory.getStockTransferLineItem(),
                        getInventoryService().getInventoryTxnType(EnumInvTxnType.CANCEL_CHECKIN), 1L, loggedOnUser);
                // Rechecking Inventory Health to mark variants instock/outofstock properly.
                getInventoryService().checkInventoryHealth(checkedOutInventory.getSku().getProductVariant());
            }
        }
        // }
    }

    public Long countOfCheckedInUnitsForStockTransferLineItem(StockTransferLineItem stockTransferLineItem) {
        Long count = getAdminPVIDao().getCheckedinItemCountForStockTransferLineItem(stockTransferLineItem);
        if (count == null) {
            count = 0L;
        }
        return count;
    }


    public List<VariantConfig> getAllVariantConfig() {
        return getAdminPVIDao().getAllVariantConfig();
    }

    public Map<Long, String> skuItemBarcodeMap(List<SkuItem> checkedInSkuItems) {
        int strLength = 20;
        SkuItem skuItem = checkedInSkuItems.get(0);
        Map<Long, String> skuItemBarcodeMap = new HashMap<Long, String>();
        ProductVariant productVariant = skuItem.getSkuGroup().getSku().getProductVariant();
        String productOptionStringBuffer = productVariant.getOptionsPipeSeparated();
        SkuGroup checkedInSkuGroup;// = skuItem.getSkuGroup();
        Date expiryDate;// = checkedInSkuGroup.getExpiryDate();


        String date = "";

        for (SkuItem checkedInSkuItem : checkedInSkuItems) {
            checkedInSkuGroup = checkedInSkuItem.getSkuGroup();
            expiryDate = checkedInSkuGroup.getExpiryDate();
            if (expiryDate == null) {
                date = "NA";
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                date = sdf.format(expiryDate);
            }
            String data = "";
            if (checkedInSkuItem.getSkuGroup().getBarcode() == null && checkedInSkuItem.getBarcode().contains(BarcodeUtil.BARCODE_SKU_ITEM_PREFIX)) {
                data = checkedInSkuItem.getBarcode() + "\t" + StringUtils.substring(productVariant.getProduct().getName(), 0, strLength) + "\t"
                        + StringUtils.substring(productOptionStringBuffer, 0, strLength) + "\t" + date + "\t" + 1 + "\t" + checkedInSkuGroup.getMrp();
            } else {
                data = checkedInSkuItem.getSkuGroup().getBarcode() + "\t" + StringUtils.substring(productVariant.getProduct().getName(), 0, strLength) + "\t"
                        + StringUtils.substring(productOptionStringBuffer, 0, strLength) + "\t" + date + "\t" + 1 + "\t" + checkedInSkuGroup.getMrp();
            }
            if (!skuItemBarcodeMap.containsKey(checkedInSkuItem.getId())) {
                skuItemBarcodeMap.put(checkedInSkuItem.getId(), data);
            }

        }
        return skuItemBarcodeMap;
    }

    public void checkoutMethod(LineItem lineItem, SkuItem skuItem) {
        User loggedOnUser = userService.getLoggedInUser();
        List<SkuItemLineItem> skuItemLineItems = skuItemLineItemService.getSkuItemLineItem(lineItem, EnumSkuItemStatus.BOOKED.getId());
        List<SkuItem> skuItemInSkuItemLineItems = new ArrayList<SkuItem>();
        for(SkuItemLineItem item : skuItemLineItems){
            SkuItem si = item.getSkuItem();
            skuItemInSkuItemLineItems.add(si);
        }
        if (skuItemInSkuItemLineItems.contains(skuItem)) {
            // TODO:
            skuItem.setSkuItemStatus(EnumSkuItemStatus.Checked_OUT.getSkuItemStatus());
            skuItem.setSkuItemOwner(EnumSkuItemOwner.CUSTOMER.getSkuItemOwnerStatus());
            skuItem = (SkuItem)baseDao.save(skuItem);
            inventoryCheckinCheckout(lineItem.getSku(), skuItem, lineItem, lineItem.getShippingOrder(), null, null, null, inventoryService.getInventoryTxnType(EnumInvTxnType.INV_CHECKOUT), -1l,loggedOnUser );
            logger.debug("Checking Out SkuItem - "+skuItem.getId()+" at Checkout");
            
        } else {
            //If skuItem is booked
            if (skuItem.getSkuItemStatus().getId().equals(EnumSkuItemStatus.BOOKED.getId()) || skuItem.getSkuItemStatus().getId().equals(EnumSkuItemStatus.Checked_IN.getId())) {
                SkuItem toReleaseSkuItem = skuItemInSkuItemLineItems.get(0);
                
                skuItem.setSkuItemStatus(EnumSkuItemStatus.Checked_OUT.getSkuItemStatus());
                skuItem.setSkuItemOwner(EnumSkuItemOwner.CUSTOMER.getSkuItemOwnerStatus());
                skuItem = (SkuItem)baseDao.save(skuItem);
                logger.debug("Checking Out SkuItem - "+skuItem.getId()+" at Checkout");
                
                toReleaseSkuItem.setSkuItemStatus(EnumSkuItemStatus.Checked_IN.getSkuItemStatus());
                toReleaseSkuItem.setSkuItemOwner(EnumSkuItemOwner.SELF.getSkuItemOwnerStatus());
                toReleaseSkuItem = (SkuItem)baseDao.save(toReleaseSkuItem);
                inventoryCheckinCheckout(lineItem.getSku(), skuItem, lineItem, lineItem.getShippingOrder(), null, null, null, inventoryService.getInventoryTxnType(EnumInvTxnType.INV_CHECKOUT), -1l,loggedOnUser );
                logger.debug("Releasing SkuItem - "+toReleaseSkuItem.getId()+" at Checkout");
                
                SkuItemCLI skuItemCLI = skuItemLineItemDao.getSkuItemCLI(toReleaseSkuItem);
                skuItemCLI.setSkuItem(skuItem);
                skuItemCLI = (SkuItemCLI) baseDao.save(skuItemCLI);
                
                
                SkuItemLineItem skuItemLineItem = skuItemLineItemDao.getSkuItemLineItem(toReleaseSkuItem);
                skuItemCLI.setSkuItem(skuItem);
                skuItemLineItem = (SkuItemLineItem) baseDao.save(skuItemLineItem);
            }
        }
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

    public ProductVariantDamageInventoryDao getPVDamageInventoryDao() {
        return pVDamageInventoryDao;
    }

    public void setPVDamageInventoryDao(ProductVariantDamageInventoryDao damageInventoryDao) {
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

    public List<SkuItem> getInStockSkuItems(List<SkuGroup> skuGroupList) {
        return adminSkuItemDao.getInStockSkuItems(skuGroupList);
    }

    public List<SkuItem> getInStockSkuItems(SkuGroup skuGroup) {
        return adminSkuItemDao.getInStockSkuItems(skuGroup);
    }

    public List<SkuItem> getInStockSkuItems(String barcode, Warehouse warehouse) {
        return adminSkuItemDao.getInStockSkuItems(barcode, warehouse);
    }


    public List<SkuItem> getCheckedInOrOutSkuItems(RvLineItem rvLineItem, StockTransferLineItem stockTransferLineItem, GrnLineItem grnLineItem, LineItem lineItem, Long transferQty) {
        return adminPVIDao.getCheckedInOrOutSkuItems(rvLineItem, stockTransferLineItem, grnLineItem, lineItem, transferQty);
    }

    public List<CreateInventoryFileDto> getCheckedInSkuGroup(String brand, Warehouse warehouse, Product product, ProductVariant productVariant) {
        return adminPVIDao.getCheckedInSkuGroup(brand, warehouse, product, productVariant);
    }

    private void deletePVIBySkuItem(List<SkuItem> skuItemList) {
        adminPVIDao.deletePVIBySkuItem(skuItemList);

    }

    @Transactional
    public void deleteInventory(GrnLineItem grnLineItem) {
        List<SkuGroup> skuGroupList = skuGroupService.getSkuGroupByGrnLineItem(grnLineItem);
        Set<SkuItem> skuItems = new HashSet<SkuItem>();
        if (skuGroupList != null && skuGroupList.size() > 0) {
            //Assuming grn+sku combination is  not unique
            for (SkuGroup skuGroup : skuGroupList) {
                Set<SkuItem> skuItemSet = skuGroup.getSkuItems();
                List<SkuItem> skuItemList = new ArrayList<SkuItem>(skuItemSet);
                //Delete PVI
                deletePVIBySkuItem(skuItemList);
                //Delete SkuItem
                skuGroupService.deleteAllSkuItemsOfSkuGroup(skuGroup);
                //Delete Sku Group
                skuGroupService.deleteSkuGroup(skuGroup);

            }
            //delete Grn Line Item
            grnLineItemService.delete(grnLineItem);

        }
    }

}
