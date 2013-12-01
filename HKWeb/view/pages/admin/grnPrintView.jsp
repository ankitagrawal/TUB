<%@ page import="java.text.DateFormat" %>
<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
  <title>GOODS RECEIVED NOTE (GRN)</title>
  <style type="text/css">
    table {
      border-collapse: collapse;
      width: 100%;
      font-size:.8em;
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

<s:useActionBean beanclass="com.hk.web.action.admin.inventory.GRNAction" var="orderSummary"/>
<table class="header">
  <tr>
    <td>
      <c:set var="warehouse" value="${orderSummary.grn.warehouse}"/>
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

<h3 align="center">GOODS RECEIVED NOTE (GRN)</h3>
<br/>
<table style="font-size:.9em">
  <tr>
    <td><b>Supplier</b></td>
    <td>${orderSummary.grn.purchaseOrder.supplier.name}</td>
    <td><b>GRN Date</b></td>
    <td><fmt:formatDate value="${orderSummary.grn.grnDate}" pattern="yyyy-MM-dd"/></td>
  </tr>
  <tr valign="top">
    <td><b>Address</b></td>
    <td>${orderSummary.grn.purchaseOrder.supplier.line1}<br/>${orderSummary.grn.purchaseOrder.supplier.line2}<br/>${orderSummary.grn.purchaseOrder.supplier.city}<br/>${orderSummary.grn.purchaseOrder.supplier.state}
    </td>
	  <td><b>GRN#</b><br/><b>Invoice#</b><br/><b>Invoice Date</b><br/><b>PO#</b><br/><b>PO Date</b></td>
	  <td>${orderSummary.grn.id}<br/>${orderSummary.grn.invoiceNumber}<br/><fmt:formatDate
			  value="${orderSummary.grn.invoiceDate}"
			  pattern="dd-MMM-yyyy"/><br/>${orderSummary.grn.purchaseOrder.id}<br/><fmt:formatDate
			  value="${orderSummary.grn.purchaseOrder.poPlaceDate}" pattern="dd-MMM-yyyy"/> </td>
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
    <%--<th></th>--%>
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
  <c:forEach var="grnLineItemDto" items="${orderSummary.grnDto.grnLineItemDtoList}" varStatus="ctr">
    <c:set var="sku" value="${grnLineItemDto.grnLineItem.sku}"/>
    <c:set var="productVariant" value="${sku.productVariant}"/>
    <tr>
      <td>${ctr.index+1}.</td>
      <td>
          ${productVariant.id}
      </td>
      <td>${productVariant.upc}</td>
      <td>${productVariant.product.name}<br/>${productVariant.optionsCommaSeparated}
      </td>
      <%--<td>
        <div class='img48' style="vertical-align:top;">
          <c:choose>
            <c:when test="${productVariant.product.mainImageId != null}">
              <hk:productImage imageId="${productVariant.product.mainImageId}" size="<%=EnumImageSize.TinySize%>"/>
            </c:when>
            <c:otherwise>
              <img class="prod48" src="${pageContext.request.contextPath}/images/ProductImages/ProductImagesThumb/${productVariant.product.id}.jpg" alt="${productLineItem.productVariant.product.name}"/>
            </c:otherwise>
          </c:choose>
        </div>
      </td>--%>
      <td>${grnLineItemDto.grnLineItem.qty}
      </td>
      <td>${grnLineItemDto.grnLineItem.costPrice}
      </td>
      <td>${grnLineItemDto.grnLineItem.mrp}
      </td>
      <td>
        <fmt:formatNumber value="${sku.tax.value * 100}" maxFractionDigits="2"/>%
      </td>
      <td>
        <fmt:formatNumber value="${grnLineItemDto.taxable}" maxFractionDigits="2"/>
      </td>
      <td>
        <fmt:formatNumber value="${grnLineItemDto.tax}" maxFractionDigits="2"/>
      </td>
      <td>
        <fmt:formatNumber value="${grnLineItemDto.surcharge}" maxFractionDigits="2"/>
      </td>
      <td>
        <fmt:formatNumber value="${grnLineItemDto.payable}" maxFractionDigits="2"/>
      </td>
    </tr>
  </c:forEach>
  </tbody>
  <tfoot>
  <tr>
    <td colspan="4">Total</td>
    <td><fmt:formatNumber value="${orderSummary.grnDto.totalQty}" maxFractionDigits="2"/></td>
      <td colspan="3"></td>
    <td><fmt:formatNumber value="${orderSummary.grnDto.totalTaxable}" maxFractionDigits="2"/></td>
    <td><fmt:formatNumber value="${orderSummary.grnDto.totalTax}" maxFractionDigits="2"/></td>
    <td><fmt:formatNumber value="${orderSummary.grnDto.totalSurcharge}" maxFractionDigits="2"/></td>
    <td><fmt:formatNumber value="${orderSummary.grnDto.totalPayable}" maxFractionDigits="2"/></td>
  </tr>
  </tfoot>
</table>

</body>
</html>
