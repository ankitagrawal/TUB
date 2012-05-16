<%@ page import="com.hk.constants.core.EnumRole" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
  <title>Order Invoice</title>
  <style type="text/css">
    table {
      width: 100%;
      border-width: 0 0 1px 1px;
      border-style: solid;
      border-collapse: separate;
    }

    table tr td {
      text-align: left;
      font-size: small;
      border-width: 1px 1px 0 0;
      border-style: solid;
    }

    table tr th {
      border-width: 1px 1px 0 0;
      border-style: solid;
      text-align: left;
    }

    h2 {
      margin: 0;
      padding: 0;
    }

    h1 {
      margin: 0;
      padding: 0;
    }

    p {
      margin-top: 2px;
      margin-bottom: 2px;
    }

    .clear {
      clear: both;
      display: block;
      overflow: hidden;
      visibility: hidden;
      width: 0;
      height: 0
    }
  </style>
  <link href="<hk:vhostCss/>/css/960.css" rel="stylesheet" type="text/css"/>
</head>
<body>
<c:set var="b2bUser" value="<%=EnumRole.B2B_USER.getRoleName()%>"/>
<s:useActionBean beanclass="com.hk.web.action.core.accounting.BOInvoiceAction" event="pre" var="orderSummary"/>

<div class="container_12" style="border: 1px solid; padding-top: 10px;">
<div class="grid_4">
  <div style="float: left;">
    <img src="${pageContext.request.contextPath}/barcodes/${orderSummary.order.gatewayOrderId}.png"/>
  </div>
</div>
<div class="grid_4">
  <div style="text-align: center;">
    ORDER INVOICE
  </div>
</div>
<div class="grid_4">
  <div style="float: right;">
   <c:choose>
      <c:when test="${orderSummary.order.user.login == 'support@madeinhealth.com' || orderSummary.order.store.id == 2}">
        <img src="${pageContext.request.contextPath}/images/mih-logo.jpg" alt="MadeInHealth Logo"/>
      </c:when>
      <c:otherwise>
        <img src="${pageContext.request.contextPath}/images/logo.png" alt="HealthKart Logo"/>
      </c:otherwise>
    </c:choose>
  </div>
</div>

<div class="clear"></div>
<div style="margin-top: 5px;"></div>

<div class="grid_12">
  <div class="grid_8 alpha omega">
    <div class="formatting" style="margin-top: 30px;">
      <p style="margin-bottom: 2px;">Proforma Invoice for
        Order#${orderSummary.order.gatewayOrderId}</p>

      <p>placed on: <fmt:formatDate
          value="${orderSummary.order.payment.createDate}" type="both" timeStyle="short"/>
      </p>
      <c:if test="${orderSummary.pricingDto.codLineCount > 0}">
        <h2>Cash on Delivery :
          <fmt:formatNumber value="${orderSummary.pricingDto.grandTotalPayable}" type="currency" currencySymbol="Rs. "
                            maxFractionDigits="0"/></h2>
      </c:if>
      <h3>
        Please do not accept if the box is tampered
      </h3>
    </div>
  </div>
  <div class="grid_4 alpha omega" style="width: 320px;">
    <div class="formatting" style="float: right;">
      <div
          style="float:right; width: 300px; padding: 10px; font-size: .7em; outline: 1px dotted gray; font-family: sans-serif;">
        <p style="margin-bottom: 4px;">Introducing the <strong>Refer and Earn</strong> program</p>

        <p>Your referral coupon code is</p>

        <p><strong
            style="text-transform:uppercase; font-size: 1.2em;">${orderSummary.coupon.code}</strong></p>

        <p><strong>How it works: </strong></p>

        <p>
          Pass this coupon code to your friends and family. They get a <strong>Rs. 100 discount on their first
          purchase*</strong> at healthkart.com and you
          <strong>get reward points worth Rs. 100</strong> in your account for your referral*.
        </p>
      </div>
    </div>
  </div>
</div>

<div class="clear"></div>
<div style="margin-top: 5px;"></div>

<div class="grid_12">
  <div class="grid_8 alpha omega">
    <div class="formatting" style="float: left; font-size:1.1em;">
      <p><strong>Name & Address</strong></p>

      <p>${orderSummary.order.address.name}</p>

      <p>
        ${orderSummary.order.address.line1}
        <c:if test="${not empty orderSummary.order.address.line2}">
          , ${orderSummary.order.address.line2}
        </c:if>
      </p>

      <p>${orderSummary.order.address.city} - ${orderSummary.order.address.pin}</p>

      <p>${orderSummary.order.address.state}</p>

      <p>Ph: ${orderSummary.order.address.phone}</p>
    </div>
  </div>

  <div class="grid_4 alpha omega" style="width: 320px;">
    <div class="formatting" style="float: right;">
      <c:if
          test="${orderSummary.order.offerInstance != null && orderSummary.order.offerInstance.coupon != null && hk:isNotBlank(orderSummary.order.offerInstance.coupon.complimentaryCoupon)}">
        <div
            style="width: 300px; padding: 10px; font-size: .7em; outline: 1px dotted gray; font-family: sans-serif; margin-top: 30px;">
          <h4>You have won a Complementary Coupon!</h4>

          <p>${orderSummary.order.offerInstance.offer.complimentaryCouponDescription}</p>

          <p>Your Complementary Coupon Code :</p>

          <p><strong>${orderSummary.order.offerInstance.coupon.complimentaryCoupon}</strong></p>
        </div>
      </c:if>
    </div>
  </div>
</div>

<div class="clear"></div>
<div style="margin-top: 5px;"></div>

<div class="grid_12">
  <c:if test="${orderSummary.order.userComments != null}">
    <hr/>
    <p><strong>User Instructions:-</strong> ${orderSummary.order.userComments}</p>
    <hr/>
  </c:if>
</div>

<div class="clear"></div>
<div style="margin-top: 5px;"></div>

<div class="grid_12">
  <h3>Order Details</h3>

  <table cellspacing="0">
    <tr>
      <th>Item</th>
      <th>Quantity</th>
      <th>Unit price</th>
      <th>Total(Rs.)</th>
    </tr>

    <c:forEach items="${orderSummary.pricingDto.productLineItems}" var="productLineItem">
      <c:if test="${productLineItem.comboInstance == null}">
        <tr>
          <td>
            <p>${productLineItem.productVariant.product.name}</p>

            <p>${productLineItem.productVariant.variantName}</p>
            <em>
              <p>
                <c:forEach items="${productLineItem.productVariant.productOptions}" var="productOption">
                  ${productOption.name} ${productOption.value}&nbsp
                </c:forEach>
              </p>

              <p>
                <c:forEach items="${productLineItem.cartLineItemExtraOptions}" var="extraOption">
                  <label>${extraOption.name} : ${extraOption.value}</label>&nbsp
                </c:forEach>

                <c:if test="${not empty productLineItem.cartLineItemConfig.cartLineItemConfigValues}">

                  <c:set var="TH" value="TH"/>
                  <c:set var="THBF" value="THBF"/>
                  <c:set var="CO" value="CO"/>
                  <c:set var="COBF" value="COBF"/>
                  <c:forEach items="${productLineItem.cartLineItemConfig.cartLineItemConfigValues}" var="configValue"
                             varStatus="configCtr">
                    <c:set var="variantConfigOption" value="${configValue.variantConfigOption}"/>
                    <c:set var="additionalParam" value="${variantConfigOption.additionalParam}"/>
                    ${variantConfigOption.displayName} : ${configValue.value}
                    <c:if
                        test="${(additionalParam ne TH) or (additionalParam ne THBF) or (additionalParam ne CO) or (additionalParam ne COBF) }">
                      <c:if
                          test="${fn:startsWith(variantConfigOption.name, 'R')==true}">
                        (R)
                      </c:if>
                      <c:if
                          test="${fn:startsWith(variantConfigOption.name, 'L')==true}">
                        (L)
                      </c:if>
                    </c:if>
                    ${!configCtr.last?',':''}

                  </c:forEach>
                </c:if>
              </p>
                <%--<p>
                  <c:forEach items="${productLineItem.extraOptions}" var="extraOption">
                    <label>${extraOption.name} : ${extraOption.value}</label>&nbsp
                  </c:forEach>
                  <c:forEach items="${productLineItem.lineItemConfig.lineItemConfigValues}" var="configValue">
                    <c:set var="variantConfigOption" value="${configValue.variantConfigOption}"/>
                    <label>${variantConfigOption.displayName} : ${configValue.value}</label>&nbsp
                  </c:forEach>
                </p>--%>
            </em>
          </td>
          <td><fmt:formatNumber value="${productLineItem.qty}" maxFractionDigits="0"/></td>
          <td> ${productLineItem.hkPrice} </td>
          <td><fmt:formatNumber value="${productLineItem.hkPrice * productLineItem.qty}" type="currency"
                                currencySymbol="Rs. "/></td>
        </tr>
      </c:if>
    </c:forEach>
    <c:set var="firstComboLineItem" value=""/>
    <c:forEach items="${orderSummary.pricingDto.productLineItems}" var="productLineItem">
      <c:if test="${productLineItem.comboInstance != null}">
        <c:if test="${firstComboLineItem != productLineItem.comboInstance.combo}">
          <c:set var="firstComboLineItem" value="${productLineItem.comboInstance.combo}"/>
          <tr>
            <td>
              <p>${productLineItem.comboInstance.combo.name}</p>
              <c:forEach items="${productLineItem.comboInstance.comboInstanceProductVariants}" var="comboVariant">
                <p>${comboVariant.qty}x${comboVariant.productVariant.product.name}</p>

                <p>${productLineItem.productVariant.variantName}</p>
                <em>
                  <c:forEach items="${comboVariant.productVariant.productOptions}" var="productOption">
                    ${productOption.name} ${productOption.value}&nbsp;
                  </c:forEach>
                </em>
              </c:forEach>
            </td>
            <td>
              <fmt:formatNumber value="${hk:getComboCount(productLineItem)}" maxFractionDigits="0"/></td>
            <td> ${productLineItem.comboInstance.combo.hkPrice} </td>
            <td>
              <fmt:formatNumber
                  value="${productLineItem.comboInstance.combo.hkPrice * hk:getComboCount(productLineItem)}"
                  type="currency" currencySymbol="Rs. "/></td>
          </tr>
        </c:if>
      </c:if>
    </c:forEach>

  </table>

  <h3>Order Summary</h3>
  <table cellspacing="0">
    <tr>
      <td>Item Total</td>
      <td>
        <fmt:formatNumber value="${orderSummary.pricingDto.payableHkSubTotal}" type="currency" currencySymbol="Rs. "/>
      </td>
    </tr>
    <tr>
      <td>Shipping</td>
      <td>
        <fmt:formatNumber value="${orderSummary.pricingDto.shippingSubTotal - orderSummary.pricingDto.shippingDiscount}"
                          type="currency" currencySymbol="Rs. "/>
      </td>
    </tr>
    <tr>
      <td>Discount ${orderSummary.order.offerInstance.coupon.code}</td>
      <td>
        <fmt:formatNumber value="${orderSummary.pricingDto.totalDiscount - orderSummary.pricingDto.shippingDiscount}"
                          type="currency" currencySymbol="Rs. "/>
      </td>
    </tr>
    <c:if test="${orderSummary.pricingDto.redeemedRewardPoints > 0}">
      <tr>
        <td>Redeemed Rewards Point</td>
        <td>
          <fmt:formatNumber value="${orderSummary.pricingDto.redeemedRewardPoints}" type="currency"
                            currencySymbol="Rs. "/></td>
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
        <strong><fmt:formatNumber value="${orderSummary.pricingDto.grandTotalPayable}" type="currency"
                                  currencySymbol="Rs. "/> </strong>
      </td>
    </tr>
  </table>
</div>

<div class="clear"></div>
<div style="margin-top: 5px;"></div>

<div class="grid_12">
  <div style="font-size:.8em">Note: This is to certify that items inside do not contain any prohibited or hazardous
    material.
  </div>
  <hr/>
  <c:choose>
    <c:when test="${hk:collectionContains(orderSummary.order.user.roleStrings, b2bUser)}">
      <p style="font-size: .8em;">Bright Lifecare Pvt. Ltd. | Khasra No. 146/25/2/1, Jail Road, Dhumaspur, Badshahpur |
        Gurgaon, Haryana- 122101 | TIN:
        06101832036 </p>
    </c:when>
    <c:otherwise>
      <p style="font-size: .8em;">Aquamarine Healthcare Pvt. Ltd. | Khasra No. 146/25/2/1, Jail Road, Dhumaspur,
        Badshahpur |
        Gurgaon, Haryana- 122101 | TIN:
        06101832327 </p>
    </c:otherwise>
  </c:choose>
</div>

<div class="clear"></div>
<div style="margin-top: 5px;"></div>

</div>

</body>
</html>