package com.hk.admin.pact.dao.courier.reversePickup;

import com.akube.framework.dao.Page;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.reversePickupOrder.ReversePickupOrder;
import com.hk.domain.reversePickupOrder.ReversePickupStatus;
import com.hk.pact.dao.BaseDao;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Seema
 * Date: 7/31/13
 * Time: 2:17 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ReversePickupDao extends BaseDao {

    public Page getReversePickRequest(ShippingOrder shippingOrder, String reversePickupId, Date startDate, Date endDate, Long customerActionStatus, List<ReversePickupStatus> reversePickupStatusList, String courierName, int pageNo, int perPage);

    public List<ReversePickupOrder> getReversePickupsForSO(ShippingOrder shippingOrder);

    public ReversePickupOrder getByReversePickupId(String reversePickupId);

    public List<ReversePickupOrder> getReversePickupsExcludeCurrentRP(ShippingOrder shippingOrder, ReversePickupOrder reversePickupOrder);
}
