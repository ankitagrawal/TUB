package web.action.admin.order;

import java.util.List;

import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import web.action.admin.AdminHomeAction;
import web.action.error.AdminPermissionAction;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.impl.service.shippingOrder.ShipmentServiceImpl;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.domain.courier.Shipment;
import com.hk.domain.order.ShippingOrder;
import com.hk.manager.EmailManager;
import com.hk.manager.LinkManager;
import com.hk.service.shippingOrder.ShippingOrderService;
import com.hk.service.shippingOrder.ShippingOrderStatusService;

@Secure(hasAnyPermissions = {PermissionConstants.UPDATE_SHIPMENT_QUEUE}, authActionBean = AdminPermissionAction.class)
@Component
public class UpdateOrderStatusAndSendEmailAction extends BaseAction {

  private EmailManager emailManager;
  private ShippingOrderService shippingOrderService;
  private LinkManager linkManager;
  private ShippingOrderStatusService shippingOrderStatusService;
  private ShipmentServiceImpl shipmentService;

  public Resolution pre() {
    List<ShippingOrder> shippingOrderList = shippingOrderService.getShippingOrdersToSendShipmentEmail();

    for (ShippingOrder shippingOrder : shippingOrderList) {
      Shipment shipment = shippingOrder.getShipment();
      boolean isEmailSent = emailManager.sendOrderShippedEmail(shippingOrder, linkManager.getShippingOrderInvoiceLink(shippingOrder));
      if (isEmailSent && shipment != null) {
        shipment.setEmailSent(true);
        shipmentService.save(shipment);
      }
      shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_ShippedEmailFired, "");
    }

    addRedirectAlertMessage(new SimpleMessage("Shipping emails for [" + shippingOrderList.size() + "] Shipping Orders have been sent."));
    return new RedirectResolution(AdminHomeAction.class);
  }

  
  public void setEmailManager(EmailManager emailManager) {
    this.emailManager = emailManager;
  }

  
  public void setShippingOrderService(ShippingOrderService shippingOrderService) {
    this.shippingOrderService = shippingOrderService;
  }

  
  public void setLinkManager(LinkManager linkManager) {
    this.linkManager = linkManager;
  }

  
  public void setShippingOrderStatusService(ShippingOrderStatusService shippingOrderStatusService) {
    this.shippingOrderStatusService = shippingOrderStatusService;
  }

  
  public void setShipmentService(ShipmentServiceImpl shipmentService) {
    this.shipmentService = shipmentService;
  }
}
