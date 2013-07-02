<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.pact.service.core.WarehouseService" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Fixed SO List">
  <s:useActionBean beanclass="com.hk.web.action.admin.shippingOrder.FixedShippingOrderAction" var="poa"/>
  <%
    WarehouseService warehouseService = ServiceLocatorFactory.getService(WarehouseService.class);
    pageContext.setAttribute("whList", warehouseService.getAllActiveWarehouses());
  %>
  <s:layout-component name="heading">
    Fixed SO List
  </s:layout-component>

  <s:layout-component name="content">

    <fieldset class="right_label">
      <legend>Search Fixed SO</legend>
      <s:form beanclass="com.hk.web.action.admin.shippingOrder.FixedShippingOrderAction">
        <label>Status:</label><s:select name="status">
        <s:option value="">-All-</s:option>
        <s:option value="OPEN">OPEN</s:option>
        <s:option value="CLOSED">CLOSED</s:option>
      </s:select>
        <label>Warehouse: </label>
        <s:select name="warehouse">
          <s:option value="0">-All-</s:option>
          <c:forEach items="${whList}" var="wh">
            <s:option value="${wh.id}">${wh.identifier}</s:option>
          </c:forEach>
        </s:select>
        <s:submit name="search" value="Search"/>
      </s:form>
    </fieldset>

    <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${poa}"/>
    <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${poa}"/>

    <table class="zebra_vert">
      <thead>
      <tr>
        <th>SO#</th>
        <th>Create Date</th>
        <th>Warehouse</th>
        <th>Created By</th>
        <th>Status</th>
        <th>Remarks</th>
        <%--<th>Closed By</th>--%>
      </tr>
      </thead>
      <c:forEach items="${poa.fixedShippingOrderList}" var="fixedSO" varStatus="ctr">
        <tr>
          <td>${fixedSO.shippingOrder.id}</td>
          <td><fmt:formatDate value="${fixedSO.createDate}" type="both" timeStyle="short"/></td>
          <td>${fixedSO.shippingOrder.warehouse.identifier}</td>
          <td>${fixedSO.createdBy.login}</td>
          <td>${fixedSO.status}</td>
          <td>${fixedSO.remarks}</td>
          <%--<td>${fixedSO.closedBy.login}</td>--%>
        </tr>
      </c:forEach>
    </table>
    <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${poa}"/>
    <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${poa}"/>
  </s:layout-component>
</s:layout-render>