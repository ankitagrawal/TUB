package com.hk.impl.dao.core;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.hk.domain.core.City;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.core.CityDao;

/**
 * Created by IntelliJ IDEA.
 * User:User
 * Date: Jun 25, 2012
 * Time: 10:50:28 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class CityDaoImpl  extends BaseDaoImpl implements CityDao {

  public City getCityByName(String name){
     name=name.trim();
     DetachedCriteria cityCriteria= DetachedCriteria.forClass(City.class);
     cityCriteria.add(Restrictions.like("name",name));
      List<City> cityList=findByCriteria(cityCriteria);
     if(cityList != null && cityList.size() >0){
      return cityList.get(0);
     }
     else
       return null;
   }
   public boolean isValidCity(String name){
     name=name.trim();
   City city=  getCityByName( name);
     if(city != null){
       return true;
     }
    else
       return false;
   }

}
