package com.hk.web.action.admin.hkDelivery;

import com.hk.admin.pact.service.hkDelivery.HubService;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.user.User;
import com.hk.pact.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.core.Pincode;
import com.hk.admin.pact.dao.hkDelivery.HubDao;
import com.hk.pact.service.core.PincodeService;
import com.hk.constants.hkDelivery.HKDeliveryConstants;
import net.sourceforge.stripes.action.*;
import org.stripesstuff.plugin.security.Secure;

import java.util.ArrayList;
import java.util.List;

@Secure(hasAnyPermissions = { PermissionConstants.ADD_HK_DELIVERY_AGENT, PermissionConstants.ADD_HUB })
@Component
public class HKDHubAction extends BaseAction {

    private     List<Hub>         hubList             = new ArrayList<Hub>();
    private     Boolean           addHub;
    private     Boolean           editExistingHub;
    private     Long              hubId;
    private     Hub               hub;
    private     String            pincode;
    private     Pincode           pincodeObj          = null;
    private     User              agent;
    private     User              loggedOnUser;
    private     String            userEmailId;
    @Autowired
    private     HubDao            hubDao;
    @Autowired
    private     PincodeService    pincodeService;
    @Autowired
    private     HubService        hubService;
    @Autowired
    private     UserService       userService;

    @DefaultHandler
    public Resolution pre() {
        hubList = hubDao.getAllHubs();
        return new ForwardResolution("/pages/admin/hubList.jsp");
    }

    public Resolution addNewHub() {
        if (addHub) {
            try {
                pincodeObj = pincodeService.getByPincode(pincode);
                if (pincodeObj != null) {
                    hub.setPincode(pincodeObj);
                    hubDao.save(hub);
                    addRedirectAlertMessage(new SimpleMessage(HKDeliveryConstants.HUB_CREATION_SUCCESS));
                } else {
                    addRedirectAlertMessage(new SimpleMessage(HKDeliveryConstants.INVALID_PINCODE_MSG));
                    return new ForwardResolution("/pages/admin/addNewHub.jsp");
                }
            } catch (Exception ex) {
                addRedirectAlertMessage(new SimpleMessage(HKDeliveryConstants.HUB_CREATION_FAILURE));
                return new ForwardResolution("/pages/admin/addNewHub.jsp");
            }
            return new RedirectResolution(HKDHubAction.class);
        } else {
            return new ForwardResolution("/pages/admin/addNewHub.jsp");
        }
    }

    public Resolution editHub() {
        if (editExistingHub) {
            try {
                pincodeObj = pincodeService.getByPincode(pincode);
                if(pincodeObj != null) {
                hub.setPincode(pincodeObj);
                hubDao.save(hub);
                addRedirectAlertMessage(new SimpleMessage(HKDeliveryConstants.HUB_EDIT_SUCCESS));
                }else {
                   addRedirectAlertMessage(new SimpleMessage(HKDeliveryConstants.INVALID_PINCODE_MSG));
                }
            } catch (Exception ex) {
                addRedirectAlertMessage(new SimpleMessage(HKDeliveryConstants.HUB_EDIT_FAILURE));
            }

            return new ForwardResolution("/pages/admin/editHub.jsp");
        } else {
     //       hub = hubDao.get(Hub.class, hubId);
            pincode = hub.getPincode().getPincode();
            return new ForwardResolution("/pages/admin/editHub.jsp");
        }
    }

    public Resolution addUserToHub(){
        loggedOnUser = getUserService().getUserById(getPrincipal().getId());
        return new ForwardResolution("/pages/admin/addAgentToHub.jsp");
    }

    public Resolution searchAgent(){
        loggedOnUser = getUserService().getUserById(getPrincipal().getId());
        agent = userService.findByLogin(userEmailId);
        if(agent == null){
            addRedirectAlertMessage(new SimpleMessage("No agent found with given email id"));
        }
        return new ForwardResolution("/pages/admin/addAgentToHub.jsp");
    }

	public Resolution removeAgentFromHub(){
		if(hub != null & agent != null){
			hubService.removeAgentFromHub(hub,agent);
		}
		addRedirectAlertMessage(new SimpleMessage("Agent removed from hub !"));
		return new RedirectResolution(HKDHubAction.class, "addUserToHub");
	}

    public Resolution saveUserToHub(){
        loggedOnUser = getUserService().getUserById(getPrincipal().getId());
        boolean status = false;
        if(agent != null && hub!=null){
            status = hubService.addAgentToHub(hub, agent);
        }
        if(status){
            addRedirectAlertMessage(new SimpleMessage("Agent added to hub"));
        }
        else{
            addRedirectAlertMessage(new SimpleMessage("Unable to add agent. Agent might already be present in the hub."));
        }
        return new RedirectResolution(HKDHubAction.class, "addUserToHub");
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

    public User getAgent() {
        return agent;
    }

    public void setAgent(User agent) {
        this.agent = agent;
    }

    public User getLoggedOnUser() {
        return loggedOnUser;
    }

    public void setLoggedOnUser(User loggedOnUser) {
        this.loggedOnUser = loggedOnUser;
    }

    public String getUserEmailId() {
        return userEmailId;
    }

    public void setUserEmailId(String userEmailId) {
        this.userEmailId = userEmailId;
    }
}
