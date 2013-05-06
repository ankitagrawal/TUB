<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page import="com.hk.dto.pricing.PricingDto" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Set, java.util.HashSet" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%--
Pass an attribute called pricingDto to render a table with pricing details
--%>
<%--<s:useActionBean beanclass="com.hk.web.action.core.cart.CartAction" var="cartAction"/>--%>
<s:useActionBean beanclass="com.hk.web.action.core.order.OrderSummaryAction" var="orderSummary" />
<s:layout-definition>
<%
  PricingDto pricingDto = (PricingDto) pageContext.getAttribute("pricingDto");
  Date orderDate = (Date) pageContext.getAttribute("orderDate");
  // this line does not mean anything, but simply gets idea to understand pricingDto's type, autocompletion works now.
  pageContext.setAttribute("pricingDto", pricingDto);
  pageContext.setAttribute("orderDate", orderDate);
  if (pricingDto != null && orderDate != null) {
%>

<div class="topFullContainer">
<div class='orderSummaryHeading'>
    <div class="productGrid">Product</div>
    <div class="prodQuantityGrid">Qty</div>
    <div class='name' style="width: 20%;left: 19px;position: relative;float: left;margin-top: 5px;">
        <span class="dispatchDateText2">Dispatch Days</span>
        <span id="dispatchDateQuesMark" class="dispatchDateQuesMark">?</span>
        <div class="popUpDDate" id="popUpDDate">The dispatch date is when the product will be shipped from our warehouse. The delivery time would be extra and will vary according to your location.
            <span id="learnMore" class="learnMore">learn more</span>
            <span id="crossNew" style="position: relative;float: right;top: 12px;cursor: pointer;">X</span>
            <span class="arrowNew"></span>
        </div>
    </div>
    <div class="prodPriceGrid">Price</div>
</div>
<div class='products_container' style="overflow: visible;min-height: 0px;clear: both;">
<c:forEach items="${pricingDto.productLineItems}" var="invoiceLineItem" varStatus="ctr1">
  <c:if
      test="${invoiceLineItem.comboInstance == null && invoiceLineItem.productVariant.paymentType.name != 'Postpaid'}">
    <div class='product newProductContainer' style="border-bottom-style: solid;height: auto;">
      <div class='img48' style="vertical-align:top;position: relative;float: left;">
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
      <div class='name' style="position: relative;float: left;width: 37%;">
        <table width="100%">
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
        </table>
      </div>

         <%--HTML code for dispatch date--%>
        <div class="dispatchedDateNew2">
            <div>${invoiceLineItem.productVariant.product.minDays} - ${invoiceLineItem.productVariant.product.maxDays} working days</div>
        </div>


      <div class='price' style="position: relative;margin-left: 0px;width: 32%;">
        <div class="hk">
        </div>
        <div style="left: 70px;position: relative;font-size: 15px;"> Rs
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
    <c:if test="${!fn:contains(firstComboLineItem,invoiceLineItem.comboInstance.id)}">
      <%--<c:set var="firstComboLineItem" value="${invoiceLineItem.comboInstance.combo}"/>--%>
        <c:set var="firstComboLineItem" value="${firstComboLineItem} + ',' + ${invoiceLineItem.comboInstance.id} + ','" />
        <div class='product newProductContainer' style="border-bottom-style: solid;height: auto;">
        <div class='img48' style="vertical-align:top;position: relative;float: left;">
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
        <div class='name' style="position: relative;float: left;width: 37%;">
          <table width="100%">
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

        <div class="dispatchedDateNew2"><div>${invoiceLineItem.productVariant.product.minDays} - ${invoiceLineItem.productVariant.product.maxDays} working days</div></div>
        <div class='price' style="position: relative;margin-left: 0px;width: 32%;">
          <div class="hk">
          </div>
          <div class="num" style="left: 70px;position: relative;"> Rs
            <span class="lineItemSubTotalMrp" style="font-weight:bold;"><fmt:formatNumber
                value="${invoiceLineItem.comboInstance.combo.hkPrice * hk:getComboCount(invoiceLineItem)}"
                pattern="<%=FormatUtils.currencyFormatPattern%>"/></span>
          </div>
        </div>
            <div class='floatix'></div>
      </div>

    </c:if>
  </c:if>
</c:forEach>
</div>
<c:if test="${pricingDto.grandTotalPayable > 0.00}">
  <div class='totals newTotals'>
    <div class='left' style="width: 42%;left: 0px;">
      <div class='shipping' style="font-size: 12px;font-weight: normal;">
        Shipping:
      </div>
      <c:if test="${pricingDto.redeemedRewardPoints > 0}">
        <div style="font-size: 12px;font-weight: normal;color: rgb(68, 68, 68);" class="green">Reward Points</div>
      </c:if>
      <c:if test="${pricingDto.codLineCount > 0}">
        <div style="font-size: 12px;font-weight: normal;" class="shipping">COD Charges</div>
      </c:if>
      <c:if
          test="${pricingDto.productsMrpSubTotal - pricingDto.productsHkSubTotal + pricingDto.prepaidServiceMrpSubTotal - pricingDto.prepaidServiceHkSubTotal + pricingDto.orderLevelDiscount + pricingDto.productsDiscount + pricingDto.prepaidServiceDiscount - pricingDto.totalPostpaidAmount + pricingDto.subscriptionDiscount > 0.00}">
        <div class='discount' style="font-size: 12px;font-weight: normal;color: rgb(68, 68, 68);">
          You saved:
        </div>
      </c:if>
      <div class='total' style="font-size: 12px;font-weight: normal;color: rgb(68, 68, 68);">
        Total:
      </div>
      <c:if test="${pricingDto.totalCashback > 0.00}">
        <div class='special' style="font-size: 12px;font-weight: normal;color: rgb(68, 68, 68);">
          Cashback:
        </div>
      </c:if>
    </div>
    <div class='right' style="width: 50%;left: 0px;text-align: right;font-size: 12px;">
      <div class='shipping num' style="font-size: 12px;color: rgb(68, 68, 68);font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;">
        Rs ${pricingDto.shippingTotal}
      </div>
      <c:if test="${pricingDto.redeemedRewardPoints > 0}">
        <div class="green" style="font-size: 12px;color: rgb(68, 68, 68);">
          (<fmt:formatNumber value="${pricingDto.redeemedRewardPoints}" type="currency" currencySymbol=""/>)
        </div>
      </c:if>
      <c:if test="${pricingDto.codLineCount > 0}">
        Rs ${pricingDto.codSubTotal}
      </c:if>
      <c:if
          test="${pricingDto.productsMrpSubTotal - pricingDto.productsHkSubTotal + pricingDto.prepaidServiceMrpSubTotal - pricingDto.prepaidServiceHkSubTotal + pricingDto.orderLevelDiscount + pricingDto.productsDiscount + pricingDto.prepaidServiceDiscount - pricingDto.totalPostpaidAmount+pricingDto.subscriptionDiscount > 0.00}">
        <div class='discount num green special'>
          <span style="font-size: 12px;font-weight: bold;color: rgb(68, 68, 68 );font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;"><fmt:formatNumber
              value="${pricingDto.productsMrpSubTotal - pricingDto.productsHkSubTotal + pricingDto.prepaidServiceMrpSubTotal - pricingDto.prepaidServiceHkSubTotal + pricingDto.orderLevelDiscount + pricingDto.productsDiscount + pricingDto.prepaidServiceDiscount - pricingDto.totalPostpaidAmount + pricingDto.subscriptionDiscount}"
              type="currency" currencySymbol="Rs. "/></span>
        </div>
      </c:if>
      <div class='total num' style="font-size: 14px;">
        <strong style="font-size: 12px;color: rgb(68,68,68);font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;">
          <fmt:formatNumber value="${pricingDto.grandTotalPayable}" type="currency" currencySymbol="Rs. "/>
        </strong>
      </div>
      <c:if test="${pricingDto.totalCashback > 0.00}">
        <div class='num special' style="font-size: 12px;font-weight: normal;color: rgb(68, 68, 68);">
          <span><fmt:formatNumber value="${pricingDto.totalCashback}" type="currency" currencySymbol="Rs. "/></span>
        </div>
      </c:if>
    </div>
  </div>
</c:if>
<c:if test="${pricingDto.grandTotalPayable == 0.00}">
  <div class='totals newTotals'>
    <div class='left' style="width: 42%;left: 0px;">
      <div class='shipping' style="font-size: 12px;font-weight: normal;color: rgb(68, 68, 68);">
        Shipping:
      </div>
      <c:if test="${pricingDto.redeemedRewardPoints > 0}">
        <div style="font-size: 12px;font-weight: normal;color: rgb(68, 68, 68);" class="green">Reward Points</div>
      </c:if>
      <c:if
          test="${pricingDto.productsMrpSubTotal - pricingDto.productsHkSubTotal + pricingDto.prepaidServiceMrpSubTotal - pricingDto.prepaidServiceHkSubTotal + pricingDto.orderLevelDiscount + pricingDto.productsDiscount + pricingDto.prepaidServiceDiscount - pricingDto.totalPostpaidAmount > 0.00}">
        <div class='discount' style="font-size: 12px;font-weight: normal;color: rgb(68, 68, 68);">
          You saved:
        </div>
      </c:if>
      <c:if test="${pricingDto.totalCashback > 0.00}">
        <div class='discount' style="font-size: 12px;font-weight: normal;color: rgb(68, 68, 68);">
          Cashback:
        </div>
      </c:if>
      <div class='total' style="font-size: 12px;font-weight: normal;color: rgb(68, 68, 68);">
        Total:
      </div>
    </div>
    <div class='right' style="width: 50%;left: 0px;text-align: right;">
      <div class='shipping num' style="font-size: 12px;font-weight: normal;color: rgb(68, 68, 68);">
        Rs ${pricingDto.shippingTotal}
      </div>
      <c:if test="${pricingDto.redeemedRewardPoints > 0}">
        <div class="green" style="font-size: 12px;font-weight: normal;color: rgb(68, 68, 68);">
          (<fmt:formatNumber value="${pricingDto.redeemedRewardPoints}" type="currency" currencySymbol=""/>)
        </div>
      </c:if>
      <c:if
          test="${pricingDto.productsMrpSubTotal - pricingDto.productsHkSubTotal + pricingDto.prepaidServiceMrpSubTotal - pricingDto.prepaidServiceHkSubTotal + pricingDto.orderLevelDiscount + pricingDto.productsDiscount + pricingDto.prepaidServiceDiscount - pricingDto.totalPostpaidAmount > 0.00}">
        <div class='discount num green special' style="font-size: 12px;font-weight: normal;color: rgb(68, 68, 68);">
          <span id="summaryTotalDiscount"><fmt:formatNumber
              value="${pricingDto.productsMrpSubTotal - pricingDto.productsHkSubTotal + pricingDto.prepaidServiceMrpSubTotal - pricingDto.prepaidServiceHkSubTotal + pricingDto.orderLevelDiscount + pricingDto.productsDiscount + pricingDto.prepaidServiceDiscount - pricingDto.totalPostpaidAmount}"
              type="currency" currencySymbol="Rs. "/></span>
        </div>
      </c:if>
      <c:if test="${pricingDto.totalCashback > 0.00}">
        <div class='discount num green special' style="font-size: 12px;font-weight: normal;color: rgb(68, 68, 68);">
          <span><fmt:formatNumber value="${pricingDto.totalCashback}" type="currency" currencySymbol="Rs. "/></span>
        </div>
      </c:if>
      <div class='total num' style="font-size: 12px;font-weight: normal;color: rgb(68, 68, 68);">
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
      <div class='product newProductContainer' style="border-bottom-style: solid;height: auto;">
        <div class='img48' style="vertical-align:top;position: relative;float: left;">
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
        <div class='name' style="position: relative;float: left;width: 37%;">
          <table width="100%">
            <tr>
              <td>
                  ${invoiceLineItem.productVariant.product.name} <br/>
                  ${invoiceLineItem.productVariant.variantName}
              </td>
              <td align="left">${invoiceLineItem.qty}</td>
            </tr>
          </table>
        </div>
        <div class="dispatchedDateNew2"><div>${invoiceLineItem.productVariant.product.minDays} - ${invoiceLineItem.productVariant.product.maxDays} working days</div></div>
        <div class='price' style="position: relative;margin-left: 0px;width: 32%">
          <div class="hk">
          </div>
          <div class="num" style="left: 70px;position: relative;"> Rs
            <span class="lineItemSubTotalMrp" style="font-weight:bold;"><fmt:formatNumber
                value="${invoiceLineItem.hkPrice * invoiceLineItem.qty}"
                pattern="<%=FormatUtils.currencyFormatPattern%>"/></span>
          </div>
        </div>
        <div class='floatix'></div>
      </div>
    </c:if>
  </c:forEach>
  <div class='totals newTotals'>
    <div class='left' style="width: 42%;left: 0px;">
      <div class='discount' style="font-size: 12px;font-weight: normal;color: rgb(68, 68, 68);">
        You saved:
      </div>
      <div class='total' style="font-size: 12px;font-weight: normal;color: rgb(68, 68, 68);">
        Total:
      </div>
    </div>
    <div class='right' style="width: 50%;left: 0px;text-align: right;">
      <div class='discount num green special' style="font-size: 12px;font-weight: normal;color: rgb(68, 68, 68);">
        <span id="summaryTotalDiscountPostpaid"><fmt:formatNumber
            value="${pricingDto.postpaidServiceMrpSubTotal - pricingDto.postpaidServiceHkSubTotal + pricingDto.postpaidServiceDiscount}"
            type="currency" currencySymbol="Rs. "/></span>
      </div>
      <div class='total num' style="font-size: 12px;font-weight: normal;color: rgb(68, 68, 68);">
        <strong>
          <fmt:formatNumber value="${pricingDto.postpaidServicesTotal}" type="currency" currencySymbol="Rs. "/>
        </strong>
      </div>
    </div>
  </div>
</c:if>
</div>

<div class='orderSummaryHeading' style="margin-bottom: 50px;margin-top: 75px;">
    <div class="deliveryDetails"> DELIVERY DETAILS</div>
    <ul>
        <li>
            - The time taken for delivery after dispatch from our warehouse varies with location.
        </li>
        <li>
            - For Metroes: 1-3 business days
        </li>
        <li>
            - For Major Cities: 2-4 business days
        </li>
        <li>
            - For Other Town/Cities: 3-6 business days
        </li>
        <li>
            - For Rest of India Non Serviceable through Couriers: 7-15 business days (Delivery done by Indian Post)
        </li>
    </ul>
</div>
<%
  }
%>
</s:layout-definition>
