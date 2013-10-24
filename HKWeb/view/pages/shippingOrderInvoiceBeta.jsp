<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.hk.constants.core.EnumRole" %>
<%@ page import="com.hk.constants.payment.EnumPaymentMode" %>
<%@ page import="com.hk.constants.catalog.category.CategoryConstants" %>
<%@ page import="com.hk.pact.dao.catalog.category.CategoryDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.domain.catalog.product.VariantConfigOptionParam" %>
<%@ page import="com.hk.admin.util.courier.thirdParty.FedExCourierUtil" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="com.hk.constants.courier.EnumCourier" %>
<%@ page import="com.hk.pact.service.shippingOrder.ShipmentService" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<c:set var="paymentMode_COD" value="<%=EnumPaymentMode.COD.getId()%>"/>
<c:set var="TH" value="<%=VariantConfigOptionParam.THICKNESS.param()%>"/>
<c:set var="THBF" value="<%=VariantConfigOptionParam.BFTHICKNESS.param()%>"/>
<c:set var="CO" value="<%=VariantConfigOptionParam.COATING.param()%>"/>
<c:set var="COBF" value="<%=VariantConfigOptionParam.BFCOATING.param()%>"/>
<c:set var="BRANDCO" value="<%=VariantConfigOptionParam.BRANDCO.param()%>"/>
<c:set var="BRANDTH" value="<%=VariantConfigOptionParam.BRANDTH.param()%>"/>
<c:set var="BRANDTHBF" value="<%=VariantConfigOptionParam.BRANDTHBF.param()%>"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <link rel="shortcut icon" href="<hk:vhostImage/>/favicon2.ico" />
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
    <%
        CategoryDao categoryDao = ServiceLocatorFactory.getService(CategoryDao.class);
        pageContext.setAttribute("sexualCare", Arrays.asList(categoryDao.getCategoryByName("personal-care"), categoryDao.getCategoryByName("sexual-care")));
        pageContext.setAttribute("personalCareWomen", Arrays.asList(categoryDao.getCategoryByName("personal-care"), categoryDao.getCategoryByName("women")));
        pageContext.setAttribute("discretePackaging", Arrays.asList(categoryDao.getCategoryByName("personal-care"), categoryDao.getCategoryByName("discrete-packaging")));
    %>
</head>
<body>
<s:useActionBean beanclass="com.hk.web.action.core.accounting.SOInvoiceAction" event="pre" var="orderSummary"/>
<c:set var="eyeCat" value="<%=CategoryConstants.eyeGlasses.getName()%>"/>
<c:set var="b2bUser" value="<%=EnumRole.B2B_USER.getRoleName()%>"/>
<c:set var="baseOrder" value="${orderSummary.shippingOrder.baseOrder}"/>
<c:set var="address" value="${baseOrder.address}"/>
<c:set var="fedEx" value="<%=EnumCourier.FedEx.getId()%>"/>
<c:set var="fedExSurface" value="<%=EnumCourier.FedEx_Surface.getId()%>"/>
<c:set var="groundShipped" value="${orderSummary.groundShipped}"/>
<c:set var="courierId" value="${orderSummary.shipment.awb.courier.id}"/>
<c:set var="shippingWarehouse" value="${hk:getShippingWarehouse(orderSummary.shippingOrder)}"/>
<c:set var="supplier" value="${orderSummary.supplier}"/>


<div class="container_12" style="border: 1px solid; padding-top: 10px;">
<div class="grid_4">
    <c:choose>
        <c:when test="${orderSummary.shipment != null}">
            <div style="float: left;">
                <strong>AWB NO.</strong>

                <c:choose>
                    <c:when test="${courierId == fedEx || courierId == fedExSurface}">
                        <div>${orderSummary.shipment.awb.awbNumber}</div>
                    </c:when>
                    <c:otherwise>
                        <div class="clear"></div>
                        <div style="font-weight:bold; margin-top:5px;">${orderSummary.shipment.awb.courier.name}</div>
                        <div class="clear"></div>
                        <img style="padding-top: 0px; padding-left: 0px; padding-right: 150px; "
                             src="${pageContext.request.contextPath}/barcodes/${orderSummary.shipment.awb.awbNumber}.png"/>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:when>
        <c:otherwise>
            &nbsp;&nbsp;
        </c:otherwise>
    </c:choose>
</div>

<div class="grid_4">
    <div style="text-align: center;">
        ORDER INVOICE <c:choose>
        <c:when
                test="${orderSummary.printable && hk:isOrderForDiscretePackaging(orderSummary.shippingOrder)}"><b>(OUT)</b></c:when>
        <c:otherwise><b>(IN)</b>
        </c:otherwise>
    </c:choose>
    </div>
</div>
<div class="grid_4">
    <div style="float: right;">
        <c:if test="${!hk:collectionContains(orderSummary.shippingOrder.baseOrder.user.roleStrings, b2bUser)}">
            <c:choose>
                <c:when test="${orderSummary.shippingOrder.baseOrder.store.id == 2 || orderSummary.shippingOrder.baseOrder.store.id == 3}">
                    <c:if test="${orderSummary.shippingOrder.baseOrder.store.id == 2}">
                        <img src="${pageContext.request.contextPath}/images/mih-logo.jpg" alt="MadeInHealth Logo"/>
                    </c:if>
                    <c:if test="${orderSummary.shippingOrder.baseOrder.store.id == 3}">
                        <img src="${pageContext.request.contextPath}/images/fitnesspro.png" alt="FitnessPro Logo"/>
                    </c:if>
                </c:when>
                <c:otherwise>
                    <img src="<hk:vhostImage/>/images/logo/HK-Logo.png" alt="Healthkart Logo"/>
                </c:otherwise>
            </c:choose>
        </c:if>
    </div>
</div>


<c:choose>
    <c:when test="${courierId == fedEx || courierId == fedExSurface}">
        <div class="grid_12">

            <div style="font-weight:bold; margin-top:5px;">${orderSummary.shipment.awb.courier.name} &nbsp;&nbsp;
                <c:choose>
                    <c:when test="${groundShipped}">
                        Economy
                    </c:when>
                    <c:otherwise>
                        Standard Overnight
                    </c:otherwise>
                </c:choose>
                <c:if test="${orderSummary.shippingOrder.COD && orderSummary.invoiceDto.grandTotal > 0}">
                    COD
                </c:if>
                &nbsp;&nbsp;wt:${orderSummary.estimatedWeightOfPackage}Kg&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Bill Sender&nbsp;&nbsp;&nbsp;D/T
                Sender
            </div>

            <div class="clear"></div>
            <img style="padding-top: 0px; padding-left: 0px; padding-right: 150px; "
                 src="${pageContext.request.contextPath}/barcodes/${orderSummary.shipment.awb.awbBarCode}.png"/>

        </div>
    </c:when>
    <c:otherwise></c:otherwise>
</c:choose>

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
                ${address.city} - ${address.pincode.pincode}
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
                <c:if test="${courierId == fedEx || courierId == fedExSurface}">
                    <div class="clear"></div>
                    <div style="font-weight:bold; margin-top:5px;">COD Return : Priority Overnight
                        &nbsp;&nbsp; ${orderSummary.shipment.awb.returnAwbNumber}
                    </div>
                    <div class="clear"></div>
                    <img style="padding-top: 0px; padding-left: 0px; padding-right: 150px; "
                         src="${pageContext.request.contextPath}/barcodes/${orderSummary.shipment.awb.returnAwbBarCode}.png"/>
                </c:if>
            </c:if>
        </div>
    </div>

    <div class="grid_4 alpha omega" style="width: 320px;">
        <strong>ORDER NO.</strong>

        <div class="clear"></div>
        <div style="margin-top:5px;"></div>
        <img src="${pageContext.request.contextPath}/barcodes/${orderSummary.shippingOrder.gatewayOrderId}.png"/>
        <c:if test="${!orderSummary.shippingOrder.dropShipping}">
            <p>Fulfillment Centre: ${shippingWarehouse.identifier}</p>
        </c:if>
        <c:if test="${orderSummary.shippingOrder.warehouse.id == 1}">
            <p>Return Location: <b>DEL/ITG/111117</b></p>
        </c:if>
        <c:if test="${orderSummary.shippingOrder.warehouse.id == 2}">
            <p>Return Location: <b>BOM/BPT/421302</b></p>
        </c:if>
        <p>

        <p></p>
        <c:if test="${courierId == fedEx || courierId == fedExSurface}">
            <div style="font-size:.8em">
                Subject to the conditions of Carriage, which limits the liability to FedEx
                for loss, delay or damage to the consignment. Visit www.fedex.com/in to view the conditions of carriage.<br>
            </div>
        </c:if>
    </div>
</div>

<div class="clear"></div>
<div style="margin-top: 5px;"></div>

<div class="grid_12">
    <c:if test="${orderSummary.printZone && orderSummary.zone != null}">
        <hr/>
        <p><strong>ZONE:-</strong> ${orderSummary.zone}</p>
        <hr/>
    </c:if>
</div>


<div class="clear"></div>
<div style="margin-top: 5px;"></div>

<div class="grid_12">
    <div style="font-size:.8em">
        <h3 style="margin:0;">Please do not accept if the box is tampered</h3>

        Note: This is to certify that items inside do not contain any prohibited or hazardous material. These items are
        meant for personal use only and are not for resale.
    </div>
    <hr/>
    <c:choose>
        <c:when test="${orderSummary.shippingOrder.dropShipping}">
            <p style="font-size: .8em;"> ${supplier.name} | ${supplier.line1}, ${supplier.line2} |
                    ${supplier.city}, ${supplier.state}- ${supplier.pincode} | TIN:
                    ${supplier.tinNumber}  </p>
        </c:when>
        <c:otherwise>
            ${shippingWarehouse.name} | ${shippingWarehouse.line1} | ${shippingWarehouse.line2} |
            ${shippingWarehouse.city} | ${shippingWarehouse.state} - ${shippingWarehouse.pincode} |
            TIN: ${shippingWarehouse.tin}
        </c:otherwise>
    </c:choose>

</div>
<div class="clear"></div>
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
    <h3 style="margin-bottom:2px">Order Details</h3>Merchant Transaction Ref No ${baseOrder.gatewayOrderId} for order
    placed on: <fmt:formatDate value="${baseOrder.payment.paymentDate}" type="both" timeStyle="short"/>
    <div style="margin-top: 5px;"></div>
    <c:if test="${orderSummary.invoiceDto.replacementOrderString != null}">
        <h3>${orderSummary.invoiceDto.replacementOrderString}</h3>
    </c:if>
    <table cellspacing="0">
        <tr>
            <th>Item</th>
            <th>Quantity</th>
            <th>MRP</th>
            <th>Rate</th>
            <th>Total(Rs.)</th>
        </tr>
        <c:forEach items="${orderSummary.invoiceDto.invoiceLineItemDtos}" var="invoiceLineItem">
            <tr>
                <td>
                    <c:choose>
                        <c:when test="${orderSummary.printable && (hk:collectionContainsCollection(invoiceLineItem.productCategories, sexualCare)
						                || hk:collectionContainsCollection(invoiceLineItem.productCategories, personalCareWomen) || hk:collectionContainsCollection(invoiceLineItem.productCategories, discretePackaging))}">
                            <p>Personal Care Product</p>
                        </c:when>
                        <c:otherwise>
                            <p>
                                <c:choose>
                                    <c:when test="${empty invoiceLineItem.variantName}">
                                       ${invoiceLineItem.productName}
                                    </c:when>
                                    <c:otherwise>
                                       ${invoiceLineItem.variantName}
                                    </c:otherwise>
                                </c:choose>
                            </p>
                            <em>
                                <p>
                                    <c:forEach items="${invoiceLineItem.productOptions}"
                                               var="productOption">
                                        <c:if test="${hk:showOptionOnUI(productOption.name)}">
                                            ${productOption.name}:${productOption.value};
                                        </c:if>
                                    </c:forEach>
                                </p>

                                <p>
                                        ${invoiceLineItem.extraOptionsPipeSeparated}
                                    <!--${invoiceLineItem.configOptionsPipeSeparated}-->
                                </p>
                            </em>

                            <c:if test="${invoiceLineItem.cartLineItemConfigValues !=null}">

                                <c:forEach items="${invoiceLineItem.cartLineItemConfigValues}"
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
                                        <c:forEach items="${invoiceLineItem.cartLineItemConfigValues}"
                                                   var="configValue" varStatus="configValueCtr">
                                            <c:set var="additinalParam"
                                                   value="${configValue.variantConfigOption.additionalParam}"/>

                                            <c:set var="side" value="${configValue.variantConfigOption.name}"/>
                                            <c:if
                                                    test="${  fn:startsWith(side,'R' ) && !( additinalParam == TH || additinalParam == THBF
								|| additinalParam == CO || additinalParam == COBF || additinalParam == BRANDCO || additinalParam == BRANDTH 
								|| additinalParam == BRANDTHBF) }">
                                                <td>
                                                    <b>${configValue.variantConfigOption.displayName}:${configValue.value}</b>
                                                </td>
                                            </c:if>
                                        </c:forEach>
                                    </tr>
                                    <tr>
                                        <td><b>Left</b></td>
                                        <c:forEach items="${invoiceLineItem.cartLineItemConfigValues}"
                                                   var="configValue" varStatus="configValueCtr">
                                            <c:set var="additinalParam"
                                                   value="${configValue.variantConfigOption.additionalParam}"/>
                                            <c:set var="side" value="${configValue.variantConfigOption.name}"/>
                                            <c:if
                                                    test="${fn:startsWith(side,'L' ) && !( additinalParam == TH || additinalParam == THBF
								|| additinalParam == CO || additinalParam == COBF || additinalParam == BRANDCO || additinalParam == BRANDTH 
								|| additinalParam == BRANDTHBF)}">
                                                <td>
                                                    <b>${configValue.variantConfigOption.displayName}:${configValue.value}</b>
                                                </td>
                                            </c:if>
                                        </c:forEach>
                                    </tr>
                                </table>
                            </c:if>
                        </c:otherwise>
                    </c:choose>
                </td>

                <td><fmt:formatNumber value="${invoiceLineItem.qty}" maxFractionDigits="0"/></td>
                <td> ${invoiceLineItem.markedPrice} </td>
                <td> ${invoiceLineItem.hkPrice} </td>
                <td class="itemsubTotal">
                    <fmt:formatNumber value="${invoiceLineItem.lineItemTotal}" type="currency"
                                      currencySymbol="Rs. "/></td>

            </tr>
            <%-- </c:if>--%>
        </c:forEach>

        <%--<c:if test="${orderSummary.freebieItem != null && orderSummary.freebieItem != ''}">
            <tr>
                <td>${orderSummary.freebieItem}</td>
                <td>1</td>
                <td>0.0</td>
                <td>0.0</td>
                <td>0.0</td>
            </tr>
        </c:if>--%>

    </table>

    <c:if test="${orderSummary.shippingOrder.dropShipping && orderSummary.installableItemPresent}">
        <h4> You have ordered an equipment that requires installation/configuration.
            <br/>Please contact HealthKart Customer Care at 0124-4616444 - so that we can arrange for the Technician's
            visit.</h4>
    </c:if>

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
            <td>
                <c:if test="${orderSummary.invoiceDto.totalDiscount > 0}"><b></c:if>
                Discount ${baseOrder.offerInstance.coupon.code}
                <c:if test="${orderSummary.invoiceDto.totalDiscount > 0}"></b></c:if>
            </td>
            <td>
                <c:if test="${orderSummary.invoiceDto.totalDiscount > 0}"><b></c:if>
                <fmt:formatNumber
                        value="${orderSummary.invoiceDto.totalDiscount}"
                        type="currency" currencySymbol="Rs. "/>
                <c:if test="${orderSummary.invoiceDto.totalDiscount > 0}"></b></c:if>
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
            <c:set var="eyeBool" value="false"/>
            <c:forEach items="${orderSummary.shippingOrder.lineItems}" var="lineItems">
                <c:if test="${lineItems.cartLineItem.productVariant.product.secondaryCategory.name eq eyeCat}">
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
                <strong><fmt:formatNumber value="${orderSummary.invoiceDto.grandTotal}" type="currency"
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
