package com.hk.admin.impl.dao.courier;

import com.hk.admin.pact.dao.courier.AwbDao;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.AwbStatus;
import com.hk.domain.warehouse.Warehouse;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.constants.courier.EnumAwbStatus;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

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
}

