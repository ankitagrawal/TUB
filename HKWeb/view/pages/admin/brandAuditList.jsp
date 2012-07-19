<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.constants.inventory.EnumAuditStatus" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Brand Audit List">
	<s:useActionBean beanclass="com.hk.web.action.admin.inventory.BrandsToAuditAction" var="poa"/>

	<s:layout-component name="heading">
		Brands Audit List
	</s:layout-component>

	<s:layout-component name="content">
		<s:link beanclass="com.hk.web.action.admin.inventory.BrandsToAuditAction" event="view">
			Add New Brand To Audit
		</s:link>
		<fieldset class="right_label">
			<legend>Search Audit List</legend>
			<s:form beanclass="com.hk.web.action.admin.inventory.BrandsToAuditAction">
				<label>Brand:</label><s:text name="brand"/>
				<s:submit name="pre" value="Search"/>
			</s:form>
		</fieldset>

		<s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${poa}"/>
		<s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${poa}"/>

		<table class="zebra_vert">
			<thead>
			<tr>
				<th>Audit Date</th>
				<th>Auditor</th>
				<th>Brand</th>
				<th>Status</th>
				<th>Actions</th>
			</tr>
			</thead>
			<c:forEach items="${poa.brandsToAuditList}" var="auditBrand" varStatus="ctr">
				<tr>
					<td><fmt:formatDate value="${auditBrand.auditDate}" pattern="dd/MM/yyyy"/></td>
					<td>${auditBrand.auditor.login}</td>
					<td>${auditBrand.brand}</td>
					<td><select>
						<c:forEach items="<%=EnumAuditStatus.getAllList()%>" var="status">
							<c:choose>
								<c:when test="${auditBrand.auditStatus == status.id}">
									<option value="${status.id}" selected="selected">${status.name}</option>
								</c:when>
								<c:otherwise>
									<option value="${status.id}">${status.name}</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select></td>
					<td>
						<s:link beanclass="com.hk.web.action.admin.inventory.BrandsToAuditAction" event="view">
							<s:param name="brandsToAudit" value="${auditBrand.id}"/>
							Edit
						</s:link>
					</td>
				</tr>
			</c:forEach>
		</table>
		<s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${poa}"/>
		<s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${poa}"/>
	</s:layout-component>
</s:layout-render>