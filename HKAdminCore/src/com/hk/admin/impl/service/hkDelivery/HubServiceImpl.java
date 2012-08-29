package com.hk.admin.impl.service.hkDelivery;

import com.hk.admin.pact.dao.hkDelivery.HubDao;
import com.hk.admin.pact.service.hkDelivery.HubService;
import com.hk.domain.hkDelivery.Hub;

import java.util.List;

import com.hk.domain.user.User;
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

    @Override
    public Hub getHubForUser(User user) {
        return hubDao.getHubForUser(user);
    }

    @Override
    public List<User> getAgentsForHub(Hub hub){
        return hubDao.getAgentsForHub(hub);
    }
}
