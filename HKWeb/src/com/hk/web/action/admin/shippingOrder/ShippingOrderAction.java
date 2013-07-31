package com.hk.web.action.admin.shippingOrder;

import com.akube.framework.gson.JsonUtils;
import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.controller.JsonHandler;
import com.hk.admin.pact.service.shippingOrder.AdminShippingOrderService;
import com.hk.constants.inventory.EnumReconciliationActionType;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.core.search.ShippingOrderSearchCriteria;
import com.hk.domain.core.PaymentStatus;
import com.hk.domain.order.ReplacementOrder;
import com.hk.domain.order.ReplacementOrderReason;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.service.core.WarehouseService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.pact.service.payment.PaymentService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;
import com.hk.web.HealthkartResponse;
import com.hk.web.action.admin.order.search.SearchShippingOrderAction;
import net.sourceforge.stripes.action.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ShippingOrderAction extends BaseAction {

    private ShippingOrder shippingOrder;

    private String cancellationRemark;
    private boolean firewall;
    private Warehouse warehouseToUpdate;
    @Autowired
    WarehouseService warehouseService;

    @Autowired
    private ShippingOrderService shippingOrderService;
    @Autowired
    private ShippingOrderStatusService shippingOrderStatusService;
    @Autowired
    private AdminShippingOrderService adminShippingOrderService;
    @Autowired
    SkuService skuService;
    @Autowired
    private PaymentService paymentService;

    private ReplacementOrderReason rtoReason;

    private String customerSatisfyReason;

    //@Validate(required = true)
    private Long reconciliationType;

    @DefaultHandler
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/order/flipShippingOrder.jsp");
    }

    public Resolution flipWarehouse() {
        boolean isWarehouseUpdated = adminShippingOrderService.updateWarehouseForShippingOrder(shippingOrder, warehouseToUpdate);

        String responseMsg = "";
        if (isWarehouseUpdated) {
            responseMsg = "Warehouse Flipped";
        } else {
            responseMsg = " Either All products not avaialable in other warehouse or sku for all products are nor available , so cannot be updated.";
        }
        addRedirectAlertMessage(new SimpleMessage(responseMsg));
        return new ForwardResolution("/pages/admin/order/flipShippingOrder.jsp");
    }

    @JsonHandler
    public Resolution initiateRTO() {
        adminShippingOrderService.initiateRTOForShippingOrder(shippingOrder, rtoReason);

        Map<String, Object> data = new HashMap<String, Object>(1);
        data.put("orderStatus", JsonUtils.hydrateHibernateObject(shippingOrder.getOrderStatus()));
        HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "success", data);
        return new JsonResolution(healthkartResponse);
    }

    public Resolution markOrderCustomerReturn() {
        getBaseDao().save(shippingOrder);
        shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_Customer_Return);
        return new RedirectResolution(SearchShippingOrderAction.class, "searchShippingOrder").addParameter("shippingOrderId", shippingOrder.getId());
    }

    public Resolution markOrderCustomerSatisfy() {
        getBaseDao().save(shippingOrder);
        shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_Customer_Satisfaction, null, customerSatisfyReason);
        return new RedirectResolution(SearchShippingOrderAction.class, "searchShippingOrder").addParameter("shippingOrderId", shippingOrder.getId());
    }


    @JsonHandler
    public Resolution markRTO() {
        adminShippingOrderService.markShippingOrderAsRTO(shippingOrder);

        Map<String, Object> data = new HashMap<String, Object>(1);
        data.put("orderStatus", JsonUtils.hydrateHibernateObject(shippingOrder.getOrderStatus()));
        HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "success", data);
        return new JsonResolution(healthkartResponse);
    }

    @JsonHandler
    public Resolution cancelShippingOrder() {
        adminShippingOrderService.cancelShippingOrder(shippingOrder, cancellationRemark, null);
        if (shippingOrder instanceof ReplacementOrder) {
            //do nothing for replacement orders
        } else {
            shippingOrderService.revertRewardPointsOnSOCancel(shippingOrder, cancellationRemark);
            if (EnumShippingOrderStatus.SO_Cancelled.getId().equals(shippingOrder.getOrderStatus().getId())) {
                if (paymentService.isValidReconciliation(shippingOrder.getBaseOrder().getPayment()) && reconciliationType != null) {
                    if (shippingOrder.getAmount() > 0) {
                        Map<Long, Object> reconMap = paymentService.reconciliationOnCancel(reconciliationType, shippingOrder.getBaseOrder(), shippingOrder.getAmount(), cancellationRemark);

                        if (reconMap.get(reconciliationType) == null) {
                            shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.RefundAmountExceedsFailed);
                            addRedirectAlertMessage(new SimpleMessage("Amount exceeds the refundable amount"));
                        } else {
                            if (EnumReconciliationActionType.RewardPoints.getId().equals(reconciliationType)) {
                                if ((Boolean) reconMap.get(reconciliationType)) {
                                    shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.RewardPointOrderCancel);
                                    addRedirectAlertMessage(new SimpleMessage("Reward Point awarded to customer"));
                                }
                            } else if (EnumReconciliationActionType.RefundAmount.getId().equals(reconciliationType)) {
                                PaymentStatus paymentStatus = (PaymentStatus) reconMap.get(reconciliationType);
                                if (EnumPaymentStatus.REFUNDED.getId().equals(paymentStatus.getId())) {
                                    shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.AmountRefundedOrderCancel);
                                    addRedirectAlertMessage(new SimpleMessage("Amount Refunded to customer"));
                                } else if (EnumPaymentStatus.REFUND_FAILURE.getId().equals(paymentStatus.getId())) {
                                    shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.RefundAmountFailed);
                                    addRedirectAlertMessage(new SimpleMessage("Amount couldn't be refunded to user, Please contact tech support"));
                                } else if (EnumPaymentStatus.REFUND_REQUEST_IN_PROCESS.getId().equals(paymentStatus.getId())) {
                                    shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.RefundAmountInProcess);
                                    addRedirectAlertMessage(new SimpleMessage("Refund is in process, Please contact tech support"));
                                }
                            }
                        }
                    }
                }
                addRedirectAlertMessage(new SimpleMessage("Shipping Order Cancelled Successfully!!!"));
            } else {
                addRedirectAlertMessage(new SimpleMessage("Please Try again Later!!!"));
            }
        }
        return new RedirectResolution(SearchShippingOrderAction.class, "searchShippingOrder").addParameter("shippingOrderGatewayId", shippingOrder.getGatewayOrderId());
    }

    @JsonHandler
    public Resolution manualEscalateShippingOrder() {
        shippingOrderService.manualEscalateShippingOrder(shippingOrder);
        Map<String, Object> data = new HashMap<String, Object>(1);
        data.put("orderStatus", JsonUtils.hydrateHibernateObject(shippingOrder.getOrderStatus()));
        HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Check SO Status", data);
        return new JsonResolution(healthkartResponse);
    }

    @JsonHandler
    public Resolution autoEscalateShippingOrder() {
        shippingOrderService.autoEscalateShippingOrder(shippingOrder, firewall);
        Map<String, Object> data = new HashMap<String, Object>(1);
        data.put("orderStatus", JsonUtils.hydrateHibernateObject(shippingOrder.getOrderStatus()));
        HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Check SO Status", data);
        return new JsonResolution(healthkartResponse);
    }

    public Resolution bulkEscalateShippingOrder() {
        ShippingOrderSearchCriteria shippingOrderSearchCriteria = new ShippingOrderSearchCriteria();
        shippingOrderSearchCriteria.setShippingOrderStatusList(Arrays.asList(shippingOrderStatusService.find(EnumShippingOrderStatus.SO_ActionAwaiting)));
        shippingOrderSearchCriteria.setPaymentStatuses(EnumPaymentStatus.getEscalablePaymentStatuses());
        shippingOrderSearchCriteria.setDropShipping(false);
        List<ShippingOrder> shippingOrders = shippingOrderService.searchShippingOrders(shippingOrderSearchCriteria, false);
        for (ShippingOrder toBeEscalateShippingOrder : shippingOrders) {
            shippingOrderService.automateManualEscalation(toBeEscalateShippingOrder);
        }
        return new ForwardResolution("/pages/admin/shipment/shipmentCostCalculator.jsp");
    }

    public ShippingOrder getShippingOrder() {
        return shippingOrder;
    }

    public void setShippingOrder(ShippingOrder shippingOrder) {
        this.shippingOrder = shippingOrder;
    }

    public ReplacementOrderReason getRtoReason() {
        return rtoReason;
    }

    public void setRtoReason(ReplacementOrderReason rtoReason) {
        this.rtoReason = rtoReason;
    }

    public void setCustomerSatisfyReason(String customerSatisfyReason) {
        this.customerSatisfyReason = customerSatisfyReason;
    }

    public String getCustomerSatisfyReason() {
        return customerSatisfyReason;
    }

    public String getCancellationRemark() {
        return cancellationRemark;
    }

    public void setCancellationRemark(String cancellationRemark) {
        this.cancellationRemark = cancellationRemark;
    }

    public Warehouse getWarehouseToUpdate() {
        return warehouseToUpdate;
    }

    public void setWarehouseToUpdate(Warehouse warehouseToUpdate) {
        this.warehouseToUpdate = warehouseToUpdate;
    }

    public boolean isFirewall() {
        return firewall;
    }

    public void setFirewall(boolean firewall) {
        this.firewall = firewall;
    }

    public Long getReconciliationType() {
        return reconciliationType;
    }

    public void setReconciliationType(Long reconciliationType) {
        this.reconciliationType = reconciliationType;
    }
}
