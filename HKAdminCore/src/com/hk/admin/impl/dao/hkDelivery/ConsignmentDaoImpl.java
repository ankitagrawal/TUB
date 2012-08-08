package com.hk.admin.impl.dao.hkDelivery;

import com.hk.impl.dao.BaseDaoImpl;
import com.hk.admin.pact.dao.hkDelivery.ConsignmentDao;
import com.hk.domain.hkDelivery.Consignment;
import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.hkDelivery.ConsignmentStatus;
import com.hk.domain.courier.Shipment;
import com.hk.constants.hkDelivery.EnumConsignmentStatus;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class ConsignmentDaoImpl extends BaseDaoImpl implements ConsignmentDao {

    @Override
    public Consignment createConsignment(Shipment shipment, Hub hub) {
        Consignment consignmentObj = new Consignment();
        consignmentObj.setHub(hub);
        consignmentObj.setConsignmentStatus(getConsignmentStatus(EnumConsignmentStatus.ShipmntRcvdAtHub.getId()));
        consignmentObj.setAwbId(shipment.getAwb().getId());
        consignmentObj.setAmount(shipment.getShippingOrder().getAmount());
        consignmentObj.setCnnNumber(shipment.getShippingOrder().getGatewayOrderId());
        consignmentObj.setCreateDate(new Date());
        consignmentObj.setPaymentMode(shipment.getShippingOrder().getBaseOrder().getPayment().getPaymentMode().getName());
        saveConsignment(consignmentObj);
        return consignmentObj;
    }

    @Override
    public void saveConsignment(Consignment consignment) {
        save(consignment);
    }

    @Override
    public ConsignmentStatus getConsignmentStatus(Long consignmentStatusId) {
        return get(ConsignmentStatus.class,consignmentStatusId);
    }
}
