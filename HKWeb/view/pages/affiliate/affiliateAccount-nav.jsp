<%@ page import="mhc.common.constants.RoleConstants" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<ul style="list-style: none; line-height: 25px;">
  <li><s:link beanclass="mhc.web.action.ReferralProgramAction" title="Referral Program"> <span id="rpLink">Referral Program</span> </s:link></li>
  <li><s:link beanclass="mhc.web.action.MyAccountAction" title="My Account"><span id="myAccountLink">My Account</span> </s:link></li>
  <li><s:link beanclass="mhc.web.action.CustomerOrderHistoryAction" title="Order History"> <span id="ohLink">Order History</span> </s:link></li>
  <shiro:hasRole name="<%=RoleConstants.AFFILIATE%>">
    <li><s:link beanclass="mhc.web.action.UserManageAddressAction" event="manageAddresses" title="My Addresses"><span id="myAddressesLink">My Addresses</span></s:link></li>
  </shiro:hasRole>
  <shiro:hasRole name="<%=RoleConstants.HK_AFFILIATE%>">
    <li>
      <s:link beanclass="mhc.web.action.affiliate.AffiliateAccountAction" title="Affiliate Account"><span id="affiliateAccountLink">Affiliate Account</span> </s:link>
    </li>
    <li><s:link beanclass="mhc.web.action.affiliate.AffiliateAccountAction" event="checksToAffiliate" title="Checks sent to me">
      <span id="myChecksLink">Checks Sent</span> </s:link></li>
    <li><s:link beanclass="mhc.web.action.affiliate.AffiliateStatisticsAction" title="Statistics">
      <span id="myTrafficStats">Statistics</span> </s:link></li>
    <li><s:link beanclass="mhc.web.action.affiliate.AffiliateInsightsAction" title="Insights">
  <span id="myReferredOrders">Insights</span></s:link></li>
  </shiro:hasRole>
</ul>