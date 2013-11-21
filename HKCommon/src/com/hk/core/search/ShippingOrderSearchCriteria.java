package com.hk.core.search;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.hk.domain.analytics.Reason;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.core.OrderStatus;
import com.hk.domain.core.PaymentMode;
import com.hk.domain.core.PaymentStatus;
import com.hk.domain.courier.Zone;
import com.hk.domain.order.ShippingOrderLifeCycleActivity;
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

	private List<Awb> awbList;
	private List<Courier> courierList;
	private Long warehouseId;

	private Boolean isServiceOrder = null;
	private Boolean dropShip;
	private Boolean containsJit;
	private Boolean b2bOrder;
	private Integer userCodCallStatus;

	private Date activityStartDate;
	private Date activityEndDate;
	private String basketCategory;
	private Long baseOrderId;
	private String baseGatewayOrderId;
	private Date shipmentStartDate;
	private Date shipmentEndDate;
	private Date paymentStartDate;
	private Date paymentEndDate;

	private List<ShippingOrderLifeCycleActivity> shippingOrderLifeCycleActivities;
	private List<ShippingOrderStatus> shippingOrderStatusList;
	private List<PaymentStatus> paymentStatuses;
	private List<PaymentMode> paymentModes;
	private List<Reason> reasonList;

	private boolean searchForPrinting = false;
	private Date lastEscStartDate;
	private Date lastEscEndDate;
	private Date startTargetDispatchDate;
	private Date endTargetDispatchDate;
	private Zone zone;
	private Set<Category> shippingOrderCategories;
	private boolean dropShipping = false;
	private boolean containsJitProducts = false;
	private boolean installable = false;
	private Date shippingOrderCreateDate;
	private Long brightSoId;

	public ShippingOrderSearchCriteria setSearchForPrinting(boolean searchForPrinting) {
		this.searchForPrinting = searchForPrinting;
		return this;
	}

	public ShippingOrderSearchCriteria setShippingOrderLifeCycleActivities(List<ShippingOrderLifeCycleActivity> shippingOrderLifeCycleActivities) {
		this.shippingOrderLifeCycleActivities = shippingOrderLifeCycleActivities;
		return this;
	}

	public ShippingOrderSearchCriteria setReasonList(List<Reason> reasonList) {
		this.reasonList = reasonList;
		return this;
	}

	public ShippingOrderSearchCriteria setShippingOrderStatusList(List<ShippingOrderStatus> shippingOrderStatusList) {
		this.shippingOrderStatusList = shippingOrderStatusList;
		return this;
	}

	public ShippingOrderSearchCriteria setServiceOrder(Boolean serviceOrder) {
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

	public ShippingOrderSearchCriteria setPaymentStartDate(Date paymentStartDate) {
		this.paymentStartDate = paymentStartDate;
		return this;
	}

	public ShippingOrderSearchCriteria setPaymentEndDate(Date paymentEndDate) {
		this.paymentEndDate = paymentEndDate;
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

	public ShippingOrderSearchCriteria setPaymentStatuses(List<PaymentStatus> paymentStatuses) {
		this.paymentStatuses = paymentStatuses;
		return this;
	}

	public ShippingOrderSearchCriteria setPaymentModes(List<PaymentMode> paymentModes) {
		this.paymentModes = paymentModes;
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

	public ShippingOrderSearchCriteria setShipmentZone(Zone zone) {
		this.zone = zone;
		return this;
	}

	public ShippingOrderSearchCriteria setShippingOrderCategories(Set<Category> shippingOrderCategories) {
		this.shippingOrderCategories = shippingOrderCategories;
		return this;
	}

	protected DetachedCriteria buildSearchCriteriaFromBaseCriteria() {
		DetachedCriteria criteria = super.buildSearchCriteriaFromBaseCriteria();

		if (lastEscStartDate != null && lastEscEndDate != null) {
			criteria.add(Restrictions.between("lastEscDate", lastEscStartDate, lastEscEndDate));
		}

		if (shippingOrderCreateDate != null) {
			criteria.add(Restrictions.ge("createDate", shippingOrderCreateDate));
		}

		if (startTargetDispatchDate != null && endTargetDispatchDate != null) {
			criteria.add(Restrictions.between("targetDispatchDate", startTargetDispatchDate, endTargetDispatchDate));
		}

		if (shippingOrderStatusList != null && shippingOrderStatusList.size() > 0) {
			criteria.add(Restrictions.in("shippingOrderStatus", shippingOrderStatusList));
		}

		DetachedCriteria shipmentCriteria = null;
		if (awbList != null && awbList.size() > 0) {
			shipmentCriteria = criteria.createCriteria("shipment");

			shipmentCriteria.add(Restrictions.in("awb", awbList));
		}

		if (zone != null) {
			if (shipmentCriteria == null) {
				shipmentCriteria = criteria.createCriteria("shipment");
			}
			shipmentCriteria.add(Restrictions.eq("zone.id", zone.getId()));
		}

		DetachedCriteria awbCriteria = null;
		if (courierList != null && !courierList.isEmpty()) {
			if (shipmentCriteria == null) {
				shipmentCriteria = criteria.createCriteria("shipment");
			}
			awbCriteria = shipmentCriteria.createCriteria("awb");
			awbCriteria.add(Restrictions.in("courier", courierList));
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

		if (userCodCallStatus != null) {
			DetachedCriteria codCallCriteria = baseOrderCriteria.createCriteria("userCodCall");
			codCallCriteria.add(Restrictions.eq("callStatus", userCodCallStatus));
		}

		DetachedCriteria warehouseCriteria = null;
		if (warehouseId != null) {
			warehouseCriteria = criteria.createCriteria("warehouse");
			warehouseCriteria.add(Restrictions.eq("id", warehouseId));
		}
		if (isServiceOrder != null) {
			criteria.add(Restrictions.eq("isServiceOrder", isServiceOrder));
		}
		if (dropShip != null) {
			criteria.add(Restrictions.eq("isDropShipping", dropShip));
		}
		if (b2bOrder != null) {
			criteria.add(Restrictions.eq("b2bOrder", b2bOrder));
		}
		if (containsJit != null) {
			criteria.add(Restrictions.eq("containsJitProducts", containsJit));
		}

		DetachedCriteria shippingOrderLifecycleCriteria = null;
		if (shippingOrderLifeCycleActivities != null && shippingOrderLifeCycleActivities.size() > 0) {
			if (shippingOrderLifecycleCriteria == null) {
				shippingOrderLifecycleCriteria = criteria.createCriteria("shippingOrderLifecycles");
			}
			shippingOrderLifecycleCriteria.add(Restrictions.in("shippingOrderLifeCycleActivity", shippingOrderLifeCycleActivities));
			DetachedCriteria lifecycleCriteria = null;
			if (reasonList != null && !reasonList.isEmpty()) {
				if (lifecycleCriteria == null) {
					lifecycleCriteria = shippingOrderLifecycleCriteria.createCriteria("lifecycleReasons");
				}
				lifecycleCriteria.add(Restrictions.in("reason", reasonList));
			}
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

		if (paymentStatuses != null && paymentStatuses.size() > 0) {
			paymentCriteria.add(Restrictions.in("paymentStatus", paymentStatuses));
		}
		if (paymentModes != null && paymentModes.size() > 0) {
			paymentCriteria.add(Restrictions.in("paymentMode", paymentModes));
		}

		if (paymentStartDate != null || paymentEndDate != null) {
			paymentCriteria.add(Restrictions.between("paymentDate", paymentStartDate, paymentEndDate));
		}

		if (!searchForPrinting) {
			if (sortByDispatchDate) {
				baseOrderCriteria.addOrder(org.hibernate.criterion.Order.desc("targetDispatchDate"));
			}
			if (sortByLastEscDate) {
				criteria.addOrder(org.hibernate.criterion.Order.desc("lastEscDate"));
			}
			if (sortByPaymentDate) {
				paymentCriteria.addOrder(OrderBySqlFormula.sqlFormula("date(payment_date) asc"));
			}
			if (sortByScore) {
				baseOrderCriteria.addOrder(org.hibernate.criterion.Order.desc("score"));
			}
		} else {
			criteria.addOrder(org.hibernate.criterion.Order.asc("targetDispatchDate"));
			// baseOrderCriteria.addOrder(org.hibernate.criterion.Order.desc("score"));
			criteria.addOrder(org.hibernate.criterion.Order.asc("lastEscDate"));
		}

		DetachedCriteria shippingOrderCategoryCriteria = null;
		if (shippingOrderCategories != null && !shippingOrderCategories.isEmpty()) {
			if (shippingOrderCategoryCriteria == null) {
				shippingOrderCategoryCriteria = criteria.createCriteria("shippingOrderCategories");
			}
			shippingOrderCategoryCriteria.add(Restrictions.in("category", shippingOrderCategories));
		}

		/*
		 * if (shippingOrderCategories != null &&
		 * !shippingOrderCategories.isEmpty()) {
		 * criteria.add(Restrictions.in("basketCategory", shippingOrderCategories));
		 * }
		 */

		DetachedCriteria lineItemsCriteria = null;
		DetachedCriteria skuCriteria = null;
		DetachedCriteria productVariantCriteria = null;
		DetachedCriteria productCriteria = null;

		if (isDropShipping()) {
			criteria.add(Restrictions.eq("isDropShipping", dropShipping));
		}
		if (containsJitProducts()) {
			criteria.add(Restrictions.eq("containsJitProducts", containsJitProducts));
		}

		if (isInstallable()) {
			lineItemsCriteria = criteria.createCriteria("lineItems");
			skuCriteria = lineItemsCriteria.createCriteria("sku");
			productVariantCriteria = skuCriteria.createCriteria("productVariant");
			productCriteria = productVariantCriteria.createCriteria("product");
			productCriteria.add(Restrictions.eq("installable", true));
		}

		/*
		 * paymentCriteria.addOrder(OrderBySqlFormula.sqlFormula(
		 * "date(payment_date) asc"));
		 * baseOrderCriteria.addOrder(org.hibernate.criterion.Order.desc("score"));
		 */

		DetachedCriteria fsicliCriteria = null;
		if (brightSoId != null) {
			fsicliCriteria = criteria.createCriteria("lineItems").createAlias("cartLineItem", "cl")
					.createAlias("cl.foreignSkuItemCLIs", "fi").add(Restrictions.eq("fi.foreignShippingOrderId", brightSoId));
		}

		return criteria;
	}

	public boolean containsJitProducts() {
		return containsJitProducts;
	}

	public void setContainsJitProducts(boolean containsJitProducts) {
		this.containsJitProducts = containsJitProducts;
	}

	public Date getLastEscStartDate() {
		return lastEscStartDate;
	}

	public boolean isDropShipping() {
		return dropShipping;
	}

	public void setDropShipping(boolean dropShipping) {
		this.dropShipping = dropShipping;
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

	public boolean isInstallable() {
		return installable;
	}

	public void setInstallable(boolean installable) {
		this.installable = installable;
	}

	public ShippingOrderSearchCriteria setStartTargetDispatchDate(Date startTargetDispatchDate) {
		this.startTargetDispatchDate = startTargetDispatchDate;
		return this;
	}

	public ShippingOrderSearchCriteria setEndTargetDispatchDate(Date endTargetDispatchDate) {
		this.endTargetDispatchDate = endTargetDispatchDate;
		return this;
	}

	public void setDropShip(Boolean dropShip) {
		this.dropShip = dropShip;
	}

	public void setContainsJit(Boolean containsJit) {
		this.containsJit = containsJit;
	}

	public void setB2bOrder(Boolean b2bOrder) {
		this.b2bOrder = b2bOrder;
	}

	public void setUserCodCallStatus(Integer userCodCallStatus) {
		this.userCodCallStatus = userCodCallStatus;
	}

	public Date getShippingOrderCreateDate() {
		return shippingOrderCreateDate;
	}

	public void setShippingOrderCreateDate(Date shippingOrderCreateDate) {
		this.shippingOrderCreateDate = shippingOrderCreateDate;
	}

	public Long getBrightSoId() {
		return brightSoId;
	}

	public void setBrightSoId(Long brightSoId) {
		this.brightSoId = brightSoId;
	}
}
