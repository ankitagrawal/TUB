<%@ page import="com.hk.web.action.core.user.VerifyUserAction" %>
<%@ page import="com.hk.constants.OfferConstants" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.VerifyUserAction" var="verifyUserBean"/>

<c:set var="outcome_alreadyVerified" value="<%=VerifyUserAction.outcome_alreadyVerified%>"/>
<c:set var="outcome_invalidLink" value="<%=VerifyUserAction.outcome_invalidLink%>"/>
<c:set var="outcome_linkExpired" value="<%=VerifyUserAction.outcome_linkExpired%>"/>
<c:set var="outcome_success" value="<%=VerifyUserAction.outcome_success%>"/>

<s:layout-render name="/layouts/default.jsp" pageTitle="Account Verification">
  <s:layout-component name="heading">Account Verification</s:layout-component>
  <s:layout-component name="rhsContent">

    <c:if test="${verifyUserBean.outcome == outcome_alreadyVerified}">
      <p class="imp_note">
        Your account is already active.
      </p>
    </c:if>
    <c:if test="${verifyUserBean.outcome == outcome_invalidLink}">
      <p class="imp_note">
        Invalid Link.
      </p>
    </c:if>
    <c:if test="${verifyUserBean.outcome == outcome_linkExpired}">
      <p class="imp_note">
        The activation link has expired.
      </p>

      <shiro:hasRole name="<%=RoleConstants.HK_USER%>">
        <p>Your account has already been activated!</p>
      </shiro:hasRole>

      <shiro:hasAnyRoles name="<%=RoleConstants.ROLE_GROUP_NEEDS_ACTIVATION%>">
        <p>
          You can request a fresh activation link by going to the
          <s:link beanclass="com.hk.web.action.WelcomeAction"><strong>Activation page</strong></s:link>.
          If you are unable to receive the activation email then please check your spam/junk folder. For further help please write to us at info@healthkart.com
        </p>
      </shiro:hasAnyRoles>
    </c:if>
    <c:if test="${verifyUserBean.outcome == outcome_success}">
      <p class="imp_note">
        Thanks! Your account has now been activated.
      </p>

      <br/>

      <div class="buttons" align="left"><s:link beanclass="com.hk.web.action.core.cart.CartAction">PROCEED TO CART</s:link></div>
    </c:if>

    <div class="buttons" align="left"><s:link beanclass="com.hk.web.action.HomeAction" event="pre">
     START SHOPPING
    </s:link></div>

    <div class="buttons" align="left"><s:link beanclass="com.hk.web.action.ReferralProgramAction" event="pre">
      REFER YOUR FRIENDS
    </s:link></div>


    <shiro:notAuthenticated>

      <div class="buttons" align="left"><s:link beanclass="com.hk.web.action.LoginAction" event="pre">
        Login
      </s:link></div>
    </shiro:notAuthenticated>

    <div style="height:250px"></div>


  </s:layout-component>

</s:layout-render>
