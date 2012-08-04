<%@ page import="com.hk.constants.subscription.EnumSubscriptionStatus" %>
<%--
  Created by IntelliJ IDEA.
  User: Pradeep
  Date: 7/25/12
  Time: 9:03 AM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.user.CustomerSubscriptionHistoryAction" var="csha"/>
<s:layout-render name="/layouts/default.jsp">
    <s:layout-component name="heading">Subscription History</s:layout-component>
    <s:layout-component name="lhsContent">
        <jsp:include page="../myaccount-nav.jsp"/>
    </s:layout-component>

    <s:layout-component name="rhsContent">
        <c:set var="subscriptionStatusCancelled" value="<%=EnumSubscriptionStatus.Cancelled.getId()%>"/>
        <c:set var="subscriptionStatusExpired" value="<%=EnumSubscriptionStatus.Expired.getId()%>"/>
        <c:set var="subscriptionStatusOutOfStock" value="<%=EnumSubscriptionStatus.OutOfStock.getId()%>"/>
        <c:set var="subscriptionStatusInProcess" value="<%=EnumSubscriptionStatus.InProcess.getId()%>"/>
        <div class="main-inn-right">

            <div class="round-cont">
                <c:choose>
                    <c:when test="${!empty csha.subscriptionList}">
                        <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${csha}"/>
                        <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${csha}"/>

                        <table class="cont footer_color">
                            <tr>
                                <th>Subscription Id</th>
                                <th>Product</th>
                                <th>Details</th>
                                <th>Shipment Details</th>

                            </tr>
                            <tbody>
                            <c:forEach items="${csha.subscriptionList}" var="subscription">
                                <tr>
                                    <td>
                                            ${subscription.id}

                                    </td>
                                    <td>
                                        <table style="border-width: 0;">
                                            <tr>
                                                <td width="250">
                                                    <s:link beanclass="com.hk.web.action.core.catalog.product.ProductAction" class="prod_link" id="productLink" target="_blank">
                                                        <s:param name="productId" value="${subscription.productVariant.product.id}"/>
                                                        <s:param name="productSlug" value="${subscription.productVariant.product.slug}"/>
                                                        ${subscription.productVariant.product.name}
                                                    </s:link>
                                                    <br/>
              <span class="sml gry em">
                <c:forEach items="${subscription.productVariant.productOptions}" var="productOption">
                    ${productOption.name} ${productOption.value}
                </c:forEach>
              </span>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td width="200">
                                        subscription Period: ${subscription.subscriptionPeriodDays} days<br/>
                                        start date : <fmt:formatDate value="${subscription.startDate}"/> <br/>
                                        fequency : ${subscription.frequencyDays} days<br/>
                                        qty per delivery : ${subscription.qtyPerDelivery} <br/>


                                    </td>
                                    <td width="180">
                                        <c:choose>
                                            <c:when test="${subscription.subscriptionStatus.id !=subscriptionStatusExpired && subscription.subscriptionStatus.id!=subscriptionStatusCancelled}">
                                                <c:choose>
                                                    <c:when test="${subscription.subscriptionStatus.id==subscriptionStatusInProcess}">
                                                        shipment for <b><strong> <fmt:formatDate value="${subscription.nextShipmentDate}" /> </strong>  </b> is in process<br/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        Due date:<b><strong> <fmt:formatDate value="${subscription.nextShipmentDate}" /> </strong>  </b> <br/>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:when>
                                            <c:otherwise>
                                                <b><strong>${subscription.subscriptionStatus.name}</strong>  </b>  <br/>
                                            </c:otherwise>
                                        </c:choose>
                                        total deliveries: <fmt:formatNumber
                                            value="${subscription.qty/subscription.qtyPerDelivery}" maxFractionDigits="0"/> <br/>
                                        Shipments delivered : <fmt:formatNumber
                                            value="${subscription.qtyDelivered/subscription.qtyPerDelivery}" maxFractionDigits="0"/> <br/>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                        <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${csha}"/>
                        <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${csha}"/>
                        <br/><br/>
                    </c:when>
                    <c:otherwise>
                        <br/>
                        <br/>
                        You haven't subscribed for any item on healthkart till now.
                        <br/> <br/>
                        <s:link beanclass="com.hk.web.action.core.subscription.AboutSubscriptionAction" event="pre" target="_blank">(click here) </s:link> to know more about subscriptions
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </s:layout-component>
</s:layout-render>

<script type="text/javascript">
    window.onload = function() {
        document.getElementById("shLink").style.fontWeight = "bold";
    };
</script>
<style type="text/css">
    table {
        width: 100%;
        margin-bottom: 10px;
        margin-top: 5px;
        border: 1px solid;
        border-collapse: separate;
    }

    table th {
        background: #f0f0f0;
        padding: 5px;
        text-align: left;
    }

    table td {
        padding: 5px;
        text-align: left;
        font-size: small;
    }
</style>