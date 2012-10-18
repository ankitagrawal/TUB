<%--
  Created by IntelliJ IDEA.
  User: Marut
  Date: 10/17/12
  Time: 6:58 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.constants.order.EnumCartLineItemType" %>
<%@ page import="com.hk.constants.order.EnumOrderStatus" %>
<%@ page import="com.hk.constants.payment.EnumPaymentMode" %>
<%@ page import="com.hk.constants.payment.EnumPaymentStatus" %>
<%@ page import="com.hk.constants.shippingOrder.EnumShippingOrderStatus" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.admin.order.search.AgentSearchOrderAction" var="orderAdmin" event="pre"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="User Orders">
    <s:form beanclass="com.hk.web.action.admin.order.search.AgentSearchOrderAction" method="get" autocomplete="false">
        <c:forEach items="${orderAdmin.orderList}" var="order" varStatus="ctr">
            <table class="cont" width="100%">
                <thead>
                <tr>
                    <th width="10">Sr.</th>
                    <th>Order Status</th>
                    <th>Order Information</th>
                    <th>Addresses</th>
                    <th>Invoice Detail</th>
                    <c:if test="${not empty order.shippingOrders}">
                        <th>Shipping Order Details</th>
                    </c:if>
                </tr>
                </thead>
                <span class="upc lgry sml">GID</span> <strong><span class="or"> ${order.gatewayOrderId}</span></strong><br/>
                <span class="upc lgry sml">Base Order Id</span> <strong><span class="or"> ${order.id}</span></strong><br/>
                <c:if test="${order.payment != null}">
                    <span class="upc lgry sml">Payment Mode</span> <strong><span class="or"> ${order.payment.paymentMode.name}</span></strong><br/>
                    <span class="upc lgry sml">Payment Mode</span> <strong><span class="or"> ${order.payment.paymentDate}</span></strong><br/>
                </c:if>
                <tr>
                    <td class="has_table">
                        <c:if test="${not empty order.shippingOrders}">
                            <table>
                                <c:forEach items="${order.shippingOrders}" var="shippingOrder">
                                    <tr>
                                        <td>
                                            GatewayId:
                                            <s:link beanclass="com.hk.web.action.admin.order.search.SearchShippingOrderAction"
                                                    event="searchShippingOrder"
                                                    target="_blank">
                                                <s:param name="shippingOrderGatewayId" value="${shippingOrder.gatewayOrderId}"/>
                                                ${shippingOrder.gatewayOrderId} </s:link><br/>
                                            Status: ${shippingOrder.orderStatus.name} <br/>
                                            <c:if test="${shippingOrder.shipment !=null}">
                                                Track Link: <s:link beanclass="com.hk.web.action.core.order.TrackCourierAction" target="_blank">
                                                <s:param name="courierId" value="${shippingOrder.shipment.courier.id}"/>
                                                <s:param name="shippingOrder" value="${shippingOrder.id}"/>
                                                <s:param name="trackingId" value="${shippingOrder.shipment.awb.awbNumber}"/>
                                                ${shippingOrder.shipment.awb.awbNumber}
                                            </s:link>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </table>
                        </c:if>
                    </td>
                </tr>
            </table>
        </c:forEach>
    </s:form>
</s:layout-render>
