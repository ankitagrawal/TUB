package com.hk.admin.impl.dao.courier;

import com.hk.admin.pact.dao.courier.AwbDao;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Courier;
import com.hk.domain.warehouse.Warehouse;
import com.hk.impl.dao.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings("unchecked")
@Repository
public class AwbDaoImpl extends BaseDaoImpl implements AwbDao{

    
    public List<Awb> getAvailableAwbForCourier(Courier courier, Warehouse warehouse) {
        String hqlQuery = " from Awb awb where awb.used = 0 and awb.courier = :courier and awb.warehouse = :warehouse order by id asc";
        return getSession().createQuery(hqlQuery)
                .setEntity("courier", courier).setEntity("warehouse", warehouse).setMaxResults(5).list();
    }

  public List<Awb> getAvailableAwbForCourier(Courier courier) {b
         String hqlQuery = " from Awb awb where awb.used = 0  order by id asc";
         return getSession().createQuery(hqlQuery)
                 .setEntity("courier", courier).list();
     }
  

    public Awb getByAWBNumber(Courier courier, String awbNumber) {
        String hqlQuery = " from Awb awb where awb.courier = :courier and awb.awbNumber = :awbNumber";
        return (Awb) getSession().createQuery(hqlQuery)
                .setEntity("courier", courier).setString("awbNumber", awbNumber).uniqueResult();
    }

    public List<Awb> getAvailableAwbForCourierByCod(Courier courier, Warehouse warehouse,boolean cod) {
        String hqlQuery = " from Awb awb where awb.used = 0 and awb.courier = :courier and awb.warehouse = :warehouse and awb.cod = :cod order by id asc";
        return getSession().createQuery(hqlQuery)
                .setEntity("courier", courier).setEntity("warehouse", warehouse).setMaxResults(5).list();
    }

}

