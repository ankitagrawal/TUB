<%@ page import="org.stripesstuff.plugin.security.J2EESecurityManager" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="com.hk.web.action.core.auth.LoginAction" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<c:set var="redirectParam" value="<%=J2EESecurityManager.redirectAfterLoginParam%>"/>


<s:layout-render name="/layouts/loginLayout.jsp" pageTitle="Welcome to HealthKart B2B Portal">

  <s:layout-component name="htmlHead">
    <script type="text/javascript">
      function forgotPasswordPopup() {
        $('#forgotEmail').val($('#loginName').val());
        $('#forgotPasswordWindow').jqmShow();
      }
      function _sendNewPasswordEmail(res) {
        if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
          $('#forgotPasswordWindow .msg').html(res.message);
          $('#forgotPasswordWindow').jqmShow();
        } else if (res.code == '<%=HealthkartResponse.STATUS_ERROR%>') {
          $('#forgot-password-errors').html(getErrorHtmlFromJsonResponse(res));
        }
      }
      $(document).ready(function() {
        $('#forgotPassowrdForm').ajaxForm({dataType: 'json', success: _sendNewPasswordEmail});
        $('#forgotPasswordWindow').jqm();
      });

    </script>
  </s:layout-component>
  <s:layout-component name="modal">
    <div class="jqmWindow" id="forgotPasswordWindow">
      <s:layout-render name="/layouts/modal.jsp">
        <s:layout-component name="heading">Forgot your password?</s:layout-component>
        <s:layout-component name="content">
          <div id="forgot-password-errors"></div>

          <div class="msg" style="text-align: center; padding: 2px 0 2px 0; font-size: 1em;"></div>

          <div>
            <s:form beanclass="com.hk.web.action.core.user.ForgotPasswordAction" id="forgotPassowrdForm">
              <div style="text-align: center; padding: 5px 0 5px 0; font-size: 1em;">
                Enter your email address:
                <s:text id="forgotEmail" name="email"/>
                <br/> <br/>
                <span class="special"
                      style="font-size: 12px;">(your password will be emailed to you at this address)</span>
              </div>
              <br>

              <s:submit class="button_orange" name="forgotPassword" value="Send Password"/>
            </s:form>

          </div>
        </s:layout-component>
      </s:layout-render>
    </div>
  </s:layout-component>

  <s:layout-component name="checkoutStep">
    <div class='current_step_content'>
      <div class='left' style="height:300px;">
        <h3>
          Welcome to the HealthKart Family
        </h3>

        <p>We are proud to have you as a member of our growing Business family. Joining hands with HealthKart will
           guarantee you:</p>
        <ul>
          <li>assured genuine products</li>
          <li>great customer service</li>
          <li>timely delivery of products</li>
          <li>attractive pricing & loyalty schemes</li>
          <li>no hassles, no tension; just order online & relax!</li>
        </ul>
        <p>Sign up to enter HealthKart'e Exclusive Business Program. In case of any queries, call us at: 0124-455 1616
           or email at: info@healthkart.com</p>

        <div style="float:right; font-size: 1.2em; width: 85%; margin-top: 10px; margin-left:20px">
          <s:link beanclass="com.hk.web.action.core.b2b.B2BAction" event="goToSignUp" class="button_orange">Sign up Now!!
          </s:link>
        </div>

      </div>
      <div class='or'>
      </div>
      <div class='right' style="height:300px;">
        <h3>
          Login
        </h3>

        <div class='login'>
          <s:form beanclass="com.hk.web.action.core.b2b.B2BAction">
            <s:errors/>
            <s:messages/>
            <div class="label">Email</div>
            <s:text name="email" id="loginName"/>
            <div class="label">Password</div>
            <s:password name="password"/>
            <div class="label" style="margin-top:2px;margin-bottom:10px;"><s:checkbox name="rememberMe"/>&nbsp;Remember
                                                                                                         me
            </div>
            <a href="#" onclick="forgotPasswordPopup();">Forgot password?</a>

            <div>
              <s:submit name="login" value="Login" class="button"/>
            </div>
          </s:form>

        </div>
      </div>
    </div>
  </s:layout-component>
</s:layout-render>