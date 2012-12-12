<%@ page import="com.hk.constants.core.EnumRole" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="org.apache.shiro.SecurityUtils" %>
<%@ page import="com.shiro.PrincipalImpl" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.constants.marketing.AnalyticsConstants" %>
<%@ page import="org.joda.time.DateTime" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.hk.taglibs.Functions" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.cart.CartAction" var="cartAction" event="getCartItems"/>
<s:useActionBean beanclass="com.hk.web.action.core.discount.RewardPointTxnStatementAction" event="pre" var="rpBean"/>

<%
  String roles = RoleConstants.HK_USER + "," + RoleConstants.HK_UNVERIFIED;

  String originalUrlHeader = (String) request.getAttribute("javax.servlet.forward.request_uri");
  if (originalUrlHeader == null) {
    originalUrlHeader = request.getRequestURI();
  }
%>

<s:layout-definition>
  <c:set var="attachRedirectParam" value="${attachRedirectParam}"/>
  <%
    String attachRedirectParamStr = (String) pageContext.getAttribute("attachRedirectParam");
//    System.out.println("aattachRedirectParam="+attachRedirectParamStr);
    boolean attachRedirectParam = attachRedirectParamStr == null ? true : Boolean.getBoolean(attachRedirectParamStr);
  %>
  <%
    DateTime dateTime = new DateTime();
    Date endOfOfferDate = new Date(new DateTime(2012, 12, 13, 23, 59, 59, 59).getMillis());
      if (dateTime.isBefore(endOfOfferDate.getTime())) {
  %>
  <!-- remove this after gosf -->
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/gosf.js"></script>
  <script>$(document).ready(function () {initSliderPaginator();});</script>
  <div id="slidebox">
    <a class="close"></a>

    <p>On popular demand, The Shopping Festival continues!! <br/><span class="gosfDisc">Over 500 products with Discounts up to <span style="font-size:1.2em;">80%</span>!</span></p>
    <div style="color: white; background-color: #4484c4; padding: 2px; text-align: center;">Another <strong><%=Functions.periodFromNow(endOfOfferDate)%></strong> remaining</div>
    <strong><a target="_blank" style="color:#e62580;" href="http://www.healthkart.com/online-shopping-festival?src=hk">Keep your wallets ready and your fingers on the mouse!</a></strong>
    <a class="more" target="_blank" href="http://www.healthkart.com/online-shopping-festival?src=hk">Browse Our Range >> </a>
</div>
<div id="pagerTrigger"></div>
<!-- remove this after gosf -->
  <%
      }
  %>

  <div class='topBar'>
    <div class='topBarContent'>
      <div style='float: left; margin-left: 5px; margin-top: 2px; line-height: 18px;' title='Call us on our customer care number for help regarding anything'>
          <div style="font-size: 12px; float: left;"><a href="${pageContext.request.contextPath}/pages/returnAndCancellations.jsp">14 day return policy</a> | <s:link beanclass="com.hk.web.action.pages.ContactAction">Contact Us</s:link>: 0124-4551616</div><div style="color: gray; float: left; font-size: 10px;">&nbsp;(24x7)</div>
      </div>
      <div class="message">
        <div class="arrow"></div>
        <span class="line1"></span>
        <s:link beanclass="com.hk.web.action.core.cart.CartAction" id="message_cart_link" rel="noFollow">
          <span class="line2">
            Proceed to checkout &rarr;
          </span>
        </s:link>

        <div class="close">(click anywhere to close this message)</div>
      </div>

      <div class='userCP'>

        <div style='float:right;'>

	        <div title='view your shopping cart'>
		        <div class="cartText">
			        <s:link beanclass="com.hk.web.action.core.cart.CartAction" rel="noFollow">
				        Cart (<span class='num' id="productsInCart">${cartAction.itemsInCart}</span>)
			        </s:link>
		        </div>
		        <s:link beanclass="com.hk.web.action.core.cart.CartAction" rel="noFollow">
			        <c:choose>
				        <c:when test="${cartAction.itemsInCart > 0}">
					        <img class='icon cartIcon' src='${pageContext.request.contextPath}/images/icons/cart.png'/>
				        </c:when>
				        <c:otherwise>
					        <img class='icon cartIcon'
					             src='${pageContext.request.contextPath}/images/icons/cart_empty.png'/>
				        </c:otherwise>
			        </c:choose>
		        </s:link>
	        </div>
          
        </div>

        <div style="margin-top: 2px; float: right; margin-right: 10px; font-size: 12px;">
          Welcome,
            <span class='name'>
              <shiro:hasRole name="<%=RoleConstants.TEMP_USER%>">
                Guest
              </shiro:hasRole>
              <shiro:guest>
                Guest
              </shiro:guest>
                <strong>
                  <shiro:hasAnyRoles name="<%=roles%>">
                    <shiro:principal property="firstName"/>
                  </shiro:hasAnyRoles>
                </strong>
            </span>
          <c:if test="${(rpBean.redeemablePoint - rpBean.user.userAccountInfo.overusedRewardPoints) > 0}">
            <s:link beanclass="com.hk.web.action.core.discount.RewardPointTxnStatementAction"
                    title="RewardPointTxnStatement"><span class="orange">(<fmt:formatNumber
                value="${rpBean.redeemablePoint - rpBean.user.userAccountInfo.overusedRewardPoints}"
                pattern="<%=FormatUtils.currencyFormatPattern%>"/>)</span>
            </s:link>
          </c:if>

          <span class='links'>
            |
            <shiro:hasRole name="<%=RoleConstants.TEMP_USER%>">
              <s:link beanclass="com.hk.web.action.core.auth.LoginAction" class="toplinksSecondary"
                      rel="noFollow"><%if (attachRedirectParam) {%><s:param name="redirectUrl"
                                                                            value="<%=originalUrlHeader%>"/><%}%>
                Login</s:link> |
              <s:link beanclass="com.hk.web.action.core.auth.LoginAction" class="toplinksSecondary"
                      rel="noFollow"><%if (attachRedirectParam) {%><s:param name="redirectUrl"
                                                                            value="<%=originalUrlHeader%>"/><%}%>Signup</s:link>
            </shiro:hasRole>
            <shiro:guest>
              <s:link beanclass="com.hk.web.action.core.auth.LoginAction" class="toplinksSecondary"
                      rel="noFollow"><%if (attachRedirectParam) {%><s:param name="redirectUrl"
                                                                            value="<%=originalUrlHeader%>"/><%}%>
                Login</s:link> |
              <s:link beanclass="com.hk.web.action.core.auth.LoginAction" class="toplinksSecondary"
                      rel="noFollow"><%if (attachRedirectParam) {%><s:param name="redirectUrl"
                                                                            value="<%=originalUrlHeader%>"/><%}%>Signup</s:link>
            </shiro:guest>
            <shiro:user>
              <shiro:lacksRole name="<%=RoleConstants.TEMP_USER%>">
                <s:link beanclass="com.hk.web.action.core.user.MyAccountAction"
                        title='view past orders / edit personal details' rel="noFollow">Your Account</s:link> |
                <shiro:hasAnyRoles name="<%=RoleConstants.ROLE_GROUP_ADMINS%>">
                  <s:link beanclass="com.hk.web.action.admin.AdminHomeAction" class="sml" rel="noFollow">Admin</s:link>
                  |
                </shiro:hasAnyRoles>
                <%
                  PrincipalImpl principal = (PrincipalImpl) SecurityUtils.getSubject().getPrincipal();
                  if (principal != null && principal.isAssumed()) {
                %>
                <s:link beanclass="com.hk.web.action.admin.user.AssumedLogoutAction" class="sml" rel="noFollow">(Release
                  assumed
                  identity)</s:link> |
                <%
                  }
                %>
                <s:link beanclass="com.hk.web.action.core.auth.LogoutAction" class="toplinksSecondary"
                        rel="noFollow">Logout</s:link>
              </shiro:lacksRole>
	            |
            </shiro:user>
          </span>
        </div>

      </div>
    </div>
  </div>
</s:layout-definition>