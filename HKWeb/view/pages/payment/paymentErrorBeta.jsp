<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultBeta.jsp" pageTitle="Payment error">

  <s:layout-component name="menu"> </s:layout-component>
  <s:layout-component name="heading">Payment Error</s:layout-component>

  <s:layout-component name="left_col">
    <s:useActionBean beanclass="com.hk.web.action.core.payment.PaymentErrorAction" var="actionBean"/>

    <c:choose>
      <c:when test="${actionBean.payment != null}">
        <div style="padding: 20px;">
        <p>There was an unexpected error in your payment. Your order has not been placed.</p>

        <p class="mrgn-t-10">The transaction id for this transaction is <span class="txt-blue">${actionBean.payment.gatewayOrderId}</span></p>

        <p><span class="fnt-bold">Cause :</span> ${actionBean.errorMessage}</p>

        <p><span class="fnt-bold">Response Message from gateway  :</span> ${actionBean.payment.responseMessage}</p>

        <h2 class="mrgn-t-10 mrgn-b-5">Support</h2>

            <p><s:link beanclass="com.hk.web.action.core.payment.PaymentModeAction" class="txt-blue">Please try again.</s:link>
                If it still does not work
                <s:link beanclass="com.hk.web.action.pages.ContactAction" class="txt-blue">write to us</s:link> with the above
                details.</p>
        </div>

      </c:when>
      <c:otherwise>
        <div style="padding: 20px;">
          Invalid request!
        </div>
      </c:otherwise>
    </c:choose>

    <a href="/" class="btn btn-blue" style="width: 250px;float: right;margin-top: 10px;">GO BACK TO HEALTHKART.COM</a>
    <div class="floatfix" style="margin-bottom: 40px;"></div>

  </s:layout-component>

</s:layout-render>
