<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
  <title>Debit Note</title>
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
  }
  
  table th {
  text-align: center;
  }

  p {
    margin-top: 2px;
    margin-bottom: 2px;
    margin-left: 2px;
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
<div class="grid_12">
	<div class="grid_4 alpha omega">
		<div class="column">
      <c:set var="warehouse" value="${debitNoteSummary.debitNote.warehouse}"/>
      ${warehouse.name}<br/>
      ${warehouse.line1}<br/>
      ${warehouse.line2}<br/>
      ${warehouse.city} &nbsp;
      -${warehouse.pincode} <br/>
      ${warehouse.state}<br/>
      TIN# ${warehouse.tin}
</div></div></div>
<h3 align="center">Debit Note</h3>
<br/>
		<div class="clear"></div>
<div style="margin-top: 20px;"></div>
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
    <c:choose>
      <c:when test="${debitNoteSummary.debitNote.debitNoteNumber!=null }">
       <td>
          ${debitNoteSummary.debitNote.debitNoteNumber}
      </td>
      </c:when>
      <c:otherwise>
    <td></td>
    </c:otherwise>
    </c:choose>
  </tr>
  <tr>
    <td><b>Contact Name</b></td>
    <td>${debitNoteSummary.debitNote.supplier.contactPerson}</td>
    <td><b>Contact Number</b></td>
    <td>${debitNoteSummary.debitNote.supplier.contactNumber}</td>
  </tr>
    <tr>
        <td><b>Tin</b></td>
        <td>${debitNoteSummary.debitNote.supplier.tinNumber}</td>
        <td colspan="2"></td>
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
    <c:choose>
    <c:when test="${debitNoteSummary.debitNote.reconciliationVoucher!=null}">
    <th>Discount %</th>
    </c:when>
    <c:otherwise></c:otherwise>
    </c:choose>
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
      
      <c:choose>
      <c:when test="${debitNoteDto.debitNoteLineItem.sku!=null }">
       <td>
          ${debitNoteDto.debitNoteLineItem.sku.productVariant.id}
      </td>
      <td>
          ${debitNoteDto.debitNoteLineItem.sku.productVariant.upc}
      </td>
      <td>${debitNoteDto.debitNoteLineItem.sku.productVariant.product.name}<br/>${debitNoteDto.debitNoteLineItem.sku.productVariant.optionsCommaSeparated}
      </td>
      </c:when>
      <c:otherwise>
      <td>N/A</td>
      <td>N/A</td>
      <td>${debitNoteDto.debitNoteLineItem.productName}</td>
      </c:otherwise>
      </c:choose>
      <td>${debitNoteDto.debitNoteLineItem.qty}
      </td>
      <td>${debitNoteDto.debitNoteLineItem.costPrice}
      </td>
      <td>${debitNoteDto.debitNoteLineItem.mrp}
      </td>
      <c:choose>
	    <c:when test="${debitNoteSummary.debitNote.reconciliationVoucher!=null}">
	    <td>${debitNoteDto.debitNoteLineItem.discountPercent}</td>
	    </c:when>
	    <c:otherwise></c:otherwise>
    	</c:choose>
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
  <c:choose>
	    <c:when test="${debitNoteSummary.debitNote.reconciliationVoucher!=null}">
	    <td colspan="8">Total</td>
	    </c:when>
	    <c:otherwise><td colspan="7">Total</td></c:otherwise>
    	</c:choose>
  
    <td><fmt:formatNumber value="${debitNoteSummary.debitNoteDto.totalTaxable}" maxFractionDigits="2"/></td>
    <td><fmt:formatNumber value="${debitNoteSummary.debitNoteDto.totalTax}" maxFractionDigits="2"/></td>
    <td><fmt:formatNumber value="${debitNoteSummary.debitNoteDto.totalSurcharge}" maxFractionDigits="2"/></td>
    <td><Strong><fmt:formatNumber value="${debitNoteSummary.debitNoteDto.totalPayable}" maxFractionDigits="2"/></Strong>
    </td>
  </tr>
  <tr>
  <c:choose>
  <c:when test="${debitNoteSummary.debitNote.reconciliationVoucher!=null}">
	    <td colspan="11">Freight And Forwarding</td>
	    </c:when>
	    <c:otherwise><td colspan="10">Freight And Forwarding</td></c:otherwise>
    	</c:choose>
    <td><Strong><fmt:formatNumber value="${debitNoteSummary.debitNote.freightForwardingCharges}" maxFractionDigits="2"/></Strong>
    </td>
  </tr>

			<c:choose>
				<c:when test="${debitNoteSummary.debitNote.reconciliationVoucher!=null}">
					<tr>
						<td colspan="11">Overall Discount</td>
						<td><Strong><fmt:formatNumber value="${debitNoteSummary.debitNote.discount}" maxFractionDigits="2"/></Strong>
					</tr>
				</c:when>
				<c:otherwise>
				</c:otherwise>
			</c:choose>


			<tr>
  <c:choose>
  <c:when test="${debitNoteSummary.debitNote.reconciliationVoucher!=null}">
	    <td colspan="11">Final Debit Amount</td>
	    </c:when>
	    <c:otherwise><td colspan="10">Final Debit Amount</td></c:otherwise>
    	</c:choose>
    <td><Strong><fmt:formatNumber value="${debitNoteSummary.debitNote.finalDebitAmount}" maxFractionDigits="2"/></Strong>
    </td>
  </tr>
  </tfoot>
</table>


</body>
</html>
