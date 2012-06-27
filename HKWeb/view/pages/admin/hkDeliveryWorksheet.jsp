<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.courier.HKDeliveryAction" var="hkdBean"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Healthkart Delivery">

<s:layout-component name="htmlHead">

  <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
  <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>

</s:layout-component>

<s:layout-component name="content">
<div class="hkDeliveryWorksheetBox">
  <s:form beanclass="com.hk.web.action.admin.courier.HKDeliveryAction">
    <fieldset class="right_label">
      <legend>Download Healthkart Delivery Worksheet</legend>
      <ul>

        <li>
          <label>Enter AWB Numbers:</label><br>(Please enter comma seperated values)
            <br>
            <br>
            <s:textarea name="trackingId" class="trackingIds" cols="52"
                            style="resize:none;word-wrap:break-word"></s:textarea>

            <%--<s:text class="date_input startDate" style="width:150px"
                                formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name=""/>--%>
        </li>
        <li>
          <s:submit name="downloadDeliveryWorkSheet" value="Download Delivery Worksheet"/>

        </li>
      </ul>
    </fieldset>
  </s:form>
</div>

</s:layout-component>
</s:layout-render>

