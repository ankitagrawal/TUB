package com.hk.admin.pact.service.courier;
import com.hk.domain.core.City;
import com.hk.domain.courier.CityCourierTAT;

/**
 * Created by IntelliJ IDEA.
 * User:User
 * Date: Jun 25, 2012
 * Time: 11:35:47 AM
 * To change this template use File | Settings | File Templates.
 */
public interface CityCourierTATService {

 public com.hk.domain.courier.CityCourierTAT getCityTatByCity(City city);

   public void saveCityCourierTAT(CityCourierTAT cityCourierTAT);
}
