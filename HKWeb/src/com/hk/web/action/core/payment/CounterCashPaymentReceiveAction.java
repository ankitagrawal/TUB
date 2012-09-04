package com.hk.web.action.core.payment;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.util.BaseUtils;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.domain.core.PaymentMode;
import com.hk.domain.order.Order;
import com.hk.domain.payment.Payment;
import com.hk.domain.user.User;
import com.hk.exception.HealthkartPaymentGatewayException;
import com.hk.manager.OrderManager;
import com.hk.manager.payment.PaymentManager;
import com.hk.pact.service.RoleService;
import com.hk.pact.service.UserService;
import com.hk.pact.service.payment.PaymentService;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA. User: rahul Date: Nov 25, 2011 Time: 12:50:58 PM To change this template use File |
 * Settings | File Templates.
 */
@Component
public class CounterCashPaymentReceiveAction extends BaseAction {
	@Autowired
	private PaymentManager paymentManager;
	@Autowired
	private PaymentService paymentService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private UserService userService;
	@Autowired
	private OrderManager orderManager;

	// PaymentModeDao paymentModeDao;

	// RoleDao roleDao;

	// UserDao userDao;

	@Validate(required = true)
	private Order order;

	@Validate(required = true)
	private Long paymentMode;

	private User user;

	public Resolution pre() {
		Resolution resolution = null;
		if (order.getOrderStatus().getId().equals(EnumOrderStatus.InCart.getId())) {
			// recalculate the pricing before creating a payment.
			order = getOrderManager().recalAndUpdateAmount(order);
			// first create a payment row, this will also cotain the payment checksum
			Payment payment = paymentManager.createNewPayment(order, getPaymentService().findPaymentMode(paymentMode), BaseUtils.getRemoteIpAddrForUser(getContext()), null);
			String gatewayOrderId = payment.getGatewayOrderId();
			try {
				getPaymentManager().verifyPayment(gatewayOrderId, order.getAmount(), null);
				getPaymentManager().counterCashSuccess(gatewayOrderId, getBaseDao().get(PaymentMode.class, paymentMode));
				resolution = new RedirectResolution(PaymentSuccessAction.class).addParameter("gatewayOrderId", gatewayOrderId);
			} catch (HealthkartPaymentGatewayException e) {
				getPaymentManager().error(gatewayOrderId, e);
				resolution = e.getRedirectResolution().addParameter("gatewayOrderId", gatewayOrderId);
			}
		} else {
			addRedirectAlertMessage(new SimpleMessage("Payment for the order is already made."));
			resolution = new RedirectResolution(PaymentModeAction.class).addParameter("order", order);
		}
		return resolution;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Long getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(Long paymentMode) {
		this.paymentMode = paymentMode;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public PaymentManager getPaymentManager() {
		return paymentManager;
	}

	public void setPaymentManager(PaymentManager paymentManager) {
		this.paymentManager = paymentManager;
	}

	public PaymentService getPaymentService() {
		return paymentService;
	}

	public void setPaymentService(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

	public RoleService getRoleService() {
		return roleService;
	}

	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public OrderManager getOrderManager() {
		return orderManager;
	}

	public void setOrderManager(OrderManager orderManager) {
		this.orderManager = orderManager;
	}

}
