package com.hk.admin.impl.service.hkDelivery;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.hk.admin.pact.service.hkDelivery.RunSheetService;
import com.hk.admin.pact.dao.hkDelivery.RunSheetDao;
import com.hk.domain.hkDelivery.Runsheet;
import com.hk.domain.hkDelivery.Hub;

@Service
public class RunSheetServiceImpl implements RunSheetService {

    @Autowired
    private RunSheetDao runsheetDao;
    @Override
    public void createRunSheet(Runsheet runsheet) {
        runsheetDao.createRunSheet(runsheet);  
    }
}
