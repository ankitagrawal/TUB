<%@ page import="app.bootstrap.guice.InjectorFactory" %>
<%@ page import="mhc.service.dao.WarehouseDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="mhc.web.action.admin.StockTransferAction" var="sta"/>
<s:useActionBean beanclass="com.hk.web.action.admin.warehouse.SelectWHAction" var="whAction" event="getUserWarehouse"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Stock Transfer List">
	<%
		WarehouseDao warehouseDao = ServiceLocatorFactory.getService(WarehouseDao.class);
		pageContext.setAttribute("whList", warehouseDao.listAll());
	%>
	<s:layout-component name="htmlHead">
		<link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
		<jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
	</s:layout-component>
	<s:layout-component name="content">
		<c:if test="${whAction.setWarehouse != null}">
			<s:link beanclass="mhc.web.action.admin.StockTransferAction"
			        event="view">Create New Stock Transfer</s:link>
		</c:if>
		<s:form beanclass="mhc.web.action.admin.StockTransferAction">

			<fieldset>
				<legend>Search Stock Transfer List</legend>
				<s:errors/>
				<br/>
				<label>Create Date:</label><s:text class="date_input" formatPattern="yyyy-MM-dd" name="createDate"/>
				<label>User Login</label><s:text style="width:150px" name="userLogin"/>
				<label>From Warehouse: </label>
						<s:select name="fromWarehouse">
							<s:option value="0">-All-</s:option>
							<c:forEach items="${whList}" var="wh">
								<s:option value="${wh.id}">${wh.city}</s:option>
							</c:forEach>
						</s:select>
					<label>   To Warehouse: </label>
						<s:select name="toWarehouse">
							<s:option value="0">-All-</s:option>
							<c:forEach items="${whList}" var="wh">
								<s:option value="${wh.id}">${wh.city}</s:option>
							</c:forEach>
						</s:select>
				<s:submit name="pre" value="Search"/>
			</fieldset>
		</s:form>
		<s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${sta}"/>
		<s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${sta}"/>
		<table class="zebra_vert">
			<thead>
			<tr>
				<th>ID</th>
				<th>Create Date</th>
				<th>Created By</th>
				<th>Received By</th>
				<th>Checkout Date</th>
				<th>Checkin Date</th>
				<th>Actions</th>
			</tr>
			</thead>
			<c:forEach items="${sta.stockTransferList}" var="stockTransfer" varStatus="ctr">
				<tr>
					<td>${stockTransfer.id}</td>
					<td><fmt:formatDate value="${stockTransfer.createDate}" type="both" timeStyle="short"/></td>
					<td>${stockTransfer.createdBy.name} <br/>(${stockTransfer.createdBy.login})</td>
					<td><c:if test="${stockTransfer.receivedBy.name != null}">
							${stockTransfer.receivedBy.name} <br/>(${stockTransfer.receivedBy.login})</c:if></td>
					<td><fmt:formatDate value="${stockTransfer.checkoutDate}" type="both" timeStyle="short"/></td>
					<td><fmt:formatDate value="${stockTransfer.checkinDate}" type="both" timeStyle="short"/></td>
					<td>
						<c:if test="${stockTransfer.fromWarehouse.id == whAction.setWarehouse.id}">
						<s:link beanclass="mhc.web.action.admin.StockTransferAction" event="view">Edit
							<s:param name="stockTransfer" value="${stockTransfer.id}"/></s:link>
						</c:if>&nbsp;
						<c:if test="${stockTransfer.toWarehouse.id == whAction.setWarehouse.id}">
						<s:link beanclass="mhc.web.action.admin.StockTransferAction" event="checkinInventoryAgainstStockTransfer">Inventory Checkin
							<s:param name="stockTransfer" value="${stockTransfer.id}"/></s:link>
						</c:if>
					</td>
				</tr>
			</c:forEach>
		</table>
		<s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${sta}"/>
		<s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${sta}"/>
	</s:layout-component>
</s:layout-render>