<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.payment.CheckPaymentAction" var="cpa"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Payment Seeker">
    <s:layout-component name="content">
        <fieldset>
            Payment Seeker (Currently works for ICICI/Citrus/EBS/Icici via Citrus (Credit debit cards only)
        </fieldset>
        <s:form beanclass="com.hk.web.action.admin.payment.CheckPaymentAction">
            <fieldset>
                <label>Enter Gateway Order Id</label>
                <s:text name="gatewayOrderId" id = "gatewayOrderId" style="width:180px;height:25px;"/> <br/><br/>
                <label>Enter Start Date (MM/dd/yyyy)</label>
                <s:text name="txnStartDate" id = "txnStartDate" style="width:180px;height:25px;"/>
                <label>Enter End Date (MM/dd/yyyy)</label>
                <s:text name="txnEndDate" id = "txnEndDate" style="width:180px;height:25px;"/>
                <label>Enter Merchant Id</label>
                <s:text name="merchantId" id = "merchantId" style="width:180px;height:25px;"/>
                <br> <br>
                <label>Enter Payment Id (EBS)</label>
                <s:text name="paymentId" id = "paymentId" style="width:180px;height:25px;"/>
                <label>Enter Amount</label>
                <s:text name="amount" id = "amount" style="width:180px;height:25px;"/>
                <br>
                <s:submit name="seekPayment" value="Seek" id="save"/>
                <s:submit name="searchTransactionByDate" value="Historical List" id="searchTransactionByDate"/>
                <s:submit name="refundPayment" value="Refund  (beta)" id="refund"/>
                <s:submit name="capturePayment" value="Capture  (EBS)" id="capture"/>
                <s:submit name="cancelPayment" value="Cancel (EBS)" id="cancel"/>
                <shiro:hasRole name="<%=RoleConstants.GOD%>">
                    <s:submit name="bulkSeekPayment" value="Bulk Seek" id="bulkSeek"/>
                </shiro:hasRole>
            </fieldset>
        </s:form>

        <c:forEach  items="${cpa.transactionList}" var="paymentResultMap">
            <c:forEach items="${paymentResultMap}" var="paymentMapEntry">
                ${paymentMapEntry.key}  -->   ${paymentMapEntry.value}  <br/>
            </c:forEach>
            <br/>
        </c:forEach>

    </s:layout-component>
</s:layout-render>