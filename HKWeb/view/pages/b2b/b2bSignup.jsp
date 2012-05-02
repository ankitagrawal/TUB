<%@ page import="org.stripesstuff.plugin.security.J2EESecurityManager" %>
<%@ page import="mhc.web.json.HealthkartResponse" %>
<%@ page import="web.action.core.affiliate.AffiliateAction" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

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
            <s:form beanclass="web.action.ForgotPasswordAction" id="forgotPassowrdForm">
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
      <s:errors/>
      <div class="left"></div>
      <div class='right'>
        <h3>
          Signup as a HealthKart partner
        </h3>

        <div class='signup'>
          <s:form beanclass="web.action.b2b.B2BAction">

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
              <a href="${pageContext.request.contextPath}/pages/b2b/b2bTnC.jsp">terms and conditions</a>
              <span class='aster' title="this field is required">*</span>
            </div>
            <s:submit name="signup" value="Signup" class="button"/>
          </s:form>
        </div>
      </div>
      
    </div>
  </s:layout-component>
</s:layout-render>
