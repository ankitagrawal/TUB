<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.constants.inventory.EnumAuditStatus" %>
<%@ page import="com.hk.pact.dao.warehouse.WarehouseDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.constants.inventory.EnumCycleCountStatus" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Cycle Count  List">
	<s:useActionBean beanclass="com.hk.web.action.admin.inventory.CycleCountAction" var="cc"/>
	<%
		WarehouseDao warehouseDao = ServiceLocatorFactory.getService(WarehouseDao.class);
		pageContext.setAttribute("whList", warehouseDao.getAllWarehouses());
	%>
	<s:layout-component name="heading">
		Cycle Count  List
	</s:layout-component>

	<s:layout-component name="content">
		<s:link beanclass="com.hk.web.action.admin.inventory.BrandsToAuditAction" event="view">
			Create New Cycle Count
		</s:link>
		<fieldset class="right_label">
			<legend>Search Cycle Count List</legend>
			<s:form beanclass="com.hk.web.action.admin.inventory.CycleCountAction">
				<label>Brand:</label><s:text name="brand"/>
				<label>Auditor Login Email:</label><s:text name="auditorLogin"/>
				<label>WH:</label><s:select name="warehouse">
				<s:option value="">-ALL-</s:option>
				<c:forEach items="${whList}" var="wh">
					<s:option value="${wh.id}">${wh.name}</s:option>
				</c:forEach>
			</s:select>
				<s:submit name="pre" value="Search"/>
			</s:form>
		</fieldset>

		<s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${cc}"/>
		<s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${cc}"/>

		<table class="zebra_vert">
			<thead>
			<tr>
				<th>Count Id</th>
				<th>CC Start Date</th>
				<th>WH</th>
				<th>Auditor</th>
				<th>Brand</th>
				<th>Status</th>
				<th>Actions</th>
			</tr>
			</thead>
			<c:forEach items="${cc.cycleCountList}" var="cycleCountV" varStatus="ctr">
				<tr>
					<td>${cycleCountV.id}</td>
					<td><fmt:formatDate value="${cycleCountV.createDate}" pattern="dd/MM/yyyy"/></td>
					<td>${cycleCountV.brandsToAudit.warehouse.city}</td>
					<td>${cycleCountV.user.login}</td>
					<td>${cycleCountV.brandsToAudit.brand}</td>
					<td>
						<c:forEach items="<%=EnumCycleCountStatus.getAllList()%>" var="status">
							<c:if test="${cycleCountV.cycleStatus == status.id}">
								${status.name}
							</c:if>
						</c:forEach>
					</td>
					<td>

						<c:set value="<%=EnumCycleCountStatus.InProgress.getId()%>" var="inProgress"/>
						<c:choose>
							<c:when test="${cycleCountV.cycleStatus == inProgress}">
								<s:link beanclass="com.hk.web.action.admin.inventory.CycleCountAction"
								        event="directToCycleCount">
									<s:param name="cycleCount" value="${cycleCountV.id}"/>
									<span style="color:brown;">Edit Cycle Count</span>
								</s:link>
							</c:when>
							<c:otherwise>
								<s:link beanclass="com.hk.web.action.admin.inventory.CycleCountAction"
								        event="save">
									<s:param name="cycleCount" value="${cycleCountV}"/>
									<span style="color:brown;">View Cycle Count</span>
								</s:link>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</c:forEach>
		</table>
		<s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${cc}"/>
		<s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${cc}"/>
	</s:layout-component>
</s:layout-render>