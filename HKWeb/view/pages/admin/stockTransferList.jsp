<%@ page import="com.hk.pact.dao.warehouse.WarehouseDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.constants.inventory.EnumStockTransferStatus" %>
<%@ page import="com.hk.pact.service.core.WarehouseService" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.inventory.StockTransferAction" var="sta"/>
<s:useActionBean beanclass="com.hk.web.action.admin.warehouse.SelectWHAction" var="whAction" event="getUserWarehouse"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Stock Transfer List">
	<c:set var="stStatusClosed" value="<%=EnumStockTransferStatus.Closed.getId()%>" />
	<c:set var="stStatusCheckoutCompleted" value="<%=EnumStockTransferStatus.Stock_Transfer_Out_Completed.getId()%>" />
	<c:set var="stStatusCheckinInProcess" value="<%=EnumStockTransferStatus.Stock_Transfer_CheckIn_In_Process.getId()%>" />
	<%
		WarehouseService warehouseService = ServiceLocatorFactory.getService(WarehouseService.class);
    pageContext.setAttribute("whList", warehouseService.getAllActiveWarehouses());
	%>
	<s:layout-component name="htmlHead">
		<link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
		<jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
	</s:layout-component>
	<s:layout-component name="content">
		<c:if test="${whAction.setWarehouse != null}">
			<s:link beanclass="com.hk.web.action.admin.inventory.StockTransferAction"
			        event="view">Create New Stock Transfer</s:link>
		</c:if>
		<s:form beanclass="com.hk.web.action.admin.inventory.StockTransferAction">

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
								<s:option value="${wh.id}">${wh.identifier}</s:option>
							</c:forEach>
						</s:select>
					<label>   To Warehouse: </label>
						<s:select name="toWarehouse">
							<s:option value="0">-All-</s:option>
							<c:forEach items="${whList}" var="wh">
								<s:option value="${wh.id}">${wh.identifier}</s:option>
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
				<th>Status</th>
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
					<td>${stockTransfer.stockTransferStatus.name}</td>
					<td>
						<c:if test="${stockTransfer.fromWarehouse.id == whAction.setWarehouse.id}">
						<s:link beanclass="com.hk.web.action.admin.inventory.StockTransferAction" event="view">View/Edit
							<s:param name="stockTransfer" value="${stockTransfer.id}"/></s:link>
						</c:if>&nbsp;
						<c:if test="${stockTransfer.toWarehouse.id == whAction.setWarehouse.id && (stockTransfer.stockTransferStatus.id == stStatusCheckoutCompleted
										|| stockTransfer.stockTransferStatus.id == stStatusCheckinInProcess)}">
						<s:link beanclass="com.hk.web.action.admin.inventory.StockTransferAction" event="checkinInventoryAgainstStockTransfer">Inventory Checkin
							<s:param name="stockTransfer" value="${stockTransfer.id}"/></s:link>
						</c:if>
						<c:if test="${stockTransfer.toWarehouse.id == whAction.setWarehouse.id && stockTransfer.stockTransferStatus.id == stStatusClosed}">
							<s:link beanclass="com.hk.web.action.admin.inventory.StockTransferAction" event="checkinInventoryAgainstStockTransfer">View Stock Transfer
								<s:param name="stockTransfer" value="${stockTransfer.id}"/></s:link>
						</c:if>
                        &nbsp;
                        <s:link beanclass="com.hk.web.action.admin.inventory.StockTransferAction" event="print" target="_blank">Print
                          <s:param name="stockTransfer" value="${stockTransfer.id}"/></s:link>
						<s:link beanclass="com.hk.web.action.admin.inventory.StockTransferAction" event="easySolView" target="_blank">Easy Sol View
						                          <s:param name="stockTransfer" value="${stockTransfer.id}"/></s:link>
					</td>
				</tr>
			</c:forEach>
		</table>
		<s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${sta}"/>
		<s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${sta}"/>
	</s:layout-component>
</s:layout-render>