package com.hk.admin.impl.dao.hkDelivery;

import com.hk.admin.pact.dao.hkDelivery.HubDao;
import com.hk.domain.hkDelivery.Hub;
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
            if (hubObj.getId().equals(HKDeliveryConstants.HEALTHKART_HUB_ID) || hubObj.getId().equals(HKDeliveryConstants.DELIVERY_HUB_ID)) {
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
}
