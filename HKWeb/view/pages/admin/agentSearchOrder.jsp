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

<s:layout-render name="/layouts/agentLayout.jsp" pageTitle="User Orders" >
 <s:layout-component name="content">
<s:form beanclass="com.hk.web.action.admin.order.search.AgentSearchOrderAction" method="get" autocomplete="false">
    <table width="100%" style="border: #222277; border-width: thick ">
    <thead>
    <tr>
        <th>Order Status</th>
        <th>Shipping Order Details</th>
        <th> Invoice Details</th>
    </tr>
    </thead>
     <tbody>
       <c:forEach items="${orderAdmin.orderList}" var="order" varStatus="ctr">
           <tr>
               <td>
                    <span class="upc lgry sml">GID</span> <strong><span class="or"> ${order.gatewayOrderId}</span></strong><br/>
                    <span class="upc lgry sml">Base Order Id</span> <strong><span class="or"> ${order.id}</span></strong><br/>
                    <c:if test="${order.payment != null}">
                        <span class="upc lgry sml">Payment mode</span> <strong><span class="or"> ${order.payment.paymentMode.name}</span></strong><br/>
                        <span class="upc lgry sml">Payment Date</span> <strong><span class="or"> ${order.payment.paymentDate}</span></strong><br/>
                    </c:if>
               </td>

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

                                        courierName: "${shippingOrder.shipment.courier.name}"
                                        awbNumber: "${shippingOrder.shipment.awb.awbNumber}"
                                        <span style="color:crimson;text-decoration:underline">
                                        Track Link: <s:link beanclass="com.hk.web.action.core.order.TrackCourierAction" target="_blank">
                                        <s:param name="courierId" value="${shippingOrder.shipment.courier.id}"/>
                                        <s:param name="shippingOrder" value="${shippingOrder.id}"/>
                                        <s:param name="trackingId" value="${shippingOrder.shipment.awb.awbNumber}"/>
                                        ${shippingOrder.shipment.awb.awbNumber}
                                        </span>
                                    </s:link>
                                    </c:if>
                                </td>
                                <td>
                                <table>
                                    Line Items:
                                    <c:forEach items="${shippingOrder.lineItems}" var="lineItem">
                                        <tr>
                                            Id: <span class="sml gry em" style="color: #009900;">  ${lineItem.id}  </span>
                                        </tr>
                                    </c:forEach>
                                </table>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>
                </c:if>
                </td>
               <td class="has_table">
               dummy
               <c:if test="${not empty order.cartLineItems}">
               <table>
                   <c:forEach items="${order.cartLineItems}" var="lineItem">
                      <%-- <c:if test="${lineItem.lineItemType.id == lineItemType_Product}">--%>
                           <tr>
                               <td width="300">
                                   <c:if test="${lineItem.comboInstance != null}">
                                        <span style="color:crimson;text-decoration:underline">
                                          (Part of Combo: ${lineItem.comboInstance.combo.name})
                                        </span><br/>
                                      </c:if>${lineItem.productVariant.product.name}<br/>
                              <span class="sml gry em">
                                <c:forEach items="${lineItem.productVariant.productOptions}" var="productOption">
                                    ${productOption.name} ${productOption.value}
                                </c:forEach>
                              </span>
                               </td>
                               <td>
                                  ${lineItem.qty}
                               </td>
                           </tr>
                       <%--</c:if>--%>
                   </c:forEach>
                   </c:if>

               </table>
            </td>
            </tr>
        </c:forEach>
     </tbody>
    </table>
</s:form>
   </s:layout-component>
</s:layout-render>
