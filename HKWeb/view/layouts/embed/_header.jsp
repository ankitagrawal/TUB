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
<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.cart.CartAction" var="cartAction" event="getCartItems"/>
<s:useActionBean beanclass="com.hk.web.action.core.discount.RewardPointTxnStatementAction" event="pre" var="rpBean"/>

<%
  PrincipalImpl principal = (PrincipalImpl) SecurityUtils.getSubject().getPrincipal();
		if(principal != null){
	        pageContext.setAttribute("userId", principal.getId());
		}
		else{
			pageContext.setAttribute("userId", null);
		}

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
              <s:link beanclass="com.hk.web.action.pages.ContactAction">Contact Us</s:link> | Email : <a href="mailto:info@healthkart.com">info@healthkart.com</a></div>
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
              	<shiro:hasRole name="<%=RoleConstants.HK_LOYALTY_USER%>">
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
                  </shiro:hasRole>
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
                  if (principal != null && principal.isAssumed()) {
                %>
                <s:link beanclass="com.hk.web.action.admin.user.AssumedLogoutAction" class="sml" rel="noFollow">(Release
                  assumed
                  identity)</s:link> |
                 <shiro:hasAnyRoles name="<%=RoleConstants.B2B_USER%>">
                  <s:link beanclass = "com.hk.web.action.core.b2b.B2BCartAction" class="sml" rel="noFollow">B2B Cart</s:link> |
                </shiro:hasAnyRoles>
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