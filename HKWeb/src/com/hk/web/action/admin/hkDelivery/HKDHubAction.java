package com.hk.web.action.admin.hkDelivery;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.core.Pincode;
import com.hk.admin.pact.dao.hkDelivery.HubDao;
import com.hk.pact.service.core.PincodeService;
import com.hk.constants.hkDelivery.HKDeliveryConstants;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.SimpleMessage;

import java.util.ArrayList;
import java.util.List;

@Component
public class HKDHubAction extends BaseAction {

    private     List<Hub>         hubList             = new ArrayList<Hub>();
    private     Boolean           addHub;
    private     Boolean           editExistingHub;
    private     Long              hubId;
    private     Hub               hub;
    private     String            pincode;
    private     Pincode           pincodeObj          = null;
    @Autowired
    private     HubDao            hubDao;
    @Autowired
    private     PincodeService    pincodeService;

    @DefaultHandler
    public Resolution pre() {
        hubList = hubDao.getAll(Hub.class);
        return new ForwardResolution("/pages/admin/hubList.jsp");
    }

    public Resolution addNewHub() {
        if (addHub) {
            try{
            pincodeObj = pincodeService.getByPincode(pincode);
            hub.setPincode(pincodeObj);
            hubDao.save(hub);
            addRedirectAlertMessage(new SimpleMessage(HKDeliveryConstants.HUB_CREATION_SUCCESS));
            }catch (Exception ex){
              addRedirectAlertMessage(new SimpleMessage(HKDeliveryConstants.HUB_CREATION_FAILURE));
            }
            return new ForwardResolution("/pages/admin/addNewHub.jsp");
        } else {
            return new ForwardResolution("/pages/admin/addNewHub.jsp");
        }
    }

    public Resolution editHub() {
        if (editExistingHub) {
            try {
                pincodeObj = pincodeService.getByPincode(pincode);
                hub.setPincode(pincodeObj);
                hubDao.save(hub);
                addRedirectAlertMessage(new SimpleMessage(HKDeliveryConstants.HUB_EDIT_SUCCESS));
            } catch (Exception ex) {
                addRedirectAlertMessage(new SimpleMessage(HKDeliveryConstants.HUB_EDIT_FAILURE));
            }

            return new ForwardResolution("/pages/admin/editHub.jsp");
        } else {
            hub = hubDao.get(Hub.class, hubId);
            pincode = hub.getPincode().getPincode();
            return new ForwardResolution("/pages/admin/editHub.jsp");
        }
    }

    public List<Hub> getHubList() {
        return hubList;
    }

    public void setHubList(List<Hub> hubList) {
        this.hubList = hubList;
    }

    public HubDao getHubDao() {
        return hubDao;
    }

    public void setHubDao(HubDao hubDao) {
        this.hubDao = hubDao;
    }

    public Boolean isAddHub() {
        return addHub;
    }

    public void setAddHub(Boolean addHub) {
        this.addHub = addHub;
    }

    public Boolean isEditExistingHub() {
        return editExistingHub;
    }

    public void setEditExistingHub(Boolean editExistingHub) {
        this.editExistingHub = editExistingHub;
    }

    public Long getHubId() {
        return hubId;
    }

    public void setHubId(Long hubId) {
        this.hubId = hubId;
    }

    public Hub getHub() {
        return hub;
    }

    public void setHub(Hub hub) {
        this.hub = hub;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
}
