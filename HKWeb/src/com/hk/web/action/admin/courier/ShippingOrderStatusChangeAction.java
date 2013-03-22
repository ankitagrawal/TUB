package com.hk.web.action.admin.courier;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.core.search.ShippingOrderSearchCriteria;
import com.hk.domain.order.ShippingOrder;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;
import net.sourceforge.stripes.action.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import java.util.ArrayList;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: Rajesh Kumar
 * Date: 3/20/13
 * Time: 5:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class ShippingOrderStatusChangeAction extends BaseAction{
    private String gatewayOrderId;
    ShippingOrder shippingOrder;
    private static Logger logger = LoggerFactory.getLogger(ShippingOrderStatusChangeAction.class);

        List<ShippingOrder> shippingOrderList = new ArrayList<ShippingOrder>(0);



    @Autowired
    ShippingOrderStatusService shippingOrderStatusService;
    @Autowired
    ShippingOrderService shippingOrderService;

    @DefaultHandler
    public Resolution pre() {
          return new ForwardResolution("/pages/admin/courier/changeShippingOrderStatus.jsp");
      }
    public Resolution search(){
        ShippingOrderSearchCriteria shippingOrderSearchCriteria = new ShippingOrderSearchCriteria();
                shippingOrderSearchCriteria.setGatewayOrderId(gatewayOrderId);
                shippingOrderSearchCriteria.setShippingOrderStatusList(shippingOrderStatusService.getOrderStatuses(EnumShippingOrderStatus.getStatusForShippingOrderChange()));
                shippingOrderList = shippingOrderService.searchShippingOrders(shippingOrderSearchCriteria, false);

                if (shippingOrderList.isEmpty()) {
                    addRedirectAlertMessage(new SimpleMessage("Invalid Gateway Order id or Shipping Order is not in applicable SO Status"));
                    return new RedirectResolution("/pages/admin/courier/changeShippingOrderStatus.jsp");
                }
            shippingOrder = shippingOrderList.get(0);
        return new ForwardResolution("/pages/admin/courier/changeShippingOrderStatus.jsp");
    }

    public String getGatewayOrderId() {
            return gatewayOrderId;
        }

        public void setGatewayOrderId(String gatewayOrderId) {
            this.gatewayOrderId = gatewayOrderId;
        }

    public ShippingOrder getShippingOrder() {
         return shippingOrder;
     }

     public void setShippingOrder(ShippingOrder shippingOrder) {
         this.shippingOrder = shippingOrder;
     }


    public List<ShippingOrder> getShippingOrderList() {
        return shippingOrderList;
    }

    public void setShippingOrderList(List<ShippingOrder> shippingOrderList) {
        this.shippingOrderList = shippingOrderList;
    }


}
