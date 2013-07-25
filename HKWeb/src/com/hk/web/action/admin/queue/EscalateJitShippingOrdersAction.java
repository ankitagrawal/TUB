package com.hk.web.action.admin.queue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.dao.inventory.PurchaseOrderDao;
import com.hk.constants.catalog.category.CategoryConstants;
import com.hk.constants.inventory.EnumPurchaseOrderStatus;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.core.search.ShippingOrderSearchCriteria;
import com.hk.domain.core.PaymentStatus;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.order.ShippingOrderLifeCycleActivity;
import com.hk.domain.order.ShippingOrderLifecycle;
import com.hk.domain.order.ShippingOrderStatus;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.shippingOrder.ShippingOrderLifecycleDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.splitter.ShippingOrderSplitter;

@Component
public class EscalateJitShippingOrdersAction extends BaseAction {

	private List<ShippingOrder> shippingOrderList = new ArrayList<ShippingOrder>();
	@Autowired
	ShippingOrderService shippingOrderService;
	@Autowired
	PurchaseOrderDao purchaseOrderDao;
	@Autowired
	BaseDao baseDao;
	@Autowired
	ProductVariantService productVariantService;
	@Autowired
	ShippingOrderLifecycleDao shippingOrderLifecycleDao;
	@Autowired
	private UserService userService;
	
	@Autowired
	private ShippingOrderSplitter shippingOrderSplitter;

	List<ShippingOrder> sortedShippingOrderList;

	@DefaultHandler
	public Resolution pre() {
		sortedShippingOrderList = new ArrayList<ShippingOrder>();
		ShippingOrderSearchCriteria shippingOrderSearchCriteria = getShippingOrderSearchCriteria();
		shippingOrderList = shippingOrderService.searchShippingOrders(shippingOrderSearchCriteria);
		Set<ShippingOrder> shippingOrderListToProcess = new HashSet<ShippingOrder>();
		if (shippingOrderList != null && shippingOrderList.size() > 0) {

			for (ShippingOrder shippingOrder : shippingOrderList) {
				if (shippingOrder.getPurchaseOrders() != null && shippingOrder.getPurchaseOrders().size() > 0) {
					boolean canAdd = true;
					for(LineItem item: shippingOrder.getLineItems()){
						if(item.getSku().getProductVariant().getProduct().getPrimaryCategory().getName().equals(CategoryConstants.EYE)){
							canAdd = false;
							break;
						}
					}
					if(canAdd)
					shippingOrderListToProcess.add(shippingOrder);
				}
			}
		}

		sortedShippingOrderList.addAll(shippingOrderListToProcess);
		sortedShippingOrderList = getSortedShippingOrders();
		Set<ShippingOrder> sortedShippingOrdersSet = new HashSet<ShippingOrder>(sortedShippingOrderList);

		int ctr = 0;

		for (ShippingOrder shippingOrder : sortedShippingOrdersSet) {
			if(shippingOrder.getId().intValue()==1639052){
				System.out.println(shippingOrder.getId());
			}
			List<PurchaseOrder> poList = shippingOrder.getPurchaseOrders();
			boolean flag = true;
			if (poList != null && poList.size() > 0) {
				for (PurchaseOrder po : poList) {
					if (!po.getPurchaseOrderStatus().equals(EnumPurchaseOrderStatus.Received.asEnumPurchaseOrderStatus())) {
						flag = false;
						break;
					}
				}
			} else {
				flag = false;
			}
			if (flag) {
				shippingOrder = shippingOrderSplitter.manualEscalateShippingOrder(shippingOrder);
				ShippingOrderLifecycle shippingOrderLifecycle = new ShippingOrderLifecycle();
				shippingOrderLifecycle.setOrder(shippingOrder);
				shippingOrderLifecycle.setShippingOrderLifeCycleActivity(getBaseDao().get(ShippingOrderLifeCycleActivity.class,
						EnumShippingOrderLifecycleActivity.SO_PO_RECEIVED.getId()));
				shippingOrderLifecycle.setUser(userService.getAdminUser());
				shippingOrderLifecycle.setComments("Tried to manually escalate as PO against the shipping order served.");
				shippingOrderLifecycle.setActivityDate(new Date());
				shippingOrderLifecycleDao.save(shippingOrderLifecycle);
				ctr++;
			}
		}


		addRedirectAlertMessage(new SimpleMessage("Tried to Escalate "+ctr+ " Shipping Orders"));
		return new RedirectResolution(ActionAwaitingQueueAction.class);

	}

	public ShippingOrderSearchCriteria getShippingOrderSearchCriteria() {
		ShippingOrderSearchCriteria shippingOrderSearchCriteria = new ShippingOrderSearchCriteria();
		List<ShippingOrderStatus> soStatusList = new ArrayList<ShippingOrderStatus>();
		soStatusList.add(EnumShippingOrderStatus.SO_ActionAwaiting.asShippingOrderStatus());
		soStatusList.add(EnumShippingOrderStatus.SO_OnHold.asShippingOrderStatus());
		List<PaymentStatus> paymentStatusList = new ArrayList<PaymentStatus>();
		paymentStatusList.add(EnumPaymentStatus.SUCCESS.asPaymenStatus());
		paymentStatusList.add(EnumPaymentStatus.ON_DELIVERY.asPaymenStatus());
		shippingOrderSearchCriteria.setShippingOrderStatusList(soStatusList);
		return shippingOrderSearchCriteria;
	}

	public List<ShippingOrder> getShippingOrderList() {
		return shippingOrderList;
	}

	public void setShippingOrderList(List<ShippingOrder> shippingOrderList) {
		this.shippingOrderList = shippingOrderList;
	}

	public List<ShippingOrder> getSortedShippingOrderList() {
		return sortedShippingOrderList;
	}

	public void setSortedShippingOrderList(List<ShippingOrder> sortedShippingOrderList) {
		this.sortedShippingOrderList = sortedShippingOrderList;
	}

	public class ShippingOrderComparator implements Comparator<ShippingOrder> {

		@Override
		public int compare(ShippingOrder o1, ShippingOrder o2) {
			if (o1.getCreateDate() != null && o2.getCreateDate() != null) {
				return o1.getCreateDate().compareTo(o2.getCreateDate());
			}
			return -1;
		}

	}

	public List<ShippingOrder> getSortedShippingOrders() {
		Collections.sort(sortedShippingOrderList, new ShippingOrderComparator());
		return sortedShippingOrderList;
	}

	/**
	 * @return the shippingOrderSplitter
	 */
	public ShippingOrderSplitter getShippingOrderSplitter() {
		return shippingOrderSplitter;
	}

	/**
	 * @param shippingOrderSplitter the shippingOrderSplitter to set
	 */
	public void setShippingOrderSplitter(ShippingOrderSplitter shippingOrderSplitter) {
		this.shippingOrderSplitter = shippingOrderSplitter;
	}

}
