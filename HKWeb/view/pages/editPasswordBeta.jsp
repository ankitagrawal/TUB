<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.user.MyAccountAction" var="maa"/>

<s:layout-render name="/layouts/defaultBeta.jsp">
  <s:layout-component name="heading">My Account</s:layout-component>

    <s:layout-component name="centralContent">
        <%--breadcrumbs begins--%>
        <div class="hk-breadcrumb-cntnr mrgn-bt-10">
                <span>
                   <s:link beanclass="com.hk.web.action.HomeAction">Home</s:link>
                </span>
            <span>&raquo;</span>
            <span class="txt-blue fnt-bold">
                <s:link beanclass="com.hk.web.action.core.user.MyAccountAction">Account</s:link>
            </span>
            <span>&raquo;</span>
            <span class="fnt-bold">Edit Password</span>
        </div>
        <%--breadcrumbs ends--%>
    </s:layout-component>

  <s:layout-component name="lhsContent">
    <jsp:include page="myaccount-navBeta.jsp"/>
  </s:layout-component>

  <s:layout-component name="rhsContent">
      <div class="mrgn-l-40 my-acnt-ht">
    <s:form beanclass="com.hk.web.action.core.user.MyAccountAction">
      <div>
        <h2 class="strikeline" style="margin-bottom: 10px;"> Change Password</h2>


        <div class="row">
          <label class="rowLabel">Old Password</label>
          <s:password name="oldPassword" class="rowText"  id="oldPasswordText"/>
          <s:label name="Kindly enter your old password!" class="errorOldPassword"/>
        </div>

        <div class="clear"></div>

        <div class="row">
          <label class="rowLabel">New Password</label>
          <s:password name="newPassword" class="rowText" id="newPasswordText" />
          <s:label name="Password must be of 1-20 characters!" class="errorNewPassword"/>
          <s:label name="New password cannot be same as old password!" class="errorMatchOldPassword"/>

        </div>

        <div class="clear"></div>

        <div class="row">
          <label class="rowLabel">Confirm Password</label>
          <s:password name="confirmPassword" class="rowText" id="confirmPasswordText" />
          <s:label name="Entered value doesnot match with the new password entered!" class="errorConfirmPassword"/>
        </div>
      </div>
      <div style="float: right; font-size: 0.7em; width: 315px; margin-top: 10px;">
        <s:submit name="changePassword" value="Change Password" class="btn btn-blue" id="submitButton"/>
      </div>
    </s:form>
      </div>
  </s:layout-component>
</s:layout-render>
<script type="text/javascript">
  window.onload = function() {$('#myAccountLink').addClass('selected');};

  $(document).ready(function() {
    var errorCount = 0;
    $('.errorOldPassword').hide();
    $('.errorNewPassword').hide();
    $('.errorConfirmPassword').hide();
      $('.errorMatchOldPassword').hide();

    $('#oldPasswordText').change(_validateOldPassword);
    $('#newPasswordText').change(_validateNewPassword);
    $('#confirmPasswordText').change(_validateConfirmPassword);

    $('#submitButton').click(function() {
      errorCount = 0;
      _validateOldPassword();
      _validateConfirmPassword();
      if ($('#oldPasswordText').val() == $('#newPasswordText').val()) {
          $('.errorMatchOldPassword').fadeIn();
          //alert("New password same as the old password!");
        errorCount++;
      }
      return !errorCount;
    });

    $(document).click(function() {
      $('.errorOldPassword').fadeOut();
      $('.errorNewPassword').fadeOut();
      $('.errorConfirmPassword').fadeOut();
        $('.errorMatchOldPassword').fadeOut();
      $('#confirmPasswordText').val("");
    });

    function _validateOldPassword() {
      if (!($('#oldPasswordText').val().length)) {
        $('.errorOldPassword').fadeIn();
        errorCount++;
      }
    }

    function _validateNewPassword() {
      if ($('#newPasswordText').val().length > 20 || $('#newPasswordText').val().length < 1) {
        $('.errorNewPassword').fadeIn();
        errorCount++;
      }
    }

    function _validateConfirmPassword() {
      if (!($('#confirmPasswordText').val() == ($('#newPasswordText').val()))) {
        $('.errorConfirmPassword').fadeIn();
        errorCount++;
      }
      _validateNewPassword();
    }
  });
</script>
<style type="text/css">
  .row {
    margin-top: 0;
    float: left;
    margin-left: 0;
    padding-top: 2px;
  }

  .rowLabel {
    float: left;
    padding-right: 5px;
    width: 200px;
    height: 24px;
    margin-top: 5px;
  }

  .rowText {
    float: left;
    border-width: 0;
    padding-top: 0;
    padding-bottom: 0;
    margin-left: 30px;
    font: inherit;
  }


  .errorOldPassword, .errorNewPassword, .errorConfirmPassword, .errorMatchOldPassword {
      color: red;
      float: right;
      bottom: 15px;
      position: relative;
      width: 315px;
      margin: 0;
      clear: both;
  }


</style>

