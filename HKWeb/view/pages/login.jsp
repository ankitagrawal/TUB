<%@ page import="com.hk.constants.core.Keys" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.web.action.core.auth.LoginAction" %>
<%@ page import="org.stripesstuff.plugin.security.J2EESecurityManager" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<c:set var="redirectParam" value="<%=J2EESecurityManager.redirectAfterLoginParam%>"/>
<c:set var="source_checkout" value="<%=LoginAction.SOURCE_CHECKOUT%>"/>
<%
    String vanillaForumUrl = (String) ServiceLocatorFactory.getProperty(Keys.Env.vanillaForumUrl);
    pageContext.setAttribute("vanillaForumUrl", vanillaForumUrl);
%>

<s:layout-render name="/layouts/loginLayout.jsp" pageTitle="Login or Signup to HealthKart.com">

  <s:layout-component name="htmlHead">
    <script type="text/javascript">
        $.ajax('http://${vanillaForumUrl}/book/forums/entry/signin');
      $(document).ready(function() {
        $('#forgotPasswordWindow').jqm({trigger: '#forgotPasswordLink', ajax: '@href'});
      });
    </script>
  </s:layout-component>
  <s:layout-component name="modal">
    <div class="jqmWindow" id="forgotPasswordWindow" style="display: none;"></div>
  </s:layout-component>

  <s:layout-component name="checkoutStep">
    <div class='current_step_content'>
      <div class='left' style="width: 410px;background: none;">
        <h3 style="margin-bottom: 1.75em;font-size: 16px;">
          Create a new Healthkart account
        </h3>

        <div class='signup' style="width: 410px; left: 0px">
          <s:form beanclass="com.hk.web.action.core.user.SignupAction">
            <s:errors/>
            <div class='label newLabel'>Name <span class='aster' title="this field is required">*</span></div>
            <s:text name="name" class="signUpInputNew" />
            <div class='label newLabel'>Email <span class='aster' title="this field is required">*</span></div>
            <s:text name="email" class="signUpInputNew" />
            <div class='label newLabel'>Password <span class='aster' title="this field is required">*</span></div>
            <s:password name="password" class="signUpInputNew" />
            <div class='label newLabel'>Confirm Password <span class='aster' title="this field is required">*</span></div>
            <s:password class="signUpInputNew" name="passwordConfirm"/>
            <div class='label' style="left: 15px;position: relative">
              <s:checkbox name="agreeToTerms"/>Agree to
              <s:link href="/pages/termsAndConditions.jsp">terms and conditions</s:link>
              <span class='aster' title="this field is required">*</span>
            </div>
            <s:submit name="signup" value="SIGNUP" class="button signUpButtonNew"/>

            <s:hidden name="redirectUrl" value="${actionBean.redirectUrl!=null?actionBean.redirectUrl:param[redirectParam]}"/>
            <s:hidden name="source"/>
          </s:form>
        </div>
      </div>
      <div class='right' style="width: 410px;background: none;position: relative;left: 80px;">
        <h3 style="margin-bottom: 1.75em;font-size: 16px;">
            Login using an existing account
        </h3>

        <div class='login'  style="width: 410px;left: 0px;">
          <s:form beanclass="com.hk.web.action.core.auth.LoginAction">
            <s:errors/>
            <div class="label newLabel">Email</div>
            <s:text class="signUpInputNew" name="email" id="loginName"/>
            <div class="label newLabel">Password</div>
            <s:password class="signUpInputNew" name="password"/>
            <s:link style="position: relative;left: 90px;" beanclass="com.hk.web.action.core.user.ForgotPasswordAction" id="forgotPasswordLink">Forgot password?</s:link>

            <div>
              <s:submit name="login" value="LOGIN" class="button signUpButtonNew"/>
            </div>
            <s:hidden name="redirectUrl" value="${actionBean.redirectUrl!=null?actionBean.redirectUrl:param[redirectParam]}"/>
            <s:hidden name="source"/>
          </s:form>
        </div>
      </div>
    </div>
  </s:layout-component>
</s:layout-render>
