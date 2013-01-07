package com.hk.web.action.core.payment;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.RoleConstants;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.payment.EnumIssuerType;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.payment.GatewayIssuerMapping;
import com.hk.domain.payment.Issuer;
import com.hk.domain.user.User;
import com.hk.manager.OrderManager;
import com.hk.pact.dao.payment.GatewayIssuerMappingDao;
import com.hk.pact.service.payment.GatewayIssuerMappingService;
import com.hk.web.action.core.auth.LoginAction;
import com.hk.web.action.core.cart.CartAction;
import com.hk.web.action.core.user.SelectAddressAction;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.stripesstuff.plugin.security.Secure;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Secure(hasAnyRoles = {RoleConstants.HK_UNVERIFIED, RoleConstants.HK_USER}, authUrl = "/core/auth/Login.action?source=" + LoginAction.SOURCE_CHECKOUT, disallowRememberMe = true)
public class PaymentModeAction extends BaseAction {

  List<Issuer>                  bankIssuers;
  List<Issuer>                  cardIssuers;
  private Set<CartLineItem>     trimCartLineItems = new HashSet<CartLineItem>();
  private Integer               sizeOfCLI;


  @Autowired
  GatewayIssuerMappingService gatewayIssuerMappingService;

  @Autowired
  OrderManager orderManager;

  Order order;

  public Resolution pre() {
    User user = getUserService().getUserById(getPrincipal().getId());
    order = orderManager.getOrCreateOrder(user);
    if (order.getCartLineItems() == null || order.getCartLineItems().isEmpty()) {
      addRedirectAlertMessage(new SimpleMessage("There are no items in your cart, Please select at least 1 item"));
      return new RedirectResolution(CartAction.class);
    }
    if (order.getAddress() == null) {
      addRedirectAlertMessage(new SimpleMessage("You have not selected the shipping address"));
      return new RedirectResolution(SelectAddressAction.class);
    }
    Set<CartLineItem> oldCartLineItems =new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();
    order = orderManager.trimEmptyLineItems(order);
    Set<CartLineItem> newCartLineItems = order.getCartLineItems();
//    Collection<CartLineItem> diffCartLineItems = CollectionUtils.subtract(oldCartLineItems,newCartLineItems);
     Set<CartLineItem> diffCartLineItems = orderManager.getDiffCartLineItems(oldCartLineItems,newCartLineItems);
    if(diffCartLineItems!=null && diffCartLineItems.size()>0){
      trimCartLineItems.addAll(diffCartLineItems);
    }
    sizeOfCLI = order.getCartLineItems().size();
    bankIssuers = gatewayIssuerMappingService.getIssuerByType(EnumIssuerType.Bank.getId(),true);
    cardIssuers = gatewayIssuerMappingService.getIssuerByType(EnumIssuerType.Card.getId(),true);
    return new ForwardResolution("/pages/paymentMode.jsp");
  }

  public List<Issuer> getBankIssuers() {
    return bankIssuers;
  }

  public void setBankIssuers(List<Issuer> bankIssuers) {
    this.bankIssuers = bankIssuers;
  }

  public List<Issuer> getCardIssuers() {
    return cardIssuers;
  }

  public void setCardIssuers(List<Issuer> cardIssuers) {
    this.cardIssuers = cardIssuers;
  }

  public Integer getSizeOfCLI() {
    return sizeOfCLI;
  }

  public void setSizeOfCLI(Integer sizeOfCLI) {
    this.sizeOfCLI = sizeOfCLI;
  }

  public Set<CartLineItem> getTrimCartLineItems() {
    return trimCartLineItems;
  }

  public void setTrimCartLineItems(Set<CartLineItem> trimCartLineItems) {
    this.trimCartLineItems = trimCartLineItems;
  }
}
