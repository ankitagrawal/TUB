package com.hk.admin.impl.service.hkDelivery;

import com.akube.framework.dao.Page;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.hk.admin.pact.service.hkDelivery.ConsignmentService;
import com.hk.admin.pact.service.shippingOrder.ShipmentService;
import com.hk.admin.pact.service.courier.AwbService;
import com.hk.admin.pact.dao.hkDelivery.ConsignmentDao;
import com.hk.domain.hkDelivery.*;
import com.hk.domain.user.User;
import com.hk.domain.order.ShippingOrder;
import com.hk.constants.hkDelivery.HKDeliveryConstants;
import com.hk.constants.hkDelivery.EnumConsignmentStatus;

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
        consignmentObj.setConsignmentStatus(consignmentDao.get(ConsignmentStatus.class, EnumConsignmentStatus.ShipmentReceivedAtHub.getId()));
        consignmentObj.setAwbNumber(awbNumber);
        consignmentObj.setAmount(amount);
        consignmentObj.setCnnNumber(cnnNumber);
        consignmentObj.setCreateDate(new Date());
        consignmentObj.setPaymentMode(paymentMode);
        return consignmentObj;
    }

    @Override
    public List<ConsignmentTracking> createConsignmentTracking(Hub sourceHub, Hub destinationHub, User user, List<Consignment> consignments ,ConsignmentLifecycleStatus consignmentLifecycleStatus) {
       List<ConsignmentTracking> consignmntTrackingList = new ArrayList<ConsignmentTracking>();
        for(Consignment consignment: consignments)
        {
        ConsignmentTracking consignmntTracking = new ConsignmentTracking();
        consignmntTracking.setConsignment(consignment);
        consignmntTracking.setCreateDate(new Date());
        consignmntTracking.setSourceHub(sourceHub);
        consignmntTracking.setDestinationHub(destinationHub);
        consignmntTracking.setUser(user);
            if (consignmentLifecycleStatus == null) {
       
            } else {
                consignmntTracking.setConsignmentLifecycleStatus(consignmentLifecycleStatus);
            }
        consignmntTrackingList.add(consignmntTracking);
        }
        return consignmntTrackingList;
    }

    @Override
    public ConsignmentTracking createConsignmentTracking(Hub sourceHub, Hub destinationHub, User user, Consignment consignment, ConsignmentLifecycleStatus consignmentLifecycleStatus) {
        ConsignmentTracking consignmntTracking = new ConsignmentTracking();
        consignmntTracking.setConsignment(consignment);
        consignmntTracking.setCreateDate(new Date());
        consignmntTracking.setSourceHub(sourceHub);
        consignmntTracking.setDestinationHub(destinationHub);
        consignmntTracking.setUser(user);
        consignmntTracking.setConsignmentLifecycleStatus(consignmentLifecycleStatus);
        return consignmntTracking;
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

    @Override
    public List<ShippingOrder> getShippingOrderFromConsignments(List<Consignment> consignments) {
        List<String> cnnNumberList = new ArrayList<String>();
        for(Consignment consignment:consignments){
            cnnNumberList.add(consignment.getCnnNumber());
        }
        return consignmentDao.getShippingOrderFromConsignments(cnnNumberList);
    }

    @Override
    public Map<Object,Object> getRunsheetCODParams(Set<Consignment> consignments) {
        Map<Object,Object> runsheetCODParams = new HashMap<Object,Object>();
        double totalCODAmount = 0.0 ;
        int totalCODPackets  = 0;

        for(Consignment consignment : consignments){
            if(consignment.getPaymentMode().equals(HKDeliveryConstants.COD)){
                totalCODAmount = totalCODAmount + consignment.getAmount();
                ++ totalCODPackets;
            }
        }
        runsheetCODParams.put(HKDeliveryConstants.TOTAL_COD_AMT ,totalCODAmount);
        runsheetCODParams.put(HKDeliveryConstants.TOTAL_COD_PKTS ,totalCODPackets);
        return runsheetCODParams;
    }

    @Override
    public List<ConsignmentTracking> getConsignmentTracking(Consignment consignment) {
        return consignmentDao.getConsignmentTracking(consignment);
    }

    @Override
    public Page searchConsignment(Consignment consignment,  String awbNumber, Date startDate, Date endDate, ConsignmentStatus consignmentStatus, Hub hub, Runsheet runsheet, Boolean reconciled, int pageNo, int perPage) {
        return consignmentDao.searchConsignment(consignment, awbNumber, startDate, endDate, consignmentStatus, hub, runsheet, reconciled, pageNo, perPage);
    }

    @Override
    public HkdeliveryPaymentReconciliation  createPaymentReconciliationForConsignmentList(List<Consignment> consignmentListForPaymentReconciliation, User user){
        HkdeliveryPaymentReconciliation hkdeliveryPaymentReconciliation = new HkdeliveryPaymentReconciliation();
        Double amount = 0.0;
        for(Consignment consignment : consignmentListForPaymentReconciliation){
            if(consignment.getPaymentMode().equals(HKDeliveryConstants.COD)){
                amount+=consignment.getAmount();
            }
        }
        Set<Consignment> reconciledConsignments = new HashSet<Consignment>(consignmentListForPaymentReconciliation);
        hkdeliveryPaymentReconciliation.setExpectedAmount(amount);
        hkdeliveryPaymentReconciliation.setUser(user);
        hkdeliveryPaymentReconciliation.setCreateDate(new Date());
        hkdeliveryPaymentReconciliation.setUpdateDate(new Date());
        hkdeliveryPaymentReconciliation.setConsignments(reconciledConsignments);
        return hkdeliveryPaymentReconciliation;
    }

    @Override
    public HkdeliveryPaymentReconciliation saveHkdeliveryPaymentReconciliation(HkdeliveryPaymentReconciliation hkdeliveryPaymentReconciliation){
        hkdeliveryPaymentReconciliation.setUpdateDate(new Date());
        consignmentDao.saveOrUpdate(hkdeliveryPaymentReconciliation);
        return hkdeliveryPaymentReconciliation;
    }
}
