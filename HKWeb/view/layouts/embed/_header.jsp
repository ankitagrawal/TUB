<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.constants.core.Keys" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page import="com.hk.domain.user.User" %>
<%@ page import="com.hk.pact.service.UserService" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.shiro.PrincipalImpl" %>
<%@ page import="org.apache.shiro.SecurityUtils" %>
<%@ page import="org.joda.time.DateTime" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.Date" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.cart.CartAction" var="cartAction" event="getCartItems"/>
<s:useActionBean beanclass="com.hk.web.action.core.discount.RewardPointTxnStatementAction" event="pre" var="rpBean"/>

<%

	String projectEnv = (String) ServiceLocatorFactory.getProperty(Keys.Env.projectEnv);
	pageContext.setAttribute("projectEnv", projectEnv);

  PrincipalImpl principal = (PrincipalImpl) SecurityUtils.getSubject().getPrincipal();
  UserService userService = ServiceLocatorFactory.getService(UserService.class);
  User loggedInUser = userService.getLoggedInUser();
  if (loggedInUser != null) {
    pageContext.setAttribute("user", loggedInUser);
    pageContext.setAttribute("userId", loggedInUser.getId());
    pageContext.setAttribute("userRoles", loggedInUser.getRoleStrings());
  } else {
    pageContext.setAttribute("userId", null);
  }

  String originalUrlHeader = (String) request.getAttribute("javax.servlet.forward.request_uri");
  if (originalUrlHeader == null) {
    originalUrlHeader = request.getRequestURI();
  }

  pageContext.setAttribute("tempUser", RoleConstants.TEMP_USER);
  pageContext.setAttribute("b2bUser", RoleConstants.B2B_USER);
  pageContext.setAttribute("loyaltyUser", RoleConstants.HK_LOYALTY_USER);
  pageContext.setAttribute("hkRoles", Arrays.asList(RoleConstants.HK_UNVERIFIED, RoleConstants.HK_USER));
  pageContext.setAttribute("groupAdminRoles", RoleConstants.ROLE_GROUP_ADMINS_LIST);
%>

<s:layout-definition>
  <c:set var="attachRedirectParam" value="${attachRedirectParam}"/>
  <%
    String attachRedirectParamStr = (String) pageContext.getAttribute("attachRedirectParam");
//    System.out.println("aattachRedirectParam="+attachRedirectParamStr);
    boolean attachRedirectParam = attachRedirectParamStr == null ? true : Boolean.getBoolean(attachRedirectParamStr);
  %>
  <%
    DateTime currentDateTime = new DateTime();
    Date startOfOfferDate = new Date(new DateTime(2013, 01, 25, 19, 59, 59, 59).getMillis());
    Date endOfOfferDate = new Date(new DateTime(2013, 01, 27, 8, 59, 59, 59).getMillis());
  %>

  <div class='topBar'>
    <div class='topBarContent'>
      <div style='float: left; margin-left: 5px; margin-top: 2px; line-height: 18px;' title='Write to us on our customer care email for help regarding anything'>
          <div style="font-size: 12px; float: left;">
            <a href="${pageContext.request.contextPath}/pages/returnAndCancellations.jsp">
              14 day return policy</a> |
              <s:link beanclass="com.hk.web.action.pages.ContactAction">Contact Us</s:link> | Email : <a href="mailto:info@healthkart.com">info@healthkart.com</a>
	          <c:if test="${projectEnv != 'prod'}">
		          <div align="center" style="height:30px; font-size:20px; color:red; font-weight:bold;">
			          Current Environment: ${projectEnv}
		          </div>
	          </c:if>
          </div>
              <% if (currentDateTime.isAfter(startOfOfferDate.getTime()) && currentDateTime.isBefore(endOfOfferDate.getTime())){%>
                <div style="color: red; float: left; ">&nbsp;(not available on 26th Jan 2013)</div>
              <%}%>
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
           </a>
          Welcome,
            <span class='name'>
              <c:if test="${hk:collectionContains(userRoles, tempUser)}">
              Guest
             </c:if>
             <shiro:guest>
               Guest
             </shiro:guest>
                <strong>
                  <c:if test="${hk:collectionContainsAnyCollectionItem(userRoles, hkRoles)}">
                    ${user.firstName}
                  </c:if>
                </strong>
              	<c:if test="${hk:collectionContains(userRoles, loyaltyUser)}">
                <c:set var="badge" value="${hk:getBadgeInfoForUser(userId)}"/>
          		 <a href="${pageContext.request.contextPath}/loyaltypg" target="_blank">
                  <c:choose>
                    <c:when test="${badge.badgeName == 'PLATINUM'}">
                      <span style="font-size:1.1em;color:white;
                       background-image:url('${pageContext.request.contextPath}/pages/loyalty/resources/images/platinum1.png');">Platinum</span>
                    </c:when>
                    <c:when test="${badge.badgeName == 'GOLD'}">
                      <span style="font-size:1.1em;color:white;
                      background-image:url('${pageContext.request.contextPath}/pages/loyalty/resources/images/gold1.png');">Gold</span>
                    </c:when>
                    <c:when test="${badge.badgeName == 'SILVER'}">
                      <span style="font-size:1.1em;color:white;
                      background-image:url('${pageContext.request.contextPath}/pages/loyalty/resources/images/silver1.png');">Silver</span>
                    </c:when>
                    <c:when test="${badge.badgeName == 'BRONZE'}">
                      <span style="font-size:1.1em;color:white;
                      background-image:url('${pageContext.request.contextPath}/pages/loyalty/resources/images/bronze1.png');">Bronze</span>
                    </c:when>
                    
                  </c:choose>
                  </a>
                  </c:if>
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
            <c:if test="${hk:collectionContains(userRoles, tempUser)}">
              <s:link beanclass="com.hk.web.action.core.auth.LoginAction" class="toplinksSecondary"
                      rel="noFollow"><%if (attachRedirectParam) {%><s:param name="redirectUrl"
                                                                            value="<%=originalUrlHeader%>"/><%}%>
                Login</s:link> |
              <s:link beanclass="com.hk.web.action.core.auth.LoginAction" class="toplinksSecondary"
                      rel="noFollow"><%if (attachRedirectParam) {%><s:param name="redirectUrl"
                                                                            value="<%=originalUrlHeader%>"/><%}%>Signup</s:link>
            </c:if>
            <shiro:guest>
              <s:link beanclass="com.hk.web.action.core.auth.LoginAction" class="toplinksSecondary"
                      rel="noFollow"><%if (attachRedirectParam) {%><s:param name="redirectUrl"
                                                                            value="<%=originalUrlHeader%>"/><%}%>
                Login</s:link> |
              <s:link beanclass="com.hk.web.action.core.auth.LoginAction" class="toplinksSecondary"
                      rel="noFollow"><%if (attachRedirectParam) {%><s:param name="redirectUrl"
                                                                            value="<%=originalUrlHeader%>"/><%}%>Signup</s:link>
            </shiro:guest>
            <c:if test="${hk:collectionContainsAnyCollectionItem(userRoles, hkRoles)}">
                <s:link beanclass="com.hk.web.action.core.user.MyAccountAction"
                        title='view past orders / edit personal details' rel="noFollow">Your Account</s:link> |
                <c:if test="${hk:collectionContainsAnyCollectionItem(userRoles, groupAdminRoles)}">
                  <s:link beanclass="com.hk.web.action.admin.AdminHomeAction" class="sml" rel="noFollow">Admin</s:link>
                  |
                </c:if>
                <%
                  if (principal != null && principal.isAssumed()) {
                %>
                <s:link beanclass="com.hk.web.action.admin.user.AssumedLogoutAction" class="sml" rel="noFollow">(Release
                  assumed
                  identity)</s:link> |
                 <c:if test="${hk:collectionContains(userRoles, b2bUser)}">
                  <s:link beanclass = "com.hk.web.action.core.b2b.B2BCartAction" class="sml" rel="noFollow">B2B Cart</s:link> |
                </c:if>
                <%
                  }
                %>
                <s:link beanclass="com.hk.web.action.core.auth.LogoutAction" class="toplinksSecondary"
                        rel="noFollow">Logout</s:link>
              </c:if>
          </span>
        </div>

      </div>
    </div>
  </div>
</s:layout-definition>