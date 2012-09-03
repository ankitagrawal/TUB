<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page import="com.hk.constants.inventory.EnumPurchaseOrderStatus" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.pact.dao.warehouse.WarehouseDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.inventory.EditPurchaseOrderAction" var="pa"/>
<s:layout-render name="/layouts/defaultAdmin.jsp">
<%
	WarehouseDao warehouseDao = ServiceLocatorFactory.getService(WarehouseDao.class);
	pageContext.setAttribute("whList", warehouseDao.getAllWarehouses());
%>
<c:set var="poApproved" value="<%=EnumPurchaseOrderStatus.Approved.getId()%>"/>
<s:layout-component name="htmlHead">
	<link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
	<jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
	<script type="text/javascript">
		$(document).ready(function () {
			$('.addRowButton').click(function () {

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
								'    <input type="hidden" name="poLineItems[' + nextIndex + '].id" />' +
								'    <input type="text" class="variant" name="poLineItems[' + nextIndex + '].productVariant"/>' +
								'  </td>' +
								'<td></td>' +
								'<td class="supplierCode"></td>' +
								'<td class="otherRemark"></td>' +
								' <td class="pvDetails"></td>' +
								'<td><input type="text" class="taxCategory" readonly="readonly" name="poLineItems[' + nextIndex + '].taxCategory"/></td>' +
								'<td></td>' +
								'<td></td>' +
								'<td></td>' +
								'  <td>' +
								'    <input type="text" name="poLineItems[' + nextIndex + '].qty" class="quantity valueChange" />' +
								'  </td>' +
								'  <td>' +
								'    <input class="costPrice valueChange" type="text" name="poLineItems[' + nextIndex + '].costPrice" />' +
								'  </td>' +
								'  <td>' +
								'    <input class="mrp" type="text" name="poLineItems[' + nextIndex + '].mrp" />' +
								'  </td>' +
								'  <td>' +
								'    <input class="discountPercentage valueChange" type="text" name="poLineItems[' + nextIndex + '].discountPercent" />' +
								'  </td>'+
								'  <td></td>'+
								'  <td ><input class="taxableAmount" type="text" readonly="readonly" name="poLineItems['+nextIndex+'].taxableAmount"/></td>' +
								'  <td ><input class="taxAmount" type="text" readonly="readonly" name="poLineItems['+nextIndex+'].taxAmount"></td>' +
								'  <td ><input class="surchargeAmount" type="text" readonly="readonly" name="poLineItems['+nextIndex+'].surchargeAmount"/></td>' +
								'  <td ><input class="payableAmount" type="text" readonly="readonly" name="poLineItems['+nextIndex+'].payableAmount"/></td>' +
								'</tr>';

				$('#poTable').append(newRowHtml);

				return false;
			});

			$('.valueChange').live("change", function () {
				var valueChangeRow = $(this).parents('.lineItemRow');
				var costPrice = valueChangeRow.find('.costPrice').val();
				var mrp = valueChangeRow.find('.mrp').val();
				var qty = valueChangeRow.find('.quantity').val();
				var discountPercentage = valueChangeRow.find('.discountPercentage').val();
				if (qty == "" || costPrice == "") {
					alert("All fields are compulsory.");
					return false;
				}
				if (isNaN(qty) || isNaN(costPrice) || qty < 0 || costPrice < 0 || discountPercentage < 0) {
					alert("Enter values in correct format.");
					return false;
				}
				var taxCategory = valueChangeRow.find('.taxCategory').val();
				if(taxCategory == null) {
					taxCategory = 0;
				}
				var surchargeCategory = 0.0;
				var stateIdentifier = $('.state').html();
				if (stateIdentifier == 'CST') {
					surchargeCategory = 0.0;
					taxCategory = 0.02;
				} else {
					surchargeCategory = 0.05;
				}
				var tax = valueChangeRow.find('.taxAmount').val();
				var taxable = costPrice * qty;

				var discountedAmount = 0.0;
				if (discountPercentage!=null && (isNaN(discountPercentage) || discountPercentage < 0)) {
					alert("Enter valid discount");
					return;
				}
				if(discountPercentage > 0) {
					discountedAmount = (discountPercentage / 100) * taxable;
				}

				taxable -= discountedAmount;
				tax = taxable*taxCategory;
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

				var finalPayable = parseFloat($('.finalPayable').val().replace(/,/g, ''));
				var overallDiscount = parseFloat($('.overallDiscount').val().replace(/,/g, ''));
				if (isNaN(overallDiscount)) {
					overallDiscount = 0;
				}

				finalPayable -= overallDiscount;
				$('.finalPayable').val(finalPayable.toFixed(2));
			});

			function updateTotal(fromTotalClass,toTotalClass,toHtml){
				var total=0;
				$.each($(fromTotalClass),function(index,value){
					var eachRow=$(value);
					var eachRowValue=eachRow.val().trim();
					total+=parseFloat(eachRowValue);
				});
				if(toHtml == 1){
					$(toTotalClass).html(total);
				} else {
					$(toTotalClass).val(total.toFixed(2));
				}
			};

			$('.variant').live("change", function () {
				var variantRow = $(this).parents('.lineItemRow');
				var productVariantId = variantRow.find('.variant').val();
				var productVariantDetails = variantRow.find('.pvDetails');
				$.getJSON(
						$('#pvInfoLink').attr('href'), {productVariantId:productVariantId, warehouse : ${pa.purchaseOrder.warehouse}},
						function (res) {
							if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
								variantRow.find('.supplierCode').html(res.data.variant.supplierCode);
								variantRow.find('.otherRemark').html(res.data.variant.otherRemark);
								variantRow.find('.mrp').val(res.data.variant.markedPrice);
								variantRow.find('.costPrice').val(res.data.variant.costPrice);
								variantRow.find('.taxCategory').val(res.data.tax);
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

			$('.footerChanges').live("change", function overallDiscount() {
				var overallDiscount=parseFloat($('.overallDiscount').val());
				if(isNaN(overallDiscount)){
					overallDiscount=0;
				}
				updateTotal('.payableAmount','.finalPayable',0);
				var finalPayable=parseFloat($('.finalPayable').val().replace(/,/g,''));
				finalPayable-=overallDiscount;
				$('.finalPayable').val(finalPayable.toFixed(2));
			} );

			$('.requiredFieldValidator').click(function () {
				var quantity = $('.quantity').val();
				if (quantity == "" || isNaN(quantity)) {
					alert("Please enter a valid quantity.");
					return false;
				}
				var returnFalse = (1==2);
				$.each($('.discountPercentage'), function(index, value){
					var eachRow=$(value);
					var discountPercentage=eachRow.val().trim();
					if(discountPercentage < 0){
						alert("Discount percentage should be greater than zero");
						returnFalse = (1==1);
						return false;
					}
				});
				if(returnFalse) {
					return false;
				}
				var statusSelected = $('.status').find('option:selected');
				var approver = $('.approver').find('option:selected');
				var test = approver.text();
				if (statusSelected.text() == "Sent For Approval" && approver.text() == "-Select Approver-") {
					alert("Approver Not Selected.");
					return false;
				}
			});


		});
	</script>
	<%--<style type="text/css">
			input {
			  text-transform: uppercase;
			}
		  </style>--%>
</s:layout-component>

<s:layout-component name="content">
<div style="display: none;">
	<s:link beanclass="com.hk.web.action.admin.inventory.EditPurchaseOrderAction" id="pvInfoLink"
	        event="getPVDetails"></s:link>
</div>
<h2>Edit PO# ${pa.purchaseOrder.id}</h2>
<s:form beanclass="com.hk.web.action.admin.inventory.EditPurchaseOrderAction">
<s:hidden name="purchaseOrder" value="${pa.purchaseOrder}"/>
<table>
	<tr>
		<td>Supplier Name</td>
		<td>${pa.purchaseOrder.supplier.name}</td>

		<td>Supplier State</td>
		<td>${pa.purchaseOrder.supplier.state}</td>
		<td>Tax</td>
		<td>
			<c:choose>
				<c:when test="${pa.purchaseOrder.supplier.state == pa.purchaseOrder.warehouse.state}">
					<label class="state">Non - CST</label>
				</c:when>
				<c:otherwise>
					<label class="state">CST</label>
				</c:otherwise>
			</c:choose>
		</td>
	</tr>
	<tr>
		<td>PO Date</td>
		<td>
			<s:text class="date_input" formatPattern="yyyy-MM-dd" name="purchaseOrder.poDate"/></td>

		<td>PO Number</td>
		<td><s:text name="purchaseOrder.poNumber"/></td>
		<td></td>
		<td></td>
	</tr>
	<tr>
		<td>Payable<br/>Adv. Payment</td>
		<td class="payable">
			<fmt:formatNumber value="${actionBean.purchaseOrderDto.totalPayable}" type="currency" currencySymbol=" "
			                  maxFractionDigits="0"/>
			<br/> <s:text name="purchaseOrder.advPayment"/>
		</td>

		<td>Payment Details<br/><span class="sml gry">(eg. Adv. Amount)</span></td>
		<td><s:textarea name="purchaseOrder.paymentDetails" style="height:60px;"/></td>
		<td></td>
		<td></td>
	</tr>

	<tr>
		<td>Status</td>
		<td class="status">
			<c:if test="${pa.purchaseOrder.purchaseOrderStatus.id >= poApproved}">
				<shiro:hasRole name="<%=RoleConstants.PO_APPROVER%>">
					<s:select name="purchaseOrder.purchaseOrderStatus"
					          value="${pa.purchaseOrder.purchaseOrderStatus.id}">
						<hk:master-data-collection service="<%=MasterDataDao.class%>"
						                           serviceProperty="purchaseOrderStatusList"
						                           value="id" label="name"/>
					</s:select>
				</shiro:hasRole>
				<shiro:lacksRole name="<%=RoleConstants.PO_APPROVER%>">
					${pa.purchaseOrder.purchaseOrderStatus.name}
				</shiro:lacksRole>
			</c:if>
			<c:if test="${pa.purchaseOrder.purchaseOrderStatus.id < poApproved}">
				<s:select name="purchaseOrder.purchaseOrderStatus" value="${pa.purchaseOrder.purchaseOrderStatus.id}">
					<hk:master-data-collection service="<%=MasterDataDao.class%>"
					                           serviceProperty="purchaseOrderStatusList"
					                           value="id" label="name"/>
				</s:select>
			</c:if>
		</td>

		<td>Approver</td>
		<td class="approver"><s:select name="purchaseOrder.approvedBy" value="${pa.purchaseOrder.approvedBy}">
			<s:option value="">-Select Approver-</s:option>
			<hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="approverList" value="id"
			                           label="name"/>
		</s:select></td>
		<td></td>
		<td></td>
	</tr>
	<tr>
		<td>Est. Delivery Date</td>
		<td>
			<s:text class="date_input" formatPattern="yyyy-MM-dd" name="purchaseOrder.estDelDate"/></td>

		<td>Est Payment Date</td>
		<td>
			<s:text class="date_input" formatPattern="yyyy-MM-dd" name="purchaseOrder.estPaymentDate"/></td>
		<td></td>
		<td></td>
	</tr>
	<tr>
		<td>For Warehouse</td>
		<td>
			<s:hidden name="purchaseOrder.warehouse" value="${pa.purchaseOrder.warehouse}" class="warehouse"/>
				${pa.purchaseOrder.warehouse}
		</td>
	</tr>
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
		<th>Current Inventory <br>Selected Warehouse</th>
		<th>Current Inventory <br>All Warehouses</th>
		<th>Last 30 days Sale</th>
		<th>Qty</th>
		<th>Cost Price<br/>(Without TAX)</th>
		<th>MRP</th>
		<th>Discount<br/>(%)</th>
		<th>Margin(MRP vs CP)</th>
		<th>Taxable</th>
		<th>Tax</th>
		<th>Surcharge</th>
		<th>Payable</th>

	</tr>
	</thead>
	<tbody id="poTable">
	<c:forEach var="poLineItemDto" items="${pa.purchaseOrderDto.poLineItemDtoList}" varStatus="ctr">
		<c:set value="${poLineItemDto.poLineItem.sku.productVariant}" var="productVariant"/>
		<c:set value="${poLineItemDto.poLineItem.sku}" var="sku"/>

		<s:hidden name="poLineItems[${ctr.index}]" value="${poLineItemDto.poLineItem.id}"/>
		<s:hidden name="poLineItems[${ctr.index}].productVariant" value="${productVariant.id}"/>
		<%--<s:hidden name="poLineItems[${ctr.index}].sku" value="${sku.id}"/>--%>

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
							     alt="${productVariant.product.name}"/>
						</c:otherwise>
					</c:choose>
				</div>
			</td>
			<td>
					${productVariant.id}
				<s:hidden class="variant" name="poLineItems[${ctr.index}].productVariant"
				          value="${poLineItemDto.poLineItem.productVariant.id}"/>
			</td>
			<td>${productVariant.upc}</td>
			<td class="supplierCode">${productVariant.supplierCode}</td>
			<td class="otherRemark">${productVariant.otherRemark} </label></td>
			<td>${productVariant.product.name}<br/>${productVariant.optionsCommaSeparated}
			</td>
			<td class="taxCategory"> ${sku.tax.value}
					<%--<input type="text" name="taxCategory" class="taxCategory"  value="${sku.tax.value}" disabled="disabled" />--%>

			</td>
			<td>
					${hk:netInventory(sku)}
			</td>
			<td>
					${hk:netInventory(productVariant)}
			</td>
			<td>
					${hk:findInventorySoldInGivenNoOfDays(sku, 30)}
			</td>
			<td>
				<s:text name="poLineItems[${ctr.index}].qty" value="${poLineItemDto.poLineItem.qty}" class="quantity valueChange"/>
			</td>
			<td>
				<s:text class="costPrice valueChange" name="poLineItems[${ctr.index}].costPrice"
				        value="${poLineItemDto.poLineItem.costPrice}"/>
			</td>
			<td>
				<s:text class="mrp" name="poLineItems[${ctr.index}].mrp" value="${poLineItemDto.poLineItem.mrp}"/>
			</td>
			<td>
				<s:text class="discountPercentage valueChange" name="poLineItems[${ctr.index}].discountPercent" value="${poLineItemDto.poLineItem.discountPercent}"/>
			</td>
			<td>
				<fmt:formatNumber value="${poLineItemDto.marginMrpVsCP}" maxFractionDigits="2"/>
			</td>
			<td>
				<s:text readonly="readonly" class="taxableAmount" name="poLineItems[${ctr.index}].taxableAmount" value="${poLineItemDto.poLineItem.taxableAmount}" />
			</td>
			<td>
				<s:text readonly="readonly" class="taxAmount" name="poLineItems[${ctr.index}].taxAmount" value="${poLineItemDto.poLineItem.taxAmount}" />
			</td>
			<td>
				<s:text readonly="readonly" class="surchargeAmount" name="poLineItems[${ctr.index}].surchargeAmount" value="${poLineItemDto.poLineItem.surchargeAmount}" />
			</td>
			<td>
				<s:text readonly="readonly" class="payableAmount" name="poLineItems[${ctr.index}].payableAmount" value="${poLineItemDto.poLineItem.payableAmount}" />
			</td>
		</tr>
	</c:forEach>
	</tbody>
	<tfoot>
	<tr>
		&nbsp; &nbsp;
		<td colspan="16">Total</td>
		<td><s:text readonly="readonly" class="totalTaxable" name="purchaseOrderDto.totalTaxable" value="${pa.purchaseOrderDto.totalTaxable}" /></td>
		<td><s:text readonly="readonly" class="totalTax" name="purchaseOrderDto.totalTax" value="${pa.purchaseOrderDto.totalTax}" /></td>
		<td><s:text readonly="readonly" class="totalSurcharge" name="purchaseOrderDto.totalSurcharge" value="${pa.purchaseOrderDto.totalSurcharge}"/></td>
		<td><s:text readonly="readonly" class="totalPayable" name="purchaseOrderDto.totalPayable" value="${pa.purchaseOrderDto.totalPayable}"/></td>
	</tr>
	<tr>
		<td colspan="18"></td><td>Overall Discount<br/>(In Rupees)</td>
		<td><s:text class="overallDiscount footerChanges" name="purchaseOrder.discount" value="${pa.purchaseOrder.discount}"/></td>
	</tr>
	<tr>
	<tr>
		<td colspan="18"></td><td>Final Payable</td>
		<td><s:text readonly="readonly" class="finalPayable" name="purchaseOrder.finalPayableAmount" value="${pa.purchaseOrder.finalPayableAmount}"/></td>
	</tr>
	</tfoot>
</table>
<div class="variantDetails info"></div>
<shiro:hasAnyRoles name="<%=RoleConstants.ROLE_GROUP_CATMAN_ADMIN%>">

	<c:if test="${pa.purchaseOrder.purchaseOrderStatus.id < poApproved}">
		<a href="editPurchaseOrder.jsp#" class="addRowButton" style="font-size:1.2em">Add new row</a>
		<s:submit name="save" value="Save" class="requiredFieldValidator"/>
	</c:if>

	<c:if test="${pa.purchaseOrder.purchaseOrderStatus.id >= poApproved}">
		<shiro:hasRole name="<%=RoleConstants.PO_APPROVER%>">
			<a href="editPurchaseOrder.jsp#" class="addRowButton" style="font-size:1.2em">Add new row</a>
			<s:submit name="save" value="Save" class="requiredFieldValidator"/>
		</shiro:hasRole>
	</c:if>
</shiro:hasAnyRoles>


</s:form>
<shiro:hasAnyRoles name="<%=RoleConstants.ROLE_GROUP_CATMAN_ADMIN%>">
	<c:if test="${pa.purchaseOrder.purchaseOrderStatus.id < poApproved}">
		<hr/>

		<fieldset>
			<legend>Upload Excel to Create PO LineItems</legend>
			<br/>
			<span class="large gry">(VARIANT_ID, QTY, COST, MRP) as excel headers</span>
			<br/><br/>
			<s:form beanclass="com.hk.web.action.admin.inventory.EditPurchaseOrderAction">
				<h2>File to Upload: <s:file name="fileBean" size="30"/></h2>
				<s:hidden name="purchaseOrder" value="${pa.purchaseOrder.id}"/>

				<div class="buttons">
					<s:submit name="parse" value="Create PO LineItems"/>
				</div>
			</s:form>
		</fieldset>
	</c:if>
</shiro:hasAnyRoles>
</s:layout-component>

</s:layout-render>
