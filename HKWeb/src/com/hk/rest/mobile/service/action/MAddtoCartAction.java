package com.hk.rest.mobile.service.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import net.sourceforge.stripes.action.DefaultHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.gson.JsonUtils;
import com.akube.framework.stripes.controller.JsonHandler;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.catalog.product.combo.Combo;
import com.hk.domain.catalog.product.combo.ComboInstance;
import com.hk.domain.catalog.product.combo.ComboInstanceHasProductVariant;
import com.hk.domain.catalog.product.combo.ComboProduct;
import com.hk.domain.marketing.ProductReferrer;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;
import com.hk.exception.OutOfStockException;
import com.hk.manager.OrderManager;
import com.hk.manager.UserManager;
import com.hk.pact.dao.catalog.combo.ComboInstanceDao;
import com.hk.pact.dao.catalog.combo.ComboInstanceHasProductVariantDao;
import com.hk.pact.dao.user.UserCartDao;
import com.hk.pact.dao.user.UserProductHistoryDao;
import com.hk.pact.service.catalog.ProductService;
import com.hk.rest.mobile.service.utils.MHKConstants;
import com.hk.web.HealthkartResponse;
import com.hk.web.action.core.user.SignupAction;

@Path("/mAddtoCart")
@Component
public class MAddtoCartAction extends MBaseAction {

	private static Logger logger = LoggerFactory
			.getLogger(MAddtoCartAction.class);

	List<ProductVariant> productVariantList;
	Combo combo;

	//@Autowired
	//UserDao userDao;
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
	UserProductHistoryDao userProductHistoryDao;
	@Autowired
	SignupAction signupAction;

	@Autowired
	ProductService productService;

	private boolean variantConfigProvided;

	private String variantId;

	private String jsonConfigValues;

	private String nameToBeEngraved;

	private String engravingRequired;

	@SuppressWarnings({ "unchecked", "deprecation" })
	@DefaultHandler
	@JsonHandler
	@GET
	@Path("/addtoCart/")
	@Produces("application/json")
	public String addToCart(@Context HttpServletResponse response,
			@QueryParam("productVariantId") String productVariantId,
			@QueryParam("productId") String productId,
			@QueryParam("productReferrerId") Long productReferrerId) {
		HealthkartResponse healthkartResponse;
		String jsonBuilder = "";
		String message = MHKConstants.STATUS_DONE;
		String status = MHKConstants.STATUS_OK;

		/*
		 * if (isVariantConfigProvided()) { if (productVariantList != null &&
		 * productVariantList.size() > 0) {
		 * setVariantId(productVariantList.get(0).getId()); } return new
		 * ForwardResolution
		 * (AddToCartWithLineItemConfigAction.class).addParameter("variantId",
		 * variantId); } else {
		 */
		// I need to pass product info
		User user = null;
		ProductReferrer productReferrer = null;
		addHeaderAttributes(response);
		if (getPrincipal() != null) {
			user = userManager.getUserService().getUserById(getPrincipal().getId());
			if (user == null) {
				user = userManager.createAndLoginAsGuestUser(null, null);
			}
		} else {
			user = userManager.createAndLoginAsGuestUser(null, null);
		}

		Order order = orderManager.getOrCreateOrder(user);
		productVariantList = productService.getProductById(productId)
				.getProductVariants();
		List<ProductVariant> selectedProductVariants = new ArrayList<ProductVariant>();
		try {
			if (productVariantList != null && productVariantList.size() > 0) {
				for (ProductVariant productVariant : productVariantList) {
					// if (productVariant != null && productVariant.isSelected()
					// != null && productVariant.isSelected()) {
					if (productVariant.getId().equalsIgnoreCase(
							productVariantId)) {
						selectedProductVariants.add(productVariant);
						userCartDao.addToCartHistory(
								productVariant.getProduct(), user);
						userProductHistoryDao.updateIsAddedToCart(
								productVariant.getProduct(), user, order);
					}
				}
			}
			ComboInstance comboInstance = null;
			if (combo != null) {
				if (combo != null && combo.getId() != null) {
					Long maxQty = 0L;
					Long netQty = 0L;
					for (ComboProduct comboProduct : combo.getComboProducts()) {
						maxQty += comboProduct.getQty();
					}
					for (ProductVariant productVariant : productVariantList) {
						if (productVariant.getQty() != null) {
							netQty += productVariant.getQty();
						} else {
							logger.error("Null qty for Combo=" + combo.getId());
						}
					}
					if (netQty != maxQty) {
						healthkartResponse = new HealthkartResponse(
								MHKConstants.STATUS_ERROR,
								MHKConstants.COMBO_VARIANT_QUANTITY_ERROR,
								new HashMap());
						noCache(response);
						return JsonUtils.getGsonDefault().toJson(
								healthkartResponse);
					}
				} else {
					if (productVariantList != null
							&& productVariantList.size() > 0) {
						for (ProductVariant productVariant : productVariantList) {
							// can pv be null here? have to check, putting a
							// null check --> still null pointer on above line
							// --> null check for PVlist
							if (productVariant != null
									&& productVariant.getQty() != null
									&& productVariant.getQty() < 1) {
								healthkartResponse = new HealthkartResponse(
										MHKConstants.STATUS_ERROR,
										MHKConstants.MIN_QTY_PROD_VARIANT,
										new HashMap());
								noCache(response);
								return JsonUtils.getGsonDefault().toJson(
										healthkartResponse);
							}
						}
					}
				}

				comboInstance = new ComboInstance();
				comboInstance.setCombo(combo);
				comboInstance = (ComboInstance) comboInstanceDao
						.save(comboInstance);
			}

			if (comboInstance != null) {
				if (selectedProductVariants != null
						&& selectedProductVariants.size() > 0) {
					for (ProductVariant variant : selectedProductVariants) {
						ComboInstanceHasProductVariant comboInstanceProductVariant = new ComboInstanceHasProductVariant();
						if (variant.getQty() != 0) {
							comboInstanceProductVariant
									.setComboInstance(comboInstance);
							comboInstanceProductVariant
									.setProductVariant(variant);
							comboInstanceProductVariant
									.setQty(variant.getQty());
							comboInstanceProductVariant = (ComboInstanceHasProductVariant) comboInstanceHasProductVariantDao
									.save(comboInstanceProductVariant);
						}
					}
				}
			}
			if (productReferrerId != null) {
				productReferrer = userCartDao.get(ProductReferrer.class,
						productReferrerId);
			}
			orderManager.createLineItems(selectedProductVariants, order, combo,
					comboInstance, productReferrer);
		} catch (OutOfStockException e) {
			message = MHKConstants.OUT_OF_STOCK;
			status = MHKConstants.STATUS_ERROR;
			noCache(response);
			// recomendationEngine.notifyAddToCart(user.getId(),
			// productVariantList);
			return JsonUtils.getGsonDefault().toJson(
					new HealthkartResponse(status, message, null));
		}

		Map dataMap = new HashMap();
		// null pointer here --> putting a null check
		if (selectedProductVariants != null
				&& selectedProductVariants.size() > 0) {
			if (combo != null) {
				dataMap.put("name", combo.getName());
			} else {
				dataMap.put("name", selectedProductVariants.get(0).getProduct()
						.getName());
			}
			if (combo != null) {
				dataMap.put("addedProducts", combo.getName());
			} else {
				String addedProducts = "";
				for (ProductVariant selectedProductVariant : selectedProductVariants) {
					addedProducts = addedProducts.concat(
							selectedProductVariant.getProduct().getName())
							.concat("  |");
				}
				dataMap.put("addedProducts", addedProducts);
			}
			dataMap.put("options", selectedProductVariants.get(0)
					.getOptionsCommaSeparated());
			dataMap.put("qty", selectedProductVariants.get(0).getQty());
			Set<CartLineItem> subscriptionCartLineItems = new CartLineItemFilter(
					order.getCartLineItems()).addCartLineItemType(
					EnumCartLineItemType.Subscription).filter();
			dataMap.put(
					"itemsInCart",
					Long.valueOf(order.getExclusivelyProductCartLineItems()
							.size()
							+ order.getExclusivelyComboCartLineItems().size())
							+ subscriptionCartLineItems.size() + 1L);
			healthkartResponse = new HealthkartResponse(
					HealthkartResponse.STATUS_OK, MHKConstants.PROD_ADDED_CART,
					dataMap);
			noCache(response);
			// recomendationEngine.notifyAddToCart(user.getId(),
			// productVariantList);
			return JsonUtils.getGsonDefault().toJson(healthkartResponse);
		}
		healthkartResponse = new HealthkartResponse(
				HealthkartResponse.STATUS_ERROR,
				MHKConstants.PROD_NOT_ADDED_CART, dataMap);
		noCache(response);
		return JsonUtils.getGsonDefault().toJson(healthkartResponse);
	}

	public List<ProductVariant> getProductVariantList() {
		return productVariantList;
	}

	public void setProductVariantList(List<ProductVariant> productVariantList) {
		this.productVariantList = productVariantList;
	}

	public Combo getCombo() {
		return combo;
	}

	public void setCombo(Combo combo) {
		this.combo = combo;
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
}