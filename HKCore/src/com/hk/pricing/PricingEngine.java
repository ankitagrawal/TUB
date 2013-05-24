package com.hk.pricing;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.testng.Assert;

import com.hk.constants.core.Keys;
import com.hk.constants.discount.OfferConstants;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.order.PricingConstants;
import com.hk.domain.builder.CartLineItemBuilder;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductGroup;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.offer.OfferAction;
import com.hk.domain.offer.OfferInstance;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.CartLineItemConfig;
import com.hk.domain.order.Order;
import com.hk.domain.sku.Sku;
import com.hk.domain.user.Address;
import com.hk.dto.pricing.PricingDto;
import com.hk.pact.service.inventory.SkuService;
import com.hk.util.CartLineItemWrapper;
import com.hk.util.OfferTriggerMatcher;

@Component
public class PricingEngine {

    @Value("#{hkEnvProps['" + Keys.Env.shippingFreeAfter + "']}")
    private Double shippingFreeAfter;

    @Autowired
    SkuService     skuService;

    /**
     * This method reset the state of offer instance. i.e after calculating the price it reset the offer intance to its
     * intial state. This way we can prevent the un-intentional saving of intermediate state of offer instance at the
     * time of automatic session/database flushing by hibernate.
     *
     * @param lineItems
     * @param offerInstance
     * @param address
     * @param redeemRewardPoints
     * @return
     */
    public Set<CartLineItem> calculatePricing(final Set<CartLineItem> lineItems, OfferInstance offerInstance, Address address, Double redeemRewardPoints) {

        OfferInstance copiedInstance = null;

        if (offerInstance != null) {
            copiedInstance = new OfferInstance();
            copiedInstance.setActive(offerInstance.isActive());
        }

        Set<CartLineItem> invoiceLines = pricing(lineItems, offerInstance, address, redeemRewardPoints);

        if (copiedInstance != null) {
            offerInstance.setActive(copiedInstance.isActive());
        }

        return invoiceLines;
    }

    /**
     * Its just a simple wrapper over the pricing function.
     *
     * @param cartLineItems
     * @param offerInstance
     * @param address
     * @param redeemRewardPoints
     * @return
     */
    public Set<CartLineItem> calculateAndApplyPricing(final Set<CartLineItem> cartLineItems, OfferInstance offerInstance, Address address, Double redeemRewardPoints) {
        Set<CartLineItem> invoiceLines = pricing(cartLineItems, offerInstance, address, redeemRewardPoints);

        return invoiceLines;
    }

    private Set<CartLineItem> pricing(final Set<CartLineItem> lineItems, OfferInstance offerInstance, Address address, Double redeemRewardPoints) {
        Set<CartLineItemWrapper> cartLineItemWrappers = initProductLineItems(lineItems, address);
        Set<CartLineItem> orderLevelDiscountLineItems = new HashSet<CartLineItem>();
        CartLineItemWrapper shippingCartLineItemWrapper = initShippingLineItem(lineItems, address);
        cartLineItemWrappers.add(shippingCartLineItemWrapper);

        Map<ProductGroup, Long> pGroupMinQtyMap = new HashMap<ProductGroup, Long>(); // this map will be used in
        // offerAction. It will take
        // care
        // of the qty to be killed during the order level discount in case of combo products offer
        Boolean comboOffer = false;

        // return early
        if (offerInstance == null || !offerInstance.isValid()) {
            Set<CartLineItem> lineItemsUnwrapped = CartLineItemWrapper.unwrap(cartLineItemWrappers);

            // add reward point invoice line if any
            if (redeemRewardPoints != null && redeemRewardPoints > 0) {
                // The change is done to make shipping line items be counted while calculating grandtotal via PricingDto
                lineItemsUnwrapped.add(createRewardPointLineItem(redeemRewardPoints, lineItems, shippingCartLineItemWrapper));
            }

            applyShippingDiscount(address, lineItemsUnwrapped, offerInstance);

            return lineItemsUnwrapped;
        }

        OfferAction offerAction = offerInstance.getOffer().getOfferAction();

        /***************************************************************************************************************
         * * while loop iterating over lineItemWrappers for a one offerInstance * *
         **************************************************************************************************************/

        while (CartLineItemWrapper.isAnyLive(cartLineItemWrappers)) {

            /*
             * This variable will keep track whether the offer action is being applied to the any of the lineItems
             * during this iteration or not If no action is applied succesfully, this means that this offer can not be
             * applied further so no point of iteration, so get out of the loop Example where this variable will be
             * used: offer: 10 Free prints matte or glossy But user has added only metallic in the cart and tried to
             * apply the offer. In this case there is no trigger as well as no matched qty. So there arises the special
             * condition to get out of the loop we use this variable.
             */
            boolean offerActionSuccess = false;

            if (!offerInstance.isValid()) {
                break;
            }

            /***********************************************************************************************************
             * * Offer Trigger implementation * *
             **********************************************************************************************************/

            if (offerInstance.getOffer().getOfferTrigger() != null) {
                // trigger logic
                OfferTriggerMatcher triggerMatcher = offerInstance.getOffer().getOfferTrigger().match(cartLineItemWrappers);

                if (triggerMatcher.hasMatch(offerInstance.getOffer().isExcludeTriggerProducts())) {
                    cartLineItemWrappers = triggerMatcher.getLineItemWrappers();
                    pGroupMinQtyMap = triggerMatcher.getPGroupMinQtyMap();
                    comboOffer = triggerMatcher.isComboOffer();
                } else {
                    // safe to return now
                    break;
                }
            }

            /***********************************************************************************************************
             * * Order Level Discount Chooser Logic, by default doesnt touch anything, gets triggered when along with
             * order level discount logic, a percentage on Hk price is also set this case has been made to handle the
             * max discount applicable using %cent dicount on Hk price * *
             **********************************************************************************************************/
            boolean isOrderLevelDiscountlogic = true;
            if (offerAction.getOrderLevelDiscountAmount() != null && offerAction.getOrderLevelDiscountAmount() > 0D && offerAction.getDiscountPercentOnHkPrice() != null) {
                Double possiblePercentDiscount = 0D;
                ProductGroup productGroup = offerAction.getProductGroup();
                for (CartLineItemWrapper cartLineItemWrapper : cartLineItemWrappers) {
                    CartLineItem lineitem = cartLineItemWrapper.getCartLineItem();
                    if ((lineitem.isType(EnumCartLineItemType.Product) && productGroup == null)
                            && cartLineItemWrapper.hasLiveQty()
                            || (lineitem.isType(EnumCartLineItemType.Product) && productGroup != null && productGroup.contains(lineitem.getProductVariant()) && cartLineItemWrapper.hasLiveQty())) {
                        possiblePercentDiscount += lineitem.getHkPrice() * lineitem.getQty() * offerAction.getDiscountPercentOnHkPrice();
                        // possiblePercentDiscount += lineitem.getDiscountOnHkPrice() + lineitem.getHkPrice() *
                        // lineitem.getQty() * offerAction.getDiscountPercentOnHkPrice();
                    }
                }
                if (possiblePercentDiscount <= offerAction.getOrderLevelDiscountAmount()) {
                    isOrderLevelDiscountlogic = false;
                }
            }

            /***********************************************************************************************************
             * * Order Level Discount Logic * *
             **********************************************************************************************************/

            /**
             * if OrderLevelDiscountAmount is not null ==> discount is order level discount which is to be applicale on
             * all or particular product group this order level discount will be distributed among all the applicable
             * lineitems A new lineitem of the type {@link mhc.common.constants.EnumLineItemType#OrderLevelDiscount}
             * will be created which will contain all the discount information
             */
            if (offerAction.getOrderLevelDiscountAmount() != null && offerAction.getOrderLevelDiscountAmount() > 0D && isOrderLevelDiscountlogic) {
                ProductGroup productGroup = offerAction.getProductGroup();
                Double liveTotal = 0.0;

                /*
                 * This will handle the condition when productGroup exist. It further leads to 2 conditions 1.
                 * ComboOffer exist eg: Buy 2 Tshirts get Rs 10 off on prints Buy 2 Tshirts get Rs 20 off on tshirts 2.
                 * ComboOffer does not exist eg: Buy Prints worth Rs 200 get Rs 20 off on prints Purchase for Rs 500 get
                 * Rs 50 off on prints NOTE: This code is not capable of handling multi level discounts in case of non
                 * combo order level discounts
                 */
                if (productGroup != null) {
                    for (CartLineItemWrapper cartLineItemWrapper : cartLineItemWrappers) {
                        Long deadQty = 0L;
                        if (cartLineItemWrapper.getCartLineItem().isType(EnumCartLineItemType.Product)
                                && productGroup.contains(cartLineItemWrapper.getCartLineItem().getProductVariant())) {
                            if (comboOffer && pGroupMinQtyMap.containsKey(productGroup)) { // condtion 1 (see eg1
                                // above)
                                deadQty = cartLineItemWrapper.getLiveQty() >= pGroupMinQtyMap.get(productGroup) ? pGroupMinQtyMap.get(productGroup)
                                        : cartLineItemWrapper.getLiveQty();
                                pGroupMinQtyMap.put(productGroup, pGroupMinQtyMap.get(productGroup) - deadQty);
                            } else { // condtion 1 (see eg2 above)
                                deadQty = cartLineItemWrapper.getLiveQty();
                            }
                            cartLineItemWrapper.mark(deadQty);
                            liveTotal += deadQty * cartLineItemWrapper.getCartLineItem().getProductVariant().getHkPrice(null);
                        }
                    }
                }

                /*
                 * This will handle the condition when productGroup in offerAction is not specified. It means that offer
                 * can be applied to all the lineitems or on some specified ones and on all Live quantites or on
                 * specified quantities. If comboOffer exist then order level discount will be calculated on specified
                 * lineitems as well as on specified qty not more than qty specified in pGroupQtyMap eg: Buy 2 tshirts
                 * and 10 prints for Rs 800 only Buy 4 tshirts and 20 prints for Rs 1600 only If comboOffer does not
                 * exist then discount will be applied on all lineitems and all qty eg: Buy prints worth Rs 200 get Rs
                 * 20 off Purchase for Rs 1000 get Rs 100 off NOTE: This code is not capable of handling multi level
                 * discounts in case of non combo order level discounts
                 */
                else {
                    for (CartLineItemWrapper cartLineItemWrapper : cartLineItemWrappers) {
                        if (cartLineItemWrapper.getCartLineItem().isType(EnumCartLineItemType.Product)) {
                            Long deadQty = 0L;
                            if (comboOffer) {
                                for (ProductGroup pGroup : pGroupMinQtyMap.keySet()) {
                                    if (pGroup.contains(cartLineItemWrapper.getCartLineItem().getProductVariant())) {
                                        deadQty = cartLineItemWrapper.getLiveQty() >= pGroupMinQtyMap.get(pGroup) ? pGroupMinQtyMap.get(pGroup) : cartLineItemWrapper.getLiveQty();
                                        pGroupMinQtyMap.put(pGroup, pGroupMinQtyMap.get(pGroup) - deadQty);
                                    } // if productScaffold is not in pGroupQtyMap then its marked qty will be zero
                                    // and on discount will be applied on it
                                }
                            } else {
                                deadQty = cartLineItemWrapper.getLiveQty();
                            }
                            cartLineItemWrapper.mark(deadQty);
                            liveTotal += deadQty * cartLineItemWrapper.getCartLineItem().getProductVariant().getHkPrice(null);

                        }
                    }
                }

                double remainingDiscount = offerInstance.getOffer().getOfferAction().getOrderLevelDiscountAmount();

                for (CartLineItemWrapper cartLineItemWrapper : cartLineItemWrappers) {
                    CartLineItem lineitem = cartLineItemWrapper.getCartLineItem();
                    if ((lineitem.isType(EnumCartLineItemType.Product) && productGroup == null && cartLineItemWrapper.hasMarkedQty())
                            || (lineitem.isType(EnumCartLineItemType.Product) && productGroup != null && cartLineItemWrapper.hasMarkedQty() && productGroup.contains(lineitem.getProductVariant()))) {
                        Double weightedDiscount = cartLineItemWrapper.getMarkedQty() * lineitem.getProductVariant().getHkPrice(null) * remainingDiscount / liveTotal;

                        // this line will ensure than discount will never be greter than the livetotal
                        // eg: Buy 2 Tshirts get Rs 10 off on prints [here Rs 10 off on prints only and what if user
                        // buys prints worth less than 10, this condition will be handled here ]
                        weightedDiscount = weightedDiscount > liveTotal ? liveTotal : weightedDiscount;

                        offerInstance.setDiscountAmountUsed((offerInstance.getDiscountAmountUsed() == null ? 0D : offerInstance.getDiscountAmountUsed()) + weightedDiscount);
                        offerInstance.setDiscountAmountUsedTotal((offerInstance.getDiscountAmountUsedTotal() == null ? 0D : offerInstance.getDiscountAmountUsedTotal())
                                + weightedDiscount);

                        orderLevelDiscountLineItems.add(createOrderLevelDiscountLineItem(lineitem.getProductVariant(), weightedDiscount));
                        cartLineItemWrapper.killMarked();
                        offerActionSuccess = true;
                    }
                }
                offerInstance.setActive(false);
            }

            /***********************************************************************************************************
             * * Percent Discount implementation * *
             **********************************************************************************************************/

            else {
                if (offerAction.getDiscountPercentOnHkPrice() != null && offerAction.getDiscountPercentOnHkPrice() > 0.0) {
                    // if discount percent not null ==> its a % dicount applicable on all or limited qty

                    ProductGroup productGroup = offerAction.getProductGroup();

                    // get the qty on which discount is applicable
                    // iterate over cartLineItemWrappers to get those limited products on which discount is applicable
                    for (CartLineItemWrapper cartLineItemWrapper : cartLineItemWrappers) {
                        CartLineItem lineitem = cartLineItemWrapper.getCartLineItem();

                        // process only if lineitem type is product
                        // and check whether product is in product group or not
                        if (((lineitem.isType(EnumCartLineItemType.Product) && productGroup == null) && cartLineItemWrapper.hasLiveQty())
                                || (lineitem.isType(EnumCartLineItemType.Product) && productGroup != null && productGroup.contains(lineitem.getProductVariant()) && cartLineItemWrapper.hasLiveQty())) {
                            cartLineItemWrapper.applyDiscount(offerInstance);
                            offerActionSuccess = true;
                        }
                    }

                } else if (offerAction.getDiscountPercentOnMarkedPrice() != null && offerAction.getDiscountPercentOnMarkedPrice() > 0.0) {
                    // if discount percent not null ==> its a % dicount applicable on all or limited qty

                    ProductGroup productGroup = offerAction.getProductGroup();

                    // get the qty on which discount is applicable
                    // iterate over cartLineItemWrappers to get those limited products on which discount is applicable
                    for (CartLineItemWrapper cartLineItemWrapper : cartLineItemWrappers) {
                        CartLineItem lineitem = cartLineItemWrapper.getCartLineItem();

                        // process only if lineitem type is product
                        // and check whether product is in product group or not
                        if (((lineitem.isType(EnumCartLineItemType.Product) && productGroup == null) && cartLineItemWrapper.hasLiveQty())
                                || (lineitem.isType(EnumCartLineItemType.Product) && productGroup != null && productGroup.contains(lineitem.getProductVariant()) && cartLineItemWrapper.hasLiveQty())) {
                            cartLineItemWrapper.applyDiscount(offerInstance);
                            offerActionSuccess = true;
                        }
                    }

                }

                /*******************************************************************************************************
                 * * Shipping Discount Implementation * *
                 ******************************************************************************************************/

                if (offerAction.getDiscountPercentOnShipping() != null) {

                    Set<CartLineItem> productGroupLineItems = new HashSet<CartLineItem>();

                    for (CartLineItemWrapper cartLineItemWrapper : cartLineItemWrappers) {
                        CartLineItem lineitem = cartLineItemWrapper.getCartLineItem();
                        ProductGroup productGroup = offerAction.getProductGroup();

                        if ((lineitem.isType(EnumCartLineItemType.Product) && productGroup == null)
                                || (lineitem.isType(EnumCartLineItemType.Product) && productGroup != null && productGroup.contains(lineitem.getProductVariant()))) {
                            productGroupLineItems.add(lineitem);
                        }

                    }

                    CartLineItemWrapper shippingDiscountCartLineItemWrapper = initShippingLineItem(productGroupLineItems, address);
                    CartLineItem lineitem = shippingDiscountCartLineItemWrapper.getCartLineItem();

                    double discount = offerAction.getDiscountPercentOnShipping() * lineitem.getHkPrice();

                    for (CartLineItemWrapper cartLineItemWrapper : cartLineItemWrappers) {
                        if (cartLineItemWrapper.getCartLineItem().isType(EnumCartLineItemType.Shipping)
                                && cartLineItemWrapper.getAddress() == shippingDiscountCartLineItemWrapper.getAddress() && cartLineItemWrapper.hasLiveQty()) {

                            cartLineItemWrapper.getCartLineItem().setDiscountOnHkPrice(cartLineItemWrapper.getCartLineItem().getDiscountOnHkPrice() + discount);
                            // cartLineItemWrapper.getCartLineItem().setTax(serviceTaxProvider.get());
                            cartLineItemWrapper.killQty(cartLineItemWrapper.getCartLineItem().getQty());

                        }
                    }

                }

                // if (offerInstance.getQtyUsed() >= offerAction.getQty() || (!offerInstance.isCarryOverAllowed())) {
                // offerInstance.setAllowedOrders(offerInstance.getAllowedOrders() - 1); // lotcha right now alreadyUsed
                // times will be increased whether nor not offer applied or not.
                // offerInstance.setQtyUsed(0L);
                // }
            }

            if (offerInstance != null && offerInstance.getOffer().getOfferIdentifier() != null
                    && offerInstance.getOffer().getOfferIdentifier().equals(OfferConstants.HK_EMPLOYEE_OFFER)) {
                for (CartLineItem lineItem : lineItems) {
                    if (lineItem.isType(EnumCartLineItemType.Product)) {
                        Double actualPrice = 0.0;
                        ProductVariant productVariant = lineItem.getProductVariant();
                        Product product = productVariant.getProduct();
                        Sku maxApplicableVatSKu = skuService.findMaxVATSKU(productVariant);
                        Double vat_cst_percentage = 0.0;
                        Double surcharge_percentage = 0.0;
                        if (maxApplicableVatSKu != null) {
                            vat_cst_percentage = maxApplicableVatSKu.getTax().getValue();
                            surcharge_percentage = vat_cst_percentage * 0.05;
                        }
                        Double costPrice = productVariant.getCostPrice();
                        if (costPrice == null) {
                            costPrice = lineItem.getHkPrice();
                        }
                        if (product.getSupplier() != null && maxApplicableVatSKu != null) {
                            if (!product.getSupplier().getState().equalsIgnoreCase(maxApplicableVatSKu.getWarehouse().getState())) {
                                Double cst = 0.02; // CST
                                actualPrice = costPrice * lineItem.getQty() * (1 + cst) * (1 + vat_cst_percentage + surcharge_percentage);
                            } else {
                                actualPrice = costPrice * lineItem.getQty() * (1 + vat_cst_percentage + surcharge_percentage);
                            }
                        } else {
                            actualPrice = costPrice * lineItem.getQty() * (1 + vat_cst_percentage + surcharge_percentage);
                        }

                        Double minPrice = lineItem.getMarkedPrice() * lineItem.getQty() * 0.5;
                        actualPrice = actualPrice > minPrice ? actualPrice : minPrice;

                        Double lineItemDiscount = lineItem.getHkPrice() * lineItem.getQty() - actualPrice;
                        if (lineItemDiscount < 0) {
                            lineItemDiscount = 0D;
                        }
                        lineItem.setDiscountOnHkPrice(lineItemDiscount);
                    }
                    if (lineItem.isType(EnumCartLineItemType.Shipping)) {
                        lineItem.setDiscountOnHkPrice(lineItem.getHkPrice());
                    }
                    if (lineItem.isType(EnumCartLineItemType.CodCharges)) {
                        lineItem.setDiscountOnHkPrice(lineItem.getHkPrice());
                    }
                }
            }

            if (!offerActionSuccess)
                break;

        } // end of while loop

        /***************************************************************************************************************
         * * Returning all lineitems * *
         **************************************************************************************************************/
        Set<CartLineItem> allLineItems = CartLineItemWrapper.unwrap(cartLineItemWrappers);
        allLineItems.addAll(orderLevelDiscountLineItems);

        /***************************************************************************************************************
         * * Adding Reward Point Invoice Line if any * *
         **************************************************************************************************************/
        if (redeemRewardPoints != null && redeemRewardPoints > 0) {
            allLineItems.add(createRewardPointLineItem(redeemRewardPoints, allLineItems, null));
        }

        applyShippingDiscount(address, allLineItems, offerInstance);

        return allLineItems;
    }

    private void applyShippingDiscount(Address address, Set<CartLineItem> lineitems, OfferInstance offerInstance) {
        PricingDto pricingDto = new PricingDto(lineitems, address);
        // Adusting gradtotal with order level discounts and reward points.
        // When someone was applying a code/redeem reward points above 500
        // & the net becomes less than 500; a shipping charge was being applied.
        boolean discountGiven = false;

        if (offerInstance != null && offerInstance.getOffer().getOfferIdentifier() != null
                && offerInstance.getOffer().getOfferIdentifier().equals(OfferConstants.HK_EMPLOYEE_OFFER)) {
            for (CartLineItem lineitem : lineitems) {
                if (lineitem.getLineItemType().getId().equals(EnumCartLineItemType.Shipping.getId())) {
                    lineitem.setDiscountOnHkPrice(lineitem.getHkPrice());
                }
            }
        }

        // was written for services to handle postpaid and prepaid cases separately, currently reverting back, may come
        // handy later 01/12/2011
        if (pricingDto.getGrandTotalProducts() + pricingDto.getOrderLevelDiscount() + pricingDto.getRedeemedRewardPoints() - pricingDto.getShippingTotal() >= shippingFreeAfter) {
            for (CartLineItem lineitem : lineitems) {
                if (lineitem.getLineItemType().getId().equals(EnumCartLineItemType.Shipping.getId())) {
                    lineitem.setDiscountOnHkPrice(lineitem.getHkPrice());
                }
            }
        } else if (pricingDto.getGrandTotalServices() > 0) {
            for (CartLineItem lineitem : lineitems) {
                if (lineitem.getLineItemType().getId().equals(EnumCartLineItemType.Shipping.getId())) {
                    lineitem.setDiscountOnHkPrice(lineitem.getHkPrice());
                }
                discountGiven = true;
            }
        }
        if (discountGiven
                && pricingDto.getGrandTotalProducts() + pricingDto.getOrderLevelDiscount() + pricingDto.getRedeemedRewardPoints() - pricingDto.getShippingTotal() >= 0
                && pricingDto.getGrandTotalProducts() + pricingDto.getOrderLevelDiscount() + pricingDto.getRedeemedRewardPoints() - pricingDto.getShippingTotal() < shippingFreeAfter) {
            for (CartLineItem lineitem : lineitems) {
                if (lineitem.getLineItemType().getId().equals(EnumCartLineItemType.Shipping.getId())) {
                    lineitem.setDiscountOnHkPrice(0D);
                }
            }
        }

    }

    /**
     * This method constructs the initial invoice lines from the line items for further calculations <p/> Two types of
     * invoice lines are created : <p/> <p/> These invoice lines are simply based upon the qty's passed in via line
     * items and the appropriate scaffold pricing depending on the qty are stored in
     * {@link mhc.domain.order.CartLineItem} <p/>
     *
     * @param cartLineItems from the shopping cart
     * @param address
     * @return List of initial invoice lines
     */
    private Set<CartLineItemWrapper> initProductLineItems(Set<CartLineItem> cartLineItems, Address address) {

        address = address != null ? address : new Address();
        Set<CartLineItemWrapper> cartLineItemWrappers = new HashSet<CartLineItemWrapper>();

        for (CartLineItem lineItem : cartLineItems) {
            if(lineItem.isType(EnumCartLineItemType.Product)||lineItem.isType(EnumCartLineItemType.Subscription)){
                if (lineItem.getProductVariant() != null) {
                    ProductVariant productVariant = lineItem.getProductVariant();
                    double variantMarkedPrice = productVariant.getMarkedPrice();
                    double variantHKPrice = productVariant.getHkPrice(lineItem.getOrder().getUser().getRoleStrings());
                    CartLineItemConfig lineItemConfig = lineItem.getCartLineItemConfig();

                    if (lineItemConfig != null) {
                        double configPrice = lineItemConfig.getPrice();
                        lineItem.setHkPrice(variantHKPrice + configPrice);
                        lineItem.setMarkedPrice(variantMarkedPrice + configPrice);
                    } else if (lineItem.getComboInstance() != null) {
                    }else if (lineItem.getOrder().isSubscriptionOrder()){
                        //this is to prevent price changes in subscription order line items
                    } else {
                        lineItem.setMarkedPrice(variantMarkedPrice);
                        lineItem.setHkPrice(variantHKPrice);
                    }
                }
                if(!lineItem.isType(EnumCartLineItemType.Subscription)&& !lineItem.getOrder().isSubscriptionOrder()){
                    lineItem.setDiscountOnHkPrice(0D);
                }
                cartLineItemWrappers.add(new CartLineItemWrapper(lineItem));
            }
        }

        return cartLineItemWrappers;
    }

    /**
     * <p/> This is a single invoice line containing the calculated shipping for all the items
     *
     * @param cartLineItems
     * @param address
     * @return
     */
    protected CartLineItemWrapper initShippingLineItem(Set<CartLineItem> cartLineItems, Address address) {

        Double shippingAmount = 30.0; // Default Shipping for order size < 500

        /*
         * for (LineItem lineItem : cartLineItems) { shippingAmount +=
         * lineItem.getProductVariantId().getShippingForQty(lineItem.getQty()); }
         */

        CartLineItem lineItem = new CartLineItemBuilder().ofType(EnumCartLineItemType.Shipping)
                // .tax(serviceTaxProvider.get())
                .hkPrice(shippingAmount).discountOnHkPrice(PricingConstants.DEFAULT_DISCOUNT).build();

        return new CartLineItemWrapper(lineItem, address);
    }

    protected CartLineItem createOrderLevelDiscountLineItem(ProductVariant productVariant, Double discount) {
        return new CartLineItemBuilder().ofType(EnumCartLineItemType.OrderLevelDiscount).forVariantQty(productVariant, 1L).discountOnHkPrice(discount).build();
    }

    private CartLineItem createRewardPointLineItem(Double redeemRewardPoints, Set<CartLineItem> cartLineItems, CartLineItemWrapper shippingCartLineItemWrapper) {
        PricingDto pricingDto = new PricingDto(cartLineItems, null);
        Double shipping = 0.0;
        if (shippingCartLineItemWrapper != null && pricingDto.getProductsHkSubTotal() < shippingFreeAfter) {
            shipping = shippingCartLineItemWrapper.getCartLineItem().getHkPrice();
        }
        return new CartLineItemBuilder().ofType(EnumCartLineItemType.RewardPoint).discountOnHkPrice(
                pricingDto.getProductsTotal() + pricingDto.getPrepaidServicesTotal() + shipping < redeemRewardPoints ? pricingDto.getProductsTotal()
                        + pricingDto.getPrepaidServicesTotal() + shipping : redeemRewardPoints).build();
    }

    public CartLineItem createRewardPointLineItemPOS (Order order, Double redeemRewardPoints) {
    	return new CartLineItemBuilder().ofType(EnumCartLineItemType.RewardPoint).discountOnHkPrice(
                order.getAmount()  < redeemRewardPoints ? order.getAmount() : redeemRewardPoints).build();
    }
}
