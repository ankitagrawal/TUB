<%@ page import="com.akube.framework.gson.JsonUtils" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page import="com.hk.constants.courier.StateList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>

<html>
  <head><title>Simple jsp page</title></head>
  <body>

    <s:form beanclass="com.hk.web.action.core.user.BillingAddressAction" id="newAddressForm">
          <s:hidden name="billingAddress.id"/>
          <span class="aster special">(Fields marked * are required.)</span>

          <div class='label'>Name<span class="aster">*</span></div>
          <s:text name="billingAddress.name"/>
          <div class='label'>Address Line 1<span class="aster">*</span></div>
          <s:text name="billingAddress.line1"/>
          <div class='label'>Address Line 2</div>
          <s:text name="billingAddress.line2"/>
          <div class='label'>City<span class="aster">*</span></div>
          <s:text name="billingAddress.city"/>
          <div class='label'>State<span class="aster">*</span></div>
          <%--<s:select  name="address.state" style="width:310px;">--%>
            <%--<s:option> </s:option>--%>
            <%--<c:forEach items="<%=StateList.stateList%>" var="state">--%>
              <%--<s:option value="${state}">${state}</s:option>--%>
            <%--</c:forEach>--%>
          <%--</s:select>--%>
            <s:text name="billingAddress.state"/>
          <%--<s:text name="address.state"/>--%>
          <div class='label'>PIN Code<span class="aster">*</span></div>
          <s:text name="billingAddress.pin" class="pincode" maxlength="6"/>
          <div class='label'>Phone / Mobile<span class="aster">*</span></div>
          <s:text name="billingAddress.phone"/>
          <s:submit name="save" value="Save the value and continue"/>
        </s:form>

    </body>

</html>