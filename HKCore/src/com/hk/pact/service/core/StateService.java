package com.hk.pact.service.core;

import com.hk.domain.core.State;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User:User
 * Date: Jun 25, 2012
 * Time: 11:12:32 AM
 * To change this template use File | Settings | File Templates.
 */
public interface StateService {
  public List<State> getAllStates();

 public State getStateByName(String name);

}
