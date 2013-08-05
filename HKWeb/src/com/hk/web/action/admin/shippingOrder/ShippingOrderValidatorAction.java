package com.hk.web.action.admin.shippingOrder;

import java.util.*;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.constants.sku.EnumSkuItemStatus;
import com.hk.constants.sku.EnumSkuItemOwner;
import com.hk.core.search.ShippingOrderSearchCriteria;
import com.hk.domain.core.PaymentStatus;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.order.ShippingOrderStatus;
import com.hk.domain.sku.SkuItemLineItem;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.sku.SkuGroup;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.inventory.SkuGroupService;
import com.hk.pact.dao.sku.SkuItemLineItemDao;
import com.hk.web.action.admin.queue.ActionAwaitingQueueAction;

@Component
public class ShippingOrderValidatorAction extends BaseAction{
	
	private static Logger logger = LoggerFactory.getLogger(ShippingOrderValidatorAction.class);
	
	@Autowired
	ShippingOrderService shippingOrderService;

  @Autowired
  SkuItemLineItemDao skuItemLineItemDao;

  @Autowired
  SkuGroupService skuGroupService;

	
	@DefaultHandler
	public Resolution pre() {
		ShippingOrderSearchCriteria shippingOrderSearchCriteria = getShippingOrderSearchCriteria();
		List<ShippingOrder> shippingOrders = shippingOrderService.searchShippingOrders(shippingOrderSearchCriteria);
		
		shippingOrders = getSortedShippingOrders(shippingOrders);
		Set<ShippingOrder> sortedShippingOrdersSet = new HashSet<ShippingOrder>(shippingOrders);
		
		for(ShippingOrder shippingOrder : sortedShippingOrdersSet){
			logger.debug("Validating Shipping Order -"+shippingOrder.getId());
			try{
				shippingOrderService.validateShippingOrder(shippingOrder);
			}
			catch(Exception e)
			{
				logger.debug("Exception while validating the Shipping Order"+shippingOrder.getId()+" "+e.getMessage());
				e.printStackTrace();
			}
			
		}
		addRedirectAlertMessage(new SimpleMessage("Shipping Orders Validated and entries in tables adjusted"));
		return new RedirectResolution(ActionAwaitingQueueAction.class); 
		
		
	}

  public Resolution fixDuplicateSI(){
    long count = 0;
    List<SkuItemLineItem> skuItemLineItems = skuItemLineItemDao.getSkuItemLIsTemp();
    SkuItem oldSkuItem = null;
    SkuGroup group = null;
    List<SkuItem> skuItems = null;
    for (SkuItemLineItem skuItemLineItem : skuItemLineItems) {
      SkuItem skuItem = skuItemLineItem.getSkuItem();
      if (oldSkuItem == null || !oldSkuItem.equals(skuItem)) {
        group = skuItem.getSkuGroup();
        skuItems = skuGroupService.getSkuItems(Arrays.asList(group.getSku()),
            Arrays.asList(EnumSkuItemStatus.Checked_IN.getId()), Arrays.asList(EnumSkuItemOwner.SELF.getSkuItemOwnerStatus()), group.getMrp());
      }else{
        SkuItem newSkuItem = skuItems.get(0);
        newSkuItem.setSkuItemStatus(EnumSkuItemStatus.BOOKED.getSkuItemStatus());
        skuGroupService.saveSkuItem(newSkuItem);

        skuItemLineItem.setSkuItem(newSkuItem);
        skuItemLineItemDao.save(skuItemLineItem);

        count++;
      }
      oldSkuItem = skuItem;
    }

    addRedirectAlertMessage(new SimpleMessage("Duplicate SI fixed on SILI. Total fixed SILIs = "+count));
		return new RedirectResolution(ActionAwaitingQueueAction.class);
  }
	
	public ShippingOrderSearchCriteria getShippingOrderSearchCriteria() {
		ShippingOrderSearchCriteria shippingOrderSearchCriteria = new ShippingOrderSearchCriteria();
		List<ShippingOrderStatus> soStatusList = new ArrayList<ShippingOrderStatus>();
		soStatusList.add(EnumShippingOrderStatus.SO_ActionAwaiting.asShippingOrderStatus());
		soStatusList.add(EnumShippingOrderStatus.SO_OnHold.asShippingOrderStatus());
		soStatusList.add(EnumShippingOrderStatus.SO_Picking.asShippingOrderStatus());
		soStatusList.add(EnumShippingOrderStatus.SO_ReadyForDropShipping.asShippingOrderStatus());
		soStatusList.add(EnumShippingOrderStatus.SO_ReadyForProcess.asShippingOrderStatus());
		soStatusList.add(EnumShippingOrderStatus.SO_MarkedForPrinting.asShippingOrderStatus());
		soStatusList.add(EnumShippingOrderStatus.SO_EscalatedBack.asShippingOrderStatus());
		shippingOrderSearchCriteria.setShippingOrderStatusList(soStatusList);
		List<PaymentStatus> paymentStatusList = new ArrayList<PaymentStatus>();
		paymentStatusList.add(EnumPaymentStatus.SUCCESS.asPaymenStatus());
		paymentStatusList.add(EnumPaymentStatus.ON_DELIVERY.asPaymenStatus());
		paymentStatusList.add(EnumPaymentStatus.AUTHORIZATION_PENDING.asPaymenStatus());
		shippingOrderSearchCriteria.setPaymentStatuses(paymentStatusList);
		return shippingOrderSearchCriteria;
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
	
	public List<ShippingOrder> getSortedShippingOrders(List<ShippingOrder> shippingOrders) {
		Collections.sort(shippingOrders, new ShippingOrderComparator());
		return shippingOrders;
	}

}
