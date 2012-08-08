package com.hk.admin.pact.dao.hkDelivery;

import com.hk.pact.dao.BaseDao;
import com.hk.domain.courier.Shipment;
import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.hkDelivery.Consignment;
import com.hk.domain.hkDelivery.ConsignmentStatus;


public interface ConsignmentDao extends BaseDao {


    public Consignment createConsignment(Shipment shipment, Hub hub);

    public void saveConsignment(Consignment consignment);

    public ConsignmentStatus getConsignmentStatus(Long consignmentStatusId);
}
