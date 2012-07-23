<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.catalog.SupplierManagementAction" var="sma"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Supplier List">
  <s:layout-component name="content">

    <s:form beanclass="com.hk.web.action.admin.catalog.SupplierManagementAction">
      <s:link beanclass="com.hk.web.action.admin.catalog.SupplierManagementAction" class="buttons"
              event="createOrEdit">Add New Supplier</s:link>

      <fieldset>
        <legend>Search Supplier List</legend>

        <label>Tin Number:</label><s:text name="supplierTin" style="width:150px"/>
        &nbsp; &nbsp;
        <label>Name:</label><s:text name="supplierName" style="width:150px"/>

        <s:submit name="pre" value="Search"/>
          <s:submit name="generateExcelReport" value="Download to Excel" />
      </fieldset>
    </s:form>

    <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${sma}"/>
    <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${sma}"/>

    <table class="zebra_vert">
      <thead>
      <tr>
        <th>Name</th>
        <th>TIN</th>
        <th>Address</th>
        <th>Contact Person</th>
        <th>Contact Number</th>
        <th>Actions</th>
      </tr>
      </thead>
      <c:forEach items="${sma.supplierList}" var="supplier">
        <tr>
          <td>${supplier.name}</td>
          <td>${supplier.tinNumber}</td>
          <td>${supplier.line1}<br/>${supplier.line2}<br/>${supplier.city}<br/>${supplier.pincode}<br/>${supplier.state}
          </td>
          <td>${supplier.contactPerson}</td>
          <td>${supplier.contactNumber}</td>
          <td>
            <s:link beanclass="com.hk.web.action.admin.catalog.SupplierManagementAction" event="createOrEdit">Edit
              <s:param name="supplier" value="${supplier.id}"/></s:link>
            &nbsp;
            <s:link beanclass="com.hk.web.action.admin.inventory.CreatePurchaseOrderAction">Create PO
              <s:param name="supplier" value="${supplier.id}"/></s:link>
            &nbsp;
            <s:link beanclass="com.hk.web.action.admin.inventory.DebitNoteAction" event="view">
              Raise Debit Note
              <s:param name="supplier" value="${supplier.id}"/></s:link>
          </td>
        </tr>
      </c:forEach>
    </table>

    <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${sma}"/>
    <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${sma}"/>

  </s:layout-component>
</s:layout-render>