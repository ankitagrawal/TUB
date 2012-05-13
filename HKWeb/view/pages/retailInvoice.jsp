<%@ page import="java.text.DateFormat" %>
<%@ page import="com.hk.constants.payment.EnumPaymentMode" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
  <title>RETAIL INVOICE/ CASH MEMO</title>

  <s:useActionBean beanclass="com.hk.web.action.core.accounting.AccountingInvoiceAction" event="retailInvoice" var="orderSummary"/>

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
    <h4>RETAIL INVOICE/ CASH MEMO</h4>
  </td>
</table>

<table>
  <td>
    <b>Invoice#&nbsp;</b>R-${orderSummary.accountingInvoice.retailInvoiceId}&nbsp; on
    &nbsp;<fmt:formatDate value="${orderSummary.accountingInvoice.invoiceDate}" type="both" timeStyle="short"/><br/>
    <b>Payment Mode:</b>${orderSummary.accountingInvoice.order.payment.paymentMode.name}
    <c:if test="${orderSummary.accountingInvoice.order.payment.paymentMode.id == paymentModeCod}">
      <label style="font-size:1.3em; font-weight:bold;">
        <fmt:formatNumber value="${orderSummary.accountingInvoice.order.payment.amount * partialRatio}" maxFractionDigits="0" currencySymbol="Rs."/>
      </label>
    </c:if>
    <br/>
    <c:if test="${orderSummary.defaultCourier != null}">
    <b>Courier:</b>${orderSummary.defaultCourier}
    </c:if>
    <br/><b>Order#:</b>
    ${orderSummary.accountingInvoice.order.gatewayOrderId}&nbsp; on &nbsp;
    <fmt:formatDate value="${orderSummary.accountingInvoice.order.payment.createDate}" type="both" timeStyle="short"/><br/>

    <div style="font-size:1.2em;">
      <b>Customer:</b> <br/>
      ${orderSummary.accountingInvoice.order.address.name}<br/>
      ${orderSummary.accountingInvoice.order.address.line1}<br/>
      <c:if test="${not empty orderSummary.accountingInvoice.order.address.line2}">
        ${orderSummary.accountingInvoice.order.address.line2}<br/>
      </c:if>
      ${orderSummary.accountingInvoice.order.address.city}, ${orderSummary.accountingInvoice.order.address.state}
      - ${orderSummary.accountingInvoice.order.address.pin},
      <br/>
      Ph: ${orderSummary.accountingInvoice.order.address.phone}
    </div>
  </td>
  <td valign="top">
    Aquamarine HealthCare Pvt. Ltd.<br/>
    3rd Floor, Parshavnath Arcadia,<br/>
    1, MG Road, Gurgaon, Haryana 122001<br/>
    TIN#06101832327
  </td>

</table>

<br/><br/>

<table style="font-size: .8em;">
  <tr>
    <th width="300">Item</th>
    <th width="20">Qty</th>
    <th width="50">Rate</th>
    <th width="55" style="display:none;">Tax Rate</th>
    <th width="50" style="display:none;">Taxable</th>
    <th width="50" style="display:none;">Tax</th>
    <th width="50" style="display:none;">Surcharge</th>
  </tr>
  <c:forEach items="${orderSummary.partialPricingDto.productLineItems}" var="invoiceLineItem">
    <tr>
      <td>${invoiceLineItem.productVariant.product.name}
        <em><c:forEach items="${invoiceLineItem.productVariant.productOptions}" var="productOption">
          ${productOption.name} ${productOption.value}
        </c:forEach></em>
      </td>
      <td>
        <fmt:formatNumber value="${invoiceLineItem.qty}" maxFractionDigits="0"/></td>
      <td>
        <fmt:formatNumber value="${invoiceLineItem.hkPrice - hk:netDiscountOnLineItem(invoiceLineItem)/invoiceLineItem.qty}" maxFractionDigits="2"/></td>
      <td style="display:none;"> ${invoiceLineItem.productVariant.tax.value*100}%</td>
      <td class="taxable" style="display:none;">
        <fmt:formatNumber value="${(invoiceLineItem.hkPrice * invoiceLineItem.qty - hk:netDiscountOnLineItem(invoiceLineItem)) * (1-invoiceLineItem.productVariant.tax.value-invoiceLineItem.productVariant.tax.value*.05)}" maxFractionDigits="2"/></td>
      <td class="tax" style="display:none;">
        <fmt:formatNumber value="${(invoiceLineItem.hkPrice * invoiceLineItem.qty - hk:netDiscountOnLineItem(invoiceLineItem)) * (invoiceLineItem.productVariant.tax.value)}" maxFractionDigits="2"/></td>
      <td class="surcharge" style="display:none;">
        <fmt:formatNumber value="${(invoiceLineItem.hkPrice * invoiceLineItem.qty - hk:netDiscountOnLineItem(invoiceLineItem)) * (invoiceLineItem.productVariant.tax.value*.05)}" maxFractionDigits="2"/></td>
    </tr>
  </c:forEach>
  <tr>
    <td colspan="3"></td>
  </tr>
  <tr>
    <td colspan="2"><b>Total</b></td>
    <%--<td class="totalTaxable"></td>
    <td class="totalTax"></td>
    <td class="totalSurcharge"></td>--%>
    <td class="grandTotal"></td>
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


<%--
<table style="font-size: .8em;">
  <tr>
    <td colspan="6">
      <div align="center"><b>Tax Summary</b></div>
    </td>
  </tr>
  <tr>
    <th width="17%">VAT Percent</th>
    <th width="5%">Qty</th>
    <th width="10%">Amount</th>
    <th width="16%">Tax on Amount</th>
    <th width="10%">Surcharge</th>
    <th width="39%">Gross Amount(Amt+Tax+Surcharge)</th>
  </tr>

  <c:forEach var="productLineItem" items="${orderSummary.pricingDto.productLineItems}">
    <c:if test="${(productLineItem.productVariant.tax.value*100) == 0}">
      <script type="text/javascript">
        summaryQtyZero += ${productLineItem.qty};
        summaryGrossAmountZero += ${(productLineItem.hkPrice * productLineItem.qty - hk:netDiscountOnLineItem(productLineItem)) * (1-productLineItem.productVariant.tax.value-productLineItem.productVariant.tax.value*.05)};
        summaryTaxZero += ${(productLineItem.hkPrice * productLineItem.qty - hk:netDiscountOnLineItem(productLineItem)) * (productLineItem.productVariant.tax.value)};
        summarySurchargeZero += ${(productLineItem.hkPrice * productLineItem.qty - hk:netDiscountOnLineItem(productLineItem)) * (productLineItem.productVariant.tax.value*.05)};
        summaryPayableZero = summaryGrossAmountZero + summaryTaxZero + summarySurchargeZero;
      </script>
    </c:if>
    <c:if test="${(productLineItem.productVariant.tax.value*100) == 5}">
      <script type="text/javascript">
        summaryQtyFive += ${productLineItem.qty};
        summaryGrossAmountFive += ${(productLineItem.hkPrice * productLineItem.qty - hk:netDiscountOnLineItem(productLineItem)) * (1-productLineItem.productVariant.tax.value-productLineItem.productVariant.tax.value*.05)};
        summaryTaxFive += ${(productLineItem.hkPrice * productLineItem.qty - hk:netDiscountOnLineItem(productLineItem)) * (productLineItem.productVariant.tax.value)};
        summarySurchargeFive += ${(productLineItem.hkPrice * productLineItem.qty - hk:netDiscountOnLineItem(productLineItem)) * (productLineItem.productVariant.tax.value*.05)};
        summaryPayableFive = summaryGrossAmountFive + summaryTaxFive + summarySurchargeFive;
      </script>
    </c:if>
    <c:if test="${(productLineItem.productVariant.tax.value*100) == 12.5}">
      <script type="text/javascript">
        summaryQtyTwelveDotFive += ${productLineItem.qty};
        summaryGrossAmountTwelveDotFive += ${(productLineItem.hkPrice * productLineItem.qty - hk:netDiscountOnLineItem(productLineItem)) * (1-productLineItem.productVariant.tax.value-productLineItem.productVariant.tax.value*.05)};
        summaryTaxTwelveDotFive += ${(productLineItem.hkPrice * productLineItem.qty - hk:netDiscountOnLineItem(productLineItem)) * (productLineItem.productVariant.tax.value)};
        summarySurchargeTweleveDotFive += ${(productLineItem.hkPrice * productLineItem.qty - hk:netDiscountOnLineItem(productLineItem)) * (productLineItem.productVariant.tax.value*.05)};
        summaryPayableTweleveDotFive = summaryGrossAmountTwelveDotFive + summaryTaxTwelveDotFive + summarySurchargeTweleveDotFive;
      </script>
    </c:if>
  </c:forEach>


  <script type="text/javascript">

    $(document).ready(function() {
      $('.summaryQtyZeroDisplay').html(summaryQtyZero);
      $('.summaryGrossAmountZeroDisplay').html(Math.round(summaryGrossAmountZero * 100) / 100);
      $('.summaryTaxZeroDisplay').html(Math.round(summaryTaxZero * 100) / 100);
      $('.summarySurchargeZeroDisplay').html(Math.round(summarySurchargeZero * 100) / 100);
      $('.summaryPayableZeroDisplay').html(Math.round(summaryPayableZero * 100) / 100);

      $('.summaryQtyFiveDisplay').html(summaryQtyFive);
      $('.summaryGrossAmountFiveDisplay').html(Math.round(summaryGrossAmountFive * 100) / 100);
      $('.summaryTaxFiveDisplay').html(Math.round(summaryTaxFive * 100) / 100);
      $('.summarySurchargeFiveDisplay').html(Math.round(summarySurchargeFive * 100) / 100);
      $('.summaryPayableFiveDisplay').html(Math.round(summaryPayableFive * 100) / 100);

      $('.summaryQtyTwelveDotFiveDisplay').html(summaryQtyTwelveDotFive);
      $('.summaryGrossAmountTwelveDotFiveDisplay').html(Math.round(summaryGrossAmountTwelveDotFive * 100) / 100);
      $('.summaryTaxTwelveDotFiveDisplay').html(Math.round(summaryTaxTwelveDotFive * 100) / 100);
      $('.summarySurchargeTweleveDotFiveDisplay').html(Math.round(summarySurchargeTweleveDotFive * 100) / 100);
      $('.summaryPayableTweleveDotFiveDisplay').html(Math.round(summaryPayableTweleveDotFive * 100) / 100);

    });

  </script>
  <tr>
    <td width="70">Vat 0%</td>

    <td class="summaryQtyZeroDisplay" width="70"></td>
    <td class="summaryGrossAmountZeroDisplay" width="70"></td>
    <td class="summaryTaxZeroDisplay" width="70"></td>
    <td class="summarySurchargeZeroDisplay" width="50"></td>
    <td class="summaryPayableZeroDisplay" width="50"></td>
  </tr>
  <tr>
    <td width="70">Vat 5%</td>
    <td class="summaryQtyFiveDisplay" width="70"></td>
    <td class="summaryGrossAmountFiveDisplay" width="70"></td>
    <td class="summaryTaxFiveDisplay" width="50"></td>
    <td class="summarySurchargeFiveDisplay" width="50"></td>
    <td class="summaryPayableFiveDisplay" width="50"></td>
  </tr>
  <tr>
    <td width="70">Vat 12.5%</td>
    <td class="summaryQtyTwelveDotFiveDisplay" width="70"></td>
    <td class="summaryGrossAmountTwelveDotFiveDisplay" width="70"></td>
    <td class="summaryTaxTwelveDotFiveDisplay" width="50"></td>
    <td class="summarySurchargeTweleveDotFiveDisplay" width="50"></td>
    <td class="summaryPayableTweleveDotFiveDisplay" width="50"></td>
  </tr>

  <script type="text/javascript">
    $(document).ready(function() {

      $('.totalSummaryQty').html(summaryQtyZero + summaryQtyFive + summaryQtyTwelveDotFive);
      $('.totalSummaryGrossAmount').html(Math.round((summaryGrossAmountZero + summaryGrossAmountFive + summaryGrossAmountTwelveDotFive) * 100) / 100);
      $('.totalSummaryTax').html(Math.round((summaryTaxZero + summaryTaxFive + summaryTaxTwelveDotFive) * 100) / 100);
      $('.totalSummarySurcharge').html(Math.round((summarySurchargeZero + summarySurchargeFive + summarySurchargeTweleveDotFive) * 100) / 100);
      $('.totalSummaryPayable').html(Math.round((summaryPayableZero + summaryPayableFive + summaryPayableTweleveDotFive) * 100) / 100);


    });
  </script>

  <tr>
    <td colspan="6"></td>
  </tr>
  <tr>
    <td><b>Total</b></td>
    <td class="totalSummaryQty"></td>
    <td class="totalSummaryGrossAmount"></td>
    <td class="totalSummaryTax"></td>
    <td class="totalSummarySurcharge"></td>
    <td class="totalSummaryPayable"></td>
  </tr>
</table>
--%>
<div>
  <table id="border_zero">
    <tr>
      <td>
        <div><b>Terms &amp; Conditions:</b><br/>
          1. Prices are inclusive of VAT/CST<br/>
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
