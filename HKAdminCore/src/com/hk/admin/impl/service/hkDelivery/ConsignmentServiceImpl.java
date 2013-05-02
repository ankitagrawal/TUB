package com.hk.admin.impl.service.hkDelivery;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hk.admin.pact.service.hkDelivery.HubService;
import com.hk.constants.hkDelivery.EnumConsignmentLifecycleStatus;
import com.hk.util.ShipmentServiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.akube.framework.dao.Page;
import com.hk.admin.dto.ConsignmentDto;
import com.hk.admin.pact.dao.hkDelivery.ConsignmentDao;
import com.hk.admin.pact.service.hkDelivery.ConsignmentService;
import com.hk.admin.pact.service.hkDelivery.RunSheetService;
import com.hk.constants.hkDelivery.EnumConsignmentStatus;
import com.hk.constants.hkDelivery.HKDeliveryConstants;
import com.hk.domain.hkDelivery.Consignment;
import com.hk.domain.hkDelivery.ConsignmentLifecycleStatus;
import com.hk.domain.hkDelivery.ConsignmentStatus;
import com.hk.domain.hkDelivery.ConsignmentTracking;
import com.hk.domain.hkDelivery.HkdeliveryPaymentReconciliation;
import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.hkDelivery.Runsheet;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.user.User;
import com.hk.pact.service.UserService;

@Service
public class ConsignmentServiceImpl implements ConsignmentService {

    @Autowired
    private ConsignmentDao consignmentDao;

    /*
     * @Autowired private ShipmentService shipmentService;
     */

    /*
     * @Autowired private AwbService awbService;
     */

    @Autowired
    private RunSheetService runsheetService;

    @Autowired
    UserService userService;

    @Autowired
    HubService hubService;

    @Override
    public Consignment createConsignment(String awbNumber, String cnnNumber, double amount, String paymentMode, String address, Hub hub) {
        Consignment consignmentObj = new Consignment();
        consignmentObj.setHub(hub);
        consignmentObj.setConsignmentStatus(consignmentDao.get(ConsignmentStatus.class, EnumConsignmentStatus.ShipmentReceivedAtHub.getId()));
        consignmentObj.setAwbNumber(awbNumber);
        consignmentObj.setAmount(amount);
        consignmentObj.setCnnNumber(cnnNumber);
        consignmentObj.setCreateDate(new Date());
        consignmentObj.setPaymentMode(paymentMode);
        consignmentObj.setAddress(address);
        return consignmentObj;
    }

    @Override
    public List<ConsignmentTracking> createConsignmentTracking(Hub sourceHub, Hub destinationHub, User user, List<Consignment> consignments,
                                                               ConsignmentLifecycleStatus consignmentLifecycleStatus, Runsheet runsheet) {
        List<ConsignmentTracking> consignmntTrackingList = new ArrayList<ConsignmentTracking>();
        for (Consignment consignment : consignments) {
            ConsignmentTracking consignmntTracking = new ConsignmentTracking();
            consignmntTracking.setConsignment(consignment);
            consignmntTracking.setCreateDate(new Date());
            consignmntTracking.setSourceHub(sourceHub);
            consignmntTracking.setDestinationHub(destinationHub);
            consignmntTracking.setUser(user);
            consignmntTracking.setRunsheet(runsheet);
            if (consignmentLifecycleStatus == null) {

            } else {
                consignmntTracking.setConsignmentLifecycleStatus(consignmentLifecycleStatus);
            }
            consignmntTrackingList.add(consignmntTracking);
        }
        return consignmntTrackingList;
    }

    @Override
    public ConsignmentTracking createConsignmentTracking(Hub sourceHub, Hub destinationHub, User user, Consignment consignment,
                                                         ConsignmentLifecycleStatus consignmentLifecycleStatus, String consignmentTrackingRemark, Runsheet runsheet) {
        ConsignmentTracking consignmntTracking = new ConsignmentTracking();
        consignmntTracking.setConsignment(consignment);
        consignmntTracking.setCreateDate(new Date());
        consignmntTracking.setSourceHub(sourceHub);
        consignmntTracking.setDestinationHub(destinationHub);
        consignmntTracking.setUser(user);
        consignmntTracking.setConsignmentLifecycleStatus(consignmentLifecycleStatus);
        consignmntTracking.setRemarks(consignmentTrackingRemark);
        consignmntTracking.setRunsheet(runsheet);
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
        if (ShipmentServiceMapper.isCod(shippingOrder.getShipment().getShipmentServiceType())) {
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
        for (Consignment consignment : consignments) {
            cnnNumberList.add(consignment.getCnnNumber());
        }
        return consignmentDao.getShippingOrderFromConsignments(cnnNumberList);
    }

    @Override
    public Map<Object, Object> getRunsheetCODParams(Set<Consignment> consignments) {
        Map<Object, Object> runsheetCODParams = new HashMap<Object, Object>();
        double totalCODAmount = 0.0;
        int totalCODPackets = 0;

        for (Consignment consignment : consignments) {
            if (consignment.getPaymentMode().equals(HKDeliveryConstants.COD)) {
                totalCODAmount = totalCODAmount + consignment.getAmount();
                ++totalCODPackets;
            }
        }
        runsheetCODParams.put(HKDeliveryConstants.TOTAL_COD_AMT, totalCODAmount);
        runsheetCODParams.put(HKDeliveryConstants.TOTAL_COD_PKTS, totalCODPackets);
        return runsheetCODParams;
    }

    @Override
    public List<ConsignmentTracking> getConsignmentTracking(Consignment consignment) {
        return consignmentDao.getConsignmentTracking(consignment);
    }

    @Override
    public Page searchConsignment(Consignment consignment, String awbNumber, Date startDate, Date endDate, ConsignmentStatus consignmentStatus, Hub hub, Runsheet runsheet,
                                  Boolean reconciled, int pageNo, int perPage) {
        return consignmentDao.searchConsignment(consignment, awbNumber, startDate, endDate, consignmentStatus, hub, runsheet, reconciled, pageNo, perPage);
    }

    @Override
    public HkdeliveryPaymentReconciliation createPaymentReconciliationForConsignmentList(List<Consignment> consignmentListForPaymentReconciliation, User user) {
        HkdeliveryPaymentReconciliation hkdeliveryPaymentReconciliation = new HkdeliveryPaymentReconciliation();
        Double amount = 0.0;
        for (Consignment consignment : consignmentListForPaymentReconciliation) {
            if (consignment.getPaymentMode().equals(HKDeliveryConstants.COD)) {
                amount += consignment.getAmount();
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
    public HkdeliveryPaymentReconciliation saveHkdeliveryPaymentReconciliation(HkdeliveryPaymentReconciliation hkdeliveryPaymentReconciliation, User loggedOnUser) {
        hkdeliveryPaymentReconciliation.setUpdateDate(new Date());
        hkdeliveryPaymentReconciliation.setUser(loggedOnUser);
        consignmentDao.saveOrUpdate(hkdeliveryPaymentReconciliation);
        return hkdeliveryPaymentReconciliation;
    }

    @Override
    public boolean isConsignmentValidForRunsheet(Consignment consignment) {
        if (consignment.getConsignmentStatus().getId().equals(EnumConsignmentStatus.ShipmentDamaged.getId())
                || consignment.getConsignmentStatus().getId().equals(EnumConsignmentStatus.ShipmentDelivered.getId())
                || consignment.getConsignmentStatus().getId().equals(EnumConsignmentStatus.ShipmentLost.getId())
                || consignment.getConsignmentStatus().getId().equals(EnumConsignmentStatus.ShipmentRTH.getId())
                || consignment.getConsignmentStatus().getId().equals(EnumConsignmentStatus.ShipmentOutForDelivery.getId())) {
            return false;
        }
        return true;
    }

    @Override
    public List<Consignment> getConsignmentsFromShippingOrderList(List<ShippingOrder> shippingOrderList) {
        List<String> awbNumbers = new ArrayList<String>();
        for (ShippingOrder shippingOrder : shippingOrderList) {
            awbNumbers.add(shippingOrder.getShipment().getAwb().getAwbNumber());
        }
        return getConsignmentListByAwbNumbers(awbNumbers);
    }

    public List<ConsignmentStatus> getConsignmentStatusList() {
        List<ConsignmentStatus> consignmentStatuses = consignmentDao.getAll(ConsignmentStatus.class);
        consignmentStatuses.remove(consignmentDao.get(ConsignmentStatus.class, EnumConsignmentStatus.ShipmentReceivedAtHub.getId()));
        consignmentStatuses.remove(consignmentDao.get(ConsignmentStatus.class, EnumConsignmentStatus.ShipmentOutForDelivery.getId()));
        return consignmentStatuses;
    }

    @Override
    public List<ConsignmentDto> getConsignmentDtoList(Set<Consignment> consignments) {
        List<ConsignmentDto> consignmentDtos = new ArrayList<ConsignmentDto>();

        for (Consignment consignment : consignments) {
            ConsignmentDto consignmentDto = new ConsignmentDto();
            consignmentDto.setAwbNumber(consignment.getAwbNumber());
            consignmentDto.setCnnNumber(consignment.getCnnNumber());
            consignmentDto.setAddress(consignment.getAddress());
            consignmentDto.setPaymentMode(consignment.getPaymentMode());
            consignmentDto.setAmount(consignment.getAmount());
            consignmentDtos.add(consignmentDto);

        }
        return consignmentDtos;
    }

    @Override
    public List<Consignment> getConsignmentsFromConsignmentDtos(List<ConsignmentDto> consignmentDtoList) {
        List<Consignment> consignments = new ArrayList<Consignment>();
        Consignment consignmentObj;
        for (ConsignmentDto consignmentDto : consignmentDtoList) {
            consignmentObj = getConsignmentByAwbNumber(consignmentDto.getAwbNumber());
            consignments.add(consignmentObj);
        }
        return consignments;
    }

    @Override
    public List<Consignment> updateTransferredConsignments(List<ConsignmentDto> consignmentDtoList, User agent, User loggedOnUser) {
        Runsheet runsheet = null;
        Consignment consignment = null;
        Hub deliveryHub = hubService.findHubByName(HKDeliveryConstants.DELIVERY_HUB);
        ConsignmentLifecycleStatus consignmentLifecycleStatus = consignmentDao.get(ConsignmentLifecycleStatus.class, EnumConsignmentLifecycleStatus.Dispatched.getId());
        List<Consignment> consignmentList = new ArrayList<Consignment>();
        List<ConsignmentTracking> consignmentTrackingList = new ArrayList<ConsignmentTracking>();
        for (ConsignmentDto consignmentDto : consignmentDtoList) {
            if (!consignmentDto.getTransferredToAgent().getId().equals(agent.getId())) {
                runsheet = runsheetService.getOpenRunsheetForAgent(consignmentDto.getTransferredToAgent());
                consignment = getConsignmentByAwbNumber(consignmentDto.getAwbNumber());
                consignment.setConsignmentStatus(consignmentDao.get(ConsignmentStatus.class, EnumConsignmentStatus.ShipmentOutForDelivery.getId()));
                runsheet = runsheetService.updateRunsheetParams(runsheet, consignmentDto);
                consignment.setRunsheet(runsheet);
                consignmentTrackingList.add(createConsignmentTracking(runsheet.getHub(), deliveryHub, loggedOnUser, consignment, consignmentLifecycleStatus, "", runsheet));
                consignmentList.add(consignment);
            }
        }
        saveConsignments(consignmentList);
        saveConsignmentTracking(consignmentTrackingList);
        return consignmentList;
    }

    @Override
    public ShippingOrder getShippingOrderFromConsignment(Consignment consignment) {
        return consignmentDao.getShippingOrderFromConsignment(consignment);
    }

    @Override
    public Page getPaymentReconciliationListByDates(Date startDate, Date endDate, int pageNo, int perPage) {
        return consignmentDao.getPaymentReconciliationListByDates(startDate, endDate, pageNo, perPage);
    }

    @Override
    public List<Consignment> getConsignmentsForPaymentReconciliation(Date startDate, Date endDate, Hub hub) {
        return consignmentDao.getConsignmentsForPaymentReconciliation(startDate, endDate, hub);
    }

    @Override
    public List<String> getCustomerOnHoldReasonsForHkDelivery() {
        List<String> customerOnHoldReasons = new ArrayList<String>();
        customerOnHoldReasons.add(HKDeliveryConstants.CUST_HOLD_UNCONTACTABLE);
        customerOnHoldReasons.add(HKDeliveryConstants.CUST_HOLD_HOUSE_LOCKED);
        customerOnHoldReasons.add(HKDeliveryConstants.CUST_HOLD_FUTURE_DELIVERY);
        customerOnHoldReasons.add(HKDeliveryConstants.CUST_HOLD_PAYMENT_NOT_READY);
        customerOnHoldReasons.add(HKDeliveryConstants.CUST_HOLD_WRONG_ADDRESS);
        customerOnHoldReasons.add(HKDeliveryConstants.CUST_HOLD_NOT_INTERESTED);
        customerOnHoldReasons.add(HKDeliveryConstants.CUST_HOLD_DELAY_DELIVERY);
        customerOnHoldReasons.add(HKDeliveryConstants.CUST_HOLD_WRONG_DELIVERY);
        return customerOnHoldReasons;
    }

    @Override
    public ConsignmentTracking getConsignmentTrackingByRunsheetAndStatus(Consignment consignment, Runsheet runsheet, ConsignmentLifecycleStatus consignmentLifecycleStatus) {
        return consignmentDao.getConsignmentTrackingByRunsheetAndStatus(consignment, runsheet, consignmentLifecycleStatus);
    }

    @Override
    public Page searchConsignmentTracking(Date startDate,Date endDate, Long consignmentLifecycleStatus, Long hubId, int pageNo, int perPage) {
        return consignmentDao.searchConsignmentTracking(startDate, endDate, consignmentLifecycleStatus, hubId, pageNo, perPage);
    }
}
