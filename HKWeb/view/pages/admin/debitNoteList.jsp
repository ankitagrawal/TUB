<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.pact.service.core.WarehouseService" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Debit Note List">
  <s:useActionBean beanclass="com.hk.web.action.admin.inventory.DebitNoteAction" var="poa"/>
  <%
    WarehouseService warehouseService = ServiceLocatorFactory.getService(WarehouseService.class);
    pageContext.setAttribute("whList", warehouseService.getAllActiveWarehouses());
  %>
  <s:layout-component name="heading">
    Debit Note List
  </s:layout-component>

  <s:layout-component name="content">

    <fieldset class="right_label">
      <legend>Search Debit Note</legend>
      <s:form beanclass="com.hk.web.action.admin.inventory.DebitNoteAction">
        <%--<label>GRN ID:</label><s:text name="grn"/>--%>
        <label>Tin Number:</label><s:text name="tinNumber"/>
        <label>Supplier Name:</label><s:text name="supplierName"/>
        <label>Status:</label><s:select name="debitNoteStatus">
        <s:option value="">-All-</s:option>
        <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="debitNoteStatusList" value="id"
                                   label="name"/>
        </s:select>
        <label>Warehouse: </label>
        <s:select name="warehouse">
          <s:option value="0">-All-</s:option>
          <c:forEach items="${whList}" var="wh">
            <s:option value="${wh.id}">${wh.identifier}</s:option>
          </c:forEach>
        </s:select>
        <s:submit name="pre" value="Search Debit Note"/>
      </s:form>
    </fieldset>

    <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${poa}"/>
    <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${poa}"/>

    <table class="zebra_vert">
      <thead>
      <tr>
        <th>DN ID</th>
          <%--<th>GRN ID</th>--%>
        <th>Create Date</th>
        <th>Supplier</th>
        <th>Supplier TIN</th>
        <th>Warehouse</th>
        <th>Status</th>
        <th>Reconciled</th>
        <th>Actions</th>
      </tr>
      </thead>
      <c:forEach items="${poa.debitNoteList}" var="debitNote" varStatus="ctr">
        <tr>
          <td>${debitNote.id}</td>
            <%--<td>
              <s:link beanclass="com.hk.web.action.admin.inventory.GRNAction" event="view">
                <s:param name="grn" value="${debitNote.goodsReceivedNote.id}"/>
                ${debitNote.goodsReceivedNote.id}
              </s:link>
            </td>--%>
          <td><fmt:formatDate value="${debitNote.createDate}" type="both" timeStyle="short"/></td>
          <td>${debitNote.supplier.name}</td>
          <td>${debitNote.supplier.tinNumber}</td>
          <td>${debitNote.warehouse.identifier}</td>
          <td>${debitNote.debitNoteStatus.name}</td>
          <td>
            <c:choose>
              <c:when test="${debitNote.isDebitToSupplier}">
                Yes &#10004;
              </c:when>
              <c:otherwise>
                No
              </c:otherwise>
            </c:choose>
          </td>
          <td>
            <s:link beanclass="com.hk.web.action.admin.inventory.DebitNoteAction" event="view">Edit
              <s:param name="debitNote" value="${debitNote.id}"/>
              <s:param name="grn" value="${debitNote.goodsReceivedNote.id}"/></s:link>
            &nbsp;
            <s:link beanclass="com.hk.web.action.admin.inventory.DebitNoteAction" event="print">Print
              <s:param name="debitNote" value="${debitNote.id}"/>
              <s:param name="grn" value="${debitNote.goodsReceivedNote.id}"/></s:link>
          </td>
        </tr>
      </c:forEach>
    </table>
    <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${poa}"/>
    <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${poa}"/>
  </s:layout-component>
</s:layout-render>