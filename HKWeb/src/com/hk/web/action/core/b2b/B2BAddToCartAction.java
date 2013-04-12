package com.hk.web.action.core.b2b;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.controller.JsonHandler;
import com.hk.constants.core.RoleConstants;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.matcher.CartLineItemMatcher;
import com.hk.domain.order.B2BOrderChecklist;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;
import com.hk.exception.OutOfStockException;
import com.hk.manager.OrderManager;
import com.hk.pact.dao.user.UserDao;
import com.hk.pact.service.order.B2BOrderService;
import com.hk.pact.service.order.CartLineItemService;
import com.hk.pact.service.order.OrderService;
import com.hk.web.HealthkartResponse;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.JsonResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationErrorHandler;
import net.sourceforge.stripes.validation.ValidationErrors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Nihal
 */

@Secure(hasAnyRoles = { RoleConstants.B2B_USER }, authActionBean = AdminPermissionAction.class)
@Component
public class B2BAddToCartAction extends BaseAction implements ValidationErrorHandler {

	private static Logger logger = LoggerFactory.getLogger(B2BAddToCartAction.class);

	private List<ProductVariant> productVariantList;

	@Autowired
	private OrderService orderService;
	@Autowired
	private CartLineItemService cartLineItemService;
	@Autowired
	private UserDao userDao;
	@Autowired
	private OrderManager orderManager;

	private boolean cFormAvailable;

	@Autowired
	B2BOrderService b2bOrderService;

	private Order order;

	@DefaultHandler
	@JsonHandler
	public Resolution b2bAddToCart() {
		Map dataMap = new HashMap();
		HealthkartResponse healthkartResponse;
		User user = null;
		if (getPrincipal() != null) {
			user = userDao.getUserById(getPrincipal().getId());
			if (user == null) {
				healthkartResponse = new HealthkartResponse("null_exception", "No user", dataMap);
				return new JsonResolution(healthkartResponse);
			}
		}

		order = orderManager.getOrCreateOrder(user);
		// cFormAvailable = getB2bOrderService().checkCForm(order);

		try {

			B2BOrderChecklist b2bOrderCheckList = new B2BOrderChecklist();
			b2bOrderCheckList.setBaseOrder(order);
			b2bOrderCheckList.setCForm(isCFormAvailable());
			getB2bOrderService().saveB2BOrder(b2bOrderCheckList);
			List<ProductVariant> pvToBeAdded = new ArrayList<ProductVariant>();
			for (ProductVariant productVariant : productVariantList) {
				CartLineItem cartLineItem = getCartLineItemService().getMatchingCartLineItemFromOrder(order,
						(new CartLineItemMatcher()).addProductVariant(productVariant));
				if (cartLineItem != null) {
					if (!cartLineItem.getQty().equals(productVariant.getQty())) {
						cartLineItem.setQty(productVariant.getQty());
						getCartLineItemService().save(cartLineItem);
					}
				} else {
					if (productVariant.getQty() > 0) {
						pvToBeAdded.add(productVariant);
					}
				}
			}

			orderManager.createLineItems(pvToBeAdded, order, null, null, null);
			orderManager.trimEmptyLineItems(order);

		} catch (OutOfStockException e) {
			getContext().getValidationErrors().add("e2", new SimpleError(e.getMessage()));
			return new JsonResolution(getContext().getValidationErrors(), getContext().getLocale());
		} catch (NullPointerException e) {
			healthkartResponse = new HealthkartResponse("null_exception",
					"Cart could not be updated | Invalid Product Ids", dataMap);
			return new JsonResolution(healthkartResponse);
		}
		dataMap.put("itemsInCart", Long.valueOf(order.getExclusivelyProductCartLineItems().size()));
		healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK,
				"Product has been added to cart, Check your form", dataMap);
		noCache();
		return new JsonResolution(healthkartResponse);

	}

	public Resolution cancel() {
		return new RedirectResolution(B2BCartAction.class);
	}

	public boolean isCFormAvailable() {
		return cFormAvailable;
	}

	public boolean getCFormAvailable() {
		return cFormAvailable;
	}

	public void setCFormAvailable(boolean cFormAvailable) {
		this.cFormAvailable = cFormAvailable;
	}

	public B2BOrderService getB2bOrderService() {
		return b2bOrderService;
	}

	public void setB2bOrderService(B2BOrderService b2bOrderService) {
		this.b2bOrderService = b2bOrderService;
	}

	public List<ProductVariant> getProductVariantList() {
		return productVariantList;
	}

	public void setProductVariantList(List<ProductVariant> productVariantList) {
		this.productVariantList = productVariantList;
	}

	public Resolution handleValidationErrors(ValidationErrors validationErrors) throws Exception {
		return new JsonResolution(validationErrors, getContext().getLocale());
	}

	public OrderService getOrderService() {
		return orderService;
	}

	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}

	public CartLineItemService getCartLineItemService() {
		return cartLineItemService;
	}

	public void setCartLineItemService(CartLineItemService cartLineItemService) {
		this.cartLineItemService = cartLineItemService;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

}
