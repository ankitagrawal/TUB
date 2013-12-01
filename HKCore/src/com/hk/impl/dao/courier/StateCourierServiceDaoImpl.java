package com.hk.impl.dao.courier;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.hk.domain.core.State;
import com.hk.domain.courier.StateCourierService;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.courier.StateCourierServiceDao;

/**
 * Created by IntelliJ IDEA.
 * User: Seema
 * Date: Jun 5, 2012
 * Time: 11:59:27 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class StateCourierServiceDaoImpl extends BaseDaoImpl implements StateCourierServiceDao {
   public List<StateCourierService> getAllStateCourierServiceByState(State state){
        DetachedCriteria stateCriteria= DetachedCriteria.forClass(StateCourierService.class);
      stateCriteria.add(Restrictions.eq("state",state));
      List<StateCourierService> stateCourierServiceList=(List<StateCourierService> ) findByCriteria(stateCriteria);
     return stateCourierServiceList;
   }

}
