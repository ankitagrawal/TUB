<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.payment.CheckPaymentAction" var="cpa"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Payment Seeker">
    <s:layout-component name="content">
        <fieldset>
            Payment Seeker (Currently works for ICICI/Citrus/Icici via Citrus (Credit debit cards only)
        </fieldset>
        <s:form beanclass="com.hk.web.action.admin.payment.CheckPaymentAction">
            <fieldset>
                <label>Enter Gateway Order Id</label>
                <s:text name="gatewayOrderId" id = "gatewayOrderId" style="width:180px;height:25px;"/> <br/>
                <label>Enter Start Date (yyyymmdd)</label>
                <s:text name="txnStartDate" id = "txnStartDate" style="width:180px;height:25px;"/>
                <label>Enter End Date (yyyymmdd)</label>
                <s:text name="txnEndDate" id = "txnEndDate" style="width:180px;height:25px;"/>
                <label>Enter Merchant Id</label>
                <s:text name="merchantId" id = "merchantId" style="width:180px;height:25px;"/>
                <s:submit name="seekPayment" value="Seek" id="save"/>
                <s:submit name="seekPayment" value="Historical List" id="searchTransactionByDate"/>
                <s:submit name="refundPayment" value="Refund  (beta)" id="refund"/>
            </fieldset>
        </s:form>

        <c:forEach  items="${cpa.transactionList}" var="paymentResultMap">
            <c:forEach items="${paymentResultMap}" var="paymentMapEntry">
                ${paymentMapEntry.key}  -->   ${paymentMapEntry.value}  <br/>
            </c:forEach>
        </c:forEach>

    </s:layout-component>
</s:layout-render>