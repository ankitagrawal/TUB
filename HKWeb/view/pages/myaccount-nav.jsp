<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<fieldset>
  <ul style="list-style: none; line-height: 25px;">
    <li>
      <s:link beanclass="com.hk.web.action.core.user.MyAccountAction" title="My Account"><span
          id="myAccountLink">MY ACCOUNT</span> </s:link>
    </li>
      <li>
          <s:link beanclass="com.hk.web.action.core.user.CustomerOrderHistoryAction" title="My Orders"> <span
                  id="ohLink">My Orders</span> </s:link>
      </li>
      <li>
          <s:link beanclass="com.hk.web.action.core.user.CustomerSubscriptionHistoryAction" title="My Subscriptions"> <span
                  id="shLink">My Subscriptions</span> </s:link>
      </li>
      <li>
          <s:link beanclass="com.hk.web.action.core.user.MyAccountAction" event="subscribeForEmails" title="My Email Subscriptions"> <span
                  id="shLink">My Email Subscriptions</span> </s:link>
      </li>
    <%--<li>
      <s:link beanclass="com.hk.web.action.core.referral.ReferralProgramAction" title="Referral Program"> <span
          id="rpLink">Referral Program</span> </s:link>
    </li>--%>
    <li><s:link beanclass="com.hk.web.action.core.discount.RewardPointTxnStatementAction"
                title="RewardPointTxnStatement"> <span
        id="rpsLink">Reward Point Txn Statement</span> </s:link>
    </li>
    <li>
      <s:link beanclass="com.hk.web.action.core.user.UserManageAddressAction" event="manageAddresses"
              title="My Addresses"><span id="myAddressesLink">My Addresses</span></s:link>
    </li>
      <shiro:hasAnyRoles name="<%=RoleConstants.AFFILIATE%>">
      <li>
        <s:link beanclass="com.hk.web.action.core.affiliate.AffiliateAccountAction" title="Affiliate Account"><span
            id="affiliateAccountLink">Affiliate Account</span> </s:link>
      </li>
        </shiro:hasAnyRoles>
        <shiro:hasRole name="<%=RoleConstants.HK_AFFILIATE%>">
        <li>
            <s:link beanclass="com.hk.web.action.core.affiliate.AffiliateAccountAction" event="showCouponScreen" title="Download Coupon Codes"><span
                    id="affiliateShowCouponScreen">Affiliate Offer Program</span> </s:link>
        </li>
        <li><s:link beanclass="com.hk.web.action.core.affiliate.AffiliateAccountAction" event="checksToAffiliate"
                  title="Payment Details">
        <span id="myChecksLink">Payment Details</span> </s:link></li>
      <li><s:link beanclass="com.hk.web.action.core.affiliate.AffiliateStatisticsAction" title="Statistics">
        <span id="myTrafficStats">Statistics</span> </s:link></li>
      <li><s:link beanclass="com.hk.web.action.core.affiliate.AffiliateInsightsAction" title="Insights">
        <span id="myReferredOrders">Insights</span> </s:link></li>
    </shiro:hasRole>


  </ul>
</fieldset>
