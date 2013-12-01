package com.hk.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.hk.constants.order.EnumCartLineItemType;
import com.hk.domain.catalog.product.ProductGroup;
import com.hk.domain.offer.OfferTrigger;
import com.hk.domain.order.CartLineItem;

public class OfferTriggerMatcher {

  private Set<CartLineItemWrapper> cartLineItemWrappers;
  private OfferTrigger offerTrigger;
  private Map<ProductGroup, Long> pGroupMinQtyMap = new HashMap<ProductGroup, Long>(); // this map will be passed to the offerAction thru lineItemWrapper and will take care of the qty to be killed during the order level discount in case of combo discount
  private Boolean comboOffer = false;
  private Set<CartLineItem> cartLineItems;

  public OfferTriggerMatcher(Set<CartLineItemWrapper> cartLineItemWrappers, OfferTrigger offerTrigger) {
    this.cartLineItemWrappers = cartLineItemWrappers;
    this.offerTrigger = offerTrigger;
  }

  public OfferTriggerMatcher(OfferTrigger offerTrigger, Set<CartLineItem> cartLineItems) {
    this.offerTrigger = offerTrigger;
    this.cartLineItems = cartLineItems;
  }

  public Set<CartLineItemWrapper> getLineItemWrappers() {
    return cartLineItemWrappers;
  }

  public void setLineItemWrappers(Set<CartLineItemWrapper> cartLineItemWrappers) {
    this.cartLineItemWrappers = cartLineItemWrappers;
  }

  public Map<ProductGroup, Long> getPGroupMinQtyMap() {
    return pGroupMinQtyMap;
  }

  public Boolean isComboOffer() {
    return comboOffer;
  }

  public void setComboOffer(Boolean comboOffer) {
    this.comboOffer = comboOffer;
  }

  public Set<CartLineItem> getCartLineItems() {
    return cartLineItems;
  }

  public void setCartLineItems(Set<CartLineItem> cartLineItems) {
    this.cartLineItems = cartLineItems;
  }

  /**
   * The method cycles through the {@link #cartLineItemWrappers} and creates a map
   * productGroupQtyMap ->
   * key = productGroup,
   * value = quantity of product group matches found in the {@link mhc.domain.order.CartLineItem}'s
   * <p/>
   * Considerations:
   * <p/>
   * We need to take care about counting only the 'live' qty's from the line items
   * See {@link CartLineItemWrapper} for a definition of 'live' and 'dead' qty's
   * <p/>
   * Counting live qty's will basically enable us for multiple offers. When a previous offer that
   * has already discounted certain items, or maybe a previous trigger has already counted certain
   * items to satisfy itself, then those qty's should not count towards the current trigger.
   * <p/>
   * Similarly we will 'mark' any qty's that satisfy our trigger and if the *complete* trigger
   * is satisfied then we will 'kill' those qty's and they will then be 'dead' for any future discounts/triggers
   * <p/>
   * Certain triggers can also be of a type where they do not need to mark those qty's, that is, those qty's
   * are still eligible for discounts.
   *
   * @param exclude if exclude is true, then the qty that match the trigger will be killed
   * @return true/false depending on whether the match is successful or not respectively
   */
  public boolean hasMatch(boolean exclude) {

    if (offerTrigger.getProductGroup() != null) {
      Double eligibleSubTotal = 0.0;

      ProductGroup pGroup = offerTrigger.getProductGroup();
      Long pGroupQty = 0L;

      // cycle through the invoice lines
      Long triggerQty = offerTrigger.getQty() == null ? 0 : offerTrigger.getQty();

      for (CartLineItemWrapper cartLineItemWrapper : cartLineItemWrappers) {

        CartLineItem cartLineItem = cartLineItemWrapper.getCartLineItem();

        if (cartLineItem.isType(EnumCartLineItemType.Product) && pGroup.contains(cartLineItem.getProductVariant())) {
          eligibleSubTotal += cartLineItem.getProductVariant().getHkPrice(null) * cartLineItemWrapper.getLiveQty();

          // get 'qtyNeeded' for to satisfy the combo product group minimum qty
          // and calculate 'qtyMatched' i.e. the no. of qty that will be marked for killing
          long qtyNeeded = triggerQty - pGroupQty;
          Long qtyMatched = cartLineItemWrapper.getLiveQty() <= qtyNeeded ? cartLineItemWrapper.getLiveQty() : qtyNeeded;

          if (exclude) cartLineItemWrapper.mark(qtyMatched);
          pGroupQty += qtyMatched;
        }

      }

      if (offerTrigger.getAmount() != null && offerTrigger.getAmount() > eligibleSubTotal) {
        for (CartLineItemWrapper cartLineItemWrapper : cartLineItemWrappers) cartLineItemWrapper.releaseMarked();
        return false;
      }

      if (triggerQty != null && pGroupQty < triggerQty) {
        // release marked qty as the offer combo match has failed
        for (CartLineItemWrapper cartLineItemWrapper : cartLineItemWrappers) cartLineItemWrapper.releaseMarked();
        pGroupMinQtyMap.clear();
        comboOffer = false;
        return false;
      } else {
        if (triggerQty > 0) {
          pGroupMinQtyMap.put(offerTrigger.getProductGroup(), triggerQty);
          comboOffer = true;
        }
      }

    } else if (offerTrigger.getAmount() != null) {  // lotcha # case of minAmount required for combo products is handled within the above condition (offerCombo != null)
      Double cartSubTotal = 0.0;
      for (CartLineItemWrapper cartLineItemWrapper : cartLineItemWrappers) {
        if (cartLineItemWrapper.getCartLineItem().isType(EnumCartLineItemType.Product)) {
          cartSubTotal += cartLineItemWrapper.getCartLineItem().getProductVariant().getHkPrice(null) * cartLineItemWrapper.getLiveQty();
        }
      }
      return cartSubTotal >= offerTrigger.getAmount();

    }

    // kill qty only when the offer trigger has successfully matched
    for (CartLineItemWrapper cartLineItemWrapper : cartLineItemWrappers) cartLineItemWrapper.killMarked();
    return true;
  }

  // newly written, a less computational one, may not handle all the cases, written for cashback offer, dated 15/02/12
  public boolean hasEasyMatch(boolean exclude) {

    if (offerTrigger.getProductGroup() != null) {
      Double eligibleSubTotal = 0.0;

      ProductGroup pGroup = offerTrigger.getProductGroup();
      Long pGroupQty = 0L;

      // cycle through the invoice lines
      Long triggerQty = offerTrigger.getQty() == null ? 0 : offerTrigger.getQty();

      for (CartLineItem lineitem : cartLineItems) {

        if (pGroup.contains(lineitem.getProductVariant())) {
          eligibleSubTotal += lineitem.getProductVariant().getHkPrice(null) * lineitem.getQty();

          // get 'qtyNeeded' for to satisfy the combo product group minimum qty
          // and calculate 'qtyMatched' i.e. the no. of qty that will be marked for killing
          long qtyNeeded = triggerQty - pGroupQty;
          Long qtyMatched = lineitem.getQty() <= qtyNeeded ? lineitem.getQty() : qtyNeeded;

          pGroupQty += qtyMatched;
        }

      }

      if (offerTrigger.getAmount() != null && offerTrigger.getAmount() > eligibleSubTotal) {
        return false;
      }

      if (triggerQty != null && pGroupQty < triggerQty) {
        // false, as the offer combo match has failed
        return false;
      }

    } else if (offerTrigger.getAmount() != null) {  // lotcha # case of minAmount required for combo products is handled within the above condition (offerCombo != null)
      Double cartSubTotal = 0.0;
      for (CartLineItem lineItem : cartLineItems) {
        if (lineItem.isType(EnumCartLineItemType.Product)) {
          cartSubTotal += lineItem.getProductVariant().getHkPrice(null) * lineItem.getQty();
        }
      }
      return cartSubTotal >= offerTrigger.getAmount();

    }
    // return only when the offer trigger has successfully matched
    return true;
  }
}
