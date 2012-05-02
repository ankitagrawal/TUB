<%@ page import="java.text.DateFormat" %>
<%@ page import="com.hk.constants.EnumImageSize" %>
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

<s:useActionBean beanclass="web.action.admin.POAction" var="orderSummary"/>
<table class="header">
  <tr>
    <td>
      Bright Lifecare Pvt. Ltd.<br/>
      3th Floor, Parsvnath Arcadia,<br/>
      1 MG Road, Gurgaon - 122001<br/>
      TIN: 06101832036
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
    <td><b>PO Date</b></td>
    <td><fmt:formatDate value="${orderSummary.purchaseOrder.poDate}" pattern="yyyy-MM-dd"/></td>
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
        <tr>
          <td>${ctr.index+1}.</td>
          <td>
              ${poLineItemDto.poLineItem.productVariant.id}
          </td>
          <td>
              ${poLineItemDto.poLineItem.productVariant.upc}
          </td>
          <td>${poLineItemDto.poLineItem.productVariant.product.name}<br/>${poLineItemDto.poLineItem.productVariant.optionsCommaSeparated}
          </td>          
          <td>${poLineItemDto.poLineItem.qty}
          </td>
          <td>${poLineItemDto.poLineItem.costPrice}
          </td>
          <td>${poLineItemDto.poLineItem.mrp}
          </td>
          <td>
            <fmt:formatNumber value="${poLineItemDto.poLineItem.productVariant.tax.value * 100}" maxFractionDigits="2"/>%
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


</body>
</html>
