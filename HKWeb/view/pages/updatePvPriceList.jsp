<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.catalog.product.UpdatePvPriceAction" var="uppBean"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Update Price of Variants List">
	<s:layout-component name="htmlHead">

		<style type="text/css">
			table tr th {
				text-align: left;
			}
		</style>
	</s:layout-component>
	<s:layout-component name="heading">Update Price of Variants List</s:layout-component>
	<s:layout-component name="content">
		<s:form beanclass="com.hk.web.action.admin.catalog.product.UpdatePvPriceAction" autocomplete="off">
			<fieldset>
				<legend>Search List</legend>

				<label>Category Name:</label>
				<s:select name="primaryCategory">
					<option value="">All categories</option>
					<hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="topLevelCategoryList"
					                           value="name" label="displayName"/>
				</s:select>
				                             &nbsp; &nbsp;
				<label>Updated:</label><s:checkbox name="updated" style="width:150px"/>
				                             &nbsp; &nbsp;
				<s:submit name="pre" value="Search"/>
			</fieldset>
		</s:form>

		<s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${uppBean}"/>
		<s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${uppBean}"/>

		<div class="clear"></div>
		<s:form beanclass="com.hk.web.action.admin.catalog.product.UpdatePvPriceAction">

			<table border="1">
				<tr>
					<th>S.No.</th>
					<th>Category</th>
					<th>Variant/Product</th>
					<th>Old MRP</th>
					<th>New MRP</th>
					<th>Txn Date</th>
					<th>Updated?</th>
					<th>Update Date</th>
					<th>Update By</th>
				</tr>
				<c:forEach var="pvToBeUpdated" items="${uppBean.updatePvPriceList}" varStatus="ctr">
					<c:set var="variant" value="${pvToBeUpdated.productVariant}"/>
					<c:set var="product" value="${variant.product}"/>
					<tr>
						<td>${ctr.index+1}.</td>
						<td valign="top">
								${product.primaryCategory}
						</td>
						<td valign="top">
								${product.id} - ${product.name}
							<br/>
								${variant.id} - ${variant.optionsCommaSeparated}
						</td>
						<td>${variant.markedPrice}</td>
						<td>${pvToBeUpdated.newMrp}</td>
						<td><fmt:formatDate value="${pvToBeUpdated.txnDate}" pattern="dd/MM/yyyy HH:mm"/></td>
						<td>
								${pvToBeUpdated.updated}
							<c:if test="${! pvToBeUpdated.updated}">
								<s:link beanclass="com.hk.web.action.admin.catalog.product.UpdatePvPriceAction"
								        event="update" style="background-color:green;color:white;padding:2px;">
									Update
									<s:param name="updatePvPrice" value="${pvToBeUpdated.id}"/>
								</s:link>
							</c:if>
						</td>
						<td><fmt:formatDate value="${pvToBeUpdated.updateDate}" pattern="dd/MM/yyyy HH:mm"/></td>
						<td>${pvToBeUpdated.updatedBy.login}</td>
					</tr>
				</c:forEach>
			</table>

		</s:form>
	</s:layout-component>
</s:layout-render>