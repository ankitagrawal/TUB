package com.hk.admin.pact.dao.hkDelivery;

import com.hk.domain.courier.Awb;
import com.hk.pact.dao.BaseDao;
import com.hk.domain.courier.Shipment;
import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.hkDelivery.Consignment;
import com.hk.domain.user.User;

import java.util.List;
import java.util.Set;


public interface ConsignmentDao extends BaseDao {


    public Consignment createConsignment(String awbNumber,String cnnNumber ,double amount, String paymentMode ,Hub hub);

    public List<String> getAwbIds();

    public Consignment getConsignmentByAwbNumber(String awbNumber);

    void updateConsignmentTracking(Hub sourceHubI, Hub destinationHub, User user, Consignment consignment);

    void updateConsignmentTracking(Hub sourceHub, Hub destinationHub, User user, Set<Consignment> consignments);
}
