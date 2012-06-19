package com.hk.web.action.admin.shipment;

import java.util.Date;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.manager.DeliveryStatusUpdateManager;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.user.User;
import com.hk.util.CustomDateTypeConvertor;
import com.hk.web.action.error.AdminPermissionAction;
import com.hk.exception.HealthkartCheckedException;


@Secure(hasAnyPermissions = {PermissionConstants.UPDATE_DELIVERY_QUEUE}, authActionBean = AdminPermissionAction.class)
@Component
public class UpdateDeliveryStatusAction extends BaseAction{


  private static     Logger                           logger = LoggerFactory.getLogger(UpdateDeliveryStatusAction.class);
  private            Date                             startDate;
  private            Date                             endDate;
  private            String                           courierName;

  @Autowired
  private            DeliveryStatusUpdateManager      deliveryStatusUpdateManager;


    @DefaultHandler
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/updateCourierDeliveryStatus.jsp");
    }


    public Resolution updateCourierStatus() {
        User loggedOnUser = null;
        if (getPrincipal() != null) {
            loggedOnUser = getUserService().getUserById(getPrincipal().getId());
        }
        int numberOfOrdersUpdated = 0;
        if (courierName != null) {
            try{
            numberOfOrdersUpdated = deliveryStatusUpdateManager.updateCourierStatus(startDate, endDate, courierName);
            }catch (HealthkartCheckedException hce){
                addRedirectAlertMessage(new SimpleMessage(hce.getMessage()));
            }
            if (numberOfOrdersUpdated == 0) {
                addRedirectAlertMessage(new SimpleMessage("Sorry! no orders found for updation."));

            } else {
                addRedirectAlertMessage(new SimpleMessage("Database Successfully Updated -: " + numberOfOrdersUpdated + " orders Updated."));
            }
        } else {
            addRedirectAlertMessage(new SimpleMessage("Please select a courier."));
        }
        return new ForwardResolution("/pages/admin/updateCourierDeliveryStatus.jsp");
    }

    public Date getStartDate() {
        return startDate;
    }

    @Validate(converter = CustomDateTypeConvertor.class)
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    @Validate(converter = CustomDateTypeConvertor.class)
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getCourierName() {
        return courierName;
    }

    public void setCourierName(String courierName) {
        this.courierName = courierName;
    }
}
