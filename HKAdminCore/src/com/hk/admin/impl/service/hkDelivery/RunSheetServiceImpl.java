package com.hk.admin.impl.service.hkDelivery;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.hk.admin.pact.service.hkDelivery.RunSheetService;
import com.hk.admin.pact.dao.hkDelivery.RunSheetDao;
import com.hk.domain.hkDelivery.Runsheet;
import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.hkDelivery.RunsheetStatus;
import com.hk.domain.user.User;
import com.akube.framework.dao.Page;

import java.util.Date;

@Service
public class RunSheetServiceImpl implements RunSheetService {

    @Autowired
    private RunSheetDao runsheetDao;
    
    @Override
    public void createRunSheet(Runsheet runsheet) {
        runsheetDao.createRunSheet(runsheet);  
    }

    @Override
    public Page searchRunsheet(Runsheet runsheet, Date startDate, Date endDate, RunsheetStatus runsheetStatus, User agent, Hub hub, int pageNo, int perPage) {
        return runsheetDao.searchRunsheet(runsheet,startDate,endDate,runsheetStatus,agent,hub,pageNo,perPage);
    }
}
