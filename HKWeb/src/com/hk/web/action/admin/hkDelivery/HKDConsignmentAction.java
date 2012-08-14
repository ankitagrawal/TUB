package com.hk.web.action.admin.hkDelivery;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Courier;
import com.hk.domain.user.User;
import com.hk.admin.pact.service.hkDelivery.ConsignmentService;
import com.hk.admin.pact.service.hkDelivery.HubService;
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
    private              int                  noOfConsignmentsCreated  = 0;

    @Autowired
    private              ConsignmentService   consignmentService;
    @Autowired
    private              HubService           hubService;


    @DefaultHandler
    public Resolution pre(){
        return new ForwardResolution("/pages/admin/hkDeliveryConsignment.jsp");        
    }

    public Resolution markShipmentsReceived() {
        Set<Awb>     awbSet;
        List<Awb>    awbList;
        List<Awb>    duplicateAwbs;
        Hub          healthkartHub;
        String       duplicateAwbString = "";
        User         loggedOnUser       = null;
        Courier      hkDelivery         = EnumCourier.HK_Delivery.asCourier();


        if (trackingIdList != null && trackingIdList.size() > 0) {
            healthkartHub = hubService.findHubByName(HKDeliveryConstants.HEALTHKART_HUB);
            if (getPrincipal() != null) {
                loggedOnUser = getUserService().getUserById(getPrincipal().getId());
            }
            // getting AWB set for the entered trackingIdList.
            awbSet = consignmentService.getAWBSet(trackingIdList, hkDelivery);
            //Creating a list out of set(needed for consignment-duplication check).
            awbList = new ArrayList<Awb>(awbSet);
            //Checking if consignment is already created for the enterd awbNumber and fetching the awb for the same .
            duplicateAwbs = consignmentService.getDuplicateAwbs(awbList);
            if (duplicateAwbs.size() > 0) {
                // removing duplicated awbs from the list.
                awbList.removeAll(duplicateAwbs);
                // creating a string for user-display.
                duplicateAwbString = getDuplicateAwbString(duplicateAwbs);
                duplicateAwbString = "Consignments already created for following trackingIds:" + duplicateAwbString;
                // convertig list to set to delete/remove duplicate elements from the list.
                awbSet = new HashSet<Awb>(awbList);

            }
            // Creating consignments.
            noOfConsignmentsCreated = consignmentService.createConsignments(awbSet, healthkartHub, hub ,loggedOnUser.getId());
            addRedirectAlertMessage(new SimpleMessage(noOfConsignmentsCreated + HKDeliveryConstants.CONSIGNMNT_CREATION_SUCCESS + duplicateAwbString));
        } else {
            addRedirectAlertMessage(new SimpleMessage(HKDeliveryConstants.CONSIGNMNT_CREATION_FAILURE));
        }
        return new RedirectResolution(HKDConsignmentAction.class);
    }

    // Getting comma seperated string for the duplicated
    public String getDuplicateAwbString(List<Awb> duplicatedAwbs) {
        StringBuffer strBuffr = new StringBuffer();
        for (Awb awb :duplicatedAwbs ) {
            strBuffr.append(awb.getAwbNumber());
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
