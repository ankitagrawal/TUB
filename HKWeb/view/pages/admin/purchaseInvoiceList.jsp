<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.constants.inventory.EnumPurchaseOrderStatus" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Purchase Invoice List">
  <s:useActionBean beanclass="com.hk.web.action.admin.inventory.PurchaseInvoiceAction" var="pia"/>
  <s:layout-component name="htmlHead">
    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
  </s:layout-component>

  <s:layout-component name="heading">
    Purchase Invoice List
  </s:layout-component>

  <s:layout-component name="content">

    <fieldset class="right_label">
      <legend>Search Purchase Invoice</legend>
      <s:form beanclass="com.hk.web.action.admin.inventory.PurchaseInvoiceAction">
        <label>Purchase Invoice ID:</label><s:text name="purchaseInvoice"/>
        <label>VariantID:</label><s:text name="productVariant"/>
        <label>Tin Number:</label><s:text name="tinNumber"/>
        <label>Supplier Name:</label><s:text name="supplierName"/>
        <label>Status:</label><s:select name="purchaseInvoiceStatus">
        <s:option value="">-All-</s:option>
          <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="purchaseInvoiceStatusList" value="id" label="name"/>
        </s:select>
        <label>Approver:</label><s:select name="approvedBy">
          <s:option value="">-All-</s:option>
          <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="approverList" value="id" label="name"/>
        </s:select>
        <label>CreatedBy:</label><s:select name="createdBy">
          <s:option value="">-All-</s:option>
          <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="creatorList" value="id" label="name"/>
        </s:select>
        <s:submit name="pre" value="Search Purchase Invoice"/>
      </s:form>
    </fieldset>

    <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${pia}"/>
    <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${pia}"/>

    <table class="zebra_vert">
      <thead>
      <tr>
        <th>ID</th>
        <th>Create Date</th>
        <th>Created By</th>
        <th>Supplier</th>
        <th>Supplier TIN</th>
        <th>Status</th>
        <th>Last Update Date</th>
          <%--<th>Reconciled</th>--%>
        <th>Payable</th>
        <th>Payment Details</th>
        <th>GRNs</th>
	      <th>Invoice No.</th>
        <th>Actions</th>
      </tr>
      </thead>
      <c:forEach items="${pia.purchaseInvoiceList}" var="purchaseInvoice" varStatus="ctr">
        <tr>
          <td>${purchaseInvoice.id}</td>
          <td><fmt:formatDate value="${purchaseInvoice.createDate}" type="both" timeStyle="short"/></td>
          <td>${purchaseInvoice.createdBy.name}</td>
          <td>${purchaseInvoice.supplier.name}</td>
          <td>${purchaseInvoice.supplier.tinNumber}</td>
          <td>${purchaseInvoice.purchaseInvoiceStatus.name}</td>
          <td><fmt:formatDate value="${purchaseInvoice.createDate}" type="both" timeStyle="short"/></td>
          <td>
            <fmt:formatNumber value="${purchaseInvoice.finalPayableAmount}" type="currency" currencySymbol=" " maxFractionDigits="0"/></td>
          <td>${purchaseInvoice.paymentDetails}</td>
          <td>
            <c:forEach var="grn" items="${purchaseInvoice.goodsReceivedNotes}">
              <s:link beanclass="com.hk.web.action.admin.inventory.GRNAction" event="view" target="_blank">
                <s:param name="grn" value="${grn.id}"/>
                ${grn.id}
              </s:link>
              &nbsp;
            </c:forEach>
          </td>
	        <td>
            <c:forEach var="grn" items="${purchaseInvoice.goodsReceivedNotes}">
              ${grn.invoiceNumber}&nbsp;
            </c:forEach>
          </td>
          <td>
            <s:link beanclass="com.hk.web.action.admin.inventory.PurchaseInvoiceAction" event="view" target="_blank">Edit/View
              <s:param name="purchaseInvoice" value="${purchaseInvoice.id}"/></s:link>
          </td>
        </tr>
      </c:forEach>
    </table>

    <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${pia}"/>
    <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${pia}"/>

  </s:layout-component>
</s:layout-render>