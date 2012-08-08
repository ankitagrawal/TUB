package com.hk.admin.impl.dao.hkDelivery;

import com.hk.admin.pact.dao.hkDelivery.HubDao;
import com.hk.domain.hkDelivery.Hub;
import com.hk.impl.dao.BaseDaoImpl;

import java.util.List;
import java.util.ArrayList;

import org.springframework.stereotype.Repository;

@Repository
public class HubDaoImpl extends BaseDaoImpl implements HubDao {
    
    public List<Hub> getAllHubs() {
        List<Hub> validHubs = new ArrayList<Hub>();
        for (Hub hubObj : getAll(Hub.class)) {
            if (hubObj.getId().equals(100l)) {
                continue;
            }
            validHubs.add(hubObj);
        }
        return validHubs;  
    }
}
