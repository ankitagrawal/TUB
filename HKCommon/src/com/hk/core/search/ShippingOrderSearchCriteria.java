package com.hk.core.search;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.domain.courier.Courier;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.order.ShippingOrderStatus;

/**
 * @author vaibhav.adlakha
 */
public class ShippingOrderSearchCriteria extends AbstractOrderSearchCriteria {

    private String                                   trackingId;
    private List<Courier>                            courierList;
    private Long                                     warehouseId;
    private boolean                                  isServiceOrder = false;
    private Date                                     activityStartDate;
    private Date                                     activityEndDate;
    private String                                   basketCategory;
    private Long                                     baseOrderId;
    private String                                   baseGatewayOrderId;

    private List<EnumShippingOrderLifecycleActivity> shippingOrderLifeCycleActivities;
    private List<ShippingOrderStatus>                shippingOrderStatusList;

    public ShippingOrderSearchCriteria setShippingOrderLifeCycleActivities(List<EnumShippingOrderLifecycleActivity> shippingOrderLifeCycleActivities) {
        this.shippingOrderLifeCycleActivities = shippingOrderLifeCycleActivities;
        return this;
    }

    public ShippingOrderSearchCriteria setShippingOrderStatusList(List<ShippingOrderStatus> shippingOrderStatusList) {
        this.shippingOrderStatusList = shippingOrderStatusList;
        return this;
    }

    public ShippingOrderSearchCriteria setServiceOrder(boolean serviceOrder) {
        isServiceOrder = serviceOrder;
        return this;
    }

    public ShippingOrderSearchCriteria setBasketCategory(String basketCategory) {
        this.basketCategory = basketCategory;
        return this;
    }

    public ShippingOrderSearchCriteria setActivityStartDate(Date activityStartDate) {
        this.activityStartDate = activityStartDate;
        return this;
    }

    public ShippingOrderSearchCriteria setActivityEndDate(Date activityEndDate) {
        this.activityEndDate = activityEndDate;
        return this;
    }

    public ShippingOrderSearchCriteria setTrackingId(String trackingId) {
        this.trackingId = trackingId;
        return this;
    }

    public ShippingOrderSearchCriteria setCourierList(List<Courier> courierList) {
        this.courierList = courierList;
        return this;
    }

    public ShippingOrderSearchCriteria setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
        return this;
    }

    public ShippingOrderSearchCriteria setBaseOrderId(Long baseOrderId) {
        this.baseOrderId = baseOrderId;
        return this;
    }

    public ShippingOrderSearchCriteria setBaseGatewayOrderId(String baseGatewayOrderId) {
        this.baseGatewayOrderId = baseGatewayOrderId;
        return this;
    }

    protected DetachedCriteria getBaseCriteria() {
        DetachedCriteria criteria = DetachedCriteria.forClass(ShippingOrder.class);
        return criteria;
    }

    protected DetachedCriteria buildSearchCriteriaFromBaseCriteria() {
        DetachedCriteria criteria = super.buildSearchCriteriaFromBaseCriteria();

        if (shippingOrderStatusList != null && shippingOrderStatusList.size() > 0) {
            criteria.add(Restrictions.in("shippingOrderStatus", shippingOrderStatusList));
        }

        DetachedCriteria shipmentCriteria = null;
        if (StringUtils.isNotBlank(trackingId)) {

            if (shipmentCriteria == null) {
                shipmentCriteria = criteria.createCriteria("shipment");
            }
            shipmentCriteria.add(Restrictions.eq("trackingId", trackingId));
        }

        if (courierList != null && !courierList.isEmpty()) {
            if (shipmentCriteria == null) {
                shipmentCriteria = criteria.createCriteria("shipment");
            }
            shipmentCriteria.add(Restrictions.in("courier", courierList));
        }

        if (StringUtils.isNotBlank(basketCategory)) {
            criteria.add(Restrictions.eq("basketCategory", basketCategory));
        }

        DetachedCriteria baseOrderCriteria = null;
        if (baseOrderId != null) {
            baseOrderCriteria = criteria.createCriteria("baseOrder");
            baseOrderCriteria.add(Restrictions.eq("id", baseOrderId));
        }

        if (baseGatewayOrderId != null) {
            if (baseOrderCriteria == null) {
                baseOrderCriteria = criteria.createCriteria("baseOrder");
            }
            baseOrderCriteria.add(Restrictions.eq("gatewayOrderId", baseGatewayOrderId));
        }

        DetachedCriteria warehouseCriteria = null;
        if (warehouseId != null) {
            warehouseCriteria = criteria.createCriteria("warehouse");
            warehouseCriteria.add(Restrictions.eq("id", warehouseId));
        }

        criteria.add(Restrictions.eq("isServiceOrder", isServiceOrder));

        DetachedCriteria shippingOrderLifecycleCriteria = null;
        if (shippingOrderLifeCycleActivities != null && shippingOrderLifeCycleActivities.size() > 0) {
            List<Long> shippingOrderLifeCycleIds = EnumShippingOrderLifecycleActivity.getSOLifecycleActivityIDs(shippingOrderLifeCycleActivities);
            if (shippingOrderLifecycleCriteria == null) {
                shippingOrderLifecycleCriteria = criteria.createCriteria("shippingOrderLifecycles");
            }
            shippingOrderLifecycleCriteria.add(Restrictions.in("shippingOrderLifeCycleActivity.id", shippingOrderLifeCycleIds));
        }

        if (activityStartDate != null || activityEndDate != null) {
            if (shippingOrderLifecycleCriteria == null) {
                shippingOrderLifecycleCriteria = criteria.createCriteria("shippingOrderLifecycles");
            }
            shippingOrderLifecycleCriteria.add(Restrictions.between("activityDate", activityStartDate, activityEndDate));
        }

        // TODO: fix later after rewrite
        // criteria.setMaxResults(100);

        return criteria;
    }

}
