<%@include file="/includes/_taglibInclude.jsp" %>
<%@ page import="com.shiro.PrincipalImpl" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>

<s:layout-definition>

  <div class='footer'>
    <div class='contents'>
      <div class='column'>
        <h5>
          HEALTHKART
        </h5>
        <ul>
          <li><a href="/"><span class="txt-white">Home</span></a></li>
          <li>
            <a href="${pageContext.request.contextPath}/pages/aboutCompany.jsp"><span class="txt-white">About Us</span></a>
          </li>
          <li><a href="${pageContext.request.contextPath}/affiliate"><span class="txt-white">Affiliate</span></a></li>
          <%--<li><a href="${pageContext.request.contextPath}/b2b"><span class="txt-white">For Business</span></a></li>--%>
          <li><s:link beanclass="com.hk.web.action.pages.ContactAction"><span class="txt-white">Contact Us</span></s:link>
          </li>
          <li><a href="${pageContext.request.contextPath}/pages/termsAndConditions.jsp"><span class="txt-white">Terms & Conditions</span></a>
          </li>
          <li><a href="${pageContext.request.contextPath}/pages/returnAndCancellations.jsp"><span class="txt-white">Return and Cancellations</span></a>
          </li>
          <li><a href="${pageContext.request.contextPath}/blog" target="_blank"><span class="txt-white">Blog</span></a></li>
          <li><a href="${pageContext.request.contextPath}/pages/careers.jsp"><span class="txt-white">Careers</span></a>
          </li>

        </ul>
      </div>
      <div class='column'>
        <h5>
          Categories
        </h5>
        <s:useActionBean beanclass="com.hk.web.action.core.menu.MenuAction" var="menuAction" event="pre"/>
        <ul>
          <c:forEach items="${menuAction.menuNodes}" var="topMenuNode" varStatus="idx">
            <c:if test="${topMenuNode.name != 'Baby'}">
            <li><a href="${pageContext.request.contextPath}${topMenuNode.url}">${topMenuNode.name}</a></li>
            </c:if>
          </c:forEach>
        </ul>
      </div>
      <div class='column'>
        <h5>
          Your Account
        </h5>
        <ul>
          <shiro:hasRole name="<%=RoleConstants.TEMP_USER%>">
            <li><s:link beanclass="com.hk.web.action.core.auth.LoginAction" class="toplinksSecondary">Login</s:link></li>
            <li><s:link beanclass="com.hk.web.action.core.auth.LoginAction" class="toplinksSecondary">Signup</s:link></li>
          </shiro:hasRole>
          <shiro:notAuthenticated>
            <li><s:link beanclass="com.hk.web.action.core.auth.LoginAction" class="toplinksSecondary">Login</s:link></li>
            <li><s:link beanclass="com.hk.web.action.core.auth.LoginAction" class="toplinksSecondary">Signup</s:link></li>
          </shiro:notAuthenticated>
          <shiro:authenticated>
              <li><s:link beanclass="com.hk.web.action.core.auth.LogoutAction" class="toplinksSecondary">Logout</s:link></li>
          </shiro:authenticated>
          <shiro:lacksRole name="<%=RoleConstants.TEMP_USER%>">
            <li>
            <s:link beanclass="com.hk.web.action.core.user.MyAccountAction" title='view past orders / edit personal details'>Your Account</s:link></li>
            <li><s:link beanclass="com.hk.web.action.core.user.CustomerOrderHistoryAction">Order History</s:link></li>
            <li><s:link beanclass="com.hk.web.action.core.user.CustomerSubscriptionHistoryAction">Subscription History</s:link> </li>
            <li><s:link beanclass="com.hk.web.action.core.referral.ReferralProgramAction">Referral Program</s:link></li>
          </shiro:lacksRole>
        </ul>
      </div>
        <div class='column'>
            <h5>
                Misc
            </h5>
            <ul>
                <li>   <s:link beanclass="com.hk.web.action.core.subscription.AboutSubscriptionAction" event="pre" >Subscriptions </s:link> </li>
            </ul>
        </div>
      <div style="float:right;">
        <img src="<hk:vhostImage/>/images/banners/home/safe_secure_footer.gif" alt="safe and secure shopping at healthkart">
      </div>
      <script type="text/javascript">
        $("#contactBox").click(function() {
          document.location.href = '${pageContext.request.contextPath}/contact';
        });
      </script>
      <div class='floatfix'></div>
    </div>
    <div class='floatfix'></div>
    <div class="footer-bottom" style=" padding: 1 0;">
      <div class="fb-contents">
        <span class="serif"> &copy; 2012 healthkart.com</span>

        <a rel="nofollow" href="http://www.facebook.com/healthkart" target="_blank" style="border-bottom:none">
          <img src="<hk:vhostImage/>/images/banners/home/facebook.png"></a>
        <a rel="nofollow" href="http://www.twitter.com/healthkart" target="_blank" style="border-bottom:none">
          <img src="<hk:vhostImage/>/images/banners/home/twitter.png" alt="HealthKart Twitter">
        </a>
          <a href="https://plus.google.com/116027214366934328880?prsrc=3" rel="publisher" target="_blank" style="border-bottom:none">
          <img src="<hk:vhostImage/>/images/banners/home/googleplus.png" alt="HealthKart Google Plus">
        </a>
        <a href="${pageContext.request.contextPath}/blog" target="_blank" style="border-bottom:none">
          <img src="<hk:vhostImage/>/images/banners/home/Blog.png" alt="HealthKart Blog"></a>

        <div class='floatfix'></div>
      </div>
    </div>
  </div>
  <div class='floatfix'></div>
  </div>

</s:layout-definition>