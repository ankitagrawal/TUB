package com.hk.admin.pact.service.hkDelivery;

import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.hkDelivery.*;
import com.hk.domain.user.User;
import com.hk.constants.hkDelivery.EnumRunsheetStatus;
import com.akube.framework.dao.Page;

import java.util.Date;
import java.util.List;


public interface RunSheetService {

    public Runsheet createRunsheet(Hub hub, List<Consignment> consignmentList,RunsheetStatus runsheetStatus,Long userId,Long prePaidBoxCount,Long totalCODPackets,Double totalCODAmount);

    public void saveRunSheet(Runsheet runsheet);

    public Page searchRunsheet(Runsheet runsheet, Date startDate, Date endDate, RunsheetStatus runsheetStatus, User agent, Hub hub, int pageNo, int perPage);
}
