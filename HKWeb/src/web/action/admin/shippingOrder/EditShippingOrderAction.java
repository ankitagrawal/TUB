package web.action.admin.shippingOrder;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;

import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import web.action.error.AdminPermissionAction;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.dao.order.cartLineItem.CartLineItemDao;
import com.hk.dao.shippingOrder.lineItem.LineItemDao;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.Sku;
import com.hk.service.SkuService;
import com.hk.service.UserService;
import com.hk.service.shippingOrder.ShippingOrderService;

@Component
public class EditShippingOrderAction extends BaseAction {

   ShippingOrderService shippingOrderService;

  @Validate(required = true)
  private ShippingOrder shippingOrder;

  
  UserService userService;

  
  SkuService skuService;

  private LineItem lineItem;

  
  LineItemDao lineItemDao;

  
  CartLineItemDao cartLineItemDao;

  public Map<Sku, Sku> skuMap = new HashMap<Sku, Sku>();

  @DefaultHandler
  @Secure(hasAnyPermissions = {PermissionConstants.UPDATE_ORDER}, authActionBean = AdminPermissionAction.class)
  public Resolution pre() {
    return new ForwardResolution("/pages/admin/editOrder.jsp");
  }

  public Resolution editOrder() {
    if (skuMap != null && skuMap.size() > 0) {
      StringBuilder commentBuilder = new StringBuilder("SO Fliping variants:");
      for (Sku srcSku : skuMap.keySet()) {
        Sku destSku = skuMap.get(srcSku);
        if (srcSku != null && !srcSku.getId().equals(destSku.getId())) {
          ProductVariant srcPV = srcSku.getProductVariant();
          ProductVariant destPV = destSku.getProductVariant();
          lineItemDao.flipProductVariants(srcSku, skuMap.get(srcSku), shippingOrder);
          cartLineItemDao.flipProductVariants(srcPV, destPV, shippingOrder.getBaseOrder());
          commentBuilder.append("Variant " + srcPV.getOptionsCommaSeparated() + " changed to " + destPV.getOptionsCommaSeparated() + "of Product " + srcPV.getProduct().getName());
        }
      }
      shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_Edited, commentBuilder.toString());

      addRedirectAlertMessage(new SimpleMessage("Order edited successfully."));
    }

    return new

        RedirectResolution(EditShippingOrderAction.class)

        .

            addParameter("shippingOrder", shippingOrder);

  }

  @Secure(hasAnyPermissions = {PermissionConstants.DELETE_LINEITEM}, authActionBean = AdminPermissionAction.class)
  public Resolution deleteLineItem() {
    /* User user = null;
    if (getPrincipal() != null) {
      user = userDao.find(getPrincipal().getId());
    }
    String deletedItem = "Deleted Item: ";
    Double amountToBeLess = lineItem.getHkPrice() * lineItem.getQty() - lineItem.getDiscountOnHkPrice();
    LineItem orderLevelDiscountLineItem = lineItemDao.getOrderLevelDiscountLineItem(lineItem);
    if (orderLevelDiscountLineItem != null) {
      amountToBeLess -= orderLevelDiscountLineItem.getDiscountOnHkPrice();
      lineItemDao.remove(orderLevelDiscountLineItem.getId());
    }
    deletedItem += lineItem.getProductVariant().getProduct().getName() + "<br/>" + lineItem.getProductVariant().getOptionsCommaSeparated();
    lineItemDao.remove(lineItem.getId());

    *//**
     * Order lifecycle activity logging - LineItem Deleted
     *//*
    orderManager.logOrderActivity(order, user, orderLifecycleActivityDao.find(EnumOrderLifecycleActivity.LineItemDeleted.getId()), deletedItem);

    Order order = lineItem.getShippingOrder();
    order.setAmount(order.getAmount() - amountToBeLess);
    orderDao.save(order);

    Payment payment = order.getPayment();
    payment.setAmount(payment.getAmount() - amountToBeLess);
    paymentDao.save(payment);

    addRedirectAlertMessage(new SimpleMessage("Item deleted successfully."));
    return new RedirectResolution(EditShippingOrderAction.class).addParameter("order", order);*/

    //TODO: # warehouse fix this.

    return null;
  }

  public ShippingOrder getShippingOrder() {
    return shippingOrder;
  }

  public void setShippingOrder(ShippingOrder shippingOrder) {
    this.shippingOrder = shippingOrder;
  }

  public Map<Sku, Sku> getSkuMap() {
    return skuMap;
  }

  public void setSkuMap(Map<Sku, Sku> skuMap) {
    this.skuMap = skuMap;
  }

  public LineItem getLineItem() {
    return lineItem;
  }

  public void setLineItem(LineItem lineItem) {
    this.lineItem = lineItem;
  }
}