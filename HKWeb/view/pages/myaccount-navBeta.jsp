<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<fieldset>
  <ul class="accnt-nav-box" style="width: 220px; line-height: 20px;float: left;padding: 0;font-size: 14px;">
    <li id="myAccountLink">
      <s:link  beanclass="com.hk.web.action.core.user.MyAccountAction" title="My Account">
            ACCOUNT
      </s:link>
        <span>&raquo;</span>
    </li>
      <li id="ohLink">
          <s:link  beanclass="com.hk.web.action.core.user.CustomerOrderHistoryAction" title="My Orders">
                Orders
          </s:link>
          <span>&raquo;</span>
      </li>

      <li id="emailLink" >
          <s:link beanclass="com.hk.web.action.core.user.MyAccountAction" event="subscribeForEmails" title="My Email Subscriptions">
                   Email Subscriptions
          </s:link>
          <span>&raquo;</span>
      </li>
    <li id="rpsLink" ><s:link beanclass="com.hk.web.action.core.discount.RewardPointTxnStatementAction" title="RewardPointTxnStatement">
        Reward Points </s:link>
        <span>&raquo;</span>
    </li>
    <li id="myAddressesLink">
      <s:link beanclass="com.hk.web.action.core.user.UserManageAddressAction" event="manageAddresses"
              title="My Addresses" > Addresses</s:link>
        <span>&raquo;</span>
    </li>
      <shiro:hasAnyRoles name="<%=RoleConstants.AFFILIATE%>">
      <li id="affiliateAccountLink">
        <s:link beanclass="com.hk.web.action.core.affiliate.AffiliateAccountAction" title="Affiliate Account" >
            Affiliate Account </s:link>
          <span>&raquo;</span>
      </li>
        </shiro:hasAnyRoles>
        <shiro:hasRole name="<%=RoleConstants.HK_AFFILIATE%>">
        <li id="affiliateShowCouponScreen">
            <s:link beanclass="com.hk.web.action.core.affiliate.AffiliateAccountAction" event="showCouponScreen" title="Download Coupon Codes" >
                Affiliate Program
            </s:link>
            <span>&raquo;</span>
        </li>
        <li id="myChecksLink"><s:link  beanclass="com.hk.web.action.core.affiliate.AffiliateAccountAction" event="checksToAffiliate" title="Payment Details">
        Payment Details
        </s:link>
            <span>&raquo;</span>
        </li>
      <li id="myTrafficStats" ><s:link beanclass="com.hk.web.action.core.affiliate.AffiliateStatisticsAction" title="Statistics">
        Statistics</s:link>
        <span>&raquo;</span>
      </li>
      <li id="myReferredOrders" ><s:link beanclass="com.hk.web.action.core.affiliate.AffiliateInsightsAction" title="Insights">
            Insights </s:link>
          <span>&raquo;</span>
      </li>
    </shiro:hasRole>
  </ul>
</fieldset>