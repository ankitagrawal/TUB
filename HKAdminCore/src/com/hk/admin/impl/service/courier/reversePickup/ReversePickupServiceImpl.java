package com.hk.admin.impl.service.courier.reversePickup;

import com.akube.framework.dao.Page;
import com.hk.admin.pact.dao.courier.reversePickup.ReversePickupDao;
import com.hk.admin.pact.service.courier.reversePickup.ReversePickupService;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.constants.analytics.EnumReason;
import com.hk.constants.inventory.EnumInvTxnType;
import com.hk.constants.reversePickup.EnumReverseAction;
import com.hk.constants.reversePickup.EnumReverseActionOnStatus;
import com.hk.constants.sku.EnumSkuItemStatus;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.reversePickupOrder.ReversePickupOrder;
import com.hk.domain.reversePickupOrder.ReversePickupStatus;
import com.hk.domain.reversePickupOrder.RpLineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.sku.SkuItemStatus;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;

import com.hk.pact.service.UserService;
import com.hk.pact.service.inventory.SkuGroupService;
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
                if (rpLineItem.getActionTaken() == null || rpLineItem.getActionTaken().equals(EnumReverseAction.Decide_Later.getId())) {
                    rpLineItem.setCustomerActionStatus(null);
                } else {
                    rpLineItem.setCustomerActionStatus(EnumReverseAction.Pending_Approval.getId());
                }
                saveRpLineItem(rpLineItem);
            }
        }
    }

    public void updateRpLineItems(List<RpLineItem> rpLineItemList, Long customerActionStatus) {
        saveRpLineItems(null, rpLineItemList, customerActionStatus);
    }

    public Page getReversePickRequest(ShippingOrder shippingOrder, String reversePickupId, Date startDate, Date endDate, Long customerActionStatus, ReversePickupStatus reversePickupStatus, String courierName, int pageNo, int perPage) {
        return reversePickupDao.getReversePickRequest(shippingOrder, reversePickupId, startDate, endDate, customerActionStatus, reversePickupStatus, courierName, pageNo, perPage);

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
    public void checkInRpLineItem(RpLineItem rpLineItem) {
        if (rpLineItem != null) {
            Warehouse warehouse = userService.getWarehouseForLoggedInUser();
            User user = userService.getLoggedInUser();
            SkuItem skuItem = skuGroupService.getSkuItemByBarcode(rpLineItem.getItemBarcode(), warehouse.getId(), null);
            SkuItemStatus skuItemStatus = setSkuItemStatusByWarehouseCondition(rpLineItem.getWarehouseReceivedCondition().getId());
            skuItem.setSkuItemStatus(skuItemStatus);
            /*change status of skuItem to checkIn*/
            skuItem = skuGroupService.saveSkuItem(skuItem);
            Sku sku = rpLineItem.getLineItem().getSku();
            ShippingOrder shippingOrder = rpLineItem.getLineItem().getShippingOrder();
            /*add invn checkIn entry in PVI*/
            Long qty = -1l;
            if (skuItemStatus.getId().equals(EnumSkuItemStatus.Checked_IN.getId())) {
                qty = 1l;
            }
            adminInvService.inventoryCheckinCheckout(sku, skuItem, rpLineItem.getLineItem(), shippingOrder, null, null, null,
                    EnumInvTxnType.REVERSE_PICKUP_INVENTORY_CHECKIN.asInvTxnType(), qty, user);
            saveRpLineItem(rpLineItem);

        }
    }

    private SkuItemStatus setSkuItemStatusByWarehouseCondition(Long id) {
        EnumReason enumReason = EnumReason.getById(id);
        SkuItemStatus skuItemStatus = null;
        switch (enumReason) {
            case Good:
                skuItemStatus = EnumSkuItemStatus.Checked_IN.getSkuItemStatus();
                break;
            case Damaged:
                skuItemStatus = EnumSkuItemStatus.Damaged.getSkuItemStatus();
                break;
            case Non_Functional:
                skuItemStatus = null;
            case Near_Expiry:
                skuItemStatus = null;
            case Expired:
                skuItemStatus = EnumSkuItemStatus.Expired.getSkuItemStatus();
        }
        return skuItemStatus;
    }


}
