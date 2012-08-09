package com.hk.admin.impl.service.hkDelivery;

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
        return consignmentDao.createConsignment(shipment,hub);
    }

    @Override
    public List<Long> getAwbIds() {
        return consignmentDao.getAwbIds();
    }

    @Override
     public Consignment getConsignmentByAwbId(Long awbId) {
        return consignmentDao.getConsignmentByAwbId(awbId);
    }
}
