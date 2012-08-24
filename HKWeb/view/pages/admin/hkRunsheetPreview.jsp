<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Runsheet List">

    <s:useActionBean beanclass="com.hk.web.action.admin.hkDelivery.HKDRunsheetAction" var="runsheetAction"/>
    <s:layout-component name="htmlHead">
    </s:layout-component>
    <s:layout-component name="heading">
        Preview Runsheet
    </s:layout-component>

    <s:layout-component name="content">
        <fieldset class="right_label">
        <legend>Runsheet Preview</legend>
        <s:form beanclass="com.hk.web.action.admin.hkDelivery.HKDRunsheetAction">
            <table>
                <tr>
                    <td><label>Agent Name:</label></td>
                    <td> ${runsheetAction.agent.name}</td>
                    <td></td>
                    <td><label>Total COD Boxes: </label></td>
                    <td>${runsheetAction.runsheet.codBoxCount}</td>
                    <td></td>
                    <td><label>Hub: </label></td>
                    <td> ${runsheetAction.runsheet.hub.name}</td>
                </tr>
                <tr>
                    <td><label>Total Prepaid Boxes:</label></td>
                    <td> ${runsheetAction.runsheet.prepaidBoxCount}</td>
                    <td></td>
                    <td><label>Runsheet Status: </label></td>
                    <td>${runsheetAction.runsheet.runsheetStatus.status}</td>
                    <td></td>
                    <td><label>Create Date: </label></td>
                    <td><fmt:formatDate value="${runsheetAction.runsheet.createDate}" type="both" timeStyle="short"/></td>
                </tr>
            </table>

            </fieldset>
            <h3><strong>Consignments</strong></h3>
            <table class="zebra_vert">
                <thead>
                <tr>
                    <th>AWB Number</th>
                    <th>CNN Number</th>
                    <th>Amount</th>
                    <th>Payment Mode</th>
                    <th>Address</th>
                    <th>City</th>
                    <th>Pincode</th>
                </tr>
                </thead>
                <c:forEach items="${runsheetAction.shippingOrderList}" var="shippingOrder" varStatus="ctr">
                    <tr>
                        <td>${shippingOrder.shipment.awb.awbNumber}</td>
                        <td>${shippingOrder.baseOrder.gatewayOrderId}</td>
                        <td><fmt:formatNumber value="${shippingOrder.amount}" type="currency" currencySymbol=" "
                                              maxFractionDigits="0"/></td>
                        <td>
                            <c:choose>
                            <c:when test="${shippingOrder.baseOrder.payment.paymentMode.name eq 'COD'}">
                                COD
                            </c:when>
                                <c:otherwise>
                                Prepaid
                                </c:otherwise>
                            </c:choose>
                        <td>${shippingOrder.baseOrder.address.line1},${shippingOrder.baseOrder.address.line2},</td>
                        <td>${shippingOrder.baseOrder.address.city}</td>
                        <td>${shippingOrder.baseOrder.address.pin}</td>
                    </tr>
                </c:forEach>
            </table>
        </s:form>
    </s:layout-component>
</s:layout-render>