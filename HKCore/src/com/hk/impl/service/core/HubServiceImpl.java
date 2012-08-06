package com.hk.impl.service.core;

import com.hk.pact.service.core.HubService;
import com.hk.pact.dao.hub.HubDao;
import com.hk.domain.hkDelivery.Hub;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HubServiceImpl implements HubService {

    @Autowired
    private HubDao hubDao;

    @Override
    public List<Hub> getAllHubs() {
        return hubDao.getAllHubs();  
    }
}
