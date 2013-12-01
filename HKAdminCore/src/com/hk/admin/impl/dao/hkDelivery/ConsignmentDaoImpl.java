package com.hk.admin.impl.dao.hkDelivery;

import com.akube.framework.dao.Page;
import com.hk.admin.pact.dao.hkDelivery.ConsignmentDao;
import com.hk.constants.hkDelivery.EnumConsignmentStatus;
import com.hk.constants.hkDelivery.EnumRunsheetStatus;
import com.hk.constants.hkDelivery.HKDeliveryConstants;
import com.hk.domain.hkDelivery.*;
import com.hk.domain.order.ShippingOrder;
import com.hk.impl.dao.BaseDaoImpl;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@SuppressWarnings("unchecked")
public class ConsignmentDaoImpl extends BaseDaoImpl implements ConsignmentDao {

    @Override
    public Consignment getConsignmentByAwbNumber(String awbNumber) {
        String query = "from Consignment cn where cn.awbNumber = :awbNumber";
        return (Consignment) findUniqueByNamedParams(query, new String[]{"awbNumber"}, new Object[]{awbNumber});
    }


    @Override
    public List<String> getDuplicateAwbNumbersinRunsheet(List<String> trackingIdList) {
        String query = "select cn.awbNumber from Consignment cn where cn.runsheet != null and cn.runsheet.runsheetStatus.id = :runsheetStatusId and cn.awbNumber in (:trackingIdList)";
        return (List<String>) findByNamedParams(query, new String[]{"runsheetStatusId", "trackingIdList"}, new Object[]{EnumRunsheetStatus.Open.getId(), trackingIdList});
    }

    @Override
    public List<String> getDuplicateAwbNumbersinConsignment(List<String> trackingIdList) {
        String query = "select cn.awbNumber from Consignment cn where cn.awbNumber in (:trackingIdList)";
        return (List<String>) findByNamedParams(query, new String[]{"trackingIdList"}, new Object[]{trackingIdList});
    }

    @Override
    public List<Consignment> getConsignmentListByAwbNumbers(List<String> awbNumbers) {
        String query = "from Consignment cn where cn.awbNumber in (:trackingIdList)";
        return (List<Consignment>) findByNamedParams(query, new String[]{"trackingIdList"}, new Object[]{awbNumbers});
    }

    @Override
    public List<ShippingOrder> getShippingOrderFromConsignments(List<String> cnnNumberList) {
        String query = "from ShippingOrder sh where sh.gatewayOrderId in (:cnnNumberList)";
        return (List<ShippingOrder>) findByNamedParams(query, new String[]{"cnnNumberList"}, new Object[]{cnnNumberList});
    }

    @Override
    public Page searchConsignment(Consignment consignment, String awbNumber, Date startDate, Date endDate, ConsignmentStatus consignmentStatus, Hub hub, Runsheet runsheet, Boolean reconciled, int pageNo, int perPage) {
        DetachedCriteria consignmentCriteria = DetachedCriteria.forClass(Consignment.class);

        if (consignment != null) {
            consignmentCriteria.add(Restrictions.eq("id", consignment.getId()));
        }

        if (awbNumber != null) {
            consignmentCriteria.add(Restrictions.eq("awbNumber", awbNumber));
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

        if (reconciled != null) {
            if (reconciled) {
                consignmentCriteria.add(Restrictions.isNotNull("hkdeliveryPaymentReconciliation"));
            } else {
                consignmentCriteria.add(Restrictions.isNull("hkdeliveryPaymentReconciliation"));
            }
        }

        if (runsheet != null) {
            consignmentCriteria.add(Restrictions.eq("runsheet", runsheet));
        }

        consignmentCriteria.addOrder(org.hibernate.criterion.Order.desc("id"));
        return list(consignmentCriteria, pageNo, perPage);
    }

    public List<ConsignmentTracking> getConsignmentTracking(Consignment consignment) {
        String query = "from ConsignmentTracking ct where ct.consignment.id = :consignmentId";
        return (List<ConsignmentTracking>) findByNamedParams(query, new String[]{"consignmentId"}, new Object[]{consignment.getId()});
    }

    public ConsignmentTracking getConsignmentTrackingByRunsheetAndStatus(Consignment consignment, Runsheet runsheet, ConsignmentLifecycleStatus consignmentLifecycleStatus) {
        String query = "from ConsignmentTracking ct where ct.consignment.id = :consignmentId and " +
                "ct.runsheet.id = :runsheetId and ct.consignmentLifecycleStatus.id = :consignmentLifecycleStatusId " +
                "order by ct.createDate desc";

        List<ConsignmentTracking> consignmentTrackingList = (List<ConsignmentTracking>) findByNamedParams(query, new String[]{"consignmentId", "runsheetId", "consignmentLifecycleStatusId"},
                new Object[]{consignment.getId(), runsheet.getId(), consignmentLifecycleStatus.getId()});
        if (consignmentTrackingList != null && consignmentTrackingList.size() > 0) {
            return consignmentTrackingList.get(0);
        }
        return null;
    }

    @Override
    public ShippingOrder getShippingOrderFromConsignment(Consignment consignment) {
        String query = "from ShippingOrder sh where sh.gatewayOrderId = :cnnNumber)";
        return (ShippingOrder) findUniqueByNamedParams(query, new String[]{"cnnNumber"}, new Object[]{consignment.getCnnNumber()});
    }

    @Override
    public Page getPaymentReconciliationListByDates(Date startDate, Date endDate, int pageNo, int perPage) {
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

    @Override
    public List<Consignment> getConsignmentsForPaymentReconciliation(Date startDate, Date endDate, Hub hub) {
        DetachedCriteria consignmentCriteria = DetachedCriteria.forClass(Consignment.class);
        if (startDate != null) {
            consignmentCriteria.add(Restrictions.ge("deliveryDate", startDate));
        }
        if (endDate != null) {
            consignmentCriteria.add(Restrictions.le("deliveryDate", endDate));
        }
        if (hub != null) {
            consignmentCriteria.add(Restrictions.eq("hub.id", hub.getId()));
        }

        consignmentCriteria.add(Restrictions.isNull("hkdeliveryPaymentReconciliation"));

        consignmentCriteria.add(Restrictions.eq("consignmentStatus.id", EnumConsignmentStatus.ShipmentDelivered.getId()));

        consignmentCriteria.add(Restrictions.like("paymentMode", HKDeliveryConstants.COD));

        consignmentCriteria.addOrder(org.hibernate.criterion.Order.asc("id"));
        return findByCriteria(consignmentCriteria);
    }

    @Override
    public Page searchConsignmentTracking(Date startDate, Date endDate, Long consignmentLifecycleStatus, Long hubId, int pageNo, int perPage) {
        DetachedCriteria consignmentTrackingCriteria = DetachedCriteria.forClass(ConsignmentTracking.class);

        if (startDate != null) {
            consignmentTrackingCriteria.add(Restrictions.ge("createDate", startDate));
        }

        if (endDate != null) {
            consignmentTrackingCriteria.add(Restrictions.le("createDate", endDate));
        }

        if (consignmentLifecycleStatus != null) {
            consignmentTrackingCriteria.add(Restrictions.eq("consignmentLifecycleStatus.id", consignmentLifecycleStatus));
        }

        if (hubId != null) {
            consignmentTrackingCriteria.add(Restrictions.or(Restrictions.eq("sourceHub.id", hubId), Restrictions.eq("destinationHub.id", hubId)));
        }

        return list(consignmentTrackingCriteria, pageNo, perPage);
    }

    @Override
    public List<Consignment> getConsignmentsByStatusOwnerAndHub(Long consignmentStatus, String owner, Hub hub) {
        String query = "from Consignment c where c.consignmentStatus.id = :consignmentStatus and c.owner = :owner and c.hub = :hub";
        return (List<Consignment>) findByNamedParams(query, new String[]{"consignmentStatus", "owner", "hub"}, new Object[]{consignmentStatus, owner, hub});
    }

    @Override
    public List<Consignment> getConsignmentsByStatusAndOwner(Long consignmentStatus, String owner) {
        String query = "from Consignment c where c.consignmentStatus.id = :consignmentStatus and c.owner = :owner";
        return (List<Consignment>) findByNamedParams(query, new String[]{"consignmentStatus", "owner"}, new Object[]{consignmentStatus, owner});
    }

    @Override
    public ConsignmentTracking getConsignmentTrackingById(Long consignmentTrackingId) {
        String query = "from ConsignmentTracking ct where ct.id = :consignmentTrackingId";
        return (ConsignmentTracking) findUniqueByNamedParams(query, new String[]{"consignmentTrackingId"}, new Object[]{consignmentTrackingId});
    }

    @Override
    public List<ConsignmentTracking> getConsignmentTrackingByStatusAndConsignment(Long consignmentLifecycleStatus, Long consignmentId) {
        String query = "from ConsignmentTracking ct where " +
                "ct.consignmentLifecycleStatus.id = :consignmentLifecycleStatus " +
                "and ct.consignment.id = :consignmentId order by ct.createDate ";
        return (List<ConsignmentTracking>) findByNamedParams(query, new String[]{"consignmentLifecycleStatus", "consignmentId"},
                new Object[]{consignmentLifecycleStatus, consignmentId});
    }
}

