<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.constants.courier.EnumDispatchLotStatus" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Dispatch Lot List">
	<s:useActionBean beanclass="com.hk.web.action.admin.courier.DispatchLotAction" var="da"/>
	<c:set var="inTransitDispatchLot" value="<%=EnumDispatchLotStatus.InTransit.getId()%>"/>
	<c:set var="generatedDispatchLot" value="<%=EnumDispatchLotStatus.Generated.getId()%>"/>
	<c:set var="receivedDispatchLot" value="<%=EnumDispatchLotStatus.Received.getId()%>"/>
	<c:set var="partialReceivedDispatchLot" value="<%=EnumDispatchLotStatus.PartiallyReceived.getId()%>"/>

	<s:layout-component name="htmlHead">
		<link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>

		<jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
		<script type="text/javascript">
			$(document).ready(function () {
				$('.cancelDispatchId').click(function () {
					if (!confirm("Are you sure, you want to cancel the Dispatch Lot")) {
						return false;
					}

				});

			});
		</script>
	</s:layout-component>
	<s:layout-component name="heading">
		Dispatch Lot List
	</s:layout-component>

	<s:layout-component name="content">
		<fieldset class="right_label">
			<legend>Search Dispatch Lot</legend>
			<s:form beanclass="com.hk.web.action.admin.courier.DispatchLotAction">
				<label>Dispatch Lot Id:</label><s:text name="dispatchLot"/>
				<label>Docket Number:</label><s:text name="docketNumber"/>
				<label>Zone</label>
				<s:select name="zone">
					<s:option value="null">-All-</s:option>
					<hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="allZones"
					                           value="id"
					                           label="name"/>
				</s:select>

				<label>Source:</label>
				<s:select name="source" class="text">
					<s:option value="">-Select-</s:option>
					<hk:master-data-collection service="<%=MasterDataDao.class%>"
					                           serviceProperty="sourceAndDestinationListForDispatchLot"
							/>
				</s:select>
				<label>Destination:</label>
				<s:select name="destination" class="text">
					<s:option value="">-Select-</s:option>
					<hk:master-data-collection service="<%=MasterDataDao.class%>"
					                           serviceProperty="sourceAndDestinationListForDispatchLot"
							/>
				</s:select>
				<label>Courier:</label>
				<s:select name="courier">
					<s:option value="">-All-</s:option>
					<hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="couriersForDispatchLot"
					                           value="id"
					                           label="name"/>
				</s:select>
				<div class="clear"></div>
				<label>Dispatch Status:</label>
				<s:select name="dispatchLotStatus">
					<s:option value="">-All-</s:option>
					<hk:master-data-collection service="<%=MasterDataDao.class%>"
					                           serviceProperty="dispatchLotStatusList"
					                           value="id"
					                           label="name"/>
				</s:select>
				<label>Dispatch Start Date:</label><s:text style="width:150px" class="date_input startDate"
				                                           formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
				                                           name="dispatchStartDate"/>
				<label>End Date:</label><s:text style="width:150px" class="date_input endDate"
				                                formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
				                                name="dispatchEndDate"/>
				<label>Awb no:</label><s:text name="awbNumber" />
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
				<th>Created By</th>
				<th>Docket Number</th>
				<th>Courier</th>
				<th>Zone</th>
				<th>Source</th>
				<th>Destination</th>
				<th>Dispatch Lot Status</th>
				<th>Dispatch Date</th>
				<th>Receiving Date</th>
				<th>Received By</th>
				<th>Actions</th>
			</tr>
			</thead>
			<c:forEach items="${da.dispatchLotList}" var="dispatchLot" varStatus="ctr">
				<tr>
					<td>${dispatchLot.id}</td>
					<td>${dispatchLot.createDate}</td>
					<td>${dispatchLot.createdBy.name}</td>
					<td>${dispatchLot.docketNumber}</td>
					<td>${dispatchLot.courier.name}</td>
					<td>${dispatchLot.zone.name}</td>
					<td>${dispatchLot.source}</td>
					<td>${dispatchLot.destination}</td>
					<td>${dispatchLot.dispatchLotStatus.name}</td>
					<td>${dispatchLot.dispatchDate}</td>
					<td>${dispatchLot.receivingDate}</td>
					<td>${dispatchLot.receivedBy.name}</td>
					<td>
						<s:link beanclass="com.hk.web.action.admin.courier.DispatchLotAction"
						        event="viewLot">View Shipments
							<s:param name="dispatchLot" value="${dispatchLot.id}"/>
						</s:link>
						&nbsp;&nbsp;&nbsp;
						<s:link beanclass="com.hk.web.action.admin.courier.DispatchLotAction">Edit/View
							<s:param name="dispatchLot" value="${dispatchLot.id}"/>
						</s:link>
						<c:if test="${dispatchLot.dispatchLotStatus.id == inTransitDispatchLot || dispatchLot.dispatchLotStatus.id == generatedDispatchLot}">
							<s:link beanclass="com.hk.web.action.admin.courier.DispatchLotAction"
							        event="cancelDispatchLot"
							        class="cancelDispatchId">Cancel Dispatch Lot
								<s:param name="dispatchLot" value="${dispatchLot.id}"/>
							</s:link>
						</c:if>
						<c:if test="${dispatchLot.dispatchLotStatus.id == inTransitDispatchLot || dispatchLot.dispatchLotStatus.id == partialReceivedDispatchLot}">
							<s:link beanclass="com.hk.web.action.admin.courier.DispatchLotAction" event="receiveLot">Receive
								<s:param name="dispatchLot" value="${dispatchLot.id}"/>
							</s:link>
							&nbsp;&nbsp;
						</c:if>
						<c:if test="${dispatchLot.documentFileName != null}">
							<s:link beanclass="com.hk.web.action.admin.courier.DispatchLotAction" event="downloadDocument">Download Document
								<s:param name="dispatchLot" value="${dispatchLot.id}"/>
							</s:link>
							&nbsp;&nbsp;
						</c:if>
					</td>
				</tr>
			</c:forEach>
		</table>
		<s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${da}"/>
		<s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${da}"/>

	</s:layout-component>
</s:layout-render>


