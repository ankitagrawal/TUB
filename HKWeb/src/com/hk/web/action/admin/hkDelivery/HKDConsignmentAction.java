package com.hk.web.action.admin.hkDelivery;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.hkDelivery.Consignment;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.Shipment;
import com.hk.domain.user.User;
import com.hk.admin.pact.service.hkDelivery.ConsignmentService;
import com.hk.admin.pact.service.hkDelivery.HubService;
import com.hk.admin.pact.service.courier.AwbService;
import com.hk.admin.pact.service.shippingOrder.ShipmentService;
import com.hk.constants.courier.EnumCourier;
import com.hk.constants.hkDelivery.HKDeliveryConstants;
import net.sourceforge.stripes.action.*;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

@Component
public class HKDConsignmentAction extends BaseAction{

    private static       Logger               logger                   = LoggerFactory.getLogger(HKDConsignmentAction.class);
    private              Hub                  hub;
    private              List<String>         trackingIdList           = new ArrayList<String>();
    private              String               cnnNumber                = null;
    private              String               paymentMode              = null;
    private              Double               amount                   = null;

    @Autowired
    private              ConsignmentService   consignmentService;
    @Autowired
    private              HubService           hubService;
    @Autowired
    private              AwbService           awbService;
    @Autowired
    private              ShipmentService      shipmentService;




    @DefaultHandler
    public Resolution pre(){
        return new ForwardResolution("/pages/admin/hkDeliveryConsignment.jsp");        
    }

    public Resolution markShipmentsReceived() {
        Set<String>  trackingIdSet ;
        List<String> existingAwbNumbers;
        Hub          healthkartHub;
        String       duplicateAwbString = "";
        User         loggedOnUser       = null;
        Shipment     shipmentObj        = null;
        int          consignmentCreatedCount   = 0;
        Courier      hkDelivery         = EnumCourier.HK_Delivery.asCourier();
        Consignment consignment;

        if (trackingIdList != null && trackingIdList.size() > 0) {
            healthkartHub = hubService.findHubByName(HKDeliveryConstants.HEALTHKART_HUB);
            if (getPrincipal() != null) {
                loggedOnUser = getUserService().getUserById(getPrincipal().getId());
            }
            //Checking if consignment is already created for the enterd awbNumber and fetching the awb for the same .
            existingAwbNumbers = consignmentService.getDuplicateAwbs(trackingIdList,consignmentService.getAwbNumbersInConsignment());
            if (existingAwbNumbers.size() > 0) {
                // removing duplicated awbs from the list.
                trackingIdList.removeAll(existingAwbNumbers);
                // creating a string for user-display.
                duplicateAwbString = getDuplicateAwbString(existingAwbNumbers);
                duplicateAwbString =HKDeliveryConstants.CONSIGNMNT_DUPLICATION_MSG + duplicateAwbString;
            }
            // convertig list to set to delete/remove duplicate elements from the list.
                trackingIdSet = new HashSet<String>(trackingIdList);
            // Creating consignments.
            for (String awbNumber : trackingIdSet) {
            try {
                shipmentObj = shipmentService.findByAwb(awbService.findByCourierAwbNumber(hkDelivery,awbNumber));
                amount = shipmentObj.getShippingOrder().getAmount();
                cnnNumber = shipmentObj.getShippingOrder().getGatewayOrderId();
                paymentMode = consignmentService.getConsignmentPaymentMode(shipmentObj.getShippingOrder().getBaseOrder().getPayment().getPaymentMode());
                // Creating consignment object.
                consignment = consignmentService.createConsignment(awbNumber,cnnNumber,amount,paymentMode,hub);
                // Making an entry in consignment-tracking for the created consignment.
                consignmentService.updateConsignmentTracking(healthkartHub, hub, loggedOnUser, consignment);
                consignmentCreatedCount++;
            } catch (Exception ex) {
                logger.info("Exception occurred"+ex.getMessage());
                continue;
            }
        }
            addRedirectAlertMessage(new SimpleMessage(consignmentCreatedCount + HKDeliveryConstants.CONSIGNMNT_CREATION_SUCCESS + duplicateAwbString));
        } else {
            addRedirectAlertMessage(new SimpleMessage(HKDeliveryConstants.CONSIGNMNT_CREATION_FAILURE));
        }
        return new RedirectResolution(HKDConsignmentAction.class);
    }

    // Getting comma seperated string for the duplicated
    public String getDuplicateAwbString(List<String> duplicatedAwbs) {
        StringBuffer strBuffr = new StringBuffer();
        for (String awbNumber : new HashSet<String>(duplicatedAwbs) ) {
            strBuffr.append(awbNumber);
            strBuffr.append(",");
        }
        return strBuffr.toString();
    }
    
    public Hub getHub() {
        return hub;
    }

    public void setHub(Hub hub) {
        this.hub = hub;
    }

    public List<String> getTrackingIdList() {
        return trackingIdList;
    }

    public void setTrackingIdList(List<String> trackingIdList) {
        this.trackingIdList = trackingIdList;
    }
}
