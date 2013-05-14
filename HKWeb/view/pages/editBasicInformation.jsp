<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.user.MyAccountAction" var="maa"/>
<s:layout-render name="/layouts/default.jsp">
  <s:layout-component name="heading">Your Account</s:layout-component>
  <s:layout-component name="htmlHead">
    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
  </s:layout-component>
  <s:layout-component name="lhsContent">
    <jsp:include page="myaccount-nav.jsp"/>
  </s:layout-component>

  <s:layout-component name="rhsContent">
    <s:form beanclass="com.hk.web.action.core.user.MyAccountAction">
      <s:errors/>
      <div>
        <h4 class="strikeline"> Basic Information</h4>

        <div style="margin-top: 15px"></div>

        <div style="font-size:0.8em;">
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
            <s:hidden name="b2bUser" value="${maa.b2bUser.id}"/>
            <s:hidden name="b2bUser.user" value="${maa.user.id}"/>
            <div class="row">
              <s:label class="rowLabel" name="Tin#"/>
              <s:text name="b2bUser.tin" value="${maa.b2bUser.tin}" class="rowText"/>
            </div>

            <div class="clear"></div>
            <div style="margin-top: 10px"></div>

            <div class="row">
              <s:label class="rowLabel" name="DL Number"/>
              <s:text name="b2bUser.dlNumber" value="${maa.b2bUser.dlNumber}" class="rowText"/>
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
            <s:select name="user.gender" class="rowText" style="height: 24px; border:1px solid #A2C4E5;">
              <s:option value="${gender}" selected="true">${gender}</s:option>
              <c:choose>
                <c:when test="${gender == null}">
                  <s:option value="Female">Female</s:option>
                  <s:option value="Male">Male</s:option>
                </c:when>
                <c:when test="${gender == 'Female'}">
                  <s:option value="Male">Male</s:option>
                </c:when>
                <c:otherwise>
                  <s:option value="Female">Female</s:option>
                </c:otherwise>
              </c:choose>
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

        <div style="float: right; font-size: 0.7em;">
          <s:submit name="saveBasicInformation" value="Update" class="button_orange"/>
        </div>

      </div>
    </s:form>

  </s:layout-component>
</s:layout-render>

<script type="text/javascript">
  window.onload = function() {
    document.getElementById('myAccountLink').style.fontWeight = "bold";
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
    padding-left: 26px;
  }

  .rowLabel {
    float: left;
    padding-right: 5px;
    padding-left: 5px;
    width: 100px;
    height: 24px;
    margin-top: 5px;
    font-weight: bold;
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
</style>