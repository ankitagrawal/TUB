package com.hk.admin.impl.dao.courier.reversePickup;

import com.akube.framework.dao.Page;
import com.hk.admin.pact.dao.courier.reversePickup.ReversePickupDao;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.reversePickupOrder.ReversePickupOrder;
import com.hk.domain.reversePickupOrder.ReversePickupStatus;
import com.hk.domain.reversePickupOrder.ReversePickupType;
import com.hk.impl.dao.BaseDaoImpl;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Seema
 * Date: 7/31/13
 * Time: 2:15 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class ReversePickupDaoImpl extends BaseDaoImpl implements ReversePickupDao {

    public Page getReversePickRequest(ShippingOrder shippingOrder, String reversePickupId, Date startDate, Date endDate, Long customerActionStatus, List<ReversePickupStatus> reversePickupStatusList, String courierName, int pageNo, int perPage, List<ReversePickupType> reversePickupTypeList) {
        DetachedCriteria reversePickupCriteria = getReversePickupOrderDetachedCriteria(shippingOrder, reversePickupId, startDate, endDate, customerActionStatus, reversePickupStatusList, courierName, reversePickupTypeList);
        reversePickupCriteria.addOrder(org.hibernate.criterion.Order.desc("createDate"));
        return list(reversePickupCriteria, true, pageNo, perPage);
    }

    private DetachedCriteria getReversePickupOrderDetachedCriteria(ShippingOrder shippingOrder, String reversePickupId, Date startDate, Date endDate, Long customerActionStatus, List<ReversePickupStatus> reversePickupStatusList, String courierName, List<ReversePickupType> reversePickupTypeList) {
        DetachedCriteria reversePickupDetachedCriteria = DetachedCriteria.forClass(ReversePickupOrder.class);
        if (shippingOrder != null) {
            reversePickupDetachedCriteria.add(Restrictions.eq("shippingOrder.id", shippingOrder.getId()));
        }
        if (startDate != null && endDate != null) {
            reversePickupDetachedCriteria.add(Restrictions.between("pickupTime", startDate, endDate));
        }
        if (courierName != null && !StringUtils.isEmpty(courierName)) {
            reversePickupDetachedCriteria.add(Restrictions.eq("courierName", courierName));
        }

        if(shippingOrder == null && reversePickupStatusList != null && !reversePickupStatusList.isEmpty()){
            reversePickupDetachedCriteria.add(Restrictions.in("reversePickupStatus", reversePickupStatusList));
        }

        if(reversePickupTypeList != null && !reversePickupTypeList.isEmpty()){
            reversePickupDetachedCriteria.add(Restrictions.in("reversePickupType", reversePickupTypeList));
        }

        if (reversePickupId != null) {
            reversePickupDetachedCriteria.add(Restrictions.eq("reversePickupId", reversePickupId.trim()));
        }
        if (customerActionStatus != null) {
            DetachedCriteria rpLineItemsCriteria = reversePickupDetachedCriteria.createCriteria("rpLineItems");
            rpLineItemsCriteria.add(Restrictions.eq("customerActionStatus", customerActionStatus));
        }
        return reversePickupDetachedCriteria;
    }


    public List<ReversePickupOrder> getReversePickupsForSO(ShippingOrder shippingOrder) {
        DetachedCriteria reversePickupCriteria = getReversePickupOrderDetachedCriteria(shippingOrder, null, null, null, null, null, null, null);
        return findByCriteria(reversePickupCriteria);
    }

    public ReversePickupOrder getByReversePickupId(String reversePickupId) {
        DetachedCriteria reversePickupCriteria = getReversePickupOrderDetachedCriteria(null, reversePickupId, null, null, null, null, null, null);
        List<ReversePickupOrder> reversePickupOrderList = findByCriteria(reversePickupCriteria);
        return reversePickupOrderList != null && reversePickupOrderList.size() > 0 ? reversePickupOrderList.get(0) : null;
    }

    public List<ReversePickupOrder> getReversePickupsExcludeCurrentRP(ShippingOrder shippingOrder, ReversePickupOrder reversePickupOrder) {
        String query = "From ReversePickupOrder  where shippingOrder.id = :shippingOrderId and id != :id ";
        return getSession().createQuery(query).setParameter("shippingOrderId", shippingOrder.getId()).setParameter("id", reversePickupOrder.getId()).list();
    }


}
