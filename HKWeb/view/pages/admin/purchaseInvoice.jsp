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
<s:useActionBean beanclass="com.hk.web.action.admin.inventory.PurchaseInvoiceAction" var="pia"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Purchase Invoice">

<%
	TaxDao taxDao = ServiceLocatorFactory.getService(TaxDao.class);
	List<Tax> taxList = taxDao.getTaxList();
	pageContext.setAttribute("taxList", taxList);

	MasterDataDao masterDataDao = (MasterDataDao) ServiceLocatorFactory.getService(MasterDataDao.class);
	List<PurchaseFormType> purchaseFormTypes = masterDataDao.getPurchaseInvoiceFormTypes();
	pageContext.setAttribute("purchaseFormTypes", purchaseFormTypes);
%>


<s:layout-component name="htmlHead">
<link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
<jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
<script type="text/javascript">
	$(document).ready(function() {


		function updateTotal(fromTotalClass, toTotalClass, toHtml) {
			var total = 0;
			$.each($(fromTotalClass), function(index, value) {
				var eachRow = $(value);
				var eachRowValue = eachRow.val().trim();
				total += parseFloat(eachRowValue);
			});
			if (toHtml == 1) {
				$(toTotalClass).html(total);
			} else {
				$(toTotalClass).val(total.toFixed(2));
			}
		}

		;


		$('.addRowButton').click(function() {

			var lastIndex = $('.lastRow').attr('count');
			if (!lastIndex) {
				lastIndex = -1;
			}
			$('.lastRow').removeClass('lastRow');

			var nextIndex = eval(lastIndex + "+1");

			var taxOptions = "";
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

			var newRowHtml =
					'<tr count="' + nextIndex + '" class="lastRow lineItemRow">' +
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

			$('#piTable').append(newRowHtml);
			return false;
		});

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

		$('.valueChange').live("change", function() {
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
			var discountPercentage = valueChangeRow.find('.discountPercentage').val();
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

			updateTotal('.taxableAmount', '.totalTaxable', 0);
			updateTotal('.taxAmount', '.totalTax', 0);
			updateTotal('.surchargeAmount', '.totalSurcharge', 0);
			updateTotal('.payableAmount', '.totalPayable', 0);
			updateTotal('.payableAmount', '.finalPayable', 0);
			updateTotal('.receivedQuantity', '.totalQuantity', 1);
			var finalPayable = parseFloat($('.finalPayable').val().replace(/,/g, ''));
			var overallDiscount = parseFloat($('.overallDiscount').val().replace(/,/g, ''));
			var freightCharges = parseFloat($('.freightCharges').val().replace(/,/g, ''));
			if (isNaN(overallDiscount)) {
				overallDiscount = 0;
			}
			if (isNaN(freightCharges)) {
				freightCharges = 0;
			}
			finalPayable -= overallDiscount;
			finalPayable += freightCharges;
			$('.finalPayable').val(finalPayable.toFixed(2));
		});

		$('.footerChanges').live("change", function overallDiscount() {
			var overallDiscount = parseFloat($('.overallDiscount').val());
			if (isNaN(overallDiscount)) {
				overallDiscount = 0;
			}
			updateTotal('.payableAmount', '.finalPayable', 0);
			var finalPayable = parseFloat($('.finalPayable').val().replace(/,/g, ''));
			var freightCharges = parseFloat($('.freightCharges').val().replace(/,/g, ''));
			if (isNaN(freightCharges)) {
				freightCharges = 0;
			}
			finalPayable -= overallDiscount;
			finalPayable += freightCharges;
			$('.finalPayable').val(finalPayable.toFixed(2));
		});

		$('.requiredFieldValidator').click(function() {
			var qty = $('.receivedQuantity').val();
			var costPrice = $('.costPrice').val();
			if (qty == "" || costPrice == "") {
				alert("All fields are compulsory.");
				return false;
			}
		});

		updateTotal('.receivedQuantity', '.totalQuantity', 1);

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

<s:form beanclass="com.hk.web.action.admin.inventory.PurchaseInvoiceAction">
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
			<s:text class="date_input" formatPattern="yyyy-MM-dd" name="purchaseInvoice.invoiceDate"/></td>
		<td>Invoice Number</td>
		<td><s:text name="purchaseInvoice.invoiceNumber"/></td>
		<td>GRN Date</td>
		<td>${pia.grnDate}</td>

	</tr>

	<tr>
		<td>Est. Payment Date</td>
		<td><fmt:formatDate value="${pia.purchaseInvoice.estPaymentDate}"/></td>
		<td>Payment Date</td>
		<td>
			<s:text class="date_input" formatPattern="yyyy-MM-dd" name="purchaseInvoice.paymentDate"/></td>
		<td>Payment Details<br/><span class="sml gry">(eg. Cheque no.)</span></td>
		<td><s:textarea name="purchaseInvoice.paymentDetails" style="height:50px;"/></td>
	</tr>
	<tr>
		<td>Generated By</td>
		<td>
				${pia.purchaseInvoice.createdBy.name}</td>
		<td>Reconciled</td>
		<td><s:checkbox name="purchaseInvoice.reconciled"/></td>
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
		<td><s:select name="purchaseInvoice.purchaseInvoiceStatus"
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
	<c:forEach var="purchaseInvoiceLineItem" items="${pia.purchaseInvoice.purchaseInvoiceLineItems}" varStatus="ctr">
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
				<s:hidden class="variant" name="purchaseInvoiceLineItems[${ctr.index}].sku.productVariant"
				          value="${productVariant.id}"/>
			</td>
			<td>${productVariant.upc}</td>
			<td>${product.name}<br/>${productVariant.optionsCommaSeparated}
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
		<td><s:text readonly="readonly" class="finalPayable" name="purchaseInvoice.finalPayableAmount"
		            value="${pia.purchaseInvoice.finalPayableAmount}"/></td>
	</tr>
	</tfoot>
</table>
<div class="variantDetails info"></div>
<br/>
<c:if test="${pia.saveEnabled}">
	<a href="purchaseInvoice.jsp#" class="addRowButton" style="font-size:1.2em">Add new row</a>

	<s:submit name="save" value="Save" class="requiredFieldValidator"/>
</c:if>
<shiro:hasRole name="<%=RoleConstants.FINANCE_ADMIN%>">
	<s:submit name="delete" value="Delete"/>
</shiro:hasRole>
</s:form>

</s:layout-component>

</s:layout-render>
