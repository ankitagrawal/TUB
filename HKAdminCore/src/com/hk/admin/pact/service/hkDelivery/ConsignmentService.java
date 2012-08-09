package com.hk.admin.pact.service.hkDelivery;

import com.hk.domain.hkDelivery.Consignment;
import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.courier.Shipment;

import java.util.List;


public interface ConsignmentService {

    public Consignment createConsignment(Shipment shipment, Hub hub);

    public List<Long> getAwbIds();

    public Consignment getConsignmentByAwbId(Long awbId);

}
