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

<s:layout-render name="/layouts/loginLayoutBeta.jsp" pageTitle="Login or Signup to HealthKart.com">

<%--
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
--%>

  <s:layout-component name="checkoutStep">
    <div class='current_step_content'>
      <div class='right'>
        <h4 class="mrgn-b-20">
            SIGN IN
        </h4>
        <div class='login'>
          <s:form beanclass="com.hk.web.action.core.auth.LoginAction">
            <s:errors/>

              <fieldset class="mrgn-b-20">
            <label class="">Email address</label>
            <s:text class="signUpInputNew" name="email" id="loginName"/>

              <label class="radio mrgn-b-5">
                  <input type="radio" name="type" class="auto-adjust check-empty" value="new"/> I am a new user
              </label>

              <label class="radio">
                  <input type="radio" name="type" class="auto-adjust check-empty" value="old" checked="checked"/> I am an
                  existing user, I have password
              </label>
              <br>

            <label class="">Password</label>
            <s:password class="signUpInputNew" name="password"/>
              </fieldset>
            <div style="float: left;">
              <s:submit name="login" value="Sign In" class="btn btn-blue"/>
            </div>
            <div style="float: right;"  name="forgot-link-cntnr">
                <span class="icn icn-sqre-blue"></span>
                <s:link  beanclass="com.hk.web.action.core.user.ForgotPasswordAction" id="forgotPasswordLink" style="color: #0091d7;">forgot password</s:link>
            </div>
            <s:hidden name="redirectUrl" value="${actionBean.redirectUrl!=null?actionBean.redirectUrl:param[redirectParam]}"/>
            <s:hidden name="source"/>
          </s:form>
        </div>
      </div>
    </div>
  </s:layout-component>
</s:layout-render>
