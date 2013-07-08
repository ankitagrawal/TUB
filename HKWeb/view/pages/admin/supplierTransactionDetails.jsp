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

        <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
        <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>

        <script type="text/javascript">
            $(document).ready(function() {
                $('.show-search-filter').click(function(){
                    $('#filter-bar').slideToggle("slow");
                    if($(this).attr("id") == "expand-search"){
                        $(this).hide();
                        $('#collapse-search').show();
                    }
                    else{
                        $(this).hide();
                        $('#expand-search').show();
                    }
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
                        Dr
                    </c:otherwise>
                </c:choose>
                &nbsp;&nbsp;&nbsp;
            </span>
        </s:layout-component>
         <br />
        <s:form beanclass="com.hk.web.action.admin.accounts.SupplierTransactionAction">

            <fieldset class="fieldset-margin">
                <strong><a href="#" id="expand-search" class="show-search-filter" style="display: none;">+</a></strong>
                <strong><a href="#" id="collapse-search" class="show-search-filter">-</a></strong>

                <div id="filter-bar">
                    <table>
                        <tr>
                            <td><label><strong>Supplier:</strong></label></td>
                            <td>${supplierTransactionBean.supplier.name}</td>
                            <td><label><strong>Start Date:</strong></label></td>
                            <td><s:text class="date_input startDate"
                                                                            style="width:150px"
                                                                            formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
                                                                            name="startDate"/></td>
                            <td><label><strong>End Date:</strong></label> </td>
                            <td><s:text class="date_input startDate"
                                                                         style="width:150px"
                                                                         formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
                                                                         name="endDate"/></td>
                            <s:hidden name="supplier" value="${supplierTransactionBean.supplier.id}"/>
                            <s:hidden name="defaultView" value="0"/>

                            <td><s:submit name="viewDetails" value="Search"/></td>
                        </tr>
                        <tr>
                            <td><label><strong>Supplier tin: </strong></label></td>
                            <td>${supplierTransactionBean.supplier.tinNumber}</td>
                            <td><label><strong>State: </strong></label></td>
                            <td>${supplierTransactionBean.supplier.state}</td>
                            <td><label><strong>Credit days: </strong></label></td>
                            <td>${supplierTransactionBean.supplier.creditDays}</td>
                        </tr>
                    </table>
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
                                Dr
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