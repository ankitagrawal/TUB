<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Product Search">
	<s:useActionBean beanclass="com.hk.web.action.admin.pos.PosProductSearchAction" var="ps"/>
	<s:layout-component name="htmlHead">
		<script type="text/javascript">

			$(document).ready(function () {
				$("#brandSelect").autocomplete({
					url: "${pageContext.request.contextPath}/core/autocomplete/AutoComplete.action?populateBrand=&q="
				});
			});
		</script>
	</s:layout-component>

	<s:layout-component name="content">
		<fieldset class="right_label">
			<legend>Search Product</legend>
			<s:form beanclass="com.hk.web.action.admin.pos.PosProductSearchAction">
				<label>Category Name:</label>
				<s:select name="primaryCategory">
					<option value="">All categories</option>
					<hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="topLevelCategoryList"
					                           value="name" label="displayName"/>
				</s:select>
				<label>Brand:</label><s:text name="brand" id="brandSelect" autocomplete="off"/>
				<label>Product Name:</label><s:text name="productName"/>
				<br>
				<label>Flavor:</label><s:text name="flavor"/>
				<label>Size:</label><s:text name="size"/>
				<label>Color:</label><s:text name="color"/>
				<label>Form:</label><s:text name="form"/>
				<s:submit name="search" value="Search"/>
			</s:form>
		</fieldset>

		<%--<div style="display:inline;float:left;">--%>
		<table class="zebra_vert">
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
					<td><s:link beanclass="com.hk.web.action.admin.pos.PosProductSearchAction" event="showBatches"
					            target="_blank">
						Get Details<s:param name="searchSku" value="${product.sku}"/>
					</s:link></td>
				</tr>
			</c:forEach>
		</table>

		<%--</div>--%>
		<%--<div style="display:inline;float:right;">--%>

		<%--</div>--%>

	</s:layout-component>
</s:layout-render>