package com.hk.impl.dao.inventory;

import com.hk.impl.dao.BaseDaoImpl;
import com.hk.domain.warehouse.DemandForcast;
import com.hk.domain.catalog.Supplier;
import com.hk.pact.dao.inventory.DemandForcastDao;

import java.util.List;
import java.util.Date;

import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Neha
 * Date: Jul 20, 2012
 * Time: 2:43:50 PM
 * To change this template use File | Settings | File Templates.
 */

@SuppressWarnings("unchecked")
@Repository
public class DemandForecastDaoImpl extends BaseDaoImpl implements DemandForcastDao {

    public List<DemandForcast> findDemandForcastByDate (Date minDate){
            return (List<DemandForcast>) getSession().createQuery("from DemandForcast df where df.forcastDate >= :minDate order by df.forcastDate").setDate("minDate", minDate).list();
    }
}
