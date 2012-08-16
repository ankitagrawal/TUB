package com.hk.admin.pact.service.hkDelivery;

import com.hk.domain.courier.Awb;
import com.hk.domain.hkDelivery.Consignment;
import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.courier.Shipment;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Courier;
import com.hk.domain.user.User;

import java.util.List;
import java.util.Set;


public interface ConsignmentService {

    public Consignment createConsignment(String awbNumber,String cnnNumber ,double amount, String paymentMode ,Hub hub);

    public List<String> getAwbNumbersInConsignment();

    public Consignment getConsignmentByAwbNumber(String awbNumber);

    public void updateConsignmentTracking(Hub sourceHub, Hub destinationHub, User user, Consignment consignment);

    public void updateConsignmentTracking(Hub sourceHub, Hub destinationHub, User user, Set<Consignment> consignmentList);

    public List<String> getDuplicateAwbs(List<String> awbNumbers ,List<String> existingAwbNumbers);

    public List<String> getAllAwbNumbersWithRunsheet();

}
