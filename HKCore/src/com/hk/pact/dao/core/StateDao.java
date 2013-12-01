package com.hk.pact.dao.core;

import com.hk.domain.core.State;
import com.hk.pact.dao.BaseDao;

/**
 * Created by IntelliJ IDEA.
 * User:User
 * Date: Jun 25, 2012
 * Time: 10:52:06 AM
 * To change this template use File | Settings | File Templates.
 */
public interface StateDao extends BaseDao {
   public State getStateByName(String name);

}
