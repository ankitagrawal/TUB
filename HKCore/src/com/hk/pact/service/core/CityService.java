package com.hk.pact.service.core;

import java.util.List;

import com.hk.domain.core.City;

/**
 * Created by IntelliJ IDEA.
 * User:User
 * Date: Jun 25, 2012
 * Time: 11:08:25 AM
 * To change this template use File | Settings | File Templates.
 */
public interface CityService {

  public List<City>  getAllCity();

   public City getCityByName(String name);

  public boolean isValidCity(String name);
}
