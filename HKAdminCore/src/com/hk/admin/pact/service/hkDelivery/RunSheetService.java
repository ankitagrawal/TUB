package com.hk.admin.pact.service.hkDelivery;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.akube.framework.dao.Page;
import com.hk.admin.dto.ConsignmentDto;
import com.hk.domain.hkDelivery.Consignment;
import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.hkDelivery.Runsheet;
import com.hk.domain.hkDelivery.RunsheetStatus;
import com.hk.domain.user.User;


public interface RunSheetService {

    public Runsheet createRunsheet(Hub hub, Set<Consignment> consignments,RunsheetStatus runsheetStatus,User agent,Long prePaidBoxCount,Long totalCODPackets,Double totalCODAmount);

    public Runsheet saveRunSheet(Runsheet runsheet);

    public void saveRunSheet(Runsheet runsheet, List<Consignment> changedConsignmentList, Map<Consignment, String> consignmentOnHoldReason);

    public Page searchRunsheet(Runsheet runsheet, Date startDate, Date endDate, RunsheetStatus runsheetStatus, User agent, Hub hub, int pageNo, int perPage);

    public boolean isRunsheetClosable(Runsheet runsheet);

    public Runsheet markAllConsignmentsAsDelivered(Runsheet runsheet);

    public boolean agentHasOpenRunsheet(User agent);

    public void updateConsignmentTrackingForRunsheet(List<Consignment> changedConsignmentsList, User user, Runsheet runsheet, Map<Consignment, String> consignmentOnHoldReason);

    public Runsheet updateExpectedAmountForClosingRunsheet(Runsheet runsheet);

    public List<User> getAgentList(RunsheetStatus runsheetStatus);

    public Runsheet getOpenRunsheetForAgent(User agent);

    public Runsheet updateRunsheetParams(Runsheet runsheet, ConsignmentDto consignmentDto);

	public void markShippingOrderDeliveredAgainstConsignments(Set<Consignment> consignmentList);

	public Runsheet closeRunsheet(Runsheet runsheet);

	public Map<Consignment, String> getOnHoldCustomerReasonForRunsheetConsignments(Runsheet runsheet);
}
