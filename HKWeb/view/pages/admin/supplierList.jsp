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
	            &nbsp; &nbsp;
                <label>Status:</label><s:select name="status">
			        <s:option value="">-ALL-</s:option>
			        <s:option value="true">Active</s:option>
			        <s:option value="false">Inactive</s:option>
	            </s:select>

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
	              <th>Email</th>
                <th>Credit Days</th>
                <th>Target Credit Days</th>
                <th>Lead Time (Days)</th>
                <th>Margin %</th>
                <th>Company/Brands Name</th>
                <th>Validity of Terms of Trade</th>
                <th>Active?</th>
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
	                  <td>${supplier.email_id}</td>
                    <td>${supplier.creditDays}</td>
                    <td>${supplier.targetCreditDays}</td>
                    <td>${supplier.leadTime}</td>
                    <td>${supplier.margins}</td>
                    <td>${supplier.brandName}</td>
                    <td>${supplier.TOT}</td>
	                <td>${supplier.active}</td>
                    <td>
                        <s:link beanclass="com.hk.web.action.admin.catalog.SupplierManagementAction" event="createOrEdit">Edit
                            <s:param name="supplier" value="${supplier.id}"/></s:link>
	                    <c:if test="${supplier.active}">
		                    <br/>
		                    <s:link beanclass="com.hk.web.action.admin.inventory.CreatePurchaseOrderAction">Create PO
			                    <s:param name="supplier" value="${supplier.id}"/></s:link>
		                    <br/>
		                    <%-- <s:link beanclass="com.hk.web.action.admin.inventory.DebitNoteAction" event="view">
			                    Raise Debit Note
			                    <s:param name="supplier" value="${supplier.id}"/></s:link> --%>
			                    <s:link beanclass="com.hk.web.action.admin.catalog.SupplierManagementAction" event="createRV">
			                    Create An RV
			                    <s:param name="supplier" value="${supplier.id}"/>
			                    </s:link>
	                    </c:if>
                    </td>
                </tr>
            </c:forEach>
        </table>

        <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${sma}"/>
        <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${sma}"/>

    </s:layout-component>
</s:layout-render>