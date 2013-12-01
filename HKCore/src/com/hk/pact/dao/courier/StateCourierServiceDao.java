package com.hk.pact.dao.courier;

import java.util.List;

import com.hk.domain.core.State;
import com.hk.domain.courier.StateCourierService;
import com.hk.pact.dao.BaseDao;

/**
 * Created by IntelliJ IDEA.
 * User: Seema
 * Date: Jun 5, 2012
 * Time: 11:56:37 AM
 * To change this template use File | Settings | File Templates.
 */
public interface StateCourierServiceDao  extends BaseDao {
 public List<StateCourierService> getAllStateCourierServiceByState(State state);
}
