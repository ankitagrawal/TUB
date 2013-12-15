package com.hk.admin.impl.service.courier.reversePickup;

import com.akube.framework.dao.Page;
import com.hk.admin.pact.dao.courier.reversePickup.ReversePickupDao;
import com.hk.admin.pact.dao.inventory.AdminProductVariantInventoryDao;
import com.hk.admin.pact.service.courier.reversePickup.ReversePickupService;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.constants.analytics.EnumReason;
import com.hk.constants.inventory.EnumInvTxnType;
import com.hk.constants.queue.EnumClassification;
import com.hk.constants.reversePickup.EnumReverseAction;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.constants.sku.EnumSkuItemOwner;
import com.hk.constants.sku.EnumSkuItemStatus;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.ProductVariantInventory;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.reversePickupOrder.ReversePickupOrder;
import com.hk.domain.reversePickupOrder.ReversePickupStatus;
import com.hk.domain.reversePickupOrder.ReversePickupType;
import com.hk.domain.reversePickupOrder.RpLineItem;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.sku.SkuItemStatus;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;

import com.hk.exception.ReversePickupException;
import com.hk.pact.dao.sku.SkuItemDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.inventory.SkuGroupService;
import com.hk.pact.service.inventory.SkuItemLineItemService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Seema
 * Date: 7/31/13
 * Time: 2:16 AM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class ReversePickupServiceImpl implements ReversePickupService {

    @Autowired
    ReversePickupDao reversePickupDao;
    @Autowired
    SkuGroupService skuGroupService;
    @Autowired
    UserService userService;
    @Autowired
    AdminInventoryService adminInvService;
    @Autowired
    private AdminProductVariantInventoryDao adminProductVariantInventoryDao;
    @Autowired
    private AdminInventoryService adminInventoryService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private SkuItemDao skuItemDao;
    @Autowired
    private ShippingOrderService shippingOrderService;
    @Autowired
    SkuItemLineItemService skuItemLineItemService;

    @Transactional
    public ReversePickupOrder saveReversePickupOrder(ReversePickupOrder reversePickupOrder) {
        return (ReversePickupOrder) reversePickupDao.save(reversePickupOrder);
    }

    @Transactional
    public RpLineItem saveRpLineItem(RpLineItem rpLineItem) {
        return (RpLineItem) reversePickupDao.save(rpLineItem);

    }

    @Transactional
    /** Assumption :the status should follow the sequence. one can not set directly to approve state.It has to go through pending status**/
    public void saveRpLineItems(ReversePickupOrder reversePickupOrder, List<RpLineItem> rpLineItemList, Long customerActionStatus) {
        if (rpLineItemList != null) {
            for (RpLineItem rpLineItem : rpLineItemList) {
                if (reversePickupOrder != null) {
                    rpLineItem.setReversePickupOrder(reversePickupOrder);
                }
                if (rpLineItem.getActionTaken() == null || rpLineItem.getActionTaken().getId().equals(EnumClassification.Decide_Later.getId())) {
                    rpLineItem.setCustomerActionStatus(null);
                } else {
                    rpLineItem.setCustomerActionStatus(EnumClassification.Pending_Approval.asClassification());
                }
                saveRpLineItem(rpLineItem);
            }
        }
    }

    public void updateRpLineItems(List<RpLineItem> rpLineItemList, Long customerActionStatus) {
        saveRpLineItems(null, rpLineItemList, customerActionStatus);
    }

    public Page getReversePickRequest(ShippingOrder shippingOrder, String reversePickupId, Date startDate, Date endDate, Long customerActionStatus, List<ReversePickupStatus> reversePickupStatusList, String courierName, int pageNo, int perPage, List<ReversePickupType> reversePickupTypeList) {
        return reversePickupDao.getReversePickRequest(shippingOrder, reversePickupId, startDate, endDate, customerActionStatus, reversePickupStatusList, courierName, pageNo, perPage, reversePickupTypeList);

    }

    @Transactional
    private void deleteRpLineItems(List<RpLineItem> rpLineItemList) {
        if (rpLineItemList != null) {
            ReversePickupOrder reversePickupOrder = rpLineItemList.get(0).getReversePickupOrder();
            reversePickupOrder.setRpLineItems(null);
            saveReversePickupOrder(reversePickupOrder);
            for (RpLineItem rpLineItem : rpLineItemList) {
                reversePickupDao.delete(rpLineItem);
            }
        }
    }

    @Transactional
    public void deleteRpLineItem(RpLineItem rpLineItem) {
        if (rpLineItem != null) {
            ReversePickupOrder reversePickupOrder = rpLineItem.getReversePickupOrder();
            List<RpLineItem> allRpLineItems = reversePickupOrder.getRpLineItems();
            allRpLineItems.remove(rpLineItem);
            reversePickupOrder.setRpLineItems(allRpLineItems);
            saveReversePickupOrder(reversePickupOrder);
            reversePickupDao.delete(rpLineItem);
        }
    }

    @Transactional
    public void SaveModifiedRpLineItems(List<RpLineItem> oldRpLineItems, ReversePickupOrder reversePickupOrder, List<RpLineItem> newRpLineItems) {
        /*delete old rpLineItems */
        deleteRpLineItems(oldRpLineItems);
        /*save new rpLineItems*/
        saveRpLineItems(reversePickupOrder, newRpLineItems, EnumReverseAction.Pending_Approval.getId());
    }

    public List<ReversePickupOrder> getReversePickupsForSO(ShippingOrder shippingOrder) {
        return reversePickupDao.getReversePickupsForSO(shippingOrder);
    }

    public List<ReversePickupOrder> getReversePickupsExcludeCurrentRP(ShippingOrder shippingOrder, ReversePickupOrder reversePickupOrder) {
        return reversePickupDao.getReversePickupsExcludeCurrentRP(shippingOrder, reversePickupOrder);
    }

    @Transactional
    public void deleteReversePickupOrder(ReversePickupOrder reversePickupOrder) {
        if (reversePickupOrder != null) {
            reversePickupDao.delete(reversePickupOrder);
        }
    }

    public RpLineItem getRpLineItemById(Long id) {
        return reversePickupDao.get(RpLineItem.class, id);
    }

    public ReversePickupOrder getReversePickupOrderById(Long id) {
        return reversePickupDao.get(ReversePickupOrder.class, id);
    }

    public ReversePickupOrder getByReversePickupId(String reversePickupId) {
        return reversePickupDao.getByReversePickupId(reversePickupId);
    }

    @Transactional
    public Boolean checkInRpLineItem(RpLineItem rpLineItem) throws ReversePickupException {
        boolean warehouseChecked = false;
        if (rpLineItem != null) {
            Warehouse warehouse = userService.getWarehouseForLoggedInUser();
            ShippingOrder shippingOrder = rpLineItem.getReversePickupOrder().getShippingOrder();
            LineItem lineItem = rpLineItem.getLineItem();
            String recheckInBarcode = rpLineItem.getItemBarcode();
            ProductVariant productVariant = lineItem.getSku().getProductVariant();
            User user = userService.getLoggedInUser();
            List<ProductVariantInventory> pviCheckoutEntries = getAdminProductVariantInventoryDao().getCheckedOutSkuItems(shippingOrder, lineItem);
            SkuItem findSkuItemByBarcode = null;
            String skuGroupBarcode, skuItemBarcode;
            User loggedOnUser = userService.getLoggedInUser();
            if (pviCheckoutEntries != null && !pviCheckoutEntries.isEmpty()) {
                findSkuItemByBarcode = skuGroupService.getSkuItemByBarcode(recheckInBarcode, warehouse.getId(), EnumSkuItemStatus.Checked_OUT.getId());
                for (ProductVariantInventory pvi : pviCheckoutEntries) {
                    SkuItem skuItem;
                    if (findSkuItemByBarcode != null) {
                        skuItem = findSkuItemByBarcode;
                        skuItemBarcode = findSkuItemByBarcode.getBarcode();
                    } else {
                        skuItem = pvi.getSkuItem();
                        skuGroupBarcode = pvi.getSkuItem().getSkuGroup().getBarcode();
                        skuItemBarcode = skuGroupBarcode;
                    }
                    if (skuItemBarcode != null && skuItemBarcode.equalsIgnoreCase(recheckInBarcode)) {
                        if (skuItem.getId().equals(pvi.getSkuItem().getId()) &&  pvi.getSkuItem().getSkuItemStatus().equals(EnumSkuItemStatus.Checked_OUT.getSkuItemStatus())) {
                            EnumReason enumReason = EnumReason.getById(rpLineItem.getWarehouseReceivedCondition().getId());
                            SkuItemStatus skuItemStatus = null;
                            switch (enumReason) {
                                case Good:
                                    getAdminInventoryService().inventoryCheckinCheckout(pvi.getSku(), skuItem, lineItem, shippingOrder, null, null, null,
                                            EnumSkuItemStatus.Checked_IN_Non_Saleable, EnumSkuItemOwner.SELF, EnumInvTxnType.REVERSE_PICKUP_INVENTORY_CHECKIN.asInvTxnType(), 0l, loggedOnUser);
                                    warehouseChecked = true;
                                    break;
                                case Damaged:
                                    getAdminInventoryService().inventoryCheckinCheckout(pvi.getSku(), skuItem, lineItem, shippingOrder, null, null, null,
                                            EnumSkuItemStatus.Damaged, EnumSkuItemOwner.SELF, EnumInvTxnType.REVERSE_PICKUP_INVENTORY_CHECKIN.asInvTxnType(), 0l, loggedOnUser);
                                    inventoryService.checkInventoryHealth(productVariant);
                                    warehouseChecked = true;
                                    break;
                                case Non_Functional:
                                    /**/
                                case Near_Expiry:
                                    /**/
                                case Expired:
                                    getAdminInventoryService().inventoryCheckinCheckout(pvi.getSku(), skuItem, lineItem, shippingOrder, null, null, null,
                                            EnumSkuItemStatus.Expired, EnumSkuItemOwner.SELF, EnumInvTxnType.REVERSE_PICKUP_INVENTORY_CHECKIN.asInvTxnType(), 0l, loggedOnUser);
                                    inventoryService.checkInventoryHealth(productVariant);
                                    warehouseChecked = true;
                                    break;
                            }
                            inventoryService.checkInventoryHealth(productVariant);
                            if(warehouseChecked){
                               break;
                           }

                        }
                    }
                }
            } else {
                throw new ReversePickupException("No Items remains  Checkout ", productVariant);
            }
            if (warehouseChecked) {
                saveRpLineItem(rpLineItem);
                String comments = "Inventory checked in : " + rpLineItem.getWarehouseReceivedCondition().getClassification().getPrimary() + " for Product " + productVariant.getProduct().getName() + "<br/>" +
                        productVariant.getOptionsCommaSeparated();
                shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_ReCheckedIn, null, comments);
                skuItemLineItemService.freeInventoryForRTOCheckIn(shippingOrder);
            } else {
                throw new ReversePickupException("The Barcode entered doesn't match any of the items OR item not in correct status   ", productVariant);
            }
        }
        return warehouseChecked;
    }

    public AdminProductVariantInventoryDao getAdminProductVariantInventoryDao() {
        return adminProductVariantInventoryDao;
    }

    public void setAdminProductVariantInventoryDao(AdminProductVariantInventoryDao adminProductVariantInventoryDao) {
        this.adminProductVariantInventoryDao = adminProductVariantInventoryDao;
    }


    public AdminInventoryService getAdminInventoryService() {
        return adminInventoryService;
    }

    public void setAdminInventoryService(AdminInventoryService adminInventoryService) {
        this.adminInventoryService = adminInventoryService;
    }

    public InventoryService getInventoryService() {
        return inventoryService;
    }

    public void setInventoryService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public SkuItemDao getSkuItemDao() {
        return skuItemDao;
    }

    public void setSkuItemDao(SkuItemDao skuItemDao) {
        this.skuItemDao = skuItemDao;
    }

    public ShippingOrderService getShippingOrderService() {
        return shippingOrderService;
    }

    public void setShippingOrderService(ShippingOrderService shippingOrderService) {
        this.shippingOrderService = shippingOrderService;
    }
}