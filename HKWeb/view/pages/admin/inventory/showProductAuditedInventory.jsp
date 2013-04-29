<%@ page import="com.hk.domain.inventory.rv.ReconciliationType" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="java.util.List" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page import="com.hk.constants.inventory.EnumReconciliationType" %>
<%@ page import="com.hk.constants.sku.EnumSkuItemTransferMode" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.inventory.ReconciliationVoucherAction" var="pa"/>
<s:useActionBean beanclass="com.hk.web.action.admin.warehouse.SelectWHAction" var="whAction" event="getUserWarehouse"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Edit Reconciliation Voucher">
    <jsp:useBean id="now" class="java.util.Date" scope="request"/>

    <s:layout-component name="content">
        <h2>Product Variant Audit</h2>

        <h2>RV No # ${pa.reconciliationVoucher.id}</h2>
        <table>
            <tr>
                <td>Reconciliation Date</td>
                <td>
                        ${pa.reconciliationVoucher.reconciliationDate}
                </td>
            </tr>
            <tr>
                <td>Remarks<br/><span class="sml gry">(eg. XXX)</span></td>
                <td>${pa.reconciliationVoucher.remarks}</td>
            </tr>
            <tr>
                <td>For Warehouse</td>
                <td>
                        ${pa.reconciliationVoucher.warehouse.identifier}
                </td>
            </tr>
        </table>


        <table border="1">
            <thead>
            <tr>
                <th>VariantID</th>
                <th>Details</th>
                <th>Qty<br/>Only(+)</th>
                <th>Reconciliation Type</th>
                <th>Reconcilied Qty</th>

            </tr>
            </thead>
            <tbody id="poTable">
            <c:forEach var="rvLineItem" items="${pa.reconciliationVoucher.rvLineItems}" varStatus="ctr">
            <c:set var="productVariant" value="${rvLineItem.sku.productVariant}"/>
            <tr style="background-color:#ccff99;">
                <td>
                        ${productVariant.id}
                </td>
                <td>${productVariant.product.name}<br/>${productVariant.productOptionsWithoutColor}
                </td>
                <td>${rvLineItem.qty}
                </td>
                <td>${rvLineItem.reconciliationType.name}
                </td>
                <td>${rvLineItem.reconciledQty}</td>
                </c:forEach>
            </tbody>
        </table>

    </s:layout-component>

</s:layout-render>