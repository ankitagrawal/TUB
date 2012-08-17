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
import com.hk.domain.courier.Shipment;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Courier;
import com.hk.domain.user.User;
import com.hk.domain.core.PaymentMode;
import com.hk.constants.hkDelivery.HKDeliveryConstants;
import com.hk.constants.payment.EnumPaymentMode;

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
    public Consignment createConsignment(String awbNumber,String cnnNumber ,double amount, String paymentMode ,Hub hub){
        return consignmentDao.createConsignment(awbNumber,cnnNumber,amount,paymentMode,hub);
    }

    @Override
    public List<String> getAwbNumbersInConsignment() {
        return consignmentDao.getAwbNumbersInConsignment();
    }

    @Override
    public Consignment getConsignmentByAwbNumber(String awbNumber) {
        return consignmentDao.getConsignmentByAwbNumber(awbNumber);
    }
    
    @Override
    public void updateConsignmentTracking(Hub sourceHub, Hub destinationHub, User user, Consignment consignment) {
        consignmentDao.updateConsignmentTracking(sourceHub, destinationHub, user, consignment);
    }

    @Override
    public void updateConsignmentTracking(Hub sourceHub, Hub destinationHub, User user, Set<Consignment> consignments) {
        consignmentDao.updateConsignmentTracking(sourceHub, destinationHub, user, consignments);
    }

    @Override
    public List<String> getDuplicateAwbs(List<String> awbNumbers ,List<String> existingAwbNumbers) {
        List<String> duplicatedAwbNumbers = new ArrayList<String>();
        for (String existingAwbNum : existingAwbNumbers) {
            for (String awbNumber :  awbNumbers) {
                if (awbNumber.equals(existingAwbNum)) {
                    duplicatedAwbNumbers.add(awbNumber);
                }
            }
        }
        return duplicatedAwbNumbers;
    }

    @Override
    public List<String> getAllAwbNumbersWithRunsheet() {
        return consignmentDao.getAllAwbNumbersWithRunsheet();
    }

    @Override
    public String getConsignmentPaymentMode(PaymentMode paymentMode) {
        String paymentModeString = null;
        if(paymentMode.getId().equals(EnumPaymentMode.COD.getId())){
            paymentModeString = HKDeliveryConstants.COD;
        } else {
            paymentModeString = HKDeliveryConstants.PREPAID;
        }
        return paymentModeString;
    }

    @Override
    public List<String> getDuplicateAwbNumbers(List<String> trackingIdList) {
        return consignmentDao.getDuplicateAwbNumbers(trackingIdList);
    }
}
