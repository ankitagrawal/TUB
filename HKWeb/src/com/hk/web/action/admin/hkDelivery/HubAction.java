package com.hk.web.action.admin.hkDelivery;

import org.springframework.stereotype.Component;
import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.hkDelivery.Hub;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.ForwardResolution;

import java.util.ArrayList;
import java.util.List;

@Component
public class HubAction extends BaseAction {

    private List<Hub> hubList = new ArrayList<Hub>();

    @DefaultHandler
    public Resolution pre() {
        hubList = 
        return new ForwardResolution("/pages/admin/hkDeliveryConsignment.jsp");
    }

    public Resolution addNewHub() {
        return new ForwardResolution("/pages/admin/hkDeliveryConsignment.jsp");
    }

    public Resolution editHub() {
        return new ForwardResolution("/pages/admin/hkDeliveryConsignment.jsp");
    }
}
