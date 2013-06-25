<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.accounts.SupplierTransactionAction" var="supplierTransactionBean"/>


<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Supplier List">
    <s:layout-component name="content">
        <style type="text/css">
            .fieldset-margin{
                margin: 0em;
            }
        </style>
        <script type="text/javascript">
            $(document).ready(function() {
                $('#show-search-filter').click(function(){
                    $('#filter-bar').slideToggle("slow");
                });

            });
        </script>
        <br />
        <s:layout-component name="heading">
            <span style="float: left;">Account Details for  ${supplierTransactionBean.supplier.name}</span>
            <span style="float: right;">Balance:
                <fmt:formatNumber value="${supplierTransactionBean.lastTransaction.currentBalance}" type="currency" currencySymbol=" " maxFractionDigits="2"/>
                <c:choose>
                    <c:when test="${supplierTransactionBean.lastTransaction.currentBalance > 0}">
                        Cr
                    </c:when>
                    <c:otherwise>
                        Db
                    </c:otherwise>
                </c:choose>
                &nbsp;&nbsp;&nbsp;
            </span>
        </s:layout-component>
         <br />
        <s:form beanclass="com.hk.web.action.admin.accounts.SupplierTransactionAction">

            <fieldset class="fieldset-margin">
                <strong><a href="#" id="show-search-filter">+</a></strong>
                <div id="filter-bar">
                <label>Supplier:</label>  &nbsp;&nbsp; <strong>${supplierTransactionBean.supplier.name}</strong>
                    &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;
                <label>Start Date:</label> &nbsp;&nbsp; <s:text class="date_input startDate" style="width:150px"
                                                                formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
                                                                name="startDate"/>
                    &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;
                <label>End Date:</label>  &nbsp;&nbsp;<s:text class="date_input startDate" style="width:150px"
                                                              formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
                                                              name="endDate"/>
                    &nbsp; &nbsp;&nbsp; &nbsp;
                <s:hidden name="supplier" value="${supplierTransactionBean.supplier.id}"/>
                <s:hidden name="defaultView" value="0"/>

                <s:submit name="viewDetails" value="Search"/>
                </div>
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
                    <td><fmt:formatDate value="${supplierTransaction.date}" /></td>
                    <td>${supplierTransaction.supplierTransactionType.name}</td>
                    <td>
                        <c:if test="${supplierTransaction.purchaseInvoice != null}">
                            <s:link beanclass="com.hk.web.action.admin.inventory.PurchaseInvoiceAction" event="view" target="_blank">${supplierTransaction.purchaseInvoice.invoiceNumber}
                                <s:param name="purchaseInvoice" value="${supplierTransaction.purchaseInvoice.id}"/>
                            </s:link>

                        </c:if>
                        <c:if test="${supplierTransaction.debitNote != null}">
                            <s:link beanclass="com.hk.web.action.admin.inventory.DebitNoteAction" event="view" target="_blank">${supplierTransaction.debitNote.debitNoteNumber}
                                <s:param name="debitNote" value="${supplierTransaction.debitNote.id}"/>
                                <s:param name="grn" value="${supplierTransaction.debitNote.goodsReceivedNote.id}"/>
                            </s:link>

                        </c:if>
                        <c:if test="${supplierTransaction.busyPaymentId != null}">
                            ${supplierTransaction.busyPaymentId}
                        </c:if>
                    </td>
                    <td>
                        <c:if test="${supplierTransaction.debitNote != null || supplierTransaction.busyPaymentId != null}">
                            <fmt:formatNumber value="${supplierTransaction.amount}" type="currency" currencySymbol=" " maxFractionDigits="2"/>
                        </c:if>
                    </td>
                    <td>
                        <c:if test="${supplierTransaction.purchaseInvoice != null}">
                            <fmt:formatNumber value="${supplierTransaction.amount}" type="currency" currencySymbol=" " maxFractionDigits="2"/>
                        </c:if>
                    </td>
                    <td>
                        <fmt:formatNumber value="${supplierTransaction.currentBalance}" type="currency" currencySymbol=" " maxFractionDigits="2"/>
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