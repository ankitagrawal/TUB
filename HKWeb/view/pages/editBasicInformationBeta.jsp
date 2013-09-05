<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.user.MyAccountAction" var="maa"/>
<s:layout-render name="/layouts/defaultBeta.jsp">
  <s:layout-component name="heading">Your Account</s:layout-component>
  <s:layout-component name="htmlHead">
    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
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
            <s:label name="Name cannot be greater than 80 characters!" class="error"/>
          </div>

          <div class="clear"></div>
          <div style="margin-top: 10px"></div>

          <div class="row">
            <s:label class="rowLabel" name="Email"/>
            <s:text name="user.email" value="${maa.user.email}" class="rowText"/>
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
          <s:submit name="saveBasicInformation" value="Update" style="display:inline-block;" class="btn btn-blue"/>
        </div>

      </div>
    </s:form>

  </s:layout-component>
</s:layout-render>

<script type="text/javascript">
  window.onload = function() {
    $('#myAccountLink').addClass('selected');
    document.getElementById('birthDate').labelify({labelledClass: 'input_tip'});
  };

  $(document).ready(function() {
    $('.error').hide();
    $('#userName').change(_validateUserName);

    $(document).click(function() {
      $('.error').fadeOut();
    });

    function _validateUserName() {
      if ($('#userName').val().length > 80) {
        $('.error').fadeIn();
        //        var position = $('#userName').position();
        //        var width = $('#userName').innerWidth();
        //        var height = $('#userName').innerHeight();
        //        $('.errorMessage .line1').html("Name cannot be greater than 80 characters!");
        //        $('.errorMessage').css("top", position.top);
        //        $('.errorMessage').css("height",height);
        //        $('.errorMessage').css("left", position.left + width);
        //        $('.errorMessage').css("left", position.left + width);
        //        $('.errorMessage').animate({
        //          opacity: 1
        //        }, 500);
      }
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
</style>