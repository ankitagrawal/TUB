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

    public List<Awb> getAvailableAwbForCourierByWarehouseAndCod(Courier courier, Warehouse warehouse, Boolean isCod , AwbStatus awbStatus) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Awb.class);
        if (courier != null) {
            detachedCriteria.add(Restrictions.eq("courier", courier));
        }
        if (warehouse != null && StringUtils.isNotBlank(Long.toString(warehouse.getId()))) {
            detachedCriteria.add(Restrictions.eq("warehouse", warehouse));
        }
        if (isCod != null) {
            detachedCriteria.add(Restrictions.eq("cod", isCod));
        }
        if(awbStatus != null){
        detachedCriteria.add(Restrictions.eq("awbStatus",awbStatus));
        }
         detachedCriteria.addOrder(org.hibernate.criterion.Order.asc("id"));
        return (List<Awb>) findByCriteria(detachedCriteria);
    }


    public Awb findByCourierWarehouseCodAwbnumber(Courier courier, Warehouse warehouse, Boolean isCod, String awbNumber) {

        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Awb.class);
        detachedCriteria.add(Restrictions.eq("awbNumber", awbNumber));
        if (courier != null) {
            detachedCriteria.add(Restrictions.eq("courier", courier));
        }
        if (warehouse != null && StringUtils.isNotBlank(Long.toString(warehouse.getId()))) {
            detachedCriteria.add(Restrictions.eq("warehouse", warehouse));
        }
        if (isCod != null) {
            detachedCriteria.add(Restrictions.eq("cod", isCod));
        }
        List<Awb> awbList = findByCriteria(detachedCriteria);
        if (awbList != null && awbList.size() > 0) {
            return awbList.get(0);
        } else {
            return null;
        }

    }

    public List<Awb> getUsedAwb(String awbNumber) {
        AwbStatus awbStatus = EnumAwbStatus.Used.getAsAwbStatus();
        List<Awb> awbList = getAwbByAwbNumberAndStatus(awbNumber, awbStatus);
        return awbList;

    }


    public List<Awb> getAwbByAwbNumberAndStatus(String awbNumber, AwbStatus awbStatus) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Awb.class);
        detachedCriteria.add(Restrictions.eq("awbNumber", awbNumber));
        detachedCriteria.add(Restrictions.eq("awbStatus", awbStatus));
        return (List<Awb>) findByCriteria(detachedCriteria);
    }

}

