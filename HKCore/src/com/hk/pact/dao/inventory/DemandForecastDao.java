package com.hk.pact.dao.inventory;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.hk.domain.warehouse.DemandForecast;

/**
 * Created by IntelliJ IDEA.
 * User: Neha
 * Date: Jul 20, 2012
 * Time: 2:46:00 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DemandForecastDao {

    public List<DemandForecast> findDemandforecastByDate (Date minDate);
    
    @SuppressWarnings("unchecked")
    public void saveOrUpdate(Collection entities) throws DataAccessException;
}
