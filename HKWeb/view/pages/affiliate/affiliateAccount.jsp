<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="web.action.affiliate.AffiliatePaymentAction" var="paymentAction"/>
<s:layout-render name="/layouts/defaultAdmin.jsp">
  <s:layout-component name="content">
    <h2>${paymentAction.affiliate.user.name}</h2>
    <br/>
    <h4>Checks Sent</h4>
    <table>
      <%--<caption>Check Details</caption>--%>
      <tr>
        <th>
          Check No.
        </th>
        <th>
          Issue Date
        </th>
        <th>
          Bank
        </th>
        <th>
          Amount
        </th>
      </tr>
      <c:forEach items="${paymentAction.checkDetailsList}" var="checkDetails">
        <tr>
          <td>
              ${checkDetails.checkNo}
          </td>
          <td>
              ${checkDetails.issueDate}
          </td>
          <td>
              ${checkDetails.bankName}
          </td>
          <td>
              ${checkDetails.affiliateTxn.amount}
          </td>
        </tr>
      </c:forEach>
    </table>

    <br/>

    <div class="buttons">
      <s:link beanclass="web.action.affiliate.AffiliatePaymentAction" event="paymentDetails">Pay
        <s:param name="affiliate" value="${paymentAction.affiliate.id}"/>
      </s:link>
    </div>
  </s:layout-component>
</s:layout-render>