package com.hk.admin.pact.dao.marketing;

import java.util.List;

import com.hk.domain.marketing.AdNetworks;
import com.hk.pact.dao.BaseDao;

public interface AdNetworksDao extends BaseDao {

    public List<AdNetworks> listAdNetworks() ;
        

}
