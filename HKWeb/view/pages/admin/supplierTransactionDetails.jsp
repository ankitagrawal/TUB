<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.accounts.SupplierTransactionAction" var="supplierTransactionBean"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Supplier List">
    <s:layout-component name="content">

        <s:form beanclass="com.hk.web.action.admin.accounts.SupplierTransactionAction">

            <fieldset>
                <legend>Filter Search</legend>
                <label>Supplier: <strong>${supplierTransactionBean.supplier.name}</strong></label>
                <label>Start Date:</label><s:text name="startDate" style="width:150px"/>
                &nbsp; &nbsp;
                <label>End Date:</label><s:text name="endDate" style="width:150px"/>
	            &nbsp; &nbsp;
                <s:hidden name="supplier" value="${supplierTransactionBean.supplier.id}"/>
                <s:hidden name="defaultView" value="0"/>

                <s:submit name="viewDetails" value="Search"/>
            </fieldset>
        </s:form>

        <table class="zebra_vert">
            <thead>
            <tr>
                <th>Date</th>
                <th>Type</th>
                <th>Vch/Bill No</th>
                <th>Debit (Rs)</th>
                <th>Credit (Rs)</th>
                <th>Balance (Rs)</th>
                <th>Short Narration</th>
            </tr>
            </thead>
            <c:forEach items="${supplierTransactionBean.supplierTransactionList}" var="supplierTransaction">
                <tr>
                    <td>${supplierTransaction.date}</td>
                    <td>${supplierTransaction.supplierTransactionType.name}</td>
                    <td>
                        <c:if test="${supplierTransaction.purchaseInvoice != null}">
                            ${supplierTransaction.purchaseInvoice.invoiceNumber}
                        </c:if>
                        <c:if test="${supplierTransaction.debitNote != null}">
                            ${supplierTransaction.debitNote.debitNoteNumber}
                        </c:if>
                        <c:if test="${supplierTransaction.busyPaymentId != null}">
                            ${supplierTransaction.busyPaymentId}
                        </c:if>
                    </td>
                    <td>
                        <c:if test="${supplierTransaction.debitNote != null || supplierTransaction.busyPaymentId != null}">
                            ${supplierTransaction.amount}
                        </c:if>
                    </td>
                    <td>
                        <c:if test="${supplierTransaction.purchaseInvoice != null}">
                            ${supplierTransaction.amount}
                        </c:if>
                    </td>
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
                        ${supplierTransaction.narration}

                    </td>
                </tr>
            </c:forEach>
        </table>

    </s:layout-component>
</s:layout-render>