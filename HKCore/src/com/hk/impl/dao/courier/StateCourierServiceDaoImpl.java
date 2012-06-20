package com.hk.impl.dao.courier;

import com.hk.domain.core.Pincode;
import com.hk.domain.courier.StateCourierService;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.courier.PincodeDao;
import com.hk.pact.dao.courier.StateCourierServiceDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
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
  @Autowired
   PincodeDao pincodeDao;

   public List<StateCourierService> getAllStateCourierServiceByState(String stateName){
   List< Pincode> pincodeList= pincodeDao.getPincodeByState(stateName);
 Pincode pincode=pincodeList.get(0);
        DetachedCriteria stateCriteria= DetachedCriteria.forClass(StateCourierService.class);
     stateCriteria.add(Restrictions.eq("state",pincode));
      List<StateCourierService> stateCourierServiceList=(List<StateCourierService> ) findByCriteria(stateCriteria);
     return stateCourierServiceList;
   }

}
