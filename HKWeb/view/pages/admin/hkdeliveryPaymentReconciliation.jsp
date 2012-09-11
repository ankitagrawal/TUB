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
        <s:form beanclass="com.hk.web.action.admin.hkDelivery.HKDConsignmentAction">
            <s:hidden name="hkdeliveryPaymentReconciliation" value="${consignmentAction.hkdeliveryPaymentReconciliation.id}" />
            <table>
                <tr>
                    <td><label><strong>Date: </strong></label></td>
                    <td>
                        <s:hidden name="hkdeliveryPaymentReconciliation.createDate" />
                        <fmt:formatDate value="${consignmentAction.hkdeliveryPaymentReconciliation.createDate}" type="both" timeStyle="short"/>
                    </td>
                    <td><label><strong>Expected COD Amount:</strong></label></td>
                    <td>
                        <s:hidden name="hkdeliveryPaymentReconciliation.expectedAmount" />
                        <fmt:formatNumber value="${consignmentAction.hkdeliveryPaymentReconciliation.expectedAmount}" type="currency" currencySymbol=" "
                                          maxFractionDigits="2"/>
                    </td>
                    <td><label><strong>Actual Amount:</strong></label></td>
                    <td><s:text name="hkdeliveryPaymentReconciliation.actualAmount"/></td>
                </tr>
                <tr>
                    <td><label><strong>User: </strong></label></td>
                    <td>
                        <s:hidden name="hkdeliveryPaymentReconciliation.user" value="${consignmentAction.hkdeliveryPaymentReconciliation.user.id}" />
                        ${consignmentAction.hkdeliveryPaymentReconciliation.user.name}</td>
                    <td><label><strong>Remarks: </strong></label></td>
                    <td><s:textarea name="hkdeliveryPaymentReconciliation.remarks" style="height:50px;"/></td>
                </tr>
            </table>
             <h3><strong>Consignments</strong></h3>
            <table class="zebra_vert">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>AWB Number</th>
                    <th>CNN Number</th>
                    <th>Amount</th>
                    <th>Payment Mode</th>
                    <th>Status</th>

                </tr>
                </thead>
                <c:forEach items="${consignmentAction.consignmentListForPaymentReconciliation}" var="consignment" varStatus="ctr">
                    <tr class="consignment-row">
                        <td><s:hidden name="consignmentListForPaymentReconciliation[${ctr.index}]" value="${consignment.id}"/>${consignment.id}</td>
                        <td>${consignment.awbNumber}</td>
                        <td>${consignment.cnnNumber}</td>
                        <td><fmt:formatNumber value="${consignment.amount}" type="currency" currencySymbol=" "
                                              maxFractionDigits="2"/></td>
                        <td>${consignment.paymentMode}</td>
                        <td>${consignment.consignmentStatus.status}</td>
                    </tr>
                </c:forEach>
            </table>
            <s:submit name="savePaymentReconciliation" value="Save payment Reconciliation" />
            <s:link beanclass="com.hk.web.action.admin.hkDelivery.HKDPaymentReconciliationAction" event="downloadPaymentReconciliation"
                    title="Download Payment Reconciliation">Download Payment Reconciliation
            <s:param name="hkdeliveryPaymentReconciliation" value="${consignmentAction.hkdeliveryPaymentReconciliation.id}" />
            </s:link>
        </s:form>
    </s:layout-component>
</s:layout-render>