<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.payment.CheckPaymentAction" var="cpa"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Payment Seeker">
    <s:layout-component name="content">
        <fieldset>
            Payment Seeker
        </fieldset>
        <s:form beanclass="com.hk.web.action.admin.payment.CheckPaymentAction">
            <fieldset>
                <label>Enter Gateway Order Id</label>
                <s:text name="gatewayOrderId" id = "gatewayOrderId" style="width:180px;height:25px;"/>
                <s:submit name="seekPayment" value="Seek" id="save"/>
            </fieldset>
        </s:form>

        <c:forEach items="${cpa.paymentResultMap}" var="paymentMapEntry">
              ${paymentMapEntry.key}  -->   ${paymentMapEntry.value}  <br/>
        </c:forEach>

    </s:layout-component>
</s:layout-render>