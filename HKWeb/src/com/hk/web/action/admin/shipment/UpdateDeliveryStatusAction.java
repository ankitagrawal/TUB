package com.hk.web.action.admin.shipment;

import java.util.Date;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.manager.DeliveryStatusUpdateManager;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.user.User;
import com.hk.exception.HealthkartCheckedException;
import com.hk.util.CustomDateTypeConvertor;
import com.hk.web.action.error.AdminPermissionAction;


@Secure(hasAnyPermissions = {PermissionConstants.UPDATE_COURIER_DELIVERY_STATUS}, authActionBean = AdminPermissionAction.class)
@Component
public class UpdateDeliveryStatusAction extends BaseAction{


  private static     Logger                           logger = LoggerFactory.getLogger(UpdateDeliveryStatusAction.class);
  private            Date                             startDate;
  private            Date                             endDate;
  private            String                           courierName;
  private            List<String>                     unmodifiedTrackingIds;
  private            String                           unmodifiedTrackingIdsAsString =null;

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
            unmodifiedTrackingIds = deliveryStatusUpdateManager.getUnmodifiedTrackingIds();
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
        if (unmodifiedTrackingIds != null && unmodifiedTrackingIds.size() > 0) {
            unmodifiedTrackingIdsAsString = getUnmodifiedTrackingIdsAsString(unmodifiedTrackingIds);
        }
        return new ForwardResolution("/pages/admin/updateCourierDeliveryStatus.jsp");
    }

    public String getUnmodifiedTrackingIdsAsString(List<String> unmodifiedTrackingIds){
        StringBuffer strBuffr=new StringBuffer();
        for(String unmodifiedTrackingId:unmodifiedTrackingIds){
            strBuffr.append(unmodifiedTrackingId);
            strBuffr.append(",");
        }
        return strBuffr.toString();
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

    public String getUnmodifiedTrackingIdsAsString() {
        return unmodifiedTrackingIdsAsString;
    }

    public void setUnmodifiedTrackingIdsAsString(String unmodifiedTrackingIdsAsString) {
        this.unmodifiedTrackingIdsAsString = unmodifiedTrackingIdsAsString;
    }
}
