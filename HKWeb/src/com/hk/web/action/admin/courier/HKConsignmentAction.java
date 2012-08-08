package com.hk.web.action.admin.courier;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Courier;
import com.hk.admin.pact.dao.courier.AwbDao;
import com.hk.admin.pact.service.courier.AwbService;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.admin.pact.service.hkDelivery.ConsignmentService;
import com.hk.admin.pact.service.shippingOrder.ShipmentService;
import com.hk.constants.courier.EnumCourier;
import net.sourceforge.stripes.action.*;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.ArrayList;

@Component
public class HKConsignmentAction extends BaseAction{

    private              Hub                  hub;
    private              List<String>         trackingIdList           = new ArrayList<String>();
    private              int                  noOfConsignmentsCreated  = 0;

    @Autowired
    private              AwbService           awbService;
    @Autowired
    private              ConsignmentService   consignmentService;
    @Autowired
    private              CourierService       courierService;
    @Autowired
    private              ShipmentService      shipmentService;


    @DefaultHandler
    public Resolution pre(){
        return new ForwardResolution("/pages/admin/hkDeliveryConsignment.jsp");        
    }

    public Resolution markShipmentsReceived(){
        List<Awb>  awbList     = new ArrayList<Awb>();
        Courier    hkDelivery  = courierService.getCourierById(EnumCourier.HK_Delivery.getId());
        awbList = getAWBlist(trackingIdList,hkDelivery);
        noOfConsignmentsCreated = createConsignments(awbList);
        addRedirectAlertMessage(new SimpleMessage(noOfConsignmentsCreated +" Consignments created successfully."));
        return new RedirectResolution(HKConsignmentAction.class);
    }

    // Method to create consignments.It wud interact with service layer.
    private int createConsignments(List<Awb> awbList) {
        int consignmentCount = 0;
        try {
            for (Awb awbObj : awbList) {
                consignmentService.createConsignment(shipmentService.findByAwb(awbObj), hub);
                consignmentCount++;
            }
        } catch (Exception ex) {
              ex.getMessage();
            ex.printStackTrace();
        }
        return consignmentCount;
    }


    // Method to get AWB list from trackingId list.
    private List<Awb> getAWBlist(List<String> awbNumberList, Courier hkDelivery){
        List<Awb> awbList=new ArrayList<Awb>();
        for(String awbNumbr:awbNumberList){
            awbList.add(awbService.findByCourierAwbNumber(hkDelivery,awbNumbr));
        }
        return awbList;
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
