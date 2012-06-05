package com.hk.impl.dao.courier;

import com.hk.domain.courier.StateCourierService;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.courier.StateCourierServiceDao;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Seema
 * Date: Jun 5, 2012
 * Time: 11:59:27 AM
 * To change this template use File | Settings | File Templates.
 */
public class StateCourierServiceDaoImpl extends BaseDaoImpl implements StateCourierServiceDao {

   public List<StateCourierService> getAllStateCourierServiceByState(String stateName){
     stateName=stateName.toUpperCase();
     Criteria stateCriteria=getSession().createCriteria(StateCourierServiceDao.class);
     List<StateCourierService> StateCourierServiceList= stateCriteria.add(Restrictions.like("state",stateName)).list();
     return StateCourierServiceList;




   }





}
