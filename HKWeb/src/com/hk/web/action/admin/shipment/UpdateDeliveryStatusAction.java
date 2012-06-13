package com.hk.web.action.admin.shipment;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationMethod;
import net.sourceforge.stripes.validation.SimpleError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.manager.DeliveryStatusUpdateManager;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.courier.CourierConstants;
import com.hk.domain.user.User;
import com.hk.util.CustomDateTypeConvertor;
import com.hk.web.action.error.AdminPermissionAction;


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
            try {
                if (courierName.equalsIgnoreCase(CourierConstants.AFL)) {
                    numberOfOrdersUpdated = deliveryStatusUpdateManager.updateDeliveryStatusAFL(startDate, endDate, loggedOnUser);


                } else if (courierName.equalsIgnoreCase(CourierConstants.CHHOTU)) {
                    numberOfOrdersUpdated = deliveryStatusUpdateManager.updateDeliveryStatusChhotu(startDate, endDate, loggedOnUser);

                } else if (courierName.equalsIgnoreCase(CourierConstants.DTDC)) {
                    numberOfOrdersUpdated = deliveryStatusUpdateManager.updateDeliveryStatusDTDC(startDate, endDate);

                } else if (courierName.equalsIgnoreCase(CourierConstants.DELHIVERY)) {
                    numberOfOrdersUpdated = deliveryStatusUpdateManager.updateDeliveryStatusDelhivery(startDate, endDate, loggedOnUser);

                } else if (courierName.equalsIgnoreCase(CourierConstants.BLUEDART)) {
                    numberOfOrdersUpdated = deliveryStatusUpdateManager.updateDeliveryStatusBlueDart(startDate, endDate, loggedOnUser);


                }
            } catch (Exception e) {
                logger.error("Exception occurred in updateCourierStatus of UpdateDeliveryStatusAction .", e);
                addRedirectAlertMessage(new SimpleMessage("Database Updation failed."));
                return new ForwardResolution("/pages/admin/updateCourierDeliveryStatus.jsp");
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
