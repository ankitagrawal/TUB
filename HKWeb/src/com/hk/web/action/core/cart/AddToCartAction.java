package com.hk.web.action.core.cart;


import java.util.*;

import com.hk.pact.dao.order.cartLineItem.CartLineItemDao;
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
import com.hk.web.HealthkartResponse;
import com.hk.web.action.core.user.SignupAction;

@Component
public class AddToCartAction extends BaseAction implements ValidationErrorHandler {

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
  CartLineItemDao cartLineItemDao;
	@Autowired
	ComboInstanceHasProductVariantDao comboInstanceHasProductVariantDao;
	@Autowired
	UserCartDao userCartDao;
	@Autowired
	UserProductHistoryDao userProductHistoryDao;

	private Long productReferrerId;
	@Autowired
	SignupAction signupAction;

	private boolean variantConfigProvided;

	private String variantId;

	private String jsonConfigValues;

	private String nameToBeEngraved;

	private String engravingRequired;

	@SuppressWarnings({"unchecked", "deprecation"})
	@DefaultHandler
	@JsonHandler
	public Resolution addToCart() {
		if (isVariantConfigProvided()) {
			if (productVariantList != null && productVariantList.size() > 0) {
				setVariantId(productVariantList.get(0).getId());
			}
			return new ForwardResolution(AddToCartWithLineItemConfigAction.class).addParameter("variantId", variantId);
		} else {
			// I need to pass product info
			User user = null;
			ProductReferrer productReferrer = null;
			if (getPrincipal() != null) {
				user = userDao.getUserById(getPrincipal().getId());
				if (user == null) {
					user = userManager.createAndLoginAsGuestUser(null, null);
				}
			} else {
				user = userManager.createAndLoginAsGuestUser(null, null);
			}

			Order order = orderManager.getOrCreateOrder(user);
			List<ProductVariant> selectedProductVariants = new ArrayList<ProductVariant>();
			try {
				if (productVariantList != null && productVariantList.size() > 0) {
					for (ProductVariant productVariant : productVariantList) {
						if (productVariant != null && productVariant.isSelected() != null && productVariant.isSelected()) {
							selectedProductVariants.add(productVariant);
							userCartDao.addToCartHistory(productVariant.getProduct(), user);
							userProductHistoryDao.updateIsAddedToCart(productVariant.getProduct(), user, order);
						}
					}
				}

				ComboInstance comboInstance = null;
        Long comboInstanceValue = null;
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
							addValidationError("Combo product variant qty are not in accordance to offer", new SimpleError("Combo product variant qty are not in accordance to offer"));
							HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Combo product variant qty are not in accordance to offer", new HashMap());
							noCache();
							return new JsonResolution(healthkartResponse);
						}
					} else {
						if (productVariantList != null && productVariantList.size() > 0) {
							for (ProductVariant productVariant : productVariantList) {
								//can pv be null here? have to check, putting a null check  --> still null pointer on above line --> null check for PVlist
								if (productVariant != null && productVariant.getQty() != null && productVariant.getQty() < 1) {
									addValidationError("Min Qty should be 1", new SimpleError("Min Qty should be 1"));
									HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Min Qty should be 1", new HashMap());
									noCache();
									return new JsonResolution(healthkartResponse);
								}
							}
						}
					}

					comboInstance = new ComboInstance();
					comboInstance.setCombo(combo);
					comboInstance = (ComboInstance) comboInstanceDao.save(comboInstance);
          comboInstanceValue = comboInstance.getId();
				}

        //validation to check if product variant is adding on combo and vice versa and combo having atleast one common product variant
        int x = 0;
        Set<Long> comboInstanceIds = new TreeSet<Long>();
        Set<CartLineItem> cartLineItems= order.getCartLineItems();
        for(ProductVariant productVariant : productVariantList){
          for(CartLineItem cartLineItem : cartLineItems){
            if(combo!=null){
              if(productVariant!=null && cartLineItem.getProductVariant()!=null && productVariant.equals(cartLineItem.getProductVariant())){
                if((cartLineItem.getComboInstance()!=null && !cartLineItem.getComboInstance().getCombo().getId().equals(combo.getId()))||(cartLineItem.getComboInstance()==null)){
                 comboInstanceIds.add(comboInstanceValue);
                 x = 1;
                }
                else if(cartLineItem.getComboInstance()!=null && cartLineItem.getComboInstance().getCombo().getId().equals(combo.getId())){
                  for(ComboInstanceHasProductVariant comboInstanceHasProductVariant : cartLineItem.getComboInstance().getComboInstanceProductVariants()){
                     if(!comboInstanceHasProductVariant.getProductVariant().equals(productVariant)) {
                        comboInstanceIds.add(comboInstanceValue);
                        x = 1;
                    }
                  }
                }
              }
            }
            else{
              if(productVariant!=null && cartLineItem.getProductVariant()!=null && productVariant.equals(cartLineItem.getProductVariant()) && cartLineItem.getComboInstance()!=null){
                x = 2;
              }
            }
          }
        }
        for(Long comboInstanceId : comboInstanceIds){
          for(CartLineItem cartLineItem : order.getCartLineItems()){
            if(cartLineItem.getComboInstance()!=null && cartLineItem.getComboInstance().equals(comboInstanceId)){
              getCartLineItemDao().delete(cartLineItem);
            }
          }
        }
        if(x==1){
          addValidationError("You can't add any combo whose product is already added in the cart", new SimpleError("You can't add any combo whose product is already added in the cart"));
          HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "You can't add any combo whose product is already added in the cart", new HashMap());
          noCache();
          return new JsonResolution(healthkartResponse);
        }
        else if (x==2){
          addValidationError("You can't add any product whose combo is already added in the cart", new SimpleError("You can't add any product whose combo is already added in the cart"));
          HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "You can't add any product whose combo is already added in the cart", new HashMap());
          noCache();
          return new JsonResolution(healthkartResponse);
        }
				if (comboInstance != null) {
					if (selectedProductVariants != null && selectedProductVariants.size() > 0) {
						for (ProductVariant variant : selectedProductVariants) {
							ComboInstanceHasProductVariant comboInstanceProductVariant = new ComboInstanceHasProductVariant();
							if (variant != null && variant.getQty() != null && variant.getQty() != 0) {
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
				getContext().getValidationErrors().add("e2", new SimpleError(e.getMessage()));
				return new JsonResolution(getContext().getValidationErrors(), getContext().getLocale());
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
				HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Product has been added to cart", dataMap);
				noCache();
				//recomendationEngine.notifyAddToCart(user.getId(), productVariantList);
				return new JsonResolution(healthkartResponse);
			}
			HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Product has not been added to cart", dataMap);
			noCache();
			return new JsonResolution(healthkartResponse);
		}
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

  public CartLineItemDao getCartLineItemDao() {
    return cartLineItemDao;
  }
}