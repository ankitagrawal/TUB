package com.hk.admin.impl.service.shippingOrder;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hk.admin.pact.dao.shippingOrder.AdminShippingOrderDao;
import com.hk.admin.pact.service.courier.AwbService;
import com.hk.admin.pact.service.courier.PincodeCourierService;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.admin.pact.service.order.AdminOrderService;
import com.hk.admin.pact.service.shippingOrder.AdminShippingOrderService;
import com.hk.constants.courier.EnumAwbStatus;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Shipment;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.order.ReplacementOrderReason;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.order.ShippingOrderLifecycle;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.shippingOrder.ShippingOrderCategory;
import com.hk.domain.sku.Sku;
import com.hk.domain.warehouse.Warehouse;
import com.hk.exception.NoSkuException;
import com.hk.helper.LineItemHelper;
import com.hk.helper.ShippingOrderHelper;
import com.hk.impl.service.queue.BucketService;
import com.hk.loyaltypg.service.LoyaltyProgramService;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.shippingOrder.LineItemDao;
import com.hk.pact.dao.shippingOrder.ShippingOrderDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.core.WarehouseService;
import com.hk.pact.service.inventory.InventoryHealthService;
import com.hk.pact.service.inventory.InventoryHealthService.FetchType;
import com.hk.pact.service.inventory.InventoryHealthService.SkuFilter;
import com.hk.pact.service.inventory.InventoryHealthService.SkuInfo;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.shippingOrder.ShipmentService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;
import com.hk.service.ServiceLocatorFactory;
import com.hk.web.action.admin.queue.ActionAwaitingQueueAction;

@Service
public class AdminShippingOrderServiceImpl implements AdminShippingOrderService {

    private static Logger logger = LoggerFactory.getLogger(AdminShippingOrderServiceImpl.class);
    @Autowired
    private ShippingOrderService shippingOrderService;
    @Autowired
    private AdminInventoryService adminInventoryService;
    @Autowired
    private BucketService bucketService;
    @Autowired
    private PincodeCourierService pincodeCourierService;
    private String cancellationRemark;

    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ShippingOrderStatusService shippingOrderStatusService;
    @Autowired
    private SkuService skuService;
    @Autowired
    private WarehouseService warehouseService;
    private AdminOrderService adminOrderService;
    @Autowired
    private ShipmentService shipmentService;
    @Autowired
    private AdminShippingOrderDao adminShippingOrderDao;
    @Autowired
    AwbService awbService;
    @Autowired
    UserService userService;
    
    @Autowired
    private LoyaltyProgramService loyaltyProgramService;
    
    @Autowired InventoryHealthService inventoryHealthService;
    
    @Autowired BaseDao baseDao;

    @Autowired LineItemDao lineItemDao;
    @Autowired ShippingOrderDao shippingOrderDao;
    
    public void cancelShippingOrder(ShippingOrder shippingOrder,String cancellationRemark) {
        // Check if Order is in Action Queue before cancelling it.
        if (shippingOrder.getOrderStatus().getId().equals(EnumShippingOrderStatus.SO_ActionAwaiting.getId())) {
	          logger.warn("Cancelling Shipping order gateway id:::"+ shippingOrder.getGatewayOrderId());
            shippingOrder.setOrderStatus(shippingOrderStatusService.find(EnumShippingOrderStatus.SO_Cancelled));
            //shippingOrder = getShippingOrderService().save(shippingOrder);
            getAdminInventoryService().reCheckInInventory(shippingOrder);
            // TODO : Write a generic ROLLBACK util which will essentially release all attached laibilities i.e.
            // inventory, reward points, shipment, discount
            getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_Cancelled,shippingOrder.getReason(),cancellationRemark);

            orderService.updateOrderStatusFromShippingOrders(shippingOrder.getBaseOrder(), EnumShippingOrderStatus.SO_Cancelled, EnumOrderStatus.Cancelled);
            if(shippingOrder.getShipment()!= null){
                Awb awbToRemove = shippingOrder.getShipment().getAwb();
                awbService.preserveAwb(awbToRemove);
                Shipment shipmentToDelete = shippingOrder.getShipment();
                shippingOrder.setShipment(null);
	            shipmentService.delete(shipmentToDelete);
	            //shippingOrderService.save(shippingOrder);
            }
			getShippingOrderService().save(shippingOrder);
            getBucketService().popFromActionQueue(shippingOrder);
        }
        for (LineItem lineItem : shippingOrder.getLineItems()) {
            getInventoryService().checkInventoryHealth(lineItem.getSku().getProductVariant());
        }
    }

	public boolean updateWarehouseForShippingOrder(ShippingOrder shippingOrder, Warehouse warehouse) {
		Set<LineItem> lineItems = shippingOrder.getLineItems();
		boolean shouldUpdate = true;
		try {
			for (LineItem lineItem : lineItems) {
				SkuFilter filter = new SkuFilter();
				filter.setFetchType(FetchType.ALL);
				filter.setWarehouseId(warehouse.getId());
				filter.setMinQty(lineItem.getQty());
				filter.setMrp(lineItem.getMarkedPrice());
				Collection<SkuInfo> skus = inventoryHealthService.getAvailableSkus(lineItem.getCartLineItem().getProductVariant(), filter);

				if(skus != null && skus.size() > 0) {
					Sku sku = baseDao.get(Sku.class, skus.iterator().next().getSkuId());
					lineItem.setSku(sku);
				}
			}
			
			for (LineItem lineItem : lineItems) {
				if (!lineItem.getSku().getWarehouse().getId().equals(warehouse.getId())) {
					shouldUpdate = false;
				}
			}

			if (shouldUpdate) {
				shippingOrder.setWarehouse(warehouse);
				shipmentService.recreateShipment(shippingOrder);
				shippingOrder = getShippingOrderService().save(shippingOrder);
				getShippingOrderService().logShippingOrderActivity(shippingOrder,
						EnumShippingOrderLifecycleActivity.SO_WarehouseChanged);

				// Re-checkin checkedout inventory in case of flipping.
				getAdminInventoryService().reCheckInInventory(shippingOrder);

			}

		} catch (NoSkuException noSku) {
			shouldUpdate = false;
		}
		return shouldUpdate;
	}
	
    public ShippingOrder createSOforManualSplit(Set<CartLineItem> cartLineItems, Warehouse warehouse) {

        if (cartLineItems != null && !cartLineItems.isEmpty() && warehouse != null) {
            Order baseOrder = cartLineItems.iterator().next().getOrder();
            ShippingOrder shippingOrder = getShippingOrderService().createSOWithBasicDetails(baseOrder, warehouse);
            shippingOrder.setBaseOrder(baseOrder);

            for (CartLineItem cartLineItem : cartLineItems) {
                ProductVariant productVariant = cartLineItem.getProductVariant();
                Sku sku = getSkuService().getSKU(productVariant, warehouse);
                if (sku != null) {
                    LineItem shippingOrderLineItem = LineItemHelper.createLineItemWithBasicDetails(sku, shippingOrder, cartLineItem);
                    shippingOrder.getLineItems().add(shippingOrderLineItem);
                } else {
                    throw new NoSkuException(productVariant, warehouse);
                }
            }

            ShippingOrderHelper.updateAccountingOnSOLineItems(shippingOrder, baseOrder);
            shippingOrder.setAmount(ShippingOrderHelper.getAmountForSO(shippingOrder));
            shippingOrder = getShippingOrderService().save(shippingOrder);
            /**
             * this additional call to save is done so that we have shipping order id to generate shipping order gateway
             * id
             */
            shippingOrder = getShippingOrderService().setGatewayIdAndTargetDateOnShippingOrder(shippingOrder);
            shippingOrder = getShippingOrderService().save(shippingOrder);
            Set<ShippingOrderCategory> categories = getOrderService().getCategoriesForShippingOrder(shippingOrder);
            shippingOrder.setShippingOrderCategories(categories);
            shippingOrder.setBasketCategory(getOrderService().getBasketCategory(categories).getName());
            shippingOrder = getShippingOrderService().save(shippingOrder);

			//shipmentService.createShipment(shippingOrder);
	        // auto escalate shipping orders if possible
	        //getShippingOrderService().autoEscalateShippingOrder(shippingOrder);

			orderService.splitBOCreateShipmentEscalateSOAndRelatedTasks(baseOrder);
            return shippingOrder;
        }
        return null;
    }

    @Transactional
    public ShippingOrder putShippingOrderOnHold(ShippingOrder shippingOrder) {
        if (shippingOrder.getOrderStatus().getId().equals(EnumShippingOrderStatus.SO_ActionAwaiting.getId())) {
            shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_OnHold));
            getAdminInventoryService().reCheckInInventory(shippingOrder);
            shippingOrder = getShippingOrderService().save(shippingOrder);
            getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_PutOnHold);
        }
        return shippingOrder;
    }

    @Transactional
    public ShippingOrder unHoldShippingOrder(ShippingOrder shippingOrder) {
        shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_ActionAwaiting));
        shippingOrder = getShippingOrderService().save(shippingOrder);

        getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_RemovedOnHold);
        return shippingOrder;
    }

    @Transactional
    public ShippingOrder
    markShippingOrderAsDelivered(ShippingOrder shippingOrder) {
        shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_Delivered));
        getShippingOrderService().save(shippingOrder);
        getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_Delivered);
        Order order = shippingOrder.getBaseOrder();
        getAdminOrderService().markOrderAsDelivered(order);
        loyaltyProgramService.approveKarmaPoints(shippingOrder.getBaseOrder());
//	    smsManager.sendOrderDeliveredSMS(shippingOrder);
	    return shippingOrder;
    }

    @Transactional
       public ShippingOrder markShippingOrderAsInstalled(ShippingOrder shippingOrder) {
           shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_Installed));
           getShippingOrderService().save(shippingOrder);
           getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_Installed);
           Order order = shippingOrder.getBaseOrder();
           getAdminOrderService().markOrderAsCompletedWithInstallation(order);
//	    smsManager.sendOrderDeliveredSMS(shippingOrder);
           return shippingOrder;
       }



    @Transactional
    public ShippingOrder markShippingOrderAsRTO(ShippingOrder shippingOrder) {
        shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_RTO));
        shippingOrder.getShipment().setReturnDate(new Date());
        getShippingOrderService().save(shippingOrder);
        getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_Returned);
        Order order = shippingOrder.getBaseOrder();
        getAdminOrderService().markOrderAsRTO(order);
        return shippingOrder;
    }

    @Transactional
    public ShippingOrder markShippingOrderAsLost(ShippingOrder shippingOrder) {
        shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_Lost));
        getShippingOrderService().save(shippingOrder);
        getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_Lost);
        Order order = shippingOrder.getBaseOrder();
        getAdminOrderService().markOrderAsLost(order);
        return shippingOrder;
    }

    public ShippingOrder initiateRTOForShippingOrder(ShippingOrder shippingOrder, ReplacementOrderReason rtoReason) {
        shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.RTO_Initiated));
        getShippingOrderService().save(shippingOrder);
	    if(rtoReason != null){
            getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.RTO_Initiated, null, rtoReason.getName());
	    }
	    else{
		    getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.RTO_Initiated);
	    }
        return shippingOrder;
    }

    public List<ShippingOrder> getShippingOrderListByCouriers(Date startDate, Date endDate, List<Long> courierId) {
        return getAdminShippingOrderDao().getShippingOrderListByCouriers(startDate, endDate, courierId);
    }


    @Transactional
    public ShippingOrder markShippingOrderAsShipped(ShippingOrder shippingOrder) {
        Shipment shipment = shippingOrder.getShipment();
        if (shipment != null) {
            shipment.getAwb().setAwbStatus(EnumAwbStatus.Used.getAsAwbStatus());
            shipment.setShipDate(new Date());
            getShipmentService().save(shipment);
        }
        shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_Shipped));
        getPincodeCourierService().setTargetDeliveryDate(shippingOrder);
        getShippingOrderService().save(shippingOrder);
        getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_Shipped);
        getBucketService().popFromActionQueue(shippingOrder);
        getAdminOrderService().markOrderAsShipped(shippingOrder.getBaseOrder());
        return shippingOrder;
    }

    @Transactional
    public ShippingOrder markShippingOrderAsPrinted(ShippingOrder shippingOrder) {
        shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_MarkedForPrinting));
        getShippingOrderService().save(shippingOrder);

        getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_ChosenForPrinting);

        return shippingOrder;
    }

    @Transactional
    public ShippingOrder moveShippingOrderToPickingQueue(ShippingOrder shippingOrder) {
        shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_Picking));
        getShippingOrderService().save(shippingOrder);

        getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_InPicking);

        return shippingOrder;
    }

    @Transactional
    public ShippingOrder moveShippingOrderBackToActionQueue(ShippingOrder shippingOrder) {
        shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_OnHold));
        getAdminInventoryService().reCheckInInventory(shippingOrder);
        shippingOrder = getShippingOrderService().save(shippingOrder);
        getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_EscalatedBackToActionQueue, shippingOrder.getReason(), null);

        getBucketService().escalateBackToActionQueue(shippingOrder);
        return shippingOrder;
    }

    @Transactional
    public ShippingOrder moveShippingOrderBackToPackingQueue(ShippingOrder shippingOrder) {
        shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_ReadyForProcess));
        getAdminInventoryService().reCheckInInventory(shippingOrder);
        getShippingOrderService().save(shippingOrder);

        getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_BackToPackingQueue);

        return shippingOrder;
    }

    @Transactional
       public ShippingOrder moveShippingOrderBackToDropShippingQueue(ShippingOrder shippingOrder) {
           shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_ReadyForDropShipping));
//           getAdminInventoryService().reCheckInInventory(shippingOrder);
           getShippingOrderService().save(shippingOrder);
           getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_BackToDropShippingQueue);
           return shippingOrder;
       }

	public ReplacementOrderReason getRTOReasonForShippingOrder(ShippingOrder shippingOrder) {
		String rtoReason = null;
		ReplacementOrderReason replacementOrderReason = null;
		for (ShippingOrderLifecycle shippingOrderLifecycle : shippingOrder.getShippingOrderLifecycles()){
			if(shippingOrderLifecycle.getShippingOrderLifeCycleActivity().getId().equals(EnumShippingOrderLifecycleActivity.RTO_Initiated.getId())){
				if(shippingOrderLifecycle.getComments() != null){
					replacementOrderReason = getReplacementOrderReasonByName(shippingOrderLifecycle.getComments());
				}
			}
		}
		return replacementOrderReason;
	}

	public ReplacementOrderReason getReplacementOrderReasonByName(String replacementOrderReasonString) {
		List<ReplacementOrderReason> replacementOrderReasonList = getAdminShippingOrderDao().getAll(ReplacementOrderReason.class);
		for(ReplacementOrderReason replacementOrderReason : replacementOrderReasonList){
			if(replacementOrderReasonString.contains(replacementOrderReason.getName())){
				return replacementOrderReason;
			}
		}
		return null;
	}

	public void splitSONormal(ShippingOrder shippingOrder, List<LineItem> lineItems, Boolean dropShipItemPresentInRemainingItems,
			Boolean jitItemPresentInRemainingItems, Boolean dropShipItemPresentInSelectedItems, Boolean jitItemPresentInSelectedItems) {
		if (shippingOrder != null && EnumShippingOrderStatus.SO_ActionAwaiting.getId().equals(shippingOrder.getOrderStatus().getId())) {

            Set<LineItem> selectedLineItems = new HashSet<LineItem>();

            for (LineItem lineItem : lineItems) {
                if (lineItem != null) {
                    logger.debug("lineItem: " + lineItem.getSku().getProductVariant());
                    selectedLineItems.add(lineItem);
                }
            }
            if (selectedLineItems.size() == shippingOrder.getLineItems().size()) {
          //      addRedirectAlertMessage(new SimpleMessage("Invalid LineItem selection for Shipping Order : " + shippingOrder.getGatewayOrderId() + ". Cannot be split."));
       //         return new RedirectResolution(ActionAwaitingQueueAction.class);
            }

            Set<LineItem> originalShippingItems = shippingOrder.getLineItems();
            originalShippingItems.removeAll(selectedLineItems);

            for (LineItem remainingLineItem : originalShippingItems) {
                if ((remainingLineItem.getSku().getProductVariant().getProduct().isDropShipping())) {
                    dropShipItemPresentInRemainingItems = true;
                    break;
                }
            }
            for (LineItem remainingLineItem : originalShippingItems) {
                if ((remainingLineItem.getSku().getProductVariant().getProduct().isJit())) {
                    jitItemPresentInRemainingItems = true;
                    break;
                }
            }

            ShippingOrder newShippingOrder = shippingOrderService.createSOWithBasicDetails(shippingOrder.getBaseOrder(), shippingOrder.getWarehouse());
            newShippingOrder.setBaseOrder(shippingOrder.getBaseOrder());
            newShippingOrder.setServiceOrder(false);
            newShippingOrder.setOrderStatus(shippingOrderStatusService.find(EnumShippingOrderStatus.SO_ActionAwaiting));
            newShippingOrder = shippingOrderService.save(newShippingOrder);

            for (LineItem selectedLineItem : selectedLineItems) {
                selectedLineItem.setShippingOrder(newShippingOrder);
                if ((selectedLineItem.getSku().getProductVariant().getProduct().isDropShipping())) {
                    dropShipItemPresentInSelectedItems = true;
                }
                lineItemDao.save(selectedLineItem);
            }
            for (LineItem selectedLineItem : selectedLineItems) {
                selectedLineItem.setShippingOrder(newShippingOrder);
                if ((selectedLineItem.getSku().getProductVariant().getProduct().isJit())) {
                    jitItemPresentInSelectedItems = true;
                    break;
                }
                lineItemDao.save(selectedLineItem);
            }
            shippingOrderDao.refresh(newShippingOrder);
            Set<ShippingOrderCategory> newShippingOrderCategories = orderService.getCategoriesForShippingOrder(newShippingOrder);
            newShippingOrder.setShippingOrderCategories(newShippingOrderCategories);
            newShippingOrder.setBasketCategory(orderService.getBasketCategory(newShippingOrderCategories).getName());
            newShippingOrder = shippingOrderService.save(newShippingOrder);
            shippingOrderDao.refresh(newShippingOrder);

            if (dropShipItemPresentInSelectedItems) {
                newShippingOrder.setDropShipping(true);
            } else {
                newShippingOrder.setDropShipping(false);
            }
            if (jitItemPresentInSelectedItems) {
                newShippingOrder.setContainsJitProducts(true);
            } else {
                newShippingOrder.setContainsJitProducts(false);
            }
            ShippingOrderHelper.updateAccountingOnSOLineItems(newShippingOrder, newShippingOrder.getBaseOrder());
            newShippingOrder.setAmount(ShippingOrderHelper.getAmountForSO(newShippingOrder));
            newShippingOrder = shippingOrderService.setGatewayIdAndTargetDateOnShippingOrder(newShippingOrder);
            newShippingOrder = shippingOrderService.save(newShippingOrder);
            shipmentService.createShipment(newShippingOrder, true);

            /**
             * Fetch previous shipping order and recalculate amount
             */

            shippingOrderDao.refresh(shippingOrder);
            //shippingOrder = shippingOrderService.find(shippingOrder.getId());
            ShippingOrderHelper.updateAccountingOnSOLineItems(shippingOrder, shippingOrder.getBaseOrder());
            shippingOrder.setAmount(ShippingOrderHelper.getAmountForSO(shippingOrder));

            if (dropShipItemPresentInRemainingItems) {
                shippingOrder.setDropShipping(true);
            } else {
                shippingOrder.setDropShipping(false);
            }
            if (jitItemPresentInRemainingItems) {
                shippingOrder.setContainsJitProducts(true);
            } else {
                shippingOrder.setContainsJitProducts(false);
            }
            Set<ShippingOrderCategory> shippingOrderCategories = orderService.getCategoriesForShippingOrder(shippingOrder);
            shippingOrder.setShippingOrderCategories(shippingOrderCategories);
            shippingOrder.setBasketCategory(orderService.getBasketCategory(shippingOrderCategories).getName());
            shippingOrder = shippingOrderService.save(shippingOrder);

            shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_Split);

        //    addRedirectAlertMessage(new SimpleMessage("Shipping Order : " + shippingOrder.getGatewayOrderId() + " was split manually."));
   //         return new RedirectResolution(ActionAwaitingQueueAction.class);
        } else {
    //        addRedirectAlertMessage(new SimpleMessage("Shipping Order : " + shippingOrder.getGatewayOrderId() + " is in incorrect status cannot be split."));
    //        return new RedirectResolution(ActionAwaitingQueueAction.class);
        }
	}
	
	
	
    public ShippingOrderService getShippingOrderService() {
        return shippingOrderService;
    }

    public void setShippingOrderService(ShippingOrderService shippingOrderService) {
        this.shippingOrderService = shippingOrderService;
    }

    public ShippingOrderStatusService getShippingOrderStatusService() {
        return shippingOrderStatusService;
    }

    public void setShippingOrderStatusService(ShippingOrderStatusService shippingOrderStatusService) {
        this.shippingOrderStatusService = shippingOrderStatusService;
    }

    public SkuService getSkuService() {
        return skuService;
    }

    public void setSkuService(SkuService skuService) {
        this.skuService = skuService;
    }

    public WarehouseService getWarehouseService() {
        return warehouseService;
    }

    public void setWarehouseService(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    public AdminOrderService getAdminOrderService() {
        if (adminOrderService == null) {
            adminOrderService = ServiceLocatorFactory.getService(AdminOrderService.class);
        }
        return adminOrderService;
    }

    public void setAdminOrderService(AdminOrderService adminOrderService) {
        this.adminOrderService = adminOrderService;
    }

    public ShipmentService getShipmentService() {
        return shipmentService;
    }

    public void setShipmentService(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
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

    public AdminShippingOrderDao getAdminShippingOrderDao() {
        return adminShippingOrderDao;
    }

    public void setAdminShippingOrderDao(AdminShippingOrderDao adminShippingOrderDao) {
        this.adminShippingOrderDao = adminShippingOrderDao;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

	public UserService getUserService() {
		return userService;
	}

    public BucketService getBucketService() {
        return bucketService;
    }

    public void setBucketService(BucketService bucketService) {
        this.bucketService = bucketService;
    }

    public PincodeCourierService getPincodeCourierService() {
        return pincodeCourierService;
    }

    public void setPincodeCourierService(PincodeCourierService pincodeCourierService) {
        this.pincodeCourierService = pincodeCourierService;
    }

    public String getCancellationRemark() {
        return cancellationRemark;
    }

    public void setCancellationRemark(String cancellationRemark) {
        this.cancellationRemark = cancellationRemark;
    }
}
