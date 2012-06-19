package com.hk.impl.dao.courier;

import com.hk.domain.courier.StateCourierService;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.courier.StateCourierServiceDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Seema
 * Date: Jun 5, 2012
 * Time: 11:59:27 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class StateCourierServiceDaoImpl extends BaseDaoImpl implements StateCourierServiceDao {
   public List<StateCourierService> getAllStateCourierServiceByState(String stateName){
     stateName=stateName.trim();
    DetachedCriteria stateCriteria= DetachedCriteria.forClass(StateCourierServiceDao.class);
     stateCriteria.add(Restrictions.eq("state",stateName));
      List<StateCourierService> stateCourierServiceList=  findByCriteria(stateCriteria);
     return stateCourierServiceList;
   }

}
