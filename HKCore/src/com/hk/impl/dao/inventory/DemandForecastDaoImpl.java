package com.hk.impl.dao.inventory;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.hk.domain.warehouse.DemandForecast;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.inventory.DemandForecastDao;

/**
 * Created by IntelliJ IDEA. User: Neha Date: Jul 20, 2012 Time: 2:43:50 PM To change this template use File | Settings |
 * File Templates.
 */

@SuppressWarnings("unchecked")
@Repository
public class DemandForecastDaoImpl extends BaseDaoImpl implements DemandForecastDao {

    public List<DemandForecast> findDemandforecastByDate(Date minDate) {
        return (List<DemandForecast>) findByNamedParams("from DemandForecast df where df.forecastDate >= :minDate order by df.forecastDate", new String[] { "minDate" },
                new Object[] { minDate });
    }

    public void saveOrUpdate(Collection entities) throws DataAccessException {
        super.saveOrUpdate(entities);
    }

}
