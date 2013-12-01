<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.core.referral.ReferredOrderDetailsAction" var="oa"/>

<s:layout-render name="/layouts/default.jsp">
  <s:layout-component name="heading">Order Details</s:layout-component>
  <s:layout-component name="left_col">

    <c:set var="pricingDto" value="${oa.pricingDto}"/>

    <table class="cont footer_color" width="100%" style="font-size: 14px;">
      <tr>
        <th style="background: #f0f0f0; padding: 5px; font-weight: bold;">Item</th>
        <th style="background: #f0f0f0; padding: 5px; font-weight: bold;">Quantity</th>
        <th style="background: #f0f0f0; padding: 5px; font-weight: bold;">HK Price</th>
      </tr>
      <c:forEach items="${pricingDto.productLineItems}" var="invoiceLineItem">
        <tr style="border-bottom: 1px solid #f0f0f0;">
          <td style="border-bottom:none; padding: 15px;">
            <strong>${invoiceLineItem.productVariant.product.name}</strong>
            <c:if test="${not empty invoiceLineItem.productVariant.productOptions}">
              <br/><span class="gry">
                <c:forEach items="${invoiceLineItem.productVariant.productOptions}" var="productOption"
                           varStatus="optionCtr">
                  ${productOption.name} ${productOption.value}${!optionCtr.last?',':''}
                </c:forEach>
              </span>
            </c:if>
          </td>
          <td style="border-bottom:none; padding-bottom: 0;">
            <fmt:formatNumber value="${invoiceLineItem.qty}" maxFractionDigits="0"/>
          </td>
          <td align="right" style="border-bottom:none; padding-bottom: 0;">
            ${invoiceLineItem.hkPrice}
          </td>
        </tr>
      </c:forEach>
    </table>

  </s:layout-component>

  <s:layout-component name="footer"> </s:layout-component>
</s:layout-render>
