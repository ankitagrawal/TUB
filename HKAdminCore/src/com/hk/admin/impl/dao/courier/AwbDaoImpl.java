package com.hk.admin.impl.dao.courier;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hk.admin.pact.dao.courier.AwbDao;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.AwbStatus;
import com.hk.domain.courier.Courier;
import com.hk.domain.warehouse.Warehouse;
import com.hk.impl.dao.BaseDaoImpl;

@SuppressWarnings("unchecked")
@Repository
public class AwbDaoImpl extends BaseDaoImpl implements AwbDao {

    public List<Awb> getAvailableAwbForCourierByWarehouseCodStatus(Courier courier, String awbNumber, Warehouse warehouse, Boolean isCod, AwbStatus awbStatus) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Awb.class);
        if (courier != null) {
            detachedCriteria.add(Restrictions.eq("courier", courier));
        }
        if (awbNumber != null && StringUtils.isNotBlank(awbNumber)) {
            detachedCriteria.add(Restrictions.eq("awbNumber", awbNumber));
        }
        if (warehouse != null && StringUtils.isNotBlank(Long.toString(warehouse.getId()))) {
            detachedCriteria.add(Restrictions.eq("warehouse", warehouse));
        }
        if (isCod != null) {
            detachedCriteria.add(Restrictions.eq("cod", isCod));
        }
        if (awbStatus != null) {
            detachedCriteria.add(Restrictions.eq("awbStatus", awbStatus));
        }

        detachedCriteria.addOrder(org.hibernate.criterion.Order.asc("id"));
        return (List<Awb>) findByCriteria(detachedCriteria);
    }

     public Awb findByCourierAwbNumber(Courier courier ,String awbNumber){
      List<Awb> awbList= getAvailableAwbForCourierByWarehouseCodStatus(courier,awbNumber,null,null,null);
       if(awbList != null && awbList.size() > 0){
           return awbList.get(0);
       }
         return null;

     }

    public List<Awb> getAlreadyPresentAwb(Courier courier, List<String> awbNumberList) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Awb.class);

        if (awbNumberList != null && awbNumberList.size() > 0 && courier != null && courier.getId() != null) {
            detachedCriteria.add(Restrictions.in("awbNumber", awbNumberList));
            detachedCriteria.add(Restrictions.eq("courier", courier));
            return (List<Awb>) findByCriteria(detachedCriteria);
        }
        return null;

    }

	@Transactional
	public Object save(Awb awb, Integer newStatus) {
		if (awb.getId() != null) {
			Integer originalStatus =  awb.getAwbStatus().getId().intValue();
			String query = "UPDATE awb SET awb_status_id=? WHERE id=? AND awb_status_id=?";
			Object[] param = {newStatus, awb.getId(),originalStatus};
			int rowsUpdate = executeNativeSql(query, param);
			return rowsUpdate;
		} else {
			return save(awb);
		}
	}

}

