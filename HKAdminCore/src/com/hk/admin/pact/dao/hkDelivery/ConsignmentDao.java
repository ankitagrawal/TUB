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

    public Consignment getConsignmentByAwbNumber(String awbNumber);

    List<String> getDuplicateAwbNumbersinRunsheet(List<String> trackingIdList);

    List<String> getDuplicateAwbNumbersinConsignment(List<String> trackingIdList);
}
