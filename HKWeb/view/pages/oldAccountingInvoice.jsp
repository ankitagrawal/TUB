<%@ page import="java.text.DateFormat" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
  <title>Order Invoice for Accounting</title>
  <style type="text/css">
    table {
      border-collapse: collapse;
      width: 100%
    }

    table tr td {
      padding: 5px;
      border: 1px solid #CCC;
    }

    table tr th {
      padding: 5px;
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

<s:useActionBean beanclass="com.hk.web.action.AccountingInvoiceAction" event="pre" var="orderSummary"/>

<div align="center"><h2>TAX INVOICE/RETAIL INVOICE/ CASH MEMO</h2></div>
<br>
<table class="header">
  <tr>
    <td>Prime HealthChakra Pvt. Ltd.</td>
  </tr>
  <tr>
    <td>C41C, SuperMart-1, DLF Phase-4</td>
  </tr>
  <tr>
    <td>Gurgaon, Haryana 122002</td>
  </tr>
  <tr>
    <td>TIN#06201831496</td>
  </tr>

</table>
<hr/>
<table class="header">
  <tr>
    <td><b>Invoice#</b>${orderSummary.order.accountingInvoiceNumber.id}</td>
  </tr>
  <tr>
    <td><b>Order Date:</b>
      <fmt:formatDate value="${orderSummary.order.payment.createDate}" type="both" timeStyle="short"/></td>
  </tr>
  <tr>
    <td><b>Payment Mode:</b>${orderSummary.order.payment.paymentMode.name}</td>
  </tr>
  <tr>
    <td>
      <div style="font-size:1.1em; float:left;">
        ${orderSummary.order.address.name}<br/>
        ${orderSummary.order.address.line1}<br/>
        <c:if test="${not empty orderSummary.order.address.line2}">
          ${orderSummary.order.address.line2}<br/>
        </c:if>
        ${orderSummary.order.address.city} - ${orderSummary.order.address.pin}<br/>
        ${orderSummary.order.address.state}<br/>
        Ph: ${orderSummary.order.address.phone}
      </div>
    </td>
  </tr>
</table>

<div>
  <h3>Order Details</h3>

  <table style="font-size: .8em;">
    <tr>
      <th width="150">Item</th>
      <th width="50">Qty</th>
      <th width="50">Rate</th>
      <th width="50">Tax Rate</th>
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
          <fmt:formatNumber value="${invoiceLineItem.hkPrice - hk:netDiscountOnLineItem(invoiceLineItem)/invoiceLineItem.qty}" maxFractionDigits="2"/></td>
        <td> ${invoiceLineItem.productVariant.tax.value*100}%</td>
        <td class="taxable">
          <fmt:formatNumber value="${((invoiceLineItem.hkPrice * invoiceLineItem.qty - hk:netDiscountOnLineItem(invoiceLineItem)) / (1+invoiceLineItem.productVariant.tax.value+invoiceLineItem.productVariant.tax.value*.05))}" maxFractionDigits="2"/></td>
        <td class="tax">
          <fmt:formatNumber value="${((invoiceLineItem.hkPrice * invoiceLineItem.qty - hk:netDiscountOnLineItem(invoiceLineItem)) / (1+invoiceLineItem.productVariant.tax.value+invoiceLineItem.productVariant.tax.value*.05)) * (invoiceLineItem.productVariant.tax.value)}" maxFractionDigits="2"/></td>
        <td class="surcharge">
          <fmt:formatNumber value="${((invoiceLineItem.hkPrice * invoiceLineItem.qty - hk:netDiscountOnLineItem(invoiceLineItem)) / (1+invoiceLineItem.productVariant.tax.value+invoiceLineItem.productVariant.tax.value*.05)) * (invoiceLineItem.productVariant.tax.value*.05)}" maxFractionDigits="2"/></td>
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
        $('.totalTaxable').html(Math.round(taxable*100)/100);

        var tax = 0.0;
        $('.tax').each(function() {
          tax += new Number(($(this).html()).replace(",", ""));
        });
        $('.totalTax').html(Math.round(tax*100)/100);

        var surcharge = 0.0;
        $('.surcharge').each(function() {
          surcharge += new Number(($(this).html()).replace(",", ""));
        });
        $('.totalSurcharge').html(Math.round(surcharge*100)/100);
      });
    </script>

  </table>

  <h3>Tax Summary Details</h3>
  <table style="font-size: .8em;">
    <tr>
      <th width="100">Vat Percent</th>
      <th width="70">Qty</th>
      <th width="70">Gross Amount</th>
      <th width="70">Tax on Gross Amount</th>
      <th width="70">Surcharge</th>
      <th width="125">Payable Amount(Gross+Tax+Surcharge)</th>
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
                    $('.summaryGrossAmountZeroDisplay').html(Math.round(summaryGrossAmountZero*100)/100);
                    $('.summaryTaxZeroDisplay').html(Math.round(summaryTaxZero*100)/100);
                    $('.summarySurchargeZeroDisplay').html(Math.round(summarySurchargeZero*100)/100);
                    $('.summaryPayableZeroDisplay').html(Math.round(summaryPayableZero*100)/100);

                    $('.summaryQtyFiveDisplay').html(summaryQtyFive);
                    $('.summaryGrossAmountFiveDisplay').html(Math.round(summaryGrossAmountFive*100)/100);
                    $('.summaryTaxFiveDisplay').html(Math.round(summaryTaxFive*100)/100);
                    $('.summarySurchargeFiveDisplay').html(Math.round(summarySurchargeFive*100)/100);
                    $('.summaryPayableFiveDisplay').html(Math.round(summaryPayableFive*100)/100);

                    $('.summaryQtyTwelveDotFiveDisplay').html(summaryQtyTwelveDotFive);
                    $('.summaryGrossAmountTwelveDotFiveDisplay').html(Math.round(summaryGrossAmountTwelveDotFive*100)/100);
                    $('.summaryTaxTwelveDotFiveDisplay').html(Math.round(summaryTaxTwelveDotFive*100)/100);
                    $('.summarySurchargeTweleveDotFiveDisplay').html(Math.round(summarySurchargeTweleveDotFive*100)/100);
                    $('.summaryPayableTweleveDotFiveDisplay').html(Math.round(summaryPayableTweleveDotFive*100)/100);

               });

    </script>
    <tr>
      <td width="70">Vat 0%</td>

      <td class="summaryQtyZeroDisplay"width="70"></td>
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

        $('.totalSummaryQty').html(summaryQtyZero+summaryQtyFive+summaryQtyTwelveDotFive);
        $('.totalSummaryGrossAmount').html(Math.round((summaryGrossAmountZero+summaryGrossAmountFive+summaryGrossAmountTwelveDotFive)*100)/100);
        $('.totalSummaryTax').html(Math.round((summaryTaxZero+summaryTaxFive+summaryTaxTwelveDotFive)*100)/100);
        $('.totalSummarySurcharge').html(Math.round((summarySurchargeZero+summarySurchargeFive+summarySurchargeTweleveDotFive)*100)/100);
        $('.totalSummaryPayable').html(Math.round((summaryPayableZero +summaryPayableFive+summaryPayableTweleveDotFive)*100)/100);


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
</div>

</body>
</html>