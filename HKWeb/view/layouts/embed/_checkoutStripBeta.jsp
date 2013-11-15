<%--
  Created by IntelliJ IDEA.
  User: Sachit
  Date: 9/5/13
  Time: 4:39 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:layout-definition>
<%-- checkout strip flow begins--%>
<div class="ttl-cntnr">
                  <span class="icn icn-sqre">
                  </span>
    <h1 class="chckout-hdr"> Checkout </h1>

    <span class="icn icn-sqre"></span>
</div>
  <c:if test="${index==null}">
    <c:set var="index" value="1" />
  </c:if>
  <div class="ttl-cntnr mrgn-b-35">
    <span class="page-title ${index eq 1 ? 'current' : ''}">
    <c:choose>
      <c:when test="${index gt 1 and index ne 5}">
         <s:link beanclass="com.hk.web.action.core.user.SelectAddressAction" >Shipping address</s:link>
      </c:when>
      <c:otherwise>
        Shipping address
      </c:otherwise>
    </c:choose>
    </span>
    <span class="page-title ${index eq 2 ? 'current' : ''}">
      <c:choose>
        <c:when test="${index gt 2 and index ne 5}">
          <s:link beanclass="com.hk.web.action.core.order.OrderSummaryAction" >
            Confirm order
          </s:link>
        </c:when>
        <c:otherwise>
          Confirm order
        </c:otherwise>
      </c:choose>
    </span>
    <span class="page-title ${index eq 3 ? 'current' : ''}">
      <c:choose>
        <c:when test="${index gt 3 and index ne 5}">
          <s:link beanclass="com.hk.web.action.core.payment.PaymentModeAction" >
            Payment
          </s:link>
        </c:when>
        <c:otherwise>
          Payment
        </c:otherwise>
      </c:choose>
    </span>
      <c:if test="${index eq 4 }">
        <span class="page-title current">
        Billing Address
        </span>
      </c:if>
    <span class="page-title ${index eq 5 ? 'current' : ''}">
      Order complete
    </span>
</div>
</s:layout-definition>
<%--checkout strip flow ends--%>