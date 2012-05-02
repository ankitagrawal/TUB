<%@ page import="java.text.DateFormat" %>
<%@ page import="com.hk.constants.EnumPaymentMode" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
  <title>TAX INVOICE</title>

  <s:useActionBean beanclass="web.action.AccountingInvoiceAction" event="b2bInvoice" var="orderSummary"/>

  <c:set var="paymentModeCod" value="<%=EnumPaymentMode.COD.getId()%>"/>
  <c:set var="partialRatio" value="${(orderSummary.partialPricingDto.productsHkSubTotal/orderSummary.pricingDto.productsHkSubTotal)}"/>
  <style type="text/css">
    table {
      border-collapse: collapse;
      width: 100%
    }

    table tr td {
      padding: 2px;
      border: 1px solid #CCC;
    }

    table#border_zero tr td {
      padding: 2px;
      border: 0;
    }

    table tr th {
      padding: 2px;
      border: 1px solid #CCC;
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

    table.header tr td {
      border: none;
      vertical-align: top
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
  <script type="text/javascript" src="<hk:vhostJs/>/js/jquery-1.6.2.min.js"></script>
  <script type="text/javascript" src="<hk:vhostJs/>/js/jquery.hkCommonPlugins.js"></script>

</head>
<body>
<script type="text/javascript">
  var summaryQtyZero = 0;
  var summaryGrossAmountZero = 0.0;
  var summaryTaxZero = 0.0;
  var summarySurchargeZero = 0.0;
  var summaryPayableZero = 0.0;
  var summaryQtyFive = 0;
  var summaryGrossAmountFive = 0.0;
  var summaryTaxFive = 0.0;
  var summarySurchargeFive = 0.0;
  var summaryPayableFive = 0.0;
  var summaryQtyTwelveDotFive = 0;
  var summaryGrossAmountTwelveDotFive = 0;
  var summaryTaxTwelveDotFive = 0.0;
  var summarySurchargeTweleveDotFive = 0.0;
  var summaryPayableTweleveDotFive = 0.0;
</script>

<table id="border_zero">
  <td align="left">
    <img src="${pageContext.request.contextPath}/barcodes/${orderSummary.accountingInvoice.order.gatewayOrderId}.png"/>
  </td>
  <td>
    <h4>TAX INVOICE</h4>
  </td>
</table>

<table>
  <td>
    <b>Invoice#&nbsp;</b>T-${orderSummary.accountingInvoice.b2bInvoiceId}&nbsp; on
    &nbsp;<fmt:formatDate value="${orderSummary.accountingInvoice.invoiceDate}" type="both" timeStyle="short"/><br/>
    <b>Order#:</b>
    ${orderSummary.accountingInvoice.order.gatewayOrderId}&nbsp; on &nbsp;
    <fmt:formatDate value="${orderSummary.accountingInvoice.order.payment.createDate}" type="both" timeStyle="short"/><br/>

    <div style="font-size:1.0em;">
      <b>Buyer:</b> <br/>
      ${orderSummary.accountingInvoice.order.address.name}<br/>
      ${orderSummary.accountingInvoice.order.address.line1}<br/>
      <c:if test="${not empty orderSummary.accountingInvoice.order.address.line2}">
        ${orderSummary.accountingInvoice.order.address.line2}<br/>
      </c:if>
      ${orderSummary.accountingInvoice.order.address.city}, ${orderSummary.accountingInvoice.order.address.state}
      - ${orderSummary.accountingInvoice.order.address.pin},
      <br/>
      Ph: ${orderSummary.accountingInvoice.order.address.phone}
      <br/>
      TIN# ${orderSummary.b2bUserDetails.tin}
      <br/>
      D. L. No. ${orderSummary.b2bUserDetails.dlNumber}
    </div>
  </td>
  <td valign="top">
    Bright Lifecare Pvt. Ltd.<br/>
    3rd Floor, Parsvnath Arcadia,<br/>
    1, MG Road, Gurgaon, Haryana 122001<br/>
    TIN#06101832036<br/>
    D. L. No. HR-6600-201-W/H,  Dated: 19/12/2011<br/>
              HR-6600-201-OW/H, Dated: 19/12/2011
  </td>

</table>

<br/><br/>

<table style="font-size: .8em;">
  <tr>
    <th width="300">Item</th>
    <th width="20">Qty</th>
    <th width="50">MRP</th>
    <th width="55">Tax Rate</th>
    <th width="55">Rate</th>
    <th width="50">Taxable</th>
    <th width="50">Tax</th>
    <th width="50">Surcharge</th>
  </tr>
  <c:forEach items="${orderSummary.pricingDto.productLineItems}" var="invoiceLineItem">
      <tr>
        <td>${invoiceLineItem.productVariant.product.name}
          <em><c:forEach items="${invoiceLineItem.productVariant.productOptions}" var="productOption">
            ${productOption.name} ${productOption.value}
          </c:forEach></em>
        </td>
        <td>
          <fmt:formatNumber value="${invoiceLineItem.qty}" maxFractionDigits="0"/></td>
        <td>
          <fmt:formatNumber value="${invoiceLineItem.markedPrice}" maxFractionDigits="0"/></td>
         <td> ${invoiceLineItem.productVariant.tax.value*100}%</td>
        <td>
          <fmt:formatNumber value="${hk:getOrderBasedApplicableOfferPrice(invoiceLineItem.productVariant, orderSummary.order) - hk:netDiscountOnLineItem(invoiceLineItem)/invoiceLineItem.qty}" maxFractionDigits="2"/></td>
        <td class="taxable">
          <fmt:formatNumber value="${((hk:getOrderBasedApplicableOfferPrice(invoiceLineItem.productVariant, orderSummary.order) * invoiceLineItem.qty - hk:netDiscountOnLineItem(invoiceLineItem)) / (1+invoiceLineItem.productVariant.tax.value+invoiceLineItem.productVariant.tax.value*.05))}" maxFractionDigits="2"/></td>
        <td class="tax">
          <fmt:formatNumber value="${((hk:getOrderBasedApplicableOfferPrice(invoiceLineItem.productVariant, orderSummary.order) * invoiceLineItem.qty - hk:netDiscountOnLineItem(invoiceLineItem)) / (1+invoiceLineItem.productVariant.tax.value+invoiceLineItem.productVariant.tax.value*.05)) * (invoiceLineItem.productVariant.tax.value)}" maxFractionDigits="2"/></td>
        <td class="surcharge">
          <fmt:formatNumber value="${((hk:getOrderBasedApplicableOfferPrice(invoiceLineItem.productVariant, orderSummary.order) * invoiceLineItem.qty - hk:netDiscountOnLineItem(invoiceLineItem)) / (1+invoiceLineItem.productVariant.tax.value+invoiceLineItem.productVariant.tax.value*.05)) * (invoiceLineItem.productVariant.tax.value*.05)}" maxFractionDigits="2"/></td>
      </tr>
    </c:forEach>
  <tr>
    <td colspan="8"></td>
  </tr>
  <tr>
    <td colspan="5"><b>Total</b></td>
    <td class="totalTaxable"></td>
    <td class="totalTax"></td>
    <td class="totalSurcharge"></td>
  </tr>
  <tr>
    <td colspan="8"></td>
  </tr>
  <tr>
    <td colspan="7" align="right"><b>Grand Total</b></td>
    <td class="grandTotal" style="font-weight:bold;"></td>
  </tr>

  <script type="text/javascript">
    $(document).ready(function() {
      var taxable = 0.0;
      $('.taxable').each(function() {
        taxable += new Number(($(this).html()).replace(",", ""));
      });
      $('.totalTaxable').html(Math.round(taxable * 100) / 100);

      var tax = 0.0;
      $('.tax').each(function() {
        tax += new Number(($(this).html()).replace(",", ""));
      });
      $('.totalTax').html(Math.round(tax * 100) / 100);

      var surcharge = 0.0;
      $('.surcharge').each(function() {
        surcharge += new Number(($(this).html()).replace(",", ""));
      });
      $('.totalSurcharge').html(Math.round(surcharge * 100) / 100);

      //$('.grandTotal').html(Math.round(taxable * 100) / 100 + Math.round(tax * 100) / 100 + Math.round(surcharge * 100) / 100);
      $('.grandTotal').html(Math.round((taxable + tax + surcharge) * 100) / 100);
    });
  </script>

</table>


<div>
  <table id="border_zero">
    <tr>
      <td>
        <div><b>Terms & Conditions:</b><br/>
          1. This is a computer generated invoice.<br/>
          2. All disputes are subject to Gurgaon Jurisdiction.
        </div>
      </td>
      <td>
        <div style="display:inline;float:right;">
          <br/>
          <b>(Authorised Signatory)</b>
        </div>
      </td>
    </tr>
  </table>
</div>

</body>
</html>
