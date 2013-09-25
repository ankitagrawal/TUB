package com.hk.web.action.admin.reversePickup;

import com.hk.admin.pact.service.courier.reversePickup.ReversePickupService;
import com.hk.constants.reversePickup.EnumCourierConstant;
import com.hk.constants.reversePickup.EnumReversePickupStatus;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.reversePickupOrder.ReversePickupOrder;
import com.hk.domain.reversePickupOrder.ReversePickupStatus;
import com.hk.domain.reversePickupOrder.RpLineItem;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.pact.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Seema
 * Date: 7/24/13
 * Time: 10:22 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class ReversePickupHelper {
    @Autowired
    UserService userService;
    @Autowired
    private ReversePickupService reversePickupService;

    public ReversePickupOrder createAndSaveReversePickupOrder( ReversePickupOrder reversePickupOrder , ShippingOrder shippingOrder, Double amount, ReversePickupStatus reversePickupStatus, Date pickupDate) {
        if (amount != null) {
            reversePickupOrder.setAmount(amount);
        }
        if (reversePickupOrder.getCreateDate() == null) {
            reversePickupOrder.setCreateDate(new Date());
        }
        if (pickupDate != null) {
            reversePickupOrder.setPickupTime(pickupDate);
        }
        if (reversePickupOrder.getCreatedBy() == null) {
            reversePickupOrder.setCreatedBy(userService.getLoggedInUser());
        }
        if (shippingOrder != null) {
            reversePickupOrder.setShippingOrder(shippingOrder);
        }
        if (reversePickupStatus != null) {
            reversePickupOrder.setReversePickupStatus(reversePickupStatus);
        }
        return reversePickupService.saveReversePickupOrder(reversePickupOrder);
    }


    public Double calculateReversePickupAccount(List<RpLineItem> selectedRpLineItemList) {
        Double requestedAmount = 0d;
        if (selectedRpLineItemList != null) {
            for (RpLineItem rpLineItem : selectedRpLineItemList) {
                requestedAmount += calculateRPLineItemAmount(rpLineItem);
//                requestedAmount += calculateRPLineItemAmount(rpLineItem);
            }
        }
        return requestedAmount;
    }

    public Double calculateRPLineItemAmount(RpLineItem rpLineItem) {
        Double requestedAmount = 0d;
        LineItem lineItem = rpLineItem.getLineItem();
        double lineItemAmount = lineItem.getHkPrice();
        double totalDiscountOnLineItem = lineItem.getDiscountOnHkPrice() + lineItem.getOrderLevelDiscount() + lineItem.getRewardPoints();
        double forwardingCharges = lineItem.getShippingCharges() + lineItem.getCodCharges(); // check this calculation
        //double forwardingCharges = 0.0;
        requestedAmount += (lineItemAmount - totalDiscountOnLineItem + forwardingCharges);
        return requestedAmount;
    }


    /*lineItemHasRpLineItemsMap  Key : LineItemId Value : List of corresponding RPLineItems created for RP*/
    public Map<Long, List<RpLineItem>> populateAlreadyCreatedRpLineItemsInfo(List<ReversePickupOrder> reversePickupOrderList) {
        Map<Long, List<RpLineItem>> lineItemHasRpLIneItemsListMap = new HashMap<Long, List<RpLineItem>>();
        if (reversePickupOrderList != null) {
            for (ReversePickupOrder reversePickupOrder : reversePickupOrderList) {
                List<RpLineItem> rpLineItemList = null;
                for (RpLineItem rpLineItem : reversePickupOrder.getRpLineItems()) {
                    Long lineItemId = rpLineItem.getLineItem().getId();
                    if (lineItemHasRpLIneItemsListMap.containsKey(lineItemId)) {
                        rpLineItemList = lineItemHasRpLIneItemsListMap.get(lineItemId);
                        rpLineItemList.add(rpLineItem);
                    } else {
                        rpLineItemList = new ArrayList<RpLineItem>();
                        rpLineItemList.add(rpLineItem);

                    }
                    lineItemHasRpLIneItemsListMap.put(lineItemId, rpLineItemList);
                }

            }

        }
        return lineItemHasRpLIneItemsListMap;
    }


    public ReversePickupStatus getReversePickupStatus(Long courierManagedBy) {
        ReversePickupStatus reversePickupStatus = null;
        if (courierManagedBy != null) {
            if (courierManagedBy.equals(EnumCourierConstant.HealthKart_Managed.getId())) {
                reversePickupStatus =EnumReversePickupStatus.RPU_Initiated.asReversePickupStatus();
            } else if (courierManagedBy.equals(EnumCourierConstant.Customer_Managed.getId())) {
                reversePickupStatus = EnumReversePickupStatus.Return_Initiated.asReversePickupStatus();
            }
        }
        return reversePickupStatus;
    }



}
