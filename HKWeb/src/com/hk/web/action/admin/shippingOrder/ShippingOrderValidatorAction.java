package com.hk.web.action.admin.shippingOrder;

import java.util.*;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.constants.sku.EnumSkuItemStatus;
import com.hk.constants.sku.EnumSkuItemOwner;
import com.hk.constants.core.RoleConstants;
import com.hk.core.search.ShippingOrderSearchCriteria;
import com.hk.domain.core.PaymentStatus;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.order.ShippingOrderStatus;
import com.hk.domain.sku.SkuItemLineItem;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.sku.SkuGroup;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.inventory.SkuGroupService;
import com.hk.pact.service.inventory.InventoryHealthService;
import com.hk.pact.dao.sku.SkuItemLineItemDao;
import com.hk.web.action.admin.booking.AdminBookingAction;
import com.hk.web.action.admin.queue.ActionAwaitingQueueAction;

@Component
@Secure(hasAnyRoles = {RoleConstants.ADMIN})
public class ShippingOrderValidatorAction extends BaseAction {

  private static Logger logger = LoggerFactory.getLogger(ShippingOrderValidatorAction.class);

  @Autowired
  ShippingOrderService shippingOrderService;

  @Autowired
  SkuItemLineItemDao skuItemLineItemDao;

  @Autowired
  SkuGroupService skuGroupService;

  @Autowired
  InventoryHealthService inventoryHealthService;

  private ShippingOrder shippingOrder;


  @DefaultHandler
  public Resolution pre() {
    ShippingOrderSearchCriteria shippingOrderSearchCriteria = getShippingOrderSearchCriteria();
    List<ShippingOrder> shippingOrders = shippingOrderService.searchShippingOrders(shippingOrderSearchCriteria);

    shippingOrders = getSortedShippingOrders(shippingOrders);
    Set<ShippingOrder> sortedShippingOrdersSet = new HashSet<ShippingOrder>(shippingOrders);

    for (ShippingOrder shippingOrder : sortedShippingOrdersSet) {
      logger.debug("Validating Shipping Order -" + shippingOrder.getId());
      try {
//        shippingOrderService.validateShippingOrderAB(shippingOrder);
      }
      catch (Exception e) {
        logger.debug("Exception while validating the Shipping Order" + shippingOrder.getId() + " " + e.getMessage());
        e.printStackTrace();
      }

    }
    addRedirectAlertMessage(new SimpleMessage("Shipping Orders Validated and entries in tables adjusted"));
    return new RedirectResolution(ActionAwaitingQueueAction.class);
  }

  public Resolution validateSO() {
    if (shippingOrder != null) {
      logger.debug("Validating Shipping Order -" + shippingOrder.getId());
      try {
         shippingOrderService.validateShippingOrderAB(shippingOrder);
      }
      catch (Exception e) {
        logger.debug("Exception while validating the Shipping Order" + shippingOrder.getId() + " " + e.getMessage());
        e.printStackTrace();
      }
      addRedirectAlertMessage(new SimpleMessage("Tried to Validate Shipping Order :"+shippingOrder.getId()+" <br/>Please refer to SO LifeCycleActivity for more info."));
    }
    return new RedirectResolution(AdminBookingAction.class).addParameter("getSkuItemLineItems").addParameter("shippingOrderId", shippingOrder.getId());
  }

  public Resolution fixDuplicateSI() {
    long count = 0;
    List<SkuItemLineItem> skuItemLineItems = skuItemLineItemDao.getSkuItemLIsTemp();
    logger.debug("LIs with duplicate entries = " + skuItemLineItems.size());
    for (SkuItemLineItem sili : skuItemLineItems) {
      SkuItem oldSkuItem = null;
      SkuGroup group = null;
      List<SkuItem> skuItems = null;
      SkuItem outSkuItem = sili.getSkuItem();
      List<SkuItemLineItem> skuItemLIs = skuItemLineItemDao.getSkuItemLIsTemp(outSkuItem);
      logger.debug("SILIs with duplicate entries for SKUITEM = " + outSkuItem + "; Records=" + skuItemLIs.size());
      for (SkuItemLineItem skuItemLineItem : skuItemLIs) {
        SkuItem skuItem = skuItemLineItem.getSkuItem();
        logger.debug("SILI=" + skuItemLineItem.getId() + "; SI=" + skuItem.getId());
        if (oldSkuItem != null && oldSkuItem.getId().longValue() == skuItem.getId().longValue()) {
          logger.debug("Do fixing...");
          group = skuItem.getSkuGroup();
          skuItems = skuGroupService.getSkuItems(Arrays.asList(group.getSku()),
              Arrays.asList(EnumSkuItemStatus.Checked_IN.getId()), Arrays.asList(EnumSkuItemOwner.SELF.getId()), group.getMrp());
          logger.debug("Instock Units=" + (skuItems != null ? skuItems.size() : 0L));
          if (!skuItems.isEmpty()) {
            SkuItem newSkuItem = skuItems.get(0);
            newSkuItem.setSkuItemStatus(EnumSkuItemStatus.BOOKED.getSkuItemStatus());
            skuGroupService.saveSkuItem(newSkuItem);

            skuItemLineItem.setSkuItem(newSkuItem);
            skuItemLineItemDao.save(skuItemLineItem);

            count++;
          } else {
            logger.debug("Insuff Inventory");
          }
        }
        oldSkuItem = skuItem;
      }
      //Inventory Check
      inventoryHealthService.inventoryHealthCheck(outSkuItem.getSkuGroup().getSku().getProductVariant());
    }

    addRedirectAlertMessage(new SimpleMessage("Duplicate SI fixed on SILI. Total fixed SILIs = " + count));
    return new RedirectResolution(ActionAwaitingQueueAction.class);
  }

  public Resolution bookSo() {
    if (shippingOrder != null) {
      logger.debug("Validating Shipping Order -" + shippingOrder.getId());
      try {
        shippingOrderService.bookSo(shippingOrder);
      }
      catch (Exception e) {
        logger.debug("Exception while booking  the Shipping Order" + shippingOrder.getId() + " " + e.getMessage());
        e.printStackTrace();
      }
      addRedirectAlertMessage(new SimpleMessage("Tried to Book Shipping Order :"+shippingOrder.getId()+" <br/>Please refer to SO LifeCycleActivity for more info."));
    }
    return new RedirectResolution(AdminBookingAction.class).addParameter("getSkuItemLineItems").addParameter("shippingOrderId", shippingOrder.getId());
  }

  public ShippingOrderSearchCriteria getShippingOrderSearchCriteria() {
    ShippingOrderSearchCriteria shippingOrderSearchCriteria = new ShippingOrderSearchCriteria();
    List<ShippingOrderStatus> soStatusList = new ArrayList<ShippingOrderStatus>();
    soStatusList.add(EnumShippingOrderStatus.SO_ActionAwaiting.asShippingOrderStatus());
    soStatusList.add(EnumShippingOrderStatus.SO_OnHold.asShippingOrderStatus());
    soStatusList.add(EnumShippingOrderStatus.SO_Picking.asShippingOrderStatus());
    soStatusList.add(EnumShippingOrderStatus.SO_ReadyForDropShipping.asShippingOrderStatus());
    soStatusList.add(EnumShippingOrderStatus.SO_ReadyForProcess.asShippingOrderStatus());
    soStatusList.add(EnumShippingOrderStatus.SO_MarkedForPrinting.asShippingOrderStatus());
    soStatusList.add(EnumShippingOrderStatus.SO_EscalatedBack.asShippingOrderStatus());
    shippingOrderSearchCriteria.setShippingOrderStatusList(soStatusList);
    List<PaymentStatus> paymentStatusList = new ArrayList<PaymentStatus>();
    paymentStatusList.add(EnumPaymentStatus.SUCCESS.asPaymenStatus());
    paymentStatusList.add(EnumPaymentStatus.ON_DELIVERY.asPaymenStatus());
    paymentStatusList.add(EnumPaymentStatus.AUTHORIZATION_PENDING.asPaymenStatus());
    shippingOrderSearchCriteria.setPaymentStatuses(paymentStatusList);
    return shippingOrderSearchCriteria;
  }

  public class ShippingOrderComparator implements Comparator<ShippingOrder> {

    @Override
    public int compare(ShippingOrder o1, ShippingOrder o2) {
      if (o1.getCreateDate() != null && o2.getCreateDate() != null) {
        return o1.getCreateDate().compareTo(o2.getCreateDate());
      }
      return -1;
    }

  }

  public List<ShippingOrder> getSortedShippingOrders(List<ShippingOrder> shippingOrders) {
    Collections.sort(shippingOrders, new ShippingOrderComparator());
    return shippingOrders;
  }

  public ShippingOrder getShippingOrder() {
    return shippingOrder;
  }

  public void setShippingOrder(ShippingOrder shippingOrder) {
    this.shippingOrder = shippingOrder;
  }
}
