package com.hk.admin.pact.dao.hkDelivery;

import com.hk.domain.courier.Awb;
import com.hk.pact.dao.BaseDao;
import com.hk.domain.courier.Shipment;
import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.hkDelivery.Consignment;

import java.util.List;


public interface ConsignmentDao extends BaseDao {


    public Consignment createConsignment(Shipment shipment, Hub hub);

    public List<Awb> getAwbIds();

    public Consignment getConsignmentByAwbId(Awb awb);

    void updateConsignmentTracking(Long sourceHubId, Long destinationHubId, Long userId, Consignment consignment);

    void updateConsignmentTracking(Long sourceHubId, Long destinationHubId, Long userId, List<Consignment> consignmentList);
}
