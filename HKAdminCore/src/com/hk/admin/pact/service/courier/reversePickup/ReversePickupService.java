package com.hk.admin.pact.service.courier.reversePickup;

import com.akube.framework.dao.Page;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.reversePickupOrder.ReversePickupOrder;
import com.hk.domain.reversePickupOrder.ReversePickupStatus;
import com.hk.domain.reversePickupOrder.RpLineItem;
import com.hk.exception.ReversePickupException;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Seema
 * Date: 7/31/13
 * Time: 2:17 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ReversePickupService {

    public ReversePickupOrder saveReversePickupOrder(ReversePickupOrder reversePickupOrder);

    public RpLineItem saveRpLineItem(RpLineItem rpLineItem);

    public void saveRpLineItems(ReversePickupOrder reversePickupOrder, List<RpLineItem> rpLineItemList, Long customerActionStatus);

    public void updateRpLineItems(List<RpLineItem> rpLineItemList, Long customerActionStatus);

    public Page getReversePickRequest(ShippingOrder shippingOrder, String reversePickupId, Date startDate, Date endDate, Long customerActionStatus, ReversePickupStatus reversePickupStatus, String courierName, int pageNo, int perPage);

    public void SaveModifiedRpLineItems(List<RpLineItem> oldRpLineItems, ReversePickupOrder reversePickupOrder, List<RpLineItem> newRpLineItems);

    public List<ReversePickupOrder> getReversePickupsForSO(ShippingOrder shippingOrder);

    public List<ReversePickupOrder> getReversePickupsExcludeCurrentRP(ShippingOrder shippingOrder, ReversePickupOrder reversePickupOrder);

    public void deleteReversePickupOrder(ReversePickupOrder reversePickupOrder);

    public void deleteRpLineItem(RpLineItem rpLineItem);

    public RpLineItem getRpLineItemById(Long id);

    public ReversePickupOrder getReversePickupOrderById(Long id);

    public ReversePickupOrder getByReversePickupId(String reversePickupId);

    public Boolean checkInRpLineItem(RpLineItem rpLineItem) throws ReversePickupException;


}
