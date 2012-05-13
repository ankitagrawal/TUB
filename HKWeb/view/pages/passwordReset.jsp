<%@ page import="org.stripesstuff.plugin.security.J2EESecurityManager" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.core.user.PasswordResetAction" var="resetBean" event="pre"/>

<s:layout-render name="/layouts/default.jsp" pageTitle="Password Reset">
  <s:layout-component name="heading">Change Password</s:layout-component>
  <s:layout-component name="rhsContent">

    <div>
      <div>
        <s:form beanclass="com.hk.web.action.core.user.PasswordResetAction" method="post">
            <s:errors/>
            <h4 class="strikeline"> Change Password</h4>
              <s:errors/>
              <s:hidden name="token" value="${resetBean.token}"/>
              <div class="label">Password:</div>
              <s:password name="password"/>
              <div class="label">Confirm Password: </div>
              <s:password name="passwordConfirm"/>
              <div style="float: right; font-size: 0.7em; width: 75%; margin-top: 10px;">
              <s:submit name="reset" value="Change Password" class="button_orange"/>
              </div>
        </s:form>
      </div>

    </div>

  </s:layout-component>

</s:layout-render>
