<%--
  Created by IntelliJ IDEA.
  User: Pradeep
  Date: 7/19/12
  Time: 7:43 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.subscription.SubscriptionOrdersAction" var="subscriptionOrdersBean"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Subscription Orders">
    <s:layout-component name="heading">Subscription Orders : Subscription#${subscriptionOrdersBean.subscription.id}</s:layout-component>
    <s:layout-component name="content">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" class="zebra_vert">
            <tr class="even_row">
                <th >Subscription Order Id</th>
                <th >BaseOrder Id</th>
                <th >subscription price</th>

                <th>hk price </th>
                <th>Base Order Price</th>
                <th>status</th>
            </tr>
            <c:forEach items="${subscriptionOrdersBean.subscriptionOrders}" var="subscriptionOrder">
                <tr>
                    <td> ${subscriptionOrder.id}</td>
                    <td>
                    <s:link beanclass="com.hk.web.action.admin.order.search.SearchOrderAction" target="_blank" event="searchOrders" >
                    ${subscriptionOrder.baseOrder.id}
                    <s:param name="orderId" value="${subscriptionOrder.baseOrder.id}"/>
                        </s:link>
                    </td>
                    <td>${subscriptionOrder.subscription.subscriptionPrice}</td>

                    <td>${subscriptionOrder.hkPriceNow}</td>
                    <td>${subscriptionOrder.baseOrder.amount}</td>
                    <td>${subscriptionOrder.subscriptionOrderStatus.name}</td>
                </tr>
            </c:forEach>
        </table>
    </s:layout-component>
</s:layout-render>