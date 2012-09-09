package com.hk.pact.dao.core;

import com.hk.domain.core.City;
import com.hk.pact.dao.BaseDao;

/**
 * Created by IntelliJ IDEA.
 * User:User
 * Date: Jun 25, 2012
 * Time: 10:47:56 AM
 * To change this template use File | Settings | File Templates.
 */
public interface CityDao extends BaseDao {

  public City getCityByName(String name);

  public boolean isValidCity(String name);

}
