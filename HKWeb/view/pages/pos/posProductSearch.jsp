<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Product Search">
	<s:useActionBean beanclass="com.hk.web.action.admin.pos.PosProductSearchAction" var="ps"/>
	<s:layout-component name="htmlHead">
		<script type="text/javascript">

			$(document).ready(function () {

				$('.searchSkuBatches').click(function () {
					$("#skuGroupTable > tbody").html("");
					var selectedSku = $(this).parent().siblings('.selectedSku').children('.selectedSkuHid').val();
					$.getJSON(
							$('#getDetailsLink').attr('href'), {searchSku: selectedSku},
							function (res) {
								if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
									var skuGroupList = res.data.skuGroupList;
									for (var i = 0; i < skuGroupList.length; i++) {
										var grnId = skuGroupList[i].grnId;
										var rvId = skuGroupList[i].rvId;
										var batchNumber = skuGroupList[i].batchNumber;
										var mfgDate = skuGroupList[i].mfgDate;
										var expiryDate = skuGroupList[i].expiryDate;
										var checkInDate = skuGroupList[i].checkInDate;
										var costPrice = skuGroupList[i].costPrice;
										var mrp = skuGroupList[i].mrp;
										var inStock = skuGroupList[i].inStockQty;
										var checkedInQty = skuGroupList[i].checkedInQty;
										var rowValue = $('<tr></tr>');
										if(grnId != null) {
											rowValue.append($('<td><a target="_blank" href="${pageContext.request.contextPath}/admin/inventory/GRN.action?grn='+grnId+'&view=">' + grnId + '</a></td>'));
										}  else{
											rowValue.append($('<td></td>'));
										}
										rowValue.append($('<td>' + batchNumber + '</td>'));
										rowValue.append($('<td>' + mfgDate + '</td>'));
										rowValue.append($('<td>' + expiryDate + '</td>'));
										rowValue.append($('<td>' + costPrice + '</td>'));
										rowValue.append($('<td>' + mrp + '</td>'));
										rowValue.append($('<td>' + checkInDate + '</td>'));
										rowValue.append($('<td>' + checkedInQty + '</td>'));
										rowValue.append($('<td>' + inStock + '</td>'));

										$('#skuGroupTable').append(rowValue);
									}

								}
							}
					);

				});

				$("#brandSelect").autocomplete({
					url: "${pageContext.request.contextPath}/core/autocomplete/AutoComplete.action?populateBrand=&q="
				});
			});
		</script>
	</s:layout-component>

	<s:layout-component name="content">
		<div style="display: none;">
			<s:link beanclass="com.hk.web.action.admin.pos.PosProductSearchAction" id="getDetailsLink"
			        event="showBatches"></s:link>
		</div>
		<fieldset class="right_label">
			<legend>Search Product</legend>
			<s:form beanclass="com.hk.web.action.admin.pos.PosProductSearchAction">
				<label>Category Name:</label>
				<s:select name="primaryCategory">
					<option value="">All categories</option>
					<hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="categoriesForPOS"
					                           value="name" label="displayName"/>
				</s:select>
				<label>Variant Id:</label><s:text name="productVariantId"/>
				<label>Brand:</label><s:text name="brand" id="brandSelect" autocomplete="off"/>
				<label>Product Name:</label><s:text name="productName"/>
				<br>
				<label>Flavor:</label><s:text name="flavor"/>
				<label>Size:</label><s:text name="size"/>
				<label>Color:</label><s:text name="color"/>
				<label>Form:</label><s:text name="form"/>
				<s:submit name="search" value="Search"/>
				<s:submit name="downloadBatches" value="Download"/>
			</s:form>
		</fieldset>

		<table class="zebra_vert" style="width: 50%; float: left;">
			<thead>
			<tr>
				<th>Variant Id</th>
				<th>Product Name</th>
				<th>Flavor</th>
				<th>Size</th>
				<th>Color</th>
				<th>Form</th>
				<th>Available<br> Inventory</th>
				<th>Actions</th>
			</tr>
			</thead>
			<c:forEach items="${ps.posProductSearchDtoList}" var="product" varStatus="ctr">
				<tr>
					<td>${product.productVariantId}</td>
					<td>${product.productName}</td>
					<td>${product.flavor}</td>
					<td>${product.size}</td>
					<td>${product.color}</td>
					<td>${product.form}</td>
					<td>${product.countId}</td>
					<td class="searchSkuBatchesTD"><a href="javascript:void(0)" class="searchSkuBatches"> Get
						Details</a></td>
					<td class="selectedSku" style="display: none;"><input type="hidden" class="selectedSkuHid"
					                                                      value="${product.sku}"></td>
				</tr>
			</c:forEach>
		</table>

		<table class="zebra_vert" id="skuGroupTable" style="width: 45%; float: right;">
			<thead>
			<tr>
				<th>GRN/RV No.</th>
				<th>Batch No.</th>
				<th>Mfg. <br>Date</th>
				<th>Expiry<br> Date</th>
				<th>Cost Price</th>
				<th>MRP</th>
				<th>Checkin<br> Date</th>
				<th>Checked-In<br> Units</th>
				<th>In-stock<br> Units</th>
			</tr>
			</thead>
			<tbody></tbody>
		</table>

	</s:layout-component>
</s:layout-render>