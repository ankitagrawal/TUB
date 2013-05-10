<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Consignment Tracking List">

  <s:useActionBean beanclass="com.hk.web.action.admin.hkDelivery.HKDConsignmentTrackingAction"
                   var="consignmentTrackingAction"/>
  <s:layout-component name="htmlHead">
    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
  </s:layout-component>

  <s:layout-component name="heading">
    Consignment Tracking List
  </s:layout-component>

  <s:layout-component name="content">

    <fieldset class="right_label">
      <legend>Search Consignment Tracking</legend>
      <s:form beanclass="com.hk.web.action.admin.hkDelivery.HKDConsignmentTrackingAction">
        <label style="margin-left: 10px;margin-right:10px;">Start Date:</label>
        <s:text class="date_input startDate" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
                name="startDate"/>

        <label style="margin-left: 10px;margin-right:10px;">End Date:</label>
        <s:text class="date_input endDate" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
                name="endDate"/>

        <label>Status:</label>
        <s:select name="consignmentLifecycleStatus">
          <s:option value="">-Select Status-</s:option>
          <hk:master-data-collection service="<%=MasterDataDao.class%>"
                                     serviceProperty="consignmentLifecycleStatusList" value="id"
                                     label="status"/>
        </s:select>
        &nbsp;&nbsp;
        <label>Hub:</label>
        <shiro:hasPermission name="<%=PermissionConstants.SELECT_HUB%>">
          <s:select name="hubId">
            <s:option value="">-Select Hub-</s:option>
            <hk:master-data-collection service="<%=MasterDataDao.class%>"
                                       serviceProperty="hubList" value="id"
                                       label="name"/>
          </s:select>
        </shiro:hasPermission>
        <shiro:hasPermission name="<%=PermissionConstants.VIEW_HUB%>">
          <shiro:lacksPermission name="<%=PermissionConstants.SELECT_HUB%>">
            <c:set var="hub" value="${hk:getHubForHkdeliveryUser(consignmentTrackingAction.loggedOnUser)}"/>
            <s:hidden name="hubId" value="${hub.id}"/><strong>${hub.name}</strong>&nbsp;&nbsp;
          </shiro:lacksPermission>
        </shiro:hasPermission>
        <s:submit name="searchConsignmentTracking" value="Search"/>
      </s:form>
    </fieldset>

    <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${consignmentTrackingAction}"/>
    <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${consignmentTrackingAction}"/>

    <table class="zebra_vert">
      <thead>
      <tr>
        <th>ID</th>
        <th>Create Date</th>
        <th>AWB Number</th>
        <th>Hub</th>
        <th>Runsheet Agent</th>
        <th>Runsheet Id</th>
        <th>Lifecycle Status</th>
        <th>NDR Action</th>
        <th>Action</th>
      </tr>
      </thead>
      <c:forEach items="${consignmentTrackingAction.consignmentList}" var="consignmentTracking">
        <tr>
          <td>${consignmentTracking.id}</td>
          <td><fmt:formatDate value="${consignmentTracking.createDate}" type="both" timeStyle="short"/></td>
          <td>${consignmentTracking.consignment.awbNumber}</td>
          <td>${consignmentTracking.consignment.hub.name}</td>
          <td>${consignmentTracking.runsheet.agent.name}</td>
          <td>${consignmentTracking.consignment.runsheet.id}</td>
          <td>${consignmentTracking.consignmentLifecycleStatus.status}</td>
          <td>${consignmentTracking.ndrResolution}</td>
          <td>
            <s:link beanclass="com.hk.web.action.admin.hkDelivery.HKDConsignmentAction" event="trackConsignment" target="_blank">
              <s:param name="consignmentNumber" value="${consignmentTracking.consignment.awbNumber}" />
              <s:param name="doTracking" value="true" />Track Consignment
            </s:link>
          </td>
        </tr>
      </c:forEach>
    </table>
  </s:layout-component>
</s:layout-render>
