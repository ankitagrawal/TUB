package com.hk.admin.pact.service.hkDelivery;

import com.hk.domain.courier.Awb;
import com.hk.domain.hkDelivery.Consignment;
import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.hkDelivery.ConsignmentTracking;
import com.hk.domain.courier.Shipment;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Courier;
import com.hk.domain.user.User;
import com.hk.domain.core.PaymentMode;
import com.hk.domain.order.ShippingOrder;

import java.util.List;
import java.util.Set;


public interface ConsignmentService {

    public Consignment createConsignment(String awbNumber,String cnnNumber ,double amount, String paymentMode ,Hub hub);

    public List<ConsignmentTracking> createConsignmentTracking(Hub sourceHub, Hub destinationHub, User user, List<Consignment> consignment);

    public void saveConsignments(List<Consignment> consignmentList);

    public void saveConsignmentTracking(List<ConsignmentTracking> consignmentTrackingList);

    public Consignment getConsignmentByAwbNumber(String awbNumber);

    public List<String> getDuplicateAwbNumbersinRunsheet(List<String> trackingIdList);

    public String  getConsignmentPaymentMode(ShippingOrder shipppingOrder);

    public List<String> getDuplicateAwbNumbersinConsignment(List<String> trackingIdList);

    public List<Consignment> getConsignmentListByAwbNumbers(List<String> awbNumbers);

}
