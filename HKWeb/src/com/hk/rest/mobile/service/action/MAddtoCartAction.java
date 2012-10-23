package com.hk.rest.mobile.service.action;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.controller.JsonHandler;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.catalog.product.combo.Combo;
import com.hk.domain.catalog.product.combo.ComboInstance;
import com.hk.domain.catalog.product.combo.ComboProduct;
import com.hk.domain.catalog.product.combo.ComboInstanceHasProductVariant;
import com.hk.domain.user.User;
import com.hk.domain.marketing.ProductReferrer;
import com.hk.domain.order.Order;
import com.hk.domain.order.CartLineItem;
import com.hk.pact.dao.user.UserDao;
import com.hk.pact.dao.user.UserCartDao;
import com.hk.pact.dao.user.UserProductHistoryDao;
import com.hk.pact.dao.catalog.combo.ComboInstanceDao;
import com.hk.pact.dao.catalog.combo.ComboInstanceHasProductVariantDao;
import com.hk.manager.UserManager;
import com.hk.manager.OrderManager;
import com.hk.web.action.core.user.SignupAction;
import com.hk.web.action.core.cart.AddToCartWithLineItemConfigAction;
import com.hk.web.HealthkartResponse;
import com.hk.exception.OutOfStockException;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.constants.order.EnumCartLineItemType;
import net.sourceforge.stripes.validation.ValidationErrorHandler;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.JsonResolution;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Satish
 * Date: Oct 8, 2012
 * Time: 9:55:07 PM
 * To change this template use File | Settings | File Templates.
 */


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
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
import com.akube.framework.gson.JsonUtils;
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
import com.hk.pact.dao.user.UserDao;
import com.hk.pact.dao.user.UserProductHistoryDao;
import com.hk.pact.service.catalog.ProductService;
import com.hk.web.HealthkartResponse;
import com.hk.web.action.core.user.SignupAction;
import com.hk.rest.mobile.service.utils.MHKConstants;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@Path("/mAddtoCart")
@Component
public class MAddtoCartAction extends MBaseAction {

	private static Logger logger = LoggerFactory.getLogger(MAddtoCartAction.class);

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
    UserProductHistoryDao userProductHistoryDao;

	private Long productReferrerId;
	@Autowired
    SignupAction signupAction;

    @Autowired
    ProductService productService;

	private boolean variantConfigProvided;

	private String variantId;

	private String jsonConfigValues;

	private String nameToBeEngraved;

	private String engravingRequired;

	@SuppressWarnings({"unchecked", "deprecation"})
	@DefaultHandler
	@JsonHandler
    @GET
    @Path("/addtoCart/")
    @Produces("application/json")

	public String addToCart(@Context HttpServletResponse response,
                            @QueryParam("productVariantId") String productVariantId,
                            @QueryParam("productId") String productId) {
        HealthkartResponse healthkartResponse;
        String jsonBuilder = "";
        String message = MHKConstants.STATUS_DONE;
        String status = MHKConstants.STATUS_OK;

/*
		if (isVariantConfigProvided()) {
			if (productVariantList != null && productVariantList.size() > 0) {
				setVariantId(productVariantList.get(0).getId());
			}
			return new ForwardResolution(AddToCartWithLineItemConfigAction.class).addParameter("variantId", variantId);
		} else {
*/
			// I need to pass product info
			User user = null;
			ProductReferrer productReferrer = null;
            addHeaderAttributes(response);
			if (getPrincipal() != null) {
				user = userDao.getUserById(getPrincipal().getId());
				if (user == null) {
					user = userManager.createAndLoginAsGuestUser(null, null);
				}
			} else {
				user = userManager.createAndLoginAsGuestUser(null, null);
			}

			Order order = orderManager.getOrCreateOrder(user);
            productVariantList = productService.getProductById(productId).getProductVariants();
            List<ProductVariant> selectedProductVariants = new ArrayList<ProductVariant>();
			 try {
				if (productVariantList != null && productVariantList.size() > 0) {
					for (ProductVariant productVariant : productVariantList) {
						//if (productVariant != null && productVariant.isSelected() != null && productVariant.isSelected()) {
                            if(productVariant.getId().equalsIgnoreCase(productVariantId)){
							selectedProductVariants.add(productVariant);
							userCartDao.addToCartHistory(productVariant.getProduct(), user);
							userProductHistoryDao.updateIsAddedToCart(productVariant.getProduct(), user, order);
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
							healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Combo product variant qty are not in accordance to offer", new HashMap());
							noCache(response);
							return JsonUtils.getGsonDefault().toJson(healthkartResponse);
						}
					} else {
						if (productVariantList != null && productVariantList.size() > 0) {
							for (ProductVariant productVariant : productVariantList) {
								//can pv be null here? have to check, putting a null check  --> still null pointer on above line --> null check for PVlist
								if (productVariant != null && productVariant.getQty() != null && productVariant.getQty() < 1) {
									healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Min Qty should be 1", new HashMap());
									noCache(response);
                                    return JsonUtils.getGsonDefault().toJson(healthkartResponse);
								}
							}
						}
					}

					comboInstance = new ComboInstance();
					comboInstance.setCombo(combo);
					comboInstance = (ComboInstance) comboInstanceDao.save(comboInstance);
				}

				if (comboInstance != null) {
					if (selectedProductVariants != null && selectedProductVariants.size() > 0) {
						for (ProductVariant variant : selectedProductVariants) {
							ComboInstanceHasProductVariant comboInstanceProductVariant = new ComboInstanceHasProductVariant();
							if (variant.getQty() != 0) {
								comboInstanceProductVariant.setComboInstance(comboInstance);
								comboInstanceProductVariant.setProductVariant(variant);
								comboInstanceProductVariant.setQty(variant.getQty());
								comboInstanceProductVariant = (ComboInstanceHasProductVariant) comboInstanceHasProductVariantDao.save(comboInstanceProductVariant);
							}
						}
					}
				}
				if (productReferrerId != null) {
					productReferrer = userCartDao.get(ProductReferrer.class, productReferrerId);
				}
				orderManager.createLineItems(selectedProductVariants, order, combo, comboInstance, productReferrer);
			} catch (OutOfStockException e) {
                message = "Out of Stock";
                status = MHKConstants.STATUS_ERROR;
                 noCache(response);
                 //recomendationEngine.notifyAddToCart(user.getId(), productVariantList);
                 return JsonUtils.getGsonDefault().toJson(new HealthkartResponse(status, message,null));
			}

			Map dataMap = new HashMap();
			//null pointer here --> putting a null check
			if (selectedProductVariants != null && selectedProductVariants.size() > 0) {
				if (combo != null) {
					dataMap.put("name", combo.getName());
				} else {
					dataMap.put("name", selectedProductVariants.get(0).getProduct().getName());
				}
				if (combo != null) {
					dataMap.put("addedProducts", combo.getName());
				} else {
					String addedProducts = "";
					for (ProductVariant selectedProductVariant : selectedProductVariants) {
						addedProducts = addedProducts.concat(selectedProductVariant.getProduct().getName()).concat("  |");
					}
					dataMap.put("addedProducts", addedProducts);
				}
				dataMap.put("options", selectedProductVariants.get(0).getOptionsCommaSeparated());
				dataMap.put("qty", selectedProductVariants.get(0).getQty());
				Set<CartLineItem> subscriptionCartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Subscription).filter();
				dataMap.put("itemsInCart", Long.valueOf(order.getExclusivelyProductCartLineItems().size() + order.getExclusivelyComboCartLineItems().size()) + subscriptionCartLineItems.size() + 1L);
				healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Product has been added to cart", dataMap);
				noCache(response);
				//recomendationEngine.notifyAddToCart(user.getId(), productVariantList);
                return JsonUtils.getGsonDefault().toJson(healthkartResponse);
			}
			healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Product has not been added to cart", dataMap);
			noCache(response);
        return JsonUtils.getGsonDefault().toJson(healthkartResponse);
//		}
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

	public Long getProductReferrerId() {
		return productReferrerId;
	}

	public void setProductReferrerId(Long productReferrerId) {
		this.productReferrerId = productReferrerId;
	}
}