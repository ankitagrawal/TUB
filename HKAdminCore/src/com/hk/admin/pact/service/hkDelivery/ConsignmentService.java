package com.hk.admin.pact.service.hkDelivery;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.akube.framework.dao.Page;
import com.hk.admin.dto.ConsignmentDto;
import com.hk.domain.hkDelivery.Consignment;
import com.hk.domain.hkDelivery.ConsignmentLifecycleStatus;
import com.hk.domain.hkDelivery.ConsignmentStatus;
import com.hk.domain.hkDelivery.ConsignmentTracking;
import com.hk.domain.hkDelivery.HkdeliveryPaymentReconciliation;
import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.hkDelivery.Runsheet;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.user.User;


public interface ConsignmentService {

    public Consignment createConsignment(String awbNumber,String cnnNumber ,double amount, String paymentMode ,String address ,Hub hub);

    public List<ConsignmentTracking> createConsignmentTracking(Hub sourceHub, Hub destinationHub, User user, List<Consignment> consignment , ConsignmentLifecycleStatus consignmentLifecycleStatus, Runsheet runsheet);

    public ConsignmentTracking createConsignmentTracking(Hub sourceHub, Hub destinationHub, User user, Consignment consignment, ConsignmentLifecycleStatus consignmentLifecycleStatus, String consignmentTrackingRemark, Runsheet runsheet);

    public void saveConsignments(List<Consignment> consignmentList);

    public void saveConsignmentTracking(List<ConsignmentTracking> consignmentTrackingList);

    public Consignment getConsignmentByAwbNumber(String awbNumber);

    public List<String> getDuplicateAwbNumbersinRunsheet(List<String> trackingIdList);

    public String  getConsignmentPaymentMode(ShippingOrder shipppingOrder);

    public List<String> getDuplicateAwbNumbersinConsignment(List<String> trackingIdList);

    public List<Consignment> getConsignmentListByAwbNumbers(List<String> awbNumbers);

    public List<ShippingOrder> getShippingOrderFromConsignments(List<Consignment> consignments);

    public Map<Object,Object> getRunsheetCODParams(Set<Consignment> consignments);

    public List<ConsignmentTracking> getConsignmentTracking(Consignment consignment);

    public Page searchConsignment(Consignment consignment, String awbNumber, Date startDate, Date endDate, ConsignmentStatus consignmentStatus, Hub hub, Runsheet runsheet, Boolean reconciled, int pageNo, int perPage);

    public HkdeliveryPaymentReconciliation createPaymentReconciliationForConsignmentList(List<Consignment> consignmentListForPaymentReconciliation, User user);

    public HkdeliveryPaymentReconciliation saveHkdeliveryPaymentReconciliation(HkdeliveryPaymentReconciliation hkdeliveryPaymentReconciliation, User loggedOnUser);

    public boolean isConsignmentValidForRunsheet(Consignment consignment);

    public List<Consignment> getConsignmentsFromShippingOrderList(List<ShippingOrder> shippingOrderList);

    public List<ConsignmentStatus> getConsignmentStatusList();

    public List<ConsignmentDto> getConsignmentDtoList(Set<Consignment> consignments);

    public List<Consignment>  getConsignmentsFromConsignmentDtos(List<ConsignmentDto> consignmentDtoList);

    public List<Consignment> updateTransferredConsignments(List<ConsignmentDto> consignmentDtoList , User agent, User loggedOnUser);

    public ShippingOrder getShippingOrderFromConsignment(Consignment consignment);

    public Page getPaymentReconciliationListByDates(Date startDate, Date endDate ,int pageNo, int perPage);

	public List<Consignment> getConsignmentsForPaymentReconciliation(Date startDate, Date endDate, Hub hub);

	public List<String> getCustomerOnHoldReasonsForHkDelivery();

	ConsignmentTracking getConsignmentTrackingByRunsheetAndStatus(Consignment consignment, Runsheet runsheet, ConsignmentLifecycleStatus consignmentLifecycleStatus);

}
