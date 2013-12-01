package com.hk.util;

import java.util.HashSet;
import java.util.Set;

import com.hk.constants.discount.OfferConstants;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.domain.offer.OfferAction;
import com.hk.domain.offer.OfferInstance;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.user.Address;

public class CartLineItemWrapper {

  private CartLineItem cartLineItem;
  private Address address;

  private long deadQty;
  private long markedForKillQty;


  public CartLineItemWrapper(CartLineItem cartLineItem) {
    this.cartLineItem = cartLineItem;
    this.deadQty = 0;
  }

  public CartLineItemWrapper(CartLineItem cartLineItem, Address address) {
    this(cartLineItem);
    this.address = address;
  }

  public CartLineItem getCartLineItem() {
    return cartLineItem;
  }

  public long getDeadQty() {
    return deadQty;
  }

  public long getLiveQty() {
    return cartLineItem.getQty() - deadQty;
  }

  public boolean hasLiveQty() {
    return cartLineItem.getQty() > deadQty;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public long getMarkedQty() {
    return markedForKillQty;
  }

  public boolean hasMarkedQty() {
    return markedForKillQty > 0;
  }

  public static Set<CartLineItem> unwrap(Set<CartLineItemWrapper> cartLineItemWrappers) {
    Set<CartLineItem> lineItems = new HashSet<CartLineItem>();
    for (CartLineItemWrapper cartLineItemWrapper : cartLineItemWrappers) {
      lineItems.add(cartLineItemWrapper.getCartLineItem());
    }
    return lineItems;
  }

  public static boolean isAnyLive(Set<CartLineItemWrapper> cartLineItemWrappers) {
    for (CartLineItemWrapper cartLineItemWrapper : cartLineItemWrappers) {
      if (cartLineItemWrapper.getCartLineItem().isType(EnumCartLineItemType.Product) && cartLineItemWrapper.getLiveQty() > 0)
        return true;
    }
    return false;
  }

  public void killQty(long qty) {
    if ((deadQty + qty) > cartLineItem.getQty())
      throw new IllegalStateException("Dead qty cannot exceed total qty for " + cartLineItem);
    deadQty = deadQty + qty;
  }

  /**
   * This method is used to temporarily mark qty's (for killing). It is cumulative in nature.
   * It is needed as a temoporary state needs to be maintained in the offer combo computation in {@link OfferTriggerMatcher#hasMatch(boolean)}
   * <p/>
   * See {@link OfferTriggerMatcher#hasMatch(boolean)} for more details
   *
   * @param qty Add the given qty to the marked qty's
   */
  public void mark(long qty) {
    this.markedForKillQty += qty;
  }

  /**
   * This method kills {#markedForKillQty} no of qty's
   */
  public void killMarked() {
    killQty(markedForKillQty);
    releaseMarked();
  }

  /**
   * This method resets any qty's marked for kill back to 0
   */
  public void releaseMarked() {
    markedForKillQty = 0;
  }

  public void applyDiscount(OfferInstance offerInstance) {
    OfferAction offerAction = offerInstance.getOffer().getOfferAction();
    Long offerActionQty = offerAction.getQty() == null ? OfferConstants.INFINITE_QTY : offerAction.getQty();
    long offerQtyRemaining = offerActionQty;

//    The following logic is required if the offer is applicable across multiple orders. we are not building that logic for now.
//    long offerQtyRemaining = offerAction.getQty() - offerInstance.getQtyUsed();

    // discountQty is equal to cartLineItem live qty if (infinite offer) or (offer qty remaining > live qty)
    long discountQty = (offerActionQty == OfferConstants.INFINITE_QTY || offerQtyRemaining >= this.getLiveQty())
        ? this.getLiveQty() : offerQtyRemaining;

    if (discountQty > getLiveQty())
      throw new IllegalStateException("Discount cannot be applied to a qty (" + discountQty + ") greater than the live qty (" + getLiveQty() + ") for " + cartLineItem);
    if (discountQty == 0) {
      killQty(discountQty);
      return;
    }
    if (offerAction.getDiscountPercentOnHkPrice() == 0.0D && offerAction.getDiscountPercentOnMarkedPrice() == 0.0D) {
      killQty(discountQty);
      return;
    }

    killQty(discountQty);

    if (offerAction.getDiscountPercentOnMarkedPrice() != null && offerAction.getDiscountPercentOnMarkedPrice() > 0.0D) {
      cartLineItem.setDiscountOnHkPrice(cartLineItem.getMarkedPrice() * (offerAction.getDiscountPercentOnMarkedPrice() - cartLineItem.getProductVariant().getDiscountPercent()) * discountQty);
    } else {
      cartLineItem.setDiscountOnHkPrice(cartLineItem.getDiscountOnHkPrice() + cartLineItem.getHkPrice() * offerAction.getDiscountPercentOnHkPrice() * discountQty);
    }

  }

}
