<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Dispatch Lot List">
	<s:useActionBean beanclass="com.hk.web.action.admin.courier.DispatchLotAction" var="da"/>
	<s:layout-component name="htmlHead">
		<link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>

		<jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
	</s:layout-component>
	<s:layout-component name="heading">
		Dispatch Lot List
	</s:layout-component>

	<s:layout-component name="content">
		<fieldset class="right_label">
			<legend>Search PO</legend>
			<s:form beanclass="com.hk.web.action.admin.courier.DispatchLotAction">
				<label>Dispatch Lot Id:</label><s:text name="dispatchLot"/>
				<label>Docket Number:</label><s:text name="docketNumber"/>
				<%--<label>Zone:</label><s:text name="zone"/>--%>
				<label>Source:</label><s:text name="source"/>
				<label>Destination:</label><s:text name="destination"/>
				<label>Courier:</label>
				<s:select name="courier">
				<s:option value="">-All-</s:option>
				<hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="courierList" value="id"
				                           label="name"/>
				</s:select>
				<div class="clear"></div>
				<label>Delivery Start Date:</label><s:text style="width:150px" class="date_input startDate"
				                                  formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="deliveryStartDate"/>
				<label>End Date:</label><s:text style="width:150px" class="date_input endDate"
				                                formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="deliveryEndDate"/>

				<s:submit name="showDispatchLotList" value="Search Dispatch Lot"/>
			</s:form>
		</fieldset>

		<s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${da}"/>
		<s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${da}"/>

		<table class="zebra_vert">
			<thead>
			<tr>
				<th>ID</th>
				<th>Create Date</th>
				<th>Docket Number</th>
				<th>Courier</th>
				<%--<th>Zone</th>--%>
				<th>Source</th>
				<th>Destination</th>
				<th>Delivery Date</th>
				<th>Actions</th>
			</tr>
			</thead>
			<c:forEach items="${da.dispatchLotList}" var="dispatchLot" varStatus="ctr">
				<tr>
					<td>${dispatchLot.id}</td>
					<td>${dispatchLot.createDate}</td>
					<td>${dispatchLot.docketNumber}</td>
					<td>${dispatchLot.courier.name}</td>
					<%--<td>${dispatchLot.zone}</td>--%>
					<td>${dispatchLot.source}</td>
					<td>${dispatchLot.destination}</td>
					<td>${dispatchLot.deliveryDate}</td>
					<td>
						<s:link beanclass="com.hk.web.action.admin.courier.DispatchLotAction">Edit/View
						<s:param name="dispatchLot" value="${dispatchLot.id}"/>
						</s:link>
					</td>
				</tr>
			</c:forEach>
		</table>
		<s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${da}"/>
		<s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${da}"/>

	</s:layout-component>
</s:layout-render>


