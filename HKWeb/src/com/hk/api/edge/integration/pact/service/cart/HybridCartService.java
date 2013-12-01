package com.hk.api.edge.integration.pact.service.cart;

import com.hk.api.edge.integration.response.cart.CartSummaryFromHKR;
import com.hk.domain.order.Order;

/**
 * @author Rimal
 */
public interface HybridCartService {

  public CartSummaryFromHKR getUserCartSummaryFromHKR(Long userId);

  public void loadOrderFromDB(Order order);
}
