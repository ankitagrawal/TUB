package com.hk.rest.pact.service;

import com.hk.domain.warehouse.DemandForcast;
import com.hk.pact.dao.BaseDao;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Neha
 * Date: Jul 16, 2012
 * Time: 1:37:22 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DemandForcastService {
    public void InsertInDB(List<String> input, List<DemandForcast> dfList);
}
