<%@ page import="com.hk.domain.inventory.rv.ReconciliationType" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="java.util.List" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.inventory.ReconciliationVoucherAction" var="pa"/>
<s:useActionBean beanclass="com.hk.web.action.admin.warehouse.SelectWHAction" var="whAction" event="getUserWarehouse"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Edit Reconciliation Voucher">
<jsp:useBean id="now" class="java.util.Date" scope="request"/>
<s:layout-component name="htmlHead">

<%
	MasterDataDao masterDataDao = ServiceLocatorFactory.getService(MasterDataDao.class);
	List<ReconciliationType> reconciliationTypeList = masterDataDao.getReconciliationTypeList();
	pageContext.setAttribute("reconciliationTypeList", reconciliationTypeList);
%>


<link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
<jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
<script type="text/javascript">
	$(document).ready(function() {
		$('.addRowButton').click(function() {
			var lastIndex = $('.lastRow').attr('count');
			if (!lastIndex) {
				lastIndex = -1;
			}
			$('.lastRow').removeClass('lastRow');

			var nextIndex = eval(lastIndex + "+1");
			var reconcilitionvalue = $('.reconciliationId').val();
			var rvlineItem = $('.rvitem').val();


			var reconciliationTypeOptions = '<select class="reconciliationType valueChange" name="rvLineItems[' + nextIndex + '].reconciliationType">';
		<c:forEach items="${reconciliationTypeList}" var="reconciliationTypeVar">
			reconciliationTypeOptions += '<option value="'+${reconciliationTypeVar.id}+
			'">' + "${reconciliationTypeVar.name}" + '</option>';
		</c:forEach>		

			var link = '<s:link  class ="singlesave" beanclass="com.hk.web.action.admin.inventory.ReconciliationVoucherAction" event="saveAndReconcileRv">Reconcile</s:link>';

			var newRowHtml =
					'<tr count="' + nextIndex + '" class="lastRow lineItemRow">' +
					'  <td>' +
					'    <input type="hidden" name="rvLineItems[' + nextIndex + '].id" />' +
					'    <input type="text" class="variant" name="rvLineItems[' + nextIndex + '].productVariant"/>' +
					'<input type="hidden" name="rvLineItems[' + nextIndex + '] value="' + rvlineItem + '"/>' +
					'<input type="hidden" name="reconciliationVoucher" value="' + reconcilitionvalue + '"/>' +
					'  </td>' +
					'  <td class="pvDetails"></td>' +
					'  <td>' +
					'    <input type="text" id="quantity" name="rvLineItems[' + nextIndex + '].qty" />' +
					'  </td>' +

					'<td>' +
					reconciliationTypeOptions +
					'</select>' +
					'<input type="hidden" value="finance" class="reconciliationTypeIdentifier"/>' +

					'</td>' +
					'  <td>' +
					'    <input class="costPrice" type="text" name="rvLineItems[' + nextIndex + '].costPrice" />' +
					'  </td>' +
					'  <td>' +
					'    <input class="mrp" type="text" name="rvLineItems[' + nextIndex + '].mrp" />' +
					'  </td>' +
					'  <td>' +
					'    <input type="text" class="batch" name="rvLineItems[' + nextIndex + '].batchNumber" />' +
					'  </td>' +
					'  <td>' +
					'    <input class="date_input" formatPattern="yyyy-MM-dd" type="text" name="rvLineItems[' + nextIndex + '].mfgDate" />' +
					'  </td>' +
					'  <td>' +
					'    <input class="date_input" formatPattern="yyyy-MM-dd" type="text" name="rvLineItems[' + nextIndex + '].expiryDate" />' +
					'  </td>' +
					'  <td>' +
					'    <textarea rows="4"  class ="textare" columns="10" name="rvLineItems[' + nextIndex + '].remarks" style="height:50px;"/>' +
					'  </td>' +
					'<td>' +
					'<input type="text" id="reconciliedqty" value="0" name="rvLineItems[' + nextIndex + '].reconciledQty" readonly="readonly" />' +
					'</td>' +					
					'<td> ' +
					link +
					'</td>' +
					'</tr>';

			$('#poTable').append(newRowHtml);

			return false;
		});

		$('.variant').live("change", function() {
			$('error').hide();
			var variantRow = $(this).parents('.lineItemRow');
			var productVariantId = variantRow.find('.variant').val();
			var productVariantDetails = variantRow.find('.pvDetails');
			$.getJSON(
					$('#pvInfoLink').attr('href'), {productVariantId: productVariantId, warehouse: ${whAction.setWarehouse.id}},
					function(res) {
						if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
							variantRow.find('.mrp').val(res.data.variant.markedPrice);
							variantRow.find('.costPrice').val(res.data.variant.costPrice);
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
		$('.batch').live("change", function() {

			var variantRow = $(this).parents('.lineItemRow');
			var batchNo = $(this).val();
			var qty = variantRow.find('#quantity').val();
			var variant = variantRow.find('.variant').val();
			if (batchNo == null || batchNo.trim() == '') {
				return false;
			}
			$.getJSON(
					$('#batchInfoLink').attr('href'), {batchNumber :batchNo , askedQty:qty, warehouse : ${whAction.setWarehouse.id},productVariantId:variant},
					function(res) {
						if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
							$('.error').empty();
							$('.error').hide();
						} else {
							$('.error').empty();
							$('.error').html(res.message);
							$('.error').show();
						}
					}
					);


		});

		$('.singlesave').live('click', function() {
			$('.error').hide();
			var curEle = $(this);
			var queryString = '';
			var sep = '';
			var qty = $('#quantity').val() ;
			var variant = $('.variant').val();
			var batch = $('.batch').val();
			if (qty == null || qty.trim() == '' || variant == null || variant.trim() == '' || batch == null || batch.trim() == '') {
				alert("ProductVariant/Qty/Batch are manadatory");
				return false;
			}
			if (qty == 0) {
				alert('Qty Can Not Be Zero');
				return false;
			}
			$(this).parents('tr').find('input,select,textarea').each(function() {
				if ($(this).attr('class') == 'reconciliationTypeIdentifier') {
					return;
				}
				queryString = queryString + sep + $(this).attr('name') + '=' + escape($(this).attr('value'));
				sep = '&';
			});
			var href = $('#reconForm').attr('action');
			curEle.css("display", "none");
			$.ajax({
				type:"POST",
				url : href + '?saveAndReconcileRv=',
				data:queryString,
				dataType:'json',
				success: function(data) {
					if (data.code == '<%=HealthkartResponse.STATUS_OK%>') {
						var reconQty = ''+data.data.rvLineItem.reconciledQty;
						var qty =   ''+data.data.rvLineItem.qty;
						if( reconQty == qty) {						
						curEle.parents('tr').css({"background-color":"#ccff99"}) ;
						curEle.parents('tr').find('input,select,textarea').each(function() {
							if ($(this).attr('type') == 'hidden')
							{
								$(this).replaceWith('');
							}
							else if ($(this).tagName == 'textarea') {

								$(this).replaceWith($(this).text());
							}
							else
							{
								if ($(this).attr('id') == 'reconciliedqty')
								{
									$(this).replaceWith('' + reconQty);
								}
								else {
									$(this).replaceWith($(this).val());
								}
							}
						});
						curEle.css("display", "none");

						}

						else{
						$('#reconciliedqty').val('' + reconQty);

						}
						$('.error').empty();
						$('.error').hide();
					}
					if (data.code == '<%=HealthkartResponse.STATUS_ERROR%>') {
						$('.singlesave').css("display","block");
						$('.error').empty();
						$('.error').html(data.message);
						$('.error').show();
					}
					console.log(data);
				},
				error:function onError() {
					$('.singlesave').css("display", "block");
					alert('Error in  Saving JSON');
				}


			});
			return false
		});

	});

</script>
</s:layout-component>

<s:layout-component name="content">
<div style="display: none;">
	<s:link beanclass="com.hk.web.action.admin.inventory.EditPurchaseOrderAction" id="pvInfoLink"
	        event="getPVDetails">

	</s:link>
	<s:link beanclass="com.hk.web.action.admin.inventory.ReconciliationVoucherAction" id="batchInfoLink"
	        event="getBatchDetails"></s:link>
	<s:link beanclass="com.hk.web.action.admin.inventory.ReconciliationVoucherAction" class="singleSaveLink"
	        event="saveAndReconcileRv"/>
</div>
<div>

</div>
<h2>Subtract/Edit Reconciliation Voucher</h2>

<h2>RV No # ${pa.reconciliationVoucher.id}</h2>
<s:form id="reconForm" beanclass="com.hk.web.action.admin.inventory.ReconciliationVoucherAction">
	<s:hidden class="reconciliationId" name="reconciliationVoucher" value="${pa.reconciliationVoucher.id}"/>
	<table>
		<tr>
			<td>Reconciliation Date</td>
			<td>
					${pa.reconciliationVoucher.reconciliationDate}
			</td>
		</tr>
		<tr>
			<td>Remarks<br/><span class="sml gry">(eg. XXX)</span></td>
			<td>${pa.reconciliationVoucher.remarks}</td>
		</tr>
		<tr>
			<td>For Warehouse</td>
			<td>
					${pa.reconciliationVoucher.warehouse.name}
			</td>
		</tr>
	</table>

	<div class="error"
	     style="display:none;background-color:salmon;font-size:12; margin-top: 20px; padding: 5px;width:550px;"></div>


	<table border="1">
		<thead>
		<tr>
			<th>VariantID</th>
			<th>Details</th>
			<th>Qty<br/>Only(+)</th>
			<th>Reconciliation Type<br/>(New)</th>
			<th>Cost Price<br/>(Without TAX)</th>
			<th>MRP</th>
			<th>Batch Number</th>
			<th>Mfg. Date<br/>(yyyy-MM-dd)</th>
			<th>Exp. Date<br/>(yyyy-MM-dd)</th>
			<th>Remarks</th>
			<th>Reconcilied Qty</th>

		</tr>
		</thead>
		<tbody id="poTable">
		<c:forEach var="rvLineItem" items="${pa.reconciliationVoucher.rvLineItems}" varStatus="ctr">
			<c:set var="productVariant" value="${rvLineItem.sku.productVariant}"/>
			<c:choose>
				<c:when test="${(rvLineItem.reconciledQty == 0) || (rvLineItem.reconciledQty < rvLineItem.qty)}">
					<tr count="${ctr.index}" id="rowno" class="${ctr.last ? 'lastRow lineItemRow':'lineItemRow'}">
						<s:hidden name="rvLineItems[${ctr.index}]" class="rvitem" value="${rvLineItem.id}"/>
						<s:hidden name="rvLineItems[${ctr.index}].reconciliationVoucher"
						          value="${rvLineItem.reconciliationVoucher.id}"/>
						<td>
							<s:text name="rvLineItems[${ctr.index}].productVariant" class="variant"
							        value="${productVariant.id}"/>
						</td>
						<td>${productVariant.product.name}<br/>${productVariant.productOptionsWithoutColor}
						</td>
						<td><s:text name="rvLineItems[${ctr.index}].qty" id="quantity" value="${rvLineItem.qty}"/>
						</td>

						<td class="reconciliationType">
							<input type="hidden" value="finance"
							       class="reconciliationTypeIdentifier"/>
							<s:select name="rvLineItems[${ctr.index}].reconciliationType"
							          value="${rvLineItem.reconciliationType.id}" class="valueChange">
								<hk:master-data-collection service="<%=MasterDataDao.class%>"
								                           serviceProperty="reconciliationTypeList" value="id"
								                           label="name"/>
							</s:select>
						</td>
						<td>
							<s:text name="rvLineItems[${ctr.index}].costPrice" value=" ${rvLineItem.costPrice}"/>
						</td>
						<td>
							<s:text name="rvLineItems[${ctr.index}].mrp" value="${rvLineItem.mrp}"/>
						</td>
						<td>
							<s:text name="rvLineItems[${ctr.index}].batchNumber" class="batch"
							        value="${rvLineItem.batchNumber}"/>
						</td>
						<td>
							<s:text name="rvLineItems[${ctr.index}].mfgDate" value="${rvLineItem.mfgDate}"
							        formatPattern="yyyy-MM-dd"/>
						</td>
						<td>
							<s:text name="rvLineItems[${ctr.index}].expiryDate" value="${rvLineItem.expiryDate}"
							        formatPattern="yyyy-MM-dd"/>
						</td>
						<td>
							<s:textarea style="height:60px;" name="rvLineItems[${ctr.index}].remarks" value="${rvLineItem.remarks}"/>

						</td>
						<td><s:text name="rvLineItems[${ctr.index}].reconciledQty" id="reconciliedqty"
						            value="${rvLineItem.reconciledQty}" readonly="readonly"/>
						</td>
						<td>
							<s:link class="singlesave"
							        beanclass="com.hk.web.action.admin.inventory.ReconciliationVoucherAction"
							        event="saveAndReconcileRv">Reconcile</s:link>
						</td>
					</tr>
				</c:when>

				<c:otherwise>
					<tr style="background-color:#ccff99;">
						<td>
							${productVariant.id}
						</td>
						<td>${productVariant.product.name}<br/>${productVariant.productOptionsWithoutColor}
						</td>
						<td>${rvLineItem.qty}
						</td>
						<td>${rvLineItem.reconciliationType.name}
						</td>
						<td>${rvLineItem.costPrice}
						</td>
						<td>${rvLineItem.mrp}
						</td>
						<td>${rvLineItem.batchNumber}</td>
						<td>
							<fmt:formatDate value="${rvLineItem.mfgDate}" type="both"/></td>
						<td>
							<fmt:formatDate value="${rvLineItem.expiryDate}" type="both"/></td>
						<td>${rvLineItem.remarks}</td>
					   <td>${rvLineItem.reconciledQty}</td>
					</tr>
				</c:otherwise>
			</c:choose>
		</c:forEach>
		</tbody>
	</table>
	<div class="variantDetails info"></div>
	<br/>
	<a href="editReconciliationVoucher.jsp#" class="addRowButton" style="font-size:1.2em">Add new row</a>

	<s:submit name="saveAll" value="Save" class="saveButton"/>

</s:form>
 <style type="text/css">
	 #reconciliedqty{
		 border:none;		
		 width:13px;
	 }


 </style>
<script type="text/javascript">
	$(document).ready(function() {


		$('.saveButton').click(function(e) {
			var result = true;
			$('#poTable tr').each(function() {
				var qty = $(this).find('#quantity').val() ;
				var variant = $(this).find('.variant').val();
				var batch = $(this).find('.batch').val();

				if ($(this).find("input").length) {
					if (variant == null || variant.trim() == '') {
						$(this).remove();
						return;
					}
				}
				if (variant != null && ( (batch == null || batch.trim() == '') || (qty == null || qty.trim() == ''))) {
					alert('Enter Batch Number && Qty for varinat :::::: ' + variant);
					result = false;
					return false;

				}

			});
			if (!result) {
				return false;
			}
			else {
				return $(this).submit();
			}

		});

	});
</script>
</s:layout-component>

</s:layout-render>
