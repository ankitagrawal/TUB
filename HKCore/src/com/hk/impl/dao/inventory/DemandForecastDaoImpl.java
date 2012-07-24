package com.hk.impl.dao.inventory;

import com.hk.impl.dao.BaseDaoImpl;
import com.hk.domain.warehouse.DemandForecast;
import com.hk.pact.dao.inventory.DemandForecastDao;

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
public class DemandForecastDaoImpl extends BaseDaoImpl implements DemandForecastDao {

    public List<DemandForecast> findDemandforecastByDate (Date minDate){
            return (List<DemandForecast>) getSession().createQuery("from DemandForecast df where df.forecastDate >= :minDate order by df.forecastDate").setDate("minDate", minDate).list();
    }

    public boolean doesProductVariantExist(String variantId) {
        Long count = (Long) (getSession().createQuery("select count(p.id) from ProductVariant p where p.id = :id").setString("id", variantId).uniqueResult());
        return (count != null && count > 0);
    }
}
