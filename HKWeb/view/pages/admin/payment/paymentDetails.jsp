<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.payment.CheckPaymentAction" var="cpa"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Payment Seeker">
    <s:layout-component name="htmlHead">
        <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
        <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
    </s:layout-component>

    <s:layout-component name="content">
        <fieldset>
            Payment Seeker (Currently works for ICICI/Citrus/EBS/Icici via Citrus (Credit debit cards only)
        </fieldset>
        <s:form beanclass="com.hk.web.action.admin.payment.CheckPaymentAction">
            <fieldset>
                <label>Enter Gateway Order Id</label>
                <s:text name="gatewayOrderId" id = "gatewayOrderId" style="width:180px;height:25px;"/> <br/><br/>

                <label>Start date</label>
                <s:text class="date_input startDate" style="width:150px" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="txnStartDate"/>

                <label>End Date</label>
                <s:text class="date_input endDate" id = "txnEndDate" style="width:150px" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="txnEndDate"/>

                <label>Enter Merchant Id</label>
                <s:text name="merchantId" id = "merchantId" style="width:180px;height:25px;"/>
                <br> <br>
                <label>Enter Payment Id (EBS)</label>
                <s:text name="paymentId" id = "paymentId" style="width:180px;height:25px;"/>
                <label>Enter Amount</label>
                <s:text name="amount" id = "amount" style="width:180px;height:25px;"/>
                <label>Enter Reason for Refund</label>
                <s:select name="refundReasonActivity">
                    <s:option value="">-- Select --</s:option>
                    <c:forEach items="${cpa.orderLifecycleActivities}" var="lifecycleActivity">
                        <s:option value="${lifecycleActivity.id}"> ${lifecycleActivity.name} </s:option>
                    </c:forEach>
                </s:select>
                <s:text name="amount" id = "amount" style="width:180px;height:25px;"/>
                <br>
                <s:submit name="seekPayment" value="Seek" id="save"/>
                <s:submit name="searchTransactionByDate" value="Historical List" id="searchTransactionByDate"/>
                <s:submit name="refundPayment" value="Refund  (beta)" id="refund"/>
                <s:submit name="capturePayment" value="Capture  (EBS)" id="capture"/>
                <s:submit name="cancelPayment" value="Cancel (EBS)" id="cancel"/>
                <%--<shiro:hasRole name="<%=RoleConstants.GOD%>">--%>
                <s:submit name="bulkSeekPayment" value="Bulk Seek" id="bulkSeek"/>
                <%--</shiro:hasRole>--%>
                <s:submit name="updatePayment"  value="Update" id="update"/>

            </fieldset>
        </s:form>

        <c:forEach  items="${cpa.hkPaymentResponseList}" var="response">
            Gateway Order Id --> ${response.gatewayOrderId}  <br/>
            Transaction Type --> ${response.transactionType}<br/>
            Amount --> ${response.amount} <br/>
            Payment Status --> ${response.HKPaymentStatus.name}<br/>
            Response Message --> ${response.responseMsg}  <br/>
            Root Reference No --> ${response.rrn}<br/>
            Authentication Code --> ${response.authIdCode}<br/>
            Gateway --> ${response.gateway.name}<br/>
            Error Log --> ${response.errorLog}<br/>
            <br/>
        </c:forEach>



        <c:if test="${not empty cpa.payment}">
            Gateway Order Id --> ${cpa.payment.gatewayOrderId}  <br/>
            Transaction Type --> ${cpa.payment.transactionType}<br/>
            Amount --> ${cpa.payment.amount} <br/>
            Payment Status --> ${cpa.payment.paymentStatus.name}<br/>
            Response Message --> ${cpa.payment.responseMessage}  <br/>
            Root Reference No --> ${cpa.payment.rrn}<br/>
            Gateway --> ${cpa.payment.gateway.name}<br/>
            Payment Date --> ${cpa.payment.createDate}<br/>
            Error Log --> ${cpa.payment.errorLog}<br/>
            Parent gateway order id --> ${cpa.payment.parent.gatewayOrderId}<br/>
            <br/>
        </c:if>

        <c:forEach items="${cpa.bulkHkPaymentResponseList}" var="responseMap">
            <c:forEach items="${responseMap}" var="content">
                For : ${content.key}  <br/>
                <c:forEach items="${content.value}" var="response">
                    Gateway Order Id --> ${response.gatewayOrderId}  <br/>
                    Transaction Type --> ${response.transactionType}<br/>
                    Amount --> ${response.amount} <br/>
                    Payment Status --> ${response.HKPaymentStatus.name}<br/>
                    Response Message --> ${response.responseMsg}  <br/>
                    Root Reference No --> ${response.rrn}<br/>
                    Gateway --> ${response.gateway.name}<br/>
                    Error Log --> ${response.errorLog}<br/>
                    <br/>
                </c:forEach>
                <br/>
            </c:forEach>

        </c:forEach>

        <%--<c:if test="${not empty cpa.paymentStatus}">
            Status --> ${cpa.paymentStatus.name}
        </c:if>--%>


    </s:layout-component>
</s:layout-render>