<%@ page import="com.hk.constants.inventory.EnumPurchaseOrderStatus" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.pact.dao.warehouse.WarehouseDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<c:set var="approved" value="<%=EnumPurchaseOrderStatus.Approved.getId()%>"/>
<s:useActionBean beanclass="com.hk.web.action.admin.warehouse.SelectWHAction" var="whAction" event="getUserWarehouse"/>
 <%
    WarehouseDao warehouseDao = ServiceLocatorFactory.getService(WarehouseDao.class);
    pageContext.setAttribute("whList", warehouseDao.getAllWarehouses());
  %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Purchase Order List">
  <s:useActionBean beanclass="com.hk.web.action.admin.inventory.POAction" var="poa"/>
  <s:layout-component name="htmlHead">
    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
  </s:layout-component>

  <s:layout-component name="heading">
    Purchase Order List
  </s:layout-component>

  <s:layout-component name="content">

    <fieldset class="right_label">
      <legend>Search PO</legend>
      <s:form beanclass="com.hk.web.action.admin.inventory.POAction">
        <label>PO ID:</label><s:text name="purchaseOrder"/>
        <label>VariantID:</label><s:text name="productVariant"/>
        <label>Tin Number:</label><s:text name="tinNumber"/>
        <label>Supplier Name:</label><s:text name="supplierName"/>
        <label>Status:</label><s:select name="purchaseOrderStatus">
        <s:option value="">-All-</s:option>
          <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="purchaseOrderStatusList" value="id" label="name"/>
        </s:select>
        <label>Approver:</label><s:select name="approvedBy">
          <s:option value="">-All-</s:option>
          <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="approverList" value="id" label="name"/>
        </s:select>
        <label>CreatedBy:</label><s:select name="createdBy">
          <s:option value="">-All-</s:option>
          <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="creatorList" value="id" label="name"/>
        </s:select>
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
          </c:choose>   &nbsp; &nbsp;
        <s:submit name="pre" value="Search PO"/>
          <s:submit name="generateExcelReport" value="Download to Excel" />
      </s:form>
    </fieldset>

    <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${poa}"/>
    <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${poa}"/>

    <table class="zebra_vert">
      <thead>
      <tr>
        <th>ID</th>
        <th>Create Date</th>
        <th>Created By</th>
        <th>No. of Sku</th>
        <th>Approver</th>
        <th>Supplier</th>
        <th>Supplier TIN</th>
        <th>Warehouse</th>
        <th>Status</th>
        <th>Last Update Date</th>
          <%--<th>Reconciled</th>--%>
        <th>Payable</th>
        <th>Payment Details</th>
        <th>GRNs</th>
        <th>Actions</th>
      </tr>
      </thead>
      <c:forEach items="${poa.purchaseOrderList}" var="purchaseOrder" varStatus="ctr">
        <tr>
          <td>${purchaseOrder.id}</td>
          <td><fmt:formatDate value="${purchaseOrder.createDate}" type="both" timeStyle="short"/></td>
          <td>${purchaseOrder.createdBy.name}</td>
            <td>
            <c:forEach items="${purchaseOrder.poLineItems}" var="purchaseOrderLineItems" varStatus="counter">
                <c:if test="${counter.last}" >${counter.count}</c:if>
            </c:forEach>
            </td>
          <td>${purchaseOrder.approvedBy.name}</td>
          <td>${purchaseOrder.supplier.name}</td>
          <td>${purchaseOrder.supplier.tinNumber}</td>
          <td>${purchaseOrder.warehouse.city}</td>
          <td>${purchaseOrder.purchaseOrderStatus.name}</td>
          <td><fmt:formatDate value="${purchaseOrder.updateDate}" type="both" timeStyle="short"/></td>
          <td>
            <fmt:formatNumber value="${purchaseOrder.payable}" type="currency" currencySymbol=" " maxFractionDigits="0"/></td>
          <td>
            ${purchaseOrder.paymentDetails}
            <br />
            <s:link beanclass="com.hk.web.action.admin.payment.PaymentHistoryAction" target="_blank">Payment History
                <s:param name="purchaseOrderId" value="${purchaseOrder.id}"/></s:link>

          </td>
          <td>
            <c:forEach var="grn" items="${purchaseOrder.goodsReceivedNotes}">
              <s:link beanclass="com.hk.web.action.admin.inventory.GRNAction" event="view" target="_blank">
                <s:param name="grn" value="${grn.id}"/>
                ${grn.id}
              </s:link>
              &nbsp;
            </c:forEach>
          </td>
          <td>
            <s:link beanclass="com.hk.web.action.admin.inventory.EditPurchaseOrderAction">Edit
              <s:param name="purchaseOrder" value="${purchaseOrder.id}"/></s:link>
            &nbsp;
            <s:link beanclass="com.hk.web.action.admin.inventory.POAction" event="print" target="_blank">Print
              <s:param name="purchaseOrder" value="${purchaseOrder.id}"/></s:link>
            &nbsp;
            <s:link beanclass="com.hk.web.action.admin.inventory.POAction" event="poInExcel" target="_blank">Excel
              <s:param name="purchaseOrder" value="${purchaseOrder.id}"/></s:link>
              <s:link beanclass="com.hk.web.action.admin.inventory.POAction" event="poInPdf" target="_blank">PDF
                            <s:param name="purchaseOrder" value="${purchaseOrder.id}"/></s:link>
            <c:if test="${purchaseOrder.purchaseOrderStatus.id >= approved}">
                <br/>
                <s:link beanclass="com.hk.web.action.admin.inventory.POAction" event="generateGRNCheck">Create GRN
                  <s:param name="purchaseOrder" value="${purchaseOrder.id}"/></s:link>
            </c:if>
          </td>
        </tr>
      </c:forEach>
    </table>

    <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${poa}"/>
    <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${poa}"/>

  </s:layout-component>
</s:layout-render>