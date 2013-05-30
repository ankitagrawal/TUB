<%@ page import="com.hk.constants.core.EnumRole" %>
<%@ page import="com.hk.constants.catalog.category.CategoryConstants" %>
<%@ page import="com.hk.domain.catalog.product.VariantConfigOptionParam" %>
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
<c:set var="eyeCat" value="<%=CategoryConstants.eyeGlasses.getName()%>"/>
<c:set var="b2bUser" value="<%=EnumRole.B2B_USER.getRoleName()%>"/>
<c:set var="TH" value="<%=VariantConfigOptionParam.THICKNESS.param()%>"/>
<c:set var="THBF" value="<%=VariantConfigOptionParam.BFTHICKNESS.param()%>"/>
<c:set var="CO" value="<%=VariantConfigOptionParam.COATING.param()%>"/>
<c:set var="COBF" value="<%=VariantConfigOptionParam.BFCOATING.param()%>"/>
<c:set var="BRANDCO" value="<%=VariantConfigOptionParam.BRANDCO.param()%>"/>
<c:set var="BRANDTH" value="<%=VariantConfigOptionParam.BRANDTH.param()%>"/>
<c:set var="BRANDTHBF" value="<%=VariantConfigOptionParam.BRANDTHBF.param()%>"/>
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
        <c:if test="${!hk:collectionContains(orderSummary.order.user.roleStrings, b2bUser)}">
            <c:choose>
                <c:when test="${orderSummary.order.store.id == 2 || orderSummary.order.store.id == 3}">
                    <c:if test="${orderSummary.order.store.id == 2}">
                        <img src="${pageContext.request.contextPath}/images/mih-logo.jpg" alt="MadeInHealth Logo"/>
                    </c:if>
                    <c:if test="${orderSummary.order.store.id == 3}">
                        <img src="${pageContext.request.contextPath}/images/fitnesspro.png" alt="FitnessPro Logo"/>
                    </c:if>
                </c:when>
                <c:otherwise>
                    <img src="${pageContext.request.contextPath}/images/logo.png" alt="HealthKart Logo"/>
                </c:otherwise>
            </c:choose>
        </c:if>
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
            <c:if test="${orderSummary.order.COD}">
                <h2>Cash on Delivery :
                    <fmt:formatNumber value="${orderSummary.pricingDto.grandTotalPayable}" type="currency"
                                      currencySymbol="Rs. "
                                      maxFractionDigits="0"/></h2>
            </c:if>
            <h3>
                Please do not accept if the box is tampered
            </h3>
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

            <p>${orderSummary.order.address.city} - ${orderSummary.order.address.pincode.pincode}</p>

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
        <tr>
            <td>
                <p>${productLineItem.productVariant.product.name}</p>

                <p>${productLineItem.productVariant.variantName}</p>
                <em>
                    <p>
                        <c:forEach items="${productLineItem.productVariant.productOptions}" var="productOption">
                            <c:if test="${hk:showOptionOnUI(productOption.name)}">
                                ${productOption.name}:${productOption.value};
                            </c:if>
                        </c:forEach>
                    </p>

                    <p>
                        <c:forEach items="${productLineItem.cartLineItemExtraOptions}" var="extraOption">
                        <label>${extraOption.name} : ${extraOption.value}</label>&nbsp
                        </c:forEach>

                        <c:if test="${not empty productLineItem.cartLineItemConfig.cartLineItemConfigValues}">

                        <c:forEach items="${productLineItem.cartLineItemConfig.cartLineItemConfigValues}"
                                   var="configValue" varStatus="configValueCtr">
                            <c:set var="additinalParam"
                                   value="${configValue.variantConfigOption.additionalParam}"/>
                        <c:if
                                test="${( additinalParam == TH || additinalParam == THBF
								|| additinalParam == CO || additinalParam == COBF || additinalParam == BRANDCO || additinalParam == BRANDTH 
								|| additinalParam == BRANDTHBF) }">
                            ${configValue.variantConfigOption.displayName}:${configValue.value}|

                        </c:if>
                        </c:forEach>
                    <table>
                        <tr>
                            <td><b>Right</b></td>
                            <c:forEach items="${productLineItem.cartLineItemConfig.cartLineItemConfigValues}"
                                       var="configValue" varStatus="configValueCtr">
                                <c:set var="additinalParam"
                                       value="${configValue.variantConfigOption.additionalParam}"/>
                                <c:set var="side" value="${configValue.variantConfigOption.name}"/>
                                <c:if
                                        test="${ fn:startsWith(side,'R' ) && !( additinalParam == TH || additinalParam == THBF
								|| additinalParam == CO || additinalParam == COBF || additinalParam == BRANDCO || additinalParam == BRANDTH 
								|| additinalParam == BRANDTHBF) }">
                                    <td><b>${configValue.variantConfigOption.displayName}:${configValue.value}</b>
                                    </td>
                                </c:if>
                            </c:forEach>
                        </tr>
                        <tr>
                            <td><b>Left</b></td>
                            <c:forEach items="${productLineItem.cartLineItemConfig.cartLineItemConfigValues}"
                                       var="configValue" varStatus="configValueCtr">
                                <c:set var="additinalParam"
                                       value="${configValue.variantConfigOption.additionalParam}"/>
                                <c:set var="side" value="${configValue.variantConfigOption.name}"/>
                                <c:if
                                        test="${fn:startsWith(side,'L' ) && !( additinalParam == TH || additinalParam == THBF
								|| additinalParam == CO || additinalParam == COBF || additinalParam == BRANDCO || additinalParam == BRANDTH 
								|| additinalParam == BRANDTHBF)}">
                                    <td><b>${configValue.variantConfigOption.displayName}:${configValue.value}</b>
                                    </td>
                                </c:if>
                            </c:forEach>
                        </tr>
                    </table>
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
    </c:forEach>
    <c:set var="firstComboLineItem" value=""/>
    <c:forEach items="${orderSummary.pricingDto.productLineItems}" var="productLineItem">
        <%--<c:if test="${productLineItem.comboInstance != null}">--%>
        <%--<c:if test="${firstComboLineItem != productLineItem.comboInstance.combo}">--%>
        <%--<c:set var="firstComboLineItem" value="${productLineItem.comboInstance.combo}"/>--%>
        <%--<tr>--%>
        <%--<td>--%>
        <%--<p>${productLineItem.comboInstance.combo.name}</p>--%>
        <%--<c:forEach items="${productLineItem.comboInstance.comboInstanceProductVariants}" var="comboVariant">--%>
        <%--<p>${comboVariant.qty}x${comboVariant.productVariant.product.name}</p>--%>

        <%--<p>${productLineItem.productVariant.variantName}</p>--%>
        <%--<em>--%>
        <%--<c:forEach items="${comboVariant.productVariant.productOptions}" var="productOption">--%>
        <%--${productOption.name} ${productOption.value}&nbsp;--%>
        <%--</c:forEach>--%>
        <%--</em>--%>
        <%--</c:forEach>--%>
        <%--</td>--%>
        <%--<td>--%>
        <%--<fmt:formatNumber value="${hk:getComboCount(productLineItem)}" maxFractionDigits="0"/></td>--%>
        <%--<td> ${productLineItem.comboInstance.combo.hkPrice} </td>--%>
        <%--<td>--%>
        <%--<fmt:formatNumber--%>
        <%--value="${productLineItem.comboInstance.combo.hkPrice * hk:getComboCount(productLineItem)}"--%>
        <%--type="currency" currencySymbol="Rs. "/></td>--%>
        <%--</tr>--%>
        <%--</c:if>--%>
        <%--</c:if>--%>
    </c:forEach>

</table>

<h3>Order Summary</h3>
<table cellspacing="0">
    <tr>
        <td>Item Total</td>
        <td>
            <fmt:formatNumber value="${orderSummary.pricingDto.payableHkSubTotal}" type="currency"
                              currencySymbol="Rs. "/>
        </td>
    </tr>
    <tr>
        <td>Shipping</td>
        <td>
            <fmt:formatNumber
                    value="${orderSummary.pricingDto.shippingSubTotal - orderSummary.pricingDto.shippingDiscount}"
                    type="currency" currencySymbol="Rs. "/>
        </td>
    </tr>
    <tr>
        <td>Discount ${orderSummary.order.offerInstance.coupon.code}</td>
        <td>
            <fmt:formatNumber
                    value="${orderSummary.pricingDto.totalDiscount - orderSummary.pricingDto.shippingDiscount - orderSummary.pricingDto.subscriptionDiscount}"
                    type="currency" currencySymbol="Rs. "/>
        </td>
    </tr>
    <c:if test="${orderSummary.pricingDto.subscriptionDiscount >0}">
        <tr>
            <td>Subscription Discount</td>
            <td>
                <fmt:formatNumber value="${orderSummary.pricingDto.subscriptionDiscount}"
                                  type="currency" currencySymbol="Rs. "/>
            </td>
        </tr>
    </c:if>
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
                <fmt:formatNumber value="${orderSummary.pricingDto.codSubTotal}" type="currency"
                                  currencySymbol="Rs. "/></td>
        </tr>
    </c:if>
    <tr>
        <c:set var="eyeBool" value="false"/>
        <c:forEach items="${orderSummary.order.cartLineItems}" var="cartLineItems">
            <c:if test="${cartLineItems.productVariant.product.secondaryCategory.name eq eyeCat}">
                <c:set var="eyeBool" value="true"/>
            </c:if>
        </c:forEach>
        <c:choose>
            <c:when test="${eyeBool}">
                <td><strong>Grand Total*</strong></td>
            </c:when>
            <c:otherwise>
                <td><strong>Grand Total</strong></td>
            </c:otherwise>
        </c:choose>
        <td>
            <strong><fmt:formatNumber value="${orderSummary.pricingDto.grandTotalPayable}" type="currency"
                                      currencySymbol="Rs. " maxFractionDigits="0"/> </strong>
        </td>
    </tr>
</table>
<c:if test="${eyeBool}">
    <br>
    <td>*Sale Price of Eyeglasses includes value of Glasses/ Frames and Cover</td>
</c:if>
</div>

<div class="clear"></div>
<div style="margin-top: 5px;"></div>

<%--<div class="grid_12">
  <div style="font-size:.8em">Note: This is to certify that items inside do not contain any prohibited or hazardous material. These items are meant for personal use only and are not for resale.
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
</div>--%>

<div class="clear"></div>
<div style="margin-top: 5px;"></div>

</div>

</body>
</html>