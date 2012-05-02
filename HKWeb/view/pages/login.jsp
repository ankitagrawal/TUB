<%@ page import="org.stripesstuff.plugin.security.J2EESecurityManager" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="com.hk.web.action.core.auth.LoginAction" %>
<%@ page import="net.tanesha.recaptcha.ReCaptcha" %>
<%@ page import="net.tanesha.recaptcha.ReCaptchaFactory" %>
<%@ page import="com.hk.constants.core.HealthkartConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<c:set var="redirectParam" value="<%=J2EESecurityManager.redirectAfterLoginParam%>"/>
<c:set var="source_checkout" value="<%=LoginAction.SOURCE_CHECKOUT%>"/>

<s:layout-render name="/layouts/loginLayout.jsp" pageTitle="Login or Signup to HealthKart.com">

  <s:layout-component name="htmlHead">
    <script type="text/javascript">
      $(document).ready(function() {
        $('#forgotPasswordWindow').jqm({trigger: '#forgotPasswordLink', ajax: '@href'});
      });
    </script>
  </s:layout-component>
  <s:layout-component name="modal">
    <div class="jqmWindow" id="forgotPasswordWindow" style="display: none;"></div>
  </s:layout-component>

  <s:layout-component name="checkoutStep">
    <div class='current_step_content login_page'>                     
      <div class='left'>
        <h3>
          Create a new Healthkart account
        </h3>

        <div class='signup'>
          <s:form beanclass="web.action.SignupAction">
            <s:errors/>
            <div class='label'>Name <span class='aster' title="this field is required">*</span></div>
            <s:text name="name" placeholder="Enter your name"/>
            <div class='label'>Email <span class='aster' title="this field is required">*</span></div>
            <s:text name="email" placeholder="Your Email"/>
            <div class='label'>Password <span class='aster' title="this field is required">*</span></div>
            <s:password name="password" placeholder="Give it a safe password"/>
            <div class='label'>Confirm Password <span class='aster' title="this field is required">*</span></div>
            <s:password name="passwordConfirm" placeholder="Reenter the password"/>
            <div class='label'>
              <s:checkbox name="agreeToTerms"/>Agree to
              <a href="${pageContext.request.contextPath}/pages/termsAndConditions.jsp">terms and conditions</a>
              <span class='aster' title="this field is required">*</span>
            </div>
            <s:submit name="signup" value="Signup" class="button"/>

            <s:hidden name="redirectUrl" value="${actionBean.redirectUrl!=null?actionBean.redirectUrl:param[redirectParam]}"/>
            <s:hidden name="source"/>
          </s:form>
        </div>
      </div>
      <div class='or'>
        or
      </div>
      <div class='right'>
        <h3>
          Login using an existing account
        </h3>

        <div class='login'>
          <s:form beanclass="web.action.LoginAction">
            <s:errors/>
            <div class="label">Email</div>
            <s:text name="email" id="loginName"/>
            <div class="label">Password</div>
            <s:password name="password"/>
            <s:link beanclass="web.action.ForgotPasswordAction" id="forgotPasswordLink">Forgot password?</s:link>

            <div>
              <s:submit name="login" value="Login" class="button"/>
            </div>
            <s:hidden name="redirectUrl" value="${actionBean.redirectUrl!=null?actionBean.redirectUrl:param[redirectParam]}"/>
            <s:hidden name="source"/>
          </s:form>
        </div>
      </div>
    </div>
  </s:layout-component>
</s:layout-render>
