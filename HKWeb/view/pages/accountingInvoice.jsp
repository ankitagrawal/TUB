<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<link href="<hk:vhostCss/>/css/960.css" rel="stylesheet" type="text/css"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
  <title>RETAIL INVOICE/ CASH MEMO/ TAX INVOICE</title>
  <script type="text/javascript" src="<hk:vhostJs/>/js/jquery-1.6.2.min.js"></script>
  <script type="text/javascript" src="<hk:vhostJs/>/js/jquery.hkCommonPlugins.js"></script>
</head>
<body>
<s:useActionBean beanclass="com.hk.web.action.core.accounting.AccountingInvoiceAction" event="pre" var="orderSummary"/>

<div class="container_12">
<div class="grid_12" style="text-align: center;">
  <h4>
    <c:choose>
      <c:when test="${orderSummary.invoiceDto.b2bUserDetails != null}">
        <c:choose>
          <c:when
              test="${fn:toUpperCase(orderSummary.shippingOrder.baseOrder.address.state) eq('HARYANA') && orderSummary.invoiceDto.b2bUserDetails.tin != null }">
            TAX INVOICE
          </c:when>
          <c:when
              test="${fn:toUpperCase(orderSummary.shippingOrder.baseOrder.address.state) eq('MAHARASHTRA') && orderSummary.invoiceDto.b2bUserDetails.tin != null}">
            TAX INVOICE
          </c:when>
          <c:otherwise>
            RETAIL INVOICE
          </c:otherwise>
        </c:choose>
      </c:when>
      <c:otherwise>
        RETAIL INVOICE
      </c:otherwise>
    </c:choose>
  </h4>
</div>

<div class="clear"></div>

<div class="grid_12" style="border: 1px black solid;">
	<div class="grid_4 alpha omega">
		<div class="column">
			<c:set var="warehouse" value="${orderSummary.shippingOrder.warehouse}"/>
			<c:set var="isB2BOrder" value="${orderSummary.shippingOrder.baseOrder.b2bOrder}"/>
			<c:choose>
				<c:when test="${isB2BOrder}">
					<p>Bright Lifecare Pvt. Ltd.</p>
				</c:when>
				<c:otherwise>
					<p>Aquamarine HealthCare Pvt. Ltd.</p>
				</c:otherwise>
			</c:choose>
			<p>${warehouse.line1}</p>
			<p>${warehouse.line2}</p>
			<p>${warehouse.city}, ${warehouse.state} - ${warehouse.pincode}</p>
			<c:choose>
				<c:when test="${isB2BOrder}">
					<c:choose>
						<c:when test="${orderSummary.invoiceDto.warehouseState == 'HARYANA'}">
							<p> TIN# 06101832036</p>
						</c:when>
						<c:when test="${orderSummary.invoiceDto.warehouseState == 'MAHARASHTRA'}">
							<p> TIN# 27210893736</p>
						</c:when>
					</c:choose>
					<p>D.L.No. <br/>HR-6600-219-OW(H), HR-6600-219-W(H)</p>
				</c:when>
				<c:otherwise>
					TIN# ${warehouse.tin}
				</c:otherwise>
			</c:choose>
		</div>
	</div>

  <div class="grid_4 alpha omega">
    <div class="column" style="border-right: 1px black solid; border-left: 1px black solid;">
      <p>
        <strong>Invoice#: </strong>${orderSummary.invoiceDto.invoiceType}-${orderSummary.shippingOrder.accountingInvoiceNumber}
      </p>

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
      <c:if test="${orderSummary.invoiceDto.b2bUserDetails != null}">
        <c:if test="${orderSummary.invoiceDto.b2bUserDetails.tin != null}">
          <p>TIN- ${orderSummary.invoiceDto.b2bUserDetails.tin}</p>
        </c:if>

        <p>DL Number- ${orderSummary.invoiceDto.b2bUserDetails.dlNumber}</p>
      </c:if>
    </div>
  </div>
</div>

<div class="clear"></div>
<div style="margin-top: 20px;"></div>

<div class="grid_12">
  <table cellspacing="0">
    <tr>
      <th>Item</th>
      <th>Qty</th>
      <th>Rate (per unit)</th>
      <th>Tax Rate</th>
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
              value="${invoiceLineItem.rate}"
              maxFractionDigits="2"/>
        </td>

        <td> ${invoiceLineItem.taxRate}</td>
        <td>
          <fmt:formatNumber
              value="${invoiceLineItem.taxable}"
              maxFractionDigits="2"/></td>
        <td>
          <fmt:formatNumber
              value="${invoiceLineItem.tax}"
              maxFractionDigits="2"/></td>
          <td>
            <fmt:formatNumber
                value="${invoiceLineItem.surcharge}"
                maxFractionDigits="2"/></td>
      </tr>
    </c:forEach>
    <tr>
      <td colspan="4"><b>Total</b></td>
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
    <tr>
      <td width="70%"><strong>Grand Total</strong></td>
      <td width="20%"><fmt:formatNumber value="${orderSummary.invoiceDto.grandTotal}" maxFractionDigits="2"/></td>
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
      <th width="17%">VAT Percent</th>
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
  </table>
</div>

<div class="clear"></div>
<div style="margin-top: 15px;"></div>

<div class="grid_12">
  <p><strong>Terms &amp; Conditions:</strong></p>

  <p>1. All disputes are subject to Gurgaon Jurisdiction.</p>
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