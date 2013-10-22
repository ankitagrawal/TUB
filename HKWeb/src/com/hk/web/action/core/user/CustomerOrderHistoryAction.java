package com.hk.web.action.core.user;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HttpCache;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;
import com.hk.pact.service.order.OrderService;

@Secure
@Component
@HttpCache(allow = false)
public class CustomerOrderHistoryAction extends BasePaginatedAction {

  User user;

  List<Order> orderList;
  Page orderPage;
  @Autowired
   OrderService orderService;

  public Resolution pre() {
    if (getPrincipal() != null) {
      user = getUserService().getUserById(getPrincipal().getId());
      orderPage = orderService.listOrdersForUser(user, getPageNo(), getPerPage());
      orderList = orderPage.getList();
    }
      if (isHybridRelease()) {
          return new ForwardResolution("/pages/customerOrderHistoryBeta.jsp");
      }
    return new ForwardResolution("/pages/customerOrderHistory.jsp");
  }

  public User getUser() {
    return user;
  }

  public List<Order> getOrderList() {
    return orderList;
  }

  public int getPerPageDefault() {
    return 20;
  }

  public int getPageCount() {
    return orderPage != null ? orderPage.getTotalPages() : 0;
  }

  public int getResultCount() {
    return orderPage != null ? orderPage.getTotalResults() : 0;
  }

  public Set<String> getParamSet() {
    return new HashSet<String>();
  }

}