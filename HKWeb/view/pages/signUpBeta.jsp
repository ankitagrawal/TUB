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
<s:layout-render name="/layouts/loginLayoutBeta.jsp" pageTitle="Signup to HealthKart.com">
  <s:layout-component name="checkoutStep">
    <div class='current_step_content' style="font-size: 14px; margin-top: 15px; padding-top: 0;">
      <div class='left' style="margin: 0 auto;float: none;">
        <h4 class="mrgn-b-20">
            REGISTRATION
        </h4>

        <div class='signup'>
          <s:form beanclass="com.hk.web.action.core.user.SignupAction">
            <s:errors/>
              <fieldset class="mrgn-b-20">
              <label>Your name<span class='aster' title="this field is required">*</span></label>
            <s:text name="name" class="signUpInputNew" />
            <label>Email Id<span class='aster' title="this field is required">*</span></label>
            <s:text name="email" class="signUpInputNew" />
            <label >Password <span class='aster' title="this field is required">*</span></label>
            <s:password name="password" class="signUpInputNew" />
            <label>Confirm Password <span class='aster' title="this field is required">*</span></label>
            <s:password class="signUpInputNew" name="passwordConfirm"/>
            <div class='label' style="margin-bottom: 15px;margin-top: 0;">
              <s:checkbox name="agreeToTerms" style="position: relative;margin: 0 5px;top: 3px;"/>Agree to
              <s:link href="/pages/termsAndConditions.jsp">terms and conditions</s:link>
              <span class='aster' title="this field is required">*</span>
            </div>

            <s:submit style="float: left;" name="signup" value="Create Account" class="btn btn-blue"/>
              <div style="float: right;">
                  <span class="icn icn-sqre-blue"></span>
                  <s:link class="txt-blue" beanclass="com.hk.web.action.core.auth.LoginAction" event="pre" style="color:#0091d7;">
                          go back &amp; login</s:link>


              </div>
              </fieldset>
            <s:hidden name="redirectUrl" value="${actionBean.redirectUrl!=null?actionBean.redirectUrl:param[redirectParam]}"/>
            <s:hidden name="source"/>
          </s:form>
        </div>
      </div>

    </div>
  </s:layout-component>
</s:layout-render>
