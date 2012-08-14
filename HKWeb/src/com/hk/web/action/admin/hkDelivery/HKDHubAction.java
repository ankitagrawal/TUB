package com.hk.web.action.admin.hkDelivery;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.hkDelivery.Hub;
import com.hk.admin.pact.dao.hkDelivery.HubDao;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.ForwardResolution;

import java.util.ArrayList;
import java.util.List;

@Component
public class HKDHubAction extends BaseAction {

    private List<Hub> hubList = new ArrayList<Hub>();
    @Autowired
    private HubDao    hubDao;

    @DefaultHandler
    public Resolution pre() {
        hubList = hubDao.getAll(Hub.class);
        return new ForwardResolution("/pages/admin/addEditHub.jsp");
    }

    public Resolution addNewHub() {
        return new ForwardResolution("/pages/admin/hkDeliveryConsignment.jsp");
    }

    public Resolution editHub() {
        return new ForwardResolution("/pages/admin/hkDeliveryConsignment.jsp");
    }

    public List<Hub> getHubList() {
        return hubList;
    }

    public void setHubList(List<Hub> hubList) {
        this.hubList = hubList;
    }
}
