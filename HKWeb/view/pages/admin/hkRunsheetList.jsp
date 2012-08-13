<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Runsheet List">

  <s:useActionBean beanclass="com.hk.web.action.admin.hkDelivery.HKDRunsheetAction" var="runsheetAction"/>
  <s:layout-component name="htmlHead">
    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
  </s:layout-component>

  <s:layout-component name="heading">
    Runsheet List
  </s:layout-component>

  <s:layout-component name="content">

    <fieldset class="right_label">
      <legend>Search Runsheet</legend>
      <s:form beanclass="com.hk.web.action.admin.hkDelivery.HKDRunsheetAction">
        <label>Runsheet ID:</label><s:text name="runsheet"/>
        <label>Start Date:</label><s:text name="startDate"/>
        <label>End Date:</label><s:text name="endDate"/>
        <label>Agent Name:</label>
          <s:select name="agent" >
                                <s:option value="-Select Agent-">-Select Agent-</s:option>
                                <hk:master-data-collection service="<%=MasterDataDao.class%>"
                                                           serviceProperty="HKDeliveryAgentList" value="id"
                                                           label="name"/>
                            </s:select>
        <label>Status:</label>
          <s:select name="runsheetStatus">
          <s:option value="-Select Status-">-Select Status-</s:option>
                                <hk:master-data-collection service="<%=MasterDataDao.class%>"
                                                           serviceProperty="runsheetStatusList" value="id"
                                                           label="status"/>
                            </s:select>
        <label>Hub:</label>
          <s:select name="hub" class="hubName">
                                <s:option value="-Select Hub-">-Select Hub-</s:option>
                                <hk:master-data-collection service="<%=MasterDataDao.class%>"
                                                           serviceProperty="hubList" value="id"
                                                           label="name"/>
                            </s:select>       
        <s:submit name="pre" value="Search Runsheets"/>
      </s:form>
    </fieldset>

    <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${runsheetAction}"/>
    <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${runsheetAction}"/>

    <table class="zebra_vert">
      <thead>
      <tr>
        <th>ID</th>
        <th>Create Date</th>
        <th>Hub</th>
        <th>Assigned to</th>
        <th>Status</th>
        <th>Expected collection</th>
        <th>Actual collection</th>
        <th>COD Boxes</th>
          <%--<th>Reconciled</th>--%>
        <th>Pre paid boxes</th>
        <th>Remarks</th>
        <th>Actions</th>
      </tr>
      </thead>
      <c:forEach items="${runsheetAction.runsheetList}" var="runsheet" varStatus="ctr">
        <tr>
          <td>${runsheet.id}</td>
          <td><fmt:formatDate value="${runsheet.createDate}" type="both" timeStyle="short"/></td>
          <td>${runsheet.hub.name}</td>
          <td>${runsheet.hkDeliveryAgent.name}</td>
          <td>${runsheet.runsheetStatus.status}</td>
          <td><fmt:formatNumber value="${runsheet.expectedCollection}" type="currency" currencySymbol=" " maxFractionDigits="0"/></td>
          <td><fmt:formatNumber value="${runsheet.actualCollection}" type="currency" currencySymbol=" " maxFractionDigits="0"/></td>
          <td>${runsheet.codBoxCount}</td>
          <td>${runsheet.prePaidBoxCount}</td>
          <td>${runsheet.remarks}</td>
          <td>
            <s:link beanclass="com.hk.web.action.admin.hkDelivery.HKDRunsheetAction" event="edit" target="_blank">Edit runsheet details
              <s:param name="runsheet" value="${runsheet.id}"/></s:link>
          </td>
        </tr>
      </c:forEach>
    </table>

    <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${runsheetAction}"/>
    <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${runsheetAction}"/>

  </s:layout-component>
</s:layout-render>