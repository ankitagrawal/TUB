<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<link href="<hk:vhostCss/>/css/960.css" rel="stylesheet" type="text/css"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
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

  .tdHead{
	font-weight: bold;
	}
	
  .column {
    padding: 2px;
  }
</style>
  <title>PURCHASE RETURN</title>
  <script type="text/javascript" src="<hk:vhostJs/>/js/jquery-1.7.2.min.js"></script>
  <script type="text/javascript" src="<hk:vhostJs/>/js/jquery.hkCommonPlugins.js"></script>
  <script type="text/javascript">

  $(document).ready(function() {
	  var payable=0;
	 $(".payable").each(function() {
	      payable = payable + parseFloat($(this).html());
	    });
	 $("#totalPayable").append(payable.toFixed(2));
	 
	 var totalTax=0;
	 $(".tax").each(function() {
		 totalTax = totalTax + parseFloat($(this).html());
	    });
	 $("#totalTax").append(totalTax.toFixed(2));
	 
	 var totalSurcharge=0;
	 $(".surcharge").each(function() {
		 totalSurcharge = totalSurcharge + parseFloat($(this).html());
	    });
	 $("#totalSurcharge").append(totalSurcharge.toFixed(2));
	 
	 var totalPayable=0;
	 $(".finalPayable").each(function() {
		 totalPayable = totalPayable + parseFloat($(this).html());
	    });
	 $("#totalFinalPayable").append(totalPayable.toFixed(2));
  
  });
</script>
</head>
<body>
<s:useActionBean beanclass="com.hk.web.action.admin.rtv.ExtraInventoryAction" var="ei"/>
<div class="container_12">
<div class="grid_12" style="text-align: center;">
<h4>
PURCHASE RETURN
</h4>
</div>
<div class="grid_12">
	<div class="grid_4 alpha omega">
		<div class="column">
		<p>${ei.purchaseOrder.warehouse.name}</p>
		<p>${ei.purchaseOrder.warehouse.line1}</p>
		<p>${ei.purchaseOrder.warehouse.line2}</p>
		<p>${ei.purchaseOrder.warehouse.city}-${ei.purchaseOrder.warehouse.pincode}</p>
		<p>${ei.purchaseOrder.warehouse.state}</p>
		<p>Tin No - ${ei.purchaseOrder.warehouse.tin}</p>
		</div>
		</div>
		</div>
		
		<div class="clear"></div>
<div style="margin-top: 20px;"></div>

<div class="grid_12">
  <table cellspacing="0" id="mainTable">
  <tr>
  <td class="tdHead">Supplier</td>
  <td>${ei.purchaseOrder.supplier.name}</td>
  <td class="tdHead">RTV Note Create Date</td>
  <td>${ei.rtvNote.createDate }</td>
  </tr>
  <tr>
  <td class="tdHead">Address</td>
  <td><p>${ei.purchaseOrder.supplier.line1}</p><p>${ei.purchaseOrder.supplier.line2}</p><p>${ei.purchaseOrder.supplier.city}-${ei.purchaseOrder.supplier.pincode}</p><p>${ei.purchaseOrder.supplier.state}</p></td>
  <td class="tdHead">RTV Note Number</td>
  <td>${ei.rtvNote.id }</td>
  </tr>
  <tr>
  <td class="tdHead">Contact Name</td>
  <c:choose>
		<c:when test="${ei.purchaseOrder.supplier.contactPerson!=null}">
		<td>${ei.purchaseOrder.supplier.contactPerson}</td>
		</c:when>
		<c:otherwise><td>N/A</td></c:otherwise>
		</c:choose>
  <td class="tdHead">Contact Number</td>
  <c:choose>
		<c:when test="${ei.purchaseOrder.supplier.contactNumber!=null}">
		<td>${ei.purchaseOrder.supplier.contactNumber}</td>
		</c:when>
		<c:otherwise><td>N/A</td></c:otherwise>
		</c:choose>
  </tr>
  <tr>
  <td class="tdHead">TIN</td>
  <c:choose>
		<c:when test="${ei.purchaseOrder.supplier.tinNumber!=null}">
		<td colspan="3">${ei.purchaseOrder.supplier.tinNumber}</td>
		</c:when>
		<c:otherwise><td colspan="3">N/A</td></c:otherwise>
		</c:choose>
  </tr>
  </table></div>
  
  <div class="clear"></div>
<div style="margin-top: 20px;"></div>
<br/>
  <div class="grid_12">
  <table cellspacing="0" id="mainTable">
  <tr>
  <th>S. No. </th>
  <th>Variant Id</th>
  <th>UPC</th>
  <th>Details</th>
  <th>Quantity</th>
  <th>Cost Price<br/>(Without Tax)</th>
  <th>MRP</th>
  <th>Taxable</th>
  <th>Tax</th>
  <th>Surcharge</th>
  <th>Payable</th>
  </tr>
  <c:forEach var="rtvLineItem" items="${ei.rtvNoteLineItems}" varStatus="ctr">
	<tr count="${ctr.index}" class="${ctr.last ? 'lastShortRow lineItemRow shortTableTr':'lineItemRow shortTableTr'}">
	<td>${ctr.index+1}.</td>
	
	<c:choose>
		<c:when test="${rtvLineItem.extraInventoryLineItem.sku!=null}">
			<td> ${rtvLineItem.extraInventoryLineItem.sku.productVariant.id}</td>
	<td>${rtvLineItem.extraInventoryLineItem.sku.productVariant.upc!=null?rtvLineItem.extraInventoryLineItem.sku.productVariant.upc:N/A}</td>
	<td>${rtvLineItem.extraInventoryLineItem.sku.productVariant.product.name}<br/>${rtvLineItem.extraInventoryLineItem.sku.productVariant.optionsCommaSeparated}</td>
		</c:when>
		<c:otherwise>
			<td>N/A</td>
			<td>N/A</td>
			<td>${rtvLineItem.extraInventoryLineItem.productName}</td>
		</c:otherwise>
	</c:choose>
	
	<td>${rtvLineItem.extraInventoryLineItem.receivedQty}</td>
	<td>${rtvLineItem.extraInventoryLineItem.costPrice}</td>
	<td>${rtvLineItem.extraInventoryLineItem.mrp}</td>
	<td class="payable">${rtvLineItem.extraInventoryLineItem.taxableAmount}</td>
	<td class="tax">${rtvLineItem.extraInventoryLineItem.taxAmount}</td>
	<td class="surcharge">${rtvLineItem.extraInventoryLineItem.surchargeAmount}</td>
	<td class="finalPayable">${rtvLineItem.extraInventoryLineItem.payableAmount}</td>
	</tr>
	</c:forEach>
	<tr>
	<td colspan="7">Total</td>
	<td id="totalPayable"></td>
	<td id="totalTax"></td>
	<td id="totalSurcharge"></td>
	<td id="totalFinalPayable"></td>
	</tr>
  </table></div>
  
</div>
</body>
</html>