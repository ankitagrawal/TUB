package com.hk.admin.impl.service.hkDelivery;

import com.hk.admin.pact.dao.hkDelivery.HubDao;
import com.hk.admin.pact.service.hkDelivery.HubService;
import com.hk.domain.hkDelivery.Hub;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HubServiceImpl implements HubService {

    @Autowired
    private HubDao hubDao;

    public List<Hub> getAllHubs() {
        return hubDao.getAllHubs();  
    }

    public Hub findHubByName(String hubName) {
        return hubDao.findHubByName(hubName);
    }
}
