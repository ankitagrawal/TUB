package com.hk.admin.impl.service.hkDelivery;

import com.akube.framework.dao.Page;
import com.hk.admin.pact.dao.hkDelivery.RunSheetDao;
import com.hk.constants.hkDelivery.EnumConsignmentStatus;
import com.hk.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.hk.admin.pact.service.hkDelivery.RunSheetService;
import com.hk.admin.pact.dao.hkDelivery.RunSheetDao;
import com.hk.domain.hkDelivery.Runsheet;
import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.hkDelivery.RunsheetStatus;
import com.hk.domain.hkDelivery.*;
import com.hk.domain.user.User;
import com.akube.framework.dao.Page;

import java.util.Date;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service

public class RunSheetServiceImpl implements RunSheetService {

    @Autowired
    private RunSheetDao runsheetDao;

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

    public void saveRunSheet(Runsheet runsheet) {
        runsheetDao.saveRunSheet(runsheet);
    }

    public Page searchRunsheet(Runsheet runsheet, Date startDate, Date endDate, RunsheetStatus runsheetStatus, User agent, Hub hub, int pageNo, int perPage) {
        return runsheetDao.searchRunsheet(runsheet,startDate,endDate,runsheetStatus,agent,hub,pageNo,perPage);
    }

    @Override
    public boolean isRunsheetClosable(Runsheet runsheet) {
        for (Consignment consignment : runsheet.getConsignments()){
            if(consignment.getConsignmentStatus().getId().equals(EnumConsignmentStatus.ShipmntOutForDelivry.getId())){
                return false;
            }
        }
        return true;
    }

    public Runsheet markAllConsignmentsAsDelivered(Runsheet runsheet){
        Set<Consignment> consignments = runsheet.getConsignments();
        for (Consignment consignment : consignments){
            consignment.setConsignmentStatus(EnumConsignmentStatus.ShpmntDelivered.asConsignmentStatus());
        }
        runsheet.setConsignments(consignments);
        return runsheet;
    }
}
