<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page import="com.hk.domain.core.Surcharge" %>
<%@ page import="com.hk.domain.core.Tax" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.pact.dao.TaxDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="java.util.List" %>
<%@ page import="com.hk.domain.core.PurchaseFormType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<%@ page import="com.hk.admin.util.TaxUtil" %>
<%@ page import="com.hk.constants.inventory.EnumPurchaseInvoiceStatus" %>
<s:useActionBean beanclass="com.hk.web.action.admin.inventory.PurchaseInvoiceAction" var="pia"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Purchase Invoice">

<%
	TaxDao taxDao = ServiceLocatorFactory.getService(TaxDao.class);
	List<Tax> taxList = taxDao.getTaxList();
	pageContext.setAttribute("taxList", taxList);

	MasterDataDao masterDataDao = (MasterDataDao) ServiceLocatorFactory.getService(MasterDataDao.class);
	List<PurchaseFormType> purchaseFormTypes = masterDataDao.getPurchaseInvoiceFormTypes();
	List<Surcharge> surchargeList = masterDataDao.getSurchargeList();
	pageContext.setAttribute("purchaseFormTypes", purchaseFormTypes);
	pageContext.setAttribute("surchargeList", surchargeList);
%>


<s:layout-component name="htmlHead">
<link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
<style type="text/css">
.shortLink{
	float: left;
    font-size: 0.8em;
    position: relative;
    width: 56px;
}

.purchaseInvoiceShortTable{
	float: left;
    position: relative;
	width: 100%;
}

.rtvTable{
	float: left;
    position: relative;
	width: 100%;
}

.rtvListForm{
	float: right;
	position: relative;
}

#finalPayableDiv{
	float: right;
	position: relative;
}

#closeButtonDiv{
	float: left;
	position: relative;
	left: 40%;
}

</style>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
<jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
<script type="text/javascript">

	$(document).ready(function() {

		var rowValue;
		
		function updateTotalPI(fromTotalClass, toTotalClass, toHtml, table) {
			var total = 0;	var otherTable;
			
			$.each(table.parent().find(fromTotalClass), function(index, value) {
				var eachRow = $(value);
				var eachRowValue = eachRow.val().trim();
				total += parseFloat(eachRowValue);
			});
			total = total.toFixed(2);
			if (toHtml == 1) {
				table.parent().find(toTotalClass).html(total);
			} else {
				table.parent().find(toTotalClass).val(total);
			}
		};
		
		function populateRow(rowValues, index){
			var indexValue = eval(index);
			var row = $('#piShortTable').find("tr")[indexValue+1];
			var rowAtIndex = $($('#piShortTable').find("tr")[indexValue+1]);
			var selectedTax = $(rowValues).find(".taxCategory :selected").val();
			rowAtIndex.find('.variant').val($(rowValues).find(".variant").val());
			var text = ($(rowValues).find(".variantName").text());
			rowAtIndex.find('.pvDetails').append(text); 
			rowAtIndex.find('.taxCategory').val(selectedTax);
			rowAtIndex.find('.costPrice').val($(rowValues).find(".costPrice").val());
			rowAtIndex.find('.mrp').val($(rowValues).find(".mrp").val());
			
		};

		$('.addRowButton').click(function(event) {
			var id = event.target.id;
			rowValue = event;
			var row;
			var lastShortIndex;
			if(id == "createShortLink"){
				lastShortIndex = $('.lastShortRow').attr('count');
				if (!lastShortIndex) {
					lastShortIndex = -1;
				}
				$('.lastShortRow').removeClass('lastShortRow');
				var nextIndex = eval(lastShortIndex + "+1");
				row = '<tr count="' + nextIndex + '" class="lastShortRow lineItemRow shortTableTr">';
			}
			else{
				var lastIndex = $('.lastRow').attr('count');
				if (!lastIndex) {
					lastIndex = -1;
				}
				$('.lastRow').removeClass('lastRow');
				var nextIndex = eval(lastIndex + "+1");
				row = '<tr count="' + nextIndex + '" class="lastRow lineItemRow">';
			}
			
			var taxOptions = "";
			var shortTaxOptions = "";
		<c:choose>
		<c:when test="${pia.purchaseInvoice.supplier.state==pia.purchaseInvoice.warehouse.state}">
			taxOptions = '<select class="taxCategory valueChange" name="purchaseInvoiceLineItems[' + nextIndex + '].tax">';
		<c:forEach items="${taxList}" var="tax">
			taxOptions += '<option value="'+${tax.id}+
			'">'+${tax.value}+
			'</option>';
		</c:forEach>
		</c:when>
		<c:otherwise>
			taxOptions = '<select class="taxCategory valueChange" name="purchaseInvoiceLineItems[' + nextIndex + '].surcharge">';
		<c:forEach items="${surchargeList}" var="surcharge">
			taxOptions += '<option value="'+${surcharge.id}+
			'">' + ${surcharge.value} + '</option>';
		</c:forEach>
		</c:otherwise>
		</c:choose>
		
		<c:choose>
		<c:when test="${pia.purchaseInvoice.supplier.state==pia.purchaseInvoice.warehouse.state}">
			shortTaxOptions = '<select class="taxCategory valueChange" name="purchaseInvoiceShortLineItems[' + nextIndex + '].tax">';
		<c:forEach items="${taxList}" var="tax">
			shortTaxOptions += '<option value="'+${tax.id}+
			'">'+${tax.value}+
			'</option>';
		</c:forEach>
		</c:when>
		<c:otherwise>
			shortTaxOptions = '<select class="taxCategory valueChange" name="purchaseInvoiceShortLineItems[' + nextIndex + '].surcharge">';
		<c:forEach items="${surchargeList}" var="surcharge">
			shortTaxOptions += '<option value="'+${surcharge.id}+
			'">' + ${surcharge.value} + '</option>';
		</c:forEach>
		</c:otherwise>
		</c:choose>

			var newRowHtml = row
					 +
					'<td>' + Math.round(nextIndex + 1) + '.</td>' +
					'<td></td>' +
					'  <td>' +
					'    <input type="hidden" name="purchaseInvoiceLineItems[' + nextIndex + '].id" />' +
					'    <input type="text" class="variant" name="purchaseInvoiceLineItems[' + nextIndex + '].productVariant"/>' +
					'  </td>' +
					'<td></td>' +
					'  <td class="pvDetails"></td>' +
					'<td>' +
					taxOptions +
					'</select>' +
					'<input type="hidden" value="finance" class="taxIdentifier"/>' +
					'</td>' +
					'  <td>' +
					'    <input type="text" name="purchaseInvoiceLineItems[' + nextIndex + '].qty" class="receivedQuantity valueChange" />' +
					'  </td>' +
					'  <td>' +
					'    <input class="costPrice valueChange" type="text" name="purchaseInvoiceLineItems[' + nextIndex + '].costPrice" />' +
					'  </td>' +
					'  <td>' +
					'    <input class="mrp" type="text" name="purchaseInvoiceLineItems[' + nextIndex + '].mrp" />' +
					' </td>' +
					'  <td>' +
					'    <input class="discountPercentage valueChange" type="text" name="purchaseInvoiceLineItems[' + nextIndex + '].discountPercent" />' +
					' </td>' +
					'  <td ><input class="taxableAmount" type="text" readonly="readonly" name="purchaseInvoiceLineItems[' + nextIndex + '].taxableAmount"/></td>' +
					'  <td ><input class="taxAmount" type="text" readonly="readonly" name="purchaseInvoiceLineItems[' + nextIndex + '].taxAmount"></td>' +
					'  <td ><input class="surchargeAmount" type="text" readonly="readonly" name="purchaseInvoiceLineItems[' + nextIndex + '].surchargeAmount"/></td>' +
					'  <td ><input class="payableAmount" type="text" readonly="readonly" name="purchaseInvoiceLineItems[' + nextIndex + '].payableAmount"/></td>' +
					'  </tr>';
					
					var newShortRowHtml = row
					 +
					'<td>' + Math.round(nextIndex + 1) + '.</td>' +
					'<td></td>' +
					'  <td>' +
					'    <input type="hidden" name="purchaseInvoiceShortLineItems[' + nextIndex + '].id" />' +
					'    <input type="text" class="variant" name="purchaseInvoiceShortLineItems[' + nextIndex + '].productVariant"/>' +
					'  </td>' +
					'<td></td>' +
					'  <td class="pvDetails"></td>' +
					'<td>' +
					shortTaxOptions +
					'</select>' +
					'<input type="hidden" value="finance" class="taxIdentifier"/>' +
					'</td>' +
					'  <td>' +
					'    <input type="text" name="purchaseInvoiceShortLineItems[' + nextIndex + '].qty" class="receivedQuantity valueChange" />' +
					'  </td>' +
					'  <td>' +
					'    <input class="costPrice valueChange" type="text" name="purchaseInvoiceShortLineItems[' + nextIndex + '].costPrice" />' +
					'  </td>' +
					'  <td>' +
					'    <input class="mrp" type="text" name="purchaseInvoiceShortLineItems[' + nextIndex + '].mrp" />' +
					' </td>' +
					'  <td>' +
					'    <input class="discountPercentage valueChange" type="text" name="purchaseInvoiceShortLineItems[' + nextIndex + '].discountPercent" />' +
					' </td>' +
					'  <td ><input class="taxableAmount" type="text" readonly="readonly" name="purchaseInvoiceShortLineItems[' + nextIndex + '].taxableAmount"/></td>' +
					'  <td ><input class="taxAmount" type="text" readonly="readonly" name="purchaseInvoiceShortLineItems[' + nextIndex + '].taxAmount"></td>' +
					'  <td ><input class="surchargeAmount" type="text" readonly="readonly" name="purchaseInvoiceShortLineItems[' + nextIndex + '].surchargeAmount"/></td>' +
					'  <td ><input class="payableAmount" type="text" readonly="readonly" name="purchaseInvoiceShortLineItems[' + nextIndex + '].payableAmount"/></td>' +
					'  </tr>';

					if(id == "createShortLink"){
						var addRow =true;
						var trValues = rowValue.target.parentElement.parentElement.parentElement;
						$('#piShortTable  > .shortTableTr').each(function(currentInedx, ob) {
							if($(this).find(".variant").is("input")){
						    if ($(this).find(".variant").val() == $(trValues).find(".variant").val()){
						        addRow = false;
						    }
							}
							else{
								if ($(this).find(".variant").text() == $(trValues).find(".variant").val()){
							        addRow = false;
							    }
							}
						});
						if(addRow){
						$('#piShortTable').append(newShortRowHtml);
						populateRow(trValues,lastShortIndex);
						}
						else{
							alert('Cannot add mutiple rows for same Variant');
						}
					}
					else
						$('#piTable').append(newRowHtml);
			
			return false;
		});

		if(${!pia.piHasRtv}){
			$('#rtvForm').hide();
		}
		
		$('.variant').live("change", function() {
			var variantRow = $(this).parents('.lineItemRow');
			var productVariantId = variantRow.find('.variant').val();
			var productVariantDetails = variantRow.find('.pvDetails');
			$.getJSON(
					$('#pvInfoLink').attr('href'), {productVariantId: productVariantId, warehouse: ${pia.purchaseInvoice.goodsReceivedNotes[0].warehouse.id}},
					function(res) {
						if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
							variantRow.find('.mrp').val(res.data.variant.markedPrice);
							variantRow.find('.costPrice').val(res.data.variant.costPrice);
							variantRow.find('.taxCategory').val(res.data.taxId);
							productVariantDetails.html(
									res.data.product + '<br/>' +
									res.data.options
									);
						} else {
							$('.variantDetails').html('<h2>' + res.message + '</h2>');
						}
					}
					);
		});
		
		function populateRtvTable(){
			$('#piRtvTable  > .rtvTableTr').each(function(currentInedx, ob) {
				if($(this).find('.taxableAmount').val()==""||isNaN(parseFloat($(this).find('.taxableAmount').val()))){
					var costPrice = $(this).find('.costPrice').val();
					var mrp = $(this).find('.mrp').val();
					var qty = $(this).find('.receivedQuantity').val();
					var taxIdentifier = $(this).find('.taxIdentifier').val();
					var taxCategory;
					if (taxIdentifier == 'finance') {
						var taxCat = $(this).find('.taxCategory');
						var selectedTax = $(this).find('option:selected');
						taxCategory = selectedTax.text();
					} else {
						taxCategory = parseFloat($(this).find('.taxCategory').val().trim());
					}
					var taxable = costPrice * qty;
					var surchargeCategory = 0.0;
					var stateIdentifier = $('.state').html();
					surchargeCategory = ${hk:getSurchargeValue(pia.purchaseInvoice.supplier.state, pia.purchaseInvoice.warehouse.state)};
					var tax = taxable * taxCategory;
					var surcharge = tax * surchargeCategory;
					var payable = surcharge + taxable + tax;
					$(this).find('.taxableAmount').val(taxable.toFixed(2));
					$(this).find('.taxAmount').val(tax.toFixed(2));
					$(this).find('.surchargeAmount').val(surcharge.toFixed(2));
					$(this).find('.payableAmount').val(payable.toFixed(2));
				}
			});
		};
		
		
		$('.valueChange').live("change", function() {
			var table = $(this).parent().parent().parent();
			var valueChangeRow = $(this).parents('.lineItemRow');
			var costPrice = valueChangeRow.find('.costPrice').val();
			var mrp = valueChangeRow.find('.mrp').val();
			var qty = valueChangeRow.find('.receivedQuantity').val();
			var taxIdentifier = valueChangeRow.find('.taxIdentifier').val();
			if (qty == "" || costPrice == "") {
				alert("All fields are compulsory.");
				return false;
			}
			if (isNaN(qty) || isNaN(costPrice) || qty < 0 || costPrice < 0) {
				alert("Enter values in correct format.");
				return false;
			}
			var taxCategory;
			if (taxIdentifier == 'finance') {
				var taxCat = valueChangeRow.find('.taxCategory');
				var selectedTax = $(taxCat).find('option:selected');
				taxCategory = selectedTax.text();
			} else {
				taxCategory = parseFloat(valueChangeRow.find('.taxCategory').val().trim());
			}
			var taxable = costPrice * qty;
			var discountPercentage;
			if(valueChangeRow.find('.discountPercentage').length!=0){
			discountPercentage = valueChangeRow.find('.discountPercentage').val();
			}
			else{
				discountPercentage=0;
			}
			var discountedAmount;
			if (isNaN(discountPercentage)) {
				alert("Enter valid discount");
				return;
			}
			discountedAmount = (discountPercentage / 100) * taxable;
			taxable -= discountedAmount;
			var surchargeCategory = 0.0;
			var stateIdentifier = $('.state').html();
			surchargeCategory = ${hk:getSurchargeValue(pia.purchaseInvoice.supplier.state, pia.purchaseInvoice.warehouse.state)};
			/*if (stateIdentifier == 'CST') {
			 surchargeCategory = 0.0;
			 } else {
			 surchargeCategory = 0.05;
			 }*/
			var tax = taxable * taxCategory;
			var surcharge = tax * surchargeCategory;
			var payable = surcharge + taxable + tax;

			valueChangeRow.find('.taxableAmount').val(taxable.toFixed(2));
			valueChangeRow.find('.taxAmount').val(tax.toFixed(2));
			valueChangeRow.find('.surchargeAmount').val(surcharge.toFixed(2));
			valueChangeRow.find('.payableAmount').val(payable.toFixed(2));

			
			updateTotalPI('.taxableAmount', '.totalTaxable', 0, table);
			updateTotalPI('.taxAmount', '.totalTax', 0, table);
			updateTotalPI('.surchargeAmount', '.totalSurcharge', 0, table);
			updateTotalPI('.payableAmount', '.totalPayable', 0, table);
			updateTotalPI('.payableAmount', '.finalPayable', 0, table);
			updateTotalPI('.receivedQuantity', '.totalQuantity', 1, table);
			var finalPayable = parseFloat(table.parent().find('.finalPayable').val().replace(/,/g, ''));
			if(valueChangeRow.find('.overallDiscount').length!=0){
			var overallDiscount = parseFloat(table.parent().find('.overallDiscount').val().replace(/,/g, ''));
			}
			if(valueChangeRow.find('.freightCharges').length!=0){
				var freightCharges = parseFloat(table.parent().find('.freightCharges').val().replace(/,/g, ''));	
			}
			
			if (isNaN(overallDiscount)) {
				overallDiscount = 0;
			}
			if (isNaN(freightCharges)) {
				freightCharges = 0;
			}
			finalPayable -= overallDiscount;
			finalPayable += freightCharges;
			table.find('.finalPayable').val(finalPayable.toFixed(2));
		});

		$('.footerChanges').live("change", function overallDiscount() {
			var table = $(this).parent().parent().parent();
			var overallDiscount = parseFloat($('.overallDiscount').val());
			if (isNaN(overallDiscount)) {
				overallDiscount = 0;
			}
			updateTotalPI('.payableAmount', '.finalPayable', 0, table);
			var finalPayable = parseFloat(table.parent().find('.finalPayable').val().replace(/,/g, ''));
			var freightCharges = parseFloat(table.parent().find('.freightCharges').val().replace(/,/g, ''));
			if (isNaN(freightCharges)) {
				freightCharges = 0;
			}
			finalPayable -= overallDiscount;
			finalPayable += freightCharges;
			table.find('.finalPayable').val(finalPayable.toFixed(2));
		});

		$('.requiredFieldValidator').click(function(event) {
			var formId = event.target.parentElement.id;
			var qty = $("#"+formId).find('.receivedQuantity').val();
			var costPrice = $("#"+formId).find('.costPrice').val();
			var mrp = $("#"+formId).find('.mrp').val();
			
			var saveObj = $(this);
	        $(this).hide();
	        var bool = true;
			$('.receivedQuantity').each(function() {
	            var receivedQuantity = $(this).val();
	            if (receivedQuantity == null || receivedQuantity.trim(receivedQuantity) == "" || isNaN(receivedQuantity)) {
	                alert("Enter Received Quantity in correct format.");
	                bool = false;
	                saveObj.show();
	                return false;
	            }
	        });
			
			$('.costPrice').each(function() {
	            var costPrice = $(this).val();
	            if (costPrice == null || costPrice.trim(costPrice) == "" || isNaN(costPrice)) {
	                alert("Enter Cost Price in correct format.");
	                bool = false;
	                saveObj.show();
	                return false;
	            }
	            costPrice = parseFloat(costPrice);
	            var mrp = $(this).parent().parent().children('td').children('.mrp').val();
	            mrp = parseFloat(mrp);
	            if (mrp <= costPrice) {
	                alert("MRP can't be less than Cost Price");
	                bool = false;
	                $(this).val("");
	                $(this).parent().parent().children('td').children('.mrp').val("");
	                saveObj.show();
	                return false;
	            }

	        });
			if(formId=="rtvForm"){
			$('.mrp').each(function() {
	            var mrp = $(this).val();
	            if (mrp == null || mrp.trim(mrp) == "" || isNaN(mrp)) {
	                alert("Enter MRP in correct format.");
	                bool = false;
	                saveObj.show();
	                return false;
	            }
	        });
			}
			
			var invoiceDateString = $('#invoice-date').val();
			var dateValues = invoiceDateString.split("-");
			var invoiceDate = new Date();
			
			invoiceDate.setYear(parseInt(dateValues[0]));
			invoiceDate.setMonth(parseInt(dateValues[1])-1);
			invoiceDate.setDate(parseInt(dateValues[2]));

			if(invoiceDate > new Date()){
				alert("Invoice date cannot be in future");
				return false;
			}
			
			if (!bool) return false;
		});
		
		if($(".rtvTable").find(".rtvFinal").val()==""|| isNaN(parseFloat($(".rtvTable").find(".rtvFinal").val()))){
			populateRtvTable();
		}
		

		updateTotalPI('.receivedQuantity', '.totalQuantity', 1, $("#piTable"));
		updateTotalPI('.receivedQuantity', '.totalQuantity', 1, $("#piShortTable"));
		updateTotalPI('.receivedQuantity', '.totalQuantity', 1, $("#piRtvTable"));
		updateTotalPI('.taxableAmount', '.totalTaxable', 0, $("#piTable"));
		updateTotalPI('.taxAmount', '.totalTax', 0, $("#piTable"));
		updateTotalPI('.surchargeAmount', '.totalSurcharge', 0, $("#piTable"));
		updateTotalPI('.taxableAmount', '.totalTaxable', 0, $("#piShortTable"));
		updateTotalPI('.taxAmount', '.totalTax', 0, $("#piShortTable"));
		updateTotalPI('.surchargeAmount', '.totalSurcharge', 0, $("#piShortTable"));
		updateTotalPI('.taxableAmount', '.totalTaxable', 0, $("#piRtvTable"));
		updateTotalPI('.taxAmount', '.totalTax', 0, $("#piRtvTable"));
		updateTotalPI('.surchargeAmount', '.totalSurcharge', 0, $("#piRtvTable"));
		updateTotalPI('.payableAmount', '.totalPayable', 0, $("#piTable"));
		updateTotalPI('.payableAmount', '.totalPayable', 0, $("#piShortTable"));
		updateTotalPI('.payableAmount', '.totalPayable', 0, $("#piRtvTable"));
		updateTotalPI('.payableAmount', '.finalPayable', 0, $("#piTable"));
		updateTotalPI('.payableAmount', '.finalPayable', 0, $("#piShortTable"));
		updateTotalPI('.payableAmount', '.finalPayable', 0, $("#piRtvTable"));
	
		var shortTotal = $(".shortFinal").val();
		$("#shortTotal").val(shortTotal);
		var rtvTotal = parseFloat($('.rtvFinal').length!=0?($(".rtvFinal").val()!=null?$(".rtvFinal").val():0):0);
		$("#piRtvTotal").val(parseFloat($('.piTotal').val())+rtvTotal);
		
	});
</script>
</s:layout-component>
<s:layout-component name="heading">
	Edit Purchase Invoice of PI # ${pia.purchaseInvoice.id}
</s:layout-component>
<s:layout-component name="content">
<div style="display: none;">
	<s:link beanclass="com.hk.web.action.admin.inventory.PurchaseInvoiceAction" id="pvInfoLink"
	        event="getPVDetails"></s:link>
</div>

<div class="rtvListForm">
<fieldset style="height:200px"><legend><em>RTV Info</em></legend>
<br/>
<s:form beanclass="com.hk.web.action.admin.inventory.PurchaseInvoiceAction">
<s:hidden name="purchaseInvoice" value="${pia.purchaseInvoice}"/>
	<c:if test="${fn:length(pia.toImportRtvList) gt 0}">
	There are rtvs attached with the PI. Select to import them.<br/>
	<table>
	<c:forEach items="${pia.toImportRtvList}" var="rtv" varStatus="ctr">
	<td>${ctr.index+1}. Purchase Order No. ${rtv.extraInventory.purchaseOrder.id}, RTV Id. ${rtv.id}</td>
	<td><s:checkbox name="rtvId[${ctr.index}]" value="${rtv.id}" class="purchaseLineItemCheckBox"/></td>
	</c:forEach>
	<tr><td colspan="2"><s:submit name="importRtv" value="Import Rtv"></s:submit></td></tr>
	</table>
	</c:if>
	<c:if test="${fn:length(pia.toImportRtvList) eq 0}">
		There are no rtvs attached with the PI.
	</c:if>
</s:form>
</fieldset>
</div>


<s:form id="piForm" beanclass="com.hk.web.action.admin.inventory.PurchaseInvoiceAction">
<s:hidden name="purchaseInvoice" value="${pia.purchaseInvoice}"/>
<table>
	<tr><td>Warehouse :</td>
		<td><strong>${pia.purchaseInvoice.warehouse.city}</strong></td>
	</tr>
	<tr>
		<td>Supplier Name</td>
		<td>${pia.purchaseInvoice.supplier.name}</td>

		<td>Supplier State</td>
		<td id="supplierState">${pia.purchaseInvoice.supplier.state}</td>

		<td>Tax</td>
		<td>
			<c:choose>
				<c:when test="${pia.purchaseInvoice.supplier.state == pia.purchaseInvoice.warehouse.state}">
					<label class="state">NON-CST</label>
				</c:when>
				<c:otherwise>
					<label class="state">CST</label>
				</c:otherwise>
			</c:choose>
		</td>

	</tr>

	<tr>
		<td>Invoice Date</td>
		<td>
			<s:text class="date_input" formatPattern="yyyy-MM-dd" name="purchaseInvoice.invoiceDate" id="invoice-date"/></td>
		<td>Invoice Number</td>
		<td><s:text name="purchaseInvoice.invoiceNumber"/></td>
		<td>GRN Date</td>
		<td>${pia.grnDate}</td>

	</tr>

	<tr>
		<td>Credit Days</td>
		<td>${pia.purchaseInvoice.supplier.creditDays}</td>
		<td>Adv .Payment</td>
		<td>
			<c:set var="advPayment" value="0"/>
			<c:forEach items="${pia.purchaseInvoice.goodsReceivedNotes}" var="grn">
				<c:set var="advPayment" value="${grn.purchaseOrder.advPayment + advPayment}"/>
				${advPayment}
			</c:forEach>

		</td>
		<td>Payable</td>
		<td><fmt:formatNumber value="${pia.purchaseInvoice.finalPayableAmount - advPayment}" type="currency" currencySymbol=" " maxFractionDigits="0"/></td>

	</tr>

	<tr>
		<td>Est. Payment Date</td>
		<td><fmt:formatDate value="${pia.purchaseInvoice.estPaymentDate}"/></td>
		<td>Payment Date</td>
		<td>
			<s:text class="date_input" formatPattern="yyyy-MM-dd" name="purchaseInvoice.paymentDate" id="payment-date"/></td>
		<td>Payment Details<br/><span class="sml gry">(eg. Cheque no.)</span></td>
		<td><s:textarea name="purchaseInvoice.paymentDetails" style="height:50px;"/></td>
	</tr>
	<tr>
		<td>Generated By</td>
		<td>
				${pia.purchaseInvoice.createdBy.name}</td>
		<td>Reconciled</td>
		<td><s:checkbox id="reconciled" name="purchaseInvoice.reconciled"/></td>
		<td>Reconcilation Date</td>
		<c:choose>
			<c:when test="${hk:isNotBlank(pia.purchaseInvoice.reconcilationDate)}">
				<td>${pia.purchaseInvoice.reconcilationDate}</td>
			</c:when>
			<c:otherwise>
				<td></td>
			</c:otherwise>
		</c:choose>

	</tr>

	<tr>
		<td>Form Type</td>
		<td>
			<s:select name="purchaseInvoice.purchaseFormType" class="purchaseFormType">
				<s:option value="">-Select-</s:option>
				<c:forEach items="${purchaseFormTypes}" var="purchaseFormTyp">
					<s:option value="${purchaseFormTyp.id}">${purchaseFormTyp.name}</s:option>
				</c:forEach>
			</s:select>
		</td>
		<td>Status</td>
		<td><s:select name="purchaseInvoice.purchaseInvoiceStatus"  id="status"
		              value="${pia.purchaseInvoice.purchaseInvoiceStatus.id}">
			<hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="purchaseInvoiceStatusList"
			                           value="id" label="name"/>
		</s:select></td>
		<td>Route Permit Number</td>
		<td><s:text name="purchaseInvoice.routePermitNumber"/></td>
		<td></td>
		<td></td>
	</tr>
</table>

<br/>

<table border="1">
	<thead>
	<tr>
		<th>S.No.</th>
		<th></th>
		<th>VariantID</th>
		<th>UPC</th>
		<th>Details</th>
		<c:choose>
			<c:when test="${pia.purchaseInvoice.supplier.state == pia.purchaseInvoice.warehouse.state}">
				<th>Tax<br/>Category</th>
			</c:when>
			<c:otherwise>
				<th>Surcharge<br/>Category</th>
			</c:otherwise>
		</c:choose>
		<th>Received Qty<br/>(Adjust -)</th>
		<th>Cost Price<br/>(Without TAX)</th>
		<th>MRP</th>
		<th>Discount<br/>(%)</th>
		<th>Taxable</th>
		<th>Tax</th>
		<th>Surcharge</th>
		<th>Payable</th>

	</tr>
	</thead>
	<tbody id="piTable">
	<s:hidden name="piFinalPayable"
		          value="${pia.purchaseInvoice.finalPayableAmount}"/>
	<c:forEach var="purchaseInvoiceLineItem" items="${pia.purchaseInvoiceLineItems}" varStatus="ctr">
		<c:set value="${purchaseInvoiceLineItem.sku}" var="sku"/>
		<c:set value="${sku.productVariant}" var="productVariant"/>
		<c:set value="${productVariant.product}" var="product"/>
		<s:hidden name="purchaseInvoiceLineItems[${ctr.index}]" value="${purchaseInvoiceLineItem.id}"/>
		<s:hidden name="purchaseInvoiceLineItems[${ctr.index}].productVariant"
		          value="${purchaseInvoiceLineItem.sku.productVariant.id}"/>
		
		<%--<s:hidden name="purchaseInvoiceLineItems[${ctr.index}].sku" value="${purchaseInvoiceLineItem.sku.id}"/>--%>
		<tr count="${ctr.index}" class="${ctr.last ? 'lastRow lineItemRow':'lineItemRow'}">
			<td>${ctr.index+1}.</td>
			<td>
				<div class='img48' style="vertical-align:top;">
					<c:choose>
						<c:when test="${product.mainImageId != null}">
							<hk:productImage imageId="${product.mainImageId}"
							                 size="<%=EnumImageSize.TinySize%>"/>
						</c:when>
						<c:otherwise>
							<img class="prod48"
							     src="${pageContext.request.contextPath}/images/ProductImages/ProductImagesThumb/${product.id}.jpg"
							     alt="${productLineItem.productVariant.product.name}"/>
						</c:otherwise>
					</c:choose>
				</div>
			</td>
			<td>
					${productVariant.id}
					<span class="shortLink"><a href="" id="createShortLink" class="addRowButton" style="font-size:1.2em">Create Short</a></span>
				<s:hidden class="variant" name="purchaseInvoiceLineItems[${ctr.index}].sku.productVariant"
				          value="${productVariant.id}"/>
			</td>
			<td>${productVariant.upc}</td>
			<td class="variantName">${product.name}<br/>${productVariant.optionsCommaSeparated}
			</td>
			<td class="taxCategory">

				<shiro:hasPermission name="<%=PermissionConstants.UPDATE_RECONCILIATION_REPORTS%>">
					<input type="hidden" value="finance"
					       class="taxIdentifier"/>
					<c:choose>
						<c:when test="${pia.purchaseInvoice.supplier.state == pia.purchaseInvoice.warehouse.state}">
							<s:select name="purchaseInvoiceLineItems[${ctr.index}].tax"
							          value="${purchaseInvoiceLineItem.tax.id}" class="valueChange">
								<hk:master-data-collection service="<%=TaxDao.class%>" serviceProperty="taxList"
								                           value="id"
								                           label="name"/>
							</s:select>
						</c:when>
						<c:otherwise>
							<s:select name="purchaseInvoiceLineItems[${ctr.index}].surcharge"
							          value="${purchaseInvoiceLineItem.surcharge.id}" class="valueChange">
								<hk:master-data-collection service="<%=MasterDataDao.class%>"
								                           serviceProperty="surchargeList" value="id"
								                           label="value"/>
							</s:select>
						</c:otherwise>
					</c:choose>
				</shiro:hasPermission>
				<shiro:lacksPermission name="<%=PermissionConstants.UPDATE_RECONCILIATION_REPORTS%>">
					<c:choose>
						<c:when test="${pia.purchaseInvoice.supplier.state==pia.purchaseInvoice.warehouse.state}">
							<s:text name="purchaseInvoiceLineItems[${ctr.index}].tax"
							        value="${purchaseInvoiceLineItem.tax.value}" class="taxCategory"/>
						</c:when>
						<c:otherwise>
							<s:text name="purchaseInvoiceLineItems[${ctr.index}].surcharge"
							        value="${purchaseInvoiceLineItem.surcharge.value}" class="taxCategory"/>
						</c:otherwise>
					</c:choose>
				</shiro:lacksPermission>
			</td>
			<td>
				<s:text name="purchaseInvoiceLineItems[${ctr.index}].qty" value="${purchaseInvoiceLineItem.qty}"
				        class="receivedQuantity valueChange"/>
			</td>
			<td>
				<s:text name="purchaseInvoiceLineItems[${ctr.index}].costPrice"
				        value="${purchaseInvoiceLineItem.costPrice}"
				        class="costPrice valueChange"/>
			</td>
			<td>
				<s:text class="mrp" name="purchaseInvoiceLineItems[${ctr.index}].mrp"
				        value="${purchaseInvoiceLineItem.mrp}"/>
			</td>
			<td>
				<s:text class="discountPercentage valueChange"
				        name="purchaseInvoiceLineItems[${ctr.index}].discountPercent"
				        value="${purchaseInvoiceLineItem.discountPercent}"/>
			</td>
			<td>
				<s:text readonly="readonly" class="taxableAmount"
				        name="purchaseInvoiceLineItems[${ctr.index}].taxableAmount"
				        value="${purchaseInvoiceLineItem.taxableAmount}"/>
			</td>
			<td>
				<s:text readonly="readonly" class="taxAmount" name="purchaseInvoiceLineItems[${ctr.index}].taxAmount"
				        value="${purchaseInvoiceLineItem.taxAmount}"/>
			</td>
			<td>
				<s:text readonly="readonly" class="surchargeAmount"
				        name="purchaseInvoiceLineItems[${ctr.index}].surchargeAmount"
				        value="${purchaseInvoiceLineItem.surchargeAmount}"/>
			</td>
			<td>

				<s:text readonly="readonly" class="payableAmount"
				        name="purchaseInvoiceLineItems[${ctr.index}].payableAmount"
				        value="${purchaseInvoiceLineItem.payableAmount}"/>
			</td>
		</tr>
	</c:forEach>
	</tbody>
	<tfoot>
	<tr>
		<td colspan="6">Totals</td>
		<td colspan="4" class="totalQuantity"></td>
		<td><s:text readonly="readonly" class="totalTaxable" name="purchaseInvoice.taxableAmount"
		            value="${pia.purchaseInvoice.taxableAmount}"/></td>
		<td><s:text readonly="readonly" class="totalTax" name="purchaseInvoice.taxAmount"
		            value="${pia.purchaseInvoice.taxAmount}"/></td>
		<td><s:text readonly="readonly" class="totalSurcharge" name="purchaseInvoice.surchargeAmount"
		            value="${pia.purchaseInvoice.surchargeAmount}"/></td>
		<td><s:text readonly="readonly" class="totalPayable" name="purchaseInvoice.payableAmount"
		            value="${pia.purchaseInvoice.payableAmount}"/></td>
	</tr>
	<tr>
		<td colspan="12"></td><td>Overall Discount<br/>(In Rupees)</td>
		<td><s:text class="overallDiscount footerChanges" name="purchaseInvoice.discount"
		            value="${pia.purchaseInvoice.discount}"/></td>
	</tr>
	<tr>
	<tr>
		<td colspan="12"></td><td>Freight and Forwarding<br/>Charges(In Rupees)</td>
		<td><s:text class="freightCharges footerChanges" name="purchaseInvoice.freightForwardingCharges"
		            value="${pia.purchaseInvoice.freightForwardingCharges}"/></td>
	</tr>
	<tr>
		<td colspan="12"></td><td>Final Payable</td>
		<td><s:text readonly="readonly" class="finalPayable piTotal" name="purchaseInvoice.finalPayableAmount"
		            value="${pia.purchaseInvoice.finalPayableAmount}"/></td>
	</tr>
	</tfoot>
</table>
<div class="variantDetails info"></div>
<br/>
<c:if test="${pia.saveEnabled}">
	<a href="purchaseInvoice.jsp#" class="addRowButton" style="font-size:1.2em">Add new row</a>

	<s:submit name="save" value="Save" class="requiredFieldValidator" id="save-button"/>
</c:if>
<shiro:hasRole name="<%=RoleConstants.FINANCE_ADMIN%>">
	<s:submit name="delete" value="Delete"/>
</shiro:hasRole>
</s:form>

<br>
<br>
<p style="font-weight: bold;font-size: medium;">Short Line Items</p>
<s:form id="shortForm" beanclass="com.hk.web.action.admin.inventory.PurchaseInvoiceAction">
<s:hidden name="purchaseInvoice" value="${pia.purchaseInvoice}"/>
<table border="1" class="purchaseInvoiceShortTable">
	<thead>
	<tr>
		<th>S.No.</th>
		<th></th>
		<th>VariantID</th>
		<th>UPC</th>
		<th>Details</th>
		<c:choose>
			<c:when test="${pia.purchaseInvoice.supplier.state == pia.purchaseInvoice.warehouse.state}">
				<th>Tax<br/>Category</th>
			</c:when>
			<c:otherwise>
				<th>Surcharge<br/>Category</th>
			</c:otherwise>
		</c:choose>
		<th>Short Qty</th>
		<th>Cost Price<br/>(Without TAX)</th>
		<th>MRP</th>
		<th>Discount<br/>(%)</th>
		<th>Taxable</th>
		<th>Tax</th>
		<th>Surcharge</th>
		<th>Payable</th>

	</tr>
	</thead>
	<tbody id="piShortTable">
	<c:forEach var="purchaseInvoiceLineItem" items="${pia.purchaseInvoiceShortLineItems}" varStatus="ctr">
		<c:set value="${purchaseInvoiceLineItem.sku}" var="sku"/>
		<c:set value="${sku.productVariant}" var="productVariant"/>
		<c:set value="${productVariant.product}" var="product"/>
		<s:hidden name="purchaseInvoiceShortLineItems[${ctr.index}]" value="${purchaseInvoiceLineItem.id}"/>
		<s:hidden name="purchaseInvoiceShortLineItems[${ctr.index}].productVariant"
		          value="${purchaseInvoiceLineItem.sku.productVariant.id}"/>
		<tr  count="${ctr.index}" class="${ctr.last ? 'lastShortRow lineItemRow shortTableTr':'lineItemRow shortTableTr'}">
			<td>${ctr.index+1}</td>
			<td>
				<div class='img48' style="vertical-align:top;">
					<c:choose>
						<c:when test="${product.mainImageId != null}">
							<hk:productImage imageId="${product.mainImageId}"
							                 size="<%=EnumImageSize.TinySize%>"/>
						</c:when>
						<c:otherwise>
							<img class="prod48"
							     src="${pageContext.request.contextPath}/images/ProductImages/ProductImagesThumb/${product.id}.jpg"
							     alt="${productLineItem.productVariant.product.name}"/>
						</c:otherwise>
					</c:choose>
				</div>
			</td>
			<td class="variant">${productVariant.id}</td>
			<td>${productVariant.upc}</td>
			<td class="variantName ">${product.name}<br/>${productVariant.optionsCommaSeparated}
			</td>
			<td class="taxCategory">

				<shiro:hasPermission name="<%=PermissionConstants.UPDATE_RECONCILIATION_REPORTS%>">
					<input type="hidden" value="finance"
					       class="taxIdentifier"/>
					<c:choose>
						<c:when test="${pia.purchaseInvoice.supplier.state == pia.purchaseInvoice.warehouse.state}">
							<s:select name="purchaseInvoiceShortLineItems[${ctr.index}].tax" id="taxValues"
							          value="${purchaseInvoiceLineItem.tax.id}" class="valueChange">
								<hk:master-data-collection service="<%=TaxDao.class%>" serviceProperty="taxList"
								                           value="id"
								                           label="name"/>
							</s:select>
						</c:when>
						<c:otherwise>
							<s:select name="purchaseInvoiceShortLineItems[${ctr.index}].surcharge" id="taxValues"
							          value="${purchaseInvoiceLineItem.surcharge.id}" class="valueChange">
								<hk:master-data-collection service="<%=MasterDataDao.class%>"
								                           serviceProperty="surchargeList" value="id"
								                           label="value"/>
							</s:select>
						</c:otherwise>
					</c:choose>
				</shiro:hasPermission>
				<shiro:lacksPermission name="<%=PermissionConstants.UPDATE_RECONCILIATION_REPORTS%>">
					<c:choose>
						<c:when test="${pia.purchaseInvoice.supplier.state==pia.purchaseInvoice.warehouse.state}">
							<s:text name="purchaseInvoiceShortLineItems[${ctr.index}].tax"
							        value="${purchaseInvoiceLineItem.tax.value}" class="taxCategory"/>
						</c:when>
						<c:otherwise>
							<s:text name="purchaseInvoiceShortLineItems[${ctr.index}].surcharge"
							        value="${purchaseInvoiceLineItem.surcharge.value}" class="taxCategory"/>
						</c:otherwise>
					</c:choose>
				</shiro:lacksPermission>
			</td>
			<td>
				<s:text name="purchaseInvoiceShortLineItems[${ctr.index}].qty" value="${purchaseInvoiceLineItem.qty}"
				        class="receivedQuantity valueChange"/>
			</td>
			<td>
				<s:text name="purchaseInvoiceShortLineItems[${ctr.index}].costPrice"
				        value="${purchaseInvoiceLineItem.costPrice}"
				        class="costPrice valueChange"/>
			</td>
			<td>
				<s:text class="mrp" name="purchaseInvoiceShortLineItems[${ctr.index}].mrp"
				        value="${purchaseInvoiceLineItem.mrp}"/>
			</td>
			<td>
				<s:text class="discountPercentage valueChange"
				        name="purchaseInvoiceShortLineItems[${ctr.index}].discountPercent"
				        value="${purchaseInvoiceLineItem.discountPercent}"/>
			</td>
			<td>
				<s:text readonly="readonly" class="taxableAmount"
				        name="purchaseInvoiceShortLineItems[${ctr.index}].taxableAmount"
				        value="${purchaseInvoiceLineItem.taxableAmount}"/>
			</td>
			<td>
				<s:text readonly="readonly" class="taxAmount" name="purchaseInvoiceShortLineItems[${ctr.index}].taxAmount"
				        value="${purchaseInvoiceLineItem.taxAmount}"/>
			</td>
			<td>
				<s:text readonly="readonly" class="surchargeAmount"
				        name="purchaseInvoiceShortLineItems[${ctr.index}].surchargeAmount"
				        value="${purchaseInvoiceLineItem.surchargeAmount}"/>
			</td>
			<td>

				<s:text readonly="readonly" class="payableAmount"
				        name="purchaseInvoiceShortLineItems[${ctr.index}].payableAmount"
				        value="${purchaseInvoiceLineItem.payableAmount}"/>
			</td>
		</tr>
	</c:forEach>
	</tbody>
	<tfoot id="piShortTableFoot">
	<tr>
		<td colspan="6">Totals</td>
		<td colspan="4" class="totalQuantity"></td>
		<td><input readonly="readonly" class="totalTaxable" name="shortTaxableAmount"/></td>
		<td><input readonly="readonly" class="totalTax" name="shortTaxAmount"/></td>
		<td><input readonly="readonly" class="totalSurcharge" name="shortSurchargeAmount"/></td>
		<td><input readonly="readonly" class="totalPayable" name="shortPayableAmount"/></td>
	</tr>
	<tr>
		<td colspan="12"></td><td>Overall Discount<br/>(In Rupees)</td>
		<td><input class="overallDiscount footerChanges" name="shortDiscount"/></td>
	</tr>
	<tr>
	<tr>
		<td colspan="12"></td><td>Freight and Forwarding<br/>Charges(In Rupees)</td>
		<td><input class="freightCharges footerChanges" name="shortFreightForwardingCharges"/></td>
	</tr>
	<tr>
		<td colspan="12"></td><td>Short Payable</td>
		<td><s:text readonly="readonly" class="finalPayable shortFinal" name="shortTotalPayable"
		            value="${pia.purchaseInvoice.finalPayableAmount}"/></td>
	</tr>
	</tfoot>
</table>
<div class="variantDetails info"></div>
	<s:submit name="saveShortLineItems" value="Save" class="requiredFieldValidator" id="save-button"/>
</s:form>

<br/>
<br/>
<s:form id="rtvForm" beanclass="com.hk.web.action.admin.inventory.PurchaseInvoiceAction">
<s:hidden name="purchaseInvoice" value="${pia.purchaseInvoice}"/>
<p style="font-weight: bold;font-size: medium;">RTV Line Items</p>

<table id="rtvTable" class="rtvTable">
<thead>
	<tr>
		<th>S.No.</th>
		<th></th>
		<th>VariantID</th>
		<th>UPC</th>
		<th>Details</th>
		<c:choose>
			<c:when test="${pia.purchaseInvoice.supplier.state == pia.purchaseInvoice.warehouse.state}">
				<th>Tax<br/>Category</th>
			</c:when>
			<c:otherwise>
				<th>Surcharge<br/>Category</th>
			</c:otherwise>
		</c:choose>
		<th>Received Qty</th>
		<th>Cost Price<br/>(Without TAX)</th>
		<th>MRP</th>
		<th>Taxable</th>
		<th>Tax</th>
		<th>Surcharge</th>
		<th>Payable</th>
		<th>Comment</th>
	</tr>
	</thead>
	<tbody id="piRtvTable">
	<c:forEach var="extraInventoryLineItem" items="${pia.extraInventoryLineItems}" varStatus="ctr">
	<tr count="${ctr.index}" class="${ctr.last ? 'lastRtvRow lineItemRow rtvTableTr':'lineItemRow rtvTableTr'}">
	    <s:hidden name="extraInventoryLineItems[${ctr.index}].id" value="${extraInventoryLineItem.id}"/>
		<s:hidden name="extraInventoryId" value="${extraInventoryLineItem.extraInventory.id}" />
		<s:hidden name="extraInventoryLineItems[${ctr.index}].grnCreated" value="${extraInventoryLineItem.grnCreated}"/>
		<s:hidden name="extraInventoryLineItems[${ctr.index}].rtvCreated" value="${extraInventoryLineItem.rtvCreated}"/>
		<s:hidden name="extraInventoryLineItems[${ctr.index}].poLineItem" value="${extraInventoryLineItem.poLineItem}"/>
		<s:hidden name="extraInventoryLineItems[${ctr.index}].updateDate" value="${extraInventoryLineItem.updateDate}"/>
		<c:choose>
						<c:when test="${extraInventoryLineItem.sku != null}">
		<c:set value="${extraInventoryLineItem.sku}" var="sku"/>
		<c:set value="${sku.productVariant}" var="productVariant"/>
		<c:set value="${productVariant.product}" var="product"/>
		<s:hidden name="extraInventoryLineItems[${ctr.index}].productVariant"
		          value="${extraInventoryLineItem.sku.productVariant.id}"/>
		<s:hidden name="extraInventoryLineItems[${ctr.index}].sku"
		          value="${extraInventoryLineItem.sku}"/>
		          
		
			<td>${ctr.index+1}.${extraInventoryLineItem.extraInventory.id}	</td>
			<td>
				<div class='img48' style="vertical-align:top;">
					<c:choose>
						<c:when test="${product.mainImageId != null}">
							<hk:productImage imageId="${product.mainImageId}"
							                 size="<%=EnumImageSize.TinySize%>"/>
						</c:when>
						<c:otherwise>
							<img class="prod48"
							     src="${pageContext.request.contextPath}/images/ProductImages/ProductImagesThumb/${product.id}.jpg"
							     alt="${productLineItem.productVariant.product.name}"/>
						</c:otherwise>
					</c:choose>
				</div>
			</td>
			<td>${productVariant.id}</td>
			<td>${productVariant.upc}</td>
			<td>
                            <s:hidden class="productName" name="extraInventoryLineItems[${ctr.index}].productName"
                                      value="${product.name}"/>
                            ${product.name}
                                   <br/>${productVariant.optionsCommaSeparated}
                </td>
			</c:when>
			<c:otherwise>
				<td>${ctr.index+1}</td>
				<td></td>
				<td></td>
				<td></td>
				<td>
				 <s:hidden class="productName" name="extraInventoryLineItems[${ctr.index}].productName"
                                      value="${extraInventoryLineItem.productName}"/>
                            ${extraInventoryLineItem.productName}
				</td>
			</c:otherwise>
			</c:choose>
			
			<td class="taxCategory">

				<shiro:hasPermission name="<%=PermissionConstants.UPDATE_RECONCILIATION_REPORTS%>">
					<input type="hidden" value="finance"
					       class="taxIdentifier"/>
					<c:choose>
						<c:when test="${pia.purchaseInvoice.supplier.state == pia.purchaseInvoice.warehouse.state}">
							<s:select name="extraInventoryLineItems[${ctr.index}].tax" id="taxValues"
							          value="${extraInventoryLineItem.tax.id}" class="valueChange">
								<hk:master-data-collection service="<%=TaxDao.class%>" serviceProperty="taxList"
								                           value="id"
								                           label="name"/>
							</s:select>
						</c:when>
						<c:otherwise>
							<s:select name="extraInventoryLineItems[${ctr.index}].surcharge" id="taxValues"
							          value="${extraInventoryLineItem.surcharge.id}" class="valueChange">
								<hk:master-data-collection service="<%=MasterDataDao.class%>"
								                           serviceProperty="surchargeList" value="id"
								                           label="value"/>
							</s:select>
						</c:otherwise>
					</c:choose>
				</shiro:hasPermission>
				<shiro:lacksPermission name="<%=PermissionConstants.UPDATE_RECONCILIATION_REPORTS%>">
					<c:choose>
						<c:when test="${pia.purchaseInvoice.supplier.state==pia.purchaseInvoice.warehouse.state}">
							<s:text name="extraInventoryLineItems[${ctr.index}].tax"
							        value="${extraInventoryLineItem.tax.value}" class="taxCategory"/>
						</c:when>
						<c:otherwise>
							<s:text name="extraInventoryLineItems[${ctr.index}].surcharge"
							        value="${extraInventoryLineItem.surcharge.value}" class="taxCategory"/>
						</c:otherwise>
					</c:choose>
				</shiro:lacksPermission>
			</td>
			<td>
				 ${extraInventoryLineItem.receivedQty}
                            <s:hidden class="receivedQuantity valueChange"
                                      name="extraInventoryLineItems[${ctr.index}].receivedQty"
                                      value="${extraInventoryLineItem.receivedQty}"/>
			</td>
			<td>
				<s:text name="extraInventoryLineItems[${ctr.index}].costPrice"
				        value="${extraInventoryLineItem.costPrice}"
				        class="costPrice valueChange"/>
			</td>
			<td>
				<s:text class="mrp valueChange" name="extraInventoryLineItems[${ctr.index}].mrp"
				        value="${extraInventoryLineItem.mrp}"/>
			</td>
			<td>
				<s:text readonly="readonly" class="taxableAmount"
				        name="extraInventoryLineItems[${ctr.index}].taxableAmount"
				        value="${extraInventoryLineItem.taxableAmount}"/>
			</td>
			<td>
				<s:text readonly="readonly" class="taxAmount" name="extraInventoryLineItems[${ctr.index}].taxAmount"
				        value="${extraInventoryLineItem.taxAmount}"/>
			</td>
			<td>
				<s:text readonly="readonly" class="surchargeAmount"
				        name="extraInventoryLineItems[${ctr.index}].surchargeAmount"
				        value="${extraInventoryLineItem.surchargeAmount}"/>
			</td>
			<td>

				<s:text readonly="readonly" class="payableAmount"
				        name="extraInventoryLineItems[${ctr.index}].payableAmount"
				        value="${extraInventoryLineItem.payableAmount}"/>
			</td>
			
			<c:choose>
			<c:when test="${extraInventoryLineItem.remarks!=null}">
			<td><s:text style="height:60px; width:210px;" name="extraInventoryLineItems[${ctr.index}].remarks" value="${extraInventoryLineItem.remarks}"/></td>
			</c:when>
			<c:otherwise><td><s:text name="extraInventoryLineItems[${ctr.index}].remarks" value=""/></td></c:otherwise>
			</c:choose>
			
		</tr>
	</c:forEach>
	</tbody>
	<tfoot id="piRtvTableFoot">
		<tr>
		<td colspan="6">Totals</td>
		<td colspan="3" class="totalQuantity"></td>
		<td><input readonly="readonly" class="totalTaxable" name="rtvTaxableAmount"/></td>
		<td><input readonly="readonly" class="totalTax" name="rtvTotalTax"/></td>
		<td><input readonly="readonly" class="totalSurcharge" id="rtvTotalSurcharge"/></td>
		<td colspan="2" ><input readonly="readonly" class="totalPayable" name="rtvTotalPayable"/></td>
	</tr>
	<tr>
		<td colspan="11"></td><td>RTV Final Total</td>
		<td><input readonly="readonly" class="finalPayable rtvFinal" name="rtvTotalPayable"/></td>
		            <td></td>
	</tr>
	</tfoot>
</table>
<s:submit name="saveRtv" value="Save" class="requiredFieldValidator" id="save-button"/>
</s:form>

<div id = "finalPayableDiv">
<fieldset>
<legend><br/><em>Final Payable Amounts</em></legend>
<table >
<tr>
<th></th>
<th>PI+RTV TOTAL</th>
<th>SHORT TOTAL</th>
</tr>
<tr>
<td><label>Final Payable Amounts</label></td>
<td><input id= "piRtvTotal" readonly="readonly" /></td>
<td><input id= "shortTotal" readonly="readonly" /></td>
</tr>
</table>
</fieldset>
</div>


<div id="closeButtonDiv">
<s:link beanclass="com.hk.web.action.admin.inventory.PurchaseInvoiceAction" event="close" Value="Close" class="button_green addToCartButton" > Close </s:link>
</div>
</s:layout-component>
</s:layout-render>