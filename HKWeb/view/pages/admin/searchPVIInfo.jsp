<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.inventory.SearchPVIInfoAction" var="spviActionBean"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Search PVI Info">
    <s:layout-component name="heading">Search PVI Info - ${spviActionBean.barcode}</s:layout-component>
    <s:layout-component name="content">
        <fieldset>
            <legend>Search</legend>
            <s:form beanclass="com.hk.web.action.admin.inventory.SearchPVIInfoAction">
                <strong>Barcode:</strong><s:text name="barcode"/>
                <s:submit name="search" value="Search"/>
            </s:form>
        </fieldset>

        <table class="zebra_vert">
            <thead>
            <tr>
                <th>Date</th>
                <th>PVID</th>
                <th>WH</th>
                <th>GRN</th>
                <th>RV</th>
                <th>ST</th>
                <th>SO</th>
                <th>LI</th>
                <th>Txn Type</th>
                <th>Qty</th>
            </tr>
            </thead>

            <c:forEach items="${spviActionBean.pviList}" var="pvi">
                <tr>
                    <td>${pvi.txnDate}</td>
                    <td>${pvi.sku.productVariant.id}</td>
                    <td>${pvi.sku.warehouse.identifier}</td>
                    <td>${pvi.grnLineItem.goodsReceivedNote.id}</td>
                    <td>${pvi.rvLineItem.reconciliationVoucher.id}</td>
                    <td>${pvi.stockTransferLineItem.stockTransfer.id}</td>
                    <td>${pvi.shippingOrder.id}</td>
                    <td>${pvi.lineItem.id}</td>
                    <td>${pvi.invTxnType.name}</td>
                    <td>${pvi.qty}</td>
                </tr>
            </c:forEach>
        </table>
    </s:layout-component>
</s:layout-render>