<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page import="com.hk.dto.pricing.PricingDto" %>
<%@ page import="java.util.Date" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%--
Pass an attribute called pricingDto to render a table with pricing details
--%>
<s:useActionBean beanclass="com.hk.web.action.core.cart.CartAction" var="cartAction"/>
<s:layout-definition>
<%
  PricingDto pricingDto = (PricingDto) pageContext.getAttribute("pricingDto");
  Date orderDate = (Date) pageContext.getAttribute("orderDate");
  // this line does not mean anything, but simply gets idea to understand pricingDto's type, autocompletion works now.
  pageContext.setAttribute("pricingDto", pricingDto);
  pageContext.setAttribute("orderDate", orderDate);
  if (pricingDto != null && orderDate != null) {
%>

<div class='products_container' style="overflow: visible;">
<c:forEach items="${pricingDto.productLineItems}" var="invoiceLineItem" varStatus="ctr1">
  <c:if
      test="${invoiceLineItem.comboInstance == null && invoiceLineItem.productVariant.paymentType.name != 'Postpaid'}">
    <div class='product' style="border-bottom-style: solid;">
      <div class='img48' style="vertical-align:top;">
        <c:choose>
          <c:when test="${invoiceLineItem.productVariant.product.mainImageId != null}">
            <hk:productImage imageId="${invoiceLineItem.productVariant.product.mainImageId}"
                             size="<%=EnumImageSize.TinySize%>"/>
          </c:when>
          <c:otherwise>
            <img class="prod48"
                 src="${pageContext.request.contextPath}/images/ProductImages/ProductImagesThumb/${invoiceLineItem.productVariant.product.id}.jpg"
                 alt="${invoiceLineItem.productVariant.product.name}"/>
          </c:otherwise>
        </c:choose>
      </div>
      <div class='name'>
        <table width="70%">
          <tr>
            <td>
                ${invoiceLineItem.productVariant.product.name} <br/>

                ${invoiceLineItem.productVariant.variantName}
            </td>
            <td align="left" style="text-align:right">${invoiceLineItem.qty}</td>
              <c:set var="TH" value="TH"/>
              <c:set var="THBF" value="THBF"/>
              <c:set var="CO" value="CO"/>
              <c:set var="COBF" value="COBF"/>
            <c:if test="${not empty invoiceLineItem.cartLineItemConfig.cartLineItemConfigValues}">
          <tr>
            <td style="text-align: left; padding:5px; border: 1px solid #f0f0f0;background: #fafafa;">${invoiceLineItem.productVariant.product.name}</td>
            <td style="text-align: left; padding:3px;border: 1px solid #f0f0f0;background: #fff;">
              Rs. ${invoiceLineItem.productVariant.hkPrice}</td>
          </tr>
          <c:forEach items="${invoiceLineItem.cartLineItemConfig.cartLineItemConfigValues}" var="configValue">
            <c:set var="variantConfigOption" value="${configValue.variantConfigOption}"/>
            <tr>
              <c:set var="addParam" value="${variantConfigOption.additionalParam}"/>
              <td style="text-align: left; padding:5px; border: 1px solid #f0f0f0;background: #fafafa;">${variantConfigOption.displayName}
                : ${configValue.value}
                <c:if test="${(addParam ne TH) or (addParam ne THBF) or (addParam ne CO) or (addParam ne COBF) }">
                  <c:if
                      test="${fn:startsWith(variantConfigOption.name, 'R')==true}">
                    (R)
                  </c:if>
                    <c:if
                      test="${fn:startsWith(variantConfigOption.name, 'L')==true}">
                    (L)
                  </c:if>
                </c:if>
              </td>
              <td style="text-align: left; padding:3px;border: 1px solid #f0f0f0;background: #fff;">
                <c:choose>
                  <c:when test="${configValue.additionalPrice eq 0 }">
                    included
                  </c:when>
                  <c:otherwise>
                    +Rs. ${configValue.additionalPrice}
                    <%--<c:if test="${(addParam eq TH) or (addParam eq THBF) or (addParam eq CO) or (addParam eq COBF) }">--%>
                    <%--/Eye--%>
                    <%--</c:if>--%>
                  </c:otherwise>
                </c:choose>
              </td>
            </tr>
          </c:forEach>
          </c:if>
          </tr>
        </table>
      </div>

      <div class='price'>
        <div class="hk">
        </div>
        <div class="num"> Rs
          <span class="lineItemSubTotalMrp" style="font-weight:bold;"><fmt:formatNumber
              value="${invoiceLineItem.hkPrice * invoiceLineItem.qty}"
              pattern="<%=FormatUtils.currencyFormatPattern%>"/></span>
        </div>
      </div>
      <div class='floatix'></div>
    </div>
  </c:if>
</c:forEach>
<c:set var="firstComboLineItem" value=""/>
<c:forEach items="${pricingDto.productLineItems}" var="invoiceLineItem" varStatus="ctr1">
  <c:if test="${invoiceLineItem.comboInstance != null}">
    <c:if test="${firstComboLineItem != invoiceLineItem.comboInstance.combo}">
      <c:set var="firstComboLineItem" value="${invoiceLineItem.comboInstance.combo}"/>
      <div class='product' style="border-bottom-style: solid;">
        <div class='img48' style="vertical-align:top;">
          <c:choose>
            <c:when test="${invoiceLineItem.comboInstance.combo.mainImageId != null}">
              <hk:productImage imageId="${invoiceLineItem.comboInstance.combo.mainImageId}"
                               size="<%=EnumImageSize.TinySize%>"/>
            </c:when>
            <c:otherwise>
              <img class="prod48"
                   src="${pageContext.request.contextPath}/images/ProductImages/ProductImagesThumb/${invoiceLineItem.comboInstance.combo.id}.jpg"
                   alt="${invoiceLineItem.comboInstance.combo.name}"/>
            </c:otherwise>
          </c:choose>
        </div>
        <div class='name'>
          <table width="70%">
            <tr>
              <td>
                  ${invoiceLineItem.comboInstance.combo.name}<br/>
                <c:forEach items="${invoiceLineItem.comboInstance.comboInstanceProductVariants}" var="comboVariant">
                      <span style="font-size:10px;">
                      ${comboVariant.qty} x
                      </span>
                      <span style="font-size:10px;">
                      ${comboVariant.productVariant.product.name} - ${comboVariant.productVariant.optionsCommaSeparated}
                      </span>
                  <br/>
                </c:forEach>
              </td>
              <td align="left">${hk:getComboCount(invoiceLineItem)}</td>
            </tr>
          </table>
        </div>

        <div class='price'>
          <div class="hk">
          </div>
          <div class="num"> Rs
            <span class="lineItemSubTotalMrp" style="font-weight:bold;"><fmt:formatNumber
                value="${invoiceLineItem.comboInstance.combo.hkPrice * hk:getComboCount(invoiceLineItem)}"
                pattern="<%=FormatUtils.currencyFormatPattern%>"/></span>
          </div>
        </div>
      </div>
      <div class='floatix'></div>
    </c:if>
  </c:if>
</c:forEach>
<c:if test="${pricingDto.grandTotalPayable > 0.00}">
  <div class='totals'>
    <div class='left'>
      <div class='shipping'>
        Shipping:
      </div>
      <c:if test="${pricingDto.redeemedRewardPoints > 0}">
        <div style="font-size:.8em" class="green">Reward Points</div>
      </c:if>
      <c:if test="${pricingDto.codLineCount > 0}">
        <div style="font-size:.8em" class="shipping">COD Charges</div>
      </c:if>
      <c:if
          test="${pricingDto.productsMrpSubTotal - pricingDto.productsHkSubTotal + pricingDto.prepaidServiceMrpSubTotal - pricingDto.prepaidServiceHkSubTotal + pricingDto.orderLevelDiscount + pricingDto.productsDiscount + pricingDto.prepaidServiceDiscount - pricingDto.totalPostpaidAmount + pricingDto.subscriptionDiscount > 0.00}">
        <div class='discount special'>
          You saved:
        </div>
      </c:if>
      <div class='total'>
        Total:
      </div>
      <c:if test="${pricingDto.totalCashback > 0.00}">
        <div class='special'>
          Cashback:
        </div>
      </c:if>
    </div>
    <div class='right'>
      <div class='shipping num'>
        Rs ${pricingDto.shippingTotal}
      </div>
      <c:if test="${pricingDto.redeemedRewardPoints > 0}">
        <div class="green">
          (<fmt:formatNumber value="${pricingDto.redeemedRewardPoints}" type="currency" currencySymbol=""/>)
        </div>
      </c:if>
      <c:if test="${pricingDto.codLineCount > 0}">
        Rs ${pricingDto.codSubTotal}
      </c:if>
      <c:if
          test="${pricingDto.productsMrpSubTotal - pricingDto.productsHkSubTotal + pricingDto.prepaidServiceMrpSubTotal - pricingDto.prepaidServiceHkSubTotal + pricingDto.orderLevelDiscount + pricingDto.productsDiscount + pricingDto.prepaidServiceDiscount - pricingDto.totalPostpaidAmount+pricingDto.subscriptionDiscount > 0.00}">
        <div class='discount num green special'>
          <span><fmt:formatNumber
              value="${pricingDto.productsMrpSubTotal - pricingDto.productsHkSubTotal + pricingDto.prepaidServiceMrpSubTotal - pricingDto.prepaidServiceHkSubTotal + pricingDto.orderLevelDiscount + pricingDto.productsDiscount + pricingDto.prepaidServiceDiscount - pricingDto.totalPostpaidAmount + pricingDto.subscriptionDiscount}"
              type="currency" currencySymbol="Rs. "/></span>
        </div>
      </c:if>
      <div class='total num'>
        <strong>
          <fmt:formatNumber value="${pricingDto.grandTotalPayable}" type="currency" currencySymbol="Rs. "/>
        </strong>
      </div>
      <c:if test="${pricingDto.totalCashback > 0.00}">
        <div class='num special'>
          <span><fmt:formatNumber value="${pricingDto.totalCashback}" type="currency" currencySymbol="Rs. "/></span>
        </div>
      </c:if>
    </div>
  </div>
</c:if>
<c:if test="${pricingDto.grandTotalPayable == 0.00}">
  <div class='totals'>
    <div class='left'>
      <div class='shipping'>
        Shipping:
      </div>
      <c:if test="${pricingDto.redeemedRewardPoints > 0}">
        <div style="font-size:.8em" class="green">Reward Points</div>
      </c:if>
      <c:if
          test="${pricingDto.productsMrpSubTotal - pricingDto.productsHkSubTotal + pricingDto.prepaidServiceMrpSubTotal - pricingDto.prepaidServiceHkSubTotal + pricingDto.orderLevelDiscount + pricingDto.productsDiscount + pricingDto.prepaidServiceDiscount - pricingDto.totalPostpaidAmount > 0.00}">
        <div class='discount special'>
          You saved:
        </div>
      </c:if>
      <c:if test="${pricingDto.totalCashback > 0.00}">
        <div class='discount special'>
          Cashback:
        </div>
      </c:if>
      <div class='total'>
        Total:
      </div>
    </div>
    <div class='right'>
      <div class='shipping num'>
        Rs ${pricingDto.shippingTotal}
      </div>
      <c:if test="${pricingDto.redeemedRewardPoints > 0}">
        <div class="green">
          (<fmt:formatNumber value="${pricingDto.redeemedRewardPoints}" type="currency" currencySymbol=""/>)
        </div>
      </c:if>
      <c:if
          test="${pricingDto.productsMrpSubTotal - pricingDto.productsHkSubTotal + pricingDto.prepaidServiceMrpSubTotal - pricingDto.prepaidServiceHkSubTotal + pricingDto.orderLevelDiscount + pricingDto.productsDiscount + pricingDto.prepaidServiceDiscount - pricingDto.totalPostpaidAmount > 0.00}">
        <div class='discount num green special'>
          <span id="summaryTotalDiscount"><fmt:formatNumber
              value="${pricingDto.productsMrpSubTotal - pricingDto.productsHkSubTotal + pricingDto.prepaidServiceMrpSubTotal - pricingDto.prepaidServiceHkSubTotal + pricingDto.orderLevelDiscount + pricingDto.productsDiscount + pricingDto.prepaidServiceDiscount - pricingDto.totalPostpaidAmount}"
              type="currency" currencySymbol="Rs. "/></span>
        </div>
      </c:if>
      <c:if test="${pricingDto.totalCashback > 0.00}">
        <div class='discount num green special'>
          <span><fmt:formatNumber value="${pricingDto.totalCashback}" type="currency" currencySymbol="Rs. "/></span>
        </div>
      </c:if>
      <div class='total num'>
        <strong>
          <fmt:formatNumber value="${pricingDto.grandTotalPayable}" type="currency" currencySymbol="Rs. "/>
        </strong>
      </div>
    </div>
  </div>
</c:if>

<c:if test="${pricingDto.postpaidServicesTotal > 0.00}">
  <c:forEach items="${pricingDto.productLineItems}" var="invoiceLineItem" varStatus="ctr1">
    <c:if test="${invoiceLineItem.productVariant.paymentType.name == 'Postpaid'}">
      <div class='product' style="border-bottom-style: solid;">
        <div class='img48' style="vertical-align:top;">
          <c:choose>
            <c:when test="${invoiceLineItem.productVariant.product.mainImageId != null}">
              <hk:productImage imageId="${invoiceLineItem.productVariant.product.mainImageId}"
                               size="<%=EnumImageSize.TinySize%>"/>
            </c:when>
            <c:otherwise>
              <img class="prod48"
                   src="${pageContext.request.contextPath}/images/ProductImages/ProductImagesThumb/${invoiceLineItem.productVariant.product.id}.jpg"
                   alt="${invoiceLineItem.productVariant.product.name}"/>
            </c:otherwise>
          </c:choose>
        </div>
        <div class='name'>
          <table width="70%">
            <tr>
              <td>
                  ${invoiceLineItem.productVariant.product.name} <br/>
                  ${invoiceLineItem.productVariant.variantName}
              </td>
              <td align="left">${invoiceLineItem.qty}</td>
            </tr>
          </table>
        </div>
        <div class='price'>
          <div class="hk">
          </div>
          <div class="num"> Rs
            <span class="lineItemSubTotalMrp" style="font-weight:bold;"><fmt:formatNumber
                value="${invoiceLineItem.hkPrice * invoiceLineItem.qty}"
                pattern="<%=FormatUtils.currencyFormatPattern%>"/></span>
          </div>
        </div>
        <div class='floatix'></div>
      </div>
    </c:if>
  </c:forEach>
  <div class='totals'>
    <div class='left'>
      <div class='discount special'>
        You saved:
      </div>
      <div class='total'>
        Total:
      </div>
    </div>
    <div class='right'>
      <div class='discount num green special'>
        <span id="summaryTotalDiscountPostpaid"><fmt:formatNumber
            value="${pricingDto.postpaidServiceMrpSubTotal - pricingDto.postpaidServiceHkSubTotal + pricingDto.postpaidServiceDiscount}"
            type="currency" currencySymbol="Rs. "/></span>
      </div>
      <div class='total num'>
        <strong>
          <fmt:formatNumber value="${pricingDto.postpaidServicesTotal}" type="currency" currencySymbol="Rs. "/>
        </strong>
      </div>
    </div>
  </div>
</c:if>
</div>
<%
  }
%>
</s:layout-definition>
