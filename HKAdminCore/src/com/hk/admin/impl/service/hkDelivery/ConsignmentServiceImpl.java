package com.hk.admin.impl.service.hkDelivery;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.hk.admin.pact.service.hkDelivery.ConsignmentService;
import com.hk.admin.pact.service.shippingOrder.ShipmentService;
import com.hk.admin.pact.service.courier.AwbService;
import com.hk.admin.pact.dao.hkDelivery.ConsignmentDao;
import com.hk.domain.hkDelivery.Consignment;
import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.courier.Shipment;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Courier;
import com.hk.constants.hkDelivery.HKDeliveryConstants;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;

@Service
public class ConsignmentServiceImpl implements ConsignmentService {

    @Autowired
    private ConsignmentDao consignmentDao;

    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private AwbService awbService;

    @Override
    public Consignment createConsignment(Shipment shipment, Hub hub) {
        return consignmentDao.createConsignment(shipment, hub);
    }

    @Override
    public List<Long> getAwbIds() {
        return consignmentDao.getAwbIds();
    }

    @Override
    public Consignment getConsignmentByAwbId(Long awbId) {
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

    @Override
    public int createConsignments(Set<Awb> awbSet, Hub sourceHub, Hub destinationHub, Long userId) {
        int consignmentCount = 0;
        Consignment consignment = new Consignment();
        for (Awb awbObj : awbSet) {
            try {
                // Creating consignment object.
                consignment = createConsignment(shipmentService.findByAwb(awbObj), destinationHub);
                // Making an entry in consignment-tracking for the created consignment.
                updateConsignmentTracking(sourceHub.getId(), destinationHub.getId(), userId, consignment);
                consignmentCount++;
            } catch (Exception ex) {
                continue;
            }
        }
        return consignmentCount;

    }

    @Override
    public List<Awb> getDuplicateAwbs(List<Awb> awbList) {
        List<Long> consignmentAwbIds = getAwbIds();
        List<Awb> duplicatedAwbs = new ArrayList<Awb>();
        for (Awb awb : awbList) {
            for (Long awbNumber : consignmentAwbIds) {
                if (awbNumber.equals(awb.getId())) {
                    duplicatedAwbs.add(awb);
                }
            }
        }
        return duplicatedAwbs;
    }

    public Set<Awb> getAWBSet(List<String> awbNumberList, Courier hkDelivery) {
        Set<Awb> awbSet = new HashSet<Awb>();
        for (String awbNumbr : awbNumberList) {
            awbSet.add(awbService.findByCourierAwbNumber(hkDelivery, awbNumbr));
        }
        return awbSet;
    }
}
