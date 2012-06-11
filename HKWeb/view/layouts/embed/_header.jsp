<%@ page import="com.hk.constants.core.EnumRole" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="org.apache.shiro.SecurityUtils" %>
<%@ page import="com.shiro.PrincipalImpl" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.constants.marketing.AnalyticsConstants" %>
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
    if (AnalyticsConstants.analytics) {
        if(session.getAttribute("signUpDate")!=null && session.getAttribute("signUpDate")!=""){
            String signUpDate=session.getAttribute("signUpDate").toString();
            session.removeAttribute("signUpDate");
%>
    <script type="text/javascript">
    var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
    document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
  </script>
  <script type="text/javascript">
    var pageTracker = _gat._getTracker("<%=AnalyticsConstants.gaCode%>");
       pageTracker._setCustomVar(
      3,                   // This custom var is set to slot #2.  first_order_date
      "signUpDate",     // The name acts as a kind of category for the user activity.  Required parameter.
      "<%=signUpDate%>",               // This value of the custom variable.  Required parameter.
      1                   // Sets the scope to visitor-level. Optional parameter.
    );
    //track signup date in event
    pageTracker._trackEvent('SignUp','signupDate','<%=signUpDate%>');
   </script>

<%
        }
    if(session.getAttribute("userId")!=null && session.getAttribute("userId")!="" ){
          String userId=session.getAttribute("userId").toString();
          session.removeAttribute("userId");
 %>
 <script type="text/javascript">
    var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
    document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
  </script>
  <script type="text/javascript">
    var pageTracker = _gat._getTracker("<%=AnalyticsConstants.gaCode%>");
       pageTracker._setCustomVar(
      4,                   // This custom var is set to slot #2.  first_order_date
      "userId",     // The name acts as a kind of category for the user activity.  Required parameter.
      "<%=userId%>",               // This value of the custom variable.  Required parameter.
      1                   // Sets the scope to visitor-level. Optional parameter.
    );
    pageTracker._trackEvent('Login','userId','<%=userId%>');
   </script>

<%
        }
    }
%>
  <div class='topBar'>
    <div class='topBarContent'>
      <div class='logoBox'>
        <s:link href="/" title='go to healthkart home'>
          <img src='<hk:vhostImage/>/images/logo.png' alt="healthkart logo"/>
        </s:link>
      </div>
      <div style='float: left; margin-left: 180px;'
           title='Call us on our customer care number for help regarding anything'>
        <span style="font-size: .8em;">Customer Care: 0124-4551616 <span style="color: gray;">(10am - 9pm, 7 days a week)</span></span>
        <div style="cursor:default; margin-top: 2px; width: 313px;">
          <s:form beanclass="com.hk.web.action.core.search.SearchAction" method="get" renderFieldsPresent="false"
                  renderSourcePage="false" autocomplete="off" style="position: relative;">
            <s:text name="query" id="searchbox" class="input_tip" title='search our catalog'
                    style="height: 14px; font-size: .8em;" value="${param['query']}" placeholder='search our catalog'/>

            <s:image name="search" src="/images/icons/search2.png" style="position: absolute; right: 3px; top: 1px;"/>
          </s:form>
        </div>
        <%--<div class='small'>--%>
        <%--(Monday to Saturday 10am - 8pm)--%>
        <%--</div>--%>
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
        <div class='left'>
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

          <div class='links'>
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
                <s:link beanclass="com.hk.web.action.core.auth.LogoutAction" class="toplinksSecondary" rel="noFollow">Logout</s:link>
              </shiro:lacksRole>
            </shiro:user>
          </div>
        </div>
        <div class='right'>
          <s:link beanclass="com.hk.web.action.core.cart.CartAction" rel="noFollow">
            <div class='cartButton' title='view your shopping cart'>

              <c:choose>
                <c:when test="${cartAction.itemsInCart > 1}">
                  <img class='icon' src='${pageContext.request.contextPath}/images/icons/cart.png'/>
                                    <span class='num' id="productsInCart">
                                      ${cartAction.itemsInCart}
                                    </span>
                  items in
                  <br/>
                  your shopping cart
                </c:when>
                <c:when test="${cartAction.itemsInCart == 1}">
                  <img class='icon' src='${pageContext.request.contextPath}/images/icons/cart.png'/>
                                    <span class='num' id="productsInCart">
                                       1
                                    </span>
                  item in
                  <br/>
                  your shopping cart
                </c:when>
                <c:otherwise>
                  <img class='icon'
                       src='${pageContext.request.contextPath}/images/icons/cart_empty.png'/>
                  your shopping
                  <br/>
                  cart is empty&nbsp;
                </c:otherwise>
              </c:choose>
            </div>
          </s:link>
        </div>
        <div class="floatfix"></div>
      </div>
    </div>
  </div>
  <script type="text/javascript" src="<hk:vhostJs/>/js/jquery.itvCommonPlugins.js"></script>
  <script type="text/javascript">
    $(document).ready(function() {
       $("#searchbox").autocomplete({url:'${pageContext.request.contextPath}/autocomplete-search/'});
    });
  </script>
</s:layout-definition>