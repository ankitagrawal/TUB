package com.hk.web.action.admin.courier;

import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.ForwardResolution;
import com.hk.domain.order.ShippingOrder;

/**
 * Created by IntelliJ IDEA.
 * User: Neha
 * Date: Feb 6, 2013
 * Time: 7:24:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class CreateReverseOrderAction {

	ShippingOrder shippingOrder;
	Resolution pre(){

	  return new ForwardResolution("/pages/admin/createReverseOrder.jsp");
	}
}
