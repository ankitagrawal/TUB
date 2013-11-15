<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.user.MyAccountAction" var="maa"/>
<s:layout-render name="/layouts/defaultBeta.jsp">
  <s:layout-component name="heading">Your Account</s:layout-component>
  <s:layout-component name="htmlHead">
    <link href="<hk:vhostCss/>/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="<hk:vhostJs/>/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="<hk:vhostJs/>/js/calendar-en.js"></script>
    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
  </s:layout-component>

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
            <span class="fnt-bold">Edit Basic Information</span>
        </div>
        <%--breadcrumbs ends--%>
    </s:layout-component>

  <s:layout-component name="lhsContent">
    <jsp:include page="myaccount-navBeta.jsp"/>
  </s:layout-component>

  <s:layout-component name="rhsContent">
    <s:form beanclass="com.hk.web.action.core.user.MyAccountAction">
      <s:errors/>
      <div class="mrgn-l-40 my-acnt-ht">
        <h2 class="strikeline" style="margin-bottom: 10px;"> Basic Information</h2>


        <div>
          <s:hidden name="user"/>
          <div class="row">
            <s:label class="rowLabel" name="Name"/>
            <s:text name="user.name" value="${maa.user.name}" class="rowText" id="userName"/>
          </div>

          <div class="clear"></div>
          <div style="margin-top: 10px"></div>

          <div class="row">
            <s:label class="rowLabel" name="Email"/>
            <s:text name="user.email" value="${maa.user.email}" class="rowText" id="userEmail"/>
          </div>

          <div class="clear"></div>
          <div style="margin-top: 10px"></div>

          <shiro:hasRole name="<%=RoleConstants.B2B_USER%>">
            <s:hidden name="b2bUserDetails" value="${maa.b2bUserDetails.id}"/>
            <s:hidden name="b2bUserDetails.user" value="${maa.user.id}"/>
            <div class="row">
              <s:label class="rowLabel" name="Tin#"/>
              <s:text name="b2bUserDetails.tin" value="${maa.b2bUserDetails.tin}" class="rowText"/>
            </div>

            <div class="clear"></div>
            <div style="margin-top: 10px"></div>

            <div class="row">
              <s:label class="rowLabel" name="DL Number"/>
              <s:text name="b2bUserDetails.dlNumber" value="${maa.b2bUserDetails.dlNumber}" class="rowText"/>
            </div>

            <div class="clear"></div>
            <div style="margin-top: 10px"></div>
          </shiro:hasRole>

          <div class="row">
            <s:label class="rowLabel" name="Gender"/>
            <c:set var="gender" value="${maa.user.gender}"/>
              <%--<s:select name="user.gender" class="rowText" style="height: 24px; border:1px solid #A2C4E5;">--%>
              <%--<s:option value="Female">Female</s:option>--%>
              <%--<s:option value="Male">Male</s:option>--%>
              <%--</s:select>--%>
            <s:select name="user.gender" class="rowText" value="${maa.user.gender}" style="height: 24px; border:1px solid #A2C4E5;">
                <s:option value="">-Select One-</s:option>
                <s:option value="Male">Male</s:option>
                <s:option value="Female">Female</s:option>
            </s:select>
          </div>

          <div class="clear"></div>
          <div style="margin-top: 10px"></div>

          <div class="row">
            <s:label class="rowLabel" name="DOB"/>
            <s:text id="birthDate" class="date_input"
                    value="<%=FormatUtils.getFormattedDateForUserEnd(maa.getUser().getBirthDate())%>"
                    name="user.birthDate" style="width:100px;"/>
          </div>
        </div>

        <div class="clear"></div>
        <div style="margin-top: 10px"></div>

        <div style="float: left; width: 210px;text-align: right;">
          <s:submit name="saveBasicInformation" value="Update" style="display:inline-block;" class="btn btn-blue" id ="submitButton"/>
        </div>

      </div>
    </s:form>

  </s:layout-component>
</s:layout-render>

<script type="text/javascript">

    window.onload = function() {$('#myAccountLink').addClass('selected');};

    $(document).ready(function() {


//      $('#userName').change(_validateIsNullName);
//      $('#userEmail').change(_validateIsNullEmail);

      $('#submitButton').click(function(event) {
          var errorCount = false;
          $('#userName').siblings('.err-txt4').remove();
          $('#userName').siblings('.err-icn3').remove();
          $('#userEmail').siblings('.err-txt4').remove();
          $('#userEmail').siblings('.err-icn3').remove();
//          $('#userName').sibling('.icn-warning-small').remove();
          if(_validateIsNullName())
             errorCount = true;
          if(_validateIsNullEmail())
              errorCount = true;
          if(errorCount == true)
             event.preventDefault();
          return !errorCount;
      });

      $(document).click(function() {
          $('#userName').siblings('.err-txt4').fadeOut();
          $('#userName').siblings('.err-icn3').fadeOut();
          $('#userEmail').siblings('.err-txt4').fadeOut();
          $('#userEmail').siblings('.err-icn3').fadeOut();

      });

      function _validateIsNullName() {
          if (!($('#userName').val().length)) {
              $('#userName').after($('<span class="icn-warning-small err-icn3"></span>'));
              $('#userName').after($('<p class="err-txt4">Please enter your Name</p>'));
              return true;
          }
          return false;
      }

      function _validateIsNullEmail() {
          if (!($('#userEmail').val().length)) {
              $('#userEmail').after($('<span class="icn-warning-small err-icn3"></span>'));
              $('#userEmail').after($('<p class="err-txt4">Please enter Email address</p>'));
              return true;
          }
         else
             return _validateIsInvalidEmail();
      }

      function _validateIsInvalidEmail() {
            regexEMAIL = /^([A-Za-z0-9_\-\.\+])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
            if (! regexEMAIL.test($('#userEmail').val())) {
                $('#userEmail').after($('<span class="icn-warning-small err-icn3"></span>'));
                $('#userEmail').after($('<p class="err-txt4">Email address is not valid</p>'));
                return true;
            }
            return false;
        }

  });



</script>
<style type="text/css">
  .row {
    margin-top: 0;
    float: left;
    margin-left: 0;
    padding-top: 2px;
    max-height: 60px;
  }

  .rowLabel {
    float: left;
    padding-right: 5px;
    width: 100px;
    height: 24px;
    margin-top: 5px;

  }

  .rowText {
    float: left;
    border-width: 0;
    padding-top: 0;
    padding-bottom: 0;
    margin-left: 20px;
    font: inherit;
  }

  .error {
    float: left;
    color: black;
    width: 150px;
    font-size: 0.8em;
    margin: 5px 5px 5px 10px;
  }

  .date_input {
    width: 100px;
    float: left;
    border-width: 0;
    padding-top: 0;
    padding-bottom: 0;
    margin-left: 20px;
  }
  .row a{
        float: right;
    }

  .errorEnterName,  .errorEnterEmail,  .errorInvalidEmail {
      color: red;
      float: right;
      bottom: 15px;
      position: relative;
      width: 315px;
      margin: 0;
      clear: both;
  }
</style>