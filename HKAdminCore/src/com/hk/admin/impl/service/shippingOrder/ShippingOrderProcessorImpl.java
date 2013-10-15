package com.hk.admin.impl.service.shippingOrder;

import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.admin.pact.service.shippingOrder.AdminShippingOrderService;
import com.hk.constants.analytics.EnumReason;
import com.hk.constants.inventory.EnumReconciliationActionType;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.constants.queue.EnumBucket;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.constants.shippingOrder.ShippingOrderConstants;
import com.hk.constants.sku.EnumSkuGroupStatus;
import com.hk.constants.sku.EnumSkuItemStatus;
import com.hk.domain.analytics.Reason;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.courier.Shipment;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.ReplacementOrder;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.payment.Payment;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.shippingOrder.ShippingOrderCategory;
import com.hk.domain.sku.SkuItemLineItem;
import com.hk.domain.store.Store;
import com.hk.domain.user.User;
import com.hk.helper.ShippingOrderHelper;
import com.hk.impl.service.queue.BucketService;
import com.hk.manager.EmailManager;
import com.hk.pact.dao.ReconciliationStatusDao;
import com.hk.pact.dao.shippingOrder.LineItemDao;
import com.hk.pact.dao.shippingOrder.ReplacementOrderDao;
import com.hk.pact.dao.shippingOrder.ShippingOrderDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.payment.PaymentService;
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

  private Logger						              logger = LoggerFactory.getLogger(ShippingOrderProcessor.class);

  @Autowired
  private UserService					            userService;

  @Autowired
  private InventoryService			          inventoryService;

  @Autowired
  private ShippingOrderDao			          shippingOrderDao;

  @Autowired
  private ShippingOrderStatusService	    shippingOrderStatusService;

  @Autowired
  private ReconciliationStatusDao   	    reconciliationStatusDao;

  @Autowired
  private LineItemDao					            lineItemDao;

  @Autowired
  private ReplacementOrderDao			        replacementOrderDao;

  @Autowired
  private EmailManager				            emailManager;

  @Autowired
  BucketService 						              bucketService;

  @Autowired
  private ShippingOrderService		        shippingOrderService;

  private OrderService				            orderService;

  @Autowired
  private ShipmentService 			          shipmentService;

  private AdminShippingOrderService       adminShippingOrderService;

  @Autowired
  private AdminInventoryService           adminInventoryService;

  @Autowired
  private ProductVariantService           productVariantService;

  @Autowired
  private PaymentService paymentService;

  @Transactional
  public ShippingOrder autoEscalateShippingOrder(ShippingOrder shippingOrder, boolean firewall) {
    if(isShippingOrderAutoEscalable(shippingOrder, firewall)){
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

      if(shippingOrder.getBaseOrder().getStore().getId()==3){
          reasons.add(EnumReason.FITNESSPRO_ORDER_SHIPMENT_NOT_CREATED.asReason());
          return false;
      }
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
        // finding line items with inventory mismatch
        shippingOrder = this.autoProcessInventoryMismatch(shippingOrder, getUserService().getAdminUser());
        if (shippingOrder == null || shippingOrder.getOrderStatus().equals(EnumShippingOrderStatus.SO_Cancelled)) {
          return false;
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
          if (firewall && estimatedShippingCharges != null && estimatedShippingCharges > SOFirewall.minAllowedShippingCharges
              && estimatedShippingCharges >= SOFirewall.calculateCutoffAmount(shippingOrder)) {
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
          EnumShippingOrderLifecycleActivity.SO_CouldNotBeAutoEscalatedToProcessingQueue,
          EnumReason.InvalidPaymentStatus.asReason());
      return false;
    }
    return false;
  }

  @Transactional
  private ShippingOrder escalateShippingOrderFromActionQueue(ShippingOrder shippingOrder) {
    EnumShippingOrderStatus applicableStatus = shippingOrder.isDropShipping() ?
        EnumShippingOrderStatus.SO_ReadyForDropShipping : EnumShippingOrderStatus.SO_ReadyForProcess;
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
        if(!(shippingOrder.isServiceOrder())){
          User loggedInUser = getUserService().getLoggedInUser();
          if(loggedInUser == null){
            loggedInUser = getUserService().getAdminUser();
          }
          shippingOrderService.validateShippingOrder(shippingOrder);
          shippingOrder = this.autoProcessInventoryMismatch(shippingOrder, loggedInUser);
          if (shippingOrder == null || shippingOrder.getOrderStatus().equals(EnumShippingOrderStatus.SO_Cancelled)) {
            return false;
          }
          if(shippingOrder.getShipment() == null && !shippingOrder.isDropShipping()){
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
      shippingOrderService.logShippingOrderActivityByAdmin(shippingOrder,
          EnumShippingOrderLifecycleActivity.SO_CouldNotBeAutoEscalatedToProcessingQueue, EnumReason.InvalidPaymentStatusManual.asReason());
      return false;
    }
    return false;
  }

  private ShippingOrder autoProcessInventoryMismatch(ShippingOrder shippingOrder, User user) {
    boolean cancelFlag = false;
    boolean isSOProcessable = false;
    Map<String, ShippingOrder> splittedOrders = new HashMap<String, ShippingOrder>();
    List<String> messages = new ArrayList<String>();
    Set<LineItem> selectedItems = new HashSet<LineItem>();

    if (!shippingOrder.isDropShipping()) {
      for (LineItem lineItem : shippingOrder.getLineItems()) {
        if(lineItem.getCartLineItem().getCartLineItemConfig() != null){
          continue;
        }

        Long orderedQty = lineItem.getQty();
        // Check for inventory mismatch for non drop shipping orders

        if (lineItem.getSkuItemLineItems()== null) {
          // if no inventory has been booked for the line item
          selectedItems.add(lineItem);
          String comments = "No available booking found for the product variant " + lineItem.getSku().getProductVariant();
          logger.debug(comments);
          shippingOrderService.logShippingOrderActivity(shippingOrder, user,
              shippingOrderService.getShippingOrderLifeCycleActivity(
                  EnumShippingOrderLifecycleActivity.SO_CouldNotBeManuallyEscalatedToProcessingQueue),
              EnumReason.PROD_INV_MISMATCH.asReason(), comments);
        } else if( this.countValidSILI(lineItem.getSkuItemLineItems()) < orderedQty) {
          // also if there is no unbooked inventory at different MRP
          if (inventoryService.getAvailableUnbookedInventory(lineItem.getSku(), null) < orderedQty) {
            selectedItems.add(lineItem);
            String comments = "Invalid or no inventory booked for " + lineItem.getSku().getProductVariant();
            logger.debug(comments);
            shippingOrderService.logShippingOrderActivity(shippingOrder, user,
                shippingOrderService.getShippingOrderLifeCycleActivity(
                    EnumShippingOrderLifecycleActivity.SO_CouldNotBeManuallyEscalatedToProcessingQueue),
                EnumReason.PROD_INV_MISMATCH.asReason(), comments);
          }
        }
      }
    }

    if (selectedItems.size() > 0) {
      if (selectedItems.size() == shippingOrder.getLineItems().size()) {
        cancelFlag = this.cancelUnfulfilledSO(shippingOrder, user);
      } else {
        boolean splitSuccess = this.autoSplitSO(shippingOrder, selectedItems, splittedOrders,
            messages);
        if (splitSuccess) {
          isSOProcessable = true;
          ShippingOrder cancelledSO = splittedOrders.get(ShippingOrderConstants.NEW_SHIPPING_ORDER);
          cancelFlag = this.cancelUnfulfilledSO(cancelledSO, user);

        }
      }
    } else {
      isSOProcessable = true;
    }
    if (isSOProcessable || (!isSOProcessable && cancelFlag)) {
      return shippingOrder;
    } else {
      return null;
    }
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
  public boolean autoSplitSO(ShippingOrder shippingOrder, Set<LineItem> selectedLineItems, Map<String,
      ShippingOrder> splittedOrders, List<String> messages ) {

    Map<String, Boolean> flagMapOldSO = new HashMap<String, Boolean>();
    Map<String, Boolean> flagMapNewSO = new HashMap<String, Boolean>();
    flagMapOldSO.put("dropShipItemPresent", false);
    flagMapOldSO.put("jitItemPresent", false);
    flagMapNewSO.put("dropShipItemPresent", false);
    flagMapNewSO.put("jitItemPresent", false);


    if (shippingOrder != null ) {
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
      ShippingOrder newShippingOrder = shippingOrderService.createSOWithBasicDetails(shippingOrder.getBaseOrder(),
          shippingOrder.getWarehouse());
      newShippingOrder.setServiceOrder(false);
      newShippingOrder.setOrderStatus(shippingOrderStatusService.find(EnumShippingOrderStatus.SO_ActionAwaiting));
      newShippingOrder = shippingOrderService.save(newShippingOrder);

      for (LineItem selectedLineItem : selectedLineItems) {
        selectedLineItem.setShippingOrder(newShippingOrder);
        if ((selectedLineItem.getSku().getProductVariant().getProduct().isDropShipping())) {
          flagMapNewSO.put("dropShipItemPresent", true);
        }
        if ((selectedLineItem.getSku().getProductVariant().getProduct().isJit())) {
          flagMapNewSO.put("jitItemPresent", true);
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

      shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_Split,
          null,"SO split, new SO created with ID " + newShippingOrder.getId());

      shippingOrderService.logShippingOrderActivity(newShippingOrder, EnumShippingOrderLifecycleActivity.SO_Split,
          null,"SO split, for old Shipping order with ID " + shippingOrder.getId());

      if (splittedOrders != null) {
        splittedOrders.put(ShippingOrderConstants.OLD_SHIPPING_ORDER, shippingOrder);
        splittedOrders.put(ShippingOrderConstants.NEW_SHIPPING_ORDER, newShippingOrder);
      }
      //Handling the PO against the shipping Orders
      if(shippingOrder.getPurchaseOrders()!=null && shippingOrder.getPurchaseOrders().size()>0){
        this.getAdminShippingOrderService().adjustPurchaseOrderForSplittedShippingOrder(shippingOrder,
            newShippingOrder);
      }

      messages.add(("Shipping Order : " + shippingOrder.getGatewayOrderId() + " was split."));
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

  /**
   * This method is used to count valid Sku line items to handle inventory mismatch
   * @param items
   * @return
   */
  private Integer countValidSILI(List<SkuItemLineItem> items) {
    Integer count = 0;
    for (SkuItemLineItem item: items) {
      if (item.getSkuItem().getSkuItemStatus().getId().equals(EnumSkuItemStatus.BOOKED.getId())) {
        if (EnumSkuGroupStatus.UNDER_REVIEW.equals(item.getSkuItem().getSkuGroup().getStatus())) {
          break;
        } else {
          count++;
        }
      }
    }
    return count;
  }

  /**
   * This method checks and cancels an SO only if it can not be fulfilled by any warehouse.
   * @param shippingOrder
   * @param user
   */
  private boolean cancelUnfulfilledSO (ShippingOrder shippingOrder, User user) {
    Set<LineItem> outOfStockLineItems = new HashSet<LineItem>();
    Set<LineItem> outOfStockJitItems = new HashSet<LineItem>();
    StringBuilder comment = new StringBuilder();
    ShippingOrder cancelledSO = null;
    for (LineItem lineItem : shippingOrder.getLineItems()) {
      Long netQty = adminInventoryService.getNetInventoryAtServiceableWarehouses(lineItem.getSku().getProductVariant());
      Long bookedQty = adminInventoryService.getBookedInventory(lineItem.getSku().getProductVariant(), null);
      if (!((netQty - bookedQty) >= lineItem.getQty())){
        ProductVariant itemVariant = lineItem.getSku().getProductVariant();
        if (itemVariant.getProduct().isJit()) {
          outOfStockJitItems.add(lineItem);
        } else {
          outOfStockLineItems.add(lineItem);
        }
      }
    }
    // BLock to handle JIT items or free variants begin
    if (!outOfStockJitItems.isEmpty()) {
      if (outOfStockJitItems.size() == shippingOrder.getLineItems().size()) {
        // just log that jit items hence could not be cancelled automatically
        shippingOrderService.logShippingOrderActivity(shippingOrder,
            EnumShippingOrderLifecycleActivity.SO_COULD_NOT_BE_CANCELLED_AUTO, EnumReason.JIT_ITEMS_IN_SO.asReason(),
            "JIT items or free product present.");
        return false;
      } else {
        // only split jit items but do not cancel those items and escalate rest of the order if possible
        Map<String,ShippingOrder> splittedOrdersForJit = new HashMap<String, ShippingOrder>();
        List<String> msgs = new ArrayList<String>();
        this.autoSplitSO(shippingOrder, outOfStockJitItems, splittedOrdersForJit, msgs);
        shippingOrder = splittedOrdersForJit.get(ShippingOrderConstants.OLD_SHIPPING_ORDER);
        shippingOrderService.logShippingOrderActivity(splittedOrdersForJit.get(ShippingOrderConstants.NEW_SHIPPING_ORDER),
            EnumShippingOrderLifecycleActivity.SO_COULD_NOT_BE_CANCELLED_AUTO, EnumReason.JIT_ITEMS_IN_SO.asReason(),
            null);
      }
    }
    // Block to handle JIt items end

    // Other items inventory mismatch begin
    if (outOfStockLineItems.size() == shippingOrder.getLineItems().size()) {
      cancelledSO = shippingOrder;
    } else if (outOfStockLineItems.isEmpty()) {
      // nothing to cancel
      comment.append("Inventory for the variant ");
      for (LineItem item : shippingOrder.getLineItems() ) {
        comment.append(item.getCartLineItem().getProductVariant().getId() + " ");
      }
      comment.append("found in another warehouse.");
      shippingOrderService.logShippingOrderActivity(shippingOrder,
          EnumShippingOrderLifecycleActivity.SO_COULD_NOT_BE_CANCELLED_AUTO,
          EnumReason.INV_FOUND_DIFF_WAREHOUSE.asReason(), comment.toString());

      return false;
    } else {
      Map<String,ShippingOrder> splittedOrders = new HashMap<String, ShippingOrder>();
      List<String>  messages = new ArrayList<String>();
      // split and cancel only the unfulfilled line items
      this.autoSplitSO(shippingOrder, outOfStockLineItems, splittedOrders, messages);
      cancelledSO = splittedOrders.get(ShippingOrderConstants.NEW_SHIPPING_ORDER);
      shippingOrder = splittedOrders.get(ShippingOrderConstants.OLD_SHIPPING_ORDER);
      
      // log the items which can be fulfilled by other warehouses.
      comment.append("Inventory for the variant ");
      for (LineItem item :shippingOrder.getLineItems() ) {
        comment.append(item.getCartLineItem().getProductVariant().getId() + " ");
      }
      comment.append("found in another warehouse.");
      shippingOrderService.logShippingOrderActivity(shippingOrder,
          EnumShippingOrderLifecycleActivity.SO_COULD_NOT_BE_CANCELLED_AUTO,
          EnumReason.INV_FOUND_DIFF_WAREHOUSE.asReason(), comment.toString());

    }

    Payment payment = cancelledSO.getBaseOrder().getPayment();
    Store store = cancelledSO.getBaseOrder().getStore();
    if (paymentService.isValidReconciliation(payment, store)) {
      this.getAdminShippingOrderService().cancelShippingOrder(cancelledSO, null,
                                                        EnumReconciliationActionType.RefundAmount.getId(), false);
    } else {
      this.getAdminShippingOrderService().cancelShippingOrder(cancelledSO, null, null, false);
    }
    // send mail
    if (cancelledSO.getBaseOrder().getShippingOrders().size() > 1) {
      emailManager.sendPartialOrderCancelEmailToUser(cancelledSO);
    } else {
      emailManager.sendOrderCancelEmailToUser(cancelledSO.getBaseOrder());
    }
    shippingOrderService.logShippingOrderActivity(cancelledSO, user,
        shippingOrderService.getShippingOrderLifeCycleActivity(EnumShippingOrderLifecycleActivity.SO_CancelledInventoryMismatch),
              EnumReason.InsufficientUnbookedInventoryManual.asReason(), "SO cancelled after splitting.");
      return true;
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
    if (this.orderService == null) {
      this.orderService = ServiceLocatorFactory.getService(OrderService.class);
    }
    return this.orderService;
  }

  /**
   * @return the orderService
   */
  public AdminShippingOrderService getAdminShippingOrderService() {
    if (this.adminShippingOrderService == null) {
      this.adminShippingOrderService = ServiceLocatorFactory.getService(AdminShippingOrderService.class);
    }
    return this.adminShippingOrderService;
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
