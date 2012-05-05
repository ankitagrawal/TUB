<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/default.jsp" pageTitle="Payment error">

  <s:layout-component name="menu"> </s:layout-component>
  <s:layout-component name="heading">
    <div style="margin-top: 50px;">
      <h2 class="red">
        Payment Error
      </h2>
    </div>
  </s:layout-component>

  <s:layout-component name="left_col">
    <s:useActionBean beanclass="com.hk.web.action.core.payment.PaymentErrorAction" var="actionBean"/>

    <c:choose>
      <c:when test="${actionBean.payment != null}">
        <div style="background: #fdd; padding: 20px;">
        <p>There was an unexpected error in your payment. Your order has not been placed.</p>

        <p>The transaction id for this transaction is ${actionBean.payment.gatewayOrderId}</p>

        <p>Suspect cause : ${actionBean.errorMessage}</p>

        <h2>Support</h2>

        <p>Please try again. If it still does not work
          <s:link beanclass="com.hk.com.hk.web.action.pages.ContactAction">write to us</s:link> with the above details.</p>
        </div>
        
      </c:when>
      <c:otherwise>
        <div style="background: #fdd; padding: 20px;">
          Invalid request!
        </div>
      </c:otherwise>
    </c:choose>

  </s:layout-component>

</s:layout-render>
