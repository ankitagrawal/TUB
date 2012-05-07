<%@ page import="mhc.service.dao.WarehouseDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.inventory.ReconciliationVoucherAction" var="poa"/>
 <s:useActionBean beanclass="com.hk.web.action.admin.warehouse.SelectWHAction" var="whAction" event="getUserWarehouse"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Reconciliation Voucher List">
   <%
       WarehouseDaoImpl warehouseDao = InjectorFactory.getInjector().getInstance(WarehouseDaoImpl.class);
       pageContext.setAttribute("whList", warehouseDao.listAll());
   %>
  <s:layout-component name="htmlHead">
    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
  </s:layout-component>

  <s:layout-component name="content">
    <c:if test="${whAction.setWarehouse != null}">
      <s:link beanclass="com.hk.web.action.admin.inventory.ReconciliationVoucherAction"
              event="view">Create New Reconciliation Voucher</s:link>
    </c:if>
    <s:form beanclass="com.hk.web.action.admin.inventory.ReconciliationVoucherAction">

      <fieldset>
        <legend>Search Reconciliation Voucher List</legend>

        <s:errors/>
        <br/>
        <label>Create Date:</label><s:text class="date_input" formatPattern="yyyy-MM-dd" name="createDate"/>
        <label>User Login</label><s:text style="width:150px" name="userLogin"/>
          <label>Warehouse: </label>
          <c:choose>
            <c:when test="${whAction.setWarehouse != null}">
              <s:hidden name="warehouse" value="${whAction.setWarehouse}"/>
              ${whAction.setWarehouse.city}
            </c:when>
            <c:otherwise>
              <s:select name="warehouse">
                <s:option value="0">-All-</s:option>
                <c:forEach items="${whList}" var="wh">
                  <s:option value="${wh.id}">${wh.city}</s:option>
                </c:forEach>
              </s:select>
            </c:otherwise>
          </c:choose>
        <s:submit name="pre" value="Search"/>
      </fieldset>
    </s:form>

    <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${poa}"/>
    <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${poa}"/>

    <table class="zebra_vert">
      <thead>
      <tr>
        <th>ID</th>
        <th>Create Date</th>
        <th>Created By</th>
        <th>Reconciliation Date</th>
        <th>Actions</th>
      </tr>
      </thead>
      <c:forEach items="${poa.reconciliationVouchers}" var="purchaseOrder" varStatus="ctr">
        <tr>
          <td>${purchaseOrder.id}</td>
          <td><fmt:formatDate value="${purchaseOrder.createDate}" type="both" timeStyle="short"/></td>
          <td>${purchaseOrder.createdBy.name} <br/>(${purchaseOrder.createdBy.login})</td>
          <td><fmt:formatDate value="${purchaseOrder.reconciliationDate}" type="both" timeStyle="short"/></td>
          <td>
            <s:link beanclass="com.hk.web.action.admin.inventory.ReconciliationVoucherAction" event="view">Edit
              <s:param name="reconciliationVoucher" value="${purchaseOrder.id}"/></s:link>
          </td>
        </tr>
      </c:forEach>
    </table>
    <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${poa}"/>
    <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${poa}"/>

  </s:layout-component>
</s:layout-render>