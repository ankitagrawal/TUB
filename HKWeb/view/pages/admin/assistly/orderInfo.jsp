<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.admin.assistly.OrderInfoAction" event="pre" var="orderSummary"/>

<s:layout-render name="/layouts/assistly.jsp">
  <s:layout-component name="content">
    <p>
      Invoice for Order#${orderSummary.order.gatewayOrderId}
      [<s:link beanclass="com.hk.web.action.admin.order.search.SearchOrderAction" event="searchOrders" target="_blank">
        <s:param name="orderId" value="${orderSummary.order.id}"/>
        search order
      </s:link>]
      <br/>placed on
      <fmt:formatDate value="${orderSummary.order.payment.createDate}" type="both" timeStyle="short"/>
    </p>

    <div>
      <c:if test="${orderSummary.pricingDto.codLineCount > 0}">
        <p>Cash on Delivery :
          <fmt:formatNumber value="${orderSummary.pricingDto.grandTotalPayable}" type="currency" currencySymbol="Rs. " maxFractionDigits="0"/></p>
      </c:if>
    </div>

    <div>
      Address: ${orderSummary.order.address.name}, ${orderSummary.order.address.line1},
      <c:if test="${not empty orderSummary.order.address.line2}">${orderSummary.order.address.line2}, </c:if>${orderSummary.order.address.city} - ${orderSummary.order.address.pincode.pincode}, ${orderSummary.order.address.state}<br/>
      Ph: ${orderSummary.order.address.phone}<br/>
    </div>

    <hr/>
    <c:if test="${orderSummary.order.userComments != null}">
      User Instructions:- ${orderSummary.order.userComments}<br/>
      <hr/>
    </c:if>
    <div>

      <table style="font-size: .8em;">
        <tr>
          <th width="150">Item</th>
          <th width="300">Item details</th>
          <th width="150"></th>
          <th width="50">Quantity</th>
          <th width="100">Unit price</th>
          <th width="50">Total(Rs.)</th>
        </tr>
        <c:forEach items="${orderSummary.pricingDto.productLineItems}" var="invoiceLineItem">
          <tr>
            <td>
                ${invoiceLineItem.productVariant.product.name}
            </td>
            <td>
              <em><c:forEach items="${invoiceLineItem.productVariant.productOptions}" var="productOption">
                ${productOption.name} ${productOption.value}, 
              </c:forEach></em>
            </td>
            <td>
              <c:forEach items="${invoiceLineItem.cartLineItemExtraOptions}" var="extraOption">
                <label>${extraOption.name} : ${extraOption.value}</label><br/>
              </c:forEach>
            </td>
            <td>
              <fmt:formatNumber value="${invoiceLineItem.qty}" maxFractionDigits="0"/></td>
            <td> ${invoiceLineItem.hkPrice} </td>
            <td>
              <fmt:formatNumber value="${invoiceLineItem.hkPrice * invoiceLineItem.qty}" type="currency" currencySymbol="Rs. "/></td>
          </tr>
        </c:forEach>
      </table>
    </div>

    <div style="font-size: .8em;">
      <h3>Order Summary</h3>
      <table class="cont footer_color">
        <tr>
          <td>Item Total</td>
          <td>
            <fmt:formatNumber value="${orderSummary.pricingDto.productsHkSubTotal}" type="currency" currencySymbol="Rs. "/>
          </td>
        </tr>
        <tr>
          <td>Shipping</td>
          <td>
            <fmt:formatNumber value="${orderSummary.pricingDto.shippingSubTotal}" type="currency" currencySymbol="Rs. "/>
          </td>
        </tr>
        <tr>
          <td>Discount ${orderSummary.order.offerInstance.coupon.code}</td>
          <td>
            <fmt:formatNumber value="${orderSummary.pricingDto.totalDiscount}" type="currency" currencySymbol="Rs. "/>
          </td>
        </tr>
        <c:if test="${orderSummary.pricingDto.redeemedRewardPoints > 0}">
          <tr>
            <td>Redeemed Rewards Point</td>
            <td>
              <fmt:formatNumber value="${orderSummary.pricingDto.redeemedRewardPoints}" type="currency" currencySymbol="Rs. "/></td>
          </tr>
        </c:if>
        <c:if test="${orderSummary.pricingDto.codLineCount > 0}">
          <tr>
            <td>COD charges</td>
            <td>
              <fmt:formatNumber value="${orderSummary.pricingDto.codSubTotal}" type="currency" currencySymbol="Rs. "/></td>
          </tr>
        </c:if>
        <tr>
          <td><strong>Grand Total</strong></td>
          <td>
            <strong><fmt:formatNumber value="${orderSummary.pricingDto.grandTotalPayable}" type="currency" currencySymbol="Rs. "/> </strong>
          </td>
        </tr>
      </table>
    </div>

    <div style="font-size: .8em;">
      <h3>Payment Details</h3>

      <p>
        Payment Mode : ${orderSummary.order.payment.paymentMode.name}<br/>
          <c:if test="${orderSummary.order.payment.gateway != null}">
              <span style="margin-left:30px;">Gateway: ${orderSummary.order.payment.gateway.name}</span>
          </c:if>
        Payment Status : ${orderSummary.order.payment.paymentStatus.name}
      </p>
    </div>

    <hr/>

  </s:layout-component>
</s:layout-render>
