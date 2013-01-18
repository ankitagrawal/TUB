package com.hk.admin.impl.dao.courier;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.admin.pact.service.courier.CourierGroupService;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierGroup;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.BaseDao;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 5/25/12
 * Time: 2:45 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class CourierGroupServiceImpl extends BaseDaoImpl implements CourierGroupService {
	@Autowired
	BaseDao baseDao;

    public CourierGroup getByName(String name){
        return (CourierGroup) getSession().createQuery("select c from CourierGroup c where c.name = :name")
                .setParameter("name", name)
                .uniqueResult();
    }

    public Set<Courier> getCommonCouriers(CourierGroup courierGroup, List<Courier> courierList){
        Set<Courier> commonCouriers = new HashSet<Courier>(); 	   
        for (Courier courier : courierList) {
	        if(courier.getCourierGroup() != null){
            if(courier.getCourierGroup().equals(courierGroup)){
                commonCouriers.add(courier);
            }
	        } 
        }

        return commonCouriers;
    }

    public CourierGroup getCourierGroup(Courier courier) {
      if(courier != null){
	      return courier.getCourierGroup();
      }
	      return null;
    }

	public List<CourierGroup> getAllCourierGroup(){
	return getAll(CourierGroup.class);

	}

	
	public CourierGroup save(CourierGroup courierGroup){
		return (CourierGroup)getBaseDao().save(courierGroup);
	}


	public BaseDao getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}

	public void saveOrUpdate(CourierGroup courierGroup) {
		getBaseDao().saveOrUpdate(courierGroup);
	}
}
