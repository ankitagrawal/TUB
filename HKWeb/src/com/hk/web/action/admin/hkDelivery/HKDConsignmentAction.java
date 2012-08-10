package com.hk.web.action.admin.hkDelivery;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Courier;
import com.hk.admin.pact.service.courier.AwbService;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.admin.pact.service.hkDelivery.ConsignmentService;
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
    private              int                  noOfConsignmentsCreated  = 0;

    @Autowired
    private              AwbService           awbService;
    @Autowired
    private              ConsignmentService   consignmentService;
    @Autowired
    private              ShipmentService      shipmentService;


    @DefaultHandler
    public Resolution pre(){
        return new ForwardResolution("/pages/admin/hkDeliveryConsignment.jsp");        
    }

    public Resolution markShipmentsReceived(){
        Set<Awb>   awbSet ;
        Courier    hkDelivery  = EnumCourier.HK_Delivery.asCourier();
       if(trackingIdList != null && trackingIdList.size() > 0){
        awbSet = getAWBSet(trackingIdList,hkDelivery);
        logger.info(awbSet.toString());
        noOfConsignmentsCreated = createConsignments(awbSet);
        addRedirectAlertMessage(new SimpleMessage(noOfConsignmentsCreated +HKDeliveryConstants.CONSIGNMNT_CREATION_SUCCESS));
       } else {
           addRedirectAlertMessage(new SimpleMessage(HKDeliveryConstants.CONSIGNMNT_CREATION_FAILURE));
       }
        return new RedirectResolution(HKDConsignmentAction.class);
    }

    //todo data duplicacy
    // Method to check data duplicacy
    private boolean checkAwbDuplicacy(Set<Awb> awbSet){
        List<Long> consignmentAwbId = consignmentService.getAwbIds();
        
        return false;
    }

    // Method to create consignments.It wud interact with service layer.
    private int createConsignments(Set<Awb> awbSet) {
        int consignmentCount = 0;
            for (Awb awbObj : awbSet) {
                try{
                consignmentService.createConsignment(shipmentService.findByAwb(awbObj), hub);
                consignmentCount++;
                } catch (Exception ex){
                    logger.info(HKDeliveryConstants.EXCEPTION+awbObj.getAwbNumber());
                    continue;
                }
            }
        return consignmentCount;
    }


    // Method to get AWB set from trackingId list.
    private Set<Awb> getAWBSet(List<String> awbNumberList, Courier hkDelivery){
        Set<Awb>   awbSet      = new HashSet<Awb>();
        for(String awbNumbr:awbNumberList){
            awbSet.add(awbService.findByCourierAwbNumber(hkDelivery,awbNumbr));
        }
        return awbSet;
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
