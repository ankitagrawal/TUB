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

  <div class='topBar container_24'>
      <div class="fnt-sz10 fnt-light" style="text-align:right">

          <a href="${pageContext.request.contextPath}/beta/hk/FAQ.action?reftag=return" target="_blank">14 day return policy</a>&nbsp;
          <a href="${pageContext.request.contextPath}/beta/hk/ContactUs.action" target="_blank">Contact Us</a>&nbsp;
          ${user.firstName}
          |&nbsp; <s:link beanclass="com.hk.web.action.core.auth.LogoutAction" class="toplinksSecondary"
                  rel="noFollow">Logout</s:link>
      </div>
      <s:link href="/" title='go to healthkart home'>
      <img src="<hk:vhostImage/>/images/logo/HK-Logo.png" class="pad-r-10" alt="Healthkart" style="position:absolute;top:-2px;"/><br>
      </s:link>

    <div class='topBarContent' style="position: absolute;right:0px;">

      <!--div class="message">
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

        </div-->



      </div>
    </div>
  </div>

</s:layout-definition>