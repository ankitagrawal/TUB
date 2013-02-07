package com.hk.web.action.admin.courier;

import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.ForwardResolution;
import com.hk.domain.order.ShippingOrder;
import com.akube.framework.stripes.action.BaseAction;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * User: Neha
 * Date: Feb 6, 2013
 * Time: 7:24:17 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class CreateReverseOrderAction extends BaseAction {

	ShippingOrder shippingOrder;

	Resolution pre(){

	  return new ForwardResolution("/pages/admin/createReverseOrder.jsp");
	}

	public ShippingOrder getShippingOrder() {
		return shippingOrder;
	}

	public void setShippingOrder(ShippingOrder shippingOrder) {
		this.shippingOrder = shippingOrder;
	}
}
