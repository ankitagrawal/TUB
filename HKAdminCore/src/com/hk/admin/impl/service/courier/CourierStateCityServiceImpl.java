package com.hk.admin.impl.service.courier;

import com.hk.admin.pact.service.courier.CourierStateCityService;
import com.hk.domain.core.City;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.BaseDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User:User
 * Date: Jun 21, 2012
 * Time: 11:12:04 AM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class CourierStateCityServiceImpl extends BaseDaoImpl implements CourierStateCityService {
     @Autowired
     BaseDao baseDao;
   public City getCityByName(String name){
     name=name.trim();
     DetachedCriteria cityCriteria= DetachedCriteria.forClass(City.class);
     cityCriteria.add(Restrictions.like("name",name));
      List<City> cityList=  findByCriteria(cityCriteria);
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
