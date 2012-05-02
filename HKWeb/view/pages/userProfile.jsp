<%@ page import="com.hk.constants.RoleConstants" %>
<%@ page import="mhc.web.json.HealthkartResponse" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="web.action.MyAccountAction" var="maa"/>
<s:layout-render name="/layouts/default.jsp">
  <s:layout-component name="heading">Your Account</s:layout-component>
  <s:layout-component name="lhsContent">
    <jsp:include page="myaccount-nav.jsp"/>
  </s:layout-component>

  <s:layout-component name="rhsContent">
    <div>
      <s:form beanclass="web.action.MyAccountAction">
        <s:errors/>
        <h4 class="strikeline"> Basic Information</h4>
        <shiro:hasRole name="<%=RoleConstants.HK_UNVERIFIED%>">
          <div class="prom yellow help" style="margin-bottom:20px; padding:5px;">
            <p class="lrg"><strong>Unverified Account</strong><br/>
              To verify, please click on the activation link sent to you via e-mail when signing up.</p>

            <p><strong>If you haven't received the mail,
              <s:link beanclass="web.action.ResendAccountActivationLinkAction" event="pre" class="resendActivationEmailLink">click here to resend it.</s:link>
            </strong>
              <br/><br/>
              <span class="emailSendMessage alert" style="display: none; font-weight:bold;"></span>
            </p>

            <p style="display:none;" class="emailNotReceived">
              If you do not receive this email, please check your spam/bulk folder.
              <br/>Write to us at info@healthkart.com if you face problems.
            </p>
          </div>
          <script type="text/javascript">

            <%-- Re-Send Activation Link --%>
            $('.resendActivationEmailLink').click(function() {

              var clickedLink = $(this);
              var clickedP = clickedLink.parents('p');
              clickedP.find('.emailSendMessage').html($('#ajaxLoader').html()).show();
              $.getJSON(clickedLink.attr('href'), function(res) {
                if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
                  clickedP.find('.emailSendMessage').html(res.data.message).show();
                  $('.emailNotReceived').show();
                }
              });
              return false;
            });

          </script>
        </shiro:hasRole>
        <s:hidden name="user" value="${maa.user.id}"/>
        <div class="label">Name</div>
        <s:text name="user.name"/>

        <div class="label">Email</div>
        <s:text name="user.email"/>

        <shiro:hasRole name="<%=RoleConstants.B2B_USER%>">
          <s:hidden name="b2bUserDetails" value="${maa.b2bUserDetails.id}"/>
          <s:hidden name="b2bUserDetails.user" value="${maa.user.id}"/>
          <div class="label">TIN#</div>
          <s:text name="b2bUserDetails.tin"/>

          <div class="label">DL Number</div>
          <s:text name="b2bUserDetails.dlNumber"/>
        </shiro:hasRole>

        <div style="float: right; font-size: 0.7em; width: 65%; margin-top: 10px;">
          <s:submit name="save" value="Update" class="button_orange"/>
        </div>
      </s:form>
    </div>
    <div style="margin-top: 50px;">
      <s:form beanclass="web.action.MyAccountAction">
        <s:errors/>
        <h4 class="strikeline"> Change Password</h4>


        <input type="hidden" name="login" value="${maa.user.login}"
               onfocus="if(this.value=='${maa.user.login}')this.value=''"
               onblur="if(this.value=='')this.value='${maa.user.login}'"/>

        <div class="label">Old Password</div>
        <s:password name="oldPassword" value=""
                    onfocus="if(this.value=='')this.value=''"
                    onblur="if(this.value=='')this.value=''"/>
        <div class="label">New Password</div>
        <input type="password" name="newPassword" value=""
               onfocus="if(this.value=='')this.value=''"
               onblur="if(this.value=='')this.value=''"/>

        <div class="label">Re-enter new password</div>
        <input type="password" name="confirmPassword" value=""
               onfocus="if(this.value=='')this.value=''"
               onblur="if(this.value=='')this.value=''"/>

        <div style="float: right; font-size: 0.7em; width: 75%; margin-top: 10px;">
          <s:submit name="changePassword" value="Change Password" class="button_orange"/>
        </div>
      </s:form>
    </div>

  </s:layout-component>

</s:layout-render>

<script type="text/javascript">
  window.onload = function() {
    document.getElementById('myAccountLink').style.fontWeight = "bold";
  };
</script>