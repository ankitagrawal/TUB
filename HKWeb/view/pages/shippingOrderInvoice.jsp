<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.hk.constants.core.EnumRole" %>
<%@ page import="com.hk.constants.payment.EnumPaymentMode" %>
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
<s:useActionBean beanclass="com.hk.web.action.core.accounting.SOInvoiceAction" event="pre" var="orderSummary"/>
<c:set var="b2bUser" value="<%=EnumRole.B2B_USER.getRoleName()%>"/>
<c:set var="baseOrder" value="${orderSummary.shippingOrder.baseOrder}"/>
<c:set var="address" value="${baseOrder.address}"/>

<div class="container_12" style="border: 1px solid; padding-top: 10px;">
<div class="grid_4">
    <c:choose>
        <c:when test="${orderSummary.shipment != null}">
            <div style="float: left;">
                <strong>AWB NO.</strong>

                <div class="clear"></div>
                <div style="font-weight:bold; margin-top:5px;">${orderSummary.shipment.courier.name}</div>
                <div class="clear"></div>
                <img style="padding-top: 0px; padding-left: 0px; padding-right: 150px; "
                     src="${pageContext.request.contextPath}/barcodes/${orderSummary.shipment.awb.awbNumber}.png"/>
            </div>
        </c:when>
        <c:otherwise>
            &nbsp;&nbsp;
        </c:otherwise>
    </c:choose>
</div>

<div class="grid_4">
    <div style="text-align: center;">
        ORDER INVOICE
    </div>
</div>
<div class="grid_4">
    <div style="float: right;">
        <c:choose>
            <c:when test="${orderSummary.shippingOrder.baseOrder.user.login == 'support@madeinhealth.com' || orderSummary.shippingOrder.baseOrder.store.id == 2}">
                <img src="${pageContext.request.contextPath}/images/mih-logo.jpg" alt="MadeInHealth Logo"/>
            </c:when>
            <c:otherwise>
                <img src="${pageContext.request.contextPath}/images/logo.png" alt="HealthKart Logo"/>
            </c:otherwise>
        </c:choose>

    </div>
</div>

<div class="clear"></div>
<div style="margin-top: 15px;"></div>

<div class="grid_12">
    <div class="grid_8 alpha omega">
        <div class="formatting" style="float: left; font-size:1.1em;">

            <strong>Name & Address</strong>

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

            <c:if test="${baseOrder.payment.paymentMode.id == paymentMode_COD && orderSummary.invoiceDto.grandTotal > 0}">
                <h2>Cash on Delivery :
                    <fmt:formatNumber value="${orderSummary.invoiceDto.grandTotal}" type="currency"
                                      currencySymbol="Rs. " maxFractionDigits="0"/></h2>
            </c:if>
        </div>
    </div>

    <div class="grid_4 alpha omega" style="width: 320px;">
        <strong>ORDER NO.</strong>

        <div class="clear"></div>
        <div style="margin-top:5px;"></div>
        <img src="${pageContext.request.contextPath}/barcodes/${orderSummary.shippingOrder.gatewayOrderId}.png"/>

        <p>Fulfillment Centre: ${orderSummary.shippingOrder.warehouse.name}</p>
        <c:if test="${orderSummary.shippingOrder.warehouse.id == 1}">
            <p>Return Location: <b>DEL/ITG/111117</b></p>
        </c:if>
        <c:if test="${orderSummary.shippingOrder.warehouse.id == 2}">
            <p>Return Location: <b>BOM/BPT/421302</b></p>
        </c:if>
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
    <div style="font-size:.8em">
        <h3 style="margin:0;">Please do not accept if the box is tampered</h3>

        Note: This is to certify that items inside do not contain any prohibited or hazardous
        material.
    </div>
    <hr/>
    <c:set var="warehouse" value="${orderSummary.shippingOrder.warehouse}"/>
    <c:choose>
        <c:when test="${hk:collectionContains(baseOrder.user.roleStrings, b2bUser)}">
            <p style="font-size: .8em;">Bright Lifecare Pvt. Ltd. | Khasra No. 146/25/2/1, Jail Road, Dhumaspur,
                Badshahpur |
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
<div class="grid_12">
    <h3>Order Details</h3>Merchant Transaction Ref No ${baseOrder.gatewayOrderId} for order
    placed on: <fmt:formatDate value="${baseOrder.payment.paymentDate}" type="both" timeStyle="short"/>

    <c:if test="${orderSummary.invoiceDto.replacementOrderString != null}">
        <h3>${orderSummary.invoiceDto.replacementOrderString}</h3>
    </c:if>
    <table cellspacing="0">
        <tr>
            <th>Item</th>
            <th>Quantity</th>
            <th>Unit price</th>
            <th>Total(Rs.)</th>
        </tr>
        <c:forEach items="${orderSummary.invoiceDto.invoiceLineItemDtos}" var="invoiceLineItem">
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
                        </p>
                        <p>
                                ${invoiceLineItem.extraOptionsPipeSeparated}
                                ${invoiceLineItem.configOptionsPipeSeparated}
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

        <c:if test="${orderSummary.freebieItem != null && orderSummary.freebieItem != ''}">
            <tr>
                <td>${orderSummary.freebieItem}</td>
                <td>1</td>
                <td>0.0</td>
                <td>0.0</td>
            </tr>
        </c:if>

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
                                          currencySymbol="Rs. " maxFractionDigits="0"/> </strong>
            </td>
        </tr>
    </table>
</div>

<div class="clear"></div>
<div style="margin-top: 5px;"></div>

<%--<div class="grid_12">--%>
<%--<div style="font-size:.8em">Note: This is to certify that items inside do not contain any prohibited or hazardous--%>
<%--material.--%>
<%--</div>--%>
<%--<hr/>--%>
<%--<c:set var="warehouse" value="${orderSummary.shippingOrder.warehouse}"/>--%>
<%--<c:choose>--%>
<%--<c:when test="${hk:collectionContains(baseOrder.user.roleStrings, b2bUser)}">--%>
<%--<p style="font-size: .8em;">Bright Lifecare Pvt. Ltd. | Khasra No. 146/25/2/1, Jail Road, Dhumaspur,--%>
<%--Badshahpur |--%>
<%--Gurgaon, Haryana- 122101 | TIN:--%>
<%--06101832036 </p>--%>
<%--</c:when>--%>
<%--<c:otherwise>--%>
<%--<p style="font-size: .8em;">Aquamarine Healthcare Pvt. Ltd. | ${warehouse.line1}, ${warehouse.line2} |--%>
<%--${warehouse.city}, ${warehouse.state}- ${warehouse.pincode} | TIN:--%>
<%--${warehouse.tin} </p>--%>
<%--</c:otherwise>--%>
<%--</c:choose>--%>

<%--</div>--%>
<%--<div class="clear"></div>--%>
<%--<div style="margin-top: 5px;"></div>--%>
</div>
</body>
</html>
