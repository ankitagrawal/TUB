<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<ul style="list-style: none; line-height: 25px;">

  <li>
    <s:link beanclass="com.hk.web.action.core.user.MyAccountAction" title="My Account"><span id="myAccountLink">My Account</span> </s:link>
  </li>
  <li><s:link beanclass="com.hk.com.hk.web.action.core.user.CustomerOrderHistoryAction" title="Order History"> <span id="ohLink">Order History</span> </s:link>
  </li>
  <li><s:link beanclass="com.hk.com.hk.web.action.core.referral.ReferralProgramAction" title="Referral Program"> <span id="rpLink">Referral Program</span> </s:link>
  </li>
  <li><s:link beanclass="com.hk.web.action.core.discount.RewardPointTxnStatementAction" title="RewardPointTxnStatement"> <span id="rpsLink">Reward Point Txn Statement</span> </s:link>
  </li>
 <shiro:hasAnyRoles name="<%=RoleConstants.AFFILIATE%>">
    <li>
      <s:link beanclass="com.hk.web.action.core.affiliate.AffiliateManageAddressAction" event="manageAddresses" title="My Addresses"><span id="myAddressesLink">My Addresses</span></s:link>
    </li>
  </shiro:hasAnyRoles>
  <shiro:hasRole name="<%=RoleConstants.HK_AFFILIATE%>">
    <li>
      <s:link beanclass="com.hk.web.action.affiliate.AffiliateAccountAction" title="Affiliate Account"><span id="affiliateAccountLink">Affiliate Account</span> </s:link>
    </li>
    <li><s:link beanclass="com.hk.web.action.affiliate.AffiliateAccountAction" event="checksToAffiliate" title="Checks sent to me">
      <span id="myChecksLink">Checks Sent</span> </s:link></li>
        <li><s:link beanclass="com.hk.web.action.affiliate.AffiliateStatisticsAction" title="Statistics">
      <span id="myTrafficStats">Statistics</span> </s:link></li>
    <li><s:link beanclass="com.hk.web.action.affiliate.AffiliateInsightsAction" title="Insights">
  <span id="myReferredOrders">Insights</span> </s:link></li>
  </shiro:hasRole>
</ul>