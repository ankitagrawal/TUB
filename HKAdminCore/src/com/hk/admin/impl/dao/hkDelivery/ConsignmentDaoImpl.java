package com.hk.admin.impl.dao.hkDelivery;

import com.akube.framework.dao.Page;
import com.hk.domain.courier.Awb;
import com.hk.domain.hkDelivery.*;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.admin.pact.dao.hkDelivery.ConsignmentDao;
import com.hk.domain.courier.Shipment;
import com.hk.domain.user.User;
import com.hk.domain.order.ShippingOrder;
import com.hk.constants.hkDelivery.EnumConsignmentStatus;
import com.hk.constants.hkDelivery.EnumRunsheetStatus;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
public class ConsignmentDaoImpl extends BaseDaoImpl implements ConsignmentDao {

    @Override
    public Consignment getConsignmentByAwbNumber(String awbNumber) {
        String query = "from Consignment cn where cn.awbNumber = :awbNumber";
        return (Consignment) findUniqueByNamedParams(query,new String[]{"awbNumber"},new Object[]{awbNumber});
    }


    @Override
    public List<String> getDuplicateAwbNumbersinRunsheet(List<String> trackingIdList) {
        String query = "select cn.awbNumber from Consignment cn where cn.runsheet != null and cn.runsheet.runsheetStatus.id = :runsheetStatusId and cn.awbNumber in (:trackingIdList)";
        return (List<String>) findByNamedParams(query, new String[]{"runsheetStatusId", "trackingIdList"}, new Object[]{EnumRunsheetStatus.Open.getId(), trackingIdList});
    }

    @Override
    public List<String> getDuplicateAwbNumbersinConsignment(List<String> trackingIdList) {
        String query = "select cn.awbNumber from Consignment cn where cn.awbNumber in (:trackingIdList)";
        return (List<String>)findByNamedParams(query,new String[]{"trackingIdList"},new Object[]{trackingIdList});
    }

    @Override
    public List<Consignment> getConsignmentListByAwbNumbers(List<String> awbNumbers) {
        String query = "from Consignment cn where cn.awbNumber in (:trackingIdList)";
        return (List<Consignment>)findByNamedParams(query,new String[]{"trackingIdList"},new Object[]{awbNumbers});
    }

    @Override
    public List<ShippingOrder> getShippingOrderFromConsignments(List<String> cnnNumberList) {
        String query = "from ShippingOrder sh where sh.gatewayOrderId in (:cnnNumberList)";
        return (List<ShippingOrder>) findByNamedParams(query,new String[]{"cnnNumberList"},new Object[]{cnnNumberList});
    }

    @Override
    public Page searchConsignment(Consignment consignment, String awbNumber, Date startDate, Date endDate, ConsignmentStatus consignmentStatus, Hub hub, Runsheet runsheet, Boolean reconciled, int pageNo, int perPage){
        DetachedCriteria consignmentCriteria = DetachedCriteria.forClass(Consignment.class);

        if (consignment != null) {
            consignmentCriteria.add(Restrictions.eq("id", consignment.getId()));
        }

        if (awbNumber != null) {
            consignmentCriteria.add(Restrictions.eq("awbNumber", awbNumber));
        }

        if (consignment != null) {
            consignmentCriteria.add(Restrictions.eq("id", consignment.getId()));
        }

        if (startDate != null) {
            consignmentCriteria.add(Restrictions.ge("createDate", startDate));
        }
        if (endDate != null) {
            consignmentCriteria.add(Restrictions.le("createDate", endDate));
        }
        if (consignmentStatus != null) {
            consignmentCriteria.add(Restrictions.eq("consignmentStatus", consignmentStatus));
        }

        if (hub != null) {
            consignmentCriteria.add(Restrictions.eq("hub", hub));
        }

        if(reconciled != null){
            if(reconciled){
                consignmentCriteria.add(Restrictions.isNotNull("hkdeliveryPaymentReconciliation"));
            }
            else{
                consignmentCriteria.add(Restrictions.isNull("hkdeliveryPaymentReconciliation"));
            }
        }

        if(runsheet != null){
            consignmentCriteria.add(Restrictions.eq("runsheet", runsheet));
        }

        consignmentCriteria.addOrder(org.hibernate.criterion.Order.desc("id"));
        return list(consignmentCriteria, pageNo, perPage);
    }

    public List<ConsignmentTracking> getConsignmentTracking(Consignment consignment) {
         String query = "from ConsignmentTracking ct where ct.consignment.id = :consignmentId";
        return (List<ConsignmentTracking>) findByNamedParams(query,new String[]{"consignmentId"},new Object[]{consignment.getId()});
    }

    @Override
    public ShippingOrder getShippingOrderFromConsignment(Consignment consignment) {
        String query = "from ShippingOrder sh where sh.gatewayOrderId = :cnnNumber)";
                return (ShippingOrder) findUniqueByNamedParams(query,new String[]{"cnnNumber"},new Object[]{consignment.getCnnNumber()});
    }

    @Override
    public Page getPaymentReconciliationListByDates(Date startDate, Date endDate ,int pageNo, int perPage) {
        DetachedCriteria paymentReconciliationCriteria = DetachedCriteria.forClass(HkdeliveryPaymentReconciliation.class);


        if (startDate != null) {
            paymentReconciliationCriteria.add(Restrictions.ge("createDate", startDate));
        }
        if (endDate != null) {
            paymentReconciliationCriteria.add(Restrictions.le("createDate", endDate));
        }
        paymentReconciliationCriteria.addOrder(org.hibernate.criterion.Order.desc("id"));
        return list(paymentReconciliationCriteria, pageNo, perPage);
    }
}

