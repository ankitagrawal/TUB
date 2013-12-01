package com.hk.impl.dao.core;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.hk.domain.core.State;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.core.StateDao;

/**
 * Created by IntelliJ IDEA.
 * User:User
 * Date: Jun 25, 2012
 * Time: 10:57:47 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class StateDaoImpl extends BaseDaoImpl implements StateDao {
  
  public State getStateByName(String name){
     name=name.trim();
     DetachedCriteria stateCriteria= DetachedCriteria.forClass(State.class);
     stateCriteria.add(Restrictions.like("name",name));
      List<State> stateList=  findByCriteria(stateCriteria);
     if(stateList != null && stateList.size() >0){
      return stateList.get(0);
     }
     else
       return null;
   }

}
