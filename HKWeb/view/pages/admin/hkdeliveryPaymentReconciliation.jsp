<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Runsheet List">

    <s:useActionBean beanclass="com.hk.web.action.admin.hkDelivery.HKDConsignmentAction" var="consignmentAction"/>
    <s:layout-component name="htmlHead">
    </s:layout-component>

    <s:layout-component name="heading">
        Reconcile HKDelivery payments
    </s:layout-component>
    <s:layout-component name="content">
        <table>
            <tr>
                <td><label><strong>Reconciliation Id:</strong></label></td>
                <td>${consignmentAction.hkdeliveryPaymentReconciliation.id}</td>
                <td><label><strong>COD Amount:</strong></label></td>
                <td>${consignmentAction.hkdeliveryPaymentReconciliation.amount}</td>
            </tr>
        </table>
    </s:layout-component>
</s:layout-render>