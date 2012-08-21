package com.hk.admin.impl.dao.courier;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.hk.admin.pact.dao.courier.CityCourierTATDao;
import com.hk.domain.core.City;
import com.hk.domain.courier.CityCourierTAT;
import com.hk.impl.dao.BaseDaoImpl;

/**
 * Created by IntelliJ IDEA.
 * User:User
 * Date: Jun 21, 2012
 * Time: 4:45:01 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class CityCourierTATDaoImpl extends BaseDaoImpl implements CityCourierTATDao {

    @SuppressWarnings("unchecked")
    public CityCourierTAT getCityTatByCity(City city) {
        DetachedCriteria cityCourierTATCriteria = DetachedCriteria.forClass(CityCourierTAT.class);
        cityCourierTATCriteria.add(Restrictions.eq("city", city));
        List<CityCourierTAT> cityCourierTAList = (List<CityCourierTAT>) findByCriteria(cityCourierTATCriteria);
        if (cityCourierTAList != null && cityCourierTAList.size() > 0) {
            return cityCourierTAList.get(0);
        }
        return null;
    }


}
