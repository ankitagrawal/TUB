package com.hk.admin.pact.service.hkDelivery;

import com.hk.domain.hkDelivery.Hub;

import com.hk.domain.hkDelivery.Runsheet;
import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.hkDelivery.RunsheetStatus;
import com.hk.domain.user.User;
import com.akube.framework.dao.Page;

import java.util.Date;


public interface RunSheetService {

    public void createRunSheet(Runsheet runsheet);

    public Page searchRunsheet(Runsheet runsheet, Date startDate, Date endDate, RunsheetStatus runsheetStatus, User agent, Hub hub, int pageNo, int perPage);
}
