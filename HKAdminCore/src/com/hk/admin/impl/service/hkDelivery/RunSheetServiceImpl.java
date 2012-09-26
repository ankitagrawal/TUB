package com.hk.admin.impl.service.hkDelivery;

import com.akube.framework.dao.Page;
import com.hk.admin.pact.dao.hkDelivery.RunSheetDao;
import com.hk.admin.pact.service.hkDelivery.ConsignmentService;
import com.hk.admin.pact.service.hkDelivery.HubService;
import com.hk.admin.pact.service.shippingOrder.AdminShippingOrderService;
import com.hk.admin.util.HKDeliveryUtil;
import com.hk.constants.hkDelivery.EnumConsignmentStatus;
import com.hk.constants.hkDelivery.EnumRunsheetStatus;
import com.hk.constants.hkDelivery.HKDeliveryConstants;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.user.User;
import com.hk.pact.service.UserService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hk.admin.pact.service.hkDelivery.RunSheetService;
import com.hk.admin.dto.ConsignmentDto;
import com.hk.domain.hkDelivery.Runsheet;
import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.hkDelivery.RunsheetStatus;
import com.hk.domain.hkDelivery.*;

import java.util.*;

import java.util.Date;

@Service

public class RunSheetServiceImpl implements RunSheetService {

    @Autowired
    private RunSheetDao runsheetDao;
    @Autowired
    ConsignmentService consignmentService;
    @Autowired
    HubService hubService;
    @Autowired
    UserService userService;
	@Autowired
	ShippingOrderService shippingOrderService;
	@Autowired
	private AdminShippingOrderService adminShippingOrderService;

    @Override
    public Runsheet createRunsheet(Hub hub, Set<Consignment> consignments,RunsheetStatus runsheetStatus,User user,Long prePaidBoxCount,Long totalCODPackets,Double totalCODAmount) {
        Runsheet runsheetObj = new Runsheet();
        runsheetObj.setCodBoxCount(totalCODPackets);
        runsheetObj.setCreateDate(new Date());
        runsheetObj.setUpdateDate(new Date());
        runsheetObj.setExpectedCollection(totalCODAmount);
        runsheetObj.setPrepaidBoxCount(prePaidBoxCount);
        runsheetObj.setAgent(user);
        runsheetObj.setHub(hub);
        runsheetObj.setConsignments(consignments);
        runsheetObj.setRunsheetStatus(runsheetStatus);
       return runsheetObj;
    }

    @Override
    public Runsheet saveRunSheet(Runsheet runsheet) {
        runsheet.setUpdateDate(new Date());
        return runsheetDao.saveRunSheet(runsheet);
    }

    @Override
    public void saveRunSheet(Runsheet runsheet, List<Consignment> changedConsignmentsList){
        if(changedConsignmentsList != null){
            updateConsignmentTrackingForRunsheet(changedConsignmentsList, userService.getLoggedInUser());
        }
        runsheet.setUpdateDate(new Date());
        runsheetDao.saveRunSheet(runsheet);
    }

    public Page searchRunsheet(Runsheet runsheet, Date startDate, Date endDate, RunsheetStatus runsheetStatus, User agent, Hub hub, int pageNo, int perPage) {
        return runsheetDao.searchRunsheet(runsheet,startDate,endDate,runsheetStatus,agent,hub,pageNo,perPage);
    }

    @Override
    public boolean isRunsheetClosable(Runsheet runsheet) {
        for (Consignment consignment : runsheet.getConsignments()){
            if(consignment.getConsignmentStatus().getId().equals(EnumConsignmentStatus.ShipmentOutForDelivery.getId())
                    || consignment.getConsignmentStatus().getId().equals(EnumConsignmentStatus.ShipmentReceivedAtHub.getId())){
                return false;
            }
        }
        return true;
    }

    public Runsheet markAllConsignmentsAsDelivered(Runsheet runsheet){
        List<Consignment> consignmentListWithChangedStatuses = new ArrayList<Consignment>();
        Set<Consignment> consignments = runsheet.getConsignments();
        for (Consignment consignment : consignments){
            if(!consignment.getConsignmentStatus().getStatus().equals(EnumConsignmentStatus.ShipmentDelivered.getStatus())){
                consignmentListWithChangedStatuses.add(consignment);
                consignment.setConsignmentStatus(runsheetDao.get(ConsignmentStatus.class, EnumConsignmentStatus.ShipmentDelivered.getId()));
            }
        }
        updateConsignmentTrackingForRunsheet(consignmentListWithChangedStatuses, userService.getLoggedInUser());
        runsheet.setConsignments(consignments);
        return runsheet;
    }

    public boolean agentHasOpenRunsheet(User agent){
        if(searchRunsheet(null,null,null, runsheetDao.get(RunsheetStatus.class, EnumRunsheetStatus.Open.getId()),agent,null,1,10).getList().size() > 0){
            return true;
        }
        return false;
    }

    public void updateConsignmentTrackingForRunsheet(List<Consignment> changedConsignmentsList, User user){
        Long consignmentLifecycleStatusId;
        Hub sourceHub = null;
        Hub destinationHub = null;
        List<ConsignmentTracking> consignmentTrackingList = new ArrayList<ConsignmentTracking>();
        for (Consignment consignmentObj : changedConsignmentsList) {
            if(consignmentObj != null){
                consignmentLifecycleStatusId = HKDeliveryUtil.getLifcycleStatusIdFromConsignmentStatus(consignmentObj.getConsignmentStatus().getStatus());
                ConsignmentLifecycleStatus consignmentLifecycleStatus = runsheetDao.get(ConsignmentLifecycleStatus.class, consignmentLifecycleStatusId);
                if(consignmentObj.getConsignmentStatus().getId().equals(EnumConsignmentStatus.ShipmentDelivered.getId())){
                    sourceHub = consignmentObj.getHub();
                    destinationHub =  hubService.findHubByName(HKDeliveryConstants.DELIVERY_HUB);
                }
                else if(consignmentObj.getConsignmentStatus().getId().equals(EnumConsignmentStatus.ShipmentRTH.getId())){
                    sourceHub = consignmentObj.getHub();
                    destinationHub =  hubService.findHubByName(HKDeliveryConstants.HEALTHKART_HUB);
                }
                else {
                    sourceHub =consignmentObj.getHub();
                    destinationHub = consignmentObj.getHub();
                }
                consignmentTrackingList.add(consignmentService.createConsignmentTracking(sourceHub, destinationHub, user, consignmentObj, consignmentLifecycleStatus));
            }
            }
        if(consignmentTrackingList.size() > 0){
            consignmentService.saveConsignmentTracking(consignmentTrackingList);
        }
    }

    @Override
    public Runsheet updateExpectedAmountForClosingRunsheet(Runsheet runsheet) {
        Double expectedCollection = 0.0;
        for(Consignment consignment : runsheet.getConsignments()){
            if(consignment.getConsignmentStatus().getId().equals(EnumConsignmentStatus.ShipmentDelivered.getId()) &&
               consignment.getPaymentMode().equals(HKDeliveryConstants.COD)){
                expectedCollection += consignment.getAmount();
            }
        }
        runsheet.setExpectedCollection(expectedCollection);
        return runsheet;
    }

    @Override
    public List<User> getAgentList(RunsheetStatus runsheetStatus) {
       User loggedOnUser = userService.getLoggedInUser();
       Hub hub = hubService.getHubForUser(loggedOnUser);
       return runsheetDao.getAgentList(runsheetStatus, hub);
    }

    @Override
    public Runsheet getOpenRunsheetForAgent(User agent) {
        return runsheetDao.getOpenRunsheetForAgent(agent);
    }

    @Override
    public Runsheet updateRunsheetParams(Runsheet runsheet, ConsignmentDto consignmentDto) {
        Double expectedCollection = runsheet.getExpectedCollection();
        Long codBoxCount = runsheet.getCodBoxCount();
        Long prepaidBoxCount = runsheet.getPrepaidBoxCount();

        if (consignmentDto.getPaymentMode().equals(EnumPaymentMode.COD.getName())) {
            expectedCollection = expectedCollection + consignmentDto.getAmount();
            ++codBoxCount;
        } else {
            ++prepaidBoxCount;
        }
        runsheet.setCodBoxCount(codBoxCount);
        runsheet.setExpectedCollection(expectedCollection);
        runsheet.setPrepaidBoxCount(prepaidBoxCount);
        return runsheet;
    }

	@Override
	public void markShippingOrderDeliveredAgainstConsignments(Set<Consignment> consignmentList){
		if(consignmentList != null){
			for(Consignment consignment : consignmentList){
				if(consignment.getConsignmentStatus().getId().equals(EnumConsignmentStatus.ShipmentDelivered.getId())){
					ShippingOrder shippingOrder = shippingOrderService.findByGatewayOrderId(consignment.getCnnNumber());
					adminShippingOrderService.markShippingOrderAsDelivered(shippingOrder);
				}
			}
		}
	}

	@Override
	public Runsheet closeRunsheet(Runsheet runsheet) {
		Set<Consignment> consignments = runsheet.getConsignments();
		runsheet = updateExpectedAmountForClosingRunsheet(runsheet);
		runsheet.setRunsheetStatus(runsheetDao.get(RunsheetStatus.class, EnumRunsheetStatus.Close.getId()));
		//mark shipments delivered on healthkart side
		markShippingOrderDeliveredAgainstConsignments(consignments); 
		return runsheet;
	}
}
