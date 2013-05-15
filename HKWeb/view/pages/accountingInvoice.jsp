<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<link href="<hk:vhostCss/>/css/960.css" rel="stylesheet" type="text/css"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
  <title>RETAIL INVOICE/ CASH MEMO/ TAX INVOICE</title>
  <script type="text/javascript" src="<hk:vhostJs/>/js/jquery-1.7.2.min.js"></script>
  <script type="text/javascript" src="<hk:vhostJs/>/js/jquery.hkCommonPlugins.js"></script>
</head>
<body>
<s:useActionBean beanclass="com.hk.web.action.core.accounting.AccountingInvoiceAction" event="pre" var="orderSummary"/>
<c:set var="shippingOrder" value="${orderSummary.shippingOrder}"/>
<c:set var="cFormAvailable" value="${orderSummary.CFormAvailable}"/>
<c:set var="baseOrder" value="${shippingOrder.baseOrder}"/>
<c:set var="address" value="${baseOrder.address}"/>
<c:set var="warehouse" value="${hk:getShippingWarehouse(shippingOrder)}"/>
<c:set var="isB2BOrder" value="${baseOrder.b2bOrder}"/>
<c:set var="b2bUserDetails" value="${orderSummary.b2bUserDetails}"/>
<div class="container_12">
<div class="grid_12" style="text-align: center;">
  <h4>
    <c:choose>
      <c:when test="${!isB2BOrder||orderSummary.invoiceDto.warehouseState == 'MUMBAI'}">
           RETAIL INVOICE            
      </c:when>
      <c:otherwise>
      <c:choose>
      <c:when test="${isB2BOrder&&(fn:toLowerCase(orderSummary.invoiceDto.warehouseState) ne fn:toLowerCase(address.state))}">
           TAX INVOICE            
      </c:when>
      <c:otherwise>
      RETAIL INVOICE
      </c:otherwise>
      </c:choose>
      </c:otherwise>
    </c:choose>
  </h4>
</div>
<c:set var="taxDiscount" scope="page" value="${0}"></c:set>
<div class="clear"></div>

<div class="grid_12" style="border: 1px black solid;">
	<div class="grid_4 alpha omega">
		<div class="column">			
      <p>${warehouse.name}</p>
			<p>${warehouse.line1}</p>
			<p>${warehouse.line2}</p>
			<p>${warehouse.city}, ${warehouse.state} - ${warehouse.pincode}</p>
      <p>TIN: ${warehouse.tin}</p>
      <c:if test="${isB2BOrder && warehouse.state == 'HARYANA'}">
        <p>D.L.No. <br/>HR-6600-219-OW(H), HR-6600-219-W(H)</p>
      </c:if>
		</div>
	</div>

  <div class="grid_4 alpha omega">
    <div class="column" style="border-right: 1px black solid; border-left: 1px black solid;">
      <p>
        <strong>Invoice#: </strong>${shippingOrder.accountingInvoiceNumber}
      </p>

      <p><strong>Invoice
        Date: </strong><fmt:formatDate value="${shippingOrder.shipment.shipDate}" type="both"

                                       timeStyle="short"/></p>

      <p><strong>Payment Mode:</strong>${baseOrder.payment.paymentMode.name}</p>

      <p><strong>Courier:</strong>${shippingOrder.shipment.awb.courier.name}</p>

      <p><strong>Order#: </strong>${shippingOrder.gatewayOrderId} on <fmt:formatDate
          value="${baseOrder.payment.createDate}" type="both" timeStyle="short"/></p>
    </div>
  </div>

  <div class="grid_4 alpha omega" style="width: 330px;">
    <div class="column">
      <p><strong>Shipped To:</strong><p>

	  <p>${address.name}</p>

      <p>${hk:escapeHtml(address.line1)}
        <c:if test="${not empty address.line2}">
          ,${hk:escapeHtml(address.line2)}
        </c:if>
      </p>

      <p>
        ${address.city}, ${hk:escapeHtml(address.state)}
        - ${address.pincode.pincode},
      </p>

      <p>Ph: ${address.phone}</p>
	    <c:if test="${isB2BOrder}">
		    <p><strong>Consignee:</strong><p>
		    <p>${baseOrder.user.name}</p>
		    <c:if test="${b2bUserDetails != null}">			    
			    <c:if test="${b2bUserDetails.tin != null}">
				    <p>TIN- ${b2bUserDetails.tin}</p>
			    </c:if>
			    <c:if test="${b2bUserDetails.dlNumber != null}">
				    <p>DL Number- ${b2bUserDetails.dlNumber}</p>
			    </c:if>
		    </c:if>
	    </c:if>
    </div>
  </div>
</div>

<div class="clear"></div>
<div style="margin-top: 20px;"></div>

<div class="grid_12">
  <table cellspacing="0" id="mainTable">
    <tr>
      <th>Item</th>
      <th>Qty</th>
      <th>MRP</th>
      <th>Rate (per unit)</th>
      <th>
      <c:choose>
      <c:when test="${isB2BOrder&&cFormAvailable}">
        Tax Rate (CST)
        </c:when>
        <c:otherwise>
        Tax Rate
        </c:otherwise>
      </c:choose>
    </th>
      <th>Taxable</th>
      <th>Tax</th>
      <th>Surcharge</th>

    </tr>
    <c:forEach items="${orderSummary.invoiceDto.invoiceLineItemDtos}" var="invoiceLineItem">
      <tr>
        <td>${hk:escapeHtml(invoiceLineItem.productName)}
          <br/>
          <c:if test="${invoiceLineItem.variantName != null}">
            <p>${invoiceLineItem.variantName}</p>
          </c:if>
          <em>
            <p>${invoiceLineItem.productOptionsPipeSeparated} </p>

            <p> ${invoiceLineItem.extraOptionsPipeSeparated} </p>

            <p> ${invoiceLineItem.configOptionsPipeSeparated} </p>
          </em>

        </td>

        <td><fmt:formatNumber value="${invoiceLineItem.qty}" maxFractionDigits="0"/></td>

        <td>
          <fmt:formatNumber
              value="${invoiceLineItem.markedPrice}"
              maxFractionDigits="2"/>
        </td>

        <td>
          <fmt:formatNumber
              value="${invoiceLineItem.rate}"
              maxFractionDigits="2"/>
        </td>

        <td> ${invoiceLineItem.taxRate}</td>
        <td class="taxAmount">
          <fmt:formatNumber
              value="${invoiceLineItem.taxable}"
              maxFractionDigits="2"/></td>
        <td class="taxAmount">
          <fmt:formatNumber
              value="${invoiceLineItem.tax}"
              maxFractionDigits="2"/></td>
          <td>
            <fmt:formatNumber
                value="${invoiceLineItem.surcharge}"
                maxFractionDigits="2"/></td>
                
      </tr>
      <c:if test="${invoiceLineItem.taxable==0 }">
      <c:set var="taxDiscount" value="${taxDiscount+invoiceLineItem.tax }" ></c:set>
      </c:if>
    </c:forEach>
    <tr>
      <td colspan="5"><b>Total</b></td>
      <td><fmt:formatNumber value="${orderSummary.invoiceDto.totalTaxable}" maxFractionDigits="2"/></td>
      <td><fmt:formatNumber value="${orderSummary.invoiceDto.totalTax}" maxFractionDigits="2"/></td>
      <td><fmt:formatNumber value="${orderSummary.invoiceDto.totalSurcharge}" maxFractionDigits="2"/></td>

    </tr>
  </table>

  <div class="clear"></div>
  <div style="margin-top:10px"></div>

  <table>
    <tr>
      <td width="70%"><strong>Shipping Cost</strong></td>
      <td width="20%"><fmt:formatNumber value="${orderSummary.invoiceDto.shipping}" maxFractionDigits="2"/></td>

    </tr>

    <tr>
      <td width="70%"><strong>Cod Cost</strong></td>
      <td width="20%"><fmt:formatNumber value="${orderSummary.invoiceDto.cod}" maxFractionDigits="2"/></td>

    </tr>
    <c:if test="${isB2BOrder && taxDiscount>0}"><tr>
    <td width="70%"><strong>Tax Discount</strong></td>
      <td width="20%">
      (-)${taxDiscount}
      </td>
    </tr></c:if>
    
    <tr>
      <td width="70%"><strong>Grand Total</strong></td>
      <%-- <td width="20%"><fmt:formatNumber value="${orderSummary.invoiceDto.grandTotal}" maxFractionDigits="2"/></td> --%>
      <c:if test="${!isB2BOrder }"><td width="20%"><fmt:formatNumber value="${orderSummary.invoiceDto.totalTaxable+orderSummary.invoiceDto.totalTax+orderSummary.invoiceDto.totalSurcharge}" maxFractionDigits="2"/></td></c:if>
      <c:if test="${isB2BOrder}"><td width="20%"><fmt:formatNumber value="${orderSummary.invoiceDto.totalTaxable+orderSummary.invoiceDto.totalTax+orderSummary.invoiceDto.totalSurcharge-taxDiscount}" maxFractionDigits="2"/></td></c:if>
    </tr>
  </table>

</div>

<div class="clear"></div>
<div style="margin-top: 40px;"></div>

<div class="grid_12">
  <table cellspacing="0">
    <tr>
      <td colspan="6">
        <div align="center"><strong>Tax Summary</strong></div>
      </td>
    </tr>
    <tr>
      <th width="17%">
      <c:choose>
      <c:when test="${isB2BOrder&&cFormAvailable}">
        CST
        </c:when>
        <c:otherwise>
        VAT
        </c:otherwise>
      </c:choose>
    </th>
      <th width="5%">Qty</th>
      <th width="10%">Amount</th>
      <th width="16%">Tax on Amount</th>
      <th width="10%">Surcharge</th>
      <th width="39%">Gross Amount(Amt+Tax+Surcharge)</th>
    </tr>

    <c:forEach items="${orderSummary.enumTaxes}" var="taxValue">
      <c:if test="${orderSummary.invoiceDto.summaryQtyMap[taxValue.name] != 0}">

        <tr>
          <td>${taxValue.name}</td>
          <td>${orderSummary.invoiceDto.summaryQtyMap[taxValue.name]}</td>
          <td><fmt:formatNumber value="${orderSummary.invoiceDto.summaryAmountMap[taxValue.name]}"
                                maxFractionDigits="2"/></td>
          <td><fmt:formatNumber value="${orderSummary.invoiceDto.summaryTaxMap[taxValue.name]}"
                                maxFractionDigits="2"/></td>
          <td><fmt:formatNumber value="${orderSummary.invoiceDto.summarySurchargeMap[taxValue.name]}"
                                maxFractionDigits="2"/></td>
          <td><fmt:formatNumber value="${orderSummary.invoiceDto.summaryPayableMap[taxValue.name]}"
                                maxFractionDigits="2"/></td>
        </tr>

      </c:if>
    </c:forEach>

    <tr>
      <td colspan="6"></td>
    </tr>
     <tr>
      <td><strong>Total</strong></td>
      <td>${orderSummary.invoiceDto.totalSummaryQty}</td>
      <td><fmt:formatNumber value="${orderSummary.invoiceDto.totalSummaryAmount}" maxFractionDigits="2"/></td>
      <td><fmt:formatNumber value="${orderSummary.invoiceDto.totalSummaryTax}" maxFractionDigits="2"/></td>
      <td><fmt:formatNumber value="${orderSummary.invoiceDto.totalSummarySurcharge}" maxFractionDigits="2"/></td>
      <td><Strong><fmt:formatNumber value="${orderSummary.invoiceDto.totalSummaryPayable}"
                                    maxFractionDigits="2"/></Strong></td>
      
    </tr>
    <c:if test="${isB2BOrder && taxDiscount>0}">
     <tr>
      <td colspan="5"><strong>Discount On Tax(if any)</strong></td>
      <td><Strong>(-)<fmt:formatNumber value="${taxDiscount }"
                                    maxFractionDigits="2"/></Strong></td>
      </tr>
      <tr>
      <td colspan="5"><strong>Grand Total</strong></td>
      <td><Strong><fmt:formatNumber value="${orderSummary.invoiceDto.totalSummaryPayable-taxDiscount}"
                                    maxFractionDigits="2"/></Strong></td>
      </c:if>
   
  </table>
</div>

<div class="clear"></div>
<div style="margin-top: 15px;"></div>

<div class="grid_12">
  <p><strong>Terms &amp; Conditions:</strong></p>

  <p>1. All disputes are subject to ${warehouse.city} Jurisdiction.</p>
  <c:if test="${orderSummary.invoiceDto.b2bUserDetails != null}">
    <p>2. This is computer generated invoice</p>
  </c:if>
  <p style="display:inline;float:right;"><strong>(Authorised Signatory)</strong></p>
</div>

<div class="clear"></div>

</div>
</body>
</html>
<style type="text/css">
  table {
    width: 100%;
    font-size: .8em;
    border-width: 0 0 1px 1px;
    border-style: solid;
    border-collapse: separate;
    border-color: black;
  }

  table tr td {
    text-align: left;
    font-size: small;
    border-width: 1px 1px 0 0;
    border-style: solid;
    border-color: black;
  }

  table tr th {
    border-width: 1px 1px 0 0;
    border-style: solid;
    border-color: black;
    text-align: left;
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