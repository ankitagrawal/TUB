<%@ page import="com.hk.constants.core.HealthkartConstants" %>
<%@ page import="net.tanesha.recaptcha.ReCaptchaFactory" %>
<%@ page import="net.tanesha.recaptcha.ReCaptcha" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/default.jsp" pageTitle="Forgot Password">
  <s:layout-component name="heading">Forgot Password</s:layout-component>
  <s:layout-component name="lhsContent">

    <s:form beanclass="com.hk.web.action.core.user.ForgotPasswordAction" method="post">
      <p class="lrg em">Please enter the email address used to make your account. We will send you an e-mail with instructions to reset it.</p>
      <fieldset class="top_label">
        <ul>
          <li><label>
          Email
          </label>
            <s:text name="email"/>
          </li>
          <li>
            <label>Tell us you are human</label>
            <%
              ReCaptcha captcha = ReCaptchaFactory.newReCaptcha(HealthkartConstants.recaptchaPublicKey, HealthkartConstants.recaptchaPrivateKey, false);
              out.print(captcha.createRecaptchaHtml(request.getParameter("error"), null));
            %>
          </li>
        </ul>
      </fieldset>
      <div class="buttons"><s:submit name="forgotPassword" value="Submit"/></div>

    </s:form>


  </s:layout-component>

</s:layout-render>
