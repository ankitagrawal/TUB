<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.accounts.SupplierTransactionAction" var="supplierTransactionBean"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Supplier List">
    <s:layout-component name="content">

        <s:form beanclass="com.hk.web.action.admin.accounts.SupplierTransactionAction">

            <fieldset>
                <legend>Search Supplier List</legend>

                <label>Tin Number:</label><s:text name="supplierTin" style="width:150px"/>
                &nbsp; &nbsp;
                <label>Name:</label><s:text name="supplierName" style="width:150px"/>
	            &nbsp; &nbsp;


                <s:submit name="pre" value="Search"/>
            </fieldset>
        </s:form>

        <table class="zebra_vert">
            <thead>
            <tr>
                <th>Name</th>
                <th>TIN</th>
                <th>Address</th>
                <th>Credit Days</th>
                <th>Balance</th>
                <th>Actions</th>
            </tr>
            </thead>
            <c:forEach items="${supplierTransactionBean.supplierTransactionList}" var="supplierTransaction">
                <tr>
                    <td>${supplierTransaction.supplier.name}</td>
                    <td>${supplierTransaction.supplier.tinNumber}</td>
                    <td>${supplierTransaction.supplier.line1}<br/>${supplierTransaction.supplier.line2}<br/>${supplierTransaction.supplier.city}
                        <br/>${supplierTransaction.supplier.pincode}<br/>${supplierTransaction.supplier.state}
                    </td>
                    <td>${supplierTransaction.supplier.creditDays}</td>
                    <td>
                        ${supplierTransaction.currentBalance}
                        <c:choose>
                            <c:when test="${supplierTransaction.currentBalance > 0}">
                                Cr
                            </c:when>
                            <c:otherwise>
                                Db
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <s:link beanclass="com.hk.web.action.admin.accounts.SupplierTransactionAction" event="viewDetails">View Details
                            <s:param name="supplier" value="${supplierTransaction.id}"/>
                            <s:param name="defaultView" value="1" />
                        </s:link>

                    </td>
                </tr>
            </c:forEach>
        </table>

    </s:layout-component>
</s:layout-render>