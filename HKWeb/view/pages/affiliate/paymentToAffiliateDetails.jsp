<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.affiliate.AffiliatePaymentAction" var="paymentAction"/>
<s:layout-render name="/layouts/defaultAdmin.jsp">
    <s:layout-component name="htmlHead">
        <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
        <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
    </s:layout-component>
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
            ${paymentAction.checkDeliveryAddress.city} - ${paymentAction.checkDeliveryAddress.pincode.pincode}<br/>
            ${paymentAction.checkDeliveryAddress.state}
            ${paymentAction.checkDeliveryAddress.phone}
          </h2>
        </c:otherwise>
      </c:choose>

    <s:form beanclass="com.hk.web.action.core.affiliate.AffiliatePaymentAction">
      <table>
        <tr>
          <th>
            Transaction Ref No.
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
              <s:text class="date_input startDate" style="width:150px"
                      formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="checkDetails.issueDate"/>
          </td>
        </tr>
        <tr>
          <th>
            Payment Mode
          </th>
          <td>
            <s:text name="checkDetails.bankName"/>
          </td>
        </tr>
        <tr>
          <th>
            Amount Qualified for
          </th>
          <td>
            <s:text name="amountToPay" readonly="readonly"/>
          </td>
        </tr>
          <tr>
              <th>
                  TDS
              </th>
              <td>
                  <s:text name="checkDetails.tds"/>
              </td>
          </tr>
      </table>
         <s:hidden name="affiliate" value="${paymentAction.affiliate.id}"/>
      <s:submit name="payToAffiliate" value="Save"/>
    </s:form>
  </s:layout-component>
</s:layout-render>