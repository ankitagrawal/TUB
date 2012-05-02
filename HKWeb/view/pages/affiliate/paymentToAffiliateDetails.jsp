<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.affiliate.AffiliatePaymentAction" var="paymentAction"/>
<s:layout-render name="/layouts/defaultAdmin.jsp">
  <s:layout-component name="content">
    <h2>
        ${paymentAction.affiliate.user.name}
    </h2>
    <br/>

    <h2>
      <c:choose>
        <c:when test="${paymentAction.affiliate.checkInFavourOf==null}">
          Affiliate has not mentioned the name check will be issued to.
        </c:when>
        <c:otherwise>
          Check will be made in favour of : ${paymentAction.affiliate.checkInFavourOf}
        </c:otherwise>
      </c:choose>
    </h2>
      <c:choose>
        <c:when test="${paymentAction.checkDeliveryAddress==null}">
          <h2>Affiliate has not mentioned the address the check should be delivered to.</h2>
        </c:when>
        <c:otherwise>
          Delivery Address :
          <h2>
           ${paymentAction.checkDeliveryAddress.line1} <c:if
              test="${hk:isNotBlank(paymentAction.checkDeliveryAddress.line2)}">${paymentAction.checkDeliveryAddress.line2}</c:if><br/>
            ${paymentAction.checkDeliveryAddress.city} - ${paymentAction.checkDeliveryAddress.pin}<br/>
            ${paymentAction.checkDeliveryAddress.state}
            ${paymentAction.checkDeliveryAddress.phone}
          </h2>
        </c:otherwise>
      </c:choose>

    <s:form beanclass="com.hk.web.action.core.affiliate.AffiliatePaymentAction">
      <table>
        <tr>
          <th>
            Check No.
          </th>
          <td>
            <s:text name="checkDetails.checkNo"/>
          </td>
        </tr>
        <tr>
          <th>
            Issue Date
          </th>
          <td>
            <s:text name="checkDetails.issueDate"/>
          </td>
        </tr>
        <tr>
          <th>
            Bank
          </th>
          <td>
            <s:text name="checkDetails.bankName"/>
          </td>
        </tr>
        <tr>
          <th>
            Amount
          </th>
          <td>
            <s:text name="amountToPay"/>
          </td>
        </tr>
      </table>
      <s:submit name="payToAffiliate" value="Save"/>
      <s:hidden name="affiliate" value="${paymentAction.affiliate.id}"/>
    </s:form>
  </s:layout-component>
</s:layout-render>