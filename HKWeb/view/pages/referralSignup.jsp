<%@ page import="net.tanesha.recaptcha.ReCaptchaFactory" %>
<%@ page import="net.tanesha.recaptcha.ReCaptcha" %>
<%@ page import="com.hk.constants.core.HealthkartConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.core.referral.ReferralSignupAction" var="referralBean"/>

<s:layout-render name="/layouts/default.jsp" pageTitle="Check out HealthKart.com (and get an additional 5% discount)">
  <s:layout-component name="htmlHead">
    <meta name="description"
          content="HealthKart.com is India's first health and wellness online store. They are running a referral program where you get an additional 5% discount if you signup through this link. Check it out!"/>
    <link rel="image_src"
          href="${pageContext.request.contextPath}/images/logo/healthkart-logo.png"/>

    <style type="text/css">
      fieldset.right_label label {
        font-size: 1.2em;
      }

      .recaptcha_box {
        margin-bottom: 10px;
      }

      .recaptcha_label {
        text-align: right;
        width: 130px;
        padding-right: 15px;
        font-size: 1.2em;
      }
    </style>
  </s:layout-component>
  <s:layout-component name="heading">
    <br/>
    ${referralBean.user.firstName} has gifted you an additional 5% off to buy anything you
    like at HealthKart.com</s:layout-component>

  <s:layout-component name="left_col">


    <s:form beanclass="com.hk.web.action.core.user.SignupAction">
      <fieldset class="right_label">
          <%--<h2>Login Details</h2>--%>
        <div style="width: 600px; margin: 10px auto; background: #fafafa; padding: 20px;">
            You need to signup to redeem those points at healthkart.
          <div class="label">Name<span class="aster">*</span></div>
          <s:text name="name"/><br/>

          <div class="label">Email<span class="aster">*</span></div>
          <s:text name="email"/><span style="font-size: 10px; color: #aaa;">&nbsp; &nbsp; &nbsp; This will be your login ID for the site.</span>

          <div class="label">Password<span class="aster">*</span></div>
          <s:password name="password"/>
          <div class="label">Confirm Password<span class="aster">*</span></div>
          <s:password name="passwordConfirm"/>
          <div class="label">Agree to
            <a href="${pageContext.request.contextPath}/pages/termsAndConditions.jsp">T&C</a>

          &nbsp;<s:checkbox name="agreeToTerms"/></div>
          <p class="gry sml">(Fields marked <span class="aster">*</span> are required.)</p>
          <s:submit name="signup" value="Signup"/>


        </div>
      </fieldset>

      <%--<table class="recaptcha_box">
        <tr>
          <td class="recaptcha_label">Security Check *</td>
          <td style="" class="recaptcha">
            <%
              ReCaptcha captcha = ReCaptchaFactory.newReCaptcha(HealthkartConstants.recaptchaPublicKey, HealthkartConstants.recaptchaPrivateKey, false);
              out.print(captcha.createRecaptchaHtml(request.getParameter("error"), null));
            %>
          </td>
        </tr>
      </table>--%>


    </s:form>

  </s:layout-component>


</s:layout-render>
