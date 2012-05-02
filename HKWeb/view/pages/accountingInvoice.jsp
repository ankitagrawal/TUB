<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<link href="<hk:vhostCss/>/css/960.css" rel="stylesheet" type="text/css"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
  <title>RETAIL INVOICE/ CASH MEMO</title>
  <style type="text/css">
    table {
      border-collapse: collapse;
      width: 75%
    }

    table tr td {
      padding: 2px;
      border: 1px black solid;
    }

    table#border_zero tr td {
      padding: 2px;
      border: 0;
    }

    table tr th {
      padding: 2px;
      border: 1px black solid;
      text-align: left;
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

    p {
      margin-top: 2px;
      margin-bottom: 2px;
      margin-left: 2px;
    }

    .column {
      padding: 2px;
    }
  </style>
  <script type="text/javascript" src="<hk:vhostJs/>/js/jquery-1.6.2.min.js"></script>
  <script type="text/javascript" src="<hk:vhostJs/>/js/jquery.hkCommonPlugins.js"></script>
</head>
<body>
<%--
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
--%>

<s:useActionBean beanclass="web.action.AccountingInvoiceAction" event="pre" var="orderSummary"/>

<div class="container_12">
<div class="grid_12" style="text-align: center;"><h4>RETAIL INVOICE/ CASH MEMO</h4></div>

<div class="clear"></div>

<div class="grid_12" style="border: 1px black solid;">
  <div class="grid_4 alpha omega">
    <div class="column">
      <p>Aquamarine HealthCare Pvt. Ltd.</p>

      <p>3rd Floor, Parshavnath Arcadia,</p>

      <p>1, MG Road, Gurgaon, Haryana 122001</p>

      <p>TIN#06101832327 </p>
    </div>
  </div>

  <div class="grid_4 alpha omega">
    <div class="column" style="border-right: 1px black solid; border-left: 1px black solid;">
      <p><strong>Invoice#: </strong>${orderSummary.shippingOrder.accountingInvoiceNumber}</p>

      <p><strong>Invoice
        Date: </strong><fmt:formatDate value="${orderSummary.shippingOrder.shipment.shipDate}" type="both"
                                       timeStyle="short"/></p>

      <p><strong>Payment Mode:</strong>${orderSummary.shippingOrder.baseOrder.payment.paymentMode.name}</p>

      <p><strong>Courier:</strong>${orderSummary.shippingOrder.shipment.courier.name}</p>

      <p><strong>Order#: </strong>${orderSummary.shippingOrder.gatewayOrderId} on <fmt:formatDate
          value="${orderSummary.shippingOrder.baseOrder.payment.createDate}" type="both" timeStyle="short"/></p>
    </div>
  </div>

  <div class="grid_4 alpha omega" style="width: 330px;">
    <div class="column">
      <p><strong>Customer:</strong>
        ${orderSummary.shippingOrder.baseOrder.address.name}</p>

      <p>${hk:escapeHtml(orderSummary.shippingOrder.baseOrder.address.line1)}
        <c:if test="${not empty orderSummary.shippingOrder.baseOrder.address.line2}">
          ,${hk:escapeHtml(orderSummary.shippingOrder.baseOrder.address.line2)}
        </c:if>
      </p>

      <p>
        ${orderSummary.shippingOrder.baseOrder.address.city}, ${hk:escapeHtml(orderSummary.shippingOrder.baseOrder.address.state)}
        - ${orderSummary.shippingOrder.baseOrder.address.pin},
      </p>

      <p>Ph: ${orderSummary.shippingOrder.baseOrder.address.phone}     </p>
    </div>
  </div>
</div>

<div class="clear"></div>
<div style="margin-top: 15px;"></div>

<div class="grid_12">
  <table style="font-size: .8em; width: 100%">
    <tr>
      <th width="300">Item</th>
      <th width="20">Qty</th>
      <th width="50">Rate</th>
      <th width="55">Tax Rate</th>
      <th width="50">Taxable</th>
      <th width="50">Tax</th>
      <th width="50">Surcharge</th>
    </tr>
    <c:forEach items="${orderSummary.invoiceDto.invoiceLineItemDtos}" var="invoiceLineItem">
      <tr>
        <td>${hk:escapeHtml(invoiceLineItem.productLineItem.sku.productVariant.product.name)}
          <em>
            <c:forEach items="${invoiceLineItem.productLineItem.sku.productVariant.productOptions}" var="productOption">
              ${productOption.name} ${hk:escapeHtml(productOption.value)}
            </c:forEach>
          </em>
        </td>

        <td><fmt:formatNumber value="${invoiceLineItem.qty}" maxFractionDigits="0"/></td>

        <td>
          <fmt:formatNumber
              value="${invoiceLineItem.rate}"
              maxFractionDigits="2"/>
        </td>

        <td> ${invoiceLineItem.taxRate}</td>
        <td class="taxable">
          <fmt:formatNumber
              value="${invoiceLineItem.taxable}"
              maxFractionDigits="2"/></td>
        <td class="tax">
          <fmt:formatNumber
              value="${invoiceLineItem.tax}"
              maxFractionDigits="2"/></td>
        <td class="surcharge">
          <fmt:formatNumber
              value="${invoiceLineItem.surcharge}"
              maxFractionDigits="2"/></td>
      </tr>
    </c:forEach>
    <tr>
      <td colspan="7"></td>
    </tr>
    <tr>
      <td colspan="4"><b>Total</b></td>
      <td class="totalTaxable"></td>
      <td class="totalTax"></td>
      <td class="totalSurcharge"></td>
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
      });
    </script>

  </table>
</div>

<div class="clear"></div>
<div style="margin-top: 15px;"></div>

<div class="grid_12">
  <table style="font-size: .8em; width: 100%">
    <tr>
      <td colspan="6">
        <div align="center"><strong>Tax Summary</strong></div>
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

    <c:forEach var="invoiceLineItem" items="${orderSummary.pricingDto.productLineItems}">
      <c:if test="${(invoiceLineItem.productVariant.tax.value*100) == 0}">
        <script type="text/javascript">
          summaryQtyZero += ${invoiceLineItem.qty};
          summaryGrossAmountZero += ${((invoiceLineItem.hkPrice * invoiceLineItem.qty - hk:netDiscountOnLineItem(invoiceLineItem)) / (1+invoiceLineItem.productVariant.tax.value+invoiceLineItem.productVariant.tax.value*.05))};
          summaryTaxZero += ${((invoiceLineItem.hkPrice * invoiceLineItem.qty - hk:netDiscountOnLineItem(invoiceLineItem)) / (1+invoiceLineItem.productVariant.tax.value+invoiceLineItem.productVariant.tax.value*.05)) * (invoiceLineItem.productVariant.tax.value)};
          summarySurchargeZero += ${((invoiceLineItem.hkPrice * invoiceLineItem.qty - hk:netDiscountOnLineItem(invoiceLineItem)) / (1+invoiceLineItem.productVariant.tax.value+invoiceLineItem.productVariant.tax.value*.05)) * (invoiceLineItem.productVariant.tax.value*.05)};
          summaryPayableZero = summaryGrossAmountZero + summaryTaxZero + summarySurchargeZero;
        </script>
      </c:if>
      <c:if test="${(invoiceLineItem.productVariant.tax.value*100) == 5}">
        <script type="text/javascript">
          summaryQtyFive += ${invoiceLineItem.qty};
          summaryGrossAmountFive += ${((invoiceLineItem.hkPrice * invoiceLineItem.qty - hk:netDiscountOnLineItem(invoiceLineItem)) / (1+invoiceLineItem.productVariant.tax.value+invoiceLineItem.productVariant.tax.value*.05))};
          summaryTaxFive += ${((invoiceLineItem.hkPrice * invoiceLineItem.qty - hk:netDiscountOnLineItem(invoiceLineItem)) / (1+invoiceLineItem.productVariant.tax.value+invoiceLineItem.productVariant.tax.value*.05)) * (invoiceLineItem.productVariant.tax.value)};
          summarySurchargeFive += ${((invoiceLineItem.hkPrice * invoiceLineItem.qty - hk:netDiscountOnLineItem(invoiceLineItem)) / (1+invoiceLineItem.productVariant.tax.value+invoiceLineItem.productVariant.tax.value*.05)) * (invoiceLineItem.productVariant.tax.value*.05)};
          summaryPayableFive = summaryGrossAmountFive + summaryTaxFive + summarySurchargeFive;
        </script>
      </c:if>
      <c:if test="${(invoiceLineItem.productVariant.tax.value*100) == 12.5}">
        <script type="text/javascript">
          summaryQtyTwelveDotFive += ${invoiceLineItem.qty};
          summaryGrossAmountTwelveDotFive += ${((invoiceLineItem.hkPrice * invoiceLineItem.qty - hk:netDiscountOnLineItem(invoiceLineItem)) / (1+invoiceLineItem.productVariant.tax.value+invoiceLineItem.productVariant.tax.value*.05))};
          summaryTaxTwelveDotFive += ${((invoiceLineItem.hkPrice * invoiceLineItem.qty - hk:netDiscountOnLineItem(invoiceLineItem)) / (1+invoiceLineItem.productVariant.tax.value+invoiceLineItem.productVariant.tax.value*.05)) * (invoiceLineItem.productVariant.tax.value)};
          summarySurchargeTweleveDotFive += ${((invoiceLineItem.hkPrice * invoiceLineItem.qty - hk:netDiscountOnLineItem(invoiceLineItem)) / (1+invoiceLineItem.productVariant.tax.value+invoiceLineItem.productVariant.tax.value*.05)) * (invoiceLineItem.productVariant.tax.value*.05)};
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
      <td><strong>Total</strong></td>
      <td class="totalSummaryQty"></td>
      <td class="totalSummaryGrossAmount"></td>
      <td class="totalSummaryTax"></td>
      <td class="totalSummarySurcharge"></td>
      <td class="totalSummaryPayable"></td>
    </tr>
  </table>
</div>

<div class="clear"></div>
<div style="margin-top: 15px;"></div>

<div class="grid_12">
  <p><strong>Terms &amp; Conditions:</strong></p>

  <p>1. All disputes are subject to Gurgaon Jurisdiction.</p>

  <p style="display:inline;float:right;"><strong>(Authorised Signatory)</strong></p>
</div>
<div class="clear"></div>
</div>
</body>
</html>