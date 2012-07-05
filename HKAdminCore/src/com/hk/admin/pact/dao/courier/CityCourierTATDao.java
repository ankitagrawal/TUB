package com.hk.admin.pact.dao.courier;

import com.hk.domain.core.City;
import com.hk.domain.courier.CityCourierTAT;
import com.hk.pact.dao.BaseDao;

/**
 * Created by IntelliJ IDEA.
 * User:User
 * Date: Jun 21, 2012
 * Time: 4:43:50 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CityCourierTATDao extends BaseDao {
  public CityCourierTAT getCityTatByCity(City city);
}
