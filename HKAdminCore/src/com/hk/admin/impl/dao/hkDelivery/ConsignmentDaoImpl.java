package com.hk.admin.impl.dao.hkDelivery;

import com.hk.domain.courier.Awb;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.admin.pact.dao.hkDelivery.ConsignmentDao;
import com.hk.domain.hkDelivery.Consignment;
import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.hkDelivery.ConsignmentTracking;
import com.hk.domain.courier.Shipment;
import com.hk.constants.hkDelivery.EnumConsignmentStatus;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class ConsignmentDaoImpl extends BaseDaoImpl implements ConsignmentDao {

    @Override
    public Consignment createConsignment(Shipment shipment, Hub hub) {
        Consignment consignmentObj = new Consignment();
        consignmentObj.setHub(hub);
        consignmentObj.setConsignmentStatus(EnumConsignmentStatus.ShipmntRcvdAtHub.asConsignmentStatus());
        consignmentObj.setAwbNumber(shipment.getAwb());
        consignmentObj.setAmount(shipment.getShippingOrder().getAmount());
        consignmentObj.setCnnNumber(shipment.getShippingOrder().getGatewayOrderId());
        consignmentObj.setCreateDate(new Date());
        consignmentObj.setPaymentMode(shipment.getShippingOrder().getBaseOrder().getPayment().getPaymentMode().getName());
        consignmentObj = (Consignment)save(consignmentObj);
        return consignmentObj;
    }

    @Override
    public List<Awb> getAwbIds() {
        return (List<Awb>) getSession().createQuery(
                "select distinct cn.awbNumber from Consignment cn ").list();
    }

    @Override
    public Consignment getConsignmentByAwbId(Awb awb) {
        return (Consignment) getSession().createQuery("from Consignment cn where cn.awbNumber = :awb").setParameter("awb", awb).uniqueResult();
    }

    public void updateConsignmentTracking(Long sourceHubId, Long destinationHubId, Long userId, List<Consignment> consignmentList) {
        for (Consignment consignment : consignmentList) {
            ConsignmentTracking consignmntTracking = new ConsignmentTracking();
            consignmntTracking.setConsignmentId(consignment.getId());
            consignmntTracking.setCreateDate(new Date());
            consignmntTracking.setSourceHubId(sourceHubId);
            consignmntTracking.setDestinationId(destinationHubId);
            consignmntTracking.setUserId(userId);
            save(consignmntTracking);
        }
    }

    @Override
    public void updateConsignmentTracking(Long sourceHubId, Long destinationHubId, Long userId, Consignment consignment) {
        ConsignmentTracking consignmntTracking = new ConsignmentTracking();
        consignmntTracking.setConsignmentId(consignment.getId());
        consignmntTracking.setCreateDate(new Date());
        consignmntTracking.setSourceHubId(sourceHubId);
        consignmntTracking.setDestinationId(destinationHubId);
        consignmntTracking.setUserId(userId);
        save(consignmntTracking);
    }

}

