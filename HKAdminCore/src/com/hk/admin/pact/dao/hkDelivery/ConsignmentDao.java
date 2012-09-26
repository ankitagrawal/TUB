package com.hk.admin.pact.dao.hkDelivery;

import com.akube.framework.dao.Page;
import com.hk.domain.courier.Awb;
import com.hk.domain.hkDelivery.*;
import com.hk.pact.dao.BaseDao;
import com.hk.domain.courier.Shipment;
import com.hk.domain.user.User;
import com.hk.domain.order.ShippingOrder;

import java.util.Date;
import java.util.List;
import java.util.Set;


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
