package com.hk.rest.pact.service;

import com.hk.domain.warehouse.DemandForecast;

import java.util.List;
import java.util.Date;

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
