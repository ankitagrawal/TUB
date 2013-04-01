<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
  <title>Debit Note</title>
  <style type="text/css">
    table {
      border-collapse: collapse;
      width: 100%;
      font-size: .8em;
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

<s:useActionBean beanclass="com.hk.web.action.admin.inventory.DebitNoteAction" var="debitNoteSummary"/>
<table class="header">
  <tr>
    <td>
      <%--Bright Lifecare Pvt. Ltd.<br/>--%>
      <%--3th Floor, Parsvnath Arcadia,<br/>--%>
      <%--1 MG Road, Gurgaon - 122001<br/>--%>
      <%--TIN: 06101832036--%>
      Bright Lifecare Pvt. Ltd.<br/>
      ${debitNoteSummary.debitNote.warehouse.line1}<br/>
      ${debitNoteSummary.debitNote.warehouse.line2}<br/>
      ${debitNoteSummary.debitNote.warehouse.city} &nbsp;
      -${debitNoteSummary.debitNote.warehouse.pincode} <br/>
      ${debitNoteSummary.debitNote.warehouse.state}<br/>
       <c:choose>
        <c:when test="${debitNoteSummary.debitNote.warehouse.state == 'HARYANA'}">
          <p> TIN# 06101832036</p>
        </c:when>
        <c:when test="${debitNoteSummary.debitNote.warehouse.state  == 'MAHARASHTRA'}">
          <p> TIN# 27210893736</p>
        </c:when>
      </c:choose>
    </td>
    <td align="right">
      <%--<img src="${pageContext.request.contextPath}/images/logo.png" alt=""/>--%>
    </td>
  </tr>
</table>

<h3 align="center">Debit Note</h3>
<br/>
<table style="font-size:.9em">
  <tr>
    <td><b>Supplier</b></td>
    <td>${debitNoteSummary.debitNote.supplier.name}</td>
    <td><b>Debit Note Create Date</b></td>
    <td><fmt:formatDate value="${debitNoteSummary.debitNote.createDate}" pattern="yyyy-MM-dd"/></td>
  </tr>
  <tr valign="top">
    <td><b>Address</b></td>
    <td>${debitNoteSummary.debitNote.supplier.line1}<br/>${debitNoteSummary.debitNote.supplier.line2}<br/>${debitNoteSummary.debitNote.supplier.city}<br/>${debitNoteSummary.debitNote.supplier.state}
    </td>
    <td><b>Debit Note Number:</b></td>
    <td>${debitNoteSummary.debitNote.id}</td>
  </tr>
  <tr>
    <td><b>Contact Name</b></td>
    <td>${debitNoteSummary.debitNote.supplier.contactPerson}</td>
    <td><b>Contact Number</b></td>
    <td>${debitNoteSummary.debitNote.supplier.contactNumber}</td>
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
    <%--<th>VAT</th>--%>
    <th>Taxable</th>
    <th>Tax</th>
    <th>Surcharge</th>
    <th>Payable</th>

  </tr>
  </thead>
  <tbody>
  <c:forEach var="debitNoteDto" items="${debitNoteSummary.debitNoteDto.debitNoteLineItemDtoList}" varStatus="ctr">
    <tr>
      <td>${ctr.index+1}.</td>
      <td>
          ${debitNoteDto.debitNoteLineItem.sku.productVariant.id}
      </td>
      <td>
          ${debitNoteDto.debitNoteLineItem.sku.productVariant.upc}
      </td>
      <td>${debitNoteDto.debitNoteLineItem.sku.productVariant.product.name}<br/>${debitNoteDto.debitNoteLineItem.sku.productVariant.optionsCommaSeparated}
      </td>
      <td>${debitNoteDto.debitNoteLineItem.qty}
      </td>
      <td>${debitNoteDto.debitNoteLineItem.costPrice}
      </td>
      <td>${debitNoteDto.debitNoteLineItem.mrp}
      </td>
      <%--<td>
        <fmt:formatNumber value="${debitNoteDto.debitNoteLineItem.sku.tax.value * 100}" maxFractionDigits="2"/>%
      </td>--%>
      <td>
        <fmt:formatNumber value="${debitNoteDto.taxable}" maxFractionDigits="2"/>
      </td>
      <td>
        <fmt:formatNumber value="${debitNoteDto.tax}" maxFractionDigits="2"/>
      </td>
      <td>
        <fmt:formatNumber value="${debitNoteDto.surcharge}" maxFractionDigits="2"/>
      </td>
      <td>
        <fmt:formatNumber value="${debitNoteDto.payable}" maxFractionDigits="2"/>
      </td>
    </tr>
  </c:forEach>
  </tbody>
  <tfoot>
  <tr>
    <td colspan="7">Total</td>
    <td><fmt:formatNumber value="${debitNoteSummary.debitNoteDto.totalTaxable}" maxFractionDigits="2"/></td>
    <td><fmt:formatNumber value="${debitNoteSummary.debitNoteDto.totalTax}" maxFractionDigits="2"/></td>
    <td><fmt:formatNumber value="${debitNoteSummary.debitNoteDto.totalSurcharge}" maxFractionDigits="2"/></td>
    <td><Strong><fmt:formatNumber value="${debitNoteSummary.debitNoteDto.totalPayable}" maxFractionDigits="2"/></Strong>
    </td>
  </tr>
  </tfoot>
</table>


</body>
</html>
