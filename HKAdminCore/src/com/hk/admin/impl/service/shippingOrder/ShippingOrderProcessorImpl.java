package com.hk.admin.impl.service.shippingOrder;

import com.hk.constants.analytics.EnumReason;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.constants.queue.EnumBucket;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.domain.analytics.Reason;
import com.hk.domain.courier.Shipment;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.payment.Payment;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.shippingOrder.ShippingOrderCategory;
import com.hk.domain.user.User;
import com.hk.helper.ShippingOrderHelper;
import com.hk.impl.service.queue.BucketService;
import com.hk.manager.EmailManager;
import com.hk.pact.dao.ReconciliationStatusDao;
import com.hk.pact.dao.shippingOrder.LineItemDao;
import com.hk.pact.dao.shippingOrder.ReplacementOrderDao;
import com.hk.pact.dao.shippingOrder.ShippingOrderDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.shippingOrder.ShipmentService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;
import com.hk.pact.service.splitter.ShippingOrderProcessor;
import com.hk.service.ServiceLocatorFactory;
import com.hk.util.HKDateUtil;
import com.hk.util.SOFirewall;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author Ankit Chhabra
 *
 */
@Service
public class ShippingOrderProcessorImpl implements ShippingOrderProcessor {

	private Logger						logger = LoggerFactory.getLogger(ShippingOrderProcessor.class);

	@Autowired
	private UserService					userService;
	
	@Autowired
	private InventoryService			inventoryService;
	
	@Autowired
	private ShippingOrderDao			shippingOrderDao;
	
	@Autowired
	private ShippingOrderStatusService	shippingOrderStatusService;
	
	@Autowired
	private ReconciliationStatusDao   	reconciliationStatusDao;
	
	@Autowired
	private LineItemDao					lineItemDao;
	
	@Autowired
	private ReplacementOrderDao			replacementOrderDao;
	
	@Autowired
	private EmailManager				emailManager;
	
	@Autowired
	BucketService 						bucketService;
	
	@Autowired
	private ShippingOrderService		shippingOrderService;
	
	private OrderService				orderService;

    @Autowired
	private ShipmentService 			shipmentService;

	
	@Transactional
	public ShippingOrder autoEscalateShippingOrder(ShippingOrder shippingOrder, boolean firewall) {
		if(isShippingOrderAutoEscalable(shippingOrder, firewall)){
	//		shippingOrderService.validateShippingOrder(shippingOrder);
			User activityUser = getUserService().getAdminUser();
			shippingOrderService.logShippingOrderActivity(shippingOrder, activityUser,
					EnumShippingOrderLifecycleActivity.SO_AutoEscalatedToProcessingQueue.asShippingOrderLifecycleActivity(), null, null);
			shippingOrder = escalateShippingOrderFromActionQueue(shippingOrder);
		}
		return shippingOrder;
	}

	@Transactional
	public ShippingOrder manualEscalateShippingOrder(ShippingOrder shippingOrder) {
		if(isShippingOrderManuallyEscalable(shippingOrder)){
			shippingOrderService.validateShippingOrder(shippingOrder);
			User activityUser = userService.getLoggedInUser();
			shippingOrderService.logShippingOrderActivity(shippingOrder, activityUser,
					EnumShippingOrderLifecycleActivity.SO_EscalatedToProcessingQueue.asShippingOrderLifecycleActivity(), null, null);
			bucketService.escalateOrderFromActionQueue(shippingOrder);
			shippingOrder = escalateShippingOrderFromActionQueue(shippingOrder);
		}
		return shippingOrder;
	}

	@Transactional
	public ShippingOrder automateManualEscalation(ShippingOrder shippingOrder) {
		if(isShippingOrderAutomaticallyManuallyEscalable(shippingOrder)){
			User activityUser = getUserService().getAdminUser();
			shippingOrderService.logShippingOrderActivity(shippingOrder, activityUser,
					EnumShippingOrderLifecycleActivity.SO_AutoEscalatedToProcessingQueue.asShippingOrderLifecycleActivity(), null, null);
			bucketService.escalateOrderFromActionQueue(shippingOrder);
			shippingOrder = escalateShippingOrderFromActionQueue(shippingOrder);
		}
		return shippingOrder;
	}

    private boolean isShippingOrderAutoEscalable(ShippingOrder shippingOrder, boolean firewall) {
        Payment payment = shippingOrder.getBaseOrder().getPayment();
        List<Reason> reasons = new ArrayList<Reason>();
        if (payment != null && EnumPaymentStatus.getEscalablePaymentStatusIds().contains(payment.getPaymentStatus().getId())) {
            if(firewall && SOFirewall.isAmountMismatch(payment.getOrder())){
              reasons.add(EnumReason.DiscrepancyInPaymentAmount.asReason());
            }
            if (shippingOrder.getOrderStatus().getId().equals(EnumShippingOrderStatus.SO_ActionAwaiting.getId())) {
                if(shippingOrder.isServiceOrder()){
                    return true;
                }
                if (shippingOrder.isDropShipping()) {
                    reasons.add(EnumReason.DROP_SHIPPED_ORDER.asReason());
                }
                List<EnumBucket> enumBuckets = bucketService.getCategoryDefaultersBuckets(shippingOrder);
                if (!enumBuckets.isEmpty()) {
                    reasons.add(EnumReason.InsufficientUnbookedInventory.asReason());
                }
                if (shippingOrder.getShipment() == null) {
                    reasons.add(EnumReason.ShipmentNotCreated.asReason());
                } else {
                    //putting checks for shipping cost
                    Double estimatedShippingCharges = shippingOrder.getShipment().getEstmShipmentCharge();
                    if (firewall && estimatedShippingCharges != null && estimatedShippingCharges > SOFirewall.minAllowedShippingCharges && estimatedShippingCharges >= SOFirewall.calculateCutoffAmount(shippingOrder)) {
                        reasons.add(EnumReason.HighShippingCost.asReason());
                    }
                }
                if (!reasons.isEmpty()) {
                    for (Reason reason : reasons) {
                    	shippingOrderService.logShippingOrderActivityByAdmin(shippingOrder,
                    			EnumShippingOrderLifecycleActivity.SO_CouldNotBeAutoEscalatedToProcessingQueue, reason);
                    }
                    return false;
                }
                return true;
            }
        } else {
        	shippingOrderService.logShippingOrderActivityByAdmin(shippingOrder,
        			EnumShippingOrderLifecycleActivity.SO_CouldNotBeAutoEscalatedToProcessingQueue,  EnumReason.InvalidPaymentStatus.asReason());
            return false;
        }
        return false;
    }

    @Transactional
    private ShippingOrder escalateShippingOrderFromActionQueue(ShippingOrder shippingOrder) {
        EnumShippingOrderStatus applicableStatus = shippingOrder.isDropShipping() ? EnumShippingOrderStatus.SO_ReadyForDropShipping : EnumShippingOrderStatus.SO_ReadyForProcess;
        shippingOrder.setLastEscDate(HKDateUtil.getNow());
        shippingOrder.setOrderStatus(applicableStatus.asShippingOrderStatus());
        shippingOrder.setLastEscDate(HKDateUtil.getNow());
        if (shippingOrder.isDropShipping()) {
            emailManager.sendEscalationToDropShipEmail(shippingOrder);
        }
        shippingOrder = (ShippingOrder) getShippingOrderDao().save(shippingOrder);
        getOrderService().escalateOrderFromActionQueue(shippingOrder.getBaseOrder(), shippingOrder.getGatewayOrderId());
        return shippingOrder;
    }
    
	private boolean isShippingOrderManuallyEscalable(ShippingOrder shippingOrder) {
		if (EnumPaymentStatus.getEscalablePaymentStatusIds().contains(shippingOrder.getBaseOrder().getPayment().getPaymentStatus().getId())) {
			if (shippingOrder.getOrderStatus().getId().equals(EnumShippingOrderStatus.SO_ActionAwaiting.getId())) {
				if (!(shippingOrder.isServiceOrder())) {

					User loggedInUser = getUserService().getLoggedInUser();
					if(loggedInUser == null){
						loggedInUser = getUserService().getAdminUser();
					}
					// Set<LineItem> selectedItems = new HashSet<LineItem>();

					for (LineItem lineItem : shippingOrder.getLineItems()) {
						Long availableUnbookedInv = 0L;
						Long availableNetPhysicalInventory = getInventoryService().getAvailableUnbookedInventory(Arrays.asList(lineItem.getSku()), false);
						if (lineItem.getCartLineItem().getCartLineItemConfig() != null) {
							// continue;
							return true;
							// availableUnbookedInv =
							// getInventoryService().getAvailableUnbookedInventoryForPrescriptionEyeglasses(Arrays.asList(lineItem.getSku()));
						} else {
							availableUnbookedInv = getInventoryService().getAvailableUnbookedInventory(lineItem.getSku(), null);
						}
                        Long bookedQty = 0L;
						Long orderedQty = lineItem.getQty();
                        if (lineItem.getSkuItemLineItems() != null){
                           bookedQty = (long)lineItem.getSkuItemLineItems().size();
                        }

						// It cannot be = as for last order/unit unbooked will
						// always be ZERO
						if (!shippingOrder.isDropShipping()) {
							if (!(bookedQty >= orderedQty)) {
								if (availableNetPhysicalInventory < 0 || availableUnbookedInv < 0) {
									String comments = lineItem.getSku().getProductVariant().getProduct().getName() + " at this instant was = " + availableUnbookedInv;
									shippingOrderService.logShippingOrderActivity(shippingOrder, loggedInUser, shippingOrderService
											.getShippingOrderLifeCycleActivity(EnumShippingOrderLifecycleActivity.SO_CouldNotBeManuallyEscalatedToProcessingQueue),
											EnumReason.InsufficientUnbookedInventoryManual.asReason(), comments);
									return false;
									// selectedItems.add(lineItem);
								}
							}
						}
					}

					if (shippingOrder.getShipment() == null && !shippingOrder.isDropShipping()) {
						Shipment newShipment = getShipmentService().createShipment(shippingOrder, true);
						if (newShipment == null) {
							shippingOrderService.logShippingOrderActivityByAdmin(shippingOrder,
									EnumShippingOrderLifecycleActivity.SO_CouldNotBeAutoEscalatedToProcessingQueue,
									EnumReason.ShipmentNotCreatedManual.asReason());
							return false;
						}
					}
				}
				return true;
			}
		} else {
			shippingOrderService.logShippingOrderActivityByAdmin(shippingOrder, EnumShippingOrderLifecycleActivity.SO_CouldNotBeAutoEscalatedToProcessingQueue,
					EnumReason.InvalidPaymentStatusManual.asReason());
			return false;
		}
		return false;
	}

  

	private boolean isShippingOrderAutomaticallyManuallyEscalable(ShippingOrder shippingOrder) {
		logger.debug("Trying to autoEscalate order#" + shippingOrder.getId());
		Payment payment = shippingOrder.getBaseOrder().getPayment();
		User adminUser = getUserService().getAdminUser();
		if (payment != null && EnumPaymentStatus.getEscalablePaymentStatusIds().contains(payment.getPaymentStatus().getId())) {
			if (shippingOrder.getOrderStatus().getId().equals(EnumShippingOrderStatus.SO_ActionAwaiting.getId())) {
				if (shippingOrder.isDropShipping()) {
					shippingOrderService.logShippingOrderActivity(shippingOrder, adminUser,
							shippingOrderService.getShippingOrderLifeCycleActivity(EnumShippingOrderLifecycleActivity.SO_CouldNotBeAutoEscalatedToProcessingQueue),
							EnumReason.DROP_SHIPPED_ORDER.asReason(), null);
					return false;
				}
				if (shippingOrder.isServiceOrder()) {
					return true;
				}
				if (shippingOrder.getShipment() == null && !shippingOrder.isDropShipping()) {
					Shipment newShipment = getShipmentService().createShipment(shippingOrder, true);
					if (newShipment == null) {
						shippingOrderService.logShippingOrderActivity(shippingOrder, adminUser,
								shippingOrderService.getShippingOrderLifeCycleActivity(EnumShippingOrderLifecycleActivity.SO_CouldNotBeAutoEscalatedToProcessingQueue),
								EnumReason.ShipmentNotCreated.asReason(), null);
						return false;
					}
				}
				for (LineItem lineItem : shippingOrder.getLineItems()) {
					CartLineItem cartLineItem = lineItem.getCartLineItem();
					if (cartLineItem.getCartLineItemConfig() != null) {
						shippingOrderService.logShippingOrderActivity(shippingOrder, adminUser,
								shippingOrderService.getShippingOrderLifeCycleActivity(EnumShippingOrderLifecycleActivity.SO_CouldNotBeAutoEscalatedToProcessingQueue),
								EnumReason.Contains_Prescription_Glasses.asReason(), null);
						return false;
					}
					if(cartLineItem.getProductVariant().getProductExtraOptions() != null && !cartLineItem.getProductVariant().getProductExtraOptions().isEmpty()){
						shippingOrderService.logShippingOrderActivity(shippingOrder, adminUser,
								shippingOrderService.getShippingOrderLifeCycleActivity(EnumShippingOrderLifecycleActivity.SO_CouldNotBeAutoEscalatedToProcessingQueue),
								EnumReason.Contains_Prescription_Glasses.asReason(), null);
						return false;
					}
					Long availableUnbookedInv = getInventoryService().getAvailableUnbookedInventory(lineItem.getSku(), lineItem.getMarkedPrice()); // This

					if (availableUnbookedInv <= 0) {
						String comments = lineItem.getSku().getProductVariant().getProduct().getName() + " at this instant was = " + availableUnbookedInv;
						logger.debug(comments);
						shippingOrderService.logShippingOrderActivity(shippingOrder, adminUser,
								shippingOrderService.getShippingOrderLifeCycleActivity(EnumShippingOrderLifecycleActivity.SO_CouldNotBeAutoEscalatedToProcessingQueue),
								EnumReason.InsufficientUnbookedInventory.asReason(), comments);
						return false;
					}
				}
				return true;
			}
		} else {
			shippingOrderService.logShippingOrderActivity (shippingOrder, adminUser, 
					shippingOrderService.getShippingOrderLifeCycleActivity(EnumShippingOrderLifecycleActivity.SO_CouldNotBeAutoEscalatedToProcessingQueue),
					EnumReason.InvalidPaymentStatus.asReason(), null);
			return false;
		}
		return false;
	}



    /**
     * This method is used to split orders, if successful it creates a new SO and process the new as well as old shipping order by
     * recalculating its amount and other data and saves them into the database.
     * @param shippingOrder -  SO to split
     * @param selectedLineItems - selected line items which needs to be split
     * @param splittedOrders - to store orders after splitting
     * @param messages - success/error messages
     * @return result (true/false)
     */
    @Override
    @Transactional
    public boolean autoSplitSO(ShippingOrder shippingOrder, Set<LineItem> selectedLineItems, Map<String, ShippingOrder> splittedOrders,
                               List<String> messages ) {

        Map<String, Boolean> flagMapOldSO = new HashMap<String, Boolean>();
        Map<String, Boolean> flagMapNewSO = new HashMap<String, Boolean>();
        flagMapOldSO.put("dropShipItemPresent", false);
        flagMapOldSO.put("jitItemPresent", false);
        flagMapNewSO.put("dropShipItemPresent", false);
        flagMapNewSO.put("jitItemPresent", false);


        if (shippingOrder != null && EnumShippingOrderStatus.SO_ActionAwaiting.getId().equals(shippingOrder.getOrderStatus().getId())) {
            if (selectedLineItems.size() == shippingOrder.getLineItems().size()) {
                messages.add("Invalid LineItem selection for Shipping Order : " + shippingOrder.getGatewayOrderId()
                        + ". Cannot be split.");
                return false;
            }

            Set<LineItem> originalShippingItems = shippingOrder.getLineItems();
            originalShippingItems.removeAll(selectedLineItems);

            for (LineItem remainingLineItem : originalShippingItems) {
                if ((remainingLineItem.getSku().getProductVariant().getProduct().isDropShipping())) {
                    flagMapOldSO.put("dropShipItemPresent", true);
                    break;
                }
            }
            for (LineItem remainingLineItem : originalShippingItems) {
                if ((remainingLineItem.getSku().getProductVariant().getProduct().isJit())) {
                    flagMapOldSO.put("jitItemPresent", true);
                    break;
                }
            }

            // Create a new shipping order to split
            ShippingOrder newShippingOrder = shippingOrderService.createSOWithBasicDetails(shippingOrder.getBaseOrder(), shippingOrder.getWarehouse());
            newShippingOrder.setServiceOrder(false);
            newShippingOrder.setOrderStatus(shippingOrderStatusService.find(EnumShippingOrderStatus.SO_ActionAwaiting));
            newShippingOrder = shippingOrderService.save(newShippingOrder);

            for (LineItem selectedLineItem : selectedLineItems) {
                selectedLineItem.setShippingOrder(newShippingOrder);
                if ((selectedLineItem.getSku().getProductVariant().getProduct().isDropShipping())) {
                    flagMapNewSO.put("dropShipItemPresent", true);
                    break;
                }
                lineItemDao.save(selectedLineItem);
            }
            for (LineItem selectedLineItem : selectedLineItems) {
                selectedLineItem.setShippingOrder(newShippingOrder);
                if ((selectedLineItem.getSku().getProductVariant().getProduct().isJit())) {
                    flagMapNewSO.put("jitItemPresent", true);
                    break;
                }
                lineItemDao.save(selectedLineItem);
            }
            shippingOrderDao.refresh(newShippingOrder);
            this.updateSplittedSODetails(flagMapNewSO, newShippingOrder);

            /**
             * Fetch previous shipping order and recalculate amount
             */

            shippingOrderDao.refresh(shippingOrder);
            this.updateSplittedSODetails(flagMapOldSO, shippingOrder);
            newShippingOrder = shippingOrderService.setGatewayIdAndTargetDateOnShippingOrder(newShippingOrder);
            newShippingOrder = shippingOrderService.save(newShippingOrder);

            shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_Split);

            if (splittedOrders != null) {
                splittedOrders.put("oldShippingOrder", shippingOrder);
                splittedOrders.put("newShippingOrder", newShippingOrder);
            }
			/*//Handling the PO against the shipping Orders
			if(shippingOrder.getPurchaseOrders()!=null && shippingOrder.getPurchaseOrders().size()>0){
				adminShippingOrderService.adjustPurchaseOrderForSplittedShippingOrder(shippingOrder, newShippingOrder);
			}*/

            messages.add(("Shipping Order : " + shippingOrder.getGatewayOrderId() + " was split manually."));
            return true;
        } else {
            messages.add("Shipping Order : " + shippingOrder.getGatewayOrderId() + " is in incorrect status cannot be split.");
            return false;
        }
    }

	/**
	 * This method is used to fill SO details for newly splitted orders.
	 * @param flagMap
	 * @param newShippingOrder
	 */
	private void updateSplittedSODetails(Map<String, Boolean> flagMap, ShippingOrder newShippingOrder) {
		Set<ShippingOrderCategory> newShippingOrderCategories =
                this.getOrderService().getCategoriesForShippingOrder(newShippingOrder);
		newShippingOrder.setShippingOrderCategories(newShippingOrderCategories);
		newShippingOrder.setBasketCategory(orderService.getBasketCategory(newShippingOrderCategories).getName());
		newShippingOrder = shippingOrderService.save(newShippingOrder);
		shippingOrderDao.refresh(newShippingOrder);

		if (flagMap.get("dropShipItemPresent")) {
			newShippingOrder.setDropShipping(true);
		} else {
			newShippingOrder.setDropShipping(false);
		}
		if (flagMap.get("jitItemPresent")) {
			newShippingOrder.setContainsJitProducts(true);
		} else {
			newShippingOrder.setContainsJitProducts(false);
		}
		ShippingOrderHelper.updateAccountingOnSOLineItems(newShippingOrder, newShippingOrder.getBaseOrder());
		newShippingOrder.setAmount(ShippingOrderHelper.getAmountForSO(newShippingOrder));

	}

	
	
	/* Setters and getters begin*/
	/**
	 * @return the logger
	 */
	public Logger getLogger() {
		return logger;
	}

	/**
	 * @param logger the logger to set
	 */
	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	/**
	 * @return the userService
	 */
	public UserService getUserService() {
		return userService;
	}

	/**
	 * @param userService the userService to set
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * @return the inventoryService
	 */
	public InventoryService getInventoryService() {
		return inventoryService;
	}

	/**
	 * @param inventoryService the inventoryService to set
	 */
	public void setInventoryService(InventoryService inventoryService) {
		this.inventoryService = inventoryService;
	}

	/**
	 * @return the shippingOrderDao
	 */
	public ShippingOrderDao getShippingOrderDao() {
		return shippingOrderDao;
	}

	/**
	 * @param shippingOrderDao the shippingOrderDao to set
	 */
	public void setShippingOrderDao(ShippingOrderDao shippingOrderDao) {
		this.shippingOrderDao = shippingOrderDao;
	}

	/**
	 * @return the shippingOrderStatusService
	 */
	public ShippingOrderStatusService getShippingOrderStatusService() {
		return shippingOrderStatusService;
	}

	/**
	 * @param shippingOrderStatusService the shippingOrderStatusService to set
	 */
	public void setShippingOrderStatusService(ShippingOrderStatusService shippingOrderStatusService) {
		this.shippingOrderStatusService = shippingOrderStatusService;
	}

	/**
	 * @return the reconciliationStatusDao
	 */
	public ReconciliationStatusDao getReconciliationStatusDao() {
		return reconciliationStatusDao;
	}

	/**
	 * @param reconciliationStatusDao the reconciliationStatusDao to set
	 */
	public void setReconciliationStatusDao(ReconciliationStatusDao reconciliationStatusDao) {
		this.reconciliationStatusDao = reconciliationStatusDao;
	}

	/**
	 * @return the lineItemDao
	 */
	public LineItemDao getLineItemDao() {
		return lineItemDao;
	}

	/**
	 * @param lineItemDao the lineItemDao to set
	 */
	public void setLineItemDao(LineItemDao lineItemDao) {
		this.lineItemDao = lineItemDao;
	}

	/**
	 * @return the replacementOrderDao
	 */
	public ReplacementOrderDao getReplacementOrderDao() {
		return replacementOrderDao;
	}

	/**
	 * @param replacementOrderDao the replacementOrderDao to set
	 */
	public void setReplacementOrderDao(ReplacementOrderDao replacementOrderDao) {
		this.replacementOrderDao = replacementOrderDao;
	}

	/**
	 * @return the emailManager
	 */
	public EmailManager getEmailManager() {
		return emailManager;
	}

	/**
	 * @param emailManager the emailManager to set
	 */
	public void setEmailManager(EmailManager emailManager) {
		this.emailManager = emailManager;
	}

	/**
	 * @return the orderService
	 */
	public OrderService getOrderService() {
        if (orderService == null) {
            this.orderService = ServiceLocatorFactory.getService(OrderService.class);
        }return orderService;
	}

	/**
	 * @return the shipmentService
	 */
	public ShipmentService getShipmentService() {
		return shipmentService;
	}

	/**
	 * @param shipmentService the shipmentService to set
	 */
	public void setShipmentService(ShipmentService shipmentService) {
		this.shipmentService = shipmentService;
	}



}
