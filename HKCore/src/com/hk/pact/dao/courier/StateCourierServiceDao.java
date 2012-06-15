package com.hk.pact.dao.courier;

import com.hk.domain.courier.StateCourierService;
import com.hk.pact.dao.BaseDao;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Seema
 * Date: Jun 5, 2012
 * Time: 11:56:37 AM
 * To change this template use File | Settings | File Templates.
 */
public interface StateCourierServiceDao  extends BaseDao {
 public List<StateCourierService> getAllStateCourierServiceByState(String stateName);
}
