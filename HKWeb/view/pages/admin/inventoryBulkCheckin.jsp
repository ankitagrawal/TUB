<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="java.util.Date" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Inventory Bulk Checkin">
  <s:layout-component name="htmlHead">
    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
  </s:layout-component>
  <s:layout-component name="heading">Inventory Bulk Checkin</s:layout-component>
  <s:layout-component name="content">
    <div style="display:inline;float:left;">
      <h2>Bulk Checkin using Excel</h2>
      <br/>
      <s:form beanclass="com.hk.web.action.admin.inventory.InventoryBulkCheckinAction">
        <%--<s:messages key="generalMessages"/>--%>
        <h2>File to Upload
            <%--<s:text name="category"/>--%>
          <s:file name="fileBean" size="30"/></h2>

        <div class="buttons">
          <s:submit name="parse" value="Bulk Checkin"/>
        </div>
      </s:form>

    </div>

  </s:layout-component>
</s:layout-render>