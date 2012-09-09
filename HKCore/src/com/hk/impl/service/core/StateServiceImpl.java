package com.hk.impl.service.core;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.domain.core.State;
import com.hk.pact.dao.core.StateDao;
import com.hk.pact.service.core.StateService;

/**
 * Created by IntelliJ IDEA.
 * User:User
 * Date: Jun 25, 2012
 * Time: 11:13:13 AM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class StateServiceImpl implements StateService {

  @Autowired
  StateDao stateDao;

  public State getStateByName(String name){
    return stateDao.getStateByName(name);
  }

  public List<State> getAllStates(){
   return  stateDao.getAll(State.class);

  }

}
