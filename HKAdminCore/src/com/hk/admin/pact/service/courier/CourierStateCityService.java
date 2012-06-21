package com.hk.admin.pact.service.courier;

import com.hk.domain.core.City;

/**
 * Created by IntelliJ IDEA.
 * User:User
 * Date: Jun 21, 2012
 * Time: 11:11:35 AM
 * To change this template use File | Settings | File Templates.
 */
public interface CourierStateCityService {

  public City getCityByName(String name);
  public boolean isValidCity(String name);

}
