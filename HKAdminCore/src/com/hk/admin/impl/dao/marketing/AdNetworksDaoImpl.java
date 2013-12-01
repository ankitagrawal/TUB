package com.hk.admin.impl.dao.marketing;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.hk.admin.pact.dao.marketing.AdNetworksDao;
import com.hk.constants.marketing.EnumAdNetworks;
import com.hk.domain.marketing.AdNetworks;
import com.hk.impl.dao.BaseDaoImpl;

@Repository
public class AdNetworksDaoImpl extends BaseDaoImpl implements AdNetworksDao{

    @SuppressWarnings("unchecked")
    public List<AdNetworks> listAdNetworks() {
        List<Long> adNetworksIdList = new ArrayList<Long>();

        adNetworksIdList.add(EnumAdNetworks.Google.getId());
        adNetworksIdList.add(EnumAdNetworks.Facebook.getId());
        adNetworksIdList.add(EnumAdNetworks.Vizury.getId());

        DetachedCriteria criteria = DetachedCriteria.forClass(AdNetworks.class);
        criteria.add(Restrictions.in("id", adNetworksIdList));

        return findByCriteria(criteria);
    }

}
