package com.hk.db.seed.core;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hk.constants.core.EnumState;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.core.State;

/**
 * Created by IntelliJ IDEA.
 * User:User
 * Date: Jun 20, 2012
 * Time: 4:14:48 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class StateSeedData extends BaseSeedData {

  public void insert(java.lang.String name, java.lang.Long id) {
    State state = new State();
    state.setName(name);
    state.setId(id);
    save(state);
  }


  public void invokeInsert() {
    List<Long> stateList = new ArrayList<Long>();

    for (EnumState enumState : EnumState.values()) {

      if (stateList.contains(enumState.getId()))
        throw new RuntimeException("Duplicate key " + enumState.getId());
      else
        stateList.add(enumState.getId());

      insert(enumState.getName(), enumState.getId());
    }
  }
}
