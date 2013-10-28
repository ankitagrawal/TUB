<%@ page import="com.hk.constants.core.HealthkartConstants" %>
<%@ page import="net.tanesha.recaptcha.ReCaptchaFactory" %>
<%@ page import="net.tanesha.recaptcha.ReCaptcha" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.user.RequestCallbackAction" var="sdcActionBean" event="pre"/>
<s:layout-render name="/layouts/loginLayoutBeta.jsp" pageTitle="Forgot Password | Healthkart.com">
  <s:layout-component name="checkoutStep">
      <div class='current_step_content' style="font-size: 14px; margin-top: 15px; padding-top: 0;">
          <div class='left' style="margin: 0 auto;float: none;">
              <h4 class="mrgn-b-20">
                  FORGOT PASSWORD
              </h4>
              <div id="forgot-password-errors"></div>
              <div class="msg red" style="text-align: left; color: blue;"></div>
              <p>
                  Enter the e-mail address associated
                  with your account, then click continue.
                  We'll email you a link to a page where you
                  can easily create a new password.
              </p>
              <div class='forgot_pwd'>
                  <s:form beanclass="com.hk.web.action.core.user.ForgotPasswordAction" id="forgotPassowrdForm">
                      <s:errors/>
                      <fieldset class="mrgn-b-10">
                          <label class="mrgn-t-10">Email Id</label>
                          <s:text id="forgotEmail" name="email" class="signUpInputNew"/>
                          <br/>
                      <s:submit style="float: left;" id="forgotPassword" value="Continue" class="btn btn-blue" name="forgotPassword" />
                      <div style="float: right;">
                          <span class="icn icn-sqre-blue"></span>
                          <s:link class="txt-blue" beanclass="com.hk.web.action.core.auth.LoginAction" event="pre" style="color:#0091d7;">
                              go back &amp; login</s:link>

                      </div>
                      </fieldset>

                  </s:form>
              </div>
          </div>
      </div>
    <script type="text/javascript">
      function _sendNewPasswordEmail(res) {
          if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
            $('#forgot-password-errors').hide();
            $('.msg').show().css({'border': '1px solid blue', 'margin': '0px 0 10px 0', 'padding': '10px'}).html(res.message);
            $('#forgotPassword').attr("disabled", true);
        } else if (res.code == '<%=HealthkartResponse.STATUS_ERROR%>') {
            $('.msg').hide();
            $('#forgot-password-errors').show().html(getErrorHtmlFromJsonResponse(res));
        }
      }
      $('#forgotPassowrdForm').ajaxForm({dataType: 'json', success: _sendNewPasswordEmail});
    </script>
  </s:layout-component>
</s:layout-render>