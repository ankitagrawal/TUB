<%@ page import="java.text.DateFormat" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
  <title>Purchase Order</title>
  <style type="text/css">
    table {
      border-collapse: collapse;
      width: 100%;
      font-size:.8em;
    }

    table tr td {
      padding: 1px;
      border: 1px solid #CCC;
    }

    table tr th {
      padding: 1px;
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
      vertical-align: top;
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
</head>
<body>

<s:useActionBean beanclass="com.hk.web.action.admin.inventory.POAction" var="orderSummary"/>
<table class="header">
  <tr>
    <td>
      <c:set var="warehouse" value="${orderSummary.purchaseOrder.warehouse}"/>
      ${warehouse.name}<br/>
      ${warehouse.line1}<br/>
      ${warehouse.line2}<br/>
      ${warehouse.city} &nbsp;
      -${warehouse.pincode} <br/>
      ${warehouse.state}<br/>

      <p> TIN# ${warehouse.tin}</p>
    </td>
    <td align="right">
      <img src="${pageContext.request.contextPath}/images/logo.png" alt=""/>
    </td>
  </tr>
</table>

<h3 align="center">PURCHASE ORDER</h3>
<br/>
<table style="font-size:.9em">
  <tr>
    <td><b>Supplier</b></td>
    <td>${orderSummary.purchaseOrder.supplier.name}</td>
    <td><b>PO Place Date</b></td>
    <td><fmt:formatDate value="${orderSummary.purchaseOrder.poPlaceDate}" pattern="dd-MMM-yyyy"/></td>
  </tr>
  <tr valign="top">
    <td><b>Address</b></td>
    <td>${orderSummary.purchaseOrder.supplier.line1}<br/>${orderSummary.purchaseOrder.supplier.line2}<br/>${orderSummary.purchaseOrder.supplier.city}<br/>${orderSummary.purchaseOrder.supplier.state}
    </td>
    <td><b>PO#</b></td>
    <td>${orderSummary.purchaseOrder.id}</td>
  </tr>
  <tr>
    <td><b>Contact Name</b></td>
    <td>${orderSummary.purchaseOrder.supplier.contactPerson}</td>
    <td><b>Contact Number</b></td>
    <td>${orderSummary.purchaseOrder.supplier.contactNumber}</td>
  </tr>
</table>

<br/>

<table border="1">
      <thead>
      <tr>
        <th>S.No.</th>
        <th>VariantID</th>
        <th>UPC</th>
        <th>Details</th>
        <th>Qty</th>
        <th>Cost Price<br/>(Without TAX)</th>
        <th>MRP</th>
        <th>VAT</th>
        <th>Taxable</th>
        <th>Tax</th>
        <th>Surcharge</th>
        <th>Payable</th>

      </tr>
      </thead>
      <tbody id="poTable">
      <c:forEach var="poLineItemDto" items="${orderSummary.purchaseOrderDto.poLineItemDtoList}" varStatus="ctr">
	      <c:set var="variant" value="${poLineItemDto.poLineItem.sku.productVariant}"/>
        <tr>
          <td>${ctr.index+1}.</td>
          <td>
              ${variant.id}
          </td>
          <td>
              ${variant.upc}
          </td>
          <td>${variant.product.name}<br/>
	          <c:forEach items="${variant.productOptions}" var="variantOption">
		          <c:if test="${hk:showOptionOnUI(variantOption.name)}">
			          ${variantOption.name}:${variantOption.value}
			          <br/>
		          </c:if>
	          </c:forEach>
	          <c:if test="${poLineItemDto.poLineItem.remarks!=null}">
	          <br/>${poLineItemDto.poLineItem.remarks}
	          </c:if>
          </td>
          <td>${poLineItemDto.poLineItem.qty}
          </td>
          <td>${poLineItemDto.poLineItem.costPrice}
          </td>
          <td>${poLineItemDto.poLineItem.mrp}
          </td>
          <td>
            <fmt:formatNumber value="${poLineItemDto.poLineItem.sku.tax.value * 100}" maxFractionDigits="2"/>%
          </td>
          <td>
            <fmt:formatNumber value="${poLineItemDto.taxable}" maxFractionDigits="2"/>
          </td>
          <td>
            <fmt:formatNumber value="${poLineItemDto.tax}" maxFractionDigits="2"/>
          </td>
          <td>
            <fmt:formatNumber value="${poLineItemDto.surcharge}" maxFractionDigits="2"/>
          </td>
          <td>
            <fmt:formatNumber value="${poLineItemDto.payable}" maxFractionDigits="2"/>
          </td>
        </tr>
      </c:forEach>
      </tbody>
      <tfoot>
      <tr>
        <td colspan="8">Total</td>
        <td><fmt:formatNumber value="${orderSummary.purchaseOrderDto.totalTaxable}" maxFractionDigits="2"/></td>
        <td><fmt:formatNumber value="${orderSummary.purchaseOrderDto.totalTax}" maxFractionDigits="2"/></td>
        <td><fmt:formatNumber value="${orderSummary.purchaseOrderDto.totalSurcharge}" maxFractionDigits="2"/></td>
        <td><fmt:formatNumber value="${orderSummary.purchaseOrderDto.totalPayable}" maxFractionDigits="2"/></td>
      </tr>
      </tfoot>
    </table>
<br>
<b>1) Please indicate Purchase Order number on all invoice and challan and correspondence.</b>
<br>
<br>
<b>2) The item supplied will be subject to our approval and all rejections will be to your account.</b>
<br>
<br>
<b>3) No excess supply will be accepted,unless agreed in writing by us.</b>
<br>
<br>
</body>
</html>
