package com.hk.web.action.core.b2b;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.JsonResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationErrorHandler;
import net.sourceforge.stripes.validation.ValidationErrors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.controller.JsonHandler;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.catalog.product.combo.Combo;
import com.hk.domain.matcher.CartLineItemMatcher;
import com.hk.domain.order.B2bOrderChecklist;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.sku.Sku;
import com.hk.domain.user.User;
import com.hk.exception.OutOfStockException;
import com.hk.manager.OrderManager;
import com.hk.manager.UserManager;
import com.hk.pact.dao.catalog.combo.ComboInstanceDao;
import com.hk.pact.dao.catalog.combo.ComboInstanceHasProductVariantDao;
import com.hk.pact.dao.inventory.ProductVariantInventoryDao;
import com.hk.pact.dao.user.UserCartDao;
import com.hk.pact.dao.user.UserDao;
import com.hk.pact.dao.user.UserProductHistoryDao;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.pact.service.order.B2BOrderService;
import com.hk.pact.service.order.CartLineItemService;
import com.hk.pact.service.order.OrderService;
import com.hk.web.HealthkartResponse;
import com.hk.web.action.core.cart.AddToCartAction;
import com.hk.web.action.core.user.SignupAction;

/**
 * 
 * @author Nihal
 * 
 */

@Component
public class B2BAddToCartAction extends BaseAction implements ValidationErrorHandler {

	private static Logger logger = LoggerFactory.getLogger(AddToCartAction.class);

	List<ProductVariant> productVariantList;
	Combo combo;

	@Autowired
	UserDao userDao;
	@Autowired
	UserManager userManager;
	@Autowired
	OrderManager orderManager;
	@Autowired
	ComboInstanceDao comboInstanceDao;
	@Autowired
	ComboInstanceHasProductVariantDao comboInstanceHasProductVariantDao;
	@Autowired
	UserCartDao userCartDao;
	@Autowired
	private OrderService orderService;
	@Autowired
	private CartLineItemService cartLineItemService;
	@Autowired
	UserProductHistoryDao userProductHistoryDao;
	@Autowired
	private SkuService skuService;
	private Long productReferrerId;
	@Autowired
	SignupAction signupAction;
	@Autowired
	private ProductVariantService productVariantService;

	private boolean variantConfigProvided;

	private String variantId;

	private List<String> productVariantIdList;

	private String jsonConfigValues;

	private String nameToBeEngraved;

	private String engravingRequired;

	private List<B2BProduct> b2bProductList;
	private Map<String, B2BProduct> mapB2bProduct;

	private boolean cFormAvailable;
	@Autowired
	ProductVariantInventoryDao productVariantInventoryDao;
	
	@Autowired
	B2BOrderService b2bOrderService;

	private Order order;

	@DefaultHandler
	@JsonHandler
	public Resolution b2bAddToCart() {

		User user = null;
		if (getPrincipal() != null) {
			user = userDao.getUserById(getPrincipal().getId());
			if (user == null) {
				user = userManager.createAndLoginAsGuestUser(null, null);
			}
		} else {
			user = userManager.createAndLoginAsGuestUser(null, null);
		}

		order = orderManager.getOrCreateOrder(user);
		//cFormAvailable = getB2bOrderService().checkCForm(order);
		Map dataMap = new HashMap();
		HealthkartResponse healthkartResponse;
		List<String> notAvailableList = new ArrayList<String>();
		try {

			b2bProductList.removeAll(Collections.singleton(null));

			Iterator<B2BProduct> b2bProductListIterator = b2bProductList.iterator();
			while (b2bProductListIterator.hasNext()) {
				B2BProduct b2bProduct = b2bProductListIterator.next();
				if (b2bProduct.getProductId() == null) {
					b2bProductListIterator.remove();
				}
			}

			for (B2BProduct b2bProduct : b2bProductList) {
				if (productVariantService.getVariantById(b2bProduct.getProductId()) == null) {
					throw new NullPointerException("Invalid Product Id Submitted");
				}
			}

			Set<CartLineItem> setCartLineItem = order.getCartLineItems();
			Map<String, CartLineItem> cartMap = new HashMap<String, CartLineItem>();
			for (CartLineItem cartLineItem : setCartLineItem) {
				cartMap.put(cartLineItem.getProductVariant().getId(), cartLineItem);
			}

			mapB2bProduct = new HashMap<String, B2BProduct>();

			for (B2BProduct b2bProduct : b2bProductList) {

				if (mapB2bProduct.get(b2bProduct.getProductId()) != null) {

					mapB2bProduct.get(b2bProduct.getProductId()).setQuantity(
							mapB2bProduct.get(b2bProduct.getProductId()).getQuantity() + b2bProduct.getQuantity());
				} else {
					mapB2bProduct.put(b2bProduct.getProductId(), b2bProduct);
				}
			}

			B2bOrderChecklist b2bOrderCheckList = new B2bOrderChecklist();
			b2bOrderCheckList.setBaseOrder(order);
			b2bOrderCheckList.setCForm(iscFormAvailable());
			getB2bOrderService().saveB2BOrder(b2bOrderCheckList);
			
			
			List<ProductVariant> variants = new ArrayList<ProductVariant>();
			for (B2BProduct b2bProduct : b2bProductList) {
				CartLineItem cartLineItem = cartMap.get(b2bProduct.getProductId());
				if (cartLineItem != null) {
					cartLineItem.setQty((long) mapB2bProduct.get(b2bProduct.getProductId()).getQuantity());
					cartLineItemService.save(cartLineItem);
				} else {
					ProductVariant variant = null;
					variant = productVariantService.getVariantById(b2bProduct.getProductId());
					variant.setQty((long) b2bProduct.getQuantity());
					if (variant.getQty() > 0l) {
						variants.add(variant);
					}
				}

			}

			orderManager.createLineItems(variants, order, null, null, null);
			

		} catch (OutOfStockException e) {
			getContext().getValidationErrors().add("e2", new SimpleError(e.getMessage()));
			return new JsonResolution(getContext().getValidationErrors(), getContext().getLocale());
		} catch (NullPointerException e) {
			healthkartResponse = new HealthkartResponse("null_exception",
					"Cart could not be updated | Invalid Product Ids", dataMap);
			return new JsonResolution(healthkartResponse);
		}

		Set<CartLineItem> subscriptionCartLineItems = new CartLineItemFilter(order.getCartLineItems())
				.addCartLineItemType(EnumCartLineItemType.Subscription).filter();
		dataMap.put("itemsInCart", subscriptionCartLineItems.size() + 1L);
		healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK,
				"Product has been added to cart, Check your form", dataMap);
		noCache();
		return new JsonResolution(healthkartResponse);

	}

	public boolean iscFormAvailable() {
		return cFormAvailable;
	}

	public void setcFormAvailable(boolean cFormAvailable) {
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

	public Combo getCombo() {
		return combo;
	}

	public void setCombo(Combo combo) {
		this.combo = null;
	}

	public String getNameToBeEngraved() {
		return nameToBeEngraved;
	}

	public void setNameToBeEngraved(String nameToBeEngraved) {
		this.nameToBeEngraved = nameToBeEngraved;
	}

	public boolean isVariantConfigProvided() {
		return variantConfigProvided;
	}

	public void setVariantConfigProvided(boolean variantConfigProvided) {
		this.variantConfigProvided = variantConfigProvided;
	}

	public String getVariantId() {
		return variantId;
	}

	public void setVariantId(String variantId) {
		this.variantId = variantId;
	}

	public String getJsonConfigValues() {
		return jsonConfigValues;
	}

	public void setJsonConfigValues(String configValue) {
		this.jsonConfigValues = configValue;
	}

	public String getEngravingRequired() {
		return engravingRequired;
	}

	public void setEngravingRequired(String engravingRequired) {
		this.engravingRequired = engravingRequired;
	}

	public Long getProductReferrerId() {
		return productReferrerId;
	}

	public void setProductReferrerId(Long productReferrerId) {
		this.productReferrerId = productReferrerId;
	}

	public ComboInstanceDao getComboInstanceDao() {
		return comboInstanceDao;
	}

	public List<String> getProductVariantIdList() {
		return productVariantIdList;
	}

	public void setProductVariantIdList(List<String> productVariantIdList) {
		this.productVariantIdList = productVariantIdList;
	}

	public List<B2BProduct> getB2bProductList() {
		return b2bProductList;
	}

	public void setB2bProductList(List<B2BProduct> b2bProductList) {
		this.b2bProductList = b2bProductList;
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

	public SkuService getSkuService() {
		return skuService;
	}

	public void setSkuService(SkuService skuService) {
		this.skuService = skuService;
	}

	public ProductVariantInventoryDao getProductVariantInventoryDao() {
		return productVariantInventoryDao;
	}

	public void setProductVariantInventoryDao(ProductVariantInventoryDao productVariantInventoryDao) {
		this.productVariantInventoryDao = productVariantInventoryDao;
	}

}
