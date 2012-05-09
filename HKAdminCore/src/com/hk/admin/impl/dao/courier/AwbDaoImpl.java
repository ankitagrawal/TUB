package com.hk.admin.impl.dao.courier;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hk.admin.pact.dao.courier.AwbDao;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Courier;
import com.hk.domain.warehouse.Warehouse;
import com.hk.impl.dao.BaseDaoImpl;

@SuppressWarnings("unchecked")
@Repository
public class AwbDaoImpl extends BaseDaoImpl implements AwbDao{

    
    public List<Awb> getAvailableAwbForCourier(Courier courier, Warehouse warehouse) {
        String hqlQuery = " from Awb awb where awb.used = 0 and awb.courier = :courier and awb.warehouse = :warehouse order by id asc";
        return getSession().createQuery(hqlQuery)
                .setEntity("courier", courier).setEntity("warehouse", warehouse).setMaxResults(5).list();
    }

    public Awb getByAWBNumber(Courier courier, String awbNumber) {
        String hqlQuery = " from Awb awb where awb.courier = :courier and awb.awbNumber = :awbNumber";
        return (Awb) getSession().createQuery(hqlQuery)
                .setEntity("courier", courier).setString("awbNumber", awbNumber).uniqueResult();
    }
}

