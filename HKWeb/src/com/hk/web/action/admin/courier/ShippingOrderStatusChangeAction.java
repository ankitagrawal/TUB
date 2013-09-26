package com.hk.web.action.admin.courier;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.akube.framework.util.DateUtils;
import com.hk.domain.courier.Shipment;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.service.shippingOrder.AdminShippingOrderService;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.ShippingOrderStatusMapping;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;
import com.hk.web.action.error.AdminPermissionAction;


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
    private String currentStatus;
    ShippingOrder shippingOrder;
    private Date statusDate;

    private static Logger logger = LoggerFactory.getLogger(ShippingOrderStatusChangeAction.class);

    List<EnumShippingOrderStatus> SOMapping = new ArrayList<EnumShippingOrderStatus>();
    ShippingOrderStatusMapping shippingOrderStatusMapping= new ShippingOrderStatusMapping();

    private List<ShippingOrder> shippingOrderList = new ArrayList<ShippingOrder>();

    @Autowired
    ShippingOrderStatusService shippingOrderStatusService;
    @Autowired
    ShippingOrderService shippingOrderService;
    
    @Autowired
	  AdminShippingOrderService adminShippingOrderService;


    @DefaultHandler
    public Resolution pre() {
          return new ForwardResolution("/pages/admin/courier/changeShippingOrderStatus.jsp");
      }

    @Secure(hasAnyPermissions = {PermissionConstants.OPS_MANAGER_SRS_CHANGE_SOSTATUS}, authActionBean = AdminPermissionAction.class)
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
           shippingOrderList.add(shippingOrder);
           SOMapping = shippingOrderStatusMapping.getShippingOrderStatusMap().get(shippingOrder.getShippingOrderStatus().getName());
           return new ForwardResolution("/pages/admin/courier/changeShippingOrderStatus.jsp");
    }

    @Secure(hasAnyPermissions = {PermissionConstants.OPS_MANAGER_SRS_CHANGE_SOSTATUS}, authActionBean = AdminPermissionAction.class)
    public Resolution saveStatus(){
      if(shippingOrder!=null && statusDate !=null &&  statusDate.after(shippingOrder.getShipment().getShipDate()) && statusDate.before(DateUtils.getEndOfDay(new Date())) ){
        currentStatus=shippingOrder.getShippingOrderStatus().getName();
        Shipment shippingOrderShipment = shippingOrder.getShipment();
        switch (enumSoUpdatedStatusId) {
          case SO_Delivered:
            shippingOrderShipment.setDeliveryDate(statusDate);
            break;
          case SO_RTO:
            shippingOrderShipment.setRtoDate(statusDate);
            break;
          case RTO_Initiated:
            shippingOrderShipment.setRtoInitiatedDate(statusDate);
            break;
          case SO_Lost:
            shippingOrderShipment.setLostDate(statusDate);
            break;
        }
        shippingOrder.setOrderStatus(enumSoUpdatedStatusId.asShippingOrderStatus());
        getBaseDao().save(shippingOrderShipment);
        getBaseDao().save(shippingOrder);
          shippingOrderService.logShippingOrderActivity(shippingOrder,
              EnumShippingOrderLifecycleActivity.SHIPMENT_RESOLUTION_ACTIVITY, null,
              "Current Status-->" + currentStatus + " New Status-->" + enumSoUpdatedStatusId.getName());
        shippingOrderList.add(shippingOrder);
        addRedirectAlertMessage(new SimpleMessage("Changes Saved Successfully!!!"));
      } else {
        addRedirectAlertMessage(new SimpleMessage("Please enter status change date which " +
            "must be between shipping date and today."));
      }
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

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

	/**
	 * @return the statusDate
	 */
	public Date getStatusDate() {
		return statusDate;
	}

	/**
	 * @param statusDate the statusDate to set
	 */
	public void setStatusDate(Date statusDate) {
		this.statusDate = statusDate;
	}

  public List<ShippingOrder> getShippingOrderList() {
    return shippingOrderList;
  }

  public void setShippingOrderList(List<ShippingOrder> shippingOrderList) {
    this.shippingOrderList = shippingOrderList;
  }
}
