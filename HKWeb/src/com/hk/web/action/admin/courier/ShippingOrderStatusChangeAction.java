package com.hk.web.action.admin.courier;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.core.search.ShippingOrderSearchCriteria;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.ShippingOrderStatusMapping;
import com.hk.pact.dao.shippingOrder.ShippingOrderStatusDao;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;
import net.sourceforge.stripes.action.*;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import java.util.*;


/**
 * Created with IntelliJ IDEA.
 * User: Rajesh Kumar
 * Date: 3/20/13
 * Time: 5:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class ShippingOrderStatusChangeAction extends BaseAction{
    private String gatewayOrderId;
    private EnumShippingOrderStatus enumSoUpdatedStatusId;
    ShippingOrder shippingOrder;
    private static Logger logger = LoggerFactory.getLogger(ShippingOrderStatusChangeAction.class);

    List<ShippingOrder> shippingOrderList = new ArrayList<ShippingOrder>(0);
    List<EnumShippingOrderStatus> SOMapping = new ArrayList<EnumShippingOrderStatus>();
    ShippingOrderStatusMapping shippingOrderStatusMapping= new ShippingOrderStatusMapping();


    @Autowired
    ShippingOrderStatusService shippingOrderStatusService;
    @Autowired
    ShippingOrderService shippingOrderService;

    @DefaultHandler
    public Resolution pre() {
          return new ForwardResolution("/pages/admin/courier/changeShippingOrderStatus.jsp");
      }



    @SuppressWarnings("unchecked")
    public Resolution search(){
                shippingOrder = shippingOrderService.findByGatewayOrderId(gatewayOrderId);
                if(shippingOrder==null){
                 addRedirectAlertMessage(new SimpleMessage("Invalid Gateway Order id"));
                 return new RedirectResolution("/pages/admin/courier/changeShippingOrderStatus.jsp");
                }

            if(!EnumShippingOrderStatus.getApplicableShippingOrderStatus().contains(shippingOrder.getShippingOrderStatus().getId())){
               addRedirectAlertMessage(new SimpleMessage("SO is not in applicable status!!!!"));
               return new RedirectResolution("/pages/admin/courier/changeShippingOrderStatus.jsp");
            }
           SOMapping = shippingOrderStatusMapping.getShippingOrderStatusMap().get(shippingOrder.getShippingOrderStatus().getName());
           return new ForwardResolution("/pages/admin/courier/changeShippingOrderStatus.jsp");
    }

    public Resolution saveStatus(){
        if(shippingOrder!=null){
          shippingOrder.setOrderStatus(enumSoUpdatedStatusId.asShippingOrderStatus());
          getBaseDao().save(shippingOrder);

        }
        addRedirectAlertMessage(new SimpleMessage("Changes Saved Successfully!!!"));
        return new RedirectResolution(ShippingOrderStatusChangeAction.class);
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
    public List<EnumShippingOrderStatus> getSOMapping() {
           return SOMapping;
       }

       public void setSOMapping(List<EnumShippingOrderStatus> SOMapping) {
           this.SOMapping = SOMapping;
       }

    public EnumShippingOrderStatus getEnumSoUpdatedStatusId() {
        return enumSoUpdatedStatusId;
    }

    public void setEnumSoUpdatedStatusId(EnumShippingOrderStatus enumSoUpdatedStatusId) {
        this.enumSoUpdatedStatusId = enumSoUpdatedStatusId;
    }
}
