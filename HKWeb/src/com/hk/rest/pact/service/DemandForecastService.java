package com.hk.rest.pact.service;

import java.util.Date;
import java.util.List;

import com.hk.domain.warehouse.DemandForecast;

/**
 * Created by IntelliJ IDEA.
 * User: Neha
 * Date: Jul 16, 2012
 * Time: 1:37:22 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DemandForecastService {
    public void saveOrUpdateForecastInDB (List<DemandForecast> input, Date minforecastDate);
}
