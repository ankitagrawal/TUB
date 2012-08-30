package com.hk.admin.impl.dao.hkDelivery;

import com.hk.admin.pact.dao.hkDelivery.HubDao;
import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.hkDelivery.HubHasUser;
import com.hk.domain.user.User;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.constants.hkDelivery.HKDeliveryConstants;

import java.util.List;
import java.util.ArrayList;

import org.springframework.stereotype.Repository;

@Repository
public class HubDaoImpl extends BaseDaoImpl implements HubDao {
    
    public List<Hub> getAllHubs() {
        List<Hub> validHubs = new ArrayList<Hub>();
        for (Hub hubObj : getAll(Hub.class)) {
            if (hubObj.getName().equals(HKDeliveryConstants.HEALTHKART_HUB) || hubObj.getName().equals(HKDeliveryConstants.DELIVERY_HUB)) {
                continue;
            }
            validHubs.add(hubObj);
        }
        return validHubs;  
    }

    @Override
    public Hub findHubByName(String hubName) {
        return (Hub) getSession().createQuery("from Hub hb where hb.name = :hubName").setString("hubName",hubName).uniqueResult();
    }

    @Override
    public Hub getHubForUser(User user) {
        return (Hub) getSession().createQuery("Select hu.hub from HubHasUser hu where hu.user = :user").setParameter("user", user).uniqueResult();
    }

    @Override
    public List<User> getAgentsForHub(Hub hub){
        List<User> agents = new ArrayList<User>();
        for(HubHasUser hubUserRow : getAll(HubHasUser.class)){
            if(hubUserRow.getHub().getId().equals(hub.getId())){
                agents.add(hubUserRow.getUser());
            }
        }
        return agents;
    }
}
