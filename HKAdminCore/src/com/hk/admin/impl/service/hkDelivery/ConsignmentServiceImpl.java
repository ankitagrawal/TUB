package com.hk.admin.impl.service.hkDelivery;

import com.hk.domain.courier.Awb;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.hk.admin.pact.service.hkDelivery.ConsignmentService;
import com.hk.admin.pact.dao.hkDelivery.ConsignmentDao;
import com.hk.domain.hkDelivery.Consignment;
import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.courier.Shipment;

import java.util.List;

@Service
public class ConsignmentServiceImpl implements ConsignmentService {

    @Autowired
    private ConsignmentDao consignmentDao;

    @Override
    public Consignment createConsignment(Shipment shipment, Hub hub) {
        return consignmentDao.createConsignment(shipment, hub);
    }

    @Override
    public List<Awb> getAwbIds() {
        return consignmentDao.getAwbIds();
    }

    @Override
    public Consignment getConsignmentByAwbId(Awb awbId) {
        return consignmentDao.getConsignmentByAwbId(awbId);
    }

    @Override
    public void updateConsignmentTracking(Long sourceHubId, Long destinationHubId, Long userId, Consignment consignment) {
        consignmentDao.updateConsignmentTracking(sourceHubId, destinationHubId, userId, consignment);
    }

    @Override
    public void updateConsignmentTracking(Long sourceHubId, Long destinationHubId, Long userId, List<Consignment> consignmentList) {
        consignmentDao.updateConsignmentTracking(sourceHubId, destinationHubId, userId, consignmentList);
    }
}
