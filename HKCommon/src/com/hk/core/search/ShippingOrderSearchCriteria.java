package com.hk.core.search;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Courier;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.order.ShippingOrderStatus;

/**
 * @author vaibhav.adlakha
 */
public class ShippingOrderSearchCriteria extends AbstractOrderSearchCriteria {

    private List<Awb>                                awbList;
    private List<Courier>                            courierList;
    private Long                                     warehouseId;
    private boolean                                  isServiceOrder    = false;
    private Date                                     activityStartDate;
    private Date                                     activityEndDate;
    private String                                   basketCategory;
    private Long                                     baseOrderId;
    private String                                   baseGatewayOrderId;
    private Date                                     shipmentStartDate;
    private Date                                     shipmentEndDate;

    private List<EnumShippingOrderLifecycleActivity> shippingOrderLifeCycleActivities;
    private List<ShippingOrderStatus>                shippingOrderStatusList;

    private boolean                                  searchForPrinting = false;
	private Date                                     lastEscStartDate;
	private Date                                     lastEscEndDate;

    public ShippingOrderSearchCriteria setSearchForPrinting(boolean searchForPrinting) {
        this.searchForPrinting = searchForPrinting;
        return this;
    }

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

    public ShippingOrderSearchCriteria setAwbList(List<Awb> awbList) {
        this.awbList = awbList;
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

    public Date getShipmentStartDate() {
        return shipmentStartDate;
    }

    public ShippingOrderSearchCriteria setShipmentStartDate(Date shipmentStartDate) {
        this.shipmentStartDate = shipmentStartDate;
        return this;
    }

    public Date getShipmentEndDate() {
        return shipmentEndDate;
    }

    public ShippingOrderSearchCriteria setShipmentEndDate(Date shipmentEndDate) {
        this.shipmentEndDate = shipmentEndDate;
        return this;
    }

    protected DetachedCriteria buildSearchCriteriaFromBaseCriteria() {
        DetachedCriteria criteria = super.buildSearchCriteriaFromBaseCriteria();

		if(lastEscStartDate != null && lastEscEndDate != null) {
			criteria.add(Restrictions.between("lastEscDate", lastEscStartDate, lastEscEndDate));
		}

        if (shippingOrderStatusList != null && shippingOrderStatusList.size() > 0) {
            criteria.add(Restrictions.in("shippingOrderStatus", shippingOrderStatusList));
        }

	    DetachedCriteria shipmentCriteria = null;
	    if (awbList != null && awbList.size() > 0) {
		    shipmentCriteria = criteria.createCriteria("shipment");

		    shipmentCriteria.add(Restrictions.in("awb", awbList));
	    }

	    if (courierList != null && !courierList.isEmpty()) {
		    if (shipmentCriteria == null) {
			    DetachedCriteria shipCriteria = criteria.createCriteria("shipment");
			    shipmentCriteria = shipCriteria.createCriteria("awb");
		    }
		    shipmentCriteria.add(Restrictions.in("courier", courierList));
	    }

        if (shipmentStartDate != null && shipmentEndDate != null) {
            if (shipmentCriteria == null) {
                shipmentCriteria = criteria.createCriteria("shipment");
            }
            shipmentCriteria.add(Restrictions.between("shipDate", shipmentStartDate, shipmentEndDate));
        }

        if (StringUtils.isNotBlank(basketCategory)) {
            criteria.add(Restrictions.eq("basketCategory", basketCategory));
        }

        DetachedCriteria baseOrderCriteria = criteria.createCriteria("baseOrder");

        if (baseOrderId != null) {
            baseOrderCriteria.add(Restrictions.eq("id", baseOrderId));
        }

        if (baseGatewayOrderId != null) {
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

        DetachedCriteria paymentCriteria = baseOrderCriteria.createCriteria("payment", CriteriaSpecification.LEFT_JOIN);

        if (!searchForPrinting) {
            if (sortByPaymentDate) {
                paymentCriteria.addOrder(OrderBySqlFormula.sqlFormula("date(payment_date) asc"));

            }
            if (sortByScore) {
                baseOrderCriteria.addOrder(org.hibernate.criterion.Order.desc("score"));
            }
        }else{
            criteria.addOrder(org.hibernate.criterion.Order.asc("targetDispatchDate"));
            baseOrderCriteria.addOrder(org.hibernate.criterion.Order.desc("score"));
            criteria.addOrder(org.hibernate.criterion.Order.asc("lastEscDate"));
        }
        

        /*
         * paymentCriteria.addOrder(OrderBySqlFormula.sqlFormula("date(payment_date) asc"));
         * baseOrderCriteria.addOrder(org.hibernate.criterion.Order.desc("score"));
         */

        return criteria;
    }

	public Date getLastEscStartDate() {
		return lastEscStartDate;
	}

	public ShippingOrderSearchCriteria setLastEscStartDate(Date lastEscStartDate) {
		this.lastEscStartDate = lastEscStartDate;
		return this;
	}

	public Date getLastEscEndDate() {
		return lastEscEndDate;
	}

	public ShippingOrderSearchCriteria setLastEscEndDate(Date lastEscEndDate) {
		this.lastEscEndDate = lastEscEndDate;
		return this;
	}
}
