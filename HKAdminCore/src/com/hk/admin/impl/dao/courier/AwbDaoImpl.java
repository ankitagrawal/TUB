package com.hk.admin.impl.dao.courier;

import com.hk.admin.pact.dao.courier.AwbDao;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Courier;
import com.hk.domain.warehouse.Warehouse;
import com.hk.impl.dao.BaseDaoImpl;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings("unchecked")
@Repository
public class AwbDaoImpl extends BaseDaoImpl implements AwbDao {


  public Awb getByAWBNumber(Courier courier, String awbNumber) {
    String hqlQuery = " from Awb awb where awb.courier = :courier and awb.awbNumber = :awbNumber";
    return (Awb) getSession().createQuery(hqlQuery)
        .setEntity("courier", courier).setString("awbNumber", awbNumber).uniqueResult();
  }
//
//    public List<Awb> getAvailableAwbForCourierByWarehouseAndCod(Courier courier, Warehouse warehouse,boolean cod) {
//        String hqlQuery = " from Awb awb where awb.used = 0 and awb.courier = :courier and awb.warehouse = :warehouse and awb.cod = :cod order by id asc";
//        return getSession().createQuery(hqlQuery)
//                .setEntity("courier", courier).setEntity("warehouse", warehouse).setEntity("cod",cod).list();

  //    }

  public List<Awb> getAvailableAwbForCourierByWarehouseAndCod(Courier courier, Warehouse warehouse, Boolean isCod) {
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
    return(List<Awb>) findByCriteria(detachedCriteria);
  }
}





