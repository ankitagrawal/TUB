package com.hk.admin.pact.service.hkDelivery;

import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.hkDelivery.*;
import com.hk.domain.user.User;
import com.hk.constants.hkDelivery.EnumRunsheetStatus;
import com.akube.framework.dao.Page;

import java.util.Date;
import java.util.List;
import java.util.Set;


public interface RunSheetService {

    public Runsheet createRunsheet(Hub hub, Set<Consignment> consignments,RunsheetStatus runsheetStatus,User user,Long prePaidBoxCount,Long totalCODPackets,Double totalCODAmount);

    public void saveRunSheet(Runsheet runsheet);

    public Page searchRunsheet(Runsheet runsheet, Date startDate, Date endDate, RunsheetStatus runsheetStatus, User agent, Hub hub, int pageNo, int perPage);

    public boolean isRunsheetClosable(Runsheet runsheet);

    public Runsheet markAllConsignmentsAsDelivered(Runsheet runsheet);

    public boolean agentHasOpenRunsheet(User agent);

    public void updateConsignmentTrackingForRunsheet(Runsheet runsheet, User user);
}
