package com.hk.web.action.admin.subscription;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.beans.factory.annotation.Autowired;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.constants.core.PermissionConstants;
import com.hk.core.search.SubscriptionSearchCriteria;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.Order;
import com.hk.domain.subscription.Subscription;
import com.hk.domain.subscription.SubscriptionStatus;
import com.hk.pact.service.subscription.SubscriptionService;
import com.hk.web.action.error.AdminPermissionAction;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 7/12/12
 * Time: 3:25 PM
 */

@Secure(hasAnyPermissions = { PermissionConstants.SEARCH_SUBSCRIPTIONS }, authActionBean = AdminPermissionAction.class)
public class SearchSubscriptionAction extends BasePaginatedAction{

    private SubscriptionStatus subscriptionStatus;
    private Long subscriptionId;
    private Long          orderId;

    private String        login;
    private String        email;
    private String        name;
    private String        phone;

    private ProductVariant productVariant;

    private Date startDate;
    private Date          endDate;

    private List<Subscription> subscriptionList = new ArrayList<Subscription>();
    private Page subscriptionPage;
    private Order         order;

    @Autowired
    SubscriptionService subscriptionService;

    @DefaultHandler
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/subscription/searchSubscription.jsp");
    }

    public Resolution searchSubscriptions() {
        SubscriptionSearchCriteria subscriptionSearchCriteria = new SubscriptionSearchCriteria();
        // We need to do this not null check otherwise there would an 'in' clause for empty list

        if (subscriptionStatus != null){
            subscriptionSearchCriteria.setSubscriptionStatusList(Arrays.asList(subscriptionStatus));
        }
        subscriptionSearchCriteria.setSubscriptionId(subscriptionId);
        subscriptionSearchCriteria.setBaseOrderId(orderId);
        //subscriptionSearchCriteria.setPaymentStartDate(startDate).setPaymentEndDate(endDate);
        subscriptionSearchCriteria.setEmail(email).setLogin(login).setName(name).setPhone(phone).setProductVariant(productVariant);
        //subscriptionSearchCriteria.setOrderAsc(false);

        subscriptionPage = subscriptionService.searchSubscriptions(subscriptionSearchCriteria, getPageNo(), getPerPage());

        subscriptionList = subscriptionPage.getList();
        return new ForwardResolution("/pages/admin/subscription/searchSubscription.jsp");
    }

    public int getPerPageDefault() {
        return 20;
    }

    public int getPageCount() {
        return subscriptionPage == null ? 0 : subscriptionPage.getTotalPages();
    }

    public int getResultCount() {
        return subscriptionPage == null ? 0 : subscriptionPage.getTotalResults();
    }

    public Set<String> getParamSet() {
        Set<String> params = new HashSet<String>();
        params.add("subscriptionStatus");
        params.add("email");
        params.add("orderId");
        params.add("login");
        params.add("name");
        params.add("phone");
        params.add("startDate");
        params.add("endDate");
        params.add("productVariant");
        return params;
    }

    public SubscriptionService getSubscriptionService() {
        return subscriptionService;
    }

    public void setSubscriptionService(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    public SubscriptionStatus getSubscriptionStatus() {
        return subscriptionStatus;
    }

    public void setSubscriptionStatus(SubscriptionStatus subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<Subscription> getSubscriptionList() {
        return subscriptionList;
    }

    public void setSubscriptionList(List<Subscription> subscriptionList) {
        this.subscriptionList = subscriptionList;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public ProductVariant getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(ProductVariant productVariant) {
        this.productVariant = productVariant;
    }
}
