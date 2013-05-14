<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.pact.service.core.WarehouseService" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Credit Note List">
  <s:useActionBean beanclass="com.hk.web.action.admin.inventory.CreditNoteAction" var="poa"/>
  <%
    WarehouseService warehouseService = ServiceLocatorFactory.getService(WarehouseService.class);
    pageContext.setAttribute("whList", warehouseService.getAllActiveWarehouses());
  %>
  <s:layout-component name="heading">
    Credit Note List
  </s:layout-component>

  <s:layout-component name="content">

    <fieldset class="right_label">
      <legend>Search Credit Note</legend>
      <s:form beanclass="com.hk.web.action.admin.inventory.CreditNoteAction">
        <label>Customer Login:</label><s:text name="customerLogin"/>
        <label>Status:</label><s:select name="creditNoteStatus">
        <s:option value="">-All-</s:option>
        <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="creditNoteStatusList" value="id"
                                   label="name"/>
        </s:select>
        <label>Warehouse: </label>
        <s:select name="warehouse">
          <s:option value="0">-All-</s:option>
          <c:forEach items="${whList}" var="wh">
            <s:option value="${wh.id}">${wh.identifier}</s:option>
          </c:forEach>
        </s:select>
        <s:submit name="pre" value="Search Credit Note"/>
      </s:form>
    </fieldset>

    <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${poa}"/>
    <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${poa}"/>

    <table class="zebra_vert">
      <thead>
      <tr>
        <th>ID</th>
        <th>Create Date</th>
        <th>Customer</th>
        <th>Login</th>
        <th>TIN#</th>
        <th>State</th>
        <th>Warehouse</th>
        <th>Created By</th>
        <th>Status</th>
        <th>Reconciled</th>
        <th>Actions</th>
      </tr>
      </thead>
      <c:forEach items="${poa.creditNoteList}" var="creditNote" varStatus="ctr">
        <tr>
          <td>${creditNote.id}</td>
          <td><fmt:formatDate value="${creditNote.createDt}" type="both" timeStyle="short"/></td>
          <td>${creditNote.user.name}</td>
          <td>${creditNote.user.login}</td>
          <c:set var="b2bUserDetails" value="${hk:getB2bUserDetails(creditNote.user)}"/>
          <td>${b2bUserDetails.tin}</td>
          <td>${hk:getStateFromTin(b2bUserDetails.tin)}</td>
          <td>${creditNote.warehouse.identifier}</td>
          <td>${creditNote.createdBy.login}</td>
          <td>${creditNote.creditNoteStatus.name}</td>
          <td>
            <c:choose>
              <c:when test="${creditNote.userCredited}">
                Yes &#10004;
              </c:when>
              <c:otherwise>
                No
              </c:otherwise>
            </c:choose>
          </td>
          <td>
            <s:link beanclass="com.hk.web.action.admin.inventory.CreditNoteAction" event="view">Edit/View
              <s:param name="creditNote" value="${creditNote.id}"/>
            </s:link>
          </td>
        </tr>
      </c:forEach>
    </table>
    <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${poa}"/>
    <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${poa}"/>
  </s:layout-component>
</s:layout-render>