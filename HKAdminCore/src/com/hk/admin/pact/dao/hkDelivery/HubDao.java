package com.hk.admin.pact.dao.hkDelivery;

import com.hk.domain.hkDelivery.Hub;
import com.hk.pact.dao.BaseDao;

import java.util.List;

public interface HubDao extends BaseDao{

    public List<Hub> getAllHubs();
    
}
