package com.hk.admin.pact.service.hkDelivery;

import com.hk.domain.courier.Awb;
import com.hk.domain.hkDelivery.Consignment;
import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.courier.Shipment;

import java.util.List;


public interface ConsignmentService {

    public Consignment createConsignment(Shipment shipment, Hub hub);

    public List<Awb> getAwbIds();

    public Consignment getConsignmentByAwbId(Awb awbId);

    public void updateConsignmentTracking(Long sourceHubId, Long destinationHubId, Long userId, Consignment consignment);

    public void updateConsignmentTracking(Long sourceHubId, Long destinationHubId, Long userId, List<Consignment> consignmentList);

}
