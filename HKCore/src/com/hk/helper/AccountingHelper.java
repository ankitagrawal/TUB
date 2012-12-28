package com.hk.helper;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hk.constants.order.EnumCartLineItemType;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.CartLineItemConfig;
import com.hk.domain.order.Order;


public class AccountingHelper {

	private static Logger logger = LoggerFactory.getLogger(AccountingHelper.class);

	private static double getDiscountOnCartLineItem(Set<CartLineItem> cartLineItems, ProductVariant productVariant, CartLineItemConfig cartLineItemConfig,
	                                                EnumCartLineItemType discountType) {
		double discount = 0;
		CartLineItem discountLineItem = null;

		CartLineItemFilter discountLineItemFilter = new CartLineItemFilter(cartLineItems).addCartLineItemType(discountType).setProductVariantId(productVariant.getId());

		if (cartLineItemConfig != null) {
			discountLineItemFilter.setCartLineItemConfigId(cartLineItemConfig.getId());
		}

		Set<CartLineItem> orderLevelDiscountLineItems = discountLineItemFilter.filter();

		/**
		 * Todo: uncomment the below code when we ensure that only one orderlevelDiscoutnLineItem would come per sku + cartLineItemConfig
		 * This is only done to cater to lenses in eye category.
		 */
		/*if (orderLevelDiscountLineItems != null && orderLevelDiscountLineItems.size() > 1) {
						throw new MultipleCartLineItemsForTypeException ("more than one order level discount", productLineItem);
					}*/

		if (orderLevelDiscountLineItems != null && !orderLevelDiscountLineItems.isEmpty()) {
			discountLineItem = orderLevelDiscountLineItems.iterator().next();
		}
		if (discountLineItem != null && discountLineItem.getDiscountOnHkPrice() != null) {
			discount = discountLineItem.getDiscountOnHkPrice();
		}
		return discount;
	}

	public static double getOrderLevelDiscOnCartLI(Set<CartLineItem> cartLineItems, ProductVariant productVariant, CartLineItemConfig cartLineItemConfig) {
		return getDiscountOnCartLineItem(cartLineItems, productVariant, cartLineItemConfig, EnumCartLineItemType.OrderLevelDiscount);
	}


	public static double calculateRewardPointDiscountOnLineItem(Set<CartLineItem> cartLineItems, ProductVariant productVariant, CartLineItemConfig cartLineItemConfig) {
		return getDiscountOnCartLineItem(cartLineItems, productVariant, cartLineItemConfig, EnumCartLineItemType.RewardPoint);
	}

	public static double getBaseAmountOnBaseOrder(Order baseOrder) {
		Set<CartLineItem> productCartLineItems = new CartLineItemFilter(baseOrder.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();

		double baseAmountOnBaseOrder = 0;
		for (CartLineItem productCartLineItem : productCartLineItems) {
			double orderLevelDiscount = getOrderLevelDiscOnCartLI(baseOrder.getCartLineItems(), productCartLineItem.getProductVariant(), productCartLineItem.getCartLineItemConfig());

			baseAmountOnBaseOrder = baseAmountOnBaseOrder + (productCartLineItem.getHkPrice() * productCartLineItem.getQty()) -
					productCartLineItem.getDiscountOnHkPrice() -
					orderLevelDiscount;
		}

		return baseAmountOnBaseOrder;
	}


	public static double getRewardPointsForBaseOrder(Set<CartLineItem> cartLineItems) {
		CartLineItem rewardPointCartLineItem = getCartLineItemByType(cartLineItems, EnumCartLineItemType.RewardPoint);
		if (rewardPointCartLineItem != null) {
			return (rewardPointCartLineItem.getDiscountOnHkPrice());
		}
		return 0D;
	}

	public static double getCODChargesForBaseOrder(Set<CartLineItem> cartLineItems) {
		CartLineItem codChargesForCartLineItem = getCartLineItemByType(cartLineItems, EnumCartLineItemType.CodCharges);

		if (codChargesForCartLineItem != null) {
			return (codChargesForCartLineItem.getHkPrice() - codChargesForCartLineItem.getDiscountOnHkPrice());
		}
		return 0D;
	}

	public static double getShippingCostForBaseOrder(Set<CartLineItem> cartLineItems) {
		CartLineItem shippingCartLineItem = getCartLineItemByType(cartLineItems, EnumCartLineItemType.Shipping);
		if (shippingCartLineItem != null) {
			return (shippingCartLineItem.getHkPrice() - shippingCartLineItem.getDiscountOnHkPrice());
		}
		return 0D;
	}

	private static CartLineItem getCartLineItemByType(Set<CartLineItem> cartLineItems, EnumCartLineItemType lineItemType) {
		Set<CartLineItem> filteredCartLineItems = new CartLineItemFilter(cartLineItems).addCartLineItemType(lineItemType).filter();
		CartLineItem filteredCartLineItem;


		if (filteredCartLineItems.size() > 1) {
			filteredCartLineItem = filteredCartLineItems.iterator().next();
			logger.error("Multiple line items found for line item type : " + lineItemType.getName() + " in gateway base order : " + filteredCartLineItem.getOrder().getGatewayOrderId());
			return filteredCartLineItem;
			//throw new MultipleCartLineItemsForTypeException("Multiple  line items found of type : " + lineItemType.getName(), filteredCartLineItems.iterator().next());
		} else if (filteredCartLineItems.size() == 1) {
			return filteredCartLineItems.iterator().next();
		}

		return null;
	}


}
