package com.hk.impl.service.core;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.domain.core.City;
import com.hk.pact.dao.core.CityDao;
import com.hk.pact.service.core.CityService;

/**
 * Created by IntelliJ IDEA.
 * User:User
 * Date: Jun 25, 2012
 * Time: 11:10:03 AM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class CityServiceImpl implements CityService {
  @Autowired
  CityDao cityDao;

   public City getCityByName(String name){
   return  cityDao.getCityByName(name);
   }

  public boolean isValidCity(String name){
    return  cityDao.isValidCity(name);
  }

  public List<City> getAllCity(){
    return cityDao.getAll(City.class);
  }
}
