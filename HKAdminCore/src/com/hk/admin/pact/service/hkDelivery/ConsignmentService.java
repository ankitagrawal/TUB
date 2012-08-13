package com.hk.admin.pact.service.hkDelivery;

import com.hk.domain.hkDelivery.Consignment;
import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.courier.Shipment;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Courier;

import java.util.List;
import java.util.Set;


public interface ConsignmentService {

    public Consignment createConsignment(Shipment shipment, Hub hub);

    public List<Long> getAwbIds();

    public Consignment getConsignmentByAwbId(Long awbId);

    public void updateConsignmentTracking(Long sourceHubId, Long destinationHubId, Long userId, Consignment consignment);

    public void updateConsignmentTracking(Long sourceHubId, Long destinationHubId, Long userId, List<Consignment> consignmentList);

    public int createConsignments(Set<Awb> awbSet, Hub sourceHub, Hub destinationHub, Long userId);

    public List<Awb> getDuplicateAwbs(List<Awb> awbList);

    public Set<Awb> getAWBSet(List<String> awbNumberList, Courier hkDelivery);

}
