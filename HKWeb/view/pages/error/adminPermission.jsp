<%@ page import="org.stripesstuff.plugin.security.J2EESecurityManager" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<c:set var="redirectParam" value="<%=J2EESecurityManager.redirectAfterLoginParam%>"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Admin | Unauthorized access">

  <s:layout-component name="heading">Unauthorized access</s:layout-component>

  <s:layout-component name="content">
    <p>You are not allowed to take this action as you may not have the authorization to do so, or you need to login again.</p>

    <p>
    <s:form beanclass="com.hk.web.action.core.auth.LoginAction">
      <s:errors/>
      <div class="label">Email</div>
      <s:text name="email" id="loginName" style="width:250px;"/>
      <div class="label">Password</div>
      <s:password name="password"/>
      <s:link beanclass="com.hk.web.action.core.user.ForgotPasswordAction" id="forgotPasswordLink">Forgot password?</s:link>

      <div>
        <s:submit name="login" value="Login" class="button"/>
      </div>
      <s:hidden name="redirectUrl" value="${param[redirectParam]}"/>
      <s:hidden name="source"/>
    </s:form>
    </p>
  </s:layout-component>
                        
</s:layout-render>
