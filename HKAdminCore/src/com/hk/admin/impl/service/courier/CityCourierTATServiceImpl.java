package com.hk.admin.impl.service.courier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.admin.pact.dao.courier.CityCourierTATDao;
import com.hk.admin.pact.service.courier.CityCourierTATService;
import com.hk.domain.core.City;
import com.hk.domain.courier.CityCourierTAT;

/**
 * Created by IntelliJ IDEA.
 * User:User
 * Date: Jun 25, 2012
 * Time: 11:36:33 AM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class CityCourierTATServiceImpl  implements CityCourierTATService {
  @Autowired
  private  CityCourierTATDao cityCourierTATDao;

   public CityCourierTAT getCityTatByCity(City city){
      return cityCourierTATDao.getCityTatByCity(city);

   }

    public void saveCityCourierTAT(CityCourierTAT cityCourierTAT) {
        cityCourierTATDao.save(cityCourierTAT);
    }
}
