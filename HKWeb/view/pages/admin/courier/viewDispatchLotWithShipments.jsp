<%@include file="/includes/_taglibInclude.jsp" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.constants.courier.EnumDispatchLotStatus" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="View Dispatch Lot">
	<s:useActionBean beanclass="com.hk.web.action.admin.courier.DispatchLotAction" var="dispatchLotBean"/>
	<c:set var="dispatchLotInTransit" value="<%=EnumDispatchLotStatus.InTransit.getId()%>"/>
	<c:set var="dispatchLotPartiallyReceived" value="<%=EnumDispatchLotStatus.PartiallyReceived.getId()%>"/>
	<s:layout-component name="htmlHead">
		<link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>

		<jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>

		<style type="text/css">
			.text {
				margin-left: 100px;
				margin-top: 20px;
			}

			.label {
				margin-top: 20px;
				float: left;
				margin-left: 10px;
				width: 150px;
			}

		</style>
		<script type="text/javascript">
			$(document).ready(function() {
				$('#markDispatchLotReceived').click(function() {
					if(!confirm("Some shipments are not yet received, do you still want to Mark Lot As Received?")) {
						return false;
					}
				});
			})
		</script>
	</s:layout-component>
	<s:layout-component name="heading">
		<h4>View Dispatch Lot: ${dispatchLotBean.dispatchLot.id}</h4>
	</s:layout-component>

	<s:layout-component name="content">
		<fieldset class="top_label">
			<legend>Details</legend>
			<table>
				<tr>
					<td><label><strong>Docket Number</strong></label></td>
					<td>${dispatchLotBean.dispatchLot.docketNumber}</td>

					<td><label><strong>Create Date</strong></label></td>
					<td><fmt:formatDate value="${dispatchLotBean.dispatchLot.createDate}" pattern="dd-MM-yyyy"/></td>

					<td><label><strong>Created By</strong></label></td>
					<td>${dispatchLotBean.dispatchLot.createdBy.name}</td>

					<td><label><strong>Dispatch Date</strong></label></td>
					<td><fmt:formatDate value="${dispatchLotBean.dispatchLot.dispatchDate}" pattern="dd-MM-yyyy"/></td>

					<td><label><strong>Receiving Date</strong></label></td>
					<td><fmt:formatDate value="${dispatchLotBean.dispatchLot.receivingDate}" pattern="dd-MM-yyyy"/></td>

					<td><label><strong>Received By</strong></label></td>
					<td>${dispatchLotBean.dispatchLot.receivedBy.name}</td>

					<td><label><strong>No. Of Shipments Sent</strong></label></td>
					<td>
						<c:choose>
							<c:when test="${dispatchLotBean.dispatchLot.noOfShipmentsSent == null}">
								0
							</c:when>
							<c:otherwise>
								${dispatchLotBean.dispatchLot.noOfShipmentsSent}
							</c:otherwise>
						</c:choose>
					</td>
				</tr>

				<tr>
					<td><label><strong>Courier</strong></label></td>
					<td>${dispatchLotBean.dispatchLot.courier.name}</td>

					<td><label><strong>Receiving Zone</strong></label></td>
					<td>${dispatchLotBean.dispatchLot.zone.name}</td>

					<td><label><strong>Source</strong></label></td>
					<td>${dispatchLotBean.dispatchLot.source}</td>

					<td><label><strong>Destination</strong></label></td>
					<td>${dispatchLotBean.dispatchLot.destination}</td>

					<td><label><strong>No. of mother bags</strong></label></td>
					<td>
						<c:choose>
							<c:when test="${dispatchLotBean.dispatchLot.noOfMotherBags == null}">
								0
							</c:when>
							<c:otherwise>
								${dispatchLotBean.dispatchLot.noOfMotherBags}
							</c:otherwise>
						</c:choose>
					</td>
					<td><label><strong>No. Of Shipments Received</strong></label></td>
					<td>
						<c:choose>
							<c:when test="${dispatchLotBean.dispatchLot.noOfShipmentsReceived == null}">
								0
							</c:when>
							<c:otherwise>
								${dispatchLotBean.dispatchLot.noOfShipmentsReceived}
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</table>
			<table>
				<tr>
					<td><label><strong>Remarks</strong></label></td>
					<td>${dispatchLotBean.dispatchLot.remarks}</td>
				</tr>
				<c:if test="${dispatchLotBean.dispatchLot.dispatchLotStatus.id == dispatchLotInTransit
				|| dispatchLotBean.dispatchLot.dispatchLotStatus.id == dispatchLotPartiallyReceived}">
					<tr>
						<td colspan="2">
							<s:form beanclass="com.hk.web.action.admin.courier.DispatchLotAction">
								<s:hidden name="dispatchLot" value="${dispatchLotBean.dispatchLot.id}"/>
								<s:submit name="markDispatchLotReceived" value="Mark Lot As Received" id="markDispatchLotReceived"/>
							</s:form>
						</td>
					</tr>
				</c:if>
			</table>
		</fieldset>

		<fieldset class="top_label">
			<legend>Filters</legend>
			<s:form beanclass="com.hk.web.action.admin.courier.DispatchLotAction">
				<label><strong>Filter shipments</strong></label>
				<s:select name="shipmentStatusFilter">
					<s:option value="">-All-</s:option>
					<hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="shipmentStatusForDispatchLot"
					                           />
				</s:select>
				<s:hidden name="dispatchLot" value="${dispatchLotBean.dispatchLot.id}"/>
				<s:submit name="viewLot" value="Filter" />
				<s:submit name="downloadLotDetailsToExcel" value="Download To Excel" />
			</s:form>
		</fieldset>

		<h4>Shipment Details for Dispatch Lot: ${dispatchLotBean.dispatchLot.id}</h4>

		<table class="zebra_vert">
			<thead>
			<tr>
				<th>Gateway Order Id</th>
				<th>AWB Number</th>
				<th>Shipment Status</th>
			</tr>
			</thead>
			<c:forEach items="${dispatchLotBean.dispatchLotShipments}" var="dispatchLotShipment">
				<tr>
					<td>${dispatchLotShipment.shipment.shippingOrder.gatewayOrderId}</td>
					<td>${dispatchLotShipment.shipment.awb.awbNumber}</td>
					<td>${dispatchLotShipment.shipmentStatus}</td>
				</tr>
			</c:forEach>
			<tbody>
		</table>

	</s:layout-component>
</s:layout-render>

