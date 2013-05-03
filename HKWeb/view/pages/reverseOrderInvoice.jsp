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
<c:set var="shippingOrder" value="${orderSummary.shippingOrder}"/>
<c:set var="reverseOrder" value="${orderSummary.reverseOrder}"/>
<c:set var="baseOrder" value="${shippingOrder.baseOrder}"/>
<c:set var="address" value="${baseOrder.address}"/>
<%--<c:set var="warehouse" value="${shippingOrder.warehouse}"/>--%>
<c:set var="warehouse" value="${hk:getShippingWarehouse(shippingOrder)}"/>
<c:set var="isB2BOrder" value="${baseOrder.b2bOrder}"/>
<c:set var="b2bUserDetails" value="${orderSummary.b2bUserDetails}"/>
<div class="container_12">
<div class="grid_12" style="text-align: center;">
  <h4>
    <c:choose>
      <c:when test="${isB2BOrder}">
           TAX INVOICE
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
			<%--<c:choose>
				<c:when test="${isB2BOrder}">
					<p>Bright Lifecare Pvt. Ltd.</p>
				</c:when>
				<c:otherwise>
					<p>Aquamarine HealthCare Pvt. Ltd.</p>
				</c:otherwise>
			</c:choose>--%>
      <p>${warehouse.name}</p>
			<p>${warehouse.line1}</p>
			<p>${warehouse.line2}</p>
			<p>${warehouse.city}, ${warehouse.state} - ${warehouse.pincode}</p>
			<%--<c:choose>
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
			</c:choose>--%>
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
        Date: </strong><fmt:formatDate value="${reverseOrder.courierPickupDetail.pickupDate}" type="both"

                                       timeStyle="short"/></p>

      <p><strong>Payment Mode:</strong>${baseOrder.payment.paymentMode.name}</p>

      <p><strong>Courier:</strong>${reverseOrder.courierPickupDetail.courier.name}</p>

      <p><strong>Reverse Order#: </strong>${reverseOrder.id} on <fmt:formatDate
          value="${reverseOrder.createDate}" type="both" timeStyle="short"/></p>
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
    <c:forEach items="${orderSummary.reverseOrderInvoiceDto.invoiceLineItemDtos}" var="invoiceLineItem">
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
      <td><fmt:formatNumber value="${orderSummary.reverseOrderInvoiceDto.totalTaxable}" maxFractionDigits="2"/></td>
      <td><fmt:formatNumber value="${orderSummary.reverseOrderInvoiceDto.totalTax}" maxFractionDigits="2"/></td>
      <td><fmt:formatNumber value="${orderSummary.reverseOrderInvoiceDto.totalSurcharge}" maxFractionDigits="2"/></td>

    </tr>
  </table>

  <div class="clear"></div>
  <div style="margin-top:10px"></div>

  <table>
    <tr>
      <td width="70%"><strong>Shipping Cost</strong></td>
      <td width="20%"><fmt:formatNumber value="${orderSummary.reverseOrderInvoiceDto.shipping}" maxFractionDigits="2"/></td>

    </tr>

    <tr>
      <td width="70%"><strong>Cod Cost</strong></td>
      <td width="20%"><fmt:formatNumber value="${orderSummary.reverseOrderInvoiceDto.cod}" maxFractionDigits="2"/></td>

    </tr>
    <tr>
      <td width="70%"><strong>Grand Total</strong></td>
      <td width="20%"><fmt:formatNumber value="${orderSummary.reverseOrderInvoiceDto.grandTotal}" maxFractionDigits="2"/></td>
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
      <c:if test="${orderSummary.reverseOrderInvoiceDto.summaryQtyMap[taxValue.name] != 0}">

        <tr>
          <td>${taxValue.name}</td>
          <td>${orderSummary.reverseOrderInvoiceDto.summaryQtyMap[taxValue.name]}</td>
          <td><fmt:formatNumber value="${orderSummary.reverseOrderInvoiceDto.summaryAmountMap[taxValue.name]}"
                                maxFractionDigits="2"/></td>
          <td><fmt:formatNumber value="${orderSummary.reverseOrderInvoiceDto.summaryTaxMap[taxValue.name]}"
                                maxFractionDigits="2"/></td>
          <td><fmt:formatNumber value="${orderSummary.reverseOrderInvoiceDto.summarySurchargeMap[taxValue.name]}"
                                maxFractionDigits="2"/></td>
          <td><fmt:formatNumber value="${orderSummary.reverseOrderInvoiceDto.summaryPayableMap[taxValue.name]}"
                                maxFractionDigits="2"/></td>
        </tr>

      </c:if>
    </c:forEach>

    <tr>
      <td colspan="6"></td>
    </tr>
    <tr>
      <td><strong>Total</strong></td>
      <td>${orderSummary.reverseOrderInvoiceDto.totalSummaryQty}</td>
      <td><fmt:formatNumber value="${orderSummary.reverseOrderInvoiceDto.totalSummaryAmount}" maxFractionDigits="2"/></td>
      <td><fmt:formatNumber value="${orderSummary.reverseOrderInvoiceDto.totalSummaryTax}" maxFractionDigits="2"/></td>
      <td><fmt:formatNumber value="${orderSummary.reverseOrderInvoiceDto.totalSummarySurcharge}" maxFractionDigits="2"/></td>
      <td><Strong><fmt:formatNumber value="${orderSummary.reverseOrderInvoiceDto.totalSummaryPayable}"
                                    maxFractionDigits="2"/></Strong></td>
    </tr>
  </table>
</div>

<div class="clear"></div>
<div style="margin-top: 15px;"></div>

<div class="grid_12">
  <p><strong>Terms &amp; Conditions:</strong></p>

  <p>1. All disputes are subject to ${warehouse.city} Jurisdiction.</p>
  <c:if test="${orderSummary.reverseOrderInvoiceDto.b2bUserDetails != null}">
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