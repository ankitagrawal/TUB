<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.user.MyAccountAction" var="maa"/>

<s:layout-render name="/layouts/defaultBeta.jsp">
  <s:layout-component name="heading">My Account</s:layout-component>

  <s:layout-component name="lhsContent">
    <jsp:include page="myaccount-navBeta.jsp"/>
  </s:layout-component>

  <s:layout-component name="rhsContent">
      <div class="mrgn-l-40">
    <s:form beanclass="com.hk.web.action.core.user.MyAccountAction">
      <div style="font-size:0.8em;">
        <h4 class="strikeline"> Change Password</h4>

        <div style="margin-top:15px"></div>

        <div class="row">
          <label class="rowLabel">Old Password</label>
          <s:password name="oldPassword" class="rowText" style="width: 180px;" id="oldPasswordText"/>
          <s:label name="Kindly entered your old password!" class="errorOldPassword"/>
        </div>

        <div class="clear"></div>
        <div style="margin-top:5px"></div>

        <div class="row">
          <label class="rowLabel">New Password</label>
          <s:password name="newPassword" class="rowText" id="newPasswordText" style="width: 180px;"/>
          <s:label name="Password must be of 1-20 characters!" class="errorNewPassword"/>
        </div>

        <div class="clear"></div>
        <div style="margin-top:5px"></div>

        <div class="row">
          <label class="rowLabel">Confirm Password</label>
          <s:password name="confirmPassword" class="rowText" id="confirmPasswordText" style="width: 180px;"/>
          <s:label name="Entered value doesnot match with the new password entered!" class="errorConfirmPassword"/>
        </div>
      </div>
      <div style="float: right; font-size: 0.7em; width: 75%; margin-top: 10px;">
        <s:submit name="changePassword" value="Change Password" class="button_orange" id="submitButton"/>
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

    $('#oldPasswordText').change(_validateOldPassword);
    $('#newPasswordText').change(_validateNewPassword);
    $('#confirmPasswordText').change(_validateConfirmPassword);

    $('#submitButton').click(function() {
      errorCount = 0;
      _validateOldPassword();
      _validateConfirmPassword();
      if ($('#oldPasswordText').val() == $('#newPasswordText').val()) {
        alert("New password same as the old password!");
        errorCount++;
      }
      return !errorCount;
    });

    $(document).click(function() {
      $('.errorOldPassword').fadeOut();
      $('.errorNewPassword').fadeOut();
      $('.errorConfirmPassword').fadeOut();
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
    padding-left: 26px;
  }

  .rowLabel {
    float: left;
    padding-right: 5px;
    padding-left: 5px;
    width: 200px;
    height: 24px;
    margin-top: 5px;
    font-weight: bold;
  }

  .rowText {
    float: left;
    border-width: 0;
    padding-top: 0;
    padding-bottom: 0;
    margin-left: 30px;
    font: inherit;
  }

  .errorOldPassword {
    float: left;
    color: black;
    width: 200px;
    font-size: 0.8em;
    margin: 5px 5px 5px 10px;
  }

  .errorNewPassword {
    float: left;
    color: black;
    width: 200px;
    font-size: 0.8em;
    margin: 5px 5px 5px 10px;
  }

  .errorConfirmPassword {
    float: left;
    color: black;
    width: 200px;
    font-size: 0.8em;
    margin: 5px 5px 5px 10px;
  }
</style>

