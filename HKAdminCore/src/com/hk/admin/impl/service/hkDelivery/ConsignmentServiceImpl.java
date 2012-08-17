package com.hk.admin.impl.service.hkDelivery;

import com.hk.domain.courier.Awb;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.hk.admin.pact.service.hkDelivery.ConsignmentService;
import com.hk.admin.pact.service.shippingOrder.ShipmentService;
import com.hk.admin.pact.service.courier.AwbService;
import com.hk.admin.pact.dao.hkDelivery.ConsignmentDao;
import com.hk.domain.hkDelivery.Consignment;
import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.hkDelivery.ConsignmentTracking;
import com.hk.domain.courier.Shipment;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Courier;
import com.hk.domain.user.User;
import com.hk.domain.core.PaymentMode;
import com.hk.domain.order.ShippingOrder;
import com.hk.constants.hkDelivery.HKDeliveryConstants;
import com.hk.constants.hkDelivery.EnumConsignmentStatus;
import com.hk.constants.payment.EnumPaymentMode;

import java.util.*;

@Service
public class ConsignmentServiceImpl implements ConsignmentService {

    @Autowired
    private ConsignmentDao consignmentDao;

    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private AwbService awbService;

    @Override
    public Consignment createConsignment(String awbNumber,String cnnNumber ,double amount, String paymentMode ,Hub hub){
        Consignment consignmentObj = new Consignment();
        consignmentObj.setHub(hub);
        consignmentObj.setConsignmentStatus(EnumConsignmentStatus.ShipmntRcvdAtHub.asConsignmentStatus());
        consignmentObj.setAwbNumber(awbNumber);
        consignmentObj.setAmount(amount);
        consignmentObj.setCnnNumber(cnnNumber);
        consignmentObj.setCreateDate(new Date());
        consignmentObj.setPaymentMode(paymentMode);
        return consignmentObj;
    }

    @Override
    public List<ConsignmentTracking> createConsignmentTracking(Hub sourceHub, Hub destinationHub, User user, List<Consignment> consignments) {
       List<ConsignmentTracking> consignmntTrackingList = new ArrayList<ConsignmentTracking>();
        for(Consignment consignment: consignments)
        {
        ConsignmentTracking consignmntTracking = new ConsignmentTracking();
        consignmntTracking.setConsignment(consignment);
        consignmntTracking.setCreateDate(new Date());
        consignmntTracking.setSourceHub(sourceHub);
        consignmntTracking.setDestinationHub(destinationHub);
        consignmntTracking.setUser(user);
        consignmntTrackingList.add(consignmntTracking);
        }
        return consignmntTrackingList;
    }

    @Override
    public void saveConsignments(List<Consignment> consignmentList) {
        consignmentDao.saveOrUpdate(consignmentList);
    }

    @Override
    public void saveConsignmentTracking(List<ConsignmentTracking> consignmentTrackingList) {
        consignmentDao.saveOrUpdate(consignmentTrackingList);
    }

    @Override
    public Consignment getConsignmentByAwbNumber(String awbNumber) {
        return consignmentDao.getConsignmentByAwbNumber(awbNumber);
    }

    @Override
    public List<String> getDuplicateAwbNumbersinRunsheet(List<String> trackingIdList) {
        return consignmentDao.getDuplicateAwbNumbersinRunsheet(trackingIdList);
    }

    @Override
    public String getConsignmentPaymentMode(ShippingOrder shippingOrder) {
        String paymentModeString = null;
        if(shippingOrder.isCOD()) {
            paymentModeString = HKDeliveryConstants.COD;
        } else {
            paymentModeString = HKDeliveryConstants.PREPAID;
        }
        return paymentModeString;
    }

    @Override
    public List<String> getDuplicateAwbNumbersinConsignment(List<String> trackingIdList) {
        return consignmentDao.getDuplicateAwbNumbersinConsignment(trackingIdList);
    }

    @Override
    public List<Consignment> getConsignmentListByAwbNumbers(List<String> awbNumbers) {
        return consignmentDao.getConsignmentListByAwbNumbers(awbNumbers);
    }
}
