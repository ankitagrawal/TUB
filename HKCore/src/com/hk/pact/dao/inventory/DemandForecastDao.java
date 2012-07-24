package com.hk.pact.dao.inventory;

import com.hk.domain.warehouse.DemandForecast;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Neha
 * Date: Jul 20, 2012
 * Time: 2:46:00 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DemandForecastDao {

    public List<DemandForecast> findDemandforecastByDate (Date minDate);

    public boolean doesProductVariantExist(String variantId);
}
