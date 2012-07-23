<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.pact.dao.warehouse.WarehouseDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.inventory.GRNAction" var="pa"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="GRN">
<%
    WarehouseDao warehouseDao = ServiceLocatorFactory.getService(WarehouseDao.class);
    pageContext.setAttribute("whList", warehouseDao.getAllWarehouses());
  %>
<s:layout-component name="htmlHead">

	<link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
	<jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
	<script type="text/javascript">
		$(document).ready(function() {

			function updateTotal(fromTotalClass,toTotalClass){
				var total=0;
				$.each($(fromTotalClass),function(index,value){
					var eachRow=$(value);
					var eachRowValue=eachRow.val().trim();
					total+=parseFloat(eachRowValue);
				});
				$(toTotalClass).html(total);
			};


			$('.addRowButton').click(function() {

				var lastIndex = $('.lastRow').attr('count');
				if (!lastIndex) {
					lastIndex = -1;
				}
				$('.lastRow').removeClass('lastRow');

				var nextIndex = eval(lastIndex + "+1");
				var newRowHtml =
						'<tr count="' + nextIndex + '" class="lastRow lineItemRow">' +
						'<td>' + Math.round(nextIndex + 1) + '.</td>' +
						'<td></td>' +
						'  <td>' +
						'    <input type="hidden" name="grnLineItems[' + nextIndex + '].id" />' +
						'    <input type="text" class="variant" name="grnLineItems[' + nextIndex + '].productVariant"/>' +
						'  </td>' +
						'<td></td>' +
						'  <td class="pvDetails"></td>' +
						'<td class="taxCategory"></td>' +
						'<td></td>' +
						'  <td>' +
						'    <input type="text" name="grnLineItems[' + nextIndex + '].qty" class="receivedQuantity valueChange" />' +
						'  </td>' +
						'  <td>' +
						'    <input class="costPrice valueChange" type="text" name="grnLineItems[' + nextIndex + '].costPrice" />' +
						'  </td>' +
						'  <td>' +
						'    <input class="mrp" type="text" name="grnLineItems[' + nextIndex + '].mrp" />' +
						' </td>'+
						'  <td ><label class="taxableAmount"></label></td>' +
						'  </tr>';

				$('#poTable').append(newRowHtml);
				return false;
			});

			$('.variant').live("change", function() {
				var variantRow = $(this).parents('.lineItemRow');
				var productVariantId = variantRow.find('.variant').val();
				var productVariantDetails = variantRow.find('.pvDetails');
				$.getJSON(
						$('#pvInfoLink').attr('href'), {productVariantId: productVariantId, warehouse: ${pa.grn.warehouse.id}},
						function(res) {
							if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
								variantRow.find('.mrp').val(res.data.variant.markedPrice);
								variantRow.find('.costPrice').val(res.data.variant.costPrice);
								productVariantDetails.html(
										res.data.product + '<br/>' +
										res.data.options
										);
							} else {
								$('.variantDetails').html('<h2>'+res.message+'</h2>');
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
				if(qty=="" || costPrice=="" ){
						alert("All fields are compulsory.");
						return false;
					}
					if(isNaN(qty) || isNaN(costPrice) || qty<0 || costPrice<0){
						alert("Enter values in correct format.");
						return false;
					}
				var taxCategory;
				if (taxIdentifier == 'finance') {
					var taxCat = valueChangeRow.find('.taxCategory');
					var selectedTax = $(taxCat).find('option:selected');
					taxCategory = selectedTax.text();
				} else {
					taxCategory = parseFloat(valueChangeRow.find('.taxCategory').html().trim());
				}
				var totalTaxable = parseFloat($('.totalTaxable').html().replace(/,/g,''));
				var totalTax = parseFloat($('.totalTax').html().replace(/,/g,''));
				var totalSurcharge = parseFloat($('.totalSurcharge').html().replace(/,/g,''));
				var totalPayable = parseFloat($('.totalPayable').html().replace(/,/g,''));
				var initTaxable=parseFloat(valueChangeRow.find('.taxableAmount').html().replace(/,/g,''));
				var initTax=parseFloat(valueChangeRow.find('.taxAmount').html().replace(/,/g,''));
				var initSurcharge=parseFloat(valueChangeRow.find('.surchargeAmount').html().replace(/,/g,''));
				var initPayable=parseFloat(valueChangeRow.find('.payableAmount').html().replace(/,/g,''));
				totalTaxable-=initTaxable;
				totalTax-=initTax;
				totalSurcharge-=initSurcharge;
				totalPayable-=initPayable;
				if (isNaN(qty)) {
					alert("Enter Valid Quantity.");
					return;
				}
				var surchargeCategory = 0.0;
				var stateIdentifier = $('.state').html();
				if (stateIdentifier == 'CST') {
					surchargeCategory = 0.0;
					taxCategory = 0.02;
				} else {
					surchargeCategory = 0.05;
				}
				var taxable = costPrice * qty;
				var tax = taxable * taxCategory;
				var surcharge = tax * surchargeCategory;
				var payable = surcharge + taxable + tax;
				totalTaxable+=taxable;
				totalTax+=tax;
				totalSurcharge+=surcharge;
				totalPayable+=payable;
				valueChangeRow.find('.taxableAmount').html(taxable.toFixed(2));
				valueChangeRow.find('.taxAmount').html(tax.toFixed(2));
				valueChangeRow.find('.surchargeAmount').html(surcharge.toFixed(2));
				valueChangeRow.find('.payableAmount').html(payable.toFixed(2));
				 updateTotal('.receivedQuantity','.totalQuantity');
				$('.totalTaxable').html(totalTaxable.toFixed(2));
				$('.totalTax').html(totalTax.toFixed(2));
				$('.totalSurcharge').html(totalSurcharge.toFixed(2));
				$('.totalPayable').html(totalPayable.toFixed(2));
			});

			$('.requiredFieldValidator').click(function() {
					var qty = $('.receivedQuantity').val();
					var costPrice = $('.costPrice').val();
					if(qty=="" || costPrice=="" || qty<0 || costPrice<0){
						alert("Enter values in correct format.");
						return false;
					}
          $(this).css("display", "none");
				} );

			updateTotal('.receivedQuantity','.totalQuantity');
		});
	</script>
</s:layout-component>
<s:layout-component name="heading">
	Edit GRN of PO # ${pa.grn.purchaseOrder.id}
</s:layout-component>
<s:layout-component name="content">
	<div style="display: none;">
		<s:link beanclass="com.hk.web.action.admin.inventory.EditPurchaseOrderAction" id="pvInfoLink" event="getPVDetails"></s:link>
	</div>

	<s:form beanclass="com.hk.web.action.admin.inventory.GRNAction">
		<s:hidden name="grn" value="${pa.grn.id}"/>
		<table>
			<tr>
				<td>Supplier Name</td>
				<td>${pa.grn.purchaseOrder.supplier.name}</td>

				<td>Supplier State</td>
				<td>${pa.grn.purchaseOrder.supplier.state}</td>

				<td>Tax</td>
				<td>
					<c:choose>
						<c:when test="${pa.grn.purchaseOrder.supplier.state == pa.grn.warehouse.state}">
							<label class="state">NON-CST</label>
						</c:when>
						<c:otherwise>
							<label class="state">CST</label>
						</c:otherwise>
					</c:choose>
				</td>
			</tr>

			<tr>
				<td>GRN Date</td>
				<td>
					<s:text class="date_input" formatPattern="yyyy-MM-dd" name="grn.grnDate"/></td>
				<td>Received By</td>
				<td>
						${pa.grn.receivedBy.name}</td>
				<td></td>
				<td></td>
			</tr>
			<tr>
				<td>Invoice Date</td>
				<td><s:text class="date_input" formatPattern="yyyy-MM-dd" name="grn.invoiceDate"/></td>
				<td>Invoice Number</td>
				<td><s:text name="grn.invoiceNumber"/></td>
				<td></td>
				<td></td>
			</tr>
			<tr>
				<td>Payable</td>
				<td class="payable">
					<fmt:formatNumber value="${actionBean.grnDto.totalPayable}" type="currency" currencySymbol=" "
					                  maxFractionDigits="0"/></td>
				<td>Payment Details<br/><span class="sml gry">(eg. Cheque no.)</span></td>
				<td><s:textarea name="grn.paymentDetails" style="height:50px;"/></td>
				<td></td>
				<td></td>
			</tr>
			<tr>
				<td>Reconciled</td>
				<td><s:checkbox name="grn.reconciled"/></td>
				<td>Status</td>
				<td><s:select name="grn.grnStatus" value="${pa.grn.grnStatus.id}">
					<hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="grnStatusList"
					                           value="id" label="name"/>
				</s:select></td>
				<td></td>
				<td></td>
			</tr>
      <tr>
      <td>For Warehouse</td>
      <td>
      <s:hidden name="grn.warehouse" value="${pa.grn.warehouse}"/>
        ${pa.grn.warehouse.city}
    </td></tr>
		</table>

		<table border="1">
			<thead>
			<tr>
				<th>S.No.</th>
				<th></th>
				<th>VariantID</th>
				<th>UPC</th>
				<th>Supplier Code</th>
				<th>Remarks</th>
				<th>Details</th>
				<th>Tax<br/>Category</th>
				<th>Asked Qty</th>
				<th>Received Qty<br/>(Adjust -)</th>
				<th>Cost Price<br/>(Without TAX)</th>
				<th>MRP</th>
				<th>Taxable</th>
				<th>Tax</th>
				<th>Surcharge</th>
				<th>Payable</th>
				<th>Weight</th>
				<th>Length</th>
				<th>Breadth</th>
				<th>Height</th>

			</tr>
			</thead>
			<tbody id="poTable">
			<c:forEach var="grnLineItemDto" items="${pa.grnDto.grnLineItemDtoList}" varStatus="ctr">
        <c:set value="${grnLineItemDto.grnLineItem.sku}" var="sku"/>
        <c:set value="${sku.productVariant}" var="productVariant"/>
        <c:set value="${productVariant.product}" var="product"/>

				<s:hidden name="grnLineItems[${ctr.index}]" value="${grnLineItemDto.grnLineItem.id}"/>
				<tr count="${ctr.index}" class="${ctr.last ? 'lastRow lineItemRow':'lineItemRow'}">
					<td>${ctr.index+1}.</td>
					<td>
						<div class='img48' style="vertical-align:top;">
							<c:choose>
								<c:when test="${productVariant.product.mainImageId != null}">
									<hk:productImage imageId="${productVariant.product.mainImageId}"
									                 size="<%=EnumImageSize.TinySize%>"/>
								</c:when>
								<c:otherwise>
									<img class="prod48"
									     src="${pageContext.request.contextPath}/images/ProductImages/ProductImagesThumb/${productVariant.product.id}.jpg"
									     alt="${productLineItem.productVariant.product.name}"/>
								</c:otherwise>
							</c:choose>
						</div>
					</td>
					<td>
							${productVariant.id}
						<s:hidden class="variant" name="grnLineItems[${ctr.index}].productVariant"
						          value="${grnLineItemDto.grnLineItem.productVariant.id}"/>
            <%--<s:hidden class="sku" name="grnLineItems[${ctr.index}].sku"
                      value="${sku}"></s:hidden>--%>
					</td>
					<td><s:text name="grnLineItems[${ctr.index}].sku.productVariant.upc" value="${productVariant.upc}"/></td>
					<td>${productVariant.supplierCode}</td>
					<td>${productVariant.otherRemark}</td>
					<td>${product.name}<br/>${productVariant.optionsCommaSeparated}
					</td>
					<td class="taxCategory">
						<%--<shiro:hasPermission name="<%=PermissionConstants.UPDATE_RECONCILIATION_REPORTS%>">
							<input type="hidden" value="finance"
							       class="taxIdentifier"/> 
							<s:select name="grnLineItems[${ctr.index}].sku.tax"
							          value="${grnLineItemDto.grnLineItem.sku.tax.id}" class="valueChange">
								<hk:master-data-collection service="<%=TaxDao.class%>" serviceProperty="taxList" value="id"
								                           label="name"/>
							</s:select>
						</shiro:hasPermission>
						<shiro:lacksPermission name="<%=PermissionConstants.UPDATE_RECONCILIATION_REPORTS%>">
							${grnLineItemDto.grnLineItem.sku.tax.value}
						</shiro:lacksPermission>--%>
                    ${grnLineItemDto.grnLineItem.sku.tax.value}
					</td>
					<td>${hk:askedPOQty(pa.grn.purchaseOrder, productVariant)}</td>
					<td>
						<s:text name="grnLineItems[${ctr.index}].qty" value="${grnLineItemDto.grnLineItem.qty}"
						        class="receivedQuantity valueChange"/>
					</td>
					<td>
						<s:text name="grnLineItems[${ctr.index}].costPrice" value="${grnLineItemDto.grnLineItem.costPrice}"
						        class="costPrice valueChange"/>
					</td>
					<td>
						<s:text class="mrp" name="grnLineItems[${ctr.index}].mrp" value="${grnLineItemDto.grnLineItem.mrp}"/>
					</td>

					<td class="taxableAmount">
						<fmt:formatNumber value="${grnLineItemDto.taxable}" maxFractionDigits="2"/>
					</td>
					<td class="taxAmount">
						<fmt:formatNumber value="${grnLineItemDto.tax}" maxFractionDigits="2"/>
					</td>
					<td class="surchargeAmount">
						<fmt:formatNumber value="${grnLineItemDto.surcharge}" maxFractionDigits="2"/>
					</td>
					<td class="payableAmount">
						<fmt:formatNumber value="${grnLineItemDto.payable}" maxFractionDigits="2"/>
					</td>
          <td><s:text name="grnLineItems[${ctr.index}].sku.productVariant.weight" class="weight" value="${productVariant.weight}"/></td>
          <td><s:text name="grnLineItems[${ctr.index}].sku.productVariant.length" value="${productVariant.length}"/></td>
          <td><s:text name="grnLineItems[${ctr.index}].sku.productVariant.breadth" value="${productVariant.breadth}"/></td>
          <td><s:text name="grnLineItems[${ctr.index}].sku.productVariant.height" value="${productVariant.height}"/></td>
				</tr>
			</c:forEach>
			</tbody>
			<tfoot>
			<tr>
				<td colspan="7">Total</td>
				<td colspan="3" class="totalQuantity"></td>
				<td class="totalTaxable"><fmt:formatNumber value="${pa.grnDto.totalTaxable}" maxFractionDigits="2"/></td>
				<td class="totalTax"><fmt:formatNumber value="${pa.grnDto.totalTax}" maxFractionDigits="2"/></td>
				<td class="totalSurcharge"><fmt:formatNumber value="${pa.grnDto.totalSurcharge}" maxFractionDigits="2"/></td>
				<td class="totalPayable"><fmt:formatNumber value="${pa.grnDto.totalPayable}" maxFractionDigits="2"/></td>
			</tr>
			</tfoot>
		</table>
		<div class="variantDetails info"></div>
		<br/>
		<a href="grn.jsp#" class="addRowButton" style="font-size:1.2em">Add new row</a>

		<s:submit name="save" value="Save" class="requiredFieldValidator"/>
	</s:form>
  
</s:layout-component>

</s:layout-render>
