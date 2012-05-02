<%@ page import="com.hk.constants.shippingOrder.EnumShippingOrderStatus" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="web.action.OrderDetailsAction" var="oa"/>
<c:set var="shippingOrderStatus_shipped" value="<%=EnumShippingOrderStatus.SO_Shipped.getId()%>"/>

<s:layout-render name="/layouts/default.jsp">
  <s:layout-component name="heading">Order Details</s:layout-component>
  <s:layout-component name="left_col">

    <table class="cont footer_color" width="100%" style="font-size: 14px;">
      <tr>
        <th style="background: #f0f0f0; padding: 5px; font-weight: bold;">Item</th>
        <th style="background: #f0f0f0; padding: 5px; font-weight: bold;">Quantity</th>
        <th style="background: #f0f0f0; padding: 5px; font-weight: bold;">Tracking Details</th>
      </tr>
      <c:choose>
        <c:when test="${!empty oa.lineItems}">
          <c:forEach items="${oa.lineItems}" var="lineItem">
            <tr style="border-bottom: 1px solid #f0f0f0;">
              <td style="border-bottom:none; padding: 15px;">
                <strong>${lineItem.sku.productVariant.product.name}</strong>
                <c:if test="${not empty lineItem.sku.productVariant.productOptions}">
                  <br/><span class="gry">
                    <c:forEach items="${lineItem.sku.productVariant.productOptions}" var="productOption"
                               varStatus="optionCtr">
                      ${productOption.name} ${productOption.value}${!optionCtr.last?',':''}
                    </c:forEach>
                  </span>
                </c:if>
              </td>
              <td style="border-bottom:none; padding-bottom: 0;">
                <fmt:formatNumber value="${lineItem.qty}" maxFractionDigits="0"/>
              </td>
              <td align="right" style="border-bottom:none; padding-bottom: 0;">
                <c:set var="shipment" value="${lineItem.shippingOrder.shipment}"/>
                ${lineItem.shippingOrder.baseOrder.orderStatus.name}
                <c:if test="${lineItem.shippingOrder.orderStatus.id == shippingOrderStatus_shipped && shipment != null}">
                  by ${shipment.courier.name} - ${shipment.trackingId} on <fmt:formatDate value="${shipment.shipDate}"/><br/>
                  <s:link beanclass="web.action.TrackCourierAction" target="_blank">
                    <s:param name="courierId" value="${shipment.courier.id}"/>
                    <s:param name="trackingId" value="${shipment.trackingId}"/>
                    (Track this)
                  </s:link>
                </c:if>
              </td>
            </tr>
          </c:forEach>
        </c:when>
        <c:otherwise>
          <c:forEach items="${oa.cartLineItems}" var="cartLineItem">
            <tr style="border-bottom: 1px solid #f0f0f0;">
              <td style="border-bottom:none; padding: 15px;">
                <strong>${cartLineItem.productVariant.product.name}</strong>
                <c:if test="${not empty cartLineItem.productVariant.productOptions}">
                  <br/><span class="gry">
                    <c:forEach items="${cartLineItem.productVariant.productOptions}" var="productOption"
                               varStatus="optionCtr">
                      ${productOption.name} ${productOption.value}${!optionCtr.last?',':''}
                    </c:forEach>
                  </span>
                </c:if>
              </td>
              <td style="border-bottom:none; padding-bottom: 0;">
                <fmt:formatNumber value="${cartLineItem.qty}" maxFractionDigits="0"/>
              </td>
              <td align="right" style="border-bottom:none; padding-bottom: 0;">
                ${cartLineItem.order.orderStatus.name}
              </td>
            </tr>
          </c:forEach>
        </c:otherwise>
      </c:choose>
    </table>
  </s:layout-component>
  <s:layout-component name="footer"> </s:layout-component>
</s:layout-render>
