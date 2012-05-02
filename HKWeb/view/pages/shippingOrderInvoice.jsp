<%@ page import="com.hk.constants.payment.EnumPaymentMode" %>
<%@ page import="com.hk.constants.core.EnumRole" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<c:set var="paymentMode_COD" value="<%=EnumPaymentMode.COD.getId()%>"/>
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
      font-size: 0.813em;
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
<s:useActionBean beanclass="com.hk.web.action.SOInvoiceAction" event="pre" var="orderSummary"/>
<c:set var="b2bUser" value="<%=EnumRole.B2B_USER.getRoleName()%>"/>
<c:set var="baseOrder" value="${orderSummary.shippingOrder.baseOrder}"/>
<c:set var="address" value="${baseOrder.address}"/>

<div class="container_12" style="border: 1px solid; padding-top: 10px;">
<div class="grid_4">
  <div style="float: left;">
    <img src="${pageContext.request.contextPath}/barcodes/${orderSummary.shippingOrder.gatewayOrderId}.png"/>
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
      <c:when test="${orderSummary.shippingOrder.baseOrder.user.login == 'support@madeinhealth.com'}">
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
        Order#${orderSummary.shippingOrder.gatewayOrderId}</p>

      <p>placed on: <fmt:formatDate
          value="${baseOrder.payment.createDate}" type="both" timeStyle="short"/>
      </p>

      <c:if test="${baseOrder.payment.paymentMode.id == paymentMode_COD}">
        <h2>Cash on Delivery :
          <fmt:formatNumber value="${orderSummary.invoiceDto.grandTotal}" type="currency"
                            currencySymbol="Rs. " maxFractionDigits="0"/></h2>
      </c:if>
      <h3>
        Please do not accept if the box is tampered
      </h3>
    </div>
  </div>

  <div class="grid_4 alpha omega" style="width: 320px;">
    <div class="formatting" style="float: right;">
      <c:if test="${orderSummary.shippingOrder.baseOrder.user.login != 'support@madeinhealth.com'}">
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
       </c:if>
    </div>
  </div>
</div>

<div class="clear"></div>
<div style="margin-top: 5px;"></div>

<div class="grid_12">
  <div class="grid_8 alpha omega">
    <div class="formatting" style="float: left; font-size:1.1em;">
      <p><strong>Name & Address</strong></p>

      <p>${address.name}</p>

      <p>
        ${address.line1}
        <c:if test="${not empty address.line2}">
          , ${address.line2}
        </c:if>
      </p>

      <p>
        ${address.city} - ${address.pin}
        <c:if test="${orderSummary.routingCode != null && orderSummary.routingCode != ''}">
          <s:label name="routingCode"
                   style="font-size:1.2em; font-weight:bold;">&nbsp; &nbsp;${orderSummary.routingCode}</s:label>
        </c:if>
      </p>

      <p>${address.state}</p>

      <p>Ph: ${address.phone}</p>
    </div>
  </div>

  <div class="grid_4 alpha omega" style="width: 320px;">
    <div class="formatting" style="float: right;">
      <c:if
          test="${baseOrder.offerInstance != null && baseOrder.offerInstance.coupon != null && hk:isNotBlank(baseOrder.offerInstance.coupon.complimentaryCoupon)}">
        <div
            style="width: 300px; padding: 10px; font-size: .7em; outline: 1px dotted gray; font-family: sans-serif; margin-top: 30px;">
          <h4>You have won a Complementary Coupon!</h4>

          <p>${baseOrder.offerInstance.offer.complimentaryCouponDescription}</p>

          <p>Your Complementary Coupon Code :</p>


          <p><strong>${baseOrder.offerInstance.coupon.complimentaryCoupon}</strong></p>
        </div>
      </c:if>
    </div>
  </div>
</div>

<div class="clear"></div>
<div style="margin-top: 5px;"></div>

<div class="grid_12">
  <c:if test="${baseOrder.userComments != null}">
    <hr/>
    <p><strong>User Instructions:-</strong> ${baseOrder.userComments}</p>
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
    <c:forEach items="${orderSummary.invoiceDto.invoiceLineItemDtos}" var="invoiceLineItem">
      <%--<c:if test="${invoiceLineItem.lineItemStatus.id != orderStatus_actionAwaiting}">--%>
      <tr>
        <td>
          <c:choose>
            <c:when test="${orderSummary.printable}">
              <c:if
                  test="${hk:collectionContains(invoiceLineItem.productCategories, orderSummary.sexualCareCategory)}">
                <p>Personal Care Product</p>
              </c:if>
              <c:if
                  test="${!hk:collectionContains(invoiceLineItem.productCategories, orderSummary.sexualCareCategory)}">
                <p>${invoiceLineItem.productName}</p>
              </c:if>
            </c:when>
            <c:otherwise>
              <p>${invoiceLineItem.productName}</p>
            </c:otherwise>
          </c:choose>

          <c:if test="${invoiceLineItem.variantName != null}">
            <p>${invoiceLineItem.variantName}</p>
          </c:if>
          <em>
            <p>
              ${invoiceLineItem.productOptionsPipeSeparated}
              <%--<c:forEach items="${invoiceLineItem.productOptions}" var="productOption">--%>
                <%--${productOption.name} ${productOption.value}&nbsp--%>
              <%--</c:forEach>--%>
            </p>

            <p>
              ${invoiceLineItem.extraOptionsPipeSeparated}
              <%--<c:forEach items="${invoiceLineItem.cartLineItemExtraOptions}" var="extraOption">--%>
                <%--<label>${extraOption.name} : ${extraOption.value}</label>&nbsp--%>
              <%--</c:forEach>--%>

               ${invoiceLineItem.configOptionsPipeSeparated}
              <%--<c:if test="${not empty invoiceLineItem.cartLineItemConfigValues}">--%>

                <%--<c:set var="TH" value="TH"/>--%>
                <%--<c:set var="THBF" value="THBF"/>--%>
                <%--<c:set var="CO" value="CO"/>--%>
                <%--<c:set var="COBF" value="COBF"/>--%>
                <%--<c:forEach items="${invoiceLineItem.cartLineItemConfigValues}" var="configValue"--%>
                           <%--varStatus="configCtr">--%>
                  <%--<c:set var="variantConfigOption" value="${configValue.variantConfigOption}"/>--%>
                  <%--<c:set var="additionalParam" value="${variantConfigOption.additionalParam}"/>--%>
                  <%--${variantConfigOption.displayName} : ${configValue.value}--%>
                  <%--<c:if--%>
                      <%--test="${(additionalParam ne TH) or (additionalParam ne THBF) or (additionalParam ne CO) or (additionalParam ne COBF) }">--%>
                    <%--<c:if--%>
                        <%--test="${fn:startsWith(variantConfigOption.name, 'R')==true}">--%>
                      <%--(R)--%>
                    <%--</c:if>--%>
                    <%--<c:if--%>
                        <%--test="${fn:startsWith(variantConfigOption.name, 'L')==true}">--%>
                      <%--(L)--%>
                    <%--</c:if>--%>
                  <%--</c:if>--%>
                  <%--${!configCtr.last?',':''}--%>

                <%--</c:forEach>--%>
              <%--</c:if>--%>


                <%--<c:forEach items="${invoiceLineItem.cartLineItemConfigValues}" var="configValue">--%>
                <%--<c:set var="variantConfigOption" value="${configValue.variantConfigOption}"/>--%>
                <%--<label>${variantConfigOption.displayName} : ${configValue.value}</label>&nbsp--%>
                <%--</c:forEach>--%>


            </p>
          </em>
        </td>

        <td><fmt:formatNumber value="${invoiceLineItem.qty}" maxFractionDigits="0"/></td>
        <td> ${invoiceLineItem.hkPrice} </td>
        <td class="itemsubTotal">
          <fmt:formatNumber value="${invoiceLineItem.lineItemTotal}" type="currency"
                            currencySymbol="Rs. "/></td>

      </tr>
      <%-- </c:if>--%>
    </c:forEach>

  </table>


  <h3>Order Summary</h3>
  <table cellspacing="0">
    <tr>
      <td>Item Total</td>

      <td class="itemTotal">
        <fmt:formatNumber value="${orderSummary.invoiceDto.itemsTotal}" type="currency"
                          currencySymbol="Rs. "/>
      </td>
    </tr>
    <tr>
      <td>Shipping</td>
      <td>
        <fmt:formatNumber
            value="${orderSummary.invoiceDto.shipping}"
            type="currency" currencySymbol="Rs. "/>
      </td>
    </tr>
    <tr>
      <td>Discount ${baseOrder.offerInstance.coupon.code}</td>
      <td>
        <fmt:formatNumber
            value="${orderSummary.invoiceDto.totalDiscount}"
            type="currency" currencySymbol="Rs. "/>
      </td>
    </tr>
    <c:if test="${orderSummary.invoiceDto.rewardPoints > 0}">
      <tr>
        <td>Redeemed Rewards Point</td>
        <td>
          <fmt:formatNumber value="${orderSummary.invoiceDto.rewardPoints}"
                            type="currency" currencySymbol="Rs. "/></td>
      </tr>
    </c:if>
    <c:if test="${orderSummary.invoiceDto.cod > 0.0}">
      <tr>
        <td>COD charges</td>
        <td>
          <fmt:formatNumber value="${orderSummary.invoiceDto.cod}" type="currency"
                            currencySymbol="Rs. "/></td>
      </tr>
    </c:if>
    <tr>
      <td><strong>Grand Total</strong></td>
      <td>
        <strong><fmt:formatNumber value="${orderSummary.invoiceDto.grandTotal}" type="currency"
                                  currencySymbol="Rs. "/> </strong>
      </td>
    </tr>
  </table>
</div>

<div class="clear"></div>
<div style="margin-top: 5px;"></div>

<div class="grid_12">
  <hr/>
  <c:set var="warehouse" value="${orderSummary.shippingOrder.warehouse}"/>
  <c:choose>
    <c:when test="${public String _elExpression13306()
		throws java.io.IOException, javax.servlet.ServletException {
javax.servlet.jsp.PageContext pageContext = null;
java.util.Map param = null;
java.util.Map paramValues = null;
java.util.Map header = null;
java.util.Map headerValues = null;
java.util.Map cookie = null;
java.util.Map initParam = null;
java.util.Map pageScope = null;
java.util.Map requestScope = null;
java.util.Map sessionScope = null;
java.util.Map applicationScope = null;
return ""+hk:collectionContains(baseOrder.user.roleStrings, b2bUser;
})}">
      <p style="font-size: .8em;">Bright Lifecare Pvt. Ltd. | Khasra No. 146/25/2/1, Jail Road, Dhumaspur, Badshahpur |
        Gurgaon, Haryana- 122101 | TIN:
        06101832036 </p>
    </c:when>
    <c:otherwise>
      <p style="font-size: .8em;">Aquamarine Healthcare Pvt. Ltd. | ${warehouse.line1}, ${warehouse.line2} |
          ${warehouse.city}, ${warehouse.state}- ${warehouse.pincode} | TIN:
          ${warehouse.tin} </p>
    </c:otherwise>
  </c:choose>

</div>
<div class="clear"></div>
<div style="margin-top: 5px;"></div>
</div>
</body>
</html>
