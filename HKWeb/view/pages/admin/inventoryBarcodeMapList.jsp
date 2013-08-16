<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.pact.service.core.WarehouseService" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.constants.inventory.EnumInventoryBarcodeMapType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Inventory Barcode Map List">
  <s:useActionBean beanclass="com.hk.web.action.admin.inventory.InventoryBarcodeMapAction" var="poa"/>
  <%
    pageContext.setAttribute("statusList", EnumInventoryBarcodeMapType.getAll());
  %>
  <s:layout-component name="heading">
    Inventory Barcode Map List
  </s:layout-component>

  <s:layout-component name="content">

    <fieldset class="right_label">
      <legend>Search</legend>
      <s:form beanclass="com.hk.web.action.admin.inventory.InventoryBarcodeMapAction">
        <label>Login:</label><s:text name="login"/>
        <label>Status:</label><s:select name="status">
        <s:option value="">-All-</s:option>
        <s:option value="Active">Active</s:option>
        <s:option value="Inactive">Inactive</s:option>
        <s:option value="Deleted">Deleted</s:option>
      </s:select>
        <s:submit name="pre" value="Search Inventory Barcode Map"/>
      </s:form>
    </fieldset>

    <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${poa}"/>
    <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${poa}"/>

    <table class="zebra_vert">
      <thead>
      <tr>
        <th>ID</th>
        <th>Create Date</th>
        <th>PO</th>
        <th>Barcodes</th>
        <th>Login</th>
        <th>Status</th>
        <th>Action</th>
      </tr>
      </thead>
      <c:forEach items="${poa.invBarcodeMapList}" var="barcodeMap" varStatus="ctr">
        <tr>
          <td>${barcodeMap.id}</td>
          <td><fmt:formatDate value="${barcodeMap.createDate}" type="both" timeStyle="short"/></td>
          <td>${barcodeMap.purchaseOrder.id}</td>
          <td>${fn:length(barcodeMap.inventoryBarcodeMapItems)}</td>
          <td>${barcodeMap.user.login}</td>
          <td>${barcodeMap.status}</td>
          <td><s:link beanclass="com.hk.web.action.admin.inventory.InventoryBarcodeMapAction" event="delete"><s:param name="purchaseOrder" value="${barcodeMap.purchaseOrder.id}"/> Delete</s:link> </td>
        </tr>
      </c:forEach>
    </table>
    <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${poa}"/>
    <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${poa}"/>
  </s:layout-component>
</s:layout-render>