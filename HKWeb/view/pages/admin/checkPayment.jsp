<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page import="com.hk.constants.order.EnumOrderStatus" %>
<%@ page import="com.hk.constants.payment.EnumPaymentMode" %>
<%@ page import="com.hk.constants.payment.EnumPaymentStatus" %>
<%@ page import="com.hk.constants.payment.EnumPaymentTransactionType" %>
<%@ page import="com.hk.constants.payment.EnumGateway" %>
<%@ page import="com.hk.constants.core.EnumPermission" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.admin.payment.CheckPaymentAction" var="checkPaymentBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Welcome">

    <s:layout-component name="heading">${checkPaymentBean.currentBreadcrumb.name}</s:layout-component>

    <c:set var="orderStatusInCart" value="<%=EnumOrderStatus.InCart.getId()%>"/>
    <c:set var="orderStatusCancelled" value="<%=EnumOrderStatus.Cancelled.getId()%>"/>
    <c:set var="orderStatusPlaced" value="<%=EnumOrderStatus.Placed.getId()%>"/>
    <c:set var="orderStatusSplit" value="<%=EnumOrderStatus.InProcess.getId()%>"/>
    <c:set var="paymentStatusAuthPending" value="<%=EnumPaymentStatus.AUTHORIZATION_PENDING.getId()%>"/>
    <c:set var="paymentStatusRequested" value="<%=EnumPaymentStatus.REQUEST.getId()%>"/>
    <c:set var="paymentStatusSuccess" value="<%=EnumPaymentStatus.SUCCESS.getId()%>"/>
    <c:set var="paymentModeCod" value="<%=EnumPaymentMode.COD.getId()%>"/>
    <c:set var="refundTxn" value="<%=EnumPaymentTransactionType.REFUND.getName()%>"/>
    <c:set var="onlinePaymentMode" value="<%=EnumPaymentMode.ONLINE_PAYMENT.getId()%>"/>
    <c:set var="autoPaymentGateways" value="<%=EnumGateway.getHKServiceEnabledGateways()%>"/>
    <c:set var="manualPaymentGateways" value="<%=EnumGateway.getManualRefundGateways()%>"/>

    <s:layout-component name="content">

        <h2>Order Id ${checkPaymentBean.order.id}, Order Status: ${checkPaymentBean.order.orderStatus.name}</h2>

        <div class="orderSummaryNew" style="width: 100%;left: -5px;margin-bottom: 30px;">
            <s:layout-render name="/layouts/embed/orderSummaryTable.jsp" pricingDto="${checkPaymentBean.pricingDto}"
                             orderDate="${checkPaymentBean.payment.paymentDate}"/>
        </div>

        <div class="leftPS">

            <div class="step2 success_order_summary" style="padding: 5px; float: left; margin-right: 5px;">
                <h2 class="paymentH2">Order Summary</h2>

                <div class="itemSummaryNew">
                    <s:layout-render name="/layouts/embed/itemSummaryTable.jsp"
                                     pricingDto="${checkPaymentBean.pricingDto}"
                                     orderDate="${checkPaymentBean.payment.paymentDate}"/>
                </div>

            </div>

        <c:choose>
            <c:when test="${checkPaymentBean.payment.paymentStatus.id == paymentStatusAuthPending}">
                <%--your cod ka message--%>
                <h1 class="youPaid" style="right: 10px;border-bottom: 1px solid #ddd;width: 100%;">
                          <span class="youPay">
                            Pay on delivery:
                          </span>
                    <strong>
                            <span id="summaryGrandTotalPayable" class="youPayValue">
                              <fmt:formatNumber value="${checkPaymentBean.pricingDto.grandTotalPayable}" type="currency"
                                                currencySymbol="Rs. "/>
                            </span>
                    </strong>

                    <div class='newShippingHandling'>
                        (inclusive of discounts, shipping, handling and taxes.)
                    </div>
                </h1>

            </c:when>
            <%--your non cod ka message--%>
            <c:otherwise>
                <h1 class="youPaid" style="right: 10px;border-bottom: 1px solid #ddd;width: 100%;">
                          <span class="youPay">
                            You paid:
                          </span>
                    <strong>
                            <span id="summaryGrandTotalPayable" class="youPayValue">
                              <fmt:formatNumber value="${checkPaymentBean.pricingDto.grandTotalPayable}" type="currency"
                                                currencySymbol="Rs. "/>
                            </span>
                    </strong>

                    <div class='newShippingHandling'>
                        (inclusive of discounts, shipping, handling and taxes.)
                    </div>
                </h1>

            </c:otherwise>
        </c:choose>

        <div class="orderShippedTo" style="margin-bottom: 60px;width: 105%;">
            <h2 class="paymentH2"
                style="font-weight:bold;border-bottom: 1px solid rgb(158, 158, 158);padding-bottom: 7px;">ORDER
                SHIPPED TO</h2>

            <p>
                <c:set var="address" value="${checkPaymentBean.payment.order.address}"/>
                <strong>${address.name}</strong> <br/>
                    ${address.line1},
                <c:if test="${not empty address.line2}">
                    ${address.line2},
                </c:if>
                    ${address.city} - ${address.pincode.pincode}<br/>
                    ${address.state}, <span class="upc">INDIA</span><br/>
                <span class="sml lgry upc">Phone </span> ${address.phone}<br/>
            </p>
        </div>



        <s:form beanclass="com.hk.web.action.admin.payment.CheckPaymentAction">

            <div>
                <table>
                    <thead>
                    <tr>
                        <shiro:hasPermission name="<%=PermissionConstants.PAYMENT_RESOLVER%>">
                            <th colspan="10">
                                Payment List
                                (
                                <s:link beanclass="com.hk.web.action.admin.payment.NewPaymentAction">
                                    <s:param name="order" value="${checkPaymentBean.order}"/>
                                    <s:param name="amount" value="${checkPaymentBean.pricingDto.grandTotalPayable}"/>
                                    <s:param name="paymentMode" value="<%=EnumPaymentMode.ONLINE_PAYMENT.getId()%>"/>
                                    <s:param name="paymentStatus" value="<%=EnumPaymentStatus.SUCCESS.getId()%>"/>
                                    Create New Payment
                                </s:link>
                                )
                                <br/>
                            </th>
                        </shiro:hasPermission>
                    </tr>
                    <tr>
                        <th></th>
                        <th>gateway Id</th>
                        <th>Create Date</th>
                        <th>Payment Date</th>
                        <th>Amount</th>
                        <th>Status</th>
                        <th>Mode</th>
                        <th>Gateway</th>
                        <th>Issuer</th>
                        <th>Response Msg</th>
                        <th>Error log</th>
                        <th>Gateway Transaction Id</th>
                        <th>RRN</th>
                        <th>AuthId Code</th>
                        <th>Txn Type</th>
                    </tr>
                    </thead>
                    <c:forEach items="${checkPaymentBean.paymentList}" var="payment" varStatus="ctr">
                        <tr>
                                <%--<c:set var="radioDisabled" value="${checkPaymentBean.pricingDto.grandTotalPayable eq payment.amount ? false : true}"/>--%>
                            <c:if test="${payment.transactionType ne refundTxn}">
                                <td><s:radio value="${payment.id}" name="payment"/></td>
                            </c:if>
                            <c:if test="${payment.transactionType eq refundTxn}"> <td></td></c:if>
                                <%--disabled="${radioDisabled}" --%>
                            <td>
                                    ${payment.gatewayOrderId}
                                <shiro:hasPermission name="<%=PermissionConstants.PAYMENT_RESOLVER%>">
                                    (<s:link beanclass="com.hk.web.action.admin.payment.EditPaymentAction">
                                    <s:param name="paymentId" value="${payment.id}"/>
                                    Edit Payment
                                </s:link>)
                                </shiro:hasPermission>
                            <c:if test="${payment.transactionType ne refundTxn}">
                                (<s:link beanclass="com.hk.web.action.admin.payment.CheckPaymentAction" target="_blank"
                                         event="seekPayment" >
                                <s:param name="gatewayOrderId" value="${payment.gatewayOrderId}"/>
                                Seek Payment
                            </s:link>) </c:if>

                             <c:if test="${payment.transactionType ne refundTxn and payment.paymentStatus.id eq paymentStatusSuccess}">
                                (<s:link beanclass="com.hk.web.action.admin.payment.CheckPaymentAction" target="_blank"
                                         event="refundPayment">
                                <s:param name="gatewayOrderId" value="${payment.gatewayOrderId}"/>
                                <s:param name="amount" value="${payment.amount}"/>
                                 Refund Payment
                            </s:link>) </c:if>
                            </td>
                            <td><fmt:formatDate value="${payment.createDate}" type="both"/></td>
                            <td><fmt:formatDate value="${payment.paymentDate}" type="both"/></td>
                            <td><fmt:formatNumber value="${payment.amount}" currencySymbol="Rs. " type="currency"/></td>
                            <td>${payment.paymentStatus.name}</td>
                            <td>${payment.paymentMode.name}</td>
                            <td><c:if test="${payment.gateway != null}">
                                ${payment.gateway.name}
                            </c:if>
                            </td>
                            <td><c:if test="${payment.issuer != null}">
                                    ${payment.issuer.name}
                            </c:if></td>
                            <td>
                                    ${payment.responseMessage}
                            </td>
                            <td>
                                    ${payment.errorLog}
                            </td>
                            <td>
                                    ${payment.gatewayReferenceId}
                            </td>
                            <td>
                                    ${payment.rrn}
                            </td>
                            <td>
                                    ${payment.authIdCode}
                            </td>
                                    <td>
                                            ${payment.transactionType}
                                    </td>
                        </tr>
                    </c:forEach>
                </table>
            </div>

            <div>
                <s:hidden name="order"/>

                <%--<c:when test="${checkPaymentBean.payment.paymentMode.id eq onlinePaymentMode}">--%>
                    <shiro:hasPermission name="<%=PermissionConstants.AUTO_UPDATE_PAYMENT%>">
                        <s:submit name="autoUpdate" value="Auto Confirm" onclick="return confirm('Are you sure?')"/>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="<%=PermissionConstants.MANUAL_UPDATE_PAYMENT%>">
                        <s:submit name="manualUpdate" value="Manual Confirm" onclick="retrun confirm('Are you sure')"/> 
                    </shiro:hasPermission>
                <%--</c:when>--%>
                <%--<c:otherwise>--%>
                    <shiro:hasPermission name="<%=PermissionConstants.UPDATE_PAYMENT%>">
                        <s:submit name="acceptAsSuccessful" value="Confirm Cheque/Neft/Cash" onclick="retrun confirm('Are you sure')"/>    
                    </shiro:hasPermission>                                        
                <%--</c:otherwise>--%>


                    <%--
                            <c:if test="${checkPaymentBean.order.orderStatus.id eq orderStatusInCart && checkPaymentBean.order.address != null}">
                              <s:submit name="acceptAsAuthPending" value="Accept payment as Auth Pending" onclick="return confirm('Are you sure?')"/>
                            </c:if>
                            <c:if test="${(checkPaymentBean.order.orderStatus.id eq orderStatusPlaced || checkPaymentBean.order.orderStatus.id eq orderStatusSplit ) && checkPaymentBean.payment.paymentStatus.id != paymentStatusSuccess}">
                              <s:submit name="updateToSuccess" value="Update as successful" onclick="return confirm('Are you sure?')"/>
                            </c:if>
                            <c:if test="${checkPaymentBean.order.address != null}">
                              <s:submit name="associateToPayment" value="Associate to payment" onclick="return confirm('Are you sure?')"/>
                            </c:if>
                    --%>
                
            </div>
        </s:form>
    </s:layout-component>

</s:layout-render>
