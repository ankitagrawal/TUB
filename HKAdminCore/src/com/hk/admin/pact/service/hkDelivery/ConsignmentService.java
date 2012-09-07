package com.hk.admin.pact.service.hkDelivery;

import com.akube.framework.dao.Page;
import com.hk.domain.courier.Awb;
import com.hk.domain.hkDelivery.*;
import com.hk.domain.courier.Shipment;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Courier;
import com.hk.domain.user.User;
import com.hk.domain.core.PaymentMode;
import com.hk.domain.order.ShippingOrder;
import com.hk.admin.dto.ConsignmentDto;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.Map;


public interface ConsignmentService {

    public Consignment createConsignment(String awbNumber,String cnnNumber ,double amount, String paymentMode ,String address ,Hub hub);

    public List<ConsignmentTracking> createConsignmentTracking(Hub sourceHub, Hub destinationHub, User user, List<Consignment> consignment , ConsignmentLifecycleStatus consignmentLifecycleStatus);

    public ConsignmentTracking createConsignmentTracking(Hub sourceHub, Hub destinationHub, User user, Consignment consignment, ConsignmentLifecycleStatus consignmentLifecycleStatus);

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

    public HkdeliveryPaymentReconciliation saveHkdeliveryPaymentReconciliation(HkdeliveryPaymentReconciliation hkdeliveryPaymentReconciliation);

    public boolean isConsignmentValidForRunsheet(Consignment consignment);

    public List<Consignment> getConsignmentsFromShippingOrderList(List<ShippingOrder> shippingOrderList);

    public List<ConsignmentStatus> getConsignmentStatusList();

    public List<ConsignmentDto> getConsignmentDtoList(Set<Consignment> consignments);

    public List<Consignment>  getConsignmentsFromConsignmentDtos(List<ConsignmentDto> consignmentDtoList);

    public List<Consignment> updateTransferredConsignments(List<ConsignmentDto> consignmentDtoList , User agent);

    public ShippingOrder getShippingOrderFromConsignment(Consignment consignment);

}
