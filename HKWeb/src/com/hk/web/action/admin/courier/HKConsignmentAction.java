package com.hk.web.action.admin.courier;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Courier;
import com.hk.admin.pact.dao.courier.AwbDao;
import com.hk.admin.pact.service.courier.AwbService;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.DefaultHandler;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.ArrayList;

@Component
public class HKConsignmentAction extends BaseAction{

    private  Hub            hub;
    private  List<String>   trackingIdList = new ArrayList<String>();

    @Autowired
    private AwbService awbService;

    @DefaultHandler
    public Resolution pre(){
        return new ForwardResolution("/pages/admin/hkDeliveryConsignment.jsp");        
    }

    public Resolution markShipmentsReceived(){
        return new ForwardResolution("/pages/admin/hkDeliveryWorksheet.jsp");
    }


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
