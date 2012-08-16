<%@ page import="org.stripesstuff.plugin.security.J2EESecurityManager" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="com.hk.web.action.core.auth.LoginAction" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<c:set var="redirectParam" value="<%=J2EESecurityManager.redirectAfterLoginParam%>"/>
<c:set var="source_checkout" value="<%=LoginAction.SOURCE_CHECKOUT%>"/>

<s:layout-render name="/layouts/loginLayout.jsp">

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
                <span class="special" style="font-size: 12px;">(your password will be emailed to you at this address)</span>
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
    <div class='current_step_content login_page'>
      <div class='left'>
        <h3>
          HealthKart presents Affiliate Program
        </h3>

        <p>Introduce people to the benefits of HealthKart and get benefitted in return. Take home a reward percentage on
          every purchase by the person you refer. So you earn, as we grow!!
        </p>

        <p>
          Please read the <a href="${pageContext.request.contextPath}/pages/affiliate/affiliateTnC.jsp">affiliate terms and conditions</a> for details on
          usage.
        </p>

        <div style="float:left; font-size: 1.2em; width: 85%; margin-top: 5px; margin-left:20px">
          <s:link beanclass="com.hk.web.action.core.affiliate.AffiliateAction" event="goToSignUp" class="button_orange">Join Now for Free
          </s:link>
        </div>

      </div>
      <div class='or'>
      </div>
      <div class='right'>
        <h3>
          Login using an existing account
        </h3>

        <div class='login'>
          <s:form beanclass="com.hk.web.action.core.affiliate.AffiliateAction">
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