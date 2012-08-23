package com.hk.admin.impl.service.hkDelivery;

import com.akube.framework.dao.Page;
import com.hk.admin.pact.dao.hkDelivery.RunSheetDao;
import com.hk.admin.pact.service.hkDelivery.ConsignmentService;
import com.hk.admin.pact.service.hkDelivery.HubService;
import com.hk.admin.util.HKDeliveryUtil;
import com.hk.constants.hkDelivery.EnumConsignmentStatus;
import com.hk.constants.hkDelivery.EnumRunsheetStatus;
import com.hk.constants.hkDelivery.HKDeliveryConstants;
import com.hk.domain.user.User;
import com.hk.pact.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hk.admin.pact.service.hkDelivery.RunSheetService;
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

    @Override
    public Runsheet createRunsheet(Hub hub, Set<Consignment> consignments,RunsheetStatus runsheetStatus,User user,Long prePaidBoxCount,Long totalCODPackets,Double totalCODAmount) {
        Runsheet runsheetObj = new Runsheet();
        runsheetObj.setCodBoxCount(totalCODPackets);
        runsheetObj.setCreateDate(new Date());
        runsheetObj.setExpectedCollection(totalCODAmount);
        runsheetObj.setPrepaidBoxCount(prePaidBoxCount);
        runsheetObj.setAgent(user);
        runsheetObj.setHub(hub);
        runsheetObj.setConsignments(consignments);
        runsheetObj.setRunsheetStatus(runsheetStatus);
       return runsheetObj;
    }

    @Override
    public void saveRunSheet(Runsheet runsheet) {
        runsheetDao.saveRunSheet(runsheet);
    }

    @Override
    public void saveRunSheet(Runsheet runsheet, List<Consignment> changedConsignmentsList){
        if(changedConsignmentsList != null){
            updateConsignmentTrackingForRunsheet(changedConsignmentsList, userService.getLoggedInUser());
        }
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
        Set<Consignment> consignments = runsheet.getConsignments();
        for (Consignment consignment : consignments){
            consignment.setConsignmentStatus(runsheetDao.get(ConsignmentStatus.class, EnumConsignmentStatus.ShipmentDelivered.getId()));
        }
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
        if(consignmentTrackingList.size() > 0){
            consignmentService.saveConsignmentTracking(consignmentTrackingList);
        }
    }

    @Override
    public Runsheet updateExpectedAmountForClosingRunsheet(Runsheet runsheet) {
        Double expectedCollection = 0.0;
        for(Consignment consignment : runsheet.getConsignments()){
            if(consignment.getConsignmentStatus().getId().equals(EnumConsignmentStatus.ShipmentDelivered.getId())){
                expectedCollection += consignment.getAmount();
            }
        }
        runsheet.setExpectedCollection(expectedCollection);
        return runsheet;
    }
}
