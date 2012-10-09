package com.hk.admin.pact.dao.hkDelivery;

import java.util.Date;
import java.util.List;

import com.akube.framework.dao.Page;
import com.hk.domain.hkDelivery.Consignment;
import com.hk.domain.hkDelivery.ConsignmentStatus;
import com.hk.domain.hkDelivery.ConsignmentTracking;
import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.hkDelivery.Runsheet;
import com.hk.domain.order.ShippingOrder;
import com.hk.pact.dao.BaseDao;


public interface ConsignmentDao extends BaseDao {

    public Consignment getConsignmentByAwbNumber(String awbNumber);

    public List<String> getDuplicateAwbNumbersinRunsheet(List<String> trackingIdList);

    public List<String> getDuplicateAwbNumbersinConsignment(List<String> trackingIdList);

    public List<Consignment> getConsignmentListByAwbNumbers(List<String> awbNumbers);

    public List<ShippingOrder> getShippingOrderFromConsignments(List<String> cnnNumberList);

    public Page searchConsignment(Consignment consignment, String awbNumber, Date startDate, Date endDate, ConsignmentStatus consignmentStatus, Hub hub, Runsheet runsheet, Boolean reconciled, int pageNo, int perPage);

    public List<ConsignmentTracking> getConsignmentTracking(Consignment consignment);

    public ShippingOrder getShippingOrderFromConsignment(Consignment consignment);

    public Page getPaymentReconciliationListByDates(Date startDate, Date endDate ,int pageNo, int perPage);

	public List<Consignment> getConsignmentsForPaymentReconciliation(Date startDate, Date endDate, Hub hub);
}
